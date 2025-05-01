package sms.gradle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.DatabaseConnection;
import sms.gradle.utils.MockDatabaseGenerator;
import sms.gradle.view.ViewFactory;

public class App extends Application {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void init() {
        DatabaseConnection.getInstance();
        if (!DatabaseConnection.getInstance().isPopulated()) {
            try {
                LOGGER.info("Populating database");
                MockDatabaseGenerator.populateTables();
            } catch (Exception e) {
                LOGGER.error("Failed to populate tables: ", e);
            }
        }
    }

    @Override
    public void start(Stage stage) {
        LOGGER.debug("Starting application");

        Scene scene = new Scene(new javafx.scene.layout.StackPane(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/common.css").toExternalForm());
        stage.setScene(scene);

        ViewFactory.getInstance().changeToLoginStage();
    }

    public static void main(String[] args) {
        LOGGER.debug("Launching application");
        launch(args);
    }
}
