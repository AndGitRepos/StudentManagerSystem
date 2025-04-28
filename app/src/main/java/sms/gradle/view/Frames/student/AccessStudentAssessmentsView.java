package sms.gradle.view.frames.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.entities.Assessment;
import sms.gradle.view.CoreViewInterface;

public class AccessStudentAssessmentsView extends BorderPane implements CoreViewInterface {

    private static final Logger LOGGER = LogManager.getLogger();

    private ComboBox<String> filterDropDown;
    private VBox leftPanel;
    private ListView<Assessment> assessmentListView;
    private Button selectButton;
    private Button refreshButton;

    private VBox rightPanel;
    private Label assessmentNameLabel;
    private Label assessmentsIdLabel;
    private Label moduleLabel;
    private Label assessmentDescriptionLabel;
    private Label lecturerLabel;
    private Label dueDateLabel;

    private VBox displayResultsArea;
    private VBox graphResultArea; // TODO:

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
        filterDropDown = new ComboBox<>();
        filterDropDown.setPromptText("Filter by Course");
        leftPanel = new VBox(20);
        assessmentListView = new ListView<>();
        selectButton = new Button("SELECT");
        refreshButton = new Button("REFRESH");

        rightPanel = new VBox(15);
        assessmentNameLabel = new Label("Assessment: ");
        assessmentsIdLabel = new Label("ID: ");
        moduleLabel = new Label("Associated Module");
        assessmentDescriptionLabel = new Label("Description: ");
        lecturerLabel = new Label("Lecturer: ");
        dueDateLabel = new Label("Due Date: ");

        displayResultsArea = new VBox(10);
        graphResultArea = new VBox(10);

        backButton = new Button("Back to Dashboard");
        signoutButton = new Button("Sign Out");

        filterDropDown.setId("filterDropDown");
        leftPanel.setId("leftPanel");
        assessmentListView.setId("assessmentListView");
        selectButton.setId("selectButton");
        refreshButton.setId("refreshButton");

        rightPanel.setId("rightPanel");
        assessmentNameLabel.setId("assessmentNameLabel");
        assessmentsIdLabel.setId("assessmentsIdLabel");
        moduleLabel.setId("moduleLabel");
        assessmentDescriptionLabel.setId("assessmentDescriptionLabel");
        lecturerLabel.setId("lecturerLabel");
        dueDateLabel.setId("dueDateLabel");
        displayResultsArea.setId("displayResultsArea");
        graphResultArea.setId("graphResultArea");
    }

    @Override
    public void layoutCoreUIComponents() {

        leftPanel.getChildren().addAll(filterDropDown, assessmentListView, selectButton, refreshButton);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setAlignment(Pos.TOP_CENTER);

        rightPanel
                .getChildren()
                .addAll(
                        assessmentNameLabel,
                        assessmentsIdLabel,
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
