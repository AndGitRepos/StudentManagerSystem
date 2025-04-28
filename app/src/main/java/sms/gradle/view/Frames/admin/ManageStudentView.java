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
    private Button enrollButton;
    private Button unenrollButton;
    private Button backButton;
    private VBox rightPanel;

    public ManageStudentView() {
        LOGGER.debug("Initialising Manage Student View");
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        setCoreUiComponentIds();
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
        enrollButton.setOnAction(ManageStudentController::enrollCourse);
        unenrollButton.setOnAction(ManageStudentController::unenrollCourse);
        backButton.setOnAction(event -> ViewFactory.getInstance().changeToAdminDashboardStage());
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
        studentListView = new ListView<>();
        refreshButton = new Button("Refresh");
        selectButton = new Button("Select");
        deleteButton = new Button("Delete");
        leftPanel = new VBox(10);

        studentDetailsPane = new GridPane();
        studentIdField = new TextField();
        studentIdField.setEditable(false);
        firstNameField = new TextField();
        lastNameField = new TextField();
        dateOfBirthPicker = new DatePicker();
        joinDatePicker = new DatePicker();
        emailField = new TextField();
        passwordField = new PasswordField();
        createNewButton = new Button("Create New");
        updateButton = new Button("Update");
        centerPanel = new VBox(10);

        enrolledCoursesListView = new ListView<>();
        availableCoursesListView = new ListView<>();
        enrollButton = new Button("Enroll");
        unenrollButton = new Button("Un-Enroll");
        backButton = new Button("Back");
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
        enrollButton.getStyleClass().add("action-button");
        unenrollButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigate-button");
    }

    public void addStudentToList(Student student) {
        studentListView.getItems().add(student);
    }
}
