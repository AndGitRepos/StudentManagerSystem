package sms.gradle.utils;

public class DatabaseScripts {

    public static final String CREATE_STUDENTS_TABLE =
            """
        CREATE TABLE IF NOT EXISTS students (
            id INT PRIMARY KEY AUTO_INCREMENT,
            first_name VARCHAR(255) NOT NULL,
            last_name VARCHAR(255) NOT NULL,
            email VARCHAR(255) UNIQUE NOT NULL,
            password VARCHAR(255) NOT NULL,
            date_of_birth DATE NOT NULL,
            join_date DATE NOT NULL
        );
        """;

    public static final String CREATE_ADMINS_TABLE =
            """
        CREATE TABLE IF NOT EXISTS admins (
            id INT PRIMARY KEY AUTO_INCREMENT,
            first_name VARCHAR(255) NOT NULL,
            last_name VARCHAR(255) NOT NULL,
            email VARCHAR(255) UNIQUE NOT NULL,
            password VARCHAR(255) NOT NULL
        );
        """;

    public static final String CREATE_COURSES_TABLE =
            """
        CREATE TABLE IF NOT EXISTS courses (
            id INT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(255) NOT NULL,
            description VARCHAR(255) NOT NULL
        );
        """;

    public static final String CREATE_MODULES_TABLE =
            """
        CREATE TABLE IF NOT EXISTS modules (
            id INT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(255) NOT NULL,
            description VARCHAR(255) NOT NULL,
            lecturer VARCHAR(255) NOT NULL,
            course_id int NOT NULL,
            FOREIGN KEY (course_id) REFERENCES courses(id)
        );
        """;

    public static final String CREATE_COURSE_ENROLLMENTS_TABLE =
            """
        CREATE TABLE IF NOT EXISTS course_enrollments (
            id INT PRIMARY KEY AUTO_INCREMENT,
            student_id INT NOT NULL,
            course_id INT NOT NULL,
            enrollment_date DATE NOT NULL,
            FOREIGN KEY (student_id) REFERENCES students(id),
            FOREIGN KEY (course_id) REFERENCES courses(id)
        );
        """;

    public static final String CREATE_ASSESSMENTS_TABLE =
            """
        CREATE TABLE IF NOT EXISTS assessments (
            id INT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(255) NOT NULL,
            description VARCHAR(255) NOT NULL,
            due_date DATE NOT NULL,
            module_id int NOT NULL,
            FOREIGN KEY (module_id) REFERENCES modules(id)
        );
        """;

    public static final String CREATE_RESULTS_TABLE =
            """
        CREATE TABLE IF NOT EXISTS results (
            id INT PRIMARY KEY AUTO_INCREMENT,
            student_id INT NOT NULL,
            assessment_id INT NOT NULL,
            grade INT NOT NULL,
            FOREIGN KEY (student_id) REFERENCES students(id),
            FOREIGN KEY (assessment_id) REFERENCES assessments(id)
        );
        """;

    // note: '8c697...' is a sha256 hash for the word 'admin'
    public static final String CREATE_DEFAULT_ADMIN =
            """
        MERGE INTO admins (
            first_name,
            last_name,
            email,
            password
        ) KEY (email) VALUES (
            'Tom',
            'Cruise',
            'admin@sms.com',
            '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918'
        );
        """;
}
