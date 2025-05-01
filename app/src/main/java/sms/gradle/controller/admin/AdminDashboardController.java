package sms.gradle.controller.admin;

import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.view.ViewFactory;

public final class AdminDashboardController {
    private static final Logger LOGGER = LogManager.getLogger();

    private AdminDashboardController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
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
