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
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.entities.Module;
import sms.gradle.view.CoreViewInterface;

/*
 * Manages Access Modules view for selected student via CoreViewInterface's Template pattern
 */

@Getter
public class AccessStudentModulesView extends BorderPane implements CoreViewInterface {

    private static final Logger LOGGER = LogManager.getLogger();

    private VBox coreContent;
    private GridPane modulesGrid;
    private ScrollPane scrollPane;

    private Label courseNameHeading;

    private Button backButton;
    private Button signoutButton;

    public void accessStudentModules() {
        LOGGER.debug("Initialising Access Student Modules View");
        getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        LOGGER.debug("Access Student Modules View Initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        coreContent = new VBox(20);
        modulesGrid = new GridPane();
        scrollPane = new ScrollPane(modulesGrid);

        courseNameHeading = new Label("Course Modules");
        courseNameHeading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        backButton = new Button("Back to Dashboard");
        signoutButton = new Button("Sign Out");

        backButton.setId("backButton");
        signoutButton.setId("signoutButton");
        modulesGrid.setId("modulesGrid");
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

        HBox buttonsBox = new HBox(20, backButton, signoutButton);
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

        backButton.getStyleClass().add("navigation-button");
        signoutButton.getStyleClass().add("navigation-button");
    }

    public void setCourseNameHeader(String selectedCourseName) {
        courseNameHeading.setText(selectedCourseName + " Modules");
    }

    public void upateModuleToGrid(Module module, int row, int column) {
        VBox moduleCard = createModuleCard(module);
        modulesGrid.add(moduleCard, column, row);
    }

    private VBox createModuleCard(Module module) {

        VBox card = new VBox(10);
        card.getStyleClass().add("module-card");
        card.setPadding(new Insets(15));

        Label moduleNameLabel = new Label(module.getName());
        moduleNameLabel.getStyleClass().add("module-name");

        Label moduleIdLabel = new Label("ID: " + module.getId());
        Label moduleLecturerLabel = new Label("Lecturer: " + module.getLecturer());
        Label moduleDescriptionLabel = new Label("Description: " + module.getDescription());
        moduleDescriptionLabel.setWrapText(true);

        card.getChildren().addAll(moduleNameLabel, moduleIdLabel, moduleLecturerLabel, moduleDescriptionLabel);

        return card;
    }

    public void clearModules() {
        modulesGrid.getChildren().clear();
    }
}
