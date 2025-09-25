package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends Person {
    private String employeeId;
    private String department;
    private List<Course> assignedCourses;

    public Instructor(String id, Name fullName, String email, String employeeId, String department) {
        super(id, fullName, email);
        this.employeeId = employeeId;
        this.department = department;
        this.assignedCourses = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Instructor";
    }

    @Override
    public String getProfileInfo() {
        return String.format("Instructor [ID: %s, Employee ID: %s, Name: %s, Email: %s, Department: %s]",
                getId(), employeeId, getFullName(), getEmail(), department);
    }

    // Getters and setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public List<Course> getAssignedCourses() { return new ArrayList<>(assignedCourses); }

    // Business methods
    public void assignCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }

    public void removeCourse(Course course) {
        assignedCourses.remove(course);
    }

    @Override
    public String toString() {
        return "Instructor{id='" + getId() + "', employeeId='" + employeeId +
                "', fullName=" + getFullName() + ", department='" + department +
                "', assignedCourses=" + assignedCourses.size() + "}";
    }
}