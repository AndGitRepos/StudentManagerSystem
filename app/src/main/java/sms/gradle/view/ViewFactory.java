package sms.gradle.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import sms.gradle.utils.Session.Session;
import sms.gradle.view.Frames.LoginUI;
import sms.gradle.view.Frames.ManageCourseView;
import sms.gradle.view.Frames.ManageStudentView;
import sms.gradle.view.Frames.StaffDashboardView;
import sms.gradle.view.Frames.StudentDashboardView;

/*
 * Co-ordinates creating & interchanging between application views using Singleton design pattern
 */

@Getter
public class ViewFactory {

    private static final ViewFactory instance = new ViewFactory();
    private Stage loginStage;
    private Stage studentDashboardStage;
    private Stage staffDashboardStage;
    private Stage manageStudentStage;
    private Stage manageCourseStage;

    private ViewFactory() {
        initialiseLoginStage();
        initialiseStudentDashboardStage();
        initialiseStaffDashboardStage();
        initialiseManageStudentStage();
        initialiseManageCourseStage();
    }

    public static ViewFactory getInstance() {
        return instance;
    }

    public void changeToLoginStage() {
        Session.getInstance().clearSession();
        studentDashboardStage.hide();
        staffDashboardStage.hide();
        loginStage.show();
    }

    public void changeToStudentDashboardStage() {
        loginStage.hide();
        studentDashboardStage.show();
    }

    public void changeToStaffDashboardStage() {
        loginStage.hide();
        manageStudentStage.hide();
        staffDashboardStage.show();
    }

    public void changeToManageStudentStage() {
        staffDashboardStage.hide();
        manageStudentStage.show();
    }

    public void changeToManageCourseStage() {
        staffDashboardStage.hide();
        manageCourseStage.show();
    }

    private void initialiseLoginStage() {
        LoginUI login = new LoginUI();
        loginStage = new Stage();
        loginStage.setScene(new Scene(login, 450, 350));
        loginStage.setTitle("Student Management System - Login");
        loginStage.setResizable(false);
    }

    private void initialiseStudentDashboardStage() {

        StudentDashboardView studentDashboardView = new StudentDashboardView();

        studentDashboardStage = new Stage();
        studentDashboardStage.setMinHeight(550);
        studentDashboardStage.setMinWidth(750);
        studentDashboardStage.setTitle("SMS - Student Dashboard");
        studentDashboardStage.setScene(new Scene(studentDashboardView));
    }

    private void initialiseStaffDashboardStage() {

        StaffDashboardView staffDashboardView = new StaffDashboardView();

        staffDashboardStage = new Stage();
        staffDashboardStage.setMinHeight(550);
        staffDashboardStage.setMinWidth(750);
        staffDashboardStage.setTitle("SMS - Staff Dashboard");
        staffDashboardStage.setScene(new Scene(staffDashboardView));
    }

    private void initialiseManageStudentStage() {

        ManageStudentView manageStudentView = new ManageStudentView();

        manageStudentStage = new Stage();
        manageStudentStage.setMinHeight(550);
        manageStudentStage.setMinWidth(750);
        manageStudentStage.setTitle("SMS - Manage Students");
        manageStudentStage.setScene(new Scene(manageStudentView));
    }

    private void initialiseManageCourseStage() {

        ManageCourseView manageCourseView = new ManageCourseView();

        manageCourseStage = new Stage();
        manageCourseStage.setMinHeight(550);
        manageCourseStage.setMinWidth(750);
        manageCourseStage.setTitle("SMS - Manage Courses");
        manageCourseStage.setScene(new Scene(manageCourseView));
    }
}
