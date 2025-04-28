package sms.gradle.controller.StudentControllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.CourseEnrollmentDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.CourseEnrollment;
import sms.gradle.utils.session.Session;
import sms.gradle.view.ViewFactory;

public class StudentDashboardController {

    private static final Logger LOGGER = LogManager.getLogger();

    private StudentDashboardController() {
        throw new UnsupportedOperationException("All methods in controller class are static");
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
            ListView<Course> courseListView = (ListView<Course>) ViewFactory.getInstance()
                    .getStudentDashboardStage()
                    .getScene()
                    .lookup("#courseListView");

            Stage stage = ViewFactory.getInstance().getStudentDashboardStage();
            Label nameLabel = (Label) stage.getScene().lookup("#studentNameLabel");
            Label emailLabel = (Label) stage.getScene().lookup("#studentEmailLabel");
            Label joinDateLabel = (Label) stage.getScene().lookup("#studentJoinDateLabel");

            int studentId = Session.getInstance().getUser().get().getId();

            StudentDAO.findById(studentId).ifPresent(student -> {
                nameLabel.setText("Student: " + student.getFirstName() + " " + student.getLastName());
                emailLabel.setText("Email: " + student.getEmail());
                joinDateLabel.setText("Join Date: " + student.getJoinDate().toString());
                LOGGER.debug("Loaded student details successfully");
            });

            List<CourseEnrollment> enrolledCourses = CourseEnrollmentDAO.findByStudentId(studentId);
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

    public static void handleSelectCourseButton(ActionEvent event) {
        ListView<Course> courseList = (ListView<Course>)
                ViewFactory.getInstance().getStudentDashboardStage().getScene().lookup("#courseListView");

        Course selectedCourse = courseList.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            LOGGER.debug("Selected course: {}", selectedCourse.getName());
            Session.getInstance().setSelectedCourseId(selectedCourse.getId());
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

    public static void handleViewStatsButton(ActionEvent event) {
        LOGGER.debug("Clicked 'view stats' button");
        // TODO: Implement access stats view
    }

    public static void handleViewAssessmentsButton(ActionEvent event) {
        LOGGER.debug("Clicked 'access assessments' button");
        ViewFactory.getInstance().changeToStudentAssessmentsStage();
    }

    public static void handleSignout(ActionEvent event) {
        LOGGER.debug("Clicked signout button");
        ViewFactory.getInstance().changeToLoginStage();
    }
}
