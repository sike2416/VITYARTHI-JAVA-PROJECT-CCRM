

# Campus Course & Records Manager (CCRM)
Implemented a console-based Java application called Campus Course &amp; Records Manager (CCRM) that lets an institute manage: Students, Courses, Grades &amp; Transcripts and File Utilities. 

# How to Run the Project
### Prerequisites
- JDK 17 or higher installed
- Git installed (to clone the repository)
### Step-by-Step Instructions 

1. Clone the repository:
```
git clone https://github.com/sakshi23bcg10019/vityarthi-java-ccrm.git
cd student-course-management
```

2. Compile the project:
```
mkdir -p bin
javac -d bin src/main/java/edu/ccrm/**/*.java
```

3. Run the application:
```
java -cp bin edu.ccrm.Main
```
----

### Alternative: Using Eclipse IDE
1. Clone the repository:
```
git clone https://github.com/sakshi23bcg10019/vityarthi-java-ccrm.git
```

2. Import into Eclipse:
Open Eclipse
File → `Import` → `Git` → Projects from Git
Select `Clone URI`

Paste your repository URL:` https://github.com/sakshi23bcg10019/student-course-management.git `

3. Select the main branch

4. Choose `Import as general project`
5. Right-click the project → Run As → Java Application
6. Select `edu.ccrm.Main` as the main class

## Evolution of Java
* 1991: Java project started by James Gosling at Sun Microsystems (originally called Oak)
* 1995: Java 1.0 released with "Write Once, Run Anywhere" capability
* 1998: Java 1.2 (Java 2) released with Swing and Collections framework
* 2004: Java 5 released with generics, annotations, and autoboxing
* 2010: Oracle acquired Sun Microsystems
* 2011: Java 7 released with improved I/O and try-with-resources
* 2014: Java 8 released with lambda expressions and Stream API
* 2017: Java 9 introduced module system (Project Jigsaw)
* 2018: Java 11 LTS released with new HTTP client and var keyword
* 2021: Java 17 LTS released with sealed classes and pattern matching

## Java ME vs SE vs EE 
| Feature | Java ME | Java SE | Java EE |
|---------|---------|---------|---------|
| Purpose | Mobile and embedded devices | Desktop and server applications | Enterprise-level applications |
| APIs | Limited core APIs | Complete core APIs | Extended APIs for enterprise |
| Memory Footprint | Small | Medium | Large |
| Examples | Mobile phones, set-top boxes | Desktop applications, web servers | Large-scale enterprise systems |

## Java Architecture: JDK, JRE, JVM
- JDK (Java Development Kit): Provides tools for developing Java applications (compiler, debugger, etc.)
- JRE (Java Runtime Environment): Contains JVM and core libraries to run Java applications
- JVM (Java Virtual Machine): Executes Java bytecode and provides platform independence
### How they interact: 
Developers use JDK to write and compile Java code. The compiled bytecode runs on JRE, which contains the JVM that executes the code.

## Install & Configure Java on Windows
1. Download JDK from Oracle's website
2. Run the installer and follow the installation steps
3. Set environment variables:
```
JAVA_HOME: C:\Program Files\Java\jdk-17
```
4. Add %JAVA_HOME%\bin to PATH
5. Verify installation:
```
java -version
javac -version
```
Note: Screenshots available in the screenshots folder.


