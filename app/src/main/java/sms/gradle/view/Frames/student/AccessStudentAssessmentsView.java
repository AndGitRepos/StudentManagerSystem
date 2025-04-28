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
import sms.gradle.view.CoreViewInterface;

public class AccessStudentAssessmentsView extends BorderPane implements CoreViewInterface {

    private static final Logger LOGGER = LogManager.getLogger();

    private VBox coreContent;
    private GridPane assessmentsGrid;
    private ScrollPane scrollPane;

    private Label viewHeading;
    private Label courseNameLabel;
    private Label assessmentsNumberLabel;

    private Button refreshButton;
    private Button backButton;
    private Button signoutButton;

    private void setupEventHandlers() {
        // TODO This will be amongst Assessments Controller Pull Request
    }

    public AccessStudentAssessmentsView() {
        LOGGER.debug("Initialising Access Student Assessments View");
        getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        setupEventHandlers();
        LOGGER.debug("Access Student Assessments View Initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        coreContent = new VBox(20);
        assessmentsGrid = new GridPane();
        scrollPane = new ScrollPane(assessmentsGrid);

        viewHeading = new Label("All Assessments");
        viewHeading.setId("assessmentsViewHeading");
        viewHeading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        courseNameLabel = new Label("Course: ");
        courseNameLabel.setId("courseNameLabel");
        courseNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        assessmentsNumberLabel = new Label("Assessment NO: ");
        assessmentsNumberLabel.setId("courseassessmentsNumberLabelNameLabel");
        assessmentsNumberLabel.setStyle("-fx-font-size: 16px;");

        refreshButton = new Button("Refresh Assessments");
        backButton = new Button("Back to Dashboard");
        signoutButton = new Button("Sign Out");

        refreshButton.setId("refreshButton");
        backButton.setId("backButton");
        signoutButton.setId("signoutButton");
        assessmentsGrid.setId("assessmentsGrid");
    }

    @Override
    public void layoutCoreUIComponents() {

        HBox headerBox = new HBox(viewHeading);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 30, 0));

        HBox courseDetails = new HBox(10);
        courseDetails.setAlignment(Pos.CENTER_RIGHT);
        courseDetails.setPadding(new Insets(0, 20, 10, 0));

        HBox refreshContainer = new HBox(refreshButton);
        refreshContainer.setAlignment(Pos.CENTER_RIGHT);
        refreshContainer.setPadding(new Insets(0, 20, 10, 0));

        assessmentsGrid.setVgap(40);
        assessmentsGrid.setHgap(40);
        assessmentsGrid.setPadding(new Insets(20));

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        HBox buttonsBox = new HBox(20, backButton, signoutButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(20, 0, 20, 0));

        coreContent.getChildren().addAll(headerBox, refreshContainer, scrollPane, buttonsBox);
        coreContent.setPadding(new Insets(20));

        setCenter(coreContent);
    }

    @Override
    public void styleCoreUIComponents() {
        getStyleClass().add("assessment-view-container");
        coreContent.getStyleClass().add("core-content");
        scrollPane.getStyleClass().add("assessment-scroll-pane");
        assessmentsGrid.getStyleClass().add("assessments-grid");

        courseNameLabel.getStyleClass().add("course-name-label");
        assessmentsNumberLabel.getStyleClass().add("assessments-number-label");

        refreshButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigation-button");
        signoutButton.getStyleClass().add("navigation-button");
    }
}
