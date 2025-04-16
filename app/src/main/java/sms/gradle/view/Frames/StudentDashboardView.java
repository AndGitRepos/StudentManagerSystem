package sms.gradle.view.Frames;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.view.CoreViewInterface;

/*
 * Manages StudentDashboard view via CoreViewInterface's Template pattern
 */

public class StudentDashboardView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    // Containers for Layout
    private VBox profileSection;
    private VBox academiaSection;
    private VBox moduleBase;

    // Student Details components
    private Label studentNameLabel;
    private Label studentEmailLabel;
    private Label studentEntryYearLabel;

    private Button signoutButton;

    public StudentDashboardView() {
        LOGGER.debug("Initialising Student Dashboard View");
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        LOGGER.debug("Student Dashboard View Initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        initialiseKeyContainers();
        initialiseStudentDetails();
        initialiseSignoutControl();
    }

    private void initialiseKeyContainers() {
        profileSection = new VBox(20);
        academiaSection = new VBox(20);
        moduleBase = new VBox(20);
    }

    private void initialiseStudentDetails() {
        studentNameLabel = new Label("Student Name");
        studentEmailLabel = new Label("student@email.com");
        studentEntryYearLabel = new Label("2025");
    }

    private void initialiseSignoutControl() {
        signoutButton = new Button("Sign Out");
        signoutButton.setTooltip(new Tooltip("Please Click to Sign Out"));
    }

    @Override
    public void layoutCoreUIComponents() {
        setupAcademiaSection();
        setupProfileSection();
        setLeft(profileSection);
        setRight(academiaSection);
    }

    private void setupProfileSection() {
        Label profileHeader = new Label("PROFILE");
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
        VBox facultyInformation = createFacultyInformationSection();
        Label modulesHeader = new Label("MODULES");

        // Input data for modules
        academiaSection
                .getChildren()
                .addAll(academiaHeader, facultyInformation, modulesHeader, moduleBase, signoutButton);
    }

    private VBox createFacultyInformationSection() {
        VBox facultyInformation = new VBox(5);

        Label facultyHeader = new Label("Faculty Details");

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
    public void styleCoreUIComponents() {}

    public void updateStudentDetails(String name, String emailAddress, String entryYear) {
        studentNameLabel.setText(name);
        studentEmailLabel.setText(emailAddress);
        studentEntryYearLabel.setText("Year of Entry: " + entryYear);
    }

    public Button getSignOutButton() {
        return signoutButton;
    }
}