## Mapping Table
| Syllabus Topic | File/Class/Method | Demonstration |
|----------------|-------------------|---------------|
| Primitive variables | `domain/Student.java` | `int credits`, `boolean active` |
| Objects | `domain/Student.java` | `Student student = new Student(...)` |
| Operators | `util/Comparators.java` | `(s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA())` |
| Operator precedence | `cli/MainCLI.java` | Documented in comments |
| Decision structures | `cli/MainCLI.java` | `if/else`, `switch` statements in menu system |
| Loops | `cli/MainCLI.java` | `while`, `for`, `enhanced for` loops |
| Jump control | `cli/MainCLI.java` | `break`, `continue` in menu navigation |
| Arrays | `service/StudentServiceImpl.java` | `List<Student> students` |
| Array utilities | `service/StudentServiceImpl.java` | `students.stream().filter(...)` |
| String methods | `domain/Name.java` | `equals`, `hashCode`, `toString` |
| Encapsulation | `domain/Person.java` | Private fields with getters/setters |
| Inheritance | `domain/Student.java` | `extends Person` |
| Abstraction | `domain/Person.java` | Abstract class with abstract methods |
| Polymorphism | `domain/Student.java` | `@Override toString()` |
| Access levels | `domain/Person.java` | `private`, `protected`, `public` |
| Constructors in inheritance | `domain/Student.java` | `super(id, fullName, email)` |
| Immutability | `domain/Name.java` | `final` fields, no setters |
| Nested classes | `domain/Student.java` | `static class StudentComparator` |
| Inner classes | `domain/Student.java` | `class Transcript` |
| Interfaces | `service/StudentService.java` | `interface StudentService` |
| Diamond problem | `service/StudentService.java` | Default methods resolution |
| Functional interfaces | `util/Comparators.java` | `Comparator<Student> STUDENT_BY_NAME` |
| Lambdas | `util/Comparators.java` | `(s1, s2) -> s1.getFullName().toString().compareTo(...)` |
| Anonymous inner classes | `cli/MainCLI.java` | Custom listener implementation |
| Enums | `domain/Semester.java` | `enum Semester` with fields |
| Upcast & downcast | `cli/MainCLI.java` | Documented with comments |
| instanceof | `cli/MainCLI.java` | Type checking in menu handling |
| Overriding & overloading | `domain/Student.java` | Method overriding |
| Singleton pattern | `config/AppConfig.java` | `static synchronized AppConfig getInstance()` |
| Builder pattern | `domain/Course.java` | `static class Builder` |
| Errors vs Exceptions | `README.md` | Explained in this section |
| Checked exceptions | `service/EnrollmentServiceImpl.java` | `throws DuplicateEnrollmentException` |
| Custom exceptions | `exception/DuplicateEnrollmentException.java` | Custom exception classes |
| Assertions | `domain/Student.java` | `assert id != null : "ID cannot be null"` |
| NIO.2 | `io/ImportExportService.java` | `Path`, `Files` operations |
| Stream API | `service/StudentServiceImpl.java` | `students.stream().filter(...)` |
| Date/Time API | `domain/Student.java` | `LocalDateTime.now()` |

## Errors vs Exceptions
In Java, both Error and Exception extend from the Throwable class, but they serve different purposes:

* Errors: Represent serious problems that applications should not try to catch. Examples include OutOfMemoryError and StackOverflowError. These are typically caused by the environment in which the application is running.
- Exceptions: Represent conditions that applications might want to catch and handle. They are further divided into:
    - Checked Exceptions: Must be declared in method signatures or caught (e.g., IOException)
    - Unchecked Exceptions: Extend RuntimeException and don't need to be declared or caught (e.g., NullPointerException)

## Enabling Assertions
Assertions are disabled by default in Java. To enable them:

