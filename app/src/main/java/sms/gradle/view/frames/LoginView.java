package sms.gradle.view.frames;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.login.LoginController;
import sms.gradle.utils.checks.textfield.MinLengthCheck;
import sms.gradle.utils.checks.textfield.TextFieldValidator;
import sms.gradle.view.CoreViewInterface;

/*
 * Implement Login view via CoreViewInterface's Template pattern Focus on: input verification, error
 * feedback & user authentication
 */
public class LoginView extends VBox implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private Label loginErrorLabel = new Label();
    private Button loginButton = new Button("Log In");
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Label titleLabel = new Label("Student Management System");
    private Button fillAdminDetailsButton = new Button("Fill Admin Details");
    private Button fillStudentDetailsButton = new Button("Fill Student Details");

    public LoginView() {
        LOGGER.debug("Initialising Login View");
        getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        setupHandlersForEvent();
        LOGGER.debug("Login View initialised");
    }

    private void setupHandlersForEvent() {
        loginButton.setOnAction(LoginController::handleLoginAttempt);
        fillAdminDetailsButton.setOnAction(LoginController::handleFillAdminDetails);
        fillStudentDetailsButton.setOnAction(LoginController::handleFillStudentDetails);
    }

    private void configureValidationChecks() {

        TextFieldValidator usernameValidator = new TextFieldValidator(List.of(new MinLengthCheck(1)));
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            new TextFieldValidator(List.of(new MinLengthCheck(1))).validate(usernameField);
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            new TextFieldValidator(List.of(new MinLengthCheck(1))).validate(passwordField);
        });
    }

    @Override
    public void initialiseCoreUIComponents() {
        usernameField.setId("username_field");
        usernameField.setPromptText("Example: johnsmith@qmul.ac.uk ");
        usernameField.setMaxWidth(250);
        usernameField.setTooltip(new Tooltip("Enter your email address"));

        passwordField.setId("password_field");
        passwordField.setPromptText("Example: GitHub123 ");
        passwordField.setMaxWidth(250);
        passwordField.setTooltip(new Tooltip("Enter your password"));

        loginButton.setMaxWidth(200);
        loginButton.setId("login_button");

        loginErrorLabel.setId("error_label");
        loginErrorLabel.setVisible(false);
        loginErrorLabel.getStyleClass().add("login-error-label");

        configureValidationChecks();
    }

    @Override
    public void layoutCoreUIComponents() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(50));

        titleLabel.getStyleClass().add("title-label");

        VBox usernameSection = new VBox(5);
        Label usernameLabel = new Label("Username");
        usernameSection.getChildren().addAll(usernameLabel, usernameField, fillAdminDetailsButton);

        VBox passwordSection = new VBox(5);
        Label passwordLabel = new Label("Password");
        passwordSection.getChildren().addAll(passwordLabel, passwordField, fillStudentDetailsButton);

        getChildren().addAll(titleLabel, usernameSection, passwordSection, loginErrorLabel, loginButton);
    }

    @Override
    public void styleCoreUIComponents() {
        getStyleClass().add("login-container");
        titleLabel.getStyleClass().add("title-label");
        loginButton.getStyleClass().add("login-button");
        loginErrorLabel.getStyleClass().add("login-error-label");

        usernameField.getStyleClass().add("text-input-field");
        passwordField.getStyleClass().add("text-input-field");
    }
}
