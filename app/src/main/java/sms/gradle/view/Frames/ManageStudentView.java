package sms.gradle.view.Frames;

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
import sms.gradle.model.entities.Student;
import sms.gradle.view.CoreViewInterface;

@Getter
public class ManageStudentView extends BorderPane implements CoreViewInterface {
    private ListView<Student> studentListView;
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

    private ListView<String> enrolledCoursesListView;
    private ListView<String> availableCoursesListView;
    private Button moveUpButton;
    private Button moveDownButton;
    private Button backButton;
    private Button logoutButton;
    private VBox rightPanel;

    public ManageStudentView() {
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
    }

    @Override
    public void initialiseCoreUIComponents() {
        studentListView = new ListView<>();
        selectButton = new Button("Select");
        createNewButton = new Button("Create New");
        leftPanel = new VBox(10);

        studentDetailsPane = new GridPane();
        studentIdField = new TextField();
        firstNameField = new TextField();
        lastNameField = new TextField();
        dateOfBirthPicker = new DatePicker();
        joinDatePicker = new DatePicker();
        emailField = new TextField();
        passwordField = new PasswordField();
        updateButton = new Button("Update");
        deleteButton = new Button("Delete");
        centerPanel = new VBox(10);

        enrolledCoursesListView = new ListView<>();
        availableCoursesListView = new ListView<>();
        moveUpButton = new Button("Move Up");
        moveDownButton = new Button("Move Down");
        backButton = new Button("Back");
        logoutButton = new Button("Logout");
        rightPanel = new VBox(10);
    }

    @Override
    public void layoutCoreUIComponents() {
        leftPanel
                .getChildren()
                .addAll(new Label("Students"), studentListView, new HBox(10, selectButton, createNewButton));
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

        HBox buttonBox = new HBox(10, updateButton, deleteButton);
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

        HBox controlButtons = new HBox(10, backButton, logoutButton);
        controlButtons.setAlignment(CENTER);

        rightPanel.getChildren().addAll(coursesBox, controlButtons);
        rightPanel.setPadding(new Insets(10));
        setRight(rightPanel);
    }

    @Override
    public void styleCoreUIComponents() {
        setPrefSize(750, 550);
        studentListView.setPrefWidth(150);
        enrolledCoursesListView.setPrefWidth(150);
        availableCoursesListView.setPrefWidth(200);

        getStyleClass().add("manage-student-view");
        studentListView.getStyleClass().add("student-list");
        studentDetailsPane.getStyleClass().add("details-pane");
        enrolledCoursesListView.getStyleClass().add("courses-list");
        availableCoursesListView.getStyleClass().add("courses-list");
    }
}
