package sms.gradle.controller.admin;

import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.view.ViewFactory;

public final class CourseDetailViewController {
    private static final Logger LOGGER = LogManager.getLogger();

    private CourseDetailViewController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    /**
     * Hides the course detail stage/window by calling the hide() method on the
     * CourseDetailStage instance obtained from the ViewFactory.
     */
    private static void hideCourseDetail() {
        ViewFactory.getInstance().getCourseDetailStage().hide();
    }

    /**
     * Handles the event when the View Assessment Detail button is clicked.
     * This method is responsible for displaying the assessment detail view.
     * Currently, it is not implemented and needs to be updated to handle the
     * assessment detail view display logic.
     *
     * @param event The ActionEvent that triggered this method call
     */
    @SuppressWarnings("unused")
    public static void handleViewAssessmentDetailButton(ActionEvent event) {
        LOGGER.debug("View Assessment button clicked");
        // TODO - Once assessment detail page has been implemented add call to change to
    }

    /**
     * Handles the event when the View Module Detail button is clicked.
     * This method is responsible for displaying the module detail view.
     * Currently, it is not implemented and needs to be updated to handle the
     * module detail view display logic.
     *
     * @param event The ActionEvent that triggered this method call
     */
    @SuppressWarnings("unused")
    public static void handleViewModuleDetailButton(ActionEvent event) {
        LOGGER.debug("View Module button clicked");
        // TODO - Once module detail page has been implemented add call to change to
    }

    /**
     * Handles the event when the View Student Detail button is clicked.
     * This method is responsible for displaying the student detail view.
     * Currently, it is not implemented and needs to be updated to handle the
     * student detail view display logic.
     *
     * @param event The ActionEvent that triggered this method call
     */
    @SuppressWarnings("unused")
    public static void handleViewStudentDetailButton(ActionEvent event) {
        LOGGER.debug("View Student button clicked");
        // TODO - Once module detail page has been implemented add call to change to
    }

    /**
     * Handles the event when the Logout button is clicked.
     * This method is responsible for logging out the user and changing the view
     * to the login stage.
     *
     * @param event The ActionEvent that triggered this method call
     */
    @SuppressWarnings("unused")
    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logout button clicked");
        hideCourseDetail();
        ViewFactory.getInstance().changeToLoginStage();
    }

    /**
     * Handles the event when the Back button is clicked.
     * This method is responsible for hiding the course detail stage and changing
     * the view to the admin dashboard stage.
     *
     * @param event The ActionEvent that triggered this method call
     */
    @SuppressWarnings("unused")
    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Back button clicked");
        hideCourseDetail();
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }
}
