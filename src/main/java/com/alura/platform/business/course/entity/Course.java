package com.alura.platform.business.course.entity;

import com.alura.platform.business.course.dto.CourseDto;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "COURSES")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTRUCTOR_ID")
    private User instructor;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private CourseStatusEnum status;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "INACTIVATED_AT")
    private LocalDateTime inactivatedAt;

    public Course(String name, String code, User instructor, String description, CourseStatusEnum status) {
        this.name = name;
        this.code = code;
        this.instructor = instructor;
        this.description = description;
        this.status = status;
    }

    public Course(CourseDto courseDto) {
        this.name = courseDto.name();
        this.code = courseDto.code();
        this.description = courseDto.description();
    }

    public Course() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public User getInstructor() {
        return instructor;
    }

    public String getDescription() {
        return description;
    }

    public CourseStatusEnum getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getInactivatedAt() {
        return inactivatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(CourseStatusEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(code, course.code) && Objects.equals(instructor, course.instructor) && Objects.equals(description, course.description) && status == course.status && Objects.equals(createdAt, course.createdAt) && Objects.equals(inactivatedAt, course.inactivatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, instructor, description, status, createdAt, inactivatedAt);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", instructor=" + instructor +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", inactivatedAt=" + inactivatedAt +
                '}';
    }
}
