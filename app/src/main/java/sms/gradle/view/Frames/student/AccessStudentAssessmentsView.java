package sms.gradle.view.frames.student;

import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.StudentControllers.AccessStudentAssessmentsController;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Course;
import sms.gradle.view.CoreViewInterface;

public class AccessStudentAssessmentsView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private ComboBox<Course> filterDropDown = new ComboBox<>();
    private VBox leftPanel = new VBox(20);
    private ListView<Assessment> assessmentListView = new ListView<>();
    private Button selectButton = new Button("SELECT");

    private VBox rightPanel = new VBox(15);
    private Label assessmentNameLabel = new Label("Assessment: ");
    private Label assessmentIdLabel = new Label("ID: ");
    private Label moduleLabel = new Label("Associated Module");
    private Label assessmentDescriptionLabel = new Label("Description: ");
    private Label lecturerLabel = new Label("Lecturer: ");
    private Label dueDateLabel = new Label("Due Date: ");

    private VBox displayResultsArea = new VBox(10);
    private VBox graphResultArea = new VBox(10); // TODO:

    private Label viewHeading = new Label("All Assessments");
    private Label courseNameLabel = new Label("Course: ");
    private Label assessmentsNumberLabel = new Label("Assessment NO: ");

    private Button refreshButton = new Button("REFRESH");
    private Button backButton = new Button("Back to Dashboard");
    private Button signoutButton = new Button("Sign Out");

    private void setupEventHandlers() {
        filterDropDown.setOnAction(AccessStudentAssessmentsController::handleFilterAssessments);
        selectButton.setOnAction(AccessStudentAssessmentsController::handleSelectButton);
        refreshButton.setOnAction(AccessStudentAssessmentsController::handleRefreshButton);
        backButton.setOnAction(AccessStudentAssessmentsController::handleBackButton);
        signoutButton.setOnAction(AccessStudentAssessmentsController::handleSignoutButton);
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

    private void setComponentIds() {
        viewHeading.setId("assessmentsViewHeading");
        courseNameLabel.setId("courseNameLabel");
        assessmentsNumberLabel.setId("courseassessmentsNumberLabelNameLabel");
        refreshButton.setId("refreshButton");

        rightPanel.setId("rightPanel");
        assessmentNameLabel.setId("assessmentNameLabel");
        assessmentIdLabel.setId("assessmentIdLabel");
        moduleLabel.setId("moduleLabel");
        assessmentDescriptionLabel.setId("assessmentDescriptionLabel");
        lecturerLabel.setId("lecturerLabel");
        dueDateLabel.setId("dueDateLabel");
        displayResultsArea.setId("displayResultsArea");
        graphResultArea.setId("graphResultArea");

        filterDropDown.setId("filterDropDown");
        leftPanel.setId("leftPanel");
        assessmentListView.setId("assessmentListView");
        selectButton.setId("selectButton");
    }

    private static class AssessmentListCell extends ListCell<Assessment> {
        @Override
        protected void updateItem(Assessment assessment, boolean empty) {
            super.updateItem(assessment, empty);

            if (empty || assessment == null) {
                setText(null);
                return;
            }
            String moduleName;
            try {
                moduleName = ModuleDAO.findById(assessment.getModuleId())
                        .map(sms.gradle.model.entities.Module::getName)
                        .orElse("Unknown Module");
            } catch (SQLException e) {
                moduleName = "Unknown Module";
            }

            String displayText = String.format(
                    "%s (ID: %d)\n%s\nDue: %s\nModule: %s",
                    assessment.getName(),
                    assessment.getId(),
                    assessment.getDescription(),
                    assessment.getDueDate(),
                    moduleName);

            setText(displayText);
            setWrapText(true);
        }
    }

    @Override
    public void initialiseCoreUIComponents() {

        filterDropDown.setPromptText("Filter by Course");
        filterDropDown.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                if (course == null) {
                    return "";
                }
                return String.format(course.getName());
            }

            @Override
            public Course fromString(String string) {
                return null;
            }
        });

        assessmentListView.setCellFactory(listView -> new AssessmentListCell());

        filterDropDown.setPromptText("Filter by Course");

        displayResultsArea.setPrefSize(200, 200);
        displayResultsArea.setAlignment(Pos.CENTER);

        viewHeading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        courseNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        assessmentsNumberLabel.setStyle("-fx-font-size: 16px;");

        leftPanel.getChildren().addAll(filterDropDown, assessmentListView, selectButton, refreshButton);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setAlignment(Pos.TOP_CENTER);
        setComponentIds();
    }

    @Override
    public void layoutCoreUIComponents() {
        HBox headerBox = new HBox(viewHeading);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 30, 0));

        rightPanel
                .getChildren()
                .addAll(
                        assessmentNameLabel,
                        assessmentIdLabel,
                        moduleLabel,
                        lecturerLabel,
                        assessmentDescriptionLabel,
                        dueDateLabel);

        HBox gradeArea = new HBox(20);
        gradeArea.getChildren().addAll(displayResultsArea, graphResultArea);
        rightPanel.getChildren().add(gradeArea);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setMaxHeight(400);
        rightPanel.setPrefWidth(450);

        rightPanel.setTranslateY(80);

        HBox navigationButtons = new HBox(20);
        navigationButtons.getChildren().addAll(backButton, signoutButton);
        navigationButtons.setAlignment(Pos.CENTER);
        navigationButtons.setPadding(new Insets(20));

        setLeft(leftPanel);
        setRight(rightPanel);
        setCenter(gradeArea);
        setBottom(navigationButtons);
    }

    @Override
    public void styleCoreUIComponents() {
        leftPanel.getStyleClass().add("left-panel");
        filterDropDown.getStyleClass().add("filter-drop-down");
        assessmentListView.getStyleClass().add("assessment-view-container");
        selectButton.getStyleClass().add("action-button");
        refreshButton.getStyleClass().add("action-button");

        rightPanel.getStyleClass().add("right-panel");
        displayResultsArea.getStyleClass().add("results-grade");
        graphResultArea.getStyleClass().add("results-graph");

        backButton.getStyleClass().add("navigation-button");
        signoutButton.getStyleClass().add("navigation-button");
    }
}
