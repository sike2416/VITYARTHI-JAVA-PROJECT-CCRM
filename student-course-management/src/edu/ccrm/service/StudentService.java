package edu.ccrm.service;

import edu.ccrm.domain.Student;
import java.util.List;

public interface StudentService {
    Student addStudent(Student student);
    Student getStudent(String id);
    List<Student> getAllStudents();
    Student updateStudent(Student student);
    boolean deactivateStudent(String id);
    List<Student> findStudentsByName(String name);
    void printStudentProfile(String id);
    void printStudentTranscript(String id);
}