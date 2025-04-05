package sms.gradle.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sms.gradle.view.Frames.LoginUI;
import sms.gradle.view.Frames.StudentDashboardView;

/*
 * Co-ordinates creating & interchanging between applicaton views using Singleton design pattern
 */

public class ViewFactory {

    private static final ViewFactory instance = new ViewFactory();
    private Stage loginStage;
    private Stage studentDashboardStage;

    private ViewFactory() {}

    public static ViewFactory getInstance() {
        return instance;
    }

    public void changeToLoginStage() {
        closeStudentDashboardStage();
        initialiseLoginStage();
    }

    public void changeToStudentDashboardStage(String username) {
        closeLoginStage();
        initialiseStudentDashboardStage(username);
    }

    private void initialiseLoginStage() {

        LoginUI login = new LoginUI();
        loginStage = new Stage();
        loginStage.setScene(new Scene(login, 450, 350));
        loginStage.setTitle("SMS - Login");
        loginStage.setResizable(false);
        loginStage.show();
    }

    private void closeLoginStage() {
        if (loginStage != null) {
            loginStage.hide();
            loginStage = null;
        }
    }

    private void initialiseStudentDashboardStage(String username) {

        StudentDashboardView studentDashboardView = new StudentDashboardView();
        studentDashboardView.displayWelcomeMessage(username);

        studentDashboardStage = new Stage();
        studentDashboardStage.setMinHeight(550);
        studentDashboardStage.setMinWidth(750);
        studentDashboardStage.setTitle("SMS - Student Dashboard");
        studentDashboardStage.setScene(new Scene(studentDashboardView));
        studentDashboardStage.show();
    }

    private void closeStudentDashboardStage() {

        if (studentDashboardStage != null) {
            studentDashboardStage.hide();
            studentDashboardStage = null;
        }
    }
}
