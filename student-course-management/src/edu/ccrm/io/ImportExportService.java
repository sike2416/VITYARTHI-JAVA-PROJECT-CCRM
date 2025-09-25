package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.util.Validator;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ImportExportService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final Validator validator;

    public ImportExportService(StudentService studentService, CourseService courseService,
                               EnrollmentService enrollmentService, Validator validator) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.validator = validator;
    }

    public void importStudentsFromCSV(Path filePath) throws IOException {
        if (filePath == null || !Files.exists(filePath)) {
            throw new IOException("File does not exist: " + filePath);
        }

        List<String> lines = Files.readAllLines(filePath);

        // Skip header line
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            if (parts.length >= 5) {
                try {
                    String id = parts[0].trim();
                    String regNo = parts[1].trim();
                    String firstName = parts[2].trim();
                    String lastName = parts[3].trim();
                    String email = parts[4].trim();

                    Name fullName = new Name(firstName, lastName);
                    Student student = new Student(id, fullName, email, regNo);

                    if (parts.length > 5) {
                        student.setStatus(StudentStatus.valueOf(parts[5].trim().toUpperCase()));
                    }

                    studentService.addStudent(student);
                } catch (Exception e) {
                    System.err.println("Error importing student from line " + (i + 1) + ": " + e.getMessage());
                }
            }
        }

        System.out.println("Imported " + (lines.size() - 1) + " students from " + filePath);
    }

    public void exportStudentsToCSV(Path filePath) throws IOException {
        if (filePath == null) throw new IllegalArgumentException("File path cannot be null");

        List<Student> students = studentService.getAllStudents();

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            // Write header
            writer.write("ID,RegNo,FirstName,LastName,Email,Status,CreatedAt,UpdatedAt,Active\n");

            // Write data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Student student : students) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        student.getId(),
                        student.getRegNo(),
                        student.getFullName().getFirstName(),
                        student.getFullName().getLastName(),
                        student.getEmail(),
                        student.getStatus(),
                        student.getCreatedAt().format(formatter),
                        student.getUpdatedAt().format(formatter),
                        student.isActive()));
            }
        }

        System.out.println("Exported " + students.size() + " students to " + filePath);
    }

    public void importCoursesFromCSV(Path filePath) throws IOException {
        if (filePath == null || !Files.exists(filePath)) {
            throw new IOException("File does not exist: " + filePath);
        }

        List<String> lines = Files.readAllLines(filePath);

        // Skip header line
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            if (parts.length >= 6) {
                try {
                    String prefix = parts[0].trim();
                    String number = parts[1].trim();
                    String title = parts[2].trim();
                    int credits = Integer.parseInt(parts[3].trim());
                    String department = parts[4].trim();
                    Semester semester = Semester.valueOf(parts[5].trim().toUpperCase());

                    CourseCode code = new CourseCode(prefix, number);

                    Course.Builder builder = new Course.Builder(code)
                            .title(title)
                            .credits(credits)
                            .department(department)
                            .semester(semester);

                    if (parts.length > 6 && !parts[6].trim().isEmpty()) {
                        builder.active(Boolean.parseBoolean(parts[6].trim()));
                    }

                    Course course = builder.build();
                    courseService.addCourse(course);
                } catch (Exception e) {
                    System.err.println("Error importing course from line " + (i + 1) + ": " + e.getMessage());
                }
            }
        }

        System.out.println("Imported " + (lines.size() - 1) + " courses from " + filePath);
    }

    public void exportCoursesToCSV(Path filePath) throws IOException {
        if (filePath == null) throw new IllegalArgumentException("File path cannot be null");

        List<Course> courses = courseService.getAllCourses();

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            // Write header
            writer.write("Prefix,Number,Title,Credits,Department,Semester,Active,InstructorID\n");

            // Write data
            for (Course course : courses) {
                writer.write(String.format("%s,%s,%s,%d,%s,%s,%s,%s\n",
                        course.getCode().getPrefix(),
                        course.getCode().getNumber(),
                        course.getTitle(),
                        course.getCredits(),
                        course.getDepartment(),
                        course.getSemester(),
                        course.isActive(),
                        course.getInstructor() != null ? course.getInstructor().getId() : ""));
            }
        }

        System.out.println("Exported " + courses.size() + " courses to " + filePath);
    }

    public void exportEnrollmentsToCSV(Path filePath) throws IOException {
        if (filePath == null) throw new IllegalArgumentException("File path cannot be null");

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            // Write header
            writer.write("ID,StudentID,CourseCode,EnrollmentDate,Grade,Active\n");

            // Write data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Enrollment enrollment : enrollments) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s\n",
                        enrollment.getId(),
                        enrollment.getStudent().getId(),
                        enrollment.getCourse().getCode().toString(),
                        enrollment.getEnrollmentDate().format(formatter),
                        enrollment.getGrade() != null ? enrollment.getGrade() : "",
                        enrollment.isActive()));
            }
        }

        System.out.println("Exported " + enrollments.size() + " enrollments to " + filePath);
    }

    public void exportAllData(Path directory) throws IOException {
        if (directory == null) throw new IllegalArgumentException("Directory cannot be null");

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path studentsFile = directory.resolve("students.csv");
        Path coursesFile = directory.resolve("courses.csv");
        Path enrollmentsFile = directory.resolve("enrollments.csv");

        exportStudentsToCSV(studentsFile);
        exportCoursesToCSV(coursesFile);
        exportEnrollmentsToCSV(enrollmentsFile);

        System.out.println("All data exported to " + directory);
    }
}