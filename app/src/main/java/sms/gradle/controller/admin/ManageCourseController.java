package sms.gradle.controller.admin;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.entities.Course;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;

public final class ManageCourseController {
    private static final Logger LOGGER = LogManager.getLogger();

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getManageCourseStage();
    }

    private static void populateFormFields(Course selectedCourse) {

        if (selectedCourse != null) {
            LOGGER.debug("Populating field for course: {}", selectedCourse);
            TextField courseIdField = Common.getNode(getViewStage(), "#courseIdField");
            TextField courseNameField = Common.getNode(getViewStage(), "#courseNameField");
            TextArea courseDescriptionArea = Common.getNode(getViewStage(), "#courseDescriptionArea");

            courseIdField.setText(Integer.toString(selectedCourse.getId()));
            courseNameField.setText(selectedCourse.getName());
            courseDescriptionArea.setText(selectedCourse.getDescription());
        }
    }

    private static void clearFields() {
        LOGGER.debug("Clearing Fields");
        TextField courseIdField = Common.getNode(getViewStage(), "#courseIdField");
        TextField courseNameField = Common.getNode(getViewStage(), "#courseNameField");
        TextArea courseDescriptionArea = Common.getNode(getViewStage(), "#courseDescriptionArea");
        courseIdField.clear();
        courseNameField.clear();
        courseDescriptionArea.clear();
    }

    public static void selectCourse(ActionEvent event) {
        LOGGER.debug("Selecting course");
        ListView<Course> courseListView = Common.getNode(getViewStage(), "#courseListView");

        Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        populateFormFields(selectedCourse);
    }

    public static void refreshListOfCourses(ActionEvent event) {

        LOGGER.debug("Updating & Refreshing List Of Courses");
        ListView<Course> courseListView = Common.getNode(getViewStage(), "#courseListView");

        courseListView.getItems().clear();

        try {
            CourseDAO.findAll().forEach(course -> courseListView.getItems().add(course));
        } catch (SQLException e) {
            LOGGER.error("Failed to update & refresh courses list", e);
        }
    }

    public static void createNewCourse(ActionEvent event) {
        LOGGER.debug("Creating new Course member");
        TextField courseNameField = Common.getNode(getViewStage(), "#courseNameField");
        TextArea courseDescriptionArea = Common.getNode(getViewStage(), "#courseDescriptionArea");

        Course newCourse = new Course(0, courseNameField.getText(), courseDescriptionArea.getText());

        try {
            CourseDAO.addCourse(newCourse);

            refreshListOfCourses(event);
            clearFields();

            LOGGER.debug("Created new course: {}", newCourse);
        } catch (SQLException e) {
            LOGGER.error("Failed to create a new course: ", e);
        }
    }

    public static void updateCourse(ActionEvent event) {
        LOGGER.debug("Updating selected Course member");
        TextField courseIdField = Common.getNode(getViewStage(), "#courseIdField");
        TextField courseNameField = Common.getNode(getViewStage(), "#courseNameField");
        TextArea courseDescriptionArea = Common.getNode(getViewStage(), "#courseDescriptionArea");

        Course updatedCourse = new Course(
                Integer.parseInt(courseIdField.getText()), courseNameField.getText(), courseDescriptionArea.getText());

        try {
            CourseDAO.update(updatedCourse);
            refreshListOfCourses(event);

            LOGGER.debug("Updated course: {}", updatedCourse);
        } catch (SQLException e) {
            LOGGER.error("Failed in updating chosen course: ", e);
        }
    }

    public static void deleteCourse(ActionEvent event) {
        LOGGER.debug("Delete selected Course member");

        ListView<Course> courseList = Common.getNode(getViewStage(), "#courseListView");
        Course selectedCourse = courseList.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            LOGGER.info("No course selected. Stopping deletion attempt");
            return;
        }

        Alert deletionConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        deletionConfirmation.setTitle("Deletion Confirmation");
        deletionConfirmation.setHeaderText("Deleting Course?");
        deletionConfirmation.setContentText("Are you sure you would like to delete course: "
                + selectedCourse.getName().toUpperCase() + "?");
        deletionConfirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    CourseDAO.delete(selectedCourse.getId());
                    courseList.getItems().remove(selectedCourse);
                    refreshListOfCourses(event);

                    clearFields();
                    LOGGER.debug("Deleted chosen course: {}", selectedCourse);
                } catch (SQLException e) {
                    LOGGER.error("Failed to delete chosen course: ", e);
                }

            } else {
                LOGGER.info("Course deletion request has been cancelled");
            }
        });
    }

    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Returning to Main Course Dashboard");
        ViewFactory.getInstance().getManageCourseStage().hide();
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }

    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logging Out - returning to Login Page");
        ViewFactory.getInstance().getManageCourseStage().hide();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
