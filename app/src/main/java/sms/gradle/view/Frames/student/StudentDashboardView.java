package sms.gradle.view.frames.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.view.CoreViewInterface;

/*
 * Manages StudentDashboard view via CoreViewInterface's Template pattern
 */

@Getter
public class StudentDashboardView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private VBox academicsContainer;
    private VBox facultyInformationBox;
    private HBox accessModulesBox;
    private VBox studentDetailsBox;

    private Label studentNameLabel;
    private Label studentEmailLabel;
    private Label studentEntryYearLabel;

    private Label departmentLabel;
    private Label courseNameLabel;
    private Label courseIdLabel;

    private Button logoutButton;
    private Button accessModulesButton;
    private Button accessAssessmentsButton;

    public StudentDashboardView() {
        LOGGER.debug("Initialising Student Dashboard View");
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        LOGGER.debug("Student Dashboard View Initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        initialiseContainers();
        initialiseLabels();
        initialiseButtons();
    }

    private void initialiseContainers() {
        academicsContainer = new VBox(20);
        academicsContainer.setMaxWidth(Double.MAX_VALUE);

        facultyInformationBox = new VBox(5);
        facultyInformationBox.setMaxWidth(Double.MAX_VALUE);

        accessModulesBox = new HBox(15);
        accessModulesBox.setMaxWidth(Double.MAX_VALUE);

        studentDetailsBox = new VBox(15);
        studentDetailsBox.setMaxWidth(Double.MAX_VALUE);

        academicsContainer.prefWidthProperty().bind(this.widthProperty().multiply(0.65));
        studentDetailsBox.prefWidthProperty().bind(this.widthProperty().multiply(0.35));
    }

    private void initialiseLabels() {
        studentNameLabel = new Label("Student Name: ");
        studentEmailLabel = new Label("Student Email: ");
        studentEntryYearLabel = new Label("Student Academic Entry Year: ");

        departmentLabel = new Label("Department: ");
        courseNameLabel = new Label("Course: ");
        courseIdLabel = new Label("Course ID: ");
    }

    private void initialiseButtons() {
        logoutButton = new Button("Logout");
        logoutButton.setPrefWidth(200);
        logoutButton.setTooltip(new Tooltip("Please Click to Sign Out"));

        accessModulesButton = new Button("View Modules");
        accessModulesButton.setPrefWidth(200);
        accessModulesButton.setTooltip(new Tooltip("Click to view all current course modules"));

        accessAssessmentsButton = new Button("View Assessments");
        accessAssessmentsButton.setPrefWidth(200);
        accessAssessmentsButton.setTooltip(new Tooltip("Click to view all current course assessments"));
    }

    @Override
    public void layoutCoreUIComponents() {
        setupAcademiaSection();
        setupStudentSection();

        setLeft(studentDetailsBox);
        this.setMinWidth(850);
        this.setMinHeight(650);
        setCenter(academicsContainer);
    }

    private void setupStudentSection() {
        Label studentHeader = new Label("Student Details");
        studentHeader.getStyleClass().add("section-header");

        Circle profilePhoto = new Circle(60, Color.GRAY); // TODO: Proposed future implementation...
        profilePhoto.setStroke(Color.WHITE);
        profilePhoto.setStrokeWidth(2);
        profilePhoto.radiusProperty().bind(studentDetailsBox.prefWidthProperty().multiply(0.20));
        Tooltip.install(profilePhoto, new Tooltip("Temporary holder for profile photo"));

        VBox studentInfo = new VBox(10);

        studentInfo
                .getChildren()
                .addAll(
                        createInformationField(studentNameLabel),
                        createInformationField(studentEmailLabel),
                        createInformationField(studentEntryYearLabel));
        studentInfo.prefWidthProperty().bind(studentDetailsBox.widthProperty().multiply(1.05));

        studentDetailsBox.getChildren().addAll(studentHeader, profilePhoto, studentInfo);
        studentDetailsBox.setPadding(new Insets(20));
    }

    private HBox createInformationField(Label standardLabel) {
        HBox field = new HBox(10);
        field.setAlignment(Pos.CENTER_LEFT);
        field.setMaxWidth(Double.MAX_VALUE);
        standardLabel.setMaxWidth(Double.MAX_VALUE);
        field.getChildren().add(standardLabel);
        return field;
    }

    private void setupAcademiaSection() {
        Label academiaHeader = new Label("Academic Information");
        academiaHeader.getStyleClass().add("section-header");

        setUpFacultyDetails();

        VBox buttonContainer = new VBox(10);
        buttonContainer.setMaxWidth(Double.MAX_VALUE);
        buttonContainer
                .prefWidthProperty()
                .bind(academicsContainer.widthProperty().multiply(1.25));
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(accessModulesBox, createNavigationBar());

        accessModulesBox.getChildren().addAll(accessModulesButton, accessAssessmentsButton);
        accessModulesBox.setAlignment(Pos.CENTER);

        academicsContainer
                .getChildren()
                .addAll(academiaHeader, createSpacer(20.0), facultyInformationBox, createSpacer(20.0), buttonContainer);

        academicsContainer.setPadding(new Insets(20));
    }

    private void setUpFacultyDetails() {

        Label facultyHeader = new Label("Faculty Details");
        facultyHeader.getStyleClass().add("sub-header");

        facultyInformationBox.setSpacing(15);
        facultyInformationBox.setPadding(new Insets(10));

        facultyInformationBox
                .getChildren()
                .addAll(facultyHeader, createSpacer(10.0), departmentLabel, courseNameLabel, courseIdLabel);
    }

    @Override
    public void styleCoreUIComponents() {}

    public void updateStudentDetails(String name, String emailAddress, String entryYear) {
        studentNameLabel.setText(name);
        studentEmailLabel.setText(emailAddress);
        studentEntryYearLabel.setText("Year of Entry: " + entryYear);
    }

    private HBox createNavigationBar() {

        HBox navigationBar = new HBox(logoutButton);
        navigationBar.setPadding(new Insets(20, 0, 0, 0));
        navigationBar.setAlignment(Pos.CENTER);
        return navigationBar;
    }

    private Region createSpacer(Double height) {
        Region makeSpace = new Region();
        makeSpace.setPrefHeight(height);
        return makeSpace;
    }
}
