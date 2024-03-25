package com.alura.platform.business.course.repository;

import com.alura.platform.business.basic.PaginationDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseFilterRepository {

    @Autowired
    private EntityManager entityManager;

    public List<Long> findIdByFilters(CourseStatusEnum status, Boolean withNps, PaginationDto pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Course> root = criteriaQuery.from(Course.class);

        List<Predicate> predicates = new ArrayList<>();
        addPredicatesByFilters(status, withNps, criteriaBuilder, root, predicates);

        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        criteriaQuery.where(finalPredicate);

        criteriaQuery.select(root.get("id"));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pagination.getOffset())
                .setMaxResults(pagination.size())
                .getResultList();
    }

    public long countByFilters(CourseStatusEnum status, Boolean withNps) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Course> root = criteriaQuery.from(Course.class);

        List<Predicate> predicates = new ArrayList<>();
        addPredicatesByFilters(status, withNps, criteriaBuilder, root, predicates);

        criteriaQuery.select(criteriaBuilder.count(root));
        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(finalPredicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private void addPredicatesByFilters(CourseStatusEnum status, Boolean withNps, CriteriaBuilder criteriaBuilder,
                                        Root<Course> root, List<Predicate> predicates) {
        addStatusPredicate(status, criteriaBuilder, root, predicates);
        addWithNpsPredicate(withNps, criteriaBuilder, root, predicates);
    }

    private void addWithNpsPredicate(Boolean withNps, CriteriaBuilder criteriaBuilder, Root<Course> root,
                                    List<Predicate> predicates) {
        if (Boolean.TRUE.equals(withNps)) {
            Predicate npsNotNullPredicate = criteriaBuilder.isNotNull(root.get("nps"));
            predicates.add(npsNotNullPredicate);
        }
    }

    private void addStatusPredicate(CourseStatusEnum status, CriteriaBuilder criteriaBuilder, Root<Course> root,
                                    List<Predicate> predicates) {
        if (status != null) {
            Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), status);
            predicates.add(statusPredicate);
        }
    }
}