1. Compile with assertions:
```
javac -d bin -sourcepath src src/main/java/edu/ccrm/**/*.java
```
2. Run with assertions enabled:
```
java -ea -cp bin edu.ccrm.Main
```
## Package Design
```
edu.ccrm
├─ cli/          // Menu, input loop
├─ domain/       // Person (abstract), Student, Instructor, Course, Enrollment, Grade (enum),
│                // Semester (enum), immutable value objects
├─ service/      // StudentService, CourseService, EnrollmentService, TranscriptService
│                // (polymorphic interfaces/impls)
├─ io/           // ImportExportService (NIO.2), BackupService, parsers (CSV)
├─ util/         // Validators, Comparators (lambdas), recursion utilities
└─ config/       // AppConfig (Singleton), builders
```
## Usage and sample commands
Sample 1: Adding a student
```
Configuration loaded with data folder: data
Configuration loaded with backup folder: backup
=== Student Course Registration Management System ===

=== MAIN MENU ===
1. Manage Students
2. Manage Courses
3. Manage Enrollments
4. Manage Grades
5. Import/Export Data
6. Backup & Restore
7. Generate Reports
8. Exit
Enter your choice: 1

=== MANAGE STUDENTS ===
1. Add Student
2. List All Students
3. Find Student by ID
4. Update Student
5. Deactivate Student
6. Print Student Profile
7. Print Student Transcript
8. Back to Main Menu
Enter your choice: 1

=== ADD STUDENT ===
Enter student ID: 19
Enter registration number: 19
Enter first name: Sakshi
Enter last name: Shaw
Enter email: sakshshaw@gmail.com
Student added successfully!
```
Sample 2: Adding a course and updating it 
```
=== MAIN MENU ===
1. Manage Students
2. Manage Courses
3. Manage Enrollments
4. Manage Grades
5. Import/Export Data
6. Backup & Restore
7. Generate Reports
8. Exit
Enter your choice: 2

=== MANAGE COURSES ===
1. Add Course
2. List All Courses
3. Find Course by Code
4. Update Course
5. Deactivate Course
6. Search Courses
7. Back to Main Menu
Enter your choice: 1

=== ADD COURSE ===
Enter course prefix (e.g., CS): cs
Enter course number (e.g., 101): 1
Enter course title: java
Enter course credits: 3
Enter department: cs
Available semesters:
1. Spring
2. Summer
3. Fall
4. Winter
Select semester: 3
Course added successfully!

=== MANAGE COURSES ===
1. Add Course
2. List All Courses
3. Find Course by Code
4. Update Course
5. Deactivate Course
6. Search Courses
7. Back to Main Menu
Enter your choice: 4

=== UPDATE COURSE ===
Enter course code to update: cs1
Current course details:
Code: cs1
Title: java
Credits: 3
Department: cs
Semester: Fall
Active: Yes

Enter new details (leave blank to keep current value):
Title [java]: java programming
Credits [3]: 4
Department [cs]: cse
Available semesters:
1. Spring
2. Summer
3. Fall
4. Winter
Select semester [Fall]: 4
Active [Yes] (true/false): true
Course updated successfully!
```
Sample 3: Importing data from csv File
```
=== MAIN MENU ===
1. Manage Students
2. Manage Courses
3. Manage Enrollments
4. Manage Grades
5. Import/Export Data
6. Backup & Restore
7. Generate Reports
8. Exit
Enter your choice: 5

=== IMPORT/EXPORT DATA ===
1. Import Students from CSV
2. Import Courses from CSV
3. Export Students to CSV
4. Export Courses to CSV
5. Export All Data
6. Back to Main Menu
Enter your choice: 1

=== IMPORT STUDENTS FROM CSV ===
Enter CSV file path (or press Enter for default 'test-data/students.csv'): 
Imported 5 students from test-data\students.csv
```
Sample 4: Creating Backups 
```
=== MAIN MENU ===
1. Manage Students
2. Manage Courses
3. Manage Enrollments
4. Manage Grades
5. Import/Export Data
6. Backup & Restore
7. Generate Reports
8. Exit
Enter your choice: 6

=== BACKUP & RESTORE ===
1. Create Backup
2. Show Backup Size
3. List Backup Contents
4. Back to Main Menu
Enter your choice: 1

=== CREATE BACKUP ===
Exported 6 students to backup\backup_20250925_005751\students.csv
Exported 1 courses to backup\backup_20250925_005751\courses.csv
Exported 0 enrollments to backup\backup_20250925_005751\enrollments.csv
All data exported to backup\backup_20250925_005751
Backup created at: backup\backup_20250925_005751
Backup created successfully at: backup\backup_20250925_005751

=== BACKUP & RESTORE ===
1. Create Backup
2. Show Backup Size
3. List Backup Contents
4. Back to Main Menu
Enter your choice: 2

=== SHOW BACKUP SIZE ===
Backup directory size: 832 bytes
That's approximately 832.00 B
```
## Academic Integrity Statement
This project represents original work developed specifically for this assignment. All code has been written independently, with proper implementation of the required Java concepts and design patterns. No external libraries or frameworks were used beyond the Java Standard Edition API.

The Campus Course & Records Manager demonstrates a thorough understanding of Java programming principles, object-oriented design, and software development best practices, while providing a practical solution to academic record management challenges.
