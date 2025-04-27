package sms.gradle.controller.StudentControllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.CourseEnrollmentDAO;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.CourseEnrollment;
import sms.gradle.utils.session.Session;
import sms.gradle.view.ViewFactory;
import sms.gradle.view.frames.student.StudentDashboardView;

@Getter
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
    public static void loadEnrolledCourses(StudentDashboardView view) {
        try {
            if (!Session.getInstance().getUser().isPresent()) {
                LOGGER.info("Unable to load courses - no user signed in");
                return;
            }
            int studentId = Session.getInstance().getUser().get().getId();
            List<CourseEnrollment> enrolledCourses = CourseEnrollmentDAO.findByStudentId(studentId);
            List<Course> courses = new ArrayList<>();

            for (CourseEnrollment enrolledCourse : enrolledCourses) {
                CourseDAO.findById(enrolledCourse.getCourseId()).ifPresent(courses::add);
            }
            view.getCourseListView().getItems().clear();
            view.getCourseListView().getItems().addAll(courses);

            LOGGER.debug("Displayed {} courses for student ID: {}", courses.size(), studentId);
        } catch (SQLException e) {
            LOGGER.info("Failed in displaying enrolled courses", e);
        }
    }

    public static void handleSelectCourseButton(ActionEvent event) {
        ListView<Course> courseList = (ListView<Course>) ViewFactory.getInstance()
                .getStudentDashboardStage().getScene().lookup("#courseListView");

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
        // TODO: Implement
    }

    public static void handleSignout(ActionEvent event) {
        LOGGER.debug("Clicked signout button");
        ViewFactory.getInstance().changeToLoginStage();
    }
}
