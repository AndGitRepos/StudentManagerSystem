package sms.gradle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sms.gradle.model.dao.DatabaseConnection;
import sms.gradle.view.Frames.LoginUI;

public class App extends Application {

    @Override
    public void init() {
        DatabaseConnection.getInstance();
    }

    @Override
    public void start(Stage stage) {
        LoginUI loginUI = new LoginUI();
        Scene scene = new Scene(loginUI, 450, 350);

        stage.setScene(scene);
        stage.setTitle("Student Management System - Login");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
