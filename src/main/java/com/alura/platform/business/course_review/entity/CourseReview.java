package com.alura.platform.business.course_review.entity;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "COURSE_REVIEWS")
public class CourseReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_ID")
    private Course course;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "RATING")
    private Integer rating;

    @Column(name = "COMMENT")
    private String comment;

    public CourseReview(User user, Course course, Integer rating, String comment) {
        this.user = user;
        this.course = course;
        this.rating = rating;
        this.comment = comment;
    }

    public CourseReview() {

    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseReview that = (CourseReview) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(course, that.course) && Objects.equals(createdAt, that.createdAt) && Objects.equals(rating, that.rating) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, createdAt, rating, comment);
    }

    @Override
    public String toString() {
        return "CourseReview{" +
                "id=" + id +
                ", user=" + user +
                ", course=" + course +
                ", createdAt=" + createdAt +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
