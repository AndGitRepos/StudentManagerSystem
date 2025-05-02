package sms.gradle.controller.admin;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.entities.Course;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;
import sms.gradle.view.frames.admin.CourseDetailView;

public final class AdminDashboardController {
    private static final Logger LOGGER = LogManager.getLogger();

    private AdminDashboardController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    /**
     * Refreshes the list of courses displayed in the admin dashboard.
     * This method clears the current items in the courseListView and repopulates it
     * with all courses from the database.
     *
     * @param event The ActionEvent that triggered this method call
     */
    public static void refreshListOfCourses(ActionEvent event) {
        LOGGER.debug("Refreshing List Of Courses");
        ListView<Course> courseListView =
                Common.getNode(ViewFactory.getInstance().getAdminDashboardStage(), "#courseListView");
        courseListView.getItems().clear();
        try {
            courseListView.getItems().addAll(CourseDAO.findAll());
        } catch (SQLException e) {
            LOGGER.error("Failed to update list of courses: ", e);
            Common.showAlert("An error occurred", "We had a problem loading the courses. Please try again.");
        }
    }

    /**
     * Handles the event when the admin dashboard is shown.
     * This method is called when the admin dashboard stage is displayed,
     * and it refreshes the list of courses displayed.
     *
     * @param event The WindowEvent that triggered this method call
     */
    public static void handleOnShowEvent(WindowEvent event) {
        refreshListOfCourses(new ActionEvent());
    }

    /**
     * Hides the admin dashboard stage.
     * This method is used to hide the admin dashboard stage from the user interface.
     */
    private static void hideAdminDashboard() {
        LOGGER.debug("Hiding Admin Dashboard");
        ViewFactory.getInstance().getAdminDashboardStage().hide();
    }

    /**
     * Handles the event when the View Course button is clicked.
     * This method hides the admin dashboard and changes the view to the course detail stage.
     *
     * @param event The ActionEvent that triggered this method call
     */
    public static void handleViewCourseButton(ActionEvent event) {
        LOGGER.debug("View Course button clicked");

        ListView<Course> courseListView =
                Common.getNode(ViewFactory.getInstance().getAdminDashboardStage(), "#courseListView");
        Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            LOGGER.debug("Selected course: {} (ID: {})", selectedCourse.getName(), selectedCourse.getId());
            hideAdminDashboard();

            CourseDetailView courseDetailView = (CourseDetailView)
                    ViewFactory.getInstance().getCourseDetailStage().getScene().getRoot();
            courseDetailView.setCourseId(selectedCourse.getId());

            ViewFactory.getInstance().changeToCourseDetailStage();
        } else {
            LOGGER.warn("No course selected");
            // Show an alert or message to the user that they need to select a course
            Common.showAlert("No Course Selected", "Please select a course to view.");
        }
    }

    /**
     * Handles the event when the Manage Assessments button is clicked.
     * This method hides the admin dashboard and changes the view to the manage assessments stage.
     *
     * @param event The ActionEvent that triggered this method call
     */
    public static void handleManageAssessmentsButton(ActionEvent event) {
        LOGGER.debug("Manage Assessments button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageAssessmentsStage();
    }

    /**
     * Handles the event when the Manage Modules button is clicked.
     * This method hides the admin dashboard and changes the view to the manage modules stage.
     *
     * @param event The ActionEvent that triggered this method call
     */
    public static void handleManageModulesButton(ActionEvent event) {
        LOGGER.debug("Manage Modules button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageModulesStage();
    }

    /**
     * Handles the event when the Manage Courses button is clicked.
     * This method hides the admin dashboard and changes the view to the manage courses stage.
     *
     * @param event The ActionEvent that triggered this method call
     */
    public static void handleManageCoursesButton(ActionEvent event) {
        LOGGER.debug("Manage Courses button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageCourseStage();
    }

    /**
     * Handles the event when the Manage Students button is clicked.
     * This method hides the admin dashboard and changes the view to the manage students stage.
     *
     * @param event The ActionEvent that triggered this method call
     */
    public static void handleManageStudentsButton(ActionEvent event) {
        LOGGER.debug("Manage Students button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageStudentStage();
    }

    /**
     * Handles the event when the Manage Admin button is clicked.
     * This method hides the admin dashboard and changes the view to the manage admin stage.
     *
     * @param event The ActionEvent that triggered this method call
     */
    public static void handleManageAdminButton(ActionEvent event) {
        LOGGER.debug("Manage Admin button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageAdminStage();
    }

    /**
     * Handles the event when the Logout button is clicked.
     * This method hides the admin dashboard and changes the view to the login stage.
     *
     * @param event The ActionEvent that triggered this method call
     */
    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logout button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
