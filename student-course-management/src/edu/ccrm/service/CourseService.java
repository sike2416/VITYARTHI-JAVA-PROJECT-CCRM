package edu.ccrm.service;

import edu.ccrm.domain.Course;
import java.util.List;
import java.util.function.Predicate;

public interface CourseService {
    Course addCourse(Course course);
    Course getCourse(String code);
    List<Course> getAllCourses();
    Course updateCourse(Course course);
    boolean deactivateCourse(String code);
    List<Course> searchCourses(Predicate<Course> predicate);
    List<Course> getCoursesByInstructor(String instructorId);
    List<Course> getCoursesByDepartment(String department);
    List<Course> getCoursesBySemester(edu.ccrm.domain.Semester semester);
}