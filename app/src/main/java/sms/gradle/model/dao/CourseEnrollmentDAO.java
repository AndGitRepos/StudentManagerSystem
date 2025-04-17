package sms.gradle.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.entities.CourseEnrollment;

public final class CourseEnrollmentDAO {
    private static final Logger LOGGER = LogManager.getLogger();

    private CourseEnrollmentDAO() {
        throw new UnsupportedOperationException("This is a DAO class and cannot be instantiated");
    }

    /**
     * Converts a <code>ResultSet</code> containing course enrollment data into a List of <code>CourseEnrollment</code> objects
     * @param resultSet The ResultSet containing course enrollment data to convert
     * @return A List of <code>CourseEnrollment</code> objects created from the <code>ResultSet</code> data
     * @throws SQLException if there is an error accessing the <code>ResultSet</code> data
     */
    private static List<CourseEnrollment> getAllCoursesEnrollmentsFromResultSet(final ResultSet resultSet)
            throws SQLException {
        LOGGER.debug("Converting ResultSet to List of CourseEnrollments");
        List<CourseEnrollment> courseEnrollments = new ArrayList<>();
        while (resultSet.next()) {
            courseEnrollments.add(new CourseEnrollment(
                    resultSet.getInt("id"),
                    resultSet.getInt("student_id"),
                    resultSet.getInt("course_id"),
                    resultSet.getDate("enrollment_date")));
        }
        return courseEnrollments;
    }

    /**
     * Creates an <code>Optional<CourseEnrollment></code> from a <code>ResultSet</code> row
     * @param resultSet The ResultSet containing course enrollment data
     * @return An Optional containing a <code>CourseEnrollment</code> object if data is present, or an empty Optional if not
     * @throws SQLException if there is an error accessing the <code>ResultSet</code> data
     */
    private static Optional<CourseEnrollment> getCourseEnrollmentFromResultSet(final ResultSet resultSet)
            throws SQLException {
        LOGGER.debug("Converting ResultSet to CourseEnrollment");
        if (resultSet.next()) {
            return Optional.of(new CourseEnrollment(
                    resultSet.getInt("id"),
                    resultSet.getInt("student_id"),
                    resultSet.getInt("course_id"),
                    resultSet.getDate("enrollment_date")));
        }
        LOGGER.info("No course enrollment found in ResultSet");
        return Optional.empty();
    }

    /**
     * Adds a new course enrollment to the database
     * @param courseEnrollment The <code>CourseEnrollment</code> object to add
     * @throws SQLException if there is an error executing the query
     */
    public static void addCourseEnrollment(final CourseEnrollment courseEnrollment) throws SQLException {
        LOGGER.debug("Adding course enrollment to database {}", courseEnrollment);
        final String sql = "INSERT INTO course_enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, ?)";
        try (PreparedStatement addSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            addSqlStatement.setInt(1, courseEnrollment.getStudentId());
            addSqlStatement.setInt(2, courseEnrollment.getCourseId());
            addSqlStatement.setDate(3, courseEnrollment.getEnrollmentDate());
            addSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to add course enrollment to database {}", courseEnrollment, e);
            throw new SQLException(
                    String.format("Failed to add course enrollment: %s", courseEnrollment.toString()), e);
        }
    }

