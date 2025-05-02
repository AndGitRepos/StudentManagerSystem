package sms.gradle.controller.StudentControllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AssessmentDAO;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.CourseEnrollmentDAO;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.dao.ResultDAO;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.CourseEnrollment;
import sms.gradle.model.entities.Module;
import sms.gradle.model.entities.Result;
import sms.gradle.utils.Common;
import sms.gradle.utils.session.Session;
import sms.gradle.view.ViewFactory;
import sms.gradle.view.components.GradeCircle;

public class AccessStudentAssessmentsController {
    private static final Logger LOGGER = LogManager.getLogger();

    private AccessStudentAssessmentsController() {
        throw new UnsupportedOperationException("All methods in controller class are static");
    }

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getStudentAssessmentsStage();
    }

    /**
     * Updates the assessment list view with assessments from a specific course.
     * Retrieves all modules for the given course, then finds all assessments for each module
     * and displays them in the assessment list view.
     *
     * @param courseId The ID of the course to show assessments for
     * @throws SQLException If there is an error accessing the database
     */
    private static void updateAssessmentList(int courseId) {
        try {
            ListView<Assessment> assessmentsList = Common.getNode(getViewStage(), "#assessmentListView");

            List<Module> modules = ModuleDAO.findByCourseId(courseId);
            List<Assessment> assessments = new ArrayList<>();

            for (Module module : modules) {
                assessments.addAll(AssessmentDAO.findByModuleId(module.getId()));
            }
            assessmentsList.getItems().clear();
            assessmentsList.getItems().addAll(assessments);
        } catch (SQLException e) {
            LOGGER.error("Failed to display assessments list for course", e);
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Loading Assessments Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed in loading assessments. Try again");
            alert.showAndWait();
        }
    }

    /**
     * Updates the UI labels with details of the selected assessment.
     * Displays the assessment name, ID, description, due date, associated module name and lecturer,
     * and the grade score for the selected assessment.
     *
     * @param assessment The Assessment object containing the details to display
     */
    private static void updateAssessmentDetails(Assessment assessment) {
        try {
            // Assessment details - update
            Label assessmentNameLabel = Common.getNode(getViewStage(), "#assessmentNameLabel");
            Label assessmentIdLabel = Common.getNode(getViewStage(), "#assessmentIdLabel");
            Label assessmentDescriptionLabel = Common.getNode(getViewStage(), "#assessmentDescriptionLabel");
            Label dueDateLabel = Common.getNode(getViewStage(), "#dueDateLabel");

            assessmentNameLabel.setText("Assessment Name: " + assessment.getName());
            assessmentIdLabel.setText("ID: " + assessment.getId());

            assessmentDescriptionLabel.setText("Description: " + assessment.getDescription());
            dueDateLabel.setText("Due Date: " + assessment.getDueDate());

            Module module = ModuleDAO.findById(assessment.getModuleId()).orElse(null);

            if (module != null) {

                Label moduleLabel = Common.getNode(getViewStage(), "#moduleLabel");
                Label lecturerLabel = Common.getNode(getViewStage(), "#lecturerLabel");

                moduleLabel.setText("Module: " + module.getName());
                lecturerLabel.setText("Lecturer: " + module.getLecturer());
            }

            // Grade Score Display - update
            VBox gradeArea = Common.getNode(getViewStage(), "#displayResultsArea");
            gradeArea.getChildren().clear();

            int studentId = Session.getInstance().getUser().get().getId();
            List<Result> results = ResultDAO.findByStudentId(studentId);

            for (Result result : results) {
                if (assessment.getId() == result.getAssessmentId()) {
                    gradeArea.getChildren().add(new GradeCircle(result.getGrade()));
                    break;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed in updating student assessment details");
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Updating Assessments Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed in updating assessments. Try again");
            alert.showAndWait();
        }
    }

    /**
     * Handles the event when a course is selected from the filter dropdown.
     * Updates the assessment list view to show only assessments for the selected course.
     *
     * @param event The ActionEvent triggered by selecting a course from the ComboBox
     */
    public static void handleFilterAssessments(ActionEvent event) {
        ComboBox<Course> filterByCourse = (ComboBox<Course>) event.getSource();
        Course chosenCourse = filterByCourse.getValue();
        if (chosenCourse != null) {
            updateAssessmentList(chosenCourse.getId());
        }
    }

    /**
     * Handles the event when a select button is clicked.
     * Retrieves the selected assessment from the list view and updates the UI with its details.
     *
     * @param event The ActionEvent triggered by clicking the select button
     */
    public static void handleSelectButton(ActionEvent event) {
        ListView<Assessment> assessmentsList = Common.getNode(getViewStage(), "#assessmentListView");
        Assessment chosenAssessment = assessmentsList.getSelectionModel().getSelectedItem();
        if (chosenAssessment != null) {
            updateAssessmentDetails(chosenAssessment);
        }
    }

    /**
     * Handles the event when the refresh button is clicked.
     * Updates the assessment list view with assessments from the selected course in the filter dropdown.
     *
     * @param event The ActionEvent triggered by clicking the refresh button
     */
    public static void handleRefreshButton(ActionEvent event) {
        loadCoursesToFilter();

        ComboBox<Course> filterByCourse = Common.getNode(getViewStage(), "#filterDropDown");
        Course selectedCourse = filterByCourse.getValue();
        if (selectedCourse != null) {
            updateAssessmentList(selectedCourse.getId());
        }
    }

    /**
     * Loads the courses that the current student is enrolled in to the filter dropdown.
     * Retrieves the student's enrollments and populates the filter dropdown with the corresponding courses.
     */
    public static void loadCoursesToFilter() {
        try {
            ComboBox<Course> filterByCourse = Common.getNode(getViewStage(), "#filterDropDown");

            int studentId = Session.getInstance().getUser().get().getId();
            List<CourseEnrollment> enrollments = CourseEnrollmentDAO.findByStudentId(studentId);

            filterByCourse.getItems().clear();

            for (CourseEnrollment enrollment : enrollments) {
                CourseDAO.findById(enrollment.getCourseId())
                        .ifPresent(course -> filterByCourse.getItems().add(course));
            }
        } catch (SQLException e) {
            LOGGER.error("Failed in loading student courses to filter");
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Loading Courses Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed in loading student courses to filter. Try again");
            alert.showAndWait();
        }
    }

    /**
     * Handles the event when the stage is shown.
     * Loads the courses that the current student is enrolled in to the filter dropdown.
     *
     * @param event The WindowEvent triggered by showing the stage
     */
    public static void handleOnShowEvent(WindowEvent event) {
        loadCoursesToFilter();
    }

    /**
     * Handles the event when the back button is clicked.
     * Hides the current stage and changes to the student dashboard stage.
     *
     * @param event The ActionEvent triggered by clicking the back button
     */
    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Clicked back button");
        ViewFactory.getInstance().getStudentAssessmentsStage().hide();
        ViewFactory.getInstance().changeToStudentDashboardStage();
    }

    /**
     * Handles the event when the signout button is clicked.
     * Hides the current stage and changes to the login stage.
     *
     * @param event The ActionEvent triggered by clicking the signout button
     */
    public static void handleSignoutButton(ActionEvent event) {
        LOGGER.debug("Clicked signout button");
        ViewFactory.getInstance().getStudentAssessmentsStage().hide();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
