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
import sms.gradle.model.entities.Course;
import sms.gradle.view.CoreViewInterface;

/*
 * Manages StudentDashboard view via CoreViewInterface's Template pattern
 */

public class StudentDashboardView extends BorderPane implements CoreViewInterface {

    private static final Logger LOGGER = LogManager.getLogger();

    private VBox profileSection;
    private VBox academiaSection;
    private ListView<Course> courseListView;

    private Button selectCourseButton;
    private Button accessAssessmentsViewButton;
    private Button viewStatsButton; // TODO: All stats in `Access Assessments View`
    private Button signoutButton;

    private Label studentNameLabel;
    private Label studentEmailLabel;
    private Label studentEntryYearLabel;

    private void initialiseKeyContainers() {
        profileSection = new VBox(20);
        academiaSection = new VBox(20);
    }

    private void initialiseStudentDetails() {
        studentNameLabel = new Label("Student: ");
        studentEmailLabel = new Label("Email: ");
        studentEntryYearLabel = new Label("Entry Year: ");
    }

    private void initialiseSignoutControl() {
        signoutButton = new Button("Sign Out");
        signoutButton.setTooltip(new Tooltip("Click to Sign Out"));
    }

    private void setupProfileSection() {
        Label profileHeader = new Label("PROFILE");
        profileHeader.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        profileHeader.setPadding(new Insets(0, 0, 20, 0));

        Circle profilePhoto = new Circle(50, Color.GRAY);

        Tooltip.install(profilePhoto, new Tooltip("Temporary holder for profile photo"));

        profileSection
                .getChildren()
                .addAll(profileHeader, profilePhoto, studentEmailLabel, studentEntryYearLabel, studentNameLabel);
        profileSection.setAlignment(Pos.TOP_CENTER);
        profileSection.setPrefWidth(350);
        profileSection.setPadding(new Insets(20));
    }

    private void setupAcademiaSection() {
        Label academicsHeader = new Label("ACADEMICS");
        academicsHeader.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        academicsHeader.setPadding(new Insets(0, 0, 20, 0));

        academicsHeader.setTranslateX(125 - academicsHeader.getLayoutBounds().getWidth() / 2);

        HBox selectCourseArea = new HBox(20);
        selectCourseArea.setAlignment(Pos.TOP_CENTER);

        VBox selectButtonContainer = new VBox(10);
        selectButtonContainer.setAlignment(Pos.CENTER);
        selectButtonContainer.getChildren().add(selectCourseButton);
        VBox.setMargin(selectCourseButton, new Insets(0, 0, 0, 0));

        selectCourseArea.getChildren().addAll(courseListView, selectButtonContainer);

        courseListView.setMinWidth(400);
        courseListView.setMinHeight(600);

        selectCourseButton.setMinWidth(120);
        selectCourseButton.setText("Select Course");
        selectCourseButton.setStyle("-fx-font-size: 15px");

        academiaSection.getChildren().addAll(academicsHeader, selectCourseArea);
        academiaSection.setPadding(new Insets(15));
    }

    public StudentDashboardView() {
        LOGGER.debug("Initialising Student Dashboard View");
        getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        LOGGER.debug("Student Dashboard View Initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        initialiseKeyContainers();
        initialiseStudentDetails();

        courseListView = new ListView<>();

        selectCourseButton = new Button("Select Course");
        selectCourseButton.setTooltip(new Tooltip("View the Modules for this chosen Course"));

        accessAssessmentsViewButton = new Button("View All Assessments");
        viewStatsButton = new Button("View Overall Course Stats");

        signoutButton = new Button("Sign Out");

        courseListView.setId("courseListView");
        selectCourseButton.setId("selectCourseButton");
        accessAssessmentsViewButton.setId("accessAssessmentsViewButton");
        viewStatsButton.setId("viewStatsButton");

        initialiseSignoutControl();
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

    public void updateStudentDetails(String name, String emailAddress, String entryYear) {
        studentNameLabel.setText("Student: " + name);
        studentEmailLabel.setText("Email Address: " + emailAddress);
        studentEntryYearLabel.setText("Year of Entry: " + entryYear);
        LOGGER.debug("Student details updated: Name: {}, Email: {}, Entry Year: {}", name, emailAddress, entryYear);
    }

    public Button getSignOutButton() {
        return signoutButton;
    }
}
