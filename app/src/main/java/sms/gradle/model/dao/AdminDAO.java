package sms.gradle.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.entities.Admin;

public final class AdminDAO {
    private static final Logger LOGGER = LogManager.getLogger();

    private AdminDAO() {
        throw new UnsupportedOperationException("This is a DAO class and cannot be instantiated");
    }

    /**
     * Converts a <code>ResultSet</code> containing admin data into a List of <code>Admin</code> objects
     * @param resultSet The ResultSet containing admin data to convert
     * @return A List of Admin objects created from the ResultSet data
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static List<Admin> getAllAdminsFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to List<Admin>");
        List<Admin> admins = new ArrayList<>();
        while (resultSet.next()) {
            admins.add(new Admin(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email")));
        }
        return admins;
    }

    /**
     * Creates an <code>Optional<Admin></code> from a <code>ResultSet</code> row
     * @param resultSet The ResultSet containing admin data
     * @return An Optional containing an Admin object if data is present, or an empty Optional if not
     * @throws SQLException if there is an error accessing the ResultSet data
     */
    private static Optional<Admin> getAdminFromResultSet(final ResultSet resultSet) throws SQLException {
        LOGGER.debug("Converting ResultSet to Optional<Admin>");
        if (resultSet.next()) {
            return Optional.of(new Admin(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email")));
        }
        LOGGER.info("No admin found in ResultSet");
        return Optional.empty();
    }

    /**
     * Adds an admin to the database with the given password.
     * @param admin the admin to add
     * @param hashedPassword the hashed password of the admin
     * @throws SQLException if a database access error occurs or the connection is closed
     */
    public static void addAdmin(final Admin admin, final String hashedPassword) throws SQLException {
        LOGGER.debug("Adding admin to database {}", admin);
        final String sql = "INSERT INTO admins (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement addSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            addSqlStatement.setString(1, admin.getFirstName());
            addSqlStatement.setString(2, admin.getLastName());
            addSqlStatement.setString(3, admin.getEmail());
            addSqlStatement.setString(4, hashedPassword);
            addSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to add admin to database {}", admin, e);
            throw new SQLException(String.format("Failed to add admin: %s", admin.toString()), e);
        }
    }

    /**
     * Finds an admin by its ID
     * @param id The ID of the admin to find
     * @return An Optional containing the Admin object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<Admin> findById(final int id) throws SQLException {
        LOGGER.debug("Finding admin by ID: {}", id);
        final String sql = "SELECT * FROM admins WHERE id = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setInt(1, id);
            ResultSet results = findSqlStatement.executeQuery();
            return getAdminFromResultSet(results);
        } catch (SQLException e) {
            LOGGER.error("Failed to find admin by ID: {}", id, e);
            throw new SQLException(String.format("Failed to find admin with Id: %d", id), e);
        }
    }

    /**
     * Finds an admin by its email address
     * @param email The email address of the admin to find
     * @return An Optional containing the Admin object if found, or an empty Optional if not found
     * @throws SQLException if there is an error executing the query
     */
    public static Optional<Admin> findByEmail(final String email) throws SQLException {
        LOGGER.debug("Finding admin by email: {}", email);
        final String sql = "SELECT * FROM admins WHERE email = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setString(1, email);
            ResultSet results = findSqlStatement.executeQuery();
            return getAdminFromResultSet(results);
        } catch (SQLException e) {
            LOGGER.error("Failed to find admin by email: {}", email, e);
            throw new SQLException(String.format("Failed to find admin with email: %s", email), e);
        }
    }

    /**
     * Retrieves all admins from the database
     * @return A List containing all Admin objects in the database
     * @throws SQLException if there is an error executing the query
     */
    public static List<Admin> findAll() throws SQLException {
        LOGGER.debug("Finding all admins");
        final String sql = "SELECT * FROM admins";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet results = findSqlStatement.executeQuery();
            return getAllAdminsFromResultSet(results);
        } catch (SQLException e) {
            LOGGER.error("Failed to find all admins", e);
            throw new SQLException("Failed to find all admins", e);
        }
    }

    /**
     * Updates an admin in the database
     * @param admin The admin object with updated information
     * @return The number of rows affected (1 if successful, 0 if admin not found)
     * @throws SQLException if there is an error executing the update operation
     */
    public static int update(final Admin admin) throws SQLException {
        LOGGER.debug("Updating admin: {}", admin);
        final String sql = "UPDATE admins SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
        try (PreparedStatement updateSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            updateSqlStatement.setString(1, admin.getFirstName());
            updateSqlStatement.setString(2, admin.getLastName());
            updateSqlStatement.setString(3, admin.getEmail());
            updateSqlStatement.setInt(4, admin.getId());
            return updateSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update admin: {}", admin, e);
            throw new SQLException(String.format("Failed to update admin with Id: %d", admin.getId()), e);
        }
    }

    /**
     * Deletes an admin from the database by its ID
     * @param id The ID of the admin to delete
     * @return The number of rows affected (1 if successful, 0 if admin not found)
     * @throws SQLException if there is an error executing the delete operation
     */
    public static int delete(final int id) throws SQLException {
        LOGGER.debug("Deleting admin with ID: {}", id);
        final String sql = "DELETE FROM admins WHERE id = ?";
        try (PreparedStatement deleteSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, id);
            return deleteSqlStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete admin with ID: {}", id, e);
            throw new SQLException(String.format("Failed to delete admin with Id: %d", id), e);
        }
    }

    /**
     * Verifies the password for an admin by their email address
     * @param email The email address of the admin
     * @param hashedPassword The hashed password to verify
     * @return true if the password matches, false otherwise
     * @throws SQLException if there is an error executing the query
     */
    public static boolean verifyPassword(final String email, final String hashedPassword) throws SQLException {
        LOGGER.debug("Verifying password for admin with email: {}", email);
        final String sql = "SELECT password FROM admins WHERE email = ?";
        try (PreparedStatement findSqlStatement =
                DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            findSqlStatement.setString(1, email);
            ResultSet results = findSqlStatement.executeQuery();
            if (results.next()) {
                LOGGER.debug("Found admin with email: {}", email);
                String storedPassword = results.getString("password");
                return hashedPassword.equals(storedPassword);
            }
            return false;
        } catch (SQLException e) {
            LOGGER.error("Failed to verify password for admin with email: {}", email, e);
            throw new SQLException(String.format("Failed to verify password for admin with email: %s", email), e);
        }
    }
}
