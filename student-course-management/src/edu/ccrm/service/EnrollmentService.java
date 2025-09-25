package edu.ccrm.service;

import java.util.stream.Collectors;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Grade;
import java.util.List;
import edu.ccrm.exception.MaxCreditLimitExceededException;
import edu.ccrm.exception.DuplicateEnrollmentException;

public interface EnrollmentService {
    Enrollment enrollStudent(Student student, Course course)
            throws MaxCreditLimitExceededException, DuplicateEnrollmentException;
    boolean unenrollStudent(String enrollmentId);
    Enrollment getEnrollment(String id);
    List<Enrollment> getEnrollmentsByStudent(String studentId);
    List<Enrollment> getEnrollmentsByCourse(String courseCode);
    boolean recordGrade(String enrollmentId, Grade grade);
    List<Enrollment> getAllEnrollments();
}