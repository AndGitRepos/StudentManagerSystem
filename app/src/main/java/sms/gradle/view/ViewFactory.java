package sms.gradle.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.StudentControllers.AccessStudentModulesController;
import sms.gradle.utils.session.Session;
import sms.gradle.view.frames.LoginView;
import sms.gradle.view.frames.admin.AdminDashboardView;
import sms.gradle.view.frames.admin.CourseDetailView;
import sms.gradle.view.frames.admin.ManageAdminView;
import sms.gradle.view.frames.admin.ManageCourseView;
import sms.gradle.view.frames.admin.ManageModulesView;
import sms.gradle.view.frames.admin.ManageStudentView;
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
    private Stage adminDashboardStage;
    private Stage manageStudentStage;
    private Stage manageCourseStage;
    private Stage manageModulesStage;
    private Stage manageAdminStage;
    private Stage courseDetailStage;
    private Stage studentModulesStage; // TODO: Access Student Modules Stage

    private ViewFactory() {
        LOGGER.info("Initialising View Factory");
        initialiseLoginStage();
        initialiseStudentDashboardStage();
        initialiseAdminDashboardStage();
        initialiseManageStudentStage();
        initialiseManageCourseStage();
        initialiseManageModulesStage();
        initialiseManageAdminStage();
        initialiseCourseDetailStage();
        initialiseStudentModulesStage();
        LOGGER.info("View Factory initialised");
    }

    public static ViewFactory getInstance() {
        return instance;
    }

    public void changeToLoginStage() {
        LOGGER.debug("Changing to login stage");
        Session.getInstance().clearSession();
        studentDashboardStage.hide();
        adminDashboardStage.hide();
        courseDetailStage.hide();
        loginStage.show();
    }

    public void changeToStudentDashboardStage() {
        LOGGER.debug("Changing to student dashboard stage");
        StudentDashboardView view =
                (StudentDashboardView) studentDashboardStage.getScene().getRoot();
        view.loadUserDetails();
        loginStage.hide();
        studentDashboardStage.show();
    }

    public void changeToStudentModulesStage() {
        LOGGER.debug("Changing to access student modules stage");
        AccessStudentModulesView modulesView =
                (AccessStudentModulesView) studentModulesStage.getScene().getRoot();
        AccessStudentModulesController.loadModules(modulesView);
        studentDashboardStage.hide();
        studentModulesStage.show();
    }

    public void changeToAdminDashboardStage() {
        LOGGER.debug("Changing to admin dashboard stage");
        loginStage.hide();
        manageStudentStage.hide();
        manageCourseStage.hide();
        manageModulesStage.hide();
        manageAdminStage.hide();
        courseDetailStage.hide();
        adminDashboardStage.show();
    }

    public void changeToManageStudentStage() {
        LOGGER.debug("Changing to manage student stage");
        adminDashboardStage.hide();
        manageStudentStage.show();
    }

    public void changeToManageCourseStage() {
        LOGGER.debug("Changing to manage course stage");
        adminDashboardStage.hide();
        manageCourseStage.show();
    }

    public void changeToManageModulesStage() {
        LOGGER.debug("Changing to manage modules stage");
        adminDashboardStage.hide();
        manageModulesStage.show();
    }

    public void changeToManageAdminStage() {
        LOGGER.debug("Changing to manage admin stage");
        adminDashboardStage.hide();
        manageAdminStage.show();
    }

    public void changeToCourseDetailStage() {
        LOGGER.debug("Changing to course detail stage");
        adminDashboardStage.hide();
        courseDetailStage.show();
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
        studentDashboardStage.setMinHeight(550);
        studentDashboardStage.setMinWidth(750);
        studentDashboardStage.setTitle("SMS - Student Dashboard");
        studentDashboardStage.setScene(new Scene(studentDashboardView));
    }

    private void initialiseStudentModulesStage() {
        LOGGER.debug("Initialising student modules stage");

        AccessStudentModulesView modulesView = new AccessStudentModulesView();

        studentModulesStage = new Stage();
        studentModulesStage.setMinHeight(550);
        studentModulesStage.setMinWidth(750);
        studentModulesStage.setTitle("SMS - Module View");
        studentModulesStage.setScene(new Scene(modulesView));
    }

    private void initialiseAdminDashboardStage() {
        LOGGER.debug("Initialising admin dashboard stage");

        AdminDashboardView adminDashboardView = new AdminDashboardView();

        adminDashboardStage = new Stage();
        adminDashboardStage.setMinHeight(550);
        adminDashboardStage.setMinWidth(750);
        adminDashboardStage.setTitle("SMS - Admin Dashboard");
        adminDashboardStage.setScene(new Scene(adminDashboardView));
    }

    private void initialiseManageStudentStage() {
        LOGGER.debug("Initialising manage student stage");

        ManageStudentView manageStudentView = new ManageStudentView();

        manageStudentStage = new Stage();
        manageStudentStage.setMinHeight(550);
        manageStudentStage.setMinWidth(750);
        manageStudentStage.setTitle("SMS - Manage Students");
        manageStudentStage.setScene(new Scene(manageStudentView));
    }

    private void initialiseManageCourseStage() {
        LOGGER.debug("Initialising manage course stage");

        ManageCourseView manageCourseView = new ManageCourseView();

        manageCourseStage = new Stage();
        manageCourseStage.setMinHeight(550);
        manageCourseStage.setMinWidth(750);
        manageCourseStage.setTitle("SMS - Manage Courses");
        manageCourseStage.setScene(new Scene(manageCourseView));
    }

    private void initialiseManageModulesStage() {
        LOGGER.debug("Initialising manage modules stage");

        ManageModulesView manageModulesView = new ManageModulesView();

        manageModulesStage = new Stage();
        manageModulesStage.setMinHeight(550);
        manageModulesStage.setMinWidth(750);
        manageModulesStage.setTitle("SMS - Manage Modules");
        manageModulesStage.setScene(new Scene(manageModulesView));
    }

    private void initialiseManageAdminStage() {
        LOGGER.debug("Initialising manage admin stage");
        ManageAdminView manageAdminView = new ManageAdminView();

        manageAdminStage = new Stage();
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
}
