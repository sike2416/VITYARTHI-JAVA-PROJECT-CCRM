package edu.ccrm.service;

import edu.ccrm.domain.Course;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CourseServiceImpl implements CourseService {
    private final List<Course> courses;

    public CourseServiceImpl() {
        this.courses = new ArrayList<>();
    }

    @Override
    public Course addCourse(Course course) {
        if (course == null) throw new IllegalArgumentException("Course cannot be null");

        // Check if course with same code already exists
        if (courses.stream().anyMatch(c -> c.getCode().equals(course.getCode()))) {
            throw new IllegalArgumentException("Course with code " + course.getCode() + " already exists");
        }

        courses.add(course);
        return course;
    }

    @Override
    public Course getCourse(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be null or empty");
        }

        return courses.stream()
                .filter(c -> c.getCode().toString().equals(code))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses); // Defensive copy
    }

    @Override
    public Course updateCourse(Course course) {
        if (course == null) throw new IllegalArgumentException("Course cannot be null");

        int index = -1;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCode().equals(course.getCode())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new IllegalArgumentException("Course with code " + course.getCode() + " not found");
        }

        courses.set(index, course);
        return course;
    }

    @Override
    public boolean deactivateCourse(String code) {
        Course course = getCourse(code);
        if (course == null) return false;

        course.setActive(false);
        return true;
    }

    @Override
    public List<Course> searchCourses(Predicate<Course> predicate) {
        if (predicate == null) return new ArrayList<>();

        return courses.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getCoursesByInstructor(String instructorId) {
        if (instructorId == null || instructorId.trim().isEmpty()) return new ArrayList<>();

        return courses.stream()
                .filter(c -> c.getInstructor() != null &&
                        c.getInstructor().getId().equals(instructorId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getCoursesByDepartment(String department) {
        if (department == null || department.trim().isEmpty()) return new ArrayList<>();

        return courses.stream()
                .filter(c -> c.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getCoursesBySemester(edu.ccrm.domain.Semester semester) {
        if (semester == null) return new ArrayList<>();

        return courses.stream()
                .filter(c -> c.getSemester() == semester)
                .collect(Collectors.toList());
    }
}