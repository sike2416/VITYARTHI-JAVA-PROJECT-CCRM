package edu.ccrm.util;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import java.util.Comparator;

public class Comparators {
    // Student comparators using lambda expressions
    public static final Comparator<Student> STUDENT_BY_NAME =
            (s1, s2) -> s1.getFullName().toString().compareTo(s2.getFullName().toString());

    public static final Comparator<Student> STUDENT_BY_REG_NO =
            Comparator.comparing(Student::getRegNo);

    public static final Comparator<Student> STUDENT_BY_GPA =
            (s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA()); // Descending

    // Course comparators using lambda expressions
    public static final Comparator<Course> COURSE_BY_CODE =
            (c1, c2) -> c1.getCode().toString().compareTo(c2.getCode().toString());

    public static final Comparator<Course> COURSE_BY_TITLE =
            Comparator.comparing(Course::getTitle);

    public static final Comparator<Course> COURSE_BY_CREDITS =
            Comparator.comparingInt(Course::getCredits);
}