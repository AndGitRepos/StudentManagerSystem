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
import sms.gradle.controller.StudentControllers.AccessStudentAssessmentsController;
import sms.gradle.model.entities.Assessment;
import sms.gradle.view.CoreViewInterface;
import sms.gradle.view.components.GradeCircle;

public class AccessStudentAssessmentsView extends BorderPane implements CoreViewInterface {

    private static final Logger LOGGER = LogManager.getLogger();

    private VBox coreContent;
    private GridPane assessmentsGrid;
    private ScrollPane scrollPane;

    private Label viewHeading;

    private Button backButton;
    private Button signoutButton;

    private VBox createAssessmentCard(Assessment assessment, int result) {

        VBox card = new VBox(15);
        card.getStyleClass().add("assessment-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));

        GradeCircle gradeCircle = new GradeCircle(result);

        Label assessmenteNameLabel = new Label(assessment.getName());
        assessmenteNameLabel.getStyleClass().add("assessment-name");

        Label assessmentIdLabel = new Label("ID: " + assessment.getId());
        assessmentIdLabel.getStyleClass().add("assessment-id");

        card.getChildren().addAll(gradeCircle, assessmenteNameLabel, assessmentIdLabel);

        return card;
    }

    private void setupEventHandler() {
        backButton.setOnAction(AccessStudentAssessmentsController::handleBackButton);
        signoutButton.setOnAction(AccessStudentAssessmentsController::handleSignoutButton);
    }

    public AccessStudentAssessmentsView() {

        LOGGER.debug("Initialising Access Student Assessments View");
        getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        setupEventHandler();
        LOGGER.debug("Access Student Assessments View Initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        coreContent = new VBox(20);
        assessmentsGrid = new GridPane();
        scrollPane = new ScrollPane(assessmentsGrid);

        viewHeading = new Label("Modules' Assessments");
        viewHeading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        backButton = new Button("Back to Dashboard");
        signoutButton = new Button("Sign Out");

        backButton.setId("backButton");
        signoutButton.setId("signoutButton");
        assessmentsGrid.setId("assessmentsGrid");
    }

    @Override
    public void layoutCoreUIComponents() {

        HBox headerBox = new HBox(viewHeading);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 30, 0));

        assessmentsGrid.setVgap(40);
        assessmentsGrid.setHgap(40);
        assessmentsGrid.setPadding(new Insets(20));

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
        getStyleClass().add("assessment-view-container");
        coreContent.getStyleClass().add("core-content");
        scrollPane.getStyleClass().add("assessment-scroll-pane");
        assessmentsGrid.getStyleClass().add("assessments-grid");

        backButton.getStyleClass().add("navigation-button");
        signoutButton.getStyleClass().add("navigation-button");
    }

    public void updateAssessmentToGrid(Assessment assessment, int result, int row, int column) {
        VBox assessmentCard = createAssessmentCard(assessment, result);
        assessmentsGrid.add(assessmentCard, column, row);
        LOGGER.debug("Added assessment {} to grid, positioned: ({}, {})", assessment.getName(), row,
                column);
    }

    public void clearAssessments() {
        assessmentsGrid.getChildren().clear();
        LOGGER.debug("All assessments have been cleared from grid");
    }
}
