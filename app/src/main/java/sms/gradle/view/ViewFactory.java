package sms.gradle.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.utils.Session.Session;
import sms.gradle.view.Frames.LoginView;
import sms.gradle.view.Frames.admin.ManageAdminView;
import sms.gradle.view.Frames.admin.ManageCourseView;
import sms.gradle.view.Frames.admin.ManageModulesView;
import sms.gradle.view.Frames.admin.ManageStudentView;
import sms.gradle.view.Frames.admin.StaffDashboardView;
import sms.gradle.view.Frames.student.StudentDashboardView;

/*
 * Co-ordinates creating & interchanging between application views using Singleton design pattern
 */

@Getter
public class ViewFactory {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final ViewFactory instance = new ViewFactory();
    private Stage loginStage;
    private Stage studentDashboardStage;
    private Stage staffDashboardStage;
    private Stage manageStudentStage;
    private Stage manageCourseStage;
    private Stage manageModulesStage;
    private Stage manageAdminStage;

    private ViewFactory() {
        LOGGER.info("Initialising View Factory");
        initialiseLoginStage();
        initialiseStudentDashboardStage();
        initialiseStaffDashboardStage();
        initialiseManageStudentStage();
        initialiseManageCourseStage();
        initialiseManageModulesStage();
        initialiseManageAdminStage();
        LOGGER.info("View Factory initialised");
    }

    public static ViewFactory getInstance() {
        return instance;
    }

    public void changeToLoginStage() {
        LOGGER.debug("Changing to login stage");
        Session.getInstance().clearSession();
        studentDashboardStage.hide();
        staffDashboardStage.hide();
        loginStage.show();
    }

    public void changeToStudentDashboardStage() {
        LOGGER.debug("Changing to student dashboard stage");
        loginStage.hide();
        studentDashboardStage.show();
    }

    public void changeToStaffDashboardStage() {
        LOGGER.debug("Changing to staff dashboard stage");
        loginStage.hide();
        manageStudentStage.hide();
        manageCourseStage.hide();
        manageModulesStage.hide();
        manageAdminStage.hide();
        staffDashboardStage.show();
    }

    public void changeToManageStudentStage() {
        LOGGER.debug("Changing to manage student stage");
        staffDashboardStage.hide();
        manageStudentStage.show();
    }

    public void changeToManageCourseStage() {
        LOGGER.debug("Changing to manage course stage");
        staffDashboardStage.hide();
        manageCourseStage.show();
    }

    public void changeToManageModulesStage() {
        LOGGER.debug("Changing to manage modules stage");
        staffDashboardStage.hide();
        manageModulesStage.show();
    }

    public void changeToManageAdminStage() {
        LOGGER.debug("Changing to manage admin stage");
        staffDashboardStage.hide();
        manageAdminStage.show();
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

    private void initialiseStaffDashboardStage() {
        LOGGER.debug("Initialising staff dashboard stage");

        StaffDashboardView staffDashboardView = new StaffDashboardView();

        staffDashboardStage = new Stage();
        staffDashboardStage.setMinHeight(550);
        staffDashboardStage.setMinWidth(750);
        staffDashboardStage.setTitle("SMS - Staff Dashboard");
        staffDashboardStage.setScene(new Scene(staffDashboardView));
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
}
