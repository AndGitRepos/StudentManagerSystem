package sms.gradle.model.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import sms.gradle.utils.DatabaseScripts;

public final class DatabaseConnection {

    // Static instance for singleton pattern
    private static DatabaseConnection instance;
    private static Connection connection;
    private static final String DB_PATH = "jdbc:h2:./data/sms";

    private DatabaseConnection() {
        createDataFolder();
        connectToDatabase();
        setupTables();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private void createDataFolder() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
            System.out.println("Creating data folder");
        }
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DB_PATH);
        } catch (SQLException e) {
            System.out.println("Failed to establish database connection: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void setupTables() {
        try (Statement statement = connection.createStatement()) {
            // Create tables
            statement.execute(DatabaseScripts.CREATE_STUDENTS_TABLE);
            statement.execute(DatabaseScripts.CREATE_ADMINS_TABLE);
            statement.execute(DatabaseScripts.CREATE_COURSES_TABLE);
            statement.execute(DatabaseScripts.CREATE_MODULES_TABLE);
            statement.execute(DatabaseScripts.CREATE_COURSE_ENROLLMENTS_TABLE);
            statement.execute(DatabaseScripts.CREATE_ASSESSMENTS_TABLE);
            statement.execute(DatabaseScripts.CREATE_RESULTS_TABLE);

            // Add default admin account to admin table
            statement.execute(DatabaseScripts.CREATE_DEFAULT_ADMIN);
            System.out.println("Tables created successfully (or already existed)");
        } catch (SQLException e) {
            System.out.println("Failed to create database tables: " + e.getMessage());
        }
    }
}
