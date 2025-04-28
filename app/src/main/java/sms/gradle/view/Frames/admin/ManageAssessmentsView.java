package sms.gradle.view.Frames.admin;

import static javafx.geometry.Pos.CENTER;

import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.ManageAssessmentsController;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Course;
import sms.gradle.utils.Common;
import sms.gradle.view.CoreViewInterface;
import sms.gradle.view.ViewFactory;

public class ManageAssessmentsView extends BorderPane implements CoreViewInterface {

    private static final Logger LOGGER = LogManager.getLogger();

    // Left Panel Components
    private final ListView<Assessment> assessmentsListView = new ListView<>();
    private final Button refreshButton = new Button("Refresh");
    private final Button selectButton = new Button("Select");
    private final Button deleteButton = new Button("Delete");
    private final VBox leftPanel = new VBox(10);

    // Center Panel Components
    private final GridPane assessmentDetailsPane = new GridPane();
    private final TextField assessmentIdField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField descriptionField = new TextField();
    private final DatePicker dueDatePicker = new DatePicker();
    private final Button createNewButton = new Button("Create New");
    private final Button updateButton = new Button("Update");
    private final VBox centerPanel = new VBox(10);

    // Right Panel Components
    private final VBox linkedModuleContainer = new VBox(10);
    private final Label linkedModuleNameLabel = new Label();
    private final Label linkedModuleIdLabel = new Label();
    private final Label linkedModuleDescriptionLabel = new Label();
    private final Label linkedModuleLecturerLabel = new Label();
    private final Label linkedModuleCourseNameLabel = new Label();
    private final ListView<sms.gradle.model.entities.Module> unlinkedModulesListView = new ListView<>();
    private final Button swapButton = new Button("Swap");
    private final Button backButton = new Button("Back");
    private final VBox rightPanel = new VBox(10);

