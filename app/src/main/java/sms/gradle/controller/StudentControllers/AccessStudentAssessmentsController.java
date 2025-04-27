package sms.gradle.controller.StudentControllers;

import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AssessmentDAO;
import sms.gradle.model.dao.ResultDAO;
import sms.gradle.model.entities.Result;
import sms.gradle.utils.session.Session;
import sms.gradle.view.ViewFactory;
import sms.gradle.view.frames.student.AccessStudentAssessmentsView;

public final class AccessStudentAssessmentsController {

    private static final Logger LOGGER = LogManager.getLogger();

    private AccessStudentAssessmentsController() {
        throw new UnsupportedOperationException("All methods in controller class are static");
    }

    /**
     * Displays all assessments and corresponding results for all courses for current student
     *
     * @param view instance of assessments view
     */
    public static void loadAssessments(AccessStudentAssessmentsView view) {

        try {
            int studentId = Session.getInstance().getUser().get().getId();

            List<Result> results = ResultDAO.findByStudentId(studentId);
            displayAssessments(view, results);

            LOGGER.debug("Displayed {} assessment results for student ID: {}", results.size(), studentId);
        } catch (SQLException e) {
            LOGGER.info("Failed in displaying assessments", e);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Assessments loading error");
            alert.setHeaderText(null);
            alert.setContentText("Failed loading assessment details. Try again.");
            alert.showAndWait();
        }
    }

    private static void displayAssessments(AccessStudentAssessmentsView view, List<Result> results) {
        view.clearAssessments();

        try {
            int[] position = {0, 0}; // pos[0] ~ row no., pos[1] ~ column no.

            for (Result result : results) {
                AssessmentDAO.findById(result.getAssessmentId()).ifPresent(assessment -> {
                    view.updateAssessmentToGrid(assessment, result.getGrade(), position[0], position[1]);
                });

                position[1]++;
                if (position[1] > 2) {
                    position[1] = 0;
                    position[0]++;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to show assessments and related grades");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Assessments loading error");
            alert.setHeaderText(null);
            alert.setContentText("Failed loading assessment details. Try again.");
            alert.showAndWait();
        }
    }

    public void updateAssessmentToGrid() {}

    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Clicked back button");
        ViewFactory.getInstance().changeToStudentDashboardStage();
    }

    public static void handleSignoutButton(ActionEvent event) {
        LOGGER.debug("Clicked signout button");
        ViewFactory.getInstance().changeToLoginStage();
    }
}
