package sms.gradle.view.Frames;

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
import sms.gradle.view.CoreViewInterface;

/*
 * Implement Login view via CoreViewInterface's Template pattern Focus on: input verification, error
 * feedback & user authentication
 */

public class LoginUI extends VBox implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private Label loginErrorLabel;
    private Button loginButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label titleLabel;

    public LoginUI() {
        LOGGER.debug("Initialising Login View");
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        LOGGER.debug("Login View initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {

        titleLabel = new Label("Student Management System");

        usernameField = new TextField();
        usernameField.setPromptText("Example: johnsmith@qmul.ac.uk ");
        usernameField.setMaxWidth(250);
        usernameField.setTooltip(new Tooltip("Enter your university email address"));

        passwordField = new PasswordField();
        passwordField.setPromptText("Example: GitHub123 ");
        passwordField.setMaxWidth(250);
        passwordField.setTooltip(new Tooltip("Enter your password"));

        loginButton = new Button("Log In");
        loginButton.setMaxWidth(200);

        loginErrorLabel = new Label();
        loginErrorLabel.setVisible(false);
        loginErrorLabel.getStyleClass().add("login-error-label");
    }

    @Override
    public void layoutCoreUIComponents() {

        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(50));

        titleLabel.getStyleClass().add("title-label");

        VBox usernameSection = new VBox(5);
        Label usernameLabel = new Label("Username");
        usernameSection.getChildren().addAll(usernameLabel, usernameField);

        VBox passwordSection = new VBox(5);
        Label passwordLabel = new Label("Password");
        passwordSection.getChildren().addAll(passwordLabel, passwordField);

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

    public void completelyClearFields() {

        usernameField.clear();
        passwordField.clear();
        loginErrorLabel.setVisible(false);
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void displayLoginError(String errorMessage) {
        loginErrorLabel.setText(errorMessage);
        loginErrorLabel.setVisible(true);
    }
}
