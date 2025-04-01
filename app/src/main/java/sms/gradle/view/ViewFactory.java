package sms.gradle.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sms.gradle.view.Frames.Login;
import sms.gradle.view.Frames.StudentDashboardView;

public class ViewFactory {

    public static ViewFactory instance;

    private ViewFactory() {}

    public static ViewFactory getInstance() {
        if (instance == null) {
            instance = new ViewFactory();
        }
        return instance;
    }

    private Stage loginStage;
    private Stage studentDashboardStage;

    public void showLogInWindow() {
        if (studentDashboardStage != null) {
            studentDashboardStage.hide(); // Prevents student dashboard view showing up on login
            // window
        }

        Login login = new Login();
        loginStage = new Stage();
        Scene scene = new Scene(login, 450, 350);

        scene.getStylesheets().add(getClass().getResource("/UIStyles/login.css").toExternalForm());

        loginStage.setScene(scene);
        loginStage.setResizable(false);
        loginStage.setTitle("SMS - Login");
        loginStage.show();
    }

    public void displayStudentDashboard(String username) {

        if (loginStage != null) {
            loginStage.hide();
        }

        StudentDashboardView dashboardview = new StudentDashboardView();
        dashboardview.displayWelcomeMessage(username);

        Stage stage = new Stage();
        Scene scene = new Scene(dashboardview, 750, 550);

        scene.getStylesheets()
                .add(getClass().getResource("/UIStyles/studentDashboard.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("SMS - Dashboard");
        stage.setMinHeight(550);
        stage.setMinWidth(750);
        stage.show();
    }
}
