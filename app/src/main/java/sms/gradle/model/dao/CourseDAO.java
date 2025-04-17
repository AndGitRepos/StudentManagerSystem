package sms.gradle.model.dao;

import java.sql.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.entities.Course;

public final class CourseDAO {
    private static final Logger LOGGER = LogManager.getLogger();

    private CourseDAO() {
        throw new UnsupportedOperationException("This is a DAO class and cannot be instantiated");
    }

    /**
     * Converts a <code>ResultSet</code> containing course data into a List of <code>Course</code> objects
     * @param resultSet The ResultSet containing course data to convert
     * @return A List of Course objects created from the ResultSet data
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static List<Course> getAllCoursesFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to List<Course>");
        List<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            courses.add(new Course(
                    resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description")));
        }
        return courses;
    }

    /**
     * Creates an <code>Optional<Course></code> from a <code>ResultSet</code> row
     * @param resultSet The ResultSet containing course data
     * @return An Optional containing a Course object if data is present, or an empty Optional if not
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static Optional<Course> getCourseFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to Optional<Course>");
        if (resultSet.next()) {
            return Optional.of(new Course(
                    resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description")));
        }
        LOGGER.info("No course found in ResultSet");
        return Optional.empty();
    }

    /**
     * Adds a new course to the database
     * @param course The Course object to add
     * @throws RuntimeException if there is an error executing the query
     */
    public static void addCourse(final Course course) throws SQLException {
        LOGGER.debug("Adding course to database {}", course);
        final String sql = "INSERT INTO courses (name, description) VALUES (?, ?)";
        try (PreparedStatement addSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            addSqlStatement.setString(1, course.getName());
            addSqlStatement.setString(2, course.getDescription());
            addSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to add course to database {}", course, e);
            throw new SQLException(String.format("Failed to add course: %s", course.toString()), e);
        }
    }

    /**
     * Finds a course by its ID
     * @param id The ID of the course to find
     * @return An Optional containing the Course object if found, or an empty Optional if not found
     * @throws RuntimeException if there is an error executing the query
     */
    public static Optional<Course> findById(final int id) throws SQLException {
        LOGGER.debug("Finding course by id: {}", id);
        final String sql = "SELECT * FROM courses WHERE id = ?";
        Optional<Course> course = Optional.empty();
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, id);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getCourseFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find course by id: {}", id, e);
            throw new SQLException(String.format("Failed to find course with Id: %d", id), e);
        }
    }

    /**
     * Finds a course by its name
     * @param name The name of the course to find
     * @return An Optional containing the Course object if found, or an empty Optional if not found
     * @throws RuntimeException if there is an error executing the query
     */
    public static Optional<Course> findByName(final String name) throws SQLException {
        LOGGER.debug("Finding course by name: {}", name);
        Optional<Course> course = Optional.empty();
        final String sql = "SELECT * FROM courses WHERE name = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setString(1, name);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getCourseFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find course by name: {}", name, e);
            throw new SQLException(String.format("Failed to find course with name: %s", name), e);
        }
    }

    /**
     * Retrieves all courses from the database
     * @return A List containing all Course objects in the database
     * @throws RuntimeException if there is an error executing the query
     */
    public static List<Course> findAll() throws SQLException {
        LOGGER.debug("Finding all courses");
        final String sql = "SELECT * FROM courses";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllCoursesFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find all courses", e);
            throw new SQLException("Failed to find all courses", e);
        }
    }

    /**
     * Updates a course in the database
     * @param course The course object with updated information
     * @return The number of rows affected (1 if successful, 0 if course not found)
     * @throws RuntimeException if there is an error executing the update operation
     */
    public static int update(final Course course) throws SQLException {
        LOGGER.debug("Updating course: {}", course);
        final String sql = "UPDATE courses SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement updateSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            updateSqlStatement.setString(1, course.getName());
            updateSqlStatement.setString(2, course.getDescription());
            updateSqlStatement.setInt(3, course.getId());
            return updateSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update course: {}", course, e);
            throw new SQLException(String.format("Failed to update course with Id: %d", course.getId()), e);
        }
    }

    /**
     * Deletes a course from the database by its ID
     * @param id The ID of the course to delete
     * @return The number of rows affected (1 if successful, 0 if course not found)
     * @throws RuntimeException if there is an error executing the delete operation
     */
    public static int delete(final int id) throws SQLException {
        LOGGER.debug("Deleting course with ID: {}", id);
        final String sql = "DELETE FROM courses WHERE id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, id);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete course with ID: {}", id, e);
            throw new SQLException(String.format("Failed to delete course with Id: %d", id), e);
        }
    }
}
