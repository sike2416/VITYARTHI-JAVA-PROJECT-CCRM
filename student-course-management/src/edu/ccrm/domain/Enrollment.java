package edu.ccrm.domain;

import java.time.LocalDateTime;

public class Enrollment {
    private final String id;
    private final Student student;
    private final Course course;
    private final LocalDateTime enrollmentDate;
    private Grade grade;
    private boolean active;

    public Enrollment(String id, Student student, Course course) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDateTime.now();
        this.active = true;
    }

    // Getters and setters
    public String getId() { return id; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public Grade getGrade() { return grade; }
    public void setGrade(Grade grade) { this.grade = grade; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Enrollment{id='" + id + "', student=" + student.getFullName() +
                ", course=" + course.getCode() +
                ", grade=" + (grade != null ? grade : "Not graded") +
                ", active=" + active + "}";
    }
}