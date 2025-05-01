package sms.gradle.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.entities.Module;

public final class ModuleDAO {
    private static final Logger LOGGER = LogManager.getLogger();

    private ModuleDAO() {
        throw new UnsupportedOperationException("This is a DAO class and cannot be instantiated");
    }

    /**
     * Converts a <code>ResultSet</code> containing module data into a List of <code>Module</code> objects
     * @param resultSet The ResultSet containing module data to convert
     * @return A List of Module objects created from the ResultSet data
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static List<Module> getAllModulesFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to List<Module>");
        List<Module> modules = new ArrayList<>();
        while (resultSet.next()) {
            modules.add(new Module(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("lecturer"),
                    resultSet.getInt("course_id")));
        }
        return modules;
    }

    /**
     * Converts a <code>ResultSet</code> containing module data into a single <code>Optional<Module></code> object
     * @param resultSet The ResultSet containing module data to convert
     * @return An Optional containing the Module object if data is found, or an empty Optional if no data is found
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static Optional<Module> getModuleFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to Optional<Module>");
        if (resultSet.next()) {
            return Optional.of(new Module(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("lecturer"),
                    resultSet.getInt("course_id")));
        }
        LOGGER.info("No module found in ResultSet");
        return Optional.empty();
    }

    /**
     * Adds a new module to the database
     * @param module The Module object to add
     * @throws SQLException if there is an error executing the query
     */
    public static void addModule(final Module module) throws SQLException {
        LOGGER.debug("Adding module to database {}", module);
        final String sql = "INSERT INTO modules (name, description, lecturer, course_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement addSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            addSqlStatement.setString(1, module.getName());
            addSqlStatement.setString(2, module.getDescription());
            addSqlStatement.setString(3, module.getLecturer());
            addSqlStatement.setInt(4, module.getCourseId());
            addSqlStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Failed to add module to database {}", module, e);
            throw new SQLException(String.format("Failed to add module: %s", module.toString()), e);
        }
    }

    /**
     * Finds a module by its ID
     * @param id The ID of the module to find
     * @return An Optional containing the Module object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<Module> findById(final int id) throws SQLException {
        LOGGER.debug("Finding module by ID: {}", id);
        final String sql = "SELECT * FROM modules WHERE id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, id);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getModuleFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find module by ID: {}", id, e);
            throw new SQLException(String.format("Failed to find module with Id: %d", id), e);
        }
    }

    /**
     * Finds a module by its name
     * @param name The name of the module to find
     * @return An Optional containing the Module object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<Module> findByName(final String name) throws SQLException {
        LOGGER.debug("Finding module by name: {}", name);
        final String sql = "SELECT * FROM modules WHERE name = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setString(1, name);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getModuleFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find module by name: {}", name, e);
            throw new SQLException(String.format("Failed to find module with name: %s", name), e);
        }
    }

    /**
     * Finds a list of modules by its lecturer
     * @param lecturer The lecturer of the module to find
     * @return A list of all the modules taught by the lecturer
     * @throws SQLException if there is an error executing the query
     */
    public static List<Module> findByLecturer(final String lecturer) throws SQLException {
        LOGGER.debug("Finding module by lecturer: {}", lecturer);
        final String sql = "SELECT * FROM modules WHERE lecturer = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setString(1, lecturer);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllModulesFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find module by lecturer: {}", lecturer, e);
            throw new SQLException(String.format("Failed to find module with lecturer: %s", lecturer), e);
        }
    }

    /**
     * Finds a list of modules by its course ID
     * @param courseId The course ID of the module to find
     * @return A list of all the modules in the course
     * @throws SQLException if there is an error executing the query
     */
    public static List<Module> findByCourseId(final int courseId) throws SQLException {
        LOGGER.debug("Finding module by course ID: {}", courseId);
        final String sql = "SELECT * FROM modules WHERE course_id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, courseId);
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllModulesFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find module by course ID: {}", courseId, e);
            throw new SQLException(String.format("Failed to find module with course_Id: %d", courseId), e);
        }
    }

    /**
     * Finds all the modules within the Database table
     * @return A List containing all Module objects in the database
     * @throws SQLException if there is an error executing the query
     */
    public static List<Module> findAll() throws SQLException {
        LOGGER.debug("Finding all modules");
        final String sql = "SELECT * FROM modules";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = findSqlStatement.executeQuery();
            return getAllModulesFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Failed to find all modules", e);
            throw new SQLException("Failed to find all modules", e);
        }
    }

    /**
     * Updates a module in the database
     * @param module The Module object with updated information
     * @return The number of rows affected (1 if successful, 0 if module not found)
     * @throws SQLException if there is an error executing the update operation
     */
    public static int update(final Module module) throws SQLException {
        LOGGER.debug("Updating module: {}", module);
        final String sql = "UPDATE modules SET name = ?, description = ?, lecturer = ?, course_id = ? WHERE id = ?";
        try (PreparedStatement updateSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            updateSqlStatement.setString(1, module.getName());
            updateSqlStatement.setString(2, module.getDescription());
            updateSqlStatement.setString(3, module.getLecturer());
            updateSqlStatement.setInt(4, module.getCourseId());
            updateSqlStatement.setInt(5, module.getId());
            return updateSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update module: {}", module, e);
            throw new SQLException(String.format("Failed to update module with Id: %d", module.getId()), e);
        }
    }

    /**
     * Deletes a module from the database by its ID
     * @param id The ID of the module to delete
     * @return The number of rows affected (1 if successful, 0 if module not found)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int delete(final int id) throws SQLException {
        LOGGER.debug("Deleting module with ID: {}", id);
        AssessmentDAO.deleteByModuleId(id);
        final String sql = "DELETE FROM modules WHERE id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, id);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete module with ID: {}", id, e);
            throw new SQLException(String.format("Failed to delete module with Id: %d", id), e);
        }
    }

    /**
     * Deletes all modules associated with a course ID
     * @param courseId The course ID of the modules to delete
     * @return The number of modules deleted
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int deleteByCourseId(final int courseId) throws SQLException {
        LOGGER.debug("Deleting all modules associated with course ID: {}", courseId);
        List<Module> modules = findByCourseId(courseId);
        for (Module module : modules) {
            delete(module.getId());
        }
        LOGGER.info("Deleted {} modules associated with course ID: {}", modules.size(), courseId);
        return modules.size();
    }
}
