package sms.gradle.controller.admin;

import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.view.ViewFactory;

public final class CourseDetailViewController {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void handleViewAssessmentDetailButton(ActionEvent event) {
        LOGGER.debug("View Assessment button clicked");
        // TODO - Once assessment detail page has been implemented add call to change to
    }

    public static void handleViewModuleDetailButton(ActionEvent event) {
        LOGGER.debug("View Module button clicked");
        // TODO - Once module detail page has been implemented add call to change to
    }

    public static void handleViewStudentDetailButton(ActionEvent event) {
        LOGGER.debug("View Student button clicked");
        // TODO - Once module detail page has been implemented add call to change to
    }

    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logout button clicked");
        ViewFactory.getInstance().changeToLoginStage();
    }

    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Back button clicked");
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }
}
