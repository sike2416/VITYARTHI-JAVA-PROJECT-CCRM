package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String regNo;
    private List<Enrollment> enrolledCourses;
    private StudentStatus status;

    public Student(String id, Name fullName, String email, String regNo) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.enrolledCourses = new ArrayList<>();
        this.status = StudentStatus.ACTIVE;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String getProfileInfo() {
        return String.format("Student [ID: %s, Reg No: %s, Name: %s, Email: %s, Status: %s]",
                getId(), regNo, getFullName(), getEmail(), status);
    }

    // Getters and setters
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public List<Enrollment> getEnrolledCourses() { return new ArrayList<>(enrolledCourses); }
    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }

    // Business methods
    public void addEnrollment(Enrollment enrollment) {
        if (!enrolledCourses.contains(enrollment)) {
            enrolledCourses.add(enrollment);
        }
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrolledCourses.remove(enrollment);
    }

    public double calculateGPA() {
        if (enrolledCourses.isEmpty()) return 0.0;

        double totalGradePoints = 0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrolledCourses) {
            if (enrollment.getGrade() != null) {
                totalGradePoints += enrollment.getGrade().getGradePoint() * enrollment.getCourse().getCredits();
                totalCredits += enrollment.getCourse().getCredits();
            }
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }

    @Override
    public String toString() {
        return "Student{id='" + getId() + "', regNo='" + regNo +
                "', fullName=" + getFullName() + ", status=" + status +
                ", enrolledCourses=" + enrolledCourses.size() + "}";
    }

    // Static nested class
    public static class StudentComparator {
        public static int compareByName(Student s1, Student s2) {
            return s1.getFullName().toString().compareTo(s2.getFullName().toString());
        }
    }

    // Inner class
    public class Transcript {
        public void printTranscript() {
            System.out.println("TRANSCRIPT FOR: " + getFullName());
            System.out.println("REGISTRATION NO: " + regNo);
            System.out.println("GPA: " + calculateGPA());
            System.out.println("COURSES:");

            for (Enrollment enrollment : enrolledCourses) {
                if (enrollment.isActive()) {
                    System.out.printf("%-10s %-30s %-3s %-5s%n",
                            enrollment.getCourse().getCode(),
                            enrollment.getCourse().getTitle(),
                            enrollment.getCourse().getCredits(),
                            enrollment.getGrade() != null ? enrollment.getGrade() : "N/A");
                }
            }
        }
    }
}