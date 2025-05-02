package sms.gradle.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.entities.Student;

public final class StudentDAO {
    private static final Logger LOGGER = LogManager.getLogger();

    private StudentDAO() {
        throw new UnsupportedOperationException("This is a DAO class and cannot be instantiated");
    }

    /**
     * Converts a <code>ResultSet</code> containing student data into a List of <code>Student</code> objects
     * @param resultSet The ResultSet containing student data to convert
     * @return A List of Student objects created from the ResultSet data
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static List<Student> getAllStudentsFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to List<Student>");
        List<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            students.add(new Student(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getDate("date_of_birth"),
                    resultSet.getDate("join_date")));
        }
        return students;
    }

    /**
     * Creates an <code>Optional<Student></code> from a <code>ResultSet</code> row
     * @param resultSet The ResultSet containing student data
     * @return An Optional containing a Student object if data is present, or an empty Optional if not
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static Optional<Student> getStudentFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to Optional<Student>");
        if (resultSet.next()) {
            return Optional.of(new Student(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getDate("date_of_birth"),
                    resultSet.getDate("join_date")));
        }
        LOGGER.info("No student found in ResultSet");
        return Optional.empty();
    }

    /**
     * Adds a student to the database with the given password.
     * @param student the student to add
     * @param hashedPassword the hashed password of the student
     * @throws SQLException if a database access error occurs or the connection is closed
     */
    public static void addStudent(final Student student, final String hashedPassword) throws SQLException {
        LOGGER.debug("Adding student to database {}", student);
        final String sql =
                "INSERT INTO students (first_name, last_name, email, password, date_of_birth, join_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement addSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            addSqlStatement.setString(1, student.getFirstName());
            addSqlStatement.setString(2, student.getLastName());
            addSqlStatement.setString(3, student.getEmail());
            addSqlStatement.setString(4, hashedPassword);
            addSqlStatement.setDate(5, student.getDateOfBirth());
            addSqlStatement.setDate(6, student.getJoinDate());
            addSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to add student to database {}", student, e);

            switch (e.getErrorCode()) {
                case 23505 -> throw new SQLException(
                        String.format("The email '%s' is associated with an existing student.", student.getEmail()), e);
                default -> throw new SQLException("Failed to add student", e);
            }
        }
    }

    /**
     * Finds a student by its ID
     * @param id The ID of the student to find
     * @return An Optional containing the Student object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<Student> findById(final int id) throws SQLException {
        LOGGER.debug("Finding student by ID: {}", id);
        final String sql = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, id);
            ResultSet results = findSqlStatement.executeQuery();
            return getStudentFromResultSet(results);
        } catch (SQLException e) {
            LOGGER.error("Failed to find student by ID: {}", id, e);
            throw new SQLException(String.format("Failed to find student with Id: %d", id), e);
        }
    }

    /**
     * Finds a student by its email address
     * @param email The email address of the student to find
     * @return An Optional containing the Student object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<Student> findByEmail(final String email) throws SQLException {
        LOGGER.debug("Finding student by email: {}", email);
        final String sql = "SELECT * FROM students WHERE email = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setString(1, email);
            ResultSet results = findSqlStatement.executeQuery();
            return getStudentFromResultSet(results);
        } catch (SQLException e) {
            LOGGER.error("Failed to find student by email: {}", email, e);
            throw new SQLException(String.format("Failed to find student with email: %s", email), e);
        }
    }

    /**
     * Retrieves all students from the database
     * @return A List containing all Student objects in the database
     * @throws SQLException if there is an error executing the query
     */
    public static List<Student> findAll() throws SQLException {
        LOGGER.debug("Finding all students");
        final String sql = "SELECT * FROM students";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet results = findSqlStatement.executeQuery();
            return getAllStudentsFromResultSet(results);
        } catch (SQLException e) {
            LOGGER.error("Failed to find all students", e);
            throw new SQLException("Failed to find all students", e);
        }
    }

    /**
     * Updates a student in the database
     * @param student The student object with updated information
     * @return The number of rows affected (1 if successful, 0 if student not found)
     * @throws SQLException if there is an error executing the update operation
     */
    public static int update(final Student student, final String hashedPassword) throws SQLException {
        LOGGER.debug("Updating student: {}", student);
        final String sql =
                "UPDATE students SET first_name = ?, last_name = ?, email = ?, password = ?, date_of_birth = ?, join_date = ? WHERE id = ?";
        try (PreparedStatement updateSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            updateSqlStatement.setString(1, student.getFirstName());
            updateSqlStatement.setString(2, student.getLastName());
            updateSqlStatement.setString(3, student.getEmail());
            updateSqlStatement.setString(4, hashedPassword);
            updateSqlStatement.setDate(5, student.getDateOfBirth());
            updateSqlStatement.setDate(6, student.getJoinDate());
            updateSqlStatement.setInt(7, student.getId());
            return updateSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update student: {}", student, e);
            throw new SQLException(String.format("Failed to update student with Id: %d", student.getId()), e);
        }
    }

    /**
     * Deletes a student from the database by its ID
     * @param id The ID of the student to delete
     * @return The number of rows affected (1 if successful, 0 if student not found)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int delete(final int id) throws SQLException {
        ResultDAO.deleteByStudentId(id);
        CourseEnrollmentDAO.deleteByStudentId(id);
        LOGGER.debug("Deleting student with ID: {}", id);
        final String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, id);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete student with ID: {}", id, e);
            throw new SQLException(String.format("Failed to delete student with Id: %d", id), e);
        }
    }

    /**
     * Deletes a student from the database by its email address
     * @param email The email address of the student to delete
     * @return The number of rows affected (1 if successful, 0 if student not found)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int deleteByEmail(final String email) throws SQLException {
        LOGGER.debug("Deleting student with email: {}", email);
        Optional<Student> student = findByEmail(email);
        if (student.isPresent()) {
            return delete(student.get().getId());
        }
        LOGGER.info("No student found with email: {}", email);
        return 0;
    }

    /**
     * Verifies the password for a student by their email address
     * @param email The email address of the student
     * @param hashedPassword The hashed password to verify
     * @return true if the password matches, false otherwise
     * @throws SQLException if there is an error executing the query
     */
    public static boolean verifyPassword(final String email, final String hashedPassword) throws SQLException {
        LOGGER.debug("Verifying password for student with email: {}", email);
        final String sql = "SELECT password FROM students WHERE email = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setString(1, email);
            ResultSet results = findSqlStatement.executeQuery();
            if (results.next()) {
                LOGGER.debug("Found student with email: {}", email);
                String storedPassword = results.getString("password");
                return hashedPassword.equals(storedPassword);
            }
            return false;
        } catch (SQLException e) {
            LOGGER.error("Failed to verify password for student with email: {}", email, e);
            throw new SQLException(String.format("Failed to verify password for student with email: %s", email), e);
        }
    }
}