    /**
     * Finds a course enrollment by its ID
     * @param id The ID of the course to find
     * @return An Optional containing the <code>CourseEnrollment</code> object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<CourseEnrollment> findById(final int id) throws SQLException {
        LOGGER.debug("Finding course enrollment by id: {}", id);
        final String sql = "SELECT * FROM course_enrollments WHERE id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, id);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getCourseEnrollmentFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find course enrollment by id: {}", id, e);
            throw new SQLException(String.format("Failed to find course enrollment with Id: %d", id), e);
        }
    }

    /**
     * Finds course enrollments by student ID
     * @param studentId The ID of the student to find course enrollments for
     * @return A List of <code>CourseEnrollment</code> objects for the specified student
     * @throws SQLException if there is an error executing the query
     */
    public static List<CourseEnrollment> findByStudentId(final int studentId) throws SQLException {
        LOGGER.debug("Finding course enrollments by student id: {}", studentId);
        final String sql = "SELECT * FROM course_enrollments WHERE student_id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, studentId);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllCoursesEnrollmentsFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find course enrollments by student id: {}", studentId, e);
            throw new SQLException(
                    String.format("Failed to find course enrollments for student with Id: %d", studentId), e);
        }
    }

    /**
     * Finds course enrollments by course ID
     * @param courseId The ID of the course to find enrollments for
     * @return A List of <code>CourseEnrollment</code> objects for the specified course
     * @throws SQLException if there is an error executing the query
     */
    public static List<CourseEnrollment> findByCourseId(final int courseId) throws SQLException {
        LOGGER.debug("Finding course enrollments by course id: {}", courseId);
        final String sql = "SELECT * FROM course_enrollments WHERE course_id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, courseId);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllCoursesEnrollmentsFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find course enrollments by course id: {}", courseId, e);
            throw new SQLException(
                    String.format("Failed to find course enrollments for course with Id: %d", courseId), e);
        }
    }

    /**
     * Retrieves all course enrollments from the database
     * @return A List containing all <code>CourseEnrollment</code> objects in the database
     * @throws SQLException if there is an error executing the query
     */
    public static List<CourseEnrollment> findAll() throws SQLException {
        LOGGER.debug("Finding all course enrollments");
        final String sql = "SELECT * FROM course_enrollments";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllCoursesEnrollmentsFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find all course enrollments", e);
            throw new SQLException("Failed to find all course enrollments", e);
        }
    }

    /**
     * Updates a course enrollment in the database
     * @param courseEnrollment The <code>CourseEnrollment</code> object with updated information
     * @return The number of rows affected (1 if successful, 0 if course enrollment not found)
     * @throws SQLException if there is an error executing the update operation
     */
    public static int update(final CourseEnrollment courseEnrollment) throws SQLException {
        LOGGER.debug("Updating course enrollment: {}", courseEnrollment);
        final String sql =
                "UPDATE course_enrollments SET student_id = ?, course_id = ?, enrollment_date = ? WHERE id = ?";
        try (PreparedStatement updateSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            updateSqlStatement.setInt(1, courseEnrollment.getStudentId());
            updateSqlStatement.setInt(2, courseEnrollment.getCourseId());
            updateSqlStatement.setDate(3, courseEnrollment.getEnrollmentDate());
            updateSqlStatement.setInt(4, courseEnrollment.getId());
            return updateSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update course enrollment: {}", courseEnrollment, e);
            throw new SQLException(
                    String.format("Failed to update course enrollment: %s", courseEnrollment.toString()), e);
        }
    }

    /**
     * Deletes a course enrollment from the database by its ID
     * @param id The ID of the course enrollment to delete
     * @return The number of rows affected (1 if successful, 0 if course enrollment is not found)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int delete(final int id) throws SQLException {
        LOGGER.debug("Deleting course enrollment with ID: {}", id);
        final String sql = "DELETE FROM course_enrollments WHERE id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, id);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete course enrollment with ID: {}", id, e);
            throw new SQLException(String.format("Failed to delete course enrollment with Id: %d", id), e);
        }
    }

    /**
     * Deletes course enrollments from the database by student ID
     * @param studentId The ID of the student to delete course enrollments for
     * @return The number of rows affected (number of course enrollments deleted)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int deleteByStudentId(final int studentId) throws SQLException {
        LOGGER.debug("Deleting course enrollments for student with ID: {}", studentId);
        List<CourseEnrollment> courseEnrollments = findByStudentId(studentId);
        for (CourseEnrollment courseEnrollment : courseEnrollments) {
            delete(courseEnrollment.getId());
        }
        LOGGER.info("Deleted {} course enrollments for student with ID: {}", courseEnrollments.size(), studentId);
        return courseEnrollments.size();
    }

    /**
     * Deletes course enrollments from the database by course ID
     * @param courseId The ID of the course to delete enrollments for
     * @return The number of rows affected (number of course enrollments deleted)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int deleteByCourseId(final int courseId) throws SQLException {
        LOGGER.debug("Deleting course enrollments for course with ID: {}", courseId);
        List<CourseEnrollment> courseEnrollments = findByCourseId(courseId);
        for (CourseEnrollment courseEnrollment : courseEnrollments) {
            delete(courseEnrollment.getId());
        }
        LOGGER.info("Deleted {} course enrollments for course with ID: {}", courseEnrollments.size(), courseId);
        return courseEnrollments.size();
    }
}
