package sms.gradle.view.frames.admin;

import static javafx.geometry.Pos.CENTER;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.ManageStudentController;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.Student;
import sms.gradle.view.CoreViewInterface;

public class ManageStudentView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private ListView<Student> studentListView = new ListView<>();
    private Button refreshButton = new Button("Refresh");
    private Button selectButton = new Button("Select");
    private Button createNewButton = new Button("Create New");
    private VBox leftPanel = new VBox(10);

    private GridPane studentDetailsPane = new GridPane();
    private TextField studentIdField = new TextField();
    private TextField firstNameField = new TextField();
    private TextField lastNameField = new TextField();
    private DatePicker dateOfBirthPicker = new DatePicker();
    private DatePicker joinDatePicker = new DatePicker();
    private TextField emailField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Button updateButton = new Button("Update");
    private Button deleteButton = new Button("Delete");
    private VBox centerPanel = new VBox(10);

    private ListView<Course> enrolledCoursesListView = new ListView<>();
    private ListView<Course> availableCoursesListView = new ListView<>();
    private Button enrollButton = new Button("Enroll");
    private Button unenrollButton = new Button("Un-Enroll");
    private Button backButton = new Button("Back");
    private VBox rightPanel = new VBox(10);

    public ManageStudentView() {
        LOGGER.debug("Initialising Manage Student View");
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        assignButtonActions();
        LOGGER.debug("Manage Student View initialised");
    }

    private void assignButtonActions() {
        refreshButton.setOnAction(ManageStudentController::updateListOfStudents);
        selectButton.setOnAction(ManageStudentController::selectStudent);
        deleteButton.setOnAction(ManageStudentController::deleteStudent);
        updateButton.setOnAction(ManageStudentController::updateStudent);
        createNewButton.setOnAction(ManageStudentController::createNewStudent);
        enrollButton.setOnAction(ManageStudentController::enrollCourse);
        unenrollButton.setOnAction(ManageStudentController::unenrollCourse);
        backButton.setOnAction(ManageStudentController::handleBackButton);
    }

    private void setCoreUiComponentIds() {
        studentListView.setId("studentListView");
        refreshButton.setId("refreshButton");
        selectButton.setId("selectButton");
        deleteButton.setId("deleteButton");

        studentIdField.setId("studentIdField");
        firstNameField.setId("firstNameField");
        lastNameField.setId("lastNameField");
        dateOfBirthPicker.setId("dateOfBirthPicker");
        joinDatePicker.setId("joinDatePicker");
        emailField.setId("emailField");
        passwordField.setId("passwordField");
        createNewButton.setId("createNewButton");
        updateButton.setId("updateButton");

        enrolledCoursesListView.setId("enrolledCoursesListView");
        availableCoursesListView.setId("availableCoursesListView");
        enrollButton.setId("enrollButton");
        unenrollButton.setId("unenrollButton");
        backButton.setId("backButton");
    }

    @Override
    public void initialiseCoreUIComponents() {
        studentIdField.setEditable(false);

        studentListView.setCellFactory(listView -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);

                if (empty || student == null) {
                    setText(null);
                    return;
                }
                String displayText = String.format(
                        "%s %s\nID: %d\nEmail: %s\nJoined: %s",
                        student.getFirstName(),
                        student.getLastName(),
                        student.getId(),
                        student.getEmail(),
                        student.getJoinDate());
                setText(displayText);
                setWrapText(true);
            }
        });

        enrolledCoursesListView.setCellFactory(listView -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);

                if (empty || course == null) {
                    setText(null);
                    return;
                }
                String displayText = String.format(
                        "%s\nID: %d\nDescription: %s", course.getName(), course.getId(), course.getDescription());
                setText(displayText);
                setWrapText(true);
            }
        });

        availableCoursesListView.setCellFactory(listView -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);

                if (empty || course == null) {
                    setText(null);
                    return;
                }
                String displayText = String.format(
                        "%s\nID: %d\nDescription: %s", course.getName(), course.getId(), course.getDescription());
                setText(displayText);
                setWrapText(true);
            }
        });

        setCoreUiComponentIds();
    }

    @Override
    public void layoutCoreUIComponents() {
        leftPanel
                .getChildren()
                .addAll(
                        new HBox(10, new Label("Students"), refreshButton),
                        studentListView,
                        new HBox(10, selectButton, deleteButton));
        leftPanel.setPadding(new Insets(10));
        setLeft(leftPanel);

        studentDetailsPane.setHgap(10);
        studentDetailsPane.setVgap(10);
        studentDetailsPane.addRow(0, new Label("Student ID:"), studentIdField);
        studentDetailsPane.addRow(1, new Label("First Name:"), firstNameField);
        studentDetailsPane.addRow(2, new Label("Last Name:"), lastNameField);
        studentDetailsPane.addRow(3, new Label("Date of Birth:"), dateOfBirthPicker);
        studentDetailsPane.addRow(4, new Label("Join Date:"), joinDatePicker);
        studentDetailsPane.addRow(5, new Label("Email:"), emailField);
        studentDetailsPane.addRow(6, new Label("Password:"), passwordField);

        HBox buttonBox = new HBox(10, createNewButton, updateButton);
        buttonBox.setAlignment(CENTER);

        centerPanel.getChildren().addAll(new Label("Student Details"), studentDetailsPane, buttonBox);
        centerPanel.setPadding(new Insets(10));
        setCenter(centerPanel);

        VBox coursesBox = new VBox(
                10,
                new Label("Enrolled Courses"),
                enrolledCoursesListView,
                new HBox(10, unenrollButton),
                new Label("Available Courses"),
                availableCoursesListView);

        HBox controlButtons = new HBox(10, enrollButton, backButton);
        controlButtons.setAlignment(CENTER);

        rightPanel.getChildren().addAll(coursesBox, controlButtons);
        rightPanel.setPadding(new Insets(10));
        setRight(rightPanel);
    }

    @Override
    public void styleCoreUIComponents() {
        applyBasicStyles();
    }

    private void applyBasicStyles() {
        setMinWidth(850);
        setPrefSize(900, 550);
        getStyleClass().add("manage-view-container");

        // Style containers
        getStyleClass().add("manage-student-view");
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

        studentListView.getStyleClass().add("student-list");
        enrolledCoursesListView.getStyleClass().add("courses-list");
        availableCoursesListView.getStyleClass().add("courses-list");

        studentDetailsPane.getStyleClass().add("details-pane");

        refreshButton.getStyleClass().add("action-button");
        selectButton.getStyleClass().add("action-button");
        deleteButton.getStyleClass().add("action-button");
        createNewButton.getStyleClass().add("action-button");
        updateButton.getStyleClass().add("action-button");
        enrollButton.getStyleClass().add("action-button");
        unenrollButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigate-button");
    }

    public void addStudentToList(Student student) {
        studentListView.getItems().add(student);
    }
}
