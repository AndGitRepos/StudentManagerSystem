package sms.gradle.controller.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AssessmentDAO;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Module;
import sms.gradle.model.entities.Student;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;
import sms.gradle.view.frames.admin.AssessmentDetailView;
import sms.gradle.view.frames.admin.ModuleDetailView;
import sms.gradle.view.frames.admin.StudentDetailView;

public final class AssessmentDetailViewController {
    private static final Logger LOGGER = LogManager.getLogger();

    private AssessmentDetailViewController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static void hideAssessmentDetail() {
        ViewFactory.getInstance().getAssessmentDetailStage().hide();
    }

    /**
     * Loads students who have submitted the assessment
     * @param assessmentId The ID of the assessment to load students for
     * @return List of students who have submitted the assessment
     */
    public static List<Student> loadStudentsForAssessment(int assessmentId) {
        LOGGER.debug("Loading students for assessment ID: {}", assessmentId);
        List<Student> students = new ArrayList<>();

        try {
            students = StudentDAO.findAll();

            LOGGER.debug("Loaded {} students for assessment ID: {}", students.size(), assessmentId);
        } catch (SQLException e) {
            LOGGER.error("Error loading students for assessment ID: {}", assessmentId, e);
        }

        return students;
    }

    /**
     * Loads the module associated with this assessment
     * @param assessmentId The ID of the assessment
     * @return List containing the module this assessment belongs to
     */
    public static List<Module> loadModulesForAssessment(int assessmentId) {
        LOGGER.debug("Loading module for assessment ID: {}", assessmentId);
        List<Module> modules = new ArrayList<>();

        try {
            Assessment assessment = AssessmentDAO.findById(assessmentId).orElse(null);
            if (assessment != null) {
                int moduleId = assessment.getModuleId();
                ModuleDAO.findById(moduleId).ifPresent(modules::add);
            }

            LOGGER.debug("Loaded {} modules for assessment ID: {}", modules.size(), assessmentId);
        } catch (SQLException e) {
            LOGGER.error("Error loading module for assessment ID: {}", assessmentId, e);
        }

        return modules;
    }

    /**
     * Loads related assessments (assessments from the same module)
     * @param assessmentId The ID of the assessment
     * @return List of assessments from the same module
     */
    public static List<Assessment> loadRelatedAssessments(int assessmentId) {
        LOGGER.debug("Loading related assessments for assessment ID: {}", assessmentId);
        List<Assessment> assessments = new ArrayList<>();

        try {
            Assessment assessment = AssessmentDAO.findById(assessmentId).orElse(null);
            if (assessment != null) {
                int moduleId = assessment.getModuleId();
                assessments = AssessmentDAO.findByModuleId(moduleId);
            }

            LOGGER.debug("Loaded {} related assessments for assessment ID: {}", assessments.size(), assessmentId);
        } catch (SQLException e) {
            LOGGER.error("Error loading related assessments for assessment ID: {}", assessmentId, e);
        }

        return assessments;
    }

    /**
     * Calculates statistics for an assessment
     * @param assessmentId The ID of the assessment
     * @return Array containing [totalSubmissions, averageGrade, passRate]
     */
    public static double[] calculateAssessmentStatistics(int assessmentId) {
        LOGGER.debug("Calculating statistics for assessment ID: {}", assessmentId);
        double[] stats = new double[3]; // [totalSubmissions, averageGrade, passRate]

        try {
            List<Student> students = loadStudentsForAssessment(assessmentId);
            stats[0] = students.size(); // Total submissions

            // TODO: Implement grade calculation when grade data is available
            stats[1] = 0.0; // Average grade placeholder
            stats[2] = 0.0; // Pass rate placeholder

        } catch (Exception e) {
            LOGGER.error("Error calculating statistics for assessment ID: {}", assessmentId, e);
        }

        return stats;
    }

    /**
     * Updates the assessment view with the latest data
     * @param view The AssessmentDetailView to update
     * @param assessmentId The ID of the assessment to display
     */
    public static void refreshAssessmentView(AssessmentDetailView view, int assessmentId) {
        if (assessmentId <= 0) {
            LOGGER.warn("Invalid assessment ID: {}", assessmentId);
            return;
        }

        view.setAssessmentId(assessmentId);
    }

    public static void handleViewModuleDetailButton(ActionEvent event) {
        LOGGER.debug("View Module button clicked");
        AssessmentDetailView view = (AssessmentDetailView)
                ((javafx.scene.Node) event.getSource()).getScene().getRoot();
        Integer moduleId = null;

        if (view.getSelectedModuleRow() != null) {
            moduleId = (Integer) view.getSelectedModuleRow().getUserData();
            LOGGER.debug("Selected module ID: {}", moduleId);

            hideAssessmentDetail();

            // Get the ModuleDetailView and set the module ID
            ModuleDetailView moduleDetailView = (ModuleDetailView)
                    ViewFactory.getInstance().getModuleDetailStage().getScene().getRoot();
            moduleDetailView.setModuleId(moduleId);

            ViewFactory.getInstance().changeToModuleDetailStage();
        } else {
            LOGGER.warn("No module selected");
            Common.showAlert("No Module Selected", "Please select a module to view.");
        }
    }

    public static void handleViewStudentDetailButton(ActionEvent event) {
        LOGGER.debug("View Student button clicked");
        AssessmentDetailView view = (AssessmentDetailView)
                ((javafx.scene.Node) event.getSource()).getScene().getRoot();
        String studentIdStr = view.getSelectedStudentId();

        if (studentIdStr != null && !studentIdStr.isEmpty()) {
            try {
                int studentId = Integer.parseInt(studentIdStr);
                LOGGER.debug("Selected student ID: {}", studentId);

                hideAssessmentDetail();

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
        hideAssessmentDetail();
        ViewFactory.getInstance().changeToCourseDetailStage();
    }

    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logout button clicked");
        hideAssessmentDetail();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
