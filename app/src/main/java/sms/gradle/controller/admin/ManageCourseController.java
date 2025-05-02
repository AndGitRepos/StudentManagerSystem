package sms.gradle.controller.admin;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

    /**
     * Populates form fields with data from the selected course.
     * Retrieves the course ID, name, and description from the selected course object
     * and sets them in the corresponding UI text fields.
     *
     * @param selectedCourse The Course object containing the data to populate the form fields with.
     *                      If null, no action is taken.
     */
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

    /**
     * Clears all input fields in the course management form.
     * Specifically clears:
     * - Course ID field
     * - Course name field
     * - Course description text area
     */
    private static void clearFields() {
        LOGGER.debug("Clearing Fields");
        TextField courseIdField = Common.getNode(getViewStage(), "#courseIdField");
        TextField courseNameField = Common.getNode(getViewStage(), "#courseNameField");
        TextArea courseDescriptionArea = Common.getNode(getViewStage(), "#courseDescriptionArea");
        courseIdField.clear();
        courseNameField.clear();
        courseDescriptionArea.clear();
    }

    /**
     * Event handler for selecting a course from the list view.
     * Populates the course form fields with the selected course's data.
     *
     * @param event The action event that triggered this method.
     */
    public static void selectCourse(ActionEvent event) {
        LOGGER.debug("Selecting course");
        ListView<Course> courseListView = Common.getNode(getViewStage(), "#courseListView");

        Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        populateFormFields(selectedCourse);
    }

    /**
     * Refreshes the list of courses displayed in the course list view.
     * Clears the current list and repopulates it with all courses from the database.
     *
     * @param event The action event that triggered this method
     */
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

    /**
     * Event handler for creating a new course.
     * Retrieves the course name and description from the form fields,
     * creates a new Course object, and adds it to the database.
     * Refreshes the course list view and clears the form fields.
     *
     * @param event The action event that triggered this method.
     */
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

    /**
     * Event handler for updating an existing course.
     * Retrieves the course ID, name, and description from the form fields,
     * creates a new Course object, and updates it in the database.
     * Refreshes the course list view and clears the form fields.
     *
     * @param event The action event that triggered this method.
     */
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

    /**
     * Event handler for deleting a course.
     * Prompts the user for confirmation before deleting the selected course.
     * If confirmed, deletes the course from the database and refreshes the course list view.
     *
     * @param event The action event that triggered this method.
     */
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

    /**
     * Event handler for the "On Show" event of the course management stage.
     * Refreshes the list of courses when the stage is shown.
     *
     * @param event The window event that triggered this method.
     */
    public static void handleOnShowEvent(WindowEvent event) {
        refreshListOfCourses(new ActionEvent());
    }

    /**
     * Event handler for navigating back to the admin dashboard.
     * Hides the current stage and changes the view to the admin dashboard.
     *
     * @param event The action event that triggered this method.
     */
    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Returning to Main Course Dashboard");
        ViewFactory.getInstance().getManageCourseStage().hide();
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }

    /**
     * Event handler for logging out and returning to the login stage.
     * Hides the current stage and changes the view to the login stage.
     *
     * @param event The action event that triggered this method.
     */
    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logging Out - returning to Login Page");
        ViewFactory.getInstance().getManageCourseStage().hide();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
