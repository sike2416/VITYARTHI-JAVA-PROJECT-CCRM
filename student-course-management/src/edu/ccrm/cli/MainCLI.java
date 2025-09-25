package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;
import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.*;
import edu.ccrm.util.Comparators;
import edu.ccrm.util.RecursiveUtils;
import edu.ccrm.util.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainCLI {
    private final Scanner scanner;
    private final AppConfig config;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final ImportExportService importExportService;
    private final BackupService backupService;
    private final Validator validator;

    public MainCLI() {
        this.scanner = new Scanner(System.in);
        this.config = AppConfig.getInstance();
        this.validator = new Validator();
        this.studentService = new StudentServiceImpl(validator);
        this.courseService = new CourseServiceImpl();
        this.enrollmentService = new EnrollmentServiceImpl();
        this.importExportService = new ImportExportService(studentService, courseService, enrollmentService, validator);
        this.backupService = new BackupService(importExportService, config);

        // Load configuration
        config.loadConfig();

        // Create data and backup directories if they don't exist
        try {
            Files.createDirectories(config.getDataFolderPath());
            Files.createDirectories(config.getBackupFolderPath());
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }

    public void start() {
        System.out.println("=== Student Course Registration Management System ===");

        boolean running = true;

        while (running) {
            printMainMenu();

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        manageStudents();
                        break;
                    case 2:
                        manageCourses();
                        break;
                    case 3:
                        manageEnrollments();
                        break;
                    case 4:
                        manageGrades();
                        break;
                    case 5:
                        importExportData();
                        break;
                    case 6:
                        backupAndRestore();
                        break;
                    case 7:
                        generateReports();
                        break;
                    case 8:
                        running = false;
                        System.out.println("Exiting application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollments");
        System.out.println("4. Manage Grades");
        System.out.println("5. Import/Export Data");
        System.out.println("6. Backup & Restore");
        System.out.println("7. Generate Reports");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private void manageStudents() {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== MANAGE STUDENTS ===");
            System.out.println("1. Add Student");
            System.out.println("2. List All Students");
            System.out.println("3. Find Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Deactivate Student");
            System.out.println("6. Print Student Profile");
            System.out.println("7. Print Student Transcript");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        listAllStudents();
                        break;
                    case 3:
                        findStudentById();
                        break;
                    case 4:
                        updateStudent();
                        break;
                    case 5:
                        deactivateStudent();
                        break;
                    case 6:
                        printStudentProfile();
                        break;
                    case 7:
                        printStudentTranscript();
                        break;
                    case 8:
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void addStudent() {
        System.out.println("\n=== ADD STUDENT ===");

        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter registration number: ");
        String regNo = scanner.nextLine();

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        try {
            Name fullName = new Name(firstName, lastName);
            Student student = new Student(id, fullName, email, regNo);
            studentService.addStudent(student);
            System.out.println("Student added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private void listAllStudents() {
        System.out.println("\n=== ALL STUDENTS ===");

        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        // Sort students by name using lambda comparator
        students.sort(Comparators.STUDENT_BY_NAME);

        System.out.printf("%-10s %-15s %-20s %-30s %-10s%n",
                "ID", "Reg No", "Name", "Email", "Status");
        System.out.println("--------------------------------------------------------------------------------");

        for (Student student : students) {
            System.out.printf("%-10s %-15s %-20s %-30s %-10s%n",
                    student.getId(),
                    student.getRegNo(),
                    student.getFullName(),
                    student.getEmail(),
                    student.getStatus());
        }

        System.out.println("Total students: " + students.size());
    }

    private void findStudentById() {
        System.out.println("\n=== FIND STUDENT BY ID ===");

        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();

        Student student = studentService.getStudent(id);

        if (student == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }

        System.out.println("STUDENT DETAILS");
        System.out.println("===============");
        System.out.println("ID: " + student.getId());
        System.out.println("Registration No: " + student.getRegNo());
        System.out.println("Name: " + student.getFullName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Status: " + student.getStatus());
        System.out.println("Active: " + (student.isActive() ? "Yes" : "No"));
        System.out.println("Enrolled Courses: " + student.getEnrolledCourses().size());
    }

    private void updateStudent() {
        System.out.println("\n=== UPDATE STUDENT ===");

        System.out.print("Enter student ID to update: ");
        String id = scanner.nextLine();

        Student student = studentService.getStudent(id);

        if (student == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }

        System.out.println("Current student details:");
        System.out.println("ID: " + student.getId());
        System.out.println("Registration No: " + student.getRegNo());
        System.out.println("Name: " + student.getFullName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Status: " + student.getStatus());

        System.out.println("\nEnter new details (leave blank to keep current value):");

        System.out.print("Registration No [" + student.getRegNo() + "]: ");
        String regNo = scanner.nextLine();
        if (!regNo.isEmpty()) {
            student.setRegNo(regNo);
        }

        System.out.print("First Name [" + student.getFullName().getFirstName() + "]: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name [" + student.getFullName().getLastName() + "]: ");
        String lastName = scanner.nextLine();

        if (!firstName.isEmpty() || !lastName.isEmpty()) {
            String newFirstName = firstName.isEmpty() ? student.getFullName().getFirstName() : firstName;
            String newLastName = lastName.isEmpty() ? student.getFullName().getLastName() : lastName;
            Name fullName = new Name(newFirstName, newLastName);
            // Note: We can't directly set the fullName as it's final in Person
            // This is a limitation of our current design
            System.out.println("Note: Name update not implemented due to immutability constraints");
        }

        System.out.print("Email [" + student.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            student.setEmail(email);
        }

        System.out.print("Status [" + student.getStatus() + "] (ACTIVE, INACTIVE, GRADUATED, SUSPENDED): ");
        String statusStr = scanner.nextLine();
        if (!statusStr.isEmpty()) {
            try {
                StudentStatus status = StudentStatus.valueOf(statusStr.toUpperCase());
                student.setStatus(status);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Keeping current status.");
            }
        }

        studentService.updateStudent(student);
        System.out.println("Student updated successfully!");
    }

    private void deactivateStudent() {
        System.out.println("\n=== DEACTIVATE STUDENT ===");

        System.out.print("Enter student ID to deactivate: ");
        String id = scanner.nextLine();

        boolean success = studentService.deactivateStudent(id);

        if (success) {
            System.out.println("Student deactivated successfully!");
        } else {
            System.out.println("Student not found with ID: " + id);
        }
    }

    private void printStudentProfile() {
        System.out.println("\n=== PRINT STUDENT PROFILE ===");

        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();

        studentService.printStudentProfile(id);
    }

    private void printStudentTranscript() {
        System.out.println("\n=== PRINT STUDENT TRANSCRIPT ===");

        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();

        studentService.printStudentTranscript(id);
    }

    private void manageCourses() {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== MANAGE COURSES ===");
            System.out.println("1. Add Course");
            System.out.println("2. List All Courses");
            System.out.println("3. Find Course by Code");
            System.out.println("4. Update Course");
            System.out.println("5. Deactivate Course");
            System.out.println("6. Search Courses");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addCourse();
                        break;
                    case 2:
                        listAllCourses();
                        break;
                    case 3:
                        findCourseByCode();
                        break;
                    case 4:
                        updateCourse();
                        break;
                    case 5:
                        deactivateCourse();
                        break;
                    case 6:
                        searchCourses();
                        break;
                    case 7:
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void addCourse() {
        System.out.println("\n=== ADD COURSE ===");

        System.out.print("Enter course prefix (e.g., CS): ");
        String prefix = scanner.nextLine();

        System.out.print("Enter course number (e.g., 101): ");
        String number = scanner.nextLine();

        System.out.print("Enter course title: ");
        String title = scanner.nextLine();

        System.out.print("Enter course credits: ");
        int credits = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter department: ");
        String department = scanner.nextLine();

        System.out.println("Available semesters:");
        for (Semester semester : Semester.values()) {
            System.out.println(semester.ordinal() + 1 + ". " + semester);
        }

        System.out.print("Select semester: ");
        int semesterIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (semesterIndex < 0 || semesterIndex >= Semester.values().length) {
            System.out.println("Invalid semester selection.");
            return;
        }

        Semester semester = Semester.values()[semesterIndex];

        try {
            CourseCode code = new CourseCode(prefix, number);
            Course course = new Course.Builder(code)
                    .title(title)
                    .credits(credits)
                    .department(department)
                    .semester(semester)
                    .build();

            courseService.addCourse(course);
            System.out.println("Course added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    private void listAllCourses() {
        System.out.println("\n=== ALL COURSES ===");

        List<Course> courses = courseService.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        // Sort courses by code using lambda comparator
        courses.sort(Comparators.COURSE_BY_CODE);

        System.out.printf("%-10s %-30s %-8s %-15s %-10s %-10s%n",
                "Code", "Title", "Credits", "Department", "Semester", "Active");
        System.out.println("--------------------------------------------------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-10s %-30s %-8d %-15s %-10s %-10s%n",
                    course.getCode(),
                    course.getTitle(),
                    course.getCredits(),
                    course.getDepartment(),
                    course.getSemester(),
                    course.isActive() ? "Yes" : "No");
        }

        System.out.println("Total courses: " + courses.size());
    }

    private void findCourseByCode() {
        System.out.println("\n=== FIND COURSE BY CODE ===");

        System.out.print("Enter course code: ");
        String code = scanner.nextLine();

        Course course = courseService.getCourse(code);

        if (course == null) {
            System.out.println("Course not found with code: " + code);
            return;
        }

        System.out.println("COURSE DETAILS");
        System.out.println("===============");
        System.out.println("Code: " + course.getCode());
        System.out.println("Title: " + course.getTitle());
        System.out.println("Credits: " + course.getCredits());
        System.out.println("Department: " + course.getDepartment());
        System.out.println("Semester: " + course.getSemester());
        System.out.println("Active: " + (course.isActive() ? "Yes" : "No"));
    }

    private void updateCourse() {
        System.out.println("\n=== UPDATE COURSE ===");

        System.out.print("Enter course code to update: ");
        String codeStr = scanner.nextLine();

        Course course = courseService.getCourse(codeStr);

        if (course == null) {
            System.out.println("Course not found with code: " + codeStr);
            return;
        }

        System.out.println("Current course details:");
        System.out.println("Code: " + course.getCode());
        System.out.println("Title: " + course.getTitle());
        System.out.println("Credits: " + course.getCredits());
        System.out.println("Department: " + course.getDepartment());
        System.out.println("Semester: " + course.getSemester());
        System.out.println("Active: " + (course.isActive() ? "Yes" : "No"));

        System.out.println("\nEnter new details (leave blank to keep current value):");

        System.out.print("Title [" + course.getTitle() + "]: ");
        String title = scanner.nextLine();
        if (!title.isEmpty()) {
            course.setTitle(title);
        }

        System.out.print("Credits [" + course.getCredits() + "]: ");
        String creditsStr = scanner.nextLine();
        if (!creditsStr.isEmpty()) {
            try {
                int credits = Integer.parseInt(creditsStr);
                course.setCredits(credits);
            } catch (NumberFormatException e) {
                System.out.println("Invalid credits value. Keeping current value.");
            }
        }

        System.out.print("Department [" + course.getDepartment() + "]: ");
        String department = scanner.nextLine();
        if (!department.isEmpty()) {
            course.setDepartment(department);
        }

        System.out.println("Available semesters:");
        for (Semester semester : Semester.values()) {
            System.out.println(semester.ordinal() + 1 + ". " + semester);
        }

        System.out.print("Select semester [" + course.getSemester() + "]: ");
        String semesterStr = scanner.nextLine();
        if (!semesterStr.isEmpty()) {
            try {
                int semesterIndex = Integer.parseInt(semesterStr) - 1;
                if (semesterIndex >= 0 && semesterIndex < Semester.values().length) {
                    course.setSemester(Semester.values()[semesterIndex]);
                } else {
                    System.out.println("Invalid semester selection. Keeping current value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Keeping current value.");
            }
        }

        System.out.print("Active [" + (course.isActive() ? "Yes" : "No") + "] (true/false): ");
        String activeStr = scanner.nextLine();
        if (!activeStr.isEmpty()) {
            try {
                boolean active = Boolean.parseBoolean(activeStr);
                course.setActive(active);
            } catch (Exception e) {
                System.out.println("Invalid input. Keeping current value.");
            }
        }

        courseService.updateCourse(course);
        System.out.println("Course updated successfully!");
    }

    private void deactivateCourse() {
        System.out.println("\n=== DEACTIVATE COURSE ===");

        System.out.print("Enter course code to deactivate: ");
        String code = scanner.nextLine();

        boolean success = courseService.deactivateCourse(code);

        if (success) {
            System.out.println("Course deactivated successfully!");
        } else {
            System.out.println("Course not found with code: " + code);
        }
    }

    private void searchCourses() {
        boolean searching = true;

        while (searching) {
            System.out.println("\n=== SEARCH COURSES ===");
            System.out.println("1. By Department");
            System.out.println("2. By Semester");
            System.out.println("3. Back to Course Management");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        searchCoursesByDepartment();
                        break;
                    case 2:
                        searchCoursesBySemester();
                        break;
                    case 3:
                        searching = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void searchCoursesByDepartment() {
        System.out.println("\n=== SEARCH COURSES BY DEPARTMENT ===");

        System.out.print("Enter department name: ");
        String department = scanner.nextLine();

        List<Course> courses = courseService.getCoursesByDepartment(department);

        if (courses.isEmpty()) {
            System.out.println("No courses found in department: " + department);
            return;
        }

        System.out.printf("%-10s %-30s %-8s %-15s %-10s%n",
                "Code", "Title", "Credits", "Department", "Semester");
        System.out.println("----------------------------------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-10s %-30s %-8d %-15s %-10s%n",
                    course.getCode(),
                    course.getTitle(),
                    course.getCredits(),
                    course.getDepartment(),
                    course.getSemester());
        }

        System.out.println("Total courses found: " + courses.size());
    }

    private void searchCoursesBySemester() {
        System.out.println("\n=== SEARCH COURSES BY SEMESTER ===");

        System.out.println("Available semesters:");
        for (Semester semester : Semester.values()) {
            System.out.println(semester.ordinal() + 1 + ". " + semester);
        }

        System.out.print("Select semester: ");
        int semesterIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (semesterIndex < 0 || semesterIndex >= Semester.values().length) {
            System.out.println("Invalid semester selection.");
            return;
        }

        Semester semester = Semester.values()[semesterIndex];

        List<Course> courses = courseService.getCoursesBySemester(semester);

        if (courses.isEmpty()) {
            System.out.println("No courses found in semester: " + semester);
            return;
        }

        System.out.printf("%-10s %-30s %-8s %-15s %-10s%n",
                "Code", "Title", "Credits", "Department", "Semester");
        System.out.println("----------------------------------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-10s %-30s %-8d %-15s %-10s%n",
                    course.getCode(),
                    course.getTitle(),
                    course.getCredits(),
                    course.getDepartment(),
                    course.getSemester());
        }

        System.out.println("Total courses found: " + courses.size());
    }

    private void manageEnrollments() {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== MANAGE ENROLLMENTS ===");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Unenroll Student from Course");
            System.out.println("3. List All Enrollments");
            System.out.println("4. List Enrollments by Student");
            System.out.println("5. List Enrollments by Course");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        enrollStudent();
                        break;
                    case 2:
                        unenrollStudent();
                        break;
                    case 3:
                        listAllEnrollments();
                        break;
                    case 4:
                        listEnrollmentsByStudent();
                        break;
                    case 5:
                        listEnrollmentsByCourse();
                        break;
                    case 6:
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void enrollStudent() {
        System.out.println("\n=== ENROLL STUDENT IN COURSE ===");

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        Student student = studentService.getStudent(studentId);
        if (student == null) {
            System.out.println("Student not found with ID: " + studentId);
            return;
        }

        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();

        Course course = courseService.getCourse(courseCode);
        if (course == null) {
            System.out.println("Course not found with code: " + courseCode);
            return;
        }

        try {
            Enrollment enrollment = enrollmentService.enrollStudent(student, course);
            System.out.println("Student enrolled successfully!");
            System.out.println("Enrollment ID: " + enrollment.getId());
        } catch (MaxCreditLimitExceededException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        } catch (DuplicateEnrollmentException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        }
    }

    private void unenrollStudent() {
        System.out.println("\n=== UNENROLL STUDENT FROM COURSE ===");

        System.out.print("Enter enrollment ID: ");
        String enrollmentId = scanner.nextLine();

        boolean success = enrollmentService.unenrollStudent(enrollmentId);

        if (success) {
            System.out.println("Student unenrolled successfully!");
        } else {
            System.out.println("Enrollment not found with ID: " + enrollmentId);
        }
    }

    private void listAllEnrollments() {
        System.out.println("\n=== ALL ENROLLMENTS ===");

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }

        System.out.printf("%-36s %-15s %-10s %-15s %-10s%n",
                "Enrollment ID", "Student", "Course", "Grade", "Active");
        System.out.println("-------------------------------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            System.out.printf("%-36s %-15s %-10s %-15s %-10s%n",
                    enrollment.getId(),
                    enrollment.getStudent().getFullName(),
                    enrollment.getCourse().getCode(),
                    enrollment.getGrade() != null ? enrollment.getGrade() : "Not graded",
                    enrollment.isActive() ? "Yes" : "No");
        }

        System.out.println("Total enrollments: " + enrollments.size());
    }

    private void listEnrollmentsByStudent() {
        System.out.println("\n=== LIST ENROLLMENTS BY STUDENT ===");

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for student ID: " + studentId);
            return;
        }

        System.out.printf("%-36s %-10s %-15s %-10s%n",
                "Enrollment ID", "Course", "Grade", "Active");
        System.out.println("-----------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            System.out.printf("%-36s %-10s %-15s %-10s%n",
                    enrollment.getId(),
                    enrollment.getCourse().getCode(),
                    enrollment.getGrade() != null ? enrollment.getGrade() : "Not graded",
                    enrollment.isActive() ? "Yes" : "No");
        }

        System.out.println("Total enrollments: " + enrollments.size());
    }

    private void listEnrollmentsByCourse() {
        System.out.println("\n=== LIST ENROLLMENTS BY COURSE ===");

        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseCode);

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for course code: " + courseCode);
            return;
        }

        System.out.printf("%-36s %-15s %-15s %-10s%n",
                "Enrollment ID", "Student", "Grade", "Active");
        System.out.println("-----------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            System.out.printf("%-36s %-15s %-15s %-10s%n",
                    enrollment.getId(),
                    enrollment.getStudent().getFullName(),
                    enrollment.getGrade() != null ? enrollment.getGrade() : "Not graded",
                    enrollment.isActive() ? "Yes" : "No");
        }

        System.out.println("Total enrollments: " + enrollments.size());
    }

    private void manageGrades() {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== MANAGE GRADES ===");
            System.out.println("1. Record Grade");
            System.out.println("2. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        recordGrade();
                        break;
                    case 2:
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void recordGrade() {
        System.out.println("\n=== RECORD GRADE ===");

        System.out.print("Enter enrollment ID: ");
        String enrollmentId = scanner.nextLine();

        Enrollment enrollment = enrollmentService.getEnrollment(enrollmentId);

        if (enrollment == null) {
            System.out.println("Enrollment not found with ID: " + enrollmentId);
            return;
        }

        System.out.println("Current enrollment details:");
        System.out.println("Student: " + enrollment.getStudent().getFullName());
        System.out.println("Course: " + enrollment.getCourse().getCode() + " - " + enrollment.getCourse().getTitle());
        System.out.println("Current grade: " + (enrollment.getGrade() != null ? enrollment.getGrade() : "Not graded"));

        System.out.println("\nAvailable grades:");
        for (Grade grade : Grade.values()) {
            System.out.println(grade.ordinal() + 1 + ". " + grade + " (Grade Point: " + grade.getGradePoint() + ")");
        }

        System.out.print("Select grade: ");
        int gradeIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (gradeIndex < 0 || gradeIndex >= Grade.values().length) {
            System.out.println("Invalid grade selection.");
            return;
        }

        Grade grade = Grade.values()[gradeIndex];

        boolean success = enrollmentService.recordGrade(enrollmentId, grade);

        if (success) {
            System.out.println("Grade recorded successfully!");
        } else {
            System.out.println("Failed to record grade.");
        }
    }

    private void importExportData() {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== IMPORT/EXPORT DATA ===");
            System.out.println("1. Import Students from CSV");
            System.out.println("2. Import Courses from CSV");
            System.out.println("3. Export Students to CSV");
            System.out.println("4. Export Courses to CSV");
            System.out.println("5. Export All Data");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        importStudentsFromCSV();
                        break;
                    case 2:
                        importCoursesFromCSV();
                        break;
                    case 3:
                        exportStudentsToCSV();
                        break;
                    case 4:
                        exportCoursesToCSV();
                        break;
                    case 5:
                        exportAllData();
                        break;
                    case 6:
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void importStudentsFromCSV() {
        System.out.println("\n=== IMPORT STUDENTS FROM CSV ===");

        System.out.print("Enter CSV file path (or press Enter for default 'test-data/students.csv'): ");
        String filePath = scanner.nextLine();

        if (filePath.isEmpty()) {
            filePath = "test-data/students.csv";
        }

        Path path = Paths.get(filePath);

        try {
            importExportService.importStudentsFromCSV(path);
        } catch (IOException e) {
            System.out.println("Error importing students: " + e.getMessage());
        }
    }

    private void importCoursesFromCSV() {
        System.out.println("\n=== IMPORT COURSES FROM CSV ===");

        System.out.print("Enter CSV file path (or press Enter for default 'test-data/courses.csv'): ");
        String filePath = scanner.nextLine();

        if (filePath.isEmpty()) {
            filePath = "test-data/courses.csv";
        }

        Path path = Paths.get(filePath);

        try {
            importExportService.importCoursesFromCSV(path);
        } catch (IOException e) {
            System.out.println("Error importing courses: " + e.getMessage());
        }
    }

    private void exportStudentsToCSV() {
        System.out.println("\n=== EXPORT STUDENTS TO CSV ===");

        System.out.print("Enter CSV file path (or press Enter for default 'data/students.csv'): ");
        String filePath = scanner.nextLine();

        if (filePath.isEmpty()) {
            filePath = "data/students.csv";
        }

        Path path = Paths.get(filePath);

        try {
            importExportService.exportStudentsToCSV(path);
        } catch (IOException e) {
            System.out.println("Error exporting students: " + e.getMessage());
        }
    }

    private void exportCoursesToCSV() {
        System.out.println("\n=== EXPORT COURSES TO CSV ===");

        System.out.print("Enter CSV file path (or press Enter for default 'data/courses.csv'): ");
        String filePath = scanner.nextLine();

        if (filePath.isEmpty()) {
            filePath = "data/courses.csv";
        }

        Path path = Paths.get(filePath);

        try {
            importExportService.exportCoursesToCSV(path);
        } catch (IOException e) {
            System.out.println("Error exporting courses: " + e.getMessage());
        }
    }

    private void exportAllData() {
        System.out.println("\n=== EXPORT ALL DATA ===");

        System.out.print("Enter directory path (or press Enter for default 'data'): ");
        String dirPath = scanner.nextLine();

        if (dirPath.isEmpty()) {
            dirPath = "data";
        }

        Path path = Paths.get(dirPath);

        try {
            importExportService.exportAllData(path);
        } catch (IOException e) {
            System.out.println("Error exporting data: " + e.getMessage());
        }
    }

    private void backupAndRestore() {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== BACKUP & RESTORE ===");
            System.out.println("1. Create Backup");
            System.out.println("2. Show Backup Size");
            System.out.println("3. List Backup Contents");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        createBackup();
                        break;
                    case 2:
                        showBackupSize();
                        break;
                    case 3:
                        listBackupContents();
                        break;
                    case 4:
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void createBackup() {
        System.out.println("\n=== CREATE BACKUP ===");

        try {
            Path backupDir = backupService.createBackup();
            System.out.println("Backup created successfully at: " + backupDir);
        } catch (IOException e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }

    private void showBackupSize() {
        System.out.println("\n=== SHOW BACKUP SIZE ===");

        try {
            long size = backupService.getBackupSize();
            System.out.println("Backup directory size: " + size + " bytes");

            // Convert to more readable format
            String[] units = {"B", "KB", "MB", "GB"};
            int unitIndex = 0;
            double tempSize = size;

            while (tempSize >= 1024 && unitIndex < units.length - 1) {
                tempSize /= 1024;
                unitIndex++;
            }

            System.out.printf("That's approximately %.2f %s%n", tempSize, units[unitIndex]);
        } catch (IOException e) {
            System.out.println("Error calculating backup size: " + e.getMessage());
        }
    }

    private void listBackupContents() {
        System.out.println("\n=== LIST BACKUP CONTENTS ===");

        System.out.print("Enter maximum depth (or press Enter for unlimited): ");
        String depthInput = scanner.nextLine();

        int maxDepth;
        if (depthInput.isEmpty()) {
            maxDepth = -1; // Unlimited
        } else {
            try {
                maxDepth = Integer.parseInt(depthInput);
                if (maxDepth < 0) maxDepth = -1; // Unlimited
            } catch (NumberFormatException e) {
                System.out.println("Invalid depth. Using unlimited depth.");
                maxDepth = -1; // Unlimited
            }
        }

        try {
            backupService.listBackupContents(maxDepth);
        } catch (IOException e) {
            System.out.println("Error listing backup contents: " + e.getMessage());
        }
    }

    private void generateReports() {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== GENERATE REPORTS ===");
            System.out.println("1. GPA Distribution");
            System.out.println("2. Top Students");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        showGPADistribution();
                        break;
                    case 2:
                        showTopStudents();
                        break;
                    case 3:
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void showGPADistribution() {
        System.out.println("\n=== GPA DISTRIBUTION ===");

        // Calculate GPA distribution using Stream API
        Map<String, Long> gradeCounts = enrollmentService.getAllEnrollments().stream()
                .filter(e -> e.getGrade() != null && e.isActive())
                .collect(Collectors.groupingBy(
                        e -> e.getGrade().getLetter(),
                        Collectors.counting()
                ));

        if (gradeCounts.isEmpty()) {
            System.out.println("No graded enrollments found.");
            return;
        }

        long totalGradedEnrollments = gradeCounts.values().stream().mapToLong(Long::longValue).sum();

        System.out.println("Grade Distribution:");
        System.out.println("------------------");

        for (Map.Entry<String, Long> entry : gradeCounts.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / totalGradedEnrollments;
            System.out.printf("%s: %.2f%%%n", entry.getKey(), percentage);
        }
    }

    private void showTopStudents() {
        System.out.println("\n=== TOP STUDENTS ===");

        System.out.print("Enter number of top students to show: ");
        int limit;

        try {
            limit = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (limit <= 0) {
                System.out.println("Number must be positive.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Clear invalid input
            return;
        }

        // Get top students using Stream API
        List<Student> topStudents = studentService.getAllStudents().stream()
                .filter(s -> !s.getEnrolledCourses().isEmpty())
                .sorted((s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA()))
                .limit(limit)
                .collect(Collectors.toList());

        if (topStudents.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.printf("%-5s %-15s %-20s %-10s%n",
                "Rank", "Reg No", "Name", "GPA");
        System.out.println("------------------------------------------------");

        int rank = 1;
        for (Student student : topStudents) {
            System.out.printf("%-5d %-15s %-20s %-10.2f%n",
                    rank++,
                    student.getRegNo(),
                    student.getFullName(),
                    student.calculateGPA());
        }
    }

    public static void main(String[] args) {
        MainCLI cli = new MainCLI();
        cli.start();

        // Print platform note
        System.out.println("\n=== PLATFORM NOTE ===");
        System.out.println("Java SE (Standard Edition) is used for this application.");
        System.out.println("Java SE provides core functionality for desktop and server applications.");
        System.out.println("Java ME (Micro Edition) is designed for embedded and mobile devices.");
        System.out.println("Java EE (Enterprise Edition) extends SE with specifications for enterprise applications.");
    }
}