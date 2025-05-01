package sms.gradle.model.dao;

import java.io.File;
import java.sql.*;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.utils.DatabaseScripts;

public final class DatabaseConnection {
    private static final Logger LOGGER = LogManager.getLogger();

    // Static instance for singleton pattern
    private static DatabaseConnection instance;
    private static Connection connection;
    private static final String DB_PATH = "jdbc:h2:./data/sms";

    private DatabaseConnection() {
        LOGGER.debug("Creating database connection");
        createDataFolder();
        connectToDatabase();
        setupTables();
        LOGGER.debug("Database connection established");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private void createDataFolder() {
        LOGGER.debug("Creating data folder if it doesn't exist");
        File dataDir = new File("data");
        if (dataDir.exists()) {
            LOGGER.info("Data folder already exists");
            return;
        }
        LOGGER.debug("Data folder doesn't exist, creating it");
        if (!dataDir.mkdir()) {
            LOGGER.error("Failed to create data folder");
            throw new RuntimeException("Failed to create data folder");
        }
        LOGGER.debug("Data folder created successfully");
    }

    private void connectToDatabase() {
        LOGGER.debug("Connecting to database");
        try {
            connection = DriverManager.getConnection(DB_PATH);
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database: {}", DB_PATH, e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void setupTables() {
        LOGGER.debug("Setting up database tables");
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
            LOGGER.info("Tables created successfully (or already existed)");
        } catch (SQLException e) {
            LOGGER.error("Failed to create database tables", e);
        }
    }

    public boolean isPopulated() {
        LOGGER.debug("Checking if database is populated");
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM students");
            if (!resultSet.next()) {
                LOGGER.error("Failed to check if database is populated: ResultSet is empty");
                return false;
            }
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            LOGGER.error("Failed to check if database is populated", e);
            return false;
        }
    }
}
