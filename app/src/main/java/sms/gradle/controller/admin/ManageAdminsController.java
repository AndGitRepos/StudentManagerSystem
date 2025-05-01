package sms.gradle.controller.admin;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AdminDAO;
import sms.gradle.model.entities.Admin;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;

public final class ManageAdminsController {

    private static final Logger LOGGER = LogManager.getLogger();

    private ManageAdminsController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getManageAdminStage();
    }

    private static boolean verifyAdminFields() {
        LOGGER.debug("Verifying admin fields");
        final TextField adminFirstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField adminLastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final TextField adminEmailField = Common.getNode(getViewStage(), "#adminEmailField");

        return !adminFirstNameField.getText().isEmpty()
                && !adminLastNameField.getText().isEmpty()
                && !adminEmailField.getText().isEmpty();
    }

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

    public static void selectAdmin(ActionEvent event) {
        LOGGER.debug("Selecting admin");
        final ListView<Admin> adminListView = Common.getNode(getViewStage(), "#adminListView");

        final Admin selectedAdmin = adminListView.getSelectionModel().getSelectedItem();
        populateFormFields(selectedAdmin);
    }

    public static void refreshListOfAdmins(ActionEvent event) {
        LOGGER.debug("Updating & Refreshing List Of Admins");
        final ListView<Admin> adminListView = Common.getNode(getViewStage(), "#adminListView");

        adminListView.getItems().clear();

        try {
            AdminDAO.findAll().forEach(admin -> adminListView.getItems().add(admin));
        } catch (SQLException e) {
            LOGGER.info("Failed to update & refresh admins list", e);
        }
    }

    public static void createNewAdmin(ActionEvent event) {
        LOGGER.debug("Creating new Admin member");

        if (!verifyAdminFields()) {
            LOGGER.info("User did not complete all fields. Admin Creation Terminated");
            return;
        }

        final TextField adminFirstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField adminLastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final TextField adminEmailField = Common.getNode(getViewStage(), "#adminEmailField");
        final PasswordField adminPasswordField = Common.getNode(getViewStage(), "#passwordField");

        try {
            final Admin newAdmin = new Admin(
                    0, adminFirstNameField.getText(), adminLastNameField.getText(), adminEmailField.getText());

            AdminDAO.addAdmin(newAdmin, Common.generateSha256Hash(adminPasswordField.getText()));

            refreshListOfAdmins(event);
            clearFields();

            LOGGER.debug("Created new admin: {}", newAdmin);
        } catch (SQLException e) {
            LOGGER.info("Failed in creating new admin: ", e);
        }
    }

    public static void updateAdmin(ActionEvent event) {
        LOGGER.debug("Updating selected Admin member");

        if (!verifyAdminFields()) {
            LOGGER.info("User did not complete all fields. Admin Creation Terminated");
            return;
        }

        final TextField adminFirstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField adminLastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final TextField adminEmailField = Common.getNode(getViewStage(), "#adminEmailField");
        final PasswordField adminPasswordField = Common.getNode(getViewStage(), "#passwordField");
        final TextField adminIdField = Common.getNode(getViewStage(), "#adminIdField");

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
        }
    }

    public static void deleteAdmin(ActionEvent event) {
        LOGGER.debug("Delete selected Admin member");

        final ListView<Admin> adminList = Common.getNode(getViewStage(), "#adminListView");
        final Admin selectedAdmin = adminList.getSelectionModel().getSelectedItem();

        if (selectedAdmin == null) {
            LOGGER.info("No admin selected for deletion. Aborting");
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
                    LOGGER.debug("Deleted chosen admin: {}", selectedAdmin);
                } catch (SQLException e) {
                    LOGGER.info("Failed to delete chosen admin: ", e);
                }

            } else {
                LOGGER.info("Admin deletion request has been cancelled");
            }
        });
    }

    public static void handleBack(ActionEvent event) {
        LOGGER.debug("Returning to Main Admin Dashboard");
        ViewFactory.getInstance().getManageAdminStage().hide();
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }

    public static void handleLogout(ActionEvent event) {
        LOGGER.debug("Logging Out - returning to LogIn Page");
        ViewFactory.getInstance().getManageAdminStage().hide();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
