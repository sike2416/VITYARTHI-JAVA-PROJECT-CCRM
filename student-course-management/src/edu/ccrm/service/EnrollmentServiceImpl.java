package edu.ccrm.service;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Grade;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnrollmentServiceImpl implements EnrollmentService {
    private final List<Enrollment> enrollments;
    private final int MAX_CREDITS_PER_SEMESTER = 21;

    public EnrollmentServiceImpl() {
        this.enrollments = new ArrayList<>();
    }

    @Override
    public Enrollment enrollStudent(Student student, Course course)
            throws MaxCreditLimitExceededException, DuplicateEnrollmentException {
        if (student == null || course == null) {
            throw new IllegalArgumentException("Student and course cannot be null");
        }

        // Check if student is already enrolled in this course
        if (enrollments.stream()
                .anyMatch(e -> e.getStudent().getId().equals(student.getId()) &&
                        e.getCourse().getCode().equals(course.getCode()) &&
                        e.isActive())) {
            throw new DuplicateEnrollmentException("Student is already enrolled in this course");
        }

        // Check credit limit
        int currentCredits = enrollments.stream()
                .filter(e -> e.getStudent().getId().equals(student.getId()) &&
                        e.isActive() &&
                        e.getCourse().getSemester() == course.getSemester())
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();

        if (currentCredits + course.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException(
                    "Enrollment would exceed maximum credit limit of " + MAX_CREDITS_PER_SEMESTER);
        }

        // Create enrollment
        String enrollmentId = UUID.randomUUID().toString();
        Enrollment enrollment = new Enrollment(enrollmentId, student, course);
        enrollments.add(enrollment);

        // Add enrollment to student
        student.addEnrollment(enrollment);

        return enrollment;
    }

    @Override
    public boolean unenrollStudent(String enrollmentId) {
        if (enrollmentId == null || enrollmentId.trim().isEmpty()) return false;

        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getId().equals(enrollmentId))
                .findFirst()
                .orElse(null);

        if (enrollment == null) return false;

        enrollment.setActive(false);
        enrollment.getStudent().removeEnrollment(enrollment);
        return true;
    }

    @Override
    public Enrollment getEnrollment(String id) {
        if (id == null || id.trim().isEmpty()) return null;

        return enrollments.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) return new ArrayList<>();

        return enrollments.stream()
                .filter(e -> e.getStudent().getId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) return new ArrayList<>();

        return enrollments.stream()
                .filter(e -> e.getCourse().getCode().toString().equals(courseCode))
                .collect(Collectors.toList());
    }

    @Override
    public boolean recordGrade(String enrollmentId, Grade grade) {
        if (enrollmentId == null || enrollmentId.trim().isEmpty() || grade == null) return false;

        Enrollment enrollment = getEnrollment(enrollmentId);
        if (enrollment == null) return false;

        enrollment.setGrade(grade);
        return true;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments); // Defensive copy
    }
}