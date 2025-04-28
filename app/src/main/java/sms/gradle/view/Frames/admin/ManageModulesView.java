package sms.gradle.view.frames.admin;

import static javafx.geometry.Pos.CENTER;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.ManageModuleController;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.Module;
import sms.gradle.view.CoreViewInterface;
import sms.gradle.view.ViewFactory;

public class ManageModulesView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    // Left Panel Components
    private final ListView<Module> modulesListView = new ListView<>();
    private final Button refreshButton = new Button("Refresh");
    private final Button selectButton = new Button("Select");
    private final Button deleteButton = new Button("Delete");
    private final VBox leftPanel = new VBox(10);

    // Center Panel Components
    private final GridPane moduleDetailsPane = new GridPane();
    private final TextField moduleIdField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField descriptionField = new TextField();
    private final TextField lecturerField = new TextField();
    private final Button createNewButton = new Button("Create New");
    private final Button updateButton = new Button("Update");
    private final VBox centerPanel = new VBox(10);

    // Right Panel Components
    private final VBox linkedCourseContainer = new VBox(10);
    private final Label linkedCourseNameLabel = new Label();
    private final Label linkedCourseIdLabel = new Label();
    private final Label linkedCourseDescriptionLabel = new Label();
    private final ListView<Course> unlinkedCoursesListView = new ListView<>();
    private final Button swapButton = new Button("Swap");
    private final Button backButton = new Button("Back");
    private final VBox rightPanel = new VBox(10);

    public ManageModulesView() {
        LOGGER.debug("Initialising Manage Modules View");
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        setupEventHandlers();
        LOGGER.debug("Manage Modules View initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        setComponentIds();
    }

    private void setComponentIds() {
        modulesListView.setId("modulesListView");
        refreshButton.setId("refreshButton");
        selectButton.setId("selectButton");
        deleteButton.setId("deleteButton");

        moduleIdField.setId("moduleIdField");
        moduleIdField.setEditable(false);
        nameField.setId("nameField");
        descriptionField.setId("descriptionField");
        lecturerField.setId("lecturerField");
        createNewButton.setId("updateButton");
        updateButton.setId("updateButton");

        linkedCourseContainer.setId("linkedCourseContainer");
        linkedCourseNameLabel.setId("linkedCourseNameLabel");
        linkedCourseIdLabel.setId("linkedCourseIdLabel");
        linkedCourseDescriptionLabel.setId("linkedCourseDescriptionLabel");
        unlinkedCoursesListView.setId("unlinkedCoursesListView");
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
                        new HBox(10, new Label("Modules"), refreshButton),
                        modulesListView,
                        new HBox(10, selectButton, deleteButton));
        leftPanel.setPadding(new Insets(10));
        setLeft(leftPanel);
    }

    private void layoutCenterPanel() {
        moduleDetailsPane.setHgap(10);
        moduleDetailsPane.setVgap(10);
        moduleDetailsPane.addRow(0, new Label("Module ID:"), moduleIdField);
        moduleDetailsPane.addRow(1, new Label("Name:"), nameField);
        moduleDetailsPane.addRow(2, new Label("Description:"), descriptionField);
        moduleDetailsPane.addRow(3, new Label("Lecturer:"), lecturerField);

        HBox buttonBox = new HBox(10, createNewButton, updateButton);
        buttonBox.setAlignment(CENTER);

        centerPanel.getChildren().addAll(new Label("Module Details"), moduleDetailsPane, buttonBox);
        centerPanel.setPadding(new Insets(10));
        setCenter(centerPanel);
    }

    private void layoutRightPanel() {
        HBox topLine = new HBox(linkedCourseNameLabel, linkedCourseIdLabel);
        topLine.setSpacing(5);

        linkedCourseContainer.getChildren().addAll(topLine, linkedCourseDescriptionLabel);

        VBox coursesBox = new VBox(
                10,
                new Label("Linked Course"),
                linkedCourseContainer,
                swapButton,
                new Label("Unlinked Courses"),
                unlinkedCoursesListView);

        HBox controlButtons = new HBox(10, backButton);
        controlButtons.setAlignment(CENTER);

        rightPanel.getChildren().addAll(coursesBox, controlButtons);
        rightPanel.setPadding(new Insets(10));
        setRight(rightPanel);
    }

    @Override
    public void styleCoreUIComponents() {
        applyBasicStyles();
        setupModuleListViewCellFactory();
        setupUnlinkedCoursesListViewCellFactory();
    }

    private void applyBasicStyles() {
        setPrefSize(750, 550);
        modulesListView.setPrefWidth(150);
        unlinkedCoursesListView.setPrefWidth(150);

        getStyleClass().add("manage-modules-view");
        modulesListView.getStyleClass().add("modules-list");
        moduleDetailsPane.getStyleClass().add("details-pane");
        unlinkedCoursesListView.getStyleClass().add("courses-list");

        applyLabelStyles();
        applyButtonStyles();
    }

    private void applyLabelStyles() {
        linkedCourseNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        linkedCourseIdLabel.setStyle("-fx-font-style: italic; -fx-text-fill: grey; -fx-font-size: 12px;");
        linkedCourseDescriptionLabel.setStyle("-fx-font-size: 12px;");
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

    private void setupModuleListViewCellFactory() {
        modulesListView.setCellFactory(lv -> new ModuleListCell());
    }

    private void setupUnlinkedCoursesListViewCellFactory() {
        unlinkedCoursesListView.setCellFactory(lv -> new CourseListCell());
    }

    private void setupEventHandlers() {
        refreshButton.setOnAction(ManageModuleController::updateListOfModules);
        selectButton.setOnAction(ManageModuleController::selectModule);
        createNewButton.setOnAction(ManageModuleController::createNewModule);
        updateButton.setOnAction(ManageModuleController::updateModule);
        deleteButton.setOnAction(ManageModuleController::deleteModule);
        swapButton.setOnAction(ManageModuleController::swapLinkedCourse);
        backButton.setOnAction(event -> ViewFactory.getInstance().changeToAdminDashboardStage());
    }

    private static class ModuleListCell extends ListCell<Module> {
        @Override
        protected void updateItem(Module module, boolean empty) {
            super.updateItem(module, empty);
            if (empty || module == null) {
                setText(null);
                setGraphic(null);
                return;
            }
            setGraphic(createModuleDisplay(module));
        }

        private VBox createModuleDisplay(Module module) {
            Label nameLabel = createStyledLabel(module.getName(), "-fx-font-weight: bold; -fx-font-size: 14px;");
            Label idLabel = createStyledLabel(
                    " (" + module.getId() + ")", "-fx-font-style: italic; -fx-text-fill: grey; -fx-font-size: 12px;");

            HBox topLine = new HBox(5, nameLabel, idLabel);

            Label descLabel = createStyledLabel(module.getDescription(), "-fx-font-size: 12px;");
            Label lecturerLabel =
                    createStyledLabel("Lecturer: " + module.getLecturer(), "-fx-font-size: 11px; -fx-text-fill: #555;");

            VBox content = new VBox(2, topLine, descLabel, lecturerLabel);
            content.setPadding(new Insets(5));
            return content;
        }

        private Label createStyledLabel(String text, String style) {
            Label label = new Label(text);
            label.setStyle(style);
            return label;
        }
    }

    private static class CourseListCell extends ListCell<Course> {
        @Override
        protected void updateItem(Course course, boolean empty) {
            super.updateItem(course, empty);
            if (empty || course == null) {
                setText(null);
                setGraphic(null);
                return;
            }
            setGraphic(createCourseDisplay(course));
        }

        private VBox createCourseDisplay(Course course) {
            Label nameLabel = createStyledLabel(course.getName(), "-fx-font-weight: bold; -fx-font-size: 14px;");
            Label idLabel = createStyledLabel(
                    " (" + course.getId() + ")", "-fx-font-style: italic; -fx-text-fill: grey; -fx-font-size: 12px;");

            HBox topLine = new HBox(5, nameLabel, idLabel);

            Label descLabel = createStyledLabel(course.getDescription(), "-fx-font-size: 12px;");

            VBox content = new VBox(2, topLine, descLabel);
            content.setPadding(new Insets(5));
            return content;
        }

        private Label createStyledLabel(String text, String style) {
            Label label = new Label(text);
            label.setStyle(style);
            return label;
        }
    }
}
