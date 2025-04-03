package sms.gradle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sms.gradle.model.dao.DatabaseConnection;

public class App extends Application {
    @Override
    public void init() {
        DatabaseConnection.getInstance();
    }

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label(
                "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Student Management System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        launch(args);
    }
}
