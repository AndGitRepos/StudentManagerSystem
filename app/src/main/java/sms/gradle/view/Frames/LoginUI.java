package sms.gradle.view.Frames;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import sms.gradle.controller.LoginController;
import sms.gradle.view.CoreViewInterface;

/*
 * Implement Login view via CoreViewInterface's Template pattern Focus on: input verification, error
 * feedback & user authentication
 */

public class LoginUI extends VBox implements CoreViewInterface {

    private Label loginErrorLabel;
    private Button loginButton;
    private TextField emailField;
    private PasswordField passwordField;
    private Label titleLabel;

    public LoginUI() {
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();

        loginButton.setOnAction(LoginController::handleLoginButton);
    }

    @Override
    public void initialiseCoreUIComponents() {

        titleLabel = new Label("Student Management System");

        emailField = new TextField();
        emailField.setPromptText("Example: johnsmith@qmul.ac.uk ");
        emailField.setMaxWidth(250);
        emailField.setTooltip(new Tooltip("Enter your university email address"));
        emailField.setId("email-field");

        passwordField = new PasswordField();
        passwordField.setPromptText("Example: GitHub123 ");
        passwordField.setMaxWidth(250);
        passwordField.setTooltip(new Tooltip("Enter your password"));
        passwordField.setId("password-field");

        loginButton = new Button("Log In");
        loginButton.setMaxWidth(200);
        loginButton.setId("login-button");

        loginErrorLabel = new Label();
        loginErrorLabel.setVisible(false);
        loginErrorLabel.getStyleClass().add("login-error-label");
        loginErrorLabel.setId("login-error-label");
    }

    @Override
    public void layoutCoreUIComponents() {

        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(50));

        titleLabel.getStyleClass().add("title-label");

        VBox usernameSection = new VBox(5);
        Label emailLabel = new Label("Email");
        usernameSection.getChildren().addAll(emailLabel, emailField);

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

        emailField.getStyleClass().add("text-input-field");
        passwordField.getStyleClass().add("text-input-field");
    }

    public void completelyClearFields() {

        emailField.clear();
        passwordField.clear();
        loginErrorLabel.setVisible(false);
    }

    public void displayLoginError(String errorMessage) {
        loginErrorLabel.setText(errorMessage);
        loginErrorLabel.setVisible(true);
    }
}
