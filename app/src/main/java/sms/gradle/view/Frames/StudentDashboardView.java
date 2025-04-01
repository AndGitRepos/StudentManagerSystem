package sms.gradle.view.Frames;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sms.gradle.view.CoreViewInterface;
import sms.gradle.view.components.ModuleRow;

public class StudentDashboardView extends BorderPane implements CoreViewInterface {

    private VBox profileSection;
    private VBox academiaSection;
    private VBox moduleBase;
    private Label studentNameLabel;
    private Label studentEmailLabel;
    private Label studentEntryYearLabel;
    private Button signoutButton;

    public StudentDashboardView() {

        componentsInitialised();
        componentsLayout();
        componentsStyling();
    }

    @Override
    public void componentsInitialised() {
        profileSection = new VBox(20);
        academiaSection = new VBox(20);
        moduleBase = new VBox(20);

        studentNameLabel = new Label("Student Name");
        studentEmailLabel = new Label("student@email.com");
        studentEntryYearLabel = new Label("2025");

        signoutButton = new Button("Sign Out");

        signoutButton.setTooltip(new Tooltip("Please Click to Sign Out"));
    }

    @Override
    public void componentsLayout() {

        setupAcademiaSection();
        setupProfileSection();
        setLeft(profileSection);
        setRight(academiaSection);
    }

    private void setupProfileSection() {

        Label profileHeader = new Label("PROFILE");
        profileHeader.getStyleClass().add("section-header");

        Circle profilePhoto = new Circle(50, Color.GRAY);

        Tooltip.install(profilePhoto, new Tooltip("Temporary holder for profile photo"));

        profileSection
                .getChildren()
                .addAll(profileHeader, profilePhoto, studentEmailLabel, studentEntryYearLabel, studentNameLabel);
        profileSection.setAlignment(Pos.TOP_CENTER);
        profileSection.setPrefWidth(350);
    }

    private void setupAcademiaSection() {

        Label academiaHeader = new Label("ACADEMIA");
        academiaHeader.getStyleClass().add("section-header");

        VBox facultyInformation = createFacultyInformationSection();

        Label modulesHeader = new Label("MODULES");
        modulesHeader.getStyleClass().add("sub-header");

        moduleBase
                .getChildren()
                .addAll(
                        // Sample data for modules
                        new ModuleRow("Quantum Engineering", "Dr Allen", "Room 100", 80),
                        new ModuleRow("Data Structures", "Dr Gray", "Room 204", 90),
                        new ModuleRow("Web Development", "Prof Smith", "Room 375", 82));

        academiaSection
                .getChildren()
                .addAll(academiaHeader, facultyInformation, modulesHeader, moduleBase, signoutButton);
    }

    private VBox createFacultyInformationSection() {
        VBox facultyInformation = new VBox(5);
        facultyInformation.getStyleClass().add("faculty-details");

        Label facultyHeader = new Label("Faculty Details");

        facultyHeader.getStyleClass().add("sub-header");

        facultyInformation
                .getChildren()
                .addAll(
                        facultyHeader,
                        new Label("Department: School of Electronic Engineering and Computer Science"),
                        new Label("Course: Bsc Software Engineering"),
                        new Label("Course ID: EECS248"));

        return facultyInformation;
    }

    @Override
    public void componentsStyling() {
        getStylesheets().add(getClass().getResource("UIStyles/dashboard.css").toExternalForm());

        getStyleClass().add("dashboard-container");

        profileSection.getStyleClass().add("profile-section");
        studentNameLabel.getStyleClass().add("student-name");
        studentEmailLabel.getStyleClass().add("student-detail");
        studentEntryYearLabel.getStyleClass().add("student-detail");

        academiaSection.getStyleClass().add("academia-section");
        moduleBase.getStyleClass().add("module-container");

        signoutButton.getStyleClass().add("sign-out-button");
    }

    public void displayWelcomeMessage(String username) {
        studentNameLabel.setText("Welcome " + username);
    }

    public void updateStudentDetails(String name, String emailAddress, String entryYear) {
        studentNameLabel.setText(name);
        studentEmailLabel.setText(emailAddress);
        studentEntryYearLabel.setText("Year of Entry: " + entryYear);
    }

    public Button getSignOutbutton() {
        return signoutButton;
    }
}
