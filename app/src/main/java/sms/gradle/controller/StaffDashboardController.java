package sms.gradle.controller;

import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.view.ViewFactory;

public final class StaffDashboardController {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void handleViewCourseButton(ActionEvent event) {
        LOGGER.debug("View Course button clicked");
        // TODO - Once view courses page has been implemented add call to change to
    }

    public static void handleManageAssessmentsButton(ActionEvent event) {
        LOGGER.debug("Manage Assessments button clicked");
        // TODO - Once Manage assessments page has been implemented add call to change to
    }

    public static void handleManageModulesButton(ActionEvent event) {
        LOGGER.debug("Manage Modules button clicked");
        // TODO - Once Manage modules page has been implemented add call to change to
    }

    public static void handleManageCoursesButton(ActionEvent event) {
        LOGGER.debug("Manage Courses button clicked");
        ViewFactory.getInstance().changeToManageCourseStage();
    }

    public static void handleManageStudentsButton(ActionEvent event) {
        LOGGER.debug("Manage Students button clicked");
        ViewFactory.getInstance().changeToManageStudentStage();
    }

    public static void handleManageAdminButton(ActionEvent event) {
        LOGGER.debug("Manage Admin button clicked");
        ViewFactory.getInstance().changeToManageAdminStage();
    }

    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logout button clicked");
        ViewFactory.getInstance().changeToLoginStage();
    }
}
