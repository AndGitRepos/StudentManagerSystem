package sms.gradle.view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.StudentControllers.AccessStudentAssessmentsController;
import sms.gradle.controller.StudentControllers.AccessStudentModulesController;
import sms.gradle.controller.StudentControllers.StudentDashboardController;
import sms.gradle.controller.admin.AdminDashboardController;
import sms.gradle.controller.admin.ManageAdminsController;
import sms.gradle.controller.admin.ManageAssessmentsController;
import sms.gradle.controller.admin.ManageCourseController;
import sms.gradle.controller.admin.ManageModuleController;
import sms.gradle.controller.admin.ManageStudentController;
import sms.gradle.utils.session.Session;
import sms.gradle.view.frames.LoginView;
import sms.gradle.view.frames.admin.AdminDashboardView;
import sms.gradle.view.frames.admin.AssessmentDetailView;
import sms.gradle.view.frames.admin.CourseDetailView;
import sms.gradle.view.frames.admin.ManageAdminView;
import sms.gradle.view.frames.admin.ManageAssessmentsView;
import sms.gradle.view.frames.admin.ManageCourseView;
import sms.gradle.view.frames.admin.ManageModulesView;
import sms.gradle.view.frames.admin.ManageStudentView;
import sms.gradle.view.frames.admin.ModuleDetailView;
import sms.gradle.view.frames.admin.StudentDetailView;
import sms.gradle.view.frames.student.AccessStudentAssessmentsView;
import sms.gradle.view.frames.student.AccessStudentModulesView;
import sms.gradle.view.frames.student.StudentDashboardView;

/*
 * Co-ordinates creating & interchanging between application views using Singleton design pattern
 */

@Getter
public class ViewFactory {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final ViewFactory instance = new ViewFactory();
    private Stage loginStage;
    private Stage studentDashboardStage;
    private Stage studentModulesStage;
    private Stage studentAssessmentsStage;
    private Stage adminDashboardStage;
    private Stage manageStudentStage;
    private Stage manageCourseStage;
    private Stage manageModulesStage;
    private Stage manageAdminStage;
    private Stage courseDetailStage;
    private Stage moduleDetailStage;
    private Stage assessmentDetailStage;
    private Stage studentDetailStage;
    private Stage manageAssessmentsStage;

    private ViewFactory() {
        LOGGER.info("Initialising View Factory");
        initialiseLoginStage();
        initialiseStudentDashboardStage();
        initialiseStudentModulesStage();
        initialiseStudentAssessmentsStage();
        initialiseAdminDashboardStage();
        initialiseManageStudentStage();
        initialiseManageCourseStage();
        initialiseManageModulesStage();
        initialiseManageAdminStage();
        initialiseCourseDetailStage();
        initialiseModuleDetailStage();
        initialiseAssessmentDetailStage();
        initialiseStudentDetailStage();
        initialiseManageAssessmentsStage();
        LOGGER.info("View Factory initialised");
    }

    public static ViewFactory getInstance() {
        return instance;
    }

    public void changeToLoginStage() {
        LOGGER.debug("Changing to login stage");
        Session.getInstance().clearSession();
        loginStage.show();
    }

    public void changeToStudentDashboardStage() {
        LOGGER.debug("Changing to student dashboard stage");
        studentDashboardStage.show();
    }

    public void changeToStudentModulesStage() {
        LOGGER.debug("Changing to access student modules stage");
        studentModulesStage.show();
    }

    public void changeToStudentAssessmentsStage() {
        LOGGER.debug("Changing to access student assessments stage");
        studentAssessmentsStage.show();
    }

    public void changeToAdminDashboardStage() {
        LOGGER.debug("Changing to admin dashboard stage");
        adminDashboardStage.show();
    }

    public void changeToManageStudentStage() {
        LOGGER.debug("Changing to manage student stage");
        manageStudentStage.show();
    }

    public void changeToManageCourseStage() {
        LOGGER.debug("Changing to manage course stage");
        manageCourseStage.show();
    }

    public void changeToManageModulesStage() {
        LOGGER.debug("Changing to manage modules stage");
        manageModulesStage.show();
    }

