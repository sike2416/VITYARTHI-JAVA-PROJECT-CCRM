package edu.ccrm.domain;

import java.util.Objects;

public class Course {
    private final CourseCode code;
    private String title;
    private int credits;
    private Instructor instructor;
    private Semester semester;
    private String department;
    private boolean active;

    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
        this.active = builder.active;
    }

    // Getters and setters
    public CourseCode getCode() { return code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
        if (instructor != null) {
            instructor.assignCourse(this);
        }
    }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Course{code=" + code + ", title='" + title +
                "', credits=" + credits + ", department='" + department +
                "', semester=" + semester + ", active=" + active +
                ", instructor=" + (instructor != null ? instructor.getFullName() : "None") + "}";
    }

    // Builder pattern
    public static class Builder {
        private final CourseCode code;
        private String title;
        private int credits;
        private Instructor instructor;
        private Semester semester;
        private String department;
        private boolean active = true;

        public Builder(CourseCode code) {
            this.code = Objects.requireNonNull(code, "Course code cannot be null");
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }

        public Builder instructor(Instructor instructor) {
            this.instructor = instructor;
            return this;
        }

        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Course build() {
            Objects.requireNonNull(title, "Course title cannot be null");
            if (credits <= 0) throw new IllegalArgumentException("Credits must be positive");
            Objects.requireNonNull(department, "Department cannot be null");
            Objects.requireNonNull(semester, "Semester cannot be null");

            return new Course(this);
        }
    }
}