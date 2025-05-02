package sms.gradle.view.frames.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.student.AccessStudentModulesController;
import sms.gradle.view.CoreViewInterface;

/*
 * Manages Access Modules view for selected student via CoreViewInterface's Template pattern
 */
public class AccessStudentModulesView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private VBox coreContent = new VBox(20);
    private GridPane modulesGrid = new GridPane();
    private ScrollPane scrollPane = new ScrollPane(modulesGrid);

    private Label courseNameHeading = new Label("Course Modules");

    private Button refreshButton = new Button("Refresh Modules");
    private Button backButton = new Button("Back to Dashboard");
    private Button signoutButton = new Button("Sign Out");

    private void setupEventHandlers() {
        refreshButton.setOnAction(AccessStudentModulesController::handleRefreshModulesButton);
        backButton.setOnAction(AccessStudentModulesController::handleBackButton);
        signoutButton.setOnAction(AccessStudentModulesController::handleSignoutButton);
    }

    public AccessStudentModulesView() {
        LOGGER.debug("Initialising Access Student Modules View");
        getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        setupEventHandlers();
        LOGGER.debug("Access Student Modules View Initialised");
    }

    private void setComponentIds() {
        courseNameHeading.setId("courseNameHeading");
        refreshButton.setId("refreshButton");
        backButton.setId("backButton");
        signoutButton.setId("signoutButton");
        modulesGrid.setId("modulesGrid");
    }

    @Override
    public void initialiseCoreUIComponents() {
        courseNameHeading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        setComponentIds();
    }

    @Override
    public void layoutCoreUIComponents() {

        HBox headerBox = new HBox(courseNameHeading);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 30, 0));

        modulesGrid.setVgap(20);
        modulesGrid.setHgap(20);
        modulesGrid.setPadding(new Insets(20));

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        HBox buttonsBox = new HBox(20, backButton, signoutButton, refreshButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(20, 0, 20, 0));

        coreContent.getChildren().addAll(headerBox, scrollPane, buttonsBox);
        coreContent.setPadding(new Insets(20));

        setCenter(coreContent);
    }

    @Override
    public void styleCoreUIComponents() {
        getStyleClass().add("module-view-container");
        coreContent.getStyleClass().add("core-content");
        scrollPane.getStyleClass().add("module-scroll-pane");
        modulesGrid.getStyleClass().add("modules-grid");

        refreshButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigation-button");
        signoutButton.getStyleClass().add("navigation-button");
    }
}
