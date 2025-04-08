package sms.gradle;

import javafx.application.Application;
import javafx.stage.Stage;
import sms.gradle.model.dao.DatabaseConnection;
import sms.gradle.view.ViewFactory;

public class App extends Application {

    @Override
    public void init() {
        DatabaseConnection.getInstance();
    }

    @Override
    public void start(Stage stage) {
        ViewFactory.getInstance().changeToLoginStage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
