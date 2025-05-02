package sms.gradle.view.frames.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.student.StudentDashboardController;
import sms.gradle.model.entities.Course;
import sms.gradle.view.CoreViewInterface;

/*
 * Manages StudentDashboard view via CoreViewInterface's Template pattern
 */
public class StudentDashboardView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private VBox profileSection = new VBox(20);
    private VBox academiaSection = new VBox(20);
    private ListView<Course> courseListView = new ListView<>();

    private Button selectCourseButton = new Button("Select Course");
    private Button accessAssessmentsViewButton = new Button("View All Assessments");
    private Button viewStatsButton = new Button("View Overall Course Stats");
    private Button signoutButton = new Button("Sign Out");
    private Button refreshButton = new Button("Refresh Courses");

    private Label studentNameLabel = new Label("Student: ");
    private Label studentEmailLabel = new Label("Email: ");
    private Label studentJoinDateLabel = new Label("Join Date: ");

    private void setupProfileSection() {
        Label profileHeader = new Label("PROFILE");
        profileHeader.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        profileHeader.setPadding(new Insets(0, 0, 20, 0));

        Circle profilePhoto = new Circle(50, Color.GRAY);

        Tooltip.install(profilePhoto, new Tooltip("Temporary holder for profile photo"));

        profileSection
                .getChildren()
                .addAll(profileHeader, profilePhoto, studentEmailLabel, studentJoinDateLabel, studentNameLabel);
        profileSection.setAlignment(Pos.TOP_CENTER);
        profileSection.setPrefWidth(350);
        profileSection.setPadding(new Insets(20));
    }

    private void setupAcademiaSection() {
        Label academicsHeader = new Label("ACADEMICS");
        academicsHeader.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        academicsHeader.setPadding(new Insets(0, 0, 20, 0));

        academicsHeader.setTranslateX(125 - academicsHeader.getLayoutBounds().getWidth() / 2);

        HBox courseListControls = new HBox(10);
        courseListControls.getChildren().add(refreshButton);
        courseListControls.setAlignment(Pos.CENTER_RIGHT);
        refreshButton.setTranslateX(-220);
        refreshButton.setTranslateY(50);

        VBox selectButtonContainer = new VBox(10);
        selectButtonContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(selectCourseButton, new Insets(0, 0, 0, 0));
        selectButtonContainer.getChildren().add(selectCourseButton);

        HBox selectCourseArea = new HBox(20);
        selectCourseArea.setAlignment(Pos.TOP_CENTER);
        selectCourseArea.getChildren().addAll(courseListView, selectButtonContainer, refreshButton);

        courseListView.setMinWidth(550);
        courseListView.setMinHeight(600);

        selectCourseButton.setMinWidth(120);
        selectCourseButton.setText("Select Course");
        selectCourseButton.setStyle("-fx-font-size: 15px");

        academiaSection.getChildren().addAll(academicsHeader, courseListControls, selectCourseArea);
        academiaSection.setPadding(new Insets(15));
    }

    private void setupEventHandlers() {
        selectCourseButton.setOnAction(StudentDashboardController::handleSelectCourseButton);
        viewStatsButton.setOnAction(StudentDashboardController::handleViewStatsButton);
        accessAssessmentsViewButton.setOnAction(StudentDashboardController::handleViewAssessmentsButton);
        signoutButton.setOnAction(StudentDashboardController::handleSignout);
        refreshButton.setOnAction(StudentDashboardController::handleCourseRefreshButton);
    }

    public StudentDashboardView() {
        LOGGER.debug("Initialising Student Dashboard View");
        getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        setupEventHandlers();
        setupCourseListView();

        LOGGER.debug("Student Dashboard View Initialised");
    }

    private void setComponentIds() {
        studentNameLabel.setId("studentNameLabel");
        studentEmailLabel.setId("studentEmailLabel");
        studentJoinDateLabel.setId("studentJoinDateLabel");

        refreshButton.setId("refreshButton");
        courseListView.setId("courseListView");
        selectCourseButton.setId("selectCourseButton");
        accessAssessmentsViewButton.setId("accessAssessmentsViewButton");
        viewStatsButton.setId("viewStatsButton");
    }

    @Override
    public void initialiseCoreUIComponents() {
        selectCourseButton.setTooltip(new Tooltip("View the Modules for this chosen Course"));
        refreshButton.setStyle("-fx-font-size: 15px");

        signoutButton.setTooltip(new Tooltip("Click to Sign Out"));

        setComponentIds();
    }

    @Override
    public void layoutCoreUIComponents() {
        setupProfileSection();
        setupAcademiaSection();

        VBox leftPanelButtons = new VBox(10);
        leftPanelButtons.getChildren().addAll(viewStatsButton, accessAssessmentsViewButton, signoutButton);
        leftPanelButtons.setAlignment(Pos.BOTTOM_CENTER);
        leftPanelButtons.setPadding(new Insets(20, 0, 20, 0));

        profileSection.getChildren().add(leftPanelButtons);
        setLeft(profileSection);
        setRight(academiaSection);
    }

    public void setupCourseListView() {
        courseListView.setCellFactory(listView -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course course, boolean empty) {

                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(String.format("Course: %s\nID: %d", course.getName(), course.getId()));
                    setWrapText(true);
                }
            }
        });
    }

    @Override
    public void styleCoreUIComponents() {
        getStyleClass().add("student-dashboard-container");
        profileSection.getStyleClass().add("profile-section");
        academiaSection.getStyleClass().add("academia-section");
        signoutButton.getStyleClass().add("menu-button");

        courseListView.getStyleClass().add("list-courses");
        selectCourseButton.getStyleClass().add("action-button");
        accessAssessmentsViewButton.getStyleClass().add("action-button");
        viewStatsButton.getStyleClass().add("action-button");
    }
}
