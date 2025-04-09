package sms.gradle.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sms.gradle.utils.Session.Session;
import sms.gradle.view.Frames.LoginUI;
import sms.gradle.view.Frames.StaffDashboardView;
import sms.gradle.view.Frames.StudentDashboardView;

/*
 * Co-ordinates creating & interchanging between applicaton views using Singleton design pattern
 */

public class ViewFactory {

    private static final ViewFactory instance = new ViewFactory();
    private Stage loginStage;
    private Stage studentDashboardStage;
    private Stage staffDashboardStage;

    private ViewFactory() {
        initialiseLoginStage();
        initialiseStudentDashboardStage();
        initialiseStaffDashboardStage();
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
        staffDashboardStage.show();
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
}
