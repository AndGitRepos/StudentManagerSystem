package sms.gradle.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.entities.Result;

/**
 * Data Access Object for managing Result entities in the database
 */
public class ResultDAO {
    private static final Logger LOGGER = LogManager.getLogger(ResultDAO.class);

    private ResultDAO() {
        throw new UnsupportedOperationException("This is a DAO class and cannot be instantiated");
    }

    /**
     * Converts a <code>ResultSet</code> containing result data into a List of <code>Result</code>
     * objects
     *
     * @param resultSet The ResultSet containing result data to convert
     * @return A List of Result objects created from the ResultSet data
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static List<Result> getAllResultsFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to List<Result>");
        List<Result> results = new ArrayList<>();
        while (resultSet.next()) {
            results.add(new Result(
                    resultSet.getInt("id"),
                    resultSet.getInt("student_id"),
                    resultSet.getInt("assessment_id"),
                    resultSet.getInt("grade")));
        }
        return results;
    }

    /**
     * Converts a <code>ResultSet</code> containing result data into a single
     * <code>Optional<Result></code> object
     *
     * @param resultSet The ResultSet containing result data to convert
     * @return An Optional containing the Result object if data is found, or an empty Optional if no
     *         data is found
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static Optional<Result> getResultFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to Optional<Result>");
        if (resultSet.next()) {
            return Optional.of(new Result(
                    resultSet.getInt("id"),
                    resultSet.getInt("student_id"),
                    resultSet.getInt("assessment_id"),
                    resultSet.getInt("grade")));
        }
        LOGGER.info("No result found in ResultSet");
        return Optional.empty();
    }

    /**
     * Adds a new result to the database
     *
     * @param result The Result object to add
     * @throws SQLException if there is an error executing the query
     */
    public static void addResult(final Result result) throws SQLException {
        LOGGER.debug("Adding result to database {}", result);
        final String sql = "INSERT INTO results (student_id, assessment_id, grade) VALUES (?, ?, ?)";
        try (PreparedStatement addSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            addSqlStatement.setInt(1, result.getStudentId());
            addSqlStatement.setInt(2, result.getAssessmentId());
            addSqlStatement.setInt(3, result.getGrade());
            addSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to add result to database {}", result, e);
            throw new SQLException(String.format("Failed to add result: %s", result.toString()), e);
        }
    }

    /**
     * Finds a result by its ID
     *
     * @param id The ID of the result to find
     * @return An Optional containing the Result object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<Result> findById(final int id) throws SQLException {
        LOGGER.debug("Finding result by id: {}", id);
        final String sql = "SELECT * FROM results WHERE id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, id);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getResultFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find result by id: {}", id, e);
            throw new SQLException(String.format("Failed to find result with Id: %d", id), e);
        }
    }

    /**
     * Finds results for a specific student
     *
     * @param studentId The ID of the student whose results to find
     * @return An List containing the Result objects for given student specified
     * @throws SQLException if there is an error executing the query
     */
    public static List<Result> findByStudentId(final int studentId) throws SQLException {
        LOGGER.debug("Finding results by student id: {}", studentId);
        final String sql = "SELECT * FROM results WHERE student_id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, studentId);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllResultsFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find results by student id: {}", studentId, e);
            throw new SQLException(String.format("Failed to find results for Student Id: %d", studentId), e);
        }
    }

    /**
     * Finds a results for specific assessment
     *
     * @param assessmentId The ID of the assessment to search results for
     * @return An List of Result objects for given assessment specified
     * @throws SQLException if there is an error executing the query
     */
    public static List<Result> findByAssessmentId(final int assessmentId) throws SQLException {
        LOGGER.debug("Finding results by assessment id: {}", assessmentId);
        final String sql = "SELECT * FROM results WHERE assessment_id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, assessmentId);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllResultsFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find results by assessment id: {}", assessmentId, e);
            throw new SQLException(String.format("Failed to find result for assessment with Id: %d", assessmentId), e);
        }
    }

    /**
     * Retrieves all results from the database
     *
     * @return A List containing all Result objects in the database
     * @throws SQLException if there is an error executing the query
     */
    public static List<Result> findAll() throws SQLException {
        LOGGER.debug("Finding all results");
        final String sql = "SELECT * FROM results";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllResultsFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find all results", e);
            throw new SQLException("Failed to find all results", e);
        }
    }

    /**
     * Updates a result in the database
     *
     * @param result The Result object with updated information
     * @return The number of rows affected (1 if successful, 0 if result not found)
     * @throws SQLException if there is an error executing the update operation
     */
    public static int update(final Result result) throws SQLException {
        LOGGER.debug("Updating result: {}", result);
        final String sql = "UPDATE results SET student_id = ?, assessment_id = ?, grade = ? WHERE id = ?";
        try (PreparedStatement updateSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            updateSqlStatement.setInt(1, result.getStudentId());
            updateSqlStatement.setInt(2, result.getAssessmentId());
            updateSqlStatement.setInt(3, result.getGrade());
            updateSqlStatement.setInt(4, result.getId());

            return updateSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update result: {}", result, e);
            throw new SQLException(String.format("Failed to update result with Id: %d", result.getId()), e);
        }
    }

    /**
     * Deletes a result from the database by its ID
     *
     * @param id The ID of the result to delete
     * @return The number of rows affected (1 if successful, 0 if result not found)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int delete(final int id) throws SQLException {
        LOGGER.debug("Deleting result with id: {}", id);
        final String sql = "DELETE FROM results WHERE id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, id);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete result with id: {}", id, e);
            throw new SQLException(String.format("Failed to delete result with Id: %d", id), e);
        }
    }

    /**
     * Deletes results from the database by student ID
     *
     * @param studentId The ID of the student whose results to delete
     * @return The number of rows affected (number of results deleted)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int deleteByStudentId(final int studentId) throws SQLException {
        LOGGER.debug("Deleting results by student id: {}", studentId);
        List<Result> results = findByStudentId(studentId);
        for (Result result : results) {
            delete(result.getId());
        }
        LOGGER.info("Deleted {} results for student id: {}", results.size(), studentId);
        return results.size();
    }

    /**
     * Deletes results from the database by assessment ID
     *
     * @param assessmentId The ID of the assessment whose results to delete
     * @return The number of rows affected (number of results deleted)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int deleteByAssessmentId(final int assessmentId) throws SQLException {
        LOGGER.debug("Deleting results by assessment id: {}", assessmentId);
        List<Result> results = findByAssessmentId(assessmentId);
        for (Result result : results) {
            delete(result.getId());
        }
        LOGGER.info("Deleted {} results for assessment id: {}", results.size(), assessmentId);
        return results.size();
    }
}