    public ManageAssessmentsView() {
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        setupEventHandlers();
        LOGGER.debug("Manage Assessments View initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        setComponentIds();
    }

    private void setComponentIds() {
        assessmentsListView.setId("assessmentsListView");
        refreshButton.setId("refreshButton");
        selectButton.setId("selectButton");
        deleteButton.setId("deleteButton");
        assessmentIdField.setId("assessmentIdField");
        nameField.setId("nameField");
        descriptionField.setId("descriptionField");
        dueDatePicker.setId("dueDatePicker");
        createNewButton.setId("updateButton");
        updateButton.setId("updateButton");
        linkedModuleContainer.setId("linkedModuleContainer");
        linkedModuleNameLabel.setId("linkedModuleNameLabel");
        linkedModuleIdLabel.setId("linkedModuleIdLabel");
        linkedModuleDescriptionLabel.setId("linkedModuleDescriptionLabel");
        linkedModuleLecturerLabel.setId("linkedModuleLecturerLabel");
        linkedModuleCourseNameLabel.setId("linkedModuleCourseNameLabel");
        unlinkedModulesListView.setId("unlinkedModulesListView");
        swapButton.setId("swapButton");
        backButton.setId("backButton");
    }

    @Override
    public void layoutCoreUIComponents() {
        layoutLeftPanel();
        layoutCenterPanel();
        layoutRightPanel();
    }

    private void layoutLeftPanel() {
        leftPanel
                .getChildren()
                .addAll(
                        new HBox(10, new Label("Assessments"), refreshButton),
                        assessmentsListView,
                        new HBox(10, selectButton, deleteButton));
        leftPanel.setPadding(new Insets(10));
        setLeft(leftPanel);
    }

    private void layoutCenterPanel() {
        assessmentDetailsPane.setHgap(10);
        assessmentDetailsPane.setVgap(10);
        assessmentDetailsPane.addRow(0, new Label("Assessment ID:"), assessmentIdField);
        assessmentDetailsPane.addRow(1, new Label("Name:"), nameField);
        assessmentDetailsPane.addRow(2, new Label("Description:"), descriptionField);
        assessmentDetailsPane.addRow(3, new Label("Due Date:"), dueDatePicker);

        HBox buttonBox = new HBox(10, createNewButton, updateButton);
        buttonBox.setAlignment(CENTER);

        centerPanel.getChildren().addAll(new Label("Assessment Details"), assessmentDetailsPane, buttonBox);
        centerPanel.setPadding(new Insets(10));
        setCenter(centerPanel);
    }

    private void layoutRightPanel() {
        HBox topLine = new HBox(linkedModuleNameLabel, linkedModuleIdLabel);
        topLine.setSpacing(5);

        linkedModuleContainer
                .getChildren()
                .addAll(topLine, linkedModuleDescriptionLabel, linkedModuleLecturerLabel, linkedModuleCourseNameLabel);

        VBox modulesBox = new VBox(
                10,
                new Label("Linked Module"),
                linkedModuleContainer,
                swapButton,
                new Label("Unlinked Modules"),
                unlinkedModulesListView);

        HBox controlButtons = new HBox(10, backButton);
        controlButtons.setAlignment(CENTER);

        rightPanel.getChildren().addAll(modulesBox, controlButtons);
        rightPanel.setPadding(new Insets(10));
        setRight(rightPanel);
    }

    @Override
    public void styleCoreUIComponents() {
        applyBasicStyles();
        setupAssessmentsListViewCellFactory();
        setupUnlinkedModulesListViewCellFactory();
    }

    private void applyBasicStyles() {
        // Main container
        getStyleClass().add("manage-assessments-view");

        // Style containers and set widths as percentages
        leftPanel.getStyleClass().add("left-panel");
        centerPanel.getStyleClass().add("center-panel");
        rightPanel.getStyleClass().add("right-panel");

        // Add width binding to maintain proportions
        widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            // Left panel (25%)
            leftPanel.setPrefWidth(width * 0.25);
            leftPanel.setMinWidth(width * 0.25);
            leftPanel.setMaxWidth(width * 0.25);

            // Center panel (50%)
            centerPanel.setPrefWidth(width * 0.50);
            centerPanel.setMinWidth(width * 0.50);
            centerPanel.setMaxWidth(width * 0.50);

            // Right panel (25%)
            rightPanel.setPrefWidth(width * 0.25);
            rightPanel.setMinWidth(width * 0.25);
            rightPanel.setMaxWidth(width * 0.25);
        });

        // Other styles
        assessmentsListView.getStyleClass().add("assessments-list");
        unlinkedModulesListView.getStyleClass().add("modules-list");
        assessmentDetailsPane.getStyleClass().add("details-pane");

