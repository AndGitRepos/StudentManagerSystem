package sms.gradle.controller.student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.CourseEnrollmentDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.CourseEnrollment;
import sms.gradle.utils.Common;
import sms.gradle.utils.session.Session;
import sms.gradle.view.ViewFactory;

public class StudentDashboardController {

    private static final Logger LOGGER = LogManager.getLogger();

    private StudentDashboardController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getStudentDashboardStage();
    }

    /**
     * Displays all enrolled courses from database for current student
     *
     * @param dashboardView instance of student-dashboard view
     */
    public static void handleCourseRefreshButton(ActionEvent event) {
        try {
            if (!Session.getInstance().getUser().isPresent()) {
                LOGGER.info("Unable to load courses - no user signed in");
                return;
            }
            final ListView<Course> courseListView = Common.getNode(getViewStage(), "#courseListView");

            final Label nameLabel = Common.getNode(getViewStage(), "#studentNameLabel");
            final Label emailLabel = Common.getNode(getViewStage(), "#studentEmailLabel");
            final Label joinDateLabel = Common.getNode(getViewStage(), "#studentJoinDateLabel");

            final int studentId = Session.getInstance().getUser().get().getId();

            StudentDAO.findById(studentId).ifPresent(student -> {
                nameLabel.setText("Student: " + student.getFirstName() + " " + student.getLastName());
                emailLabel.setText("Email: " + student.getEmail());
                joinDateLabel.setText("Join Date: " + student.getJoinDate().toString());
                LOGGER.debug("Loaded student details successfully");
            });

            final List<CourseEnrollment> enrolledCourses = CourseEnrollmentDAO.findByStudentId(studentId);
            List<Course> courses = new ArrayList<>();

            for (CourseEnrollment enrolledCourse : enrolledCourses) {
                CourseDAO.findById(enrolledCourse.getCourseId()).ifPresent(courses::add);
            }
            courseListView.getItems().clear();
            courseListView.getItems().addAll(courses);

            LOGGER.debug("Displayed {} courses for student ID: {}", courses.size(), studentId);
        } catch (SQLException e) {
            LOGGER.error("Failed in displaying enrolled courses", e);
        }
    }

    /**
     * Displays course details for selected course
     *
     * @param dashboardView instance of student-dashboard view
     */
    public static void handleSelectCourseButton(ActionEvent event) {
        final ListView<Course> courseList = Common.getNode(getViewStage(), "#courseListView");

        final Course selectedCourse = courseList.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            LOGGER.debug("Selected course: {}", selectedCourse.getName());
            Session.getInstance().setSelectedCourseId(selectedCourse.getId());
            ViewFactory.getInstance().getStudentDashboardStage().hide();
            ViewFactory.getInstance().changeToStudentModulesStage();

        } else {
            LOGGER.info("No course was selected - alert showing");
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No course selected");
            alert.setHeaderText(null);
            alert.setContentText("First select a course from the list");
            alert.showAndWait();
        }
    }

    /**
     * Loads all courses from database to course list view
     *
     * @param dashboardView instance of student-dashboard view
     */
    public static void handleOnShowEvent(WindowEvent event) {
        handleCourseRefreshButton(new ActionEvent());
    }

    /**
     * Handler for the view statistics button click event
     * Currently logs the button click but functionality is not yet implemented
     *
     * @param event The ActionEvent triggered by clicking the view stats button
     */
    public static void handleViewStatsButton(ActionEvent event) {
        LOGGER.debug("Clicked 'view stats' button");
        // TODO: Implement access stats view
    }

    /**
     * Handler for the view assessments button click event
     * Currently logs the button click but functionality is not yet implemented
     *
     * @param event The ActionEvent triggered by clicking the view assessments button
     */
    public static void handleViewAssessmentsButton(ActionEvent event) {
        LOGGER.debug("Clicked 'access assessments' button");
        ViewFactory.getInstance().getStudentDashboardStage().hide();
        ViewFactory.getInstance().changeToStudentAssessmentsStage();
    }

    /**
     * Handler for the signout button click event
     * Logs the button click and changes the stage to the login stage
     *
     * @param event The ActionEvent triggered by clicking the signout button
     */
    public static void handleSignout(ActionEvent event) {
        LOGGER.debug("Clicked signout button");
        ViewFactory.getInstance().getStudentDashboardStage().hide();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
