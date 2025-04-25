package sms.gradle.view.frames.admin;

import static javafx.geometry.Pos.CENTER;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.ManageStudentController;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.Student;
import sms.gradle.view.CoreViewInterface;
import sms.gradle.view.ViewFactory;

@Getter
public class ManageStudentView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private ListView<Student> studentListView;
    private Button refreshButton;
    private Button selectButton;
    private Button createNewButton;
    private VBox leftPanel;

    private GridPane studentDetailsPane;
    private TextField studentIdField;
    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker dateOfBirthPicker;
    private DatePicker joinDatePicker;
    private TextField emailField;
    private PasswordField passwordField;
    private Button updateButton;
    private Button deleteButton;
    private VBox centerPanel;

    private ListView<Course> enrolledCoursesListView;
    private ListView<Course> availableCoursesListView;
    private Button moveUpButton;
    private Button moveDownButton;
    private Button backButton;
    private VBox rightPanel;

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
        createNewButton.setOnAction(ManageStudentController::createNewStudent);
        backButton.setOnAction(event -> ViewFactory.getInstance().changeToAdminDashboardStage());
    }

    @Override
    public void initialiseCoreUIComponents() {
        studentListView = new ListView<>();
        studentListView.setId("studentListView");
        refreshButton = new Button("Refresh");
        refreshButton.setId("refreshButton");
        selectButton = new Button("Select");
        selectButton.setId("selectButton");
        deleteButton = new Button("Delete");
        deleteButton.setId("deleteButton");
        leftPanel = new VBox(10);

        studentDetailsPane = new GridPane();
        studentIdField = new TextField();
        studentIdField.setId("studentIdField");
        firstNameField = new TextField();
        firstNameField.setId("firstNameField");
        lastNameField = new TextField();
        lastNameField.setId("lastNameField");
        dateOfBirthPicker = new DatePicker();
        dateOfBirthPicker.setId("dateOfBirthPicker");
        joinDatePicker = new DatePicker();
        joinDatePicker.setId("joinDatePicker");
        emailField = new TextField();
        emailField.setId("emailField");
        passwordField = new PasswordField();
        passwordField.setId("passwordField");
        createNewButton = new Button("Create New");
        createNewButton.setId("createNewButton");
        updateButton = new Button("Update");
        updateButton.setId("updateButton");
        centerPanel = new VBox(10);

        enrolledCoursesListView = new ListView<>();
        enrolledCoursesListView.setId("enrolledCoursesListView");
        availableCoursesListView = new ListView<>();
        availableCoursesListView.setId("availableCoursesListView");
        moveUpButton = new Button("Move Up");
        moveUpButton.setId("moveUpButton");
        moveDownButton = new Button("Move Down");
        moveDownButton.setId("moveDownButton");
        backButton = new Button("Back");
        backButton.setId("backButton");
        rightPanel = new VBox(10);
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
                new HBox(10, moveUpButton, moveDownButton),
                new Label("Available Courses"),
                availableCoursesListView);

        HBox controlButtons = new HBox(10, backButton);
        controlButtons.setAlignment(CENTER);

        rightPanel.getChildren().addAll(coursesBox, controlButtons);
        rightPanel.setPadding(new Insets(10));
        setRight(rightPanel);
    }

    @Override
    public void styleCoreUIComponents() {

        setMinWidth(850);
        setPrefSize(900, 550);
        getStyleClass().add("manage-view-container");

        leftPanel.getStyleClass().add("left-panel");
        studentListView.setPrefWidth(150);

        enrolledCoursesListView.setPrefWidth(150);
        availableCoursesListView.setPrefWidth(200);

        // Style containers
        getStyleClass().add("manage-student-view");

        centerPanel.getStyleClass().add("center-panel");
        rightPanel.getStyleClass().add("right-panel");

        studentListView.getStyleClass().add("student-list");
        enrolledCoursesListView.getStyleClass().add("courses-list");
        availableCoursesListView.getStyleClass().add("courses-list");

        studentDetailsPane.getStyleClass().add("details-pane");

        refreshButton.getStyleClass().add("action-button");
        selectButton.getStyleClass().add("action-button");
        deleteButton.getStyleClass().add("action-button");
        createNewButton.getStyleClass().add("action-button");
        updateButton.getStyleClass().add("action-button");
        moveUpButton.getStyleClass().add("action-button");
        moveDownButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigate-button");
    }

    public void addStudentToList(Student student) {
        studentListView.getItems().add(student);
    }
}