        applyLabelStyles();
        applyButtonStyles();
    }

    private void applyLabelStyles() {
        linkedModuleNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        linkedModuleIdLabel.setStyle("-fx-font-style: italic; -fx-text-fill: grey; -fx-font-size: 12px;");
        linkedModuleDescriptionLabel.setStyle("-fx-font-size: 12px;");
        linkedModuleLecturerLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #555;");
        linkedModuleCourseNameLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #555;");
    }

    private void applyButtonStyles() {
        refreshButton.getStyleClass().add("action-button");
        selectButton.getStyleClass().add("action-button");
        deleteButton.getStyleClass().add("action-button");
        createNewButton.getStyleClass().add("action-button");
        updateButton.getStyleClass().add("action-button");
        swapButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigate-button");
    }

    private void setupAssessmentsListViewCellFactory() {
        assessmentsListView.setCellFactory(lv -> new AssessmentListCell());
    }

    private void setupUnlinkedModulesListViewCellFactory() {
        unlinkedModulesListView.setCellFactory(lv -> new ModuleListCell());
    }

    private void setupEventHandlers() {
        refreshButton.setOnAction(ManageAssessmentsController::updateListOfAssessments);
        selectButton.setOnAction(ManageAssessmentsController::selectAssessment);
        createNewButton.setOnAction(ManageAssessmentsController::createNewAssessment);
        updateButton.setOnAction(ManageAssessmentsController::updateAssessment);
        deleteButton.setOnAction(ManageAssessmentsController::deleteAssessment);
        swapButton.setOnAction(ManageAssessmentsController::swapLinkedModule);
        backButton.setOnAction(event -> ViewFactory.getInstance().changeToAdminDashboardStage());
    }

    private static class AssessmentListCell extends ListCell<Assessment> {
        @Override
        protected void updateItem(Assessment assessment, boolean empty) {
            super.updateItem(assessment, empty);
            if (empty || assessment == null) {
                setText(null);
                setGraphic(null);
                return;
            }
            setGraphic(createAssessmentDisplay(assessment));
        }

        private VBox createAssessmentDisplay(Assessment assessment) {
            // Get module name using DAO
            String moduleName;
            try {
                moduleName = ModuleDAO.findById(assessment.getModuleId())
                        .map(sms.gradle.model.entities.Module::getName)
                        .orElse("Unknown Module");
            } catch (SQLException e) {
                moduleName = "Unknown Module";
            }

            Label nameLabel =
                    Common.createStyledLabel(assessment.getName(), "-fx-font-weight: bold; -fx-font-size: 14px;");

            Label idLabel = Common.createStyledLabel(
                    "(" + assessment.getId() + ")",
                    "-fx-font-style: italic; -fx-text-fill: grey; -fx-font-size: 12px;");

            HBox topLine = new HBox(5, nameLabel, idLabel);

            Label descLabel = Common.createStyledLabel(assessment.getDescription(), "-fx-font-size: 12px;");

            Label dueDateLabel = Common.createStyledLabel(
                    "Due: " + assessment.getDueDate(), "-fx-font-size: 11px; -fx-text-fill: #555;");

            Label moduleLabel =
                    Common.createStyledLabel("Module: " + moduleName, "-fx-font-size: 11px; -fx-text-fill: #555;");

            VBox content = new VBox(2, topLine, descLabel, dueDateLabel, moduleLabel);
            content.setPadding(new Insets(5));

            return content;
        }
    }

    private static class ModuleListCell extends ListCell<sms.gradle.model.entities.Module> {
        @Override
        protected void updateItem(sms.gradle.model.entities.Module module, boolean empty) {
            super.updateItem(module, empty);

            if (empty || module == null) {
                setText(null);
                setGraphic(null);
                return;
            }
            setGraphic(createModuleDisplay(module));
        }

        private VBox createModuleDisplay(sms.gradle.model.entities.Module module) {
            // Lookup course name
            String courseName;
            try {
                courseName = CourseDAO.findById(module.getCourseId())
                        .map(Course::getName)
                        .orElse("Unknown Course");
            } catch (SQLException e) {
                courseName = "Unknown Course";
            }

            Label nameLabel = Common.createStyledLabel(module.getName(), "-fx-font-weight: bold; -fx-font-size: 14px;");

            Label idLabel = Common.createStyledLabel(
                    " (" + module.getId() + ")", "-fx-font-style: italic; -fx-text-fill: grey; -fx-font-size: 12px;");

            HBox topLine = new HBox(5, nameLabel, idLabel);

            Label descLabel = Common.createStyledLabel(module.getDescription(), "-fx-font-size: 12px;");

            Label lecturerLabel = Common.createStyledLabel(
                    "Lecturer: " + module.getLecturer(), "-fx-font-size: 11px; -fx-text-fill: #555;");

            Label courseLabel =
                    Common.createStyledLabel("Course: " + courseName, "-fx-font-size: 11px; -fx-text-fill: #555;");

            VBox content = new VBox(2, topLine, descLabel, lecturerLabel, courseLabel);
            content.setPadding(new Insets(5));

            return content;
        }
    }
}
