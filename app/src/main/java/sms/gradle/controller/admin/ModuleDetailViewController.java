package sms.gradle.controller.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AssessmentDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Student;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;
import sms.gradle.view.frames.admin.AssessmentDetailView;
import sms.gradle.view.frames.admin.ModuleDetailView;
import sms.gradle.view.frames.admin.StudentDetailView;

public final class ModuleDetailViewController {
    private static final Logger LOGGER = LogManager.getLogger();

    private ModuleDetailViewController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static void hideModuleDetail() {
        ViewFactory.getInstance().getModuleDetailStage().hide();
    }

    /**
     * Loads students enrolled in a specific module
     * @param moduleId The ID of the module to load students for
     * @return List of students enrolled in the module
     */
    public static List<Student> loadStudentsForModule(int moduleId) {
        LOGGER.debug("Loading students for module ID: {}", moduleId);
        List<Student> students = new ArrayList<>();

        try {
            students = StudentDAO.findAll();

            LOGGER.debug("Loaded {} students for module ID: {}", students.size(), moduleId);
        } catch (SQLException e) {
            LOGGER.error("Error loading students for module ID: {}", moduleId, e);
            Common.showAlert(
                    "An error occurred", "We had a problem loading the students for the module. Please try again.");
        }

        return students;
    }

    /**
     * Loads assessments for a specific module
     * @param moduleId The ID of the module to load assessments for
     * @return List of assessments for the module
     */
    public static List<Assessment> loadAssessmentsForModule(int moduleId) {
        LOGGER.debug("Loading assessments for module ID: {}", moduleId);
        List<Assessment> assessments = new ArrayList<>();

        try {
            assessments = AssessmentDAO.findByModuleId(moduleId);
            LOGGER.debug("Loaded {} assessments for module ID: {}", assessments.size(), moduleId);
        } catch (SQLException e) {
            LOGGER.error("Error loading assessments for module ID: {}", moduleId, e);
            Common.showAlert(
                    "An error occurred", "We had a problem loading the assessments for the module. Please try again.");
        }

        return assessments;
    }

    /**
     * Calculates statistics for students in a module
     * @param moduleId The ID of the module
     * @return Array containing [totalStudents, averageGrade, passRate]
     */
    public static double[] calculateModuleStatistics(int moduleId) {
        LOGGER.debug("Calculating statistics for module ID: {}", moduleId);
        double[] stats = new double[3]; // [totalStudents, averageGrade, passRate]

        try {
            List<Student> students = loadStudentsForModule(moduleId);
            stats[0] = students.size(); // Total students

            // TODO: Implement grade calculation when grade data is available
            stats[1] = 0.0; // Average grade placeholder
            stats[2] = 0.0; // Pass rate placeholder

        } catch (Exception e) {
            LOGGER.error("Error calculating statistics for module ID: {}", moduleId, e);
        }

        return stats;
    }

    /**
     * Updates the module view with the latest data
     * @param view The ModuleDetailView to update
     * @param moduleId The ID of the module to display
     */
    public static void refreshModuleView(ModuleDetailView view, int moduleId) {
        if (moduleId <= 0) {
            LOGGER.warn("Invalid module ID: {}", moduleId);
            return;
        }

        view.setModuleId(moduleId);
    }

    public static void handleViewAssessmentDetailButton(ActionEvent event) {
        LOGGER.debug("View Assessment button clicked");
        ModuleDetailView view = (ModuleDetailView)
                ((javafx.scene.Node) event.getSource()).getScene().getRoot();
        Integer assessmentId = null;

        if (view.getSelectedAssessmentRow() != null) {
            assessmentId = (Integer) view.getSelectedAssessmentRow().getUserData();
            LOGGER.debug("Selected assessment ID: {}", assessmentId);

            hideModuleDetail();

            AssessmentDetailView assessmentDetailView = (AssessmentDetailView) ViewFactory.getInstance()
                    .getAssessmentDetailStage()
                    .getScene()
                    .getRoot();
            assessmentDetailView.setAssessmentId(assessmentId);

            ViewFactory.getInstance().changeToAssessmentDetailStage();
        } else {
            LOGGER.warn("No assessment selected");
            Common.showAlert("No Assessment Selected", "Please select an assessment to view.");
        }
    }

    public static void handleViewStudentDetailButton(ActionEvent event) {
        LOGGER.debug("View Student button clicked");
        ModuleDetailView view = (ModuleDetailView)
                ((javafx.scene.Node) event.getSource()).getScene().getRoot();
        String studentIdStr = view.getSelectedStudentId();

        if (studentIdStr != null && !studentIdStr.isEmpty()) {
            try {
                int studentId = Integer.parseInt(studentIdStr);
                LOGGER.debug("Selected student ID: {}", studentId);

                hideModuleDetail();

                StudentDetailView studentDetailView = (StudentDetailView) ViewFactory.getInstance()
                        .getStudentDetailStage()
                        .getScene()
                        .getRoot();
                studentDetailView.setStudentId(studentId);

                ViewFactory.getInstance().changeToStudentDetailStage();
            } catch (NumberFormatException e) {
                LOGGER.error("Invalid student ID format: {}", studentIdStr, e);
                Common.showAlert("Error", "Invalid student ID format.");
            }
        } else {
            LOGGER.warn("No student selected");
            Common.showAlert("No Student Selected", "Please select a student to view.");
        }
    }

    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Back button clicked");
        hideModuleDetail();
        ViewFactory.getInstance().changeToCourseDetailStage();
    }

    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logout button clicked");
        hideModuleDetail();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
