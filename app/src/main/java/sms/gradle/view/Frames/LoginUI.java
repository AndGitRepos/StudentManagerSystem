package sms.gradle.view.Frames;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import sms.gradle.view.CoreViewInterface;

/*
 * Implement Login view via CoreViewInterface's Template pattern Focus on: input verification, error
 * feedback & user authentication
 */

public class LoginUI extends VBox implements CoreViewInterface {

    private Label loginErrorLabel;
    private Button loginButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label titleLabel;
    private ComboBox<String> roleComboBox;

    public LoginUI() {
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
    }

    @Override
    public void initialiseCoreUIComponents() {

        titleLabel = new Label("Student Management System");

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your email address");
        usernameField.setMaxWidth(250);
        usernameField.setTooltip(new Tooltip("Enter your university email address"));

        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password");
        passwordField.setMaxWidth(250);
        passwordField.setTooltip(new Tooltip("Enter your password"));

        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Student", "Staff");
        roleComboBox.setValue("Student");
        roleComboBox.setMaxWidth(250);

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
        Label usernamLabel = new Label("Username");
        usernameSection.getChildren().addAll(usernamLabel, usernameField);

        VBox passwordSection = new VBox(5);
        Label passwordLabel = new Label("Password");
        passwordSection.getChildren().addAll(passwordLabel, passwordField);

        VBox roleSection = new VBox(5);
        Label roleLabel = new Label("Role");
        roleSection.getChildren().addAll(roleLabel, roleComboBox);

        getChildren().addAll(titleLabel, roleSection, usernameSection, passwordSection, loginErrorLabel, loginButton);
    }

    @Override
    public void styleCoreUIComponents() {
        getStyleClass().add("login-container");
        titleLabel.getStyleClass().add("title-label");
        loginButton.getStyleClass().add("login-button");
        loginErrorLabel.getStyleClass().add("login-error-label");

        usernameField.getStyleClass().add("text-input-field");
        passwordField.getStyleClass().add("text-input-field");
        roleComboBox.getStyleClass().add("role-combo-box");
    }

    public void completelyClearFields() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.setValue("Student");
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

    public ComboBox<String> getRoleComboBox() {
        return roleComboBox;
    }

    public void displayLoginError(String errorMessage) {
        loginErrorLabel.setText(errorMessage);
        loginErrorLabel.setVisible(true);
    }
}
