package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.util.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentServiceImpl implements StudentService {
    private final List<Student> students;
    private final Validator validator;

    public StudentServiceImpl(Validator validator) {
        this.students = new ArrayList<>();
        this.validator = validator;
    }

    @Override
    public Student addStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        if (!validator.validateStudent(student)) throw new IllegalArgumentException("Invalid student data");

        // Check if student with same ID already exists
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " already exists");
        }

        // Check if student with same registration number already exists
        if (students.stream().anyMatch(s -> s.getRegNo().equals(student.getRegNo()))) {
            throw new IllegalArgumentException("Student with registration number " + student.getRegNo() + " already exists");
        }

        students.add(student);
        return student;
    }

    @Override
    public Student getStudent(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // Defensive copy
    }

    @Override
    public Student updateStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        if (!validator.validateStudent(student)) throw new IllegalArgumentException("Invalid student data");

        int index = -1;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(student.getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " not found");
        }

        students.set(index, student);
        return student;
    }

    @Override
    public boolean deactivateStudent(String id) {
        Student student = getStudent(id);
        if (student == null) return false;

        student.setActive(false);
        return true;
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) return new ArrayList<>();

        final String searchName = name.toLowerCase();
        return students.stream()
                .filter(s -> s.getFullName().toString().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    @Override
    public void printStudentProfile(String id) {
        Student student = getStudent(id);
        if (student == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }

        System.out.println("STUDENT PROFILE");
        System.out.println("===============");
        System.out.println("ID: " + student.getId());
        System.out.println("Registration No: " + student.getRegNo());
        System.out.println("Name: " + student.getFullName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Status: " + student.getStatus());
        System.out.println("Active: " + (student.isActive() ? "Yes" : "No"));
        System.out.println("Enrolled Courses: " + student.getEnrolledCourses().size());
    }

    @Override
    public void printStudentTranscript(String id) {
        Student student = getStudent(id);
        if (student == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }

        // Using inner class to print transcript
        Student.Transcript transcript = student.new Transcript();
        transcript.printTranscript();
    }
}