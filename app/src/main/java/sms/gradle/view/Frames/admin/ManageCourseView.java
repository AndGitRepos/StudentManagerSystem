package sms.gradle.view.frames.admin;

import static javafx.geometry.Pos.CENTER;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.ManageCourseController;
import sms.gradle.model.entities.Course;
import sms.gradle.view.CoreViewInterface;

@Getter
public class ManageCourseView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private GridPane courseDetailsPane;
    private ListView<Course> courseListView;
    private Button selectButton;
    private TextField courseIdField;
    private TextField courseNameField;
    private TextArea courseDescriptionArea;
    private Button updateButton;
    private Button createNewButton;
    private Button deleteButton;
    private Button refreshButton;
    private Button backButton;
    private Button logoutButton;
    private VBox centerPanel;

    public ManageCourseView() {
        LOGGER.debug("Initialising Manage Course View");
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        setComponentIds();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        setEventHandlers();
        LOGGER.debug("Manage Course View initialised");
    }

    private void setEventHandlers() {
        selectButton.setOnAction(ManageCourseController::selectCourse);
        createNewButton.setOnAction(ManageCourseController::createNewCourse);
        updateButton.setOnAction(ManageCourseController::updateCourse);
        deleteButton.setOnAction(ManageCourseController::deleteCourse);
        backButton.setOnAction(ManageCourseController::handleBackButton);
        logoutButton.setOnAction(ManageCourseController::handleLogoutButton);
        refreshButton.setOnAction(ManageCourseController::refreshListOfCourses);
    }

    private void setComponentIds() {
        courseListView.setId("courseListView");
        selectButton.setId("selectButton");
        createNewButton.setId("createNewButton");
        updateButton.setId("updateButton");
        deleteButton.setId("deleteButton");
        refreshButton.setId("refreshButton");
        backButton.setId("backButton");
        logoutButton.setId("logoutButton");
        courseIdField.setId("courseIdField");
        courseNameField.setId("courseNameField");
        courseDescriptionArea.setId("courseDescriptionArea");
    }

    @Override
    public void initialiseCoreUIComponents() {
        courseListView = new ListView<>();
        selectButton = new Button("Select");
        createNewButton = new Button("Create New");
        courseDetailsPane = new GridPane();
        courseIdField = new TextField();
        courseIdField.setEditable(false);
        courseNameField = new TextField();
        courseDescriptionArea = new TextArea();
        updateButton = new Button("Update");
        deleteButton = new Button("Delete");
        refreshButton = new Button("Refresh");
        centerPanel = new VBox(10);
        backButton = new Button("Back");
        logoutButton = new Button("Logout");
    }

    @Override
    public void layoutCoreUIComponents() {
        VBox courseListSection = new VBox(10);
        courseListSection.getStyleClass().add("left-panel");
        Label coursesLabel = new Label("Courses");
        coursesLabel.getStyleClass().add("section-header");
        courseListSection.getChildren().addAll(coursesLabel, refreshButton, courseListView, selectButton);
        courseListSection.setAlignment(CENTER);
        setLeft(courseListSection);
        courseListSection.setPadding(new Insets(10));

        courseDetailsPane.setHgap(10);
        courseDetailsPane.setVgap(10);

        Label detailsLabel = new Label("Course Details");
        detailsLabel.getStyleClass().add("section-header");

        courseDescriptionArea.setPrefRowCount(3);
        courseDescriptionArea.setPrefWidth(300);
        courseDescriptionArea.setWrapText(true);

        courseDetailsPane.addRow(0, new Label("Course ID:"), courseIdField);
        courseDetailsPane.addRow(1, new Label("Course Name:"), courseNameField);
        courseDetailsPane.addRow(2, new Label("Course Description:"), courseDescriptionArea);

        HBox actionButtons = new HBox(10, createNewButton, updateButton, deleteButton);
        actionButtons.setAlignment(CENTER);

        HBox navigationButtons = new HBox(10, backButton, logoutButton);
        navigationButtons.setAlignment(CENTER);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        centerPanel.getChildren().clear();
        centerPanel.getChildren().addAll(detailsLabel, courseDetailsPane, actionButtons, spacer, navigationButtons);
        centerPanel.setAlignment(CENTER);
        centerPanel.setPadding(new Insets(10));

        setCenter(centerPanel);
    }

    @Override
    public void styleCoreUIComponents() {
        setPrefSize(750, 550);
        courseListView.setPrefWidth(150);

        getStyleClass().add("manage-course-view");
        centerPanel.getStyleClass().add("center-panel");

        selectButton.getStyleClass().add("action-button");
        deleteButton.getStyleClass().add("action-button");
        refreshButton.getStyleClass().add("action-button");
        createNewButton.getStyleClass().add("action-button");
        updateButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigate-button");
        logoutButton.getStyleClass().add("navigate-button");

        courseListView.getStyleClass().add("course-list");
        courseDetailsPane.getStyleClass().add("details-pane");
    }
}
