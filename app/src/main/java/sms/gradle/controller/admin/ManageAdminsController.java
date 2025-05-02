package sms.gradle.controller.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AdminDAO;
import sms.gradle.model.entities.Admin;
import sms.gradle.utils.Common;
import sms.gradle.utils.checks.ChecksProcessor;
import sms.gradle.utils.checks.NodeValidator;
import sms.gradle.utils.checks.textfield.MinLengthCheck;
import sms.gradle.view.ViewFactory;

public final class ManageAdminsController {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final NodeValidator idValidator =
            new NodeValidator("Id", "#adminIdField", List.of(new MinLengthCheck(1)), List.of());

    private static final List<NodeValidator> nodeValidators = List.of(
            new NodeValidator("First Name", "#firstNameField", List.of(new MinLengthCheck(1)), List.of()),
            new NodeValidator("Last Name", "#lastNameField", List.of(new MinLengthCheck(1)), List.of()),
            new NodeValidator("Email", "#adminEmailField", List.of(new MinLengthCheck(1)), List.of()),
            new NodeValidator("Password", "#passwordField", List.of(new MinLengthCheck(1)), List.of()));

    private ManageAdminsController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getManageAdminStage();
    }

    /**
     * Populates the admin form fields with data from the selected admin.
     * Sets the ID, email, first name, and last name fields based on the admin object.
     * Clears the password field for security.
     *
     * @param selectedAdmin The Admin object containing the data to populate the form with.
     *                     If null, no fields will be populated.
     */
    private static void populateFormFields(Admin selectedAdmin) {
        final TextField adminFirstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField adminLastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final TextField adminEmailField = Common.getNode(getViewStage(), "#adminEmailField");
        final PasswordField adminPasswordField = Common.getNode(getViewStage(), "#passwordField");
        final TextField adminIdField = Common.getNode(getViewStage(), "#adminIdField");

        if (selectedAdmin != null) {
            LOGGER.debug("Populating field for admin: {}", selectedAdmin);
            adminIdField.setText(String.valueOf(selectedAdmin.getId()));
            adminEmailField.setText(selectedAdmin.getEmail());
            adminPasswordField.clear();
            adminFirstNameField.setText(selectedAdmin.getFirstName());
            adminLastNameField.setText(selectedAdmin.getLastName());
        }
    }

    /**
     * Clears all admin form fields.
     * Clears the values of the ID, first name, last name, email, and password fields.
     */
    private static void clearFields() {
        LOGGER.debug("Clearing Fields");
        final TextField adminFirstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField adminLastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final TextField adminEmailField = Common.getNode(getViewStage(), "#adminEmailField");
        final PasswordField adminPasswordField = Common.getNode(getViewStage(), "#passwordField");
        final TextField adminIdField = Common.getNode(getViewStage(), "#adminIdField");

        adminIdField.clear();
        adminFirstNameField.clear();
        adminLastNameField.clear();
        adminEmailField.clear();
        adminPasswordField.clear();
    }

    /**
     * Event handler for selecting an admin from the list view.
     * Populates the admin form fields with the selected admin's data.
     *
     * @param event The action event that triggered this method.
     */
    public static void selectAdmin(ActionEvent event) {
        LOGGER.debug("Selecting admin");
        final ListView<Admin> adminListView = Common.getNode(getViewStage(), "#adminListView");

        final Admin selectedAdmin = adminListView.getSelectionModel().getSelectedItem();
        populateFormFields(selectedAdmin);
    }

    /**
     * Event handler for refreshing the list of admins.
     * Clears the admin list view and fetches the latest list of admins from the database.
     *
     * @param event The action event that triggered this method.
     */
    public static void refreshListOfAdmins(ActionEvent event) {
        LOGGER.debug("Updating & Refreshing List Of Admins");
        final ListView<Admin> adminListView = Common.getNode(getViewStage(), "#adminListView");

        adminListView.getItems().clear();

        try {
            AdminDAO.findAll().forEach(admin -> adminListView.getItems().add(admin));
        } catch (SQLException e) {
            LOGGER.info("Failed to update & refresh admins list", e);
            Common.showAlert("An error occurred", "Failed to update & refresh admins list");
        }
    }

    /**
     * Event handler for creating a new admin.
     * Verifies that all required fields are filled out.
     * If verified, creates a new admin object and adds it to the database.
     * Refreshes the admin list view and clears the form fields.
     *
     * @param event The action event that triggered this method.
     */
    public static void createNewAdmin(ActionEvent event) {
        LOGGER.debug("Creating new Admin member");

        final TextField adminFirstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField adminLastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final TextField adminEmailField = Common.getNode(getViewStage(), "#adminEmailField");
        final PasswordField adminPasswordField = Common.getNode(getViewStage(), "#passwordField");

        if (!ChecksProcessor.checkValidationError(getViewStage(), nodeValidators)) {
            return;
        }

        try {
            final Admin newAdmin = new Admin(
                    0, adminFirstNameField.getText(), adminLastNameField.getText(), adminEmailField.getText());

            AdminDAO.addAdmin(newAdmin, Common.generateSha256Hash(adminPasswordField.getText()));

            refreshListOfAdmins(event);
            clearFields();

            LOGGER.debug("Created new admin: {}", newAdmin);
        } catch (SQLException e) {
            LOGGER.info("Failed to create new admin: ", e);
            Common.showAlert("Failed to create new admin", e.getMessage());
        }
    }

    /**
     * Event handler for updating an existing admin.
     * Verifies that all required fields are filled out.
     * If verified, updates the selected admin with the new data and refreshes the list view.
     *
     * @param event The action event that triggered this method.
     */
    public static void updateAdmin(ActionEvent event) {
        LOGGER.debug("Updating selected Admin member");

        final TextField adminIdField = Common.getNode(getViewStage(), "#adminIdField");
        final TextField adminFirstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField adminLastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final TextField adminEmailField = Common.getNode(getViewStage(), "#adminEmailField");
        final PasswordField adminPasswordField = Common.getNode(getViewStage(), "#passwordField");

        List<NodeValidator> mergedValidators = List.of(idValidator).stream().collect(Collectors.toList());
        mergedValidators.addAll(nodeValidators);

        if (!ChecksProcessor.checkValidationError(getViewStage(), mergedValidators)) {
            return;
        }

        try {
            final Admin updatedAdmin = new Admin(
                    Integer.parseInt(adminIdField.getText()),
                    adminFirstNameField.getText(),
                    adminLastNameField.getText(),
                    adminEmailField.getText());

            AdminDAO.update(updatedAdmin, Common.generateSha256Hash(adminPasswordField.getText()));
            refreshListOfAdmins(event);

            LOGGER.debug("Updated admin: {}", updatedAdmin);
        } catch (SQLException e) {
            LOGGER.info("Failed in updating chosen admin: ", e);
            Common.showAlert("An error occurred", "Failed to update the chosen admin");
        }
    }

    /**
     * Event handler for deleting an admin.
     * Prompts the user for confirmation before deleting the selected admin.
     * If confirmed, deletes the admin from the database and refreshes the list view.
     *
     * @param event The action event that triggered this method.
     */
    public static void deleteAdmin(ActionEvent event) {
        LOGGER.debug("Delete selected Admin member");

        final ListView<Admin> adminList = Common.getNode(getViewStage(), "#adminListView");
        final Admin selectedAdmin = adminList.getSelectionModel().getSelectedItem();

        if (selectedAdmin == null) {
            LOGGER.info("No admin selected for deletion. Aborting");
            Common.showAlert("Warning", "Please highlight an admin to delete");
            return;
        }

        Alert deletionConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        deletionConfirmation.setTitle("Deletion Confirmation");
        deletionConfirmation.setHeaderText("Deleting Admin?");
        deletionConfirmation.setContentText("Are you sure you would like to delete admin: "
                + selectedAdmin.getFirstName().toUpperCase() + " "
                + selectedAdmin.getLastName().toUpperCase() + "?");
        deletionConfirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    AdminDAO.delete(selectedAdmin.getId());
                    adminList.getItems().remove(selectedAdmin);
                    refreshListOfAdmins(event);

                    clearFields();
                    LOGGER.debug("Deleted selected admin: {}", selectedAdmin);
                } catch (SQLException e) {
                    LOGGER.info("Failed to delete selected admin: ", e);
                    Common.showAlert("An error occurred", "Failed to delete selected admin");
                }

            } else {
                LOGGER.info("Admin deletion request has been cancelled");
            }
        });
    }

    /**
     * Event handler for handling the window show event.
     * Refreshes the list of admins when the manage admin stage is shown.
     *
     * @param event The window event that triggered this method.
     */
    public static void handleOnShowEvent(WindowEvent event) {
        refreshListOfAdmins(new ActionEvent());
    }

    /**
     * Event handler for navigating back to the admin dashboard.
     * Hides the current stage and changes the view to the admin dashboard.
     *
     * @param event The action event that triggered this method.
     */
    public static void handleBack(ActionEvent event) {
        LOGGER.debug("Returning to Main Admin Dashboard");
        ViewFactory.getInstance().getManageAdminStage().hide();
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }

    /**
     * Event handler for logging out and returning to the login stage.
     * Hides the current stage and changes the view to the login stage.
     *
     * @param event The action event that triggered this method.
     */
    public static void handleLogout(ActionEvent event) {
        LOGGER.debug("Logging Out - returning to Login Page");
        ViewFactory.getInstance().getManageAdminStage().hide();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