    public void changeToManageAdminStage() {
        LOGGER.debug("Changing to manage admin stage");
        manageAdminStage.show();
    }

    public void changeToCourseDetailStage() {
        LOGGER.debug("Changing to course detail stage");
        courseDetailStage.show();
    }

    public void changeToModuleDetailStage() {
        LOGGER.debug("Changing to module detail stage");
        moduleDetailStage.show();
    }

    public void changeToAssessmentDetailStage() {
        LOGGER.debug("Changing to assessment detail stage");
        assessmentDetailStage.show();
    }

    public void changeToStudentDetailStage() {
        LOGGER.debug("Changing to student detail stage");
        studentDetailStage.show();
    }

    public void changeToManageAssessmentsStage() {
        LOGGER.debug("Changing to manage assessments stage");
        manageAssessmentsStage.show();
    }

    private void initialiseLoginStage() {
        LOGGER.debug("Initialising login stage");
        LoginView login = new LoginView();
        loginStage = new Stage();
        loginStage.setScene(new Scene(login, 450, 350));
        loginStage.setTitle("Student Management System - Login");
        loginStage.setResizable(false);
    }

    private void initialiseStudentDashboardStage() {
        LOGGER.debug("Initialising student dashboard stage");

        StudentDashboardView studentDashboardView = new StudentDashboardView();

        studentDashboardStage = new Stage();
        studentDashboardStage.setOnShown(StudentDashboardController::handleOnShowEvent);
        studentDashboardStage.setMinHeight(550);
        studentDashboardStage.setMinWidth(750);
        studentDashboardStage.setTitle("SMS - Student Dashboard");
        studentDashboardStage.setScene(new Scene(studentDashboardView));
    }

    private void initialiseStudentModulesStage() {
        LOGGER.debug("Initialising student modules stage");

        AccessStudentModulesView modulesView = new AccessStudentModulesView();

        studentModulesStage = new Stage();
        studentModulesStage.setOnShown(AccessStudentModulesController::handleOnShowEvent);
        studentModulesStage.setMinHeight(550);
        studentModulesStage.setMinWidth(750);
        studentModulesStage.setTitle("SMS - Module View");
        studentModulesStage.setScene(new Scene(modulesView));
    }

    private void initialiseStudentAssessmentsStage() {
        LOGGER.debug("Initialising student assessments stage");

        AccessStudentAssessmentsView assessmentsView = new AccessStudentAssessmentsView();

        studentAssessmentsStage = new Stage();
        studentAssessmentsStage.setOnShown(AccessStudentAssessmentsController::handleOnShowEvent);
        studentAssessmentsStage.setMinHeight(550);
        studentAssessmentsStage.setMinWidth(750);
        studentAssessmentsStage.setTitle("SMS - Assessments View");
        studentAssessmentsStage.setScene(new Scene(assessmentsView));
    }

    private void initialiseAdminDashboardStage() {
        LOGGER.debug("Initialising admin dashboard stage");

        AdminDashboardView adminDashboardView = new AdminDashboardView();

        adminDashboardStage = new Stage();
        adminDashboardStage.setOnShown(AdminDashboardController::handleOnShowEvent);
        adminDashboardStage.setMinHeight(550);
        adminDashboardStage.setMinWidth(750);
        adminDashboardStage.setTitle("SMS - Admin Dashboard");
        adminDashboardStage.setScene(new Scene(adminDashboardView));
    }

    private void initialiseManageStudentStage() {
        LOGGER.debug("Initialising manage student stage");

        ManageStudentView manageStudentView = new ManageStudentView();

        manageStudentStage = new Stage();
        manageStudentStage.setOnShown(ManageStudentController::handleOnShowEvent);
        manageStudentStage.setMinHeight(550);
        manageStudentStage.setMinWidth(750);
        manageStudentStage.setTitle("SMS - Manage Students");
        manageStudentStage.setScene(new Scene(manageStudentView));
    }

