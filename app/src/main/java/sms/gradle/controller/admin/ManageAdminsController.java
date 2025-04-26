package sms.gradle.controller.admin;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AdminDAO;
import sms.gradle.model.entities.Admin;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;

public final class ManageAdminsController {

    private static final Logger LOGGER = LogManager.getLogger();

    private static TextField getAdminIdField() {
        return (TextField)
                ViewFactory.getInstance().getManageAdminStage().getScene().lookup("#adminIdField");
    }

    private static TextField getAdminEmailField() {
        return (TextField)
                ViewFactory.getInstance().getManageAdminStage().getScene().lookup("#adminEmailField");
    }

    private static PasswordField getAdminPasswordField() {
        return (PasswordField)
                ViewFactory.getInstance().getManageAdminStage().getScene().lookup("#passwordField");
    }

    private static TextField getAdminFirstNameField() {
        return (TextField)
                ViewFactory.getInstance().getManageAdminStage().getScene().lookup("#firstNameField");
    }

    private static TextField getAdminLastNameField() {
        return (TextField)
                ViewFactory.getInstance().getManageAdminStage().getScene().lookup("#lastNameField");
    }

    private static boolean verifyAdminFields() {
        return !getAdminFirstNameField().getText().isEmpty()
                && !getAdminLastNameField().getText().isEmpty()
                && !getAdminEmailField().getText().isEmpty();
    }

    private static void populateFormFields(Admin selectedAdmin) {

        if (selectedAdmin != null) {
            LOGGER.debug("Populating field for admin: {}", selectedAdmin);
            getAdminIdField().setText(String.valueOf(selectedAdmin.getId()));
            getAdminEmailField().setText(selectedAdmin.getEmail());
            getAdminPasswordField().clear();
            getAdminFirstNameField().setText(selectedAdmin.getFirstName());
            getAdminLastNameField().setText(selectedAdmin.getLastName());
        }
    }

    private static void clearFields() {
        LOGGER.debug("Clearing Fields");
        getAdminIdField().clear();
        getAdminEmailField().clear();
        getAdminPasswordField().clear();
        getAdminFirstNameField().clear();
        getAdminLastNameField().clear();
    }

    public static void selectAdmin(ActionEvent event) {
        LOGGER.debug("Selecting admin");
        ListView<Admin> adminListView = (ListView<Admin>)
                ViewFactory.getInstance().getManageAdminStage().getScene().lookup("#adminListView");

        Admin selectedAdmin = adminListView.getSelectionModel().getSelectedItem();
        populateFormFields(selectedAdmin);
    }

    public static void refreshListOfAdmins(ActionEvent event) {

        LOGGER.debug("Updating & Refreshing List Of Admins");
        ListView<Admin> adminListView = (ListView<Admin>)
                ViewFactory.getInstance().getManageAdminStage().getScene().lookup("#adminListView");

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

        try {
            Admin newAdmin = new Admin(
                    0,
                    getAdminFirstNameField().getText(),
                    getAdminLastNameField().getText(),
                    getAdminEmailField().getText());

            AdminDAO.addAdmin(
                    newAdmin, Common.generateSha256Hash(getAdminPasswordField().getText()));

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

        try {
            Admin updatedAdmin = new Admin(
                    Integer.parseInt(getAdminIdField().getText()),
                    getAdminFirstNameField().getText(),
                    getAdminLastNameField().getText(),
                    getAdminEmailField().getText());

            AdminDAO.update(updatedAdmin);
            refreshListOfAdmins(event);

            LOGGER.debug("Updated admin: {}", updatedAdmin);
        } catch (SQLException e) {
            LOGGER.info("Failed in updating chosen admin: ", e);
        }
    }

    public static void deleteAdmin(ActionEvent event) {
        LOGGER.debug("Delete selected Admin member");

        ListView<Admin> adminList = (ListView<Admin>)
                ViewFactory.getInstance().getManageAdminStage().getScene().lookup("#adminListView");
        Admin selectedAdmin = adminList.getSelectionModel().getSelectedItem();

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
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }

    public static void handleLogout(ActionEvent event) {
        LOGGER.debug("Logging Out - returning to LogIn Page");
        ViewFactory.getInstance().getManageAdminStage().hide();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
