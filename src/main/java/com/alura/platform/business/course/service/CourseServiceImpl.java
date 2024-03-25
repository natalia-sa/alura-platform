package com.alura.platform.business.course.service;

import com.alura.platform.business.course.dto.CourseDto;
import com.alura.platform.business.course.dto.CourseFilterDto;
import com.alura.platform.business.course.dto.CourseFilterResponseDto;
import com.alura.platform.business.course.dto.CourseNpsReportDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.repository.CourseRepository;
import com.alura.platform.business.user.service.UserService;
import com.alura.platform.exception.ActionDeniedException;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    @Override
    public JpaRepository<Course, Long> getRepository() {
        return courseRepository;
    }

    @Transactional
    @Override
    public Course save(CourseDto courseDto) {
        User user = userService.findById(courseDto.instructorId()).orElseThrow();
        boolean isUserInstructor = checkIfUserIsInstructor(user);

        if(!isUserInstructor) {
            throw new ActionDeniedException("User is not instructor");
        }

        Course course = new Course(courseDto.name(), courseDto.code(), user, courseDto.description(), CourseStatusEnum.ACTIVE);
        return courseRepository.save(course);
    }

    private boolean checkIfUserIsInstructor(User user) {
        return user.getRole().equals(UserRoleEnum.INSTRUCTOR);
    }

    @Transactional
    @Override
    public void inactivate(String code) {
        Course course = courseRepository.findByCode(code).orElseThrow();
        course.setStatus(CourseStatusEnum.INACTIVE);
    }

    @Override
    public List<CourseFilterResponseDto> findByFilters(CourseFilterDto filter) {
        List<Long> courseIds = findIdByFilters(filter);

        return courseIds
                .stream()
                .map(courseId -> {
                    Course course = courseRepository.findById(courseId).orElseThrow();
                    User instructor = userService.findById(course.getInstructor().getId()).orElseThrow();
                    return new CourseFilterResponseDto(course, instructor);})
                .toList();
    }

    @Override
    public List<CourseNpsReportDto> findNpsReport() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(CourseNpsReportDto::new).toList();
    }

    private List<Long> findIdByFilters(CourseFilterDto filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Course> root = criteriaQuery.from(Course.class);

        if (filter.status() != null) {
            Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), filter.status());
            criteriaQuery.where(statusPredicate);
        }

        criteriaQuery.select(root.get("id"));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(filter.pagination().getOffset())
                .setMaxResults(filter.pagination().size())
                .getResultList();
    }

    public long countByFilters(CourseFilterDto filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Course> root = countQuery.from(Course.class);

        countQuery.select(criteriaBuilder.count(root));
        if (filter.status() != null) {
            countQuery.where(criteriaBuilder.equal(root.get("status"), filter.status()));
        }

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
