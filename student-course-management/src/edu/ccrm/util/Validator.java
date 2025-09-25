package edu.ccrm.util;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public boolean validateStudent(Student student) {
        if (student == null) return false;

        if (student.getId() == null || student.getId().trim().isEmpty()) return false;
        if (student.getRegNo() == null || student.getRegNo().trim().isEmpty()) return false;
        if (student.getFullName() == null) return false;
        if (student.getEmail() == null || !EMAIL_PATTERN.matcher(student.getEmail()).matches()) return false;

        return true;
    }

    public boolean validateCourse(Course course) {
        if (course == null) return false;

        if (course.getCode() == null) return false;
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) return false;
        if (course.getCredits() <= 0) return false;
        if (course.getDepartment() == null || course.getDepartment().trim().isEmpty()) return false;
        if (course.getSemester() == null) return false;

        return true;
    }
}