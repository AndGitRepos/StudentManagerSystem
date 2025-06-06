package sms.gradle.model.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.entities.Assessment;

public final class AssessmentDAO {
    private static final Logger LOGGER = LogManager.getLogger();

    private AssessmentDAO() {
        throw new UnsupportedOperationException("This is a DAO class and cannot be instantiated");
    }

    /**
     * Converts a <code>ResultSet</code> containing assessment data into a List of <code>Assessment</code> objects
     * @param resultSet The ResultSet containing assessment data to convert
     * @return A List of Assessment objects created from the ResultSet data
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static List<Assessment> getAllAssessmentsFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to List<Assessment>");
        List<Assessment> assessments = new ArrayList<>();
        while (resultSet.next()) {
            assessments.add(new Assessment(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getDate("due_date"),
                    resultSet.getInt("module_id")));
        }
        return assessments;
    }

    /**
     * Converts a <code>ResultSet</code> containing assessment data into a single <code>Optional<Assessment></code> object
     * @param resultSet The ResultSet containing assessment data to convert
     * @return An Optional containing the Assessment object if data is found, or an empty Optional if not data is found
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static Optional<Assessment> getAssessmentFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to Optional<Assessment>");
        if (resultSet.next()) {
            return Optional.of(new Assessment(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getDate("due_date"),
                    resultSet.getInt("module_id")));
        }
        LOGGER.info("No assessment found in ResultSet");
        return Optional.empty();
    }

    /**
     * Adds a new assessment to type database
     * @param assessment The Assessment to add
     * @throws SQLException if there is an error executing the query
     */
    public static void addAssessment(final Assessment assessment) throws SQLException {
        LOGGER.debug("Adding assessment to database {}", assessment);
        final String sql = "INSERT INTO assessments (name, description, due_date, module_id) VALUES (?, ? , ?, ?)";
        try (PreparedStatement addSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            addSqlStatement.setString(1, assessment.getName());
            addSqlStatement.setString(2, assessment.getDescription());
            addSqlStatement.setDate(3, assessment.getDueDate());
            addSqlStatement.setInt(4, assessment.getModuleId());
            addSqlStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Failed to add assessment to database {}", assessment, e);
            throw new SQLException(String.format("Failed to add assessment: %s", assessment.toString()), e);
        }
    }

    /**
     * Finds an assessment by its ID
     * @param id The ID of the assessment to find
     * @return An Optional containing the Assessment Object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<Assessment> findById(final int id) throws SQLException {
        LOGGER.debug("Finding assessment by ID: {}", id);
        final String sql = "SELECT * FROM assessments WHERE id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, id);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAssessmentFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find assessment by ID: {}", id, e);
            throw new SQLException(String.format("Failed to find assessment with Id: %d", id), e);
        }
    }

    /**
     * Finds an assessment by its name
     * @param name The name of the assessment to find
     * @return An Optional containing the Assessment object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<Assessment> findByName(final String name) throws SQLException {
        LOGGER.debug("Finding assessment by name: {}", name);
        final String sql = "SELECT * FROM assessments WHERE name = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setString(1, name);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAssessmentFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find assessment by name: {}", name, e);
            throw new SQLException(String.format("Failed to find assessment with name: %s", name), e);
        }
    }

    /**
     * Find assessments by their due date
     * @param dueDate The due date for the assessments to find
     * @return A List containing all Assessment objects with matching due date in the Database table
     * @throws SQLException if there is an error executing the query
     */
    public static List<Assessment> findByDueDate(final Date dueDate) throws SQLException {
        LOGGER.debug("Finding assessment by due date: {}", dueDate);
        final String sql = "SELECT * FROM assessments WHERE due_date = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setDate(1, dueDate);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllAssessmentsFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find assessment by due date: {}", dueDate, e);
            throw new SQLException(String.format("Failed to find assessment with due date: %s", dueDate), e);
        }
    }

    /**
     * Finds assessments by their module ID
     * @param moduleId The module ID of the assessment to find
     * @return A List containing all Assessment objects with matching module ID in the Database table
     * @throws SQLException if there is an error executing the query
     */
    public static List<Assessment> findByModuleId(final int moduleId) throws SQLException {
        LOGGER.debug("Finding assessment by module ID: {}", moduleId);
        final String sql = "SELECT * FROM assessments WHERE module_id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, moduleId);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllAssessmentsFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find assessment by module ID: {}", moduleId, e);
            throw new SQLException(String.format("Failed to find all assessments with module ID: %d", moduleId), e);
        }
    }

    /**
     * Finds all the assessments within the Database table
     * @return A List containing all Assessment objects in the database
     * @throws SQLException if there is an error executing the query
     */
    public static List<Assessment> findAll() throws SQLException {
        LOGGER.debug("Finding all assessments");
        final String sql = "SELECT * FROM assessments";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllAssessmentsFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find all assessments", e);
            throw new SQLException("Failed to find all assessments", e);
        }
    }

    /**
     * Updates an assessment in the database
     * @param assessment The Assessment object with updated information
     * @return The number of rows affected (1 if successful, 0 if module not found)
     * @throws SQLException if there is an error executing the update operation
     */
    public static int update(final Assessment assessment) throws SQLException {
        LOGGER.debug("Updating assessment: {}", assessment);
        final String sql = "UPDATE assessments SET name = ?, description = ?, due_date = ?, module_id = ? WHERE id = ?";
        try (PreparedStatement updateSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            updateSqlStatement.setString(1, assessment.getName());
            updateSqlStatement.setString(2, assessment.getDescription());
            updateSqlStatement.setDate(3, assessment.getDueDate());
            updateSqlStatement.setInt(4, assessment.getModuleId());
            updateSqlStatement.setInt(5, assessment.getId());
            return updateSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update assessment: {}", assessment, e);
            throw new SQLException(String.format("Failed to update assessment with Id: %d", assessment.getId()), e);
        }
    }

    /**
     * Deletes an assessment from the database by its ID
     * @param id The ID of the assessment to delete
     * @return The number of rows affected (1 if successful, 0 if module not found)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int delete(final int id) throws SQLException {
        LOGGER.debug("Deleting assessment with ID: {}", id);
        ResultDAO.deleteByAssessmentId(id);
        final String sql = "DELETE FROM assessments WHERE id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, id);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete assessment with ID: {}", id, e);
            throw new SQLException(String.format("Failed to delete assessment with Id: %d", id), e);
        }
    }

    /**
     * Deletes all assessments associated with a module ID
     * @param moduleId The module ID of the assessments to delete
     * @return The number of assessments deleted
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int deleteByModuleId(final int moduleId) throws SQLException {
        LOGGER.debug("Deleting assessments by module ID: {}", moduleId);
        List<Assessment> assessments = AssessmentDAO.findByModuleId(moduleId);
        for (Assessment assessment : assessments) {
            AssessmentDAO.delete(assessment.getId());
        }
        LOGGER.info("Deleted {} assessments for module ID: {}", assessments.size(), moduleId);
        return assessments.size();
    }
}
