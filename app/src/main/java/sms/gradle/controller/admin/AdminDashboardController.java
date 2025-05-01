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

public final class AdminDashboardController {
    private static final Logger LOGGER = LogManager.getLogger();

    private AdminDashboardController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    public static void refreshListOfCourses(ActionEvent event) {
        LOGGER.debug("Refreshing List Of Courses");
        ListView<Course> courseListView =
                Common.getNode(ViewFactory.getInstance().getAdminDashboardStage(), "#courseListView");
        courseListView.getItems().clear();
        try {
            courseListView.getItems().addAll(CourseDAO.findAll());
        } catch (SQLException e) {
            LOGGER.error("Failed to update list of courses: ", e);
        }
    }

    public static void handleOnShowEvent(WindowEvent event) {
        refreshListOfCourses(new ActionEvent());
    }

    private static void hideAdminDashboard() {
        LOGGER.debug("Hiding Admin Dashboard");
        ViewFactory.getInstance().getAdminDashboardStage().hide();
    }

    public static void handleViewCourseButton(ActionEvent event) {
        LOGGER.debug("View Course button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToCourseDetailStage();
    }

    public static void handleManageAssessmentsButton(ActionEvent event) {
        LOGGER.debug("Manage Assessments button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageAssessmentsStage();
    }

    public static void handleManageModulesButton(ActionEvent event) {
        LOGGER.debug("Manage Modules button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageModulesStage();
    }

    public static void handleManageCoursesButton(ActionEvent event) {
        LOGGER.debug("Manage Courses button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageCourseStage();
    }

    public static void handleManageStudentsButton(ActionEvent event) {
        LOGGER.debug("Manage Students button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageStudentStage();
    }

    public static void handleManageAdminButton(ActionEvent event) {
        LOGGER.debug("Manage Admin button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToManageAdminStage();
    }

    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logout button clicked");
        hideAdminDashboard();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