    private void initialiseManageCourseStage() {
        LOGGER.debug("Initialising manage course stage");

        ManageCourseView manageCourseView = new ManageCourseView();

        manageCourseStage = new Stage();
        manageCourseStage.setOnShown(ManageCourseController::handleOnShowEvent);
        manageCourseStage.setMinHeight(550);
        manageCourseStage.setMinWidth(750);
        manageCourseStage.setTitle("SMS - Manage Courses");
        manageCourseStage.setScene(new Scene(manageCourseView));
    }

    private void initialiseManageModulesStage() {
        LOGGER.debug("Initialising manage modules stage");

        ManageModulesView manageModulesView = new ManageModulesView();

        manageModulesStage = new Stage();
        manageModulesStage.setOnShown(ManageModuleController::handleOnShowEvent);
        manageModulesStage.setMinHeight(550);
        manageModulesStage.setMinWidth(750);
        manageModulesStage.setTitle("SMS - Manage Modules");
        manageModulesStage.setScene(new Scene(manageModulesView));
    }

    private void initialiseManageAdminStage() {
        LOGGER.debug("Initialising manage admin stage");
        ManageAdminView manageAdminView = new ManageAdminView();

        manageAdminStage = new Stage();
        manageAdminStage.setOnShown(ManageAdminsController::handleOnShowEvent);
        manageAdminStage.setMinHeight(550);
        manageAdminStage.setMinWidth(750);
        manageAdminStage.setScene(new Scene(manageAdminView));
        manageAdminStage.setTitle("SMS - Manage Admins");
    }

    private void initialiseCourseDetailStage() {
        LOGGER.debug("Initialising course detail stage");
        CourseDetailView courseDetailView = new CourseDetailView();

        courseDetailStage = new Stage();
        courseDetailStage.setMinHeight(550);
        courseDetailStage.setMinWidth(750);
        courseDetailStage.setScene(new Scene(courseDetailView));
        courseDetailStage.setTitle("SMS - Course Detail");
    }

    private void initialiseModuleDetailStage() {
        LOGGER.debug("Initialising module detail stage");
        ModuleDetailView moduleDetailView = new ModuleDetailView();

        moduleDetailStage = new Stage();
        moduleDetailStage.setMinHeight(550);
        moduleDetailStage.setMinWidth(750);
        moduleDetailStage.setScene(new Scene(moduleDetailView));
        moduleDetailStage.setTitle("SMS - Module Detail");
    }

    private void initialiseAssessmentDetailStage() {
        LOGGER.debug("Initialising assessment detail stage");
        AssessmentDetailView assessmentDetailView = new AssessmentDetailView();

        assessmentDetailStage = new Stage();
        assessmentDetailStage.setMinHeight(550);
        assessmentDetailStage.setMinWidth(750);
        assessmentDetailStage.setScene(new Scene(assessmentDetailView));
        assessmentDetailStage.setTitle("SMS - Assessment Detail");
    }

    private void initialiseStudentDetailStage() {
        LOGGER.debug("Initialising student detail stage");
        StudentDetailView studentDetailView = new StudentDetailView();

        studentDetailStage = new Stage();
        studentDetailStage.setMinHeight(550);
        studentDetailStage.setMinWidth(750);
        studentDetailStage.setScene(new Scene(studentDetailView));
        studentDetailStage.setTitle("SMS - Student Detail");
    }

    private void initialiseManageAssessmentsStage() {
        LOGGER.debug("Initialising manage assessments stage");

        ManageAssessmentsView manageAssessmentsView = new ManageAssessmentsView();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        manageAssessmentsStage = new Stage();
        manageAssessmentsStage.setOnShown(ManageAssessmentsController::handleOnShowEvent);
        manageAssessmentsStage.setX(screenBounds.getMinX());
        manageAssessmentsStage.setY(screenBounds.getMinY());
        manageAssessmentsStage.setWidth(screenBounds.getWidth());
        manageAssessmentsStage.setHeight(screenBounds.getHeight());
        manageAssessmentsStage.setTitle("SMS - Manage Assessments");
        manageAssessmentsStage.setScene(new Scene(manageAssessmentsView));
    }
}
