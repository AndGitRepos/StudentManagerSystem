package sms.gradle.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sms.gradle.model.entities.Result;

/**
 * Data Access Object for managing Result entities in the database
 */
public class ResultDAO {

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
        if (resultSet.next()) {
            return Optional.of(new Result(
                    resultSet.getInt("id"),
                    resultSet.getInt("student_id"),
                    resultSet.getInt("assessment_id"),
                    resultSet.getInt("grade")));
        }
        return Optional.empty();
    }

    /**
     * Adds a new result to the database
     *
     * @param result The Result object to add
     * @throws SQLException if there is an error executing the query
     */
    public static void addResult(final Result result) throws SQLException {
        final String sql = "INSERT INTO results (student_id, assessment_id, grade) VALUES (?, ?, ?)";
        try (PreparedStatement addSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            addSqlStatement.setInt(1, result.getStudentId());
            addSqlStatement.setInt(2, result.getAssessmentId());
            addSqlStatement.setInt(3, result.getGrade());
            addSqlStatement.executeUpdate();

        } catch (SQLException e) {
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
        final String sql = "SELECT * FROM results WHERE id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, id);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getResultFromResultSet(resultSet);
        } catch (SQLException e) {
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
        final String sql = "SELECT * FROM results WHERE student_id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, studentId);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllResultsFromResultSet(resultSet);
        } catch (SQLException e) {
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
        final String sql = "SELECT * FROM results WHERE assessment_id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, assessmentId);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllResultsFromResultSet(resultSet);
        } catch (SQLException e) {
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
        final String sql = "SELECT * FROM results";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllResultsFromResultSet(resultSet);
        } catch (SQLException e) {
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
        final String sql = "UPDATE results SET student_id = ?, assessment_id = ?, grade = ? WHERE id = ?";
        try (PreparedStatement updateSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            updateSqlStatement.setInt(1, result.getStudentId());
            updateSqlStatement.setInt(2, result.getAssessmentId());
            updateSqlStatement.setInt(3, result.getGrade());
            updateSqlStatement.setInt(4, result.getId());

            return updateSqlStatement.executeUpdate();
        } catch (SQLException e) {
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
        final String sql = "DELETE FROM results WHERE id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, id);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
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
        final String sql = "DELETE FROM results WHERE student_id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, studentId);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(String.format("Failed to delete results for student with Id: %d", studentId), e);
        }
    }

    /**
     * Deletes results from the database by assessment ID
     *
     * @param assessmentId The ID of the assessment whose results to delete
     * @return The number of rows affected (number of results deleted)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int deleteByAssessmentId(final int assessmentId) throws SQLException {
        final String sql = "DELETE FROM results WHERE assessment_id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, assessmentId);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(
                    String.format("Failed to delete results for assessment with Id: %d", assessmentId), e);
        }
    }
}
