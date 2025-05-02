package sms.gradle.controller.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AssessmentDAO;
import sms.gradle.model.dao.CourseDAO;
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
import sms.gradle.view.frames.admin.CourseDetailView;
import sms.gradle.view.frames.admin.ModuleDetailView;
import sms.gradle.view.frames.admin.StudentDetailView;

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
     * Loads students enrolled in a specific course
     * @param courseId The ID of the course to load students for
     * @return List of students enrolled in the course
     */
    public static List<Student> loadStudentsForCourse(int courseId) {
        LOGGER.debug("Loading students for course ID: {}", courseId);
        List<Student> students = new ArrayList<>();

        try {
            List<CourseEnrollment> enrollments = CourseEnrollmentDAO.findByCourseId(courseId);

            for (CourseEnrollment enrollment : enrollments) {
                StudentDAO.findById(enrollment.getStudentId()).ifPresent(students::add);
            }

            LOGGER.debug("Loaded {} students for course ID: {}", students.size(), courseId);
        } catch (SQLException e) {
            LOGGER.error("Error loading students for course ID: {}", courseId, e);
        }

        return students;
    }

    /**
     * Calculates statistics for students in a course
     * @param courseId The ID of the course
     * @return Array containing [totalStudents, averageGrade, passRate]
     */
    public static double[] calculateCourseStatistics(int courseId) {
        LOGGER.debug("Calculating statistics for course ID: {}", courseId);
        double[] stats = new double[3]; // [totalStudents, averageGrade, passRate]

        try {
            List<CourseEnrollment> enrollments = CourseEnrollmentDAO.findByCourseId(courseId);
            stats[0] = enrollments.size(); // Total students

            // TODO: Implement grade calculation when grade data is available
            stats[1] = 0.0; // Average grade placeholder
            stats[2] = 0.0; // Pass rate placeholder

        } catch (SQLException e) {
            LOGGER.error("Error calculating statistics for course ID: {}", courseId, e);
        }

        return stats;
    }

    /**
     * Loads course information (name and description) for a specific course
     * @param courseId The ID of the course to load information for
     * @return String array containing [name, description], or null if course not found
     */
    public static String[] loadCourseInformation(int courseId) {
        LOGGER.debug("Loading course information for course ID: {}", courseId);
        String[] courseInfo = new String[2]; // [name, description]

        try {
            return CourseDAO.findById(courseId)
                    .map(course -> {
                        courseInfo[0] = course.getName();
                        courseInfo[1] = course.getDescription();
                        LOGGER.debug("Loaded course information for course ID: {}", courseId);
                        return courseInfo;
                    })
                    .orElse(null);
        } catch (SQLException e) {
            LOGGER.error("Error loading course information for course ID: {}", courseId, e);
            return null;
        }
    }

    /**
     * Loads modules for a specific course
     * @param courseId The ID of the course to load modules for
     * @return List of modules in the course
     */
    public static List<Module> loadModulesForCourse(int courseId) {
        LOGGER.debug("Loading modules for course ID: {}", courseId);
        List<Module> modules = new ArrayList<>();

        try {
            modules = ModuleDAO.findByCourseId(courseId);
            LOGGER.debug("Loaded {} modules for course ID: {}", modules.size(), courseId);
        } catch (SQLException e) {
            LOGGER.error("Error loading modules for course ID: {}", courseId, e);
        }

        return modules;
    }

    /**
     * Loads assessments for modules in a specific course
     * @param courseId The ID of the course to load assessments for
     * @return List of assessments for the course's modules
     */
    public static List<Assessment> loadAssessmentsForCourse(int courseId) {
        LOGGER.debug("Loading assessments for course ID: {}", courseId);
        List<Assessment> assessments = new ArrayList<>();

        try {
            // Get all modules for the course
            List<Module> modules = ModuleDAO.findByCourseId(courseId);

            // For each module, get its assessments
            for (Module module : modules) {
                List<Assessment> moduleAssessments = AssessmentDAO.findByModuleId(module.getId());
                assessments.addAll(moduleAssessments);
            }

            LOGGER.debug("Loaded {} assessments for course ID: {}", assessments.size(), courseId);
        } catch (SQLException e) {
            LOGGER.error("Error loading assessments for course ID: {}", courseId, e);
        }

        return assessments;
    }

    /**
     * Updates the course view with the latest data
     * @param view The CourseDetailView to update
     * @param courseId The ID of the course to display
     */
    public static void refreshCourseView(CourseDetailView view, int courseId) {
        if (courseId <= 0) {
            LOGGER.warn("Invalid course ID: {}", courseId);
            return;
        }

        view.setCourseId(courseId);
        view.updateModuleList();
        view.updateAssessmentList();
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
        CourseDetailView view = (CourseDetailView)
                ((javafx.scene.Node) event.getSource()).getScene().getRoot();
        Integer assessmentId = null;

        if (view.getSelectedAssessmentRow() != null) {
            assessmentId = (Integer) view.getSelectedAssessmentRow().getUserData();
            LOGGER.debug("Selected assessment ID: {}", assessmentId);

            hideCourseDetail();

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
        CourseDetailView view = (CourseDetailView)
                ((javafx.scene.Node) event.getSource()).getScene().getRoot();
        Integer moduleId = null;

        if (view.getSelectedModuleRow() != null) {
            moduleId = (Integer) view.getSelectedModuleRow().getUserData();
            LOGGER.debug("Selected module ID: {}", moduleId);

            hideCourseDetail();

            ModuleDetailView moduleDetailView = (ModuleDetailView)
                    ViewFactory.getInstance().getModuleDetailStage().getScene().getRoot();
            moduleDetailView.setModuleId(moduleId);

            ViewFactory.getInstance().changeToModuleDetailStage();
        } else {
            LOGGER.warn("No module selected");
            Common.showAlert("No Module Selected", "Please select a module to view.");
        }
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
        CourseDetailView view = (CourseDetailView)
                ((javafx.scene.Node) event.getSource()).getScene().getRoot();
        String studentIdStr = view.getSelectedStudentId();

        if (studentIdStr != null && !studentIdStr.isEmpty()) {
            try {
                int studentId = Integer.parseInt(studentIdStr);
                LOGGER.debug("Selected student ID: {}", studentId);

                hideCourseDetail();

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
