package sms.gradle.controller.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AssessmentDAO;
import sms.gradle.model.dao.CourseEnrollmentDAO;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.CourseEnrollment;
import sms.gradle.model.entities.Module;
import sms.gradle.model.entities.Student;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;
import sms.gradle.view.frames.admin.AssessmentDetailView;
import sms.gradle.view.frames.admin.ModuleDetailView;
import sms.gradle.view.frames.admin.StudentDetailView;

public final class StudentDetailViewController {
    private static final Logger LOGGER = LogManager.getLogger();

    private StudentDetailViewController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static void hideStudentDetail() {
        ViewFactory.getInstance().getStudentDetailStage().hide();
    }

    /**
     * Loads a student by ID
     * @param studentId The ID of the student to load
     * @return The student object or null if not found
     */
    public static Student loadStudent(int studentId) {
        LOGGER.debug("Loading student with ID: {}", studentId);

        try {
            return StudentDAO.findById(studentId).orElse(null);
        } catch (SQLException e) {
            LOGGER.error("Error loading student with ID: {}", studentId, e);
            Common.showAlert("An error occurred", "We had a problem loading the student. Please try again.");
            return null;
        }
    }

    /**
     * Loads modules for a specific student
     * @param studentId The ID of the student to load modules for
     * @return List of modules the student is enrolled in
     */
    public static List<Module> loadModulesForStudent(int studentId) {
        LOGGER.debug("Loading modules for student ID: {}", studentId);
        List<Module> modules = new ArrayList<>();

        try {
            List<CourseEnrollment> enrollments = CourseEnrollmentDAO.findByStudentId(studentId);

            for (CourseEnrollment enrollment : enrollments) {
                int courseId = enrollment.getCourseId();
                List<Module> courseModules = ModuleDAO.findByCourseId(courseId);
                modules.addAll(courseModules);
            }

            LOGGER.debug("Loaded {} modules for student ID: {}", modules.size(), studentId);
        } catch (SQLException e) {
            LOGGER.error("Error loading modules for student ID: {}", studentId, e);
            Common.showAlert("An error occurred", "We had a problem loading the student's modules. Please try again.");
        }

        return modules;
    }

    /**
     * Loads assessments for a specific student
     * @param studentId The ID of the student to load assessments for
     * @return List of assessments for the student's modules
     */
    public static List<Assessment> loadAssessmentsForStudent(int studentId) {
        LOGGER.debug("Loading assessments for student ID: {}", studentId);
        List<Assessment> assessments = new ArrayList<>();

        try {
            // Get all modules for the student
            List<Module> modules = loadModulesForStudent(studentId);

            // For each module, get its assessments
            for (Module module : modules) {
                List<Assessment> moduleAssessments = AssessmentDAO.findByModuleId(module.getId());
                assessments.addAll(moduleAssessments);
            }

            LOGGER.debug("Loaded {} assessments for student ID: {}", assessments.size(), studentId);
        } catch (SQLException e) {
            LOGGER.error("Error loading assessments for student ID: {}", studentId, e);
            Common.showAlert(
                    "An error occurred", "We had a problem loading the student's assessments. Please try again.");
        }

        return assessments;
    }

    /**
     * Calculates statistics for a student
     * @param studentId The ID of the student
     * @return Array containing [totalModules, averageGrade, completionRate]
     */
    public static double[] calculateStudentStatistics(int studentId) {
        LOGGER.debug("Calculating statistics for student ID: {}", studentId);
        double[] stats = new double[3]; // [totalModules, averageGrade, completionRate]

        try {
            List<Module> modules = loadModulesForStudent(studentId);
            stats[0] = modules.size(); // Total modules

            // TODO: Implement grade calculation when grade data is available
            stats[1] = 0.0; // Average grade placeholder
            stats[2] = 0.0; // Completion rate placeholder

        } catch (Exception e) {
            LOGGER.error("Error calculating statistics for student ID: {}", studentId, e);
        }

        return stats;
    }

    /**
     * Updates the student view with the latest data
     * @param view The StudentDetailView to update
     * @param studentId The ID of the student to display
     */
    public static void refreshStudentView(StudentDetailView view, int studentId) {
        if (studentId <= 0) {
            LOGGER.warn("Invalid student ID: {}", studentId);
            return;
        }

        view.setStudentId(studentId);
    }

    public static void handleViewModuleDetailButton(ActionEvent event) {
        LOGGER.debug("View Module button clicked");
        StudentDetailView view = (StudentDetailView)
                ((javafx.scene.Node) event.getSource()).getScene().getRoot();
        Integer moduleId = null;

        if (view.getSelectedModuleRow() != null) {
            moduleId = (Integer) view.getSelectedModuleRow().getUserData();
            LOGGER.debug("Selected module ID: {}", moduleId);

            hideStudentDetail();

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

    public static void handleViewAssessmentDetailButton(ActionEvent event) {
        LOGGER.debug("View Assessment button clicked");
        StudentDetailView view = (StudentDetailView)
                ((javafx.scene.Node) event.getSource()).getScene().getRoot();
        Integer assessmentId = null;

        if (view.getSelectedAssessmentRow() != null) {
            assessmentId = (Integer) view.getSelectedAssessmentRow().getUserData();
            LOGGER.debug("Selected assessment ID: {}", assessmentId);

            hideStudentDetail();

            // Get the AssessmentDetailView and set the assessment ID
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

    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Back button clicked");
        hideStudentDetail();
        ViewFactory.getInstance().changeToCourseDetailStage();
    }

    public static void handleLogoutButton(ActionEvent event) {
        LOGGER.debug("Logout button clicked");
        hideStudentDetail();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
