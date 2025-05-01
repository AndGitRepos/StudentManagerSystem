package sms.gradle.view.frames.admin;

import static javafx.geometry.Pos.CENTER;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.ManageAdminsController;
import sms.gradle.model.entities.Admin;
import sms.gradle.view.CoreViewInterface;

public class ManageAdminView extends BorderPane implements CoreViewInterface {

    private static final Logger LOGGER = LogManager.getLogger();

    private ListView<Admin> adminListView = new ListView<>();
    private Button refreshAdminListButton = new Button("Refresh");
    private Button selectAdminButton = new Button("Select");
    private Button deleteAdminButton = new Button("Delete");

    VBox leftPanel = new VBox(10);
    private GridPane adminDetailsPane = new GridPane();

    private TextField firstNameField = new TextField();
    private TextField lastNameField = new TextField();
    private TextField adminIdField = new TextField();
    private TextField adminEmailField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Button createNewAdminButton = new Button("Create New");
    private Button updateAdminButton = new Button("Update");
    private Button backButton = new Button("Back");
    private Button logoutButton = new Button("Logout");

    private VBox centerPanel = new VBox(15);

    public ManageAdminView() {
        LOGGER.debug("Initialising Manage Admin View");
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
    }

    public void initialiseCoreUIComponents() {
        initialiseAdminListComponents();
        initialiseAdminDetailsComponents();
        initialiseActionButtons();
    }

    private void initialiseAdminListComponents() {
        adminListView.setId("adminListView");

        refreshAdminListButton.setId("refreshAdminListButton");
        refreshAdminListButton.setOnAction(ManageAdminsController::refreshListOfAdmins);

        deleteAdminButton.setId("deleteAdminButton");
        deleteAdminButton.setOnAction(ManageAdminsController::deleteAdmin);

        selectAdminButton.setId("selectAdminButton");
        selectAdminButton.setOnAction(ManageAdminsController::selectAdmin);
    }

    private void initialiseAdminDetailsComponents() {
        adminIdField.setId("adminIdField");
        adminIdField.setEditable(false);

        firstNameField.setId("firstNameField");
        firstNameField.setPromptText("Enter First Name");

        lastNameField.setId("lastNameField");
        lastNameField.setPromptText("Enter Last Name");

        adminEmailField.setId("adminEmailField");
        adminEmailField.setPromptText("Enter Email Address");

        passwordField.setId("passwordField");
        passwordField.setPromptText("Enter Password");
    }

    private void initialiseActionButtons() {
        createNewAdminButton.setId("createNewAdminButton");
        createNewAdminButton.setOnAction(ManageAdminsController::createNewAdmin);

        updateAdminButton.setId("updateAdminButton");
        updateAdminButton.setOnAction(ManageAdminsController::updateAdmin);

        backButton.setId("backButton");
        backButton.setOnAction(ManageAdminsController::handleBack);

        logoutButton.setId("logoutButton");
        logoutButton.setOnAction(ManageAdminsController::handleLogout);
    }

    @Override
    public void layoutCoreUIComponents() {
        organiseLeftPanel();
        organiseCenterPanel();
    }

    private void organiseLeftPanel() {
        leftPanel.setPadding(new Insets(10));

        Label adminListLabel = new Label("Admin List");
        adminListLabel.getStyleClass().add("section-header");

        HBox displayActions = new HBox(10, selectAdminButton, deleteAdminButton);
        HBox displayControls = new HBox(10, refreshAdminListButton);

        leftPanel.getChildren().addAll(adminListLabel, displayActions, displayControls, adminListView);

        leftPanel.setMinWidth(350);
        leftPanel.setPrefWidth(400);
        leftPanel.setPadding(new Insets(20));
        leftPanel.getStyleClass().add("left-panel");

        setLeft(leftPanel);
    }

    public void organiseCenterPanel() {

        Label detailsLabel = new Label("Admin Details");
        detailsLabel.getStyleClass().add("section-header");

        adminDetailsPane.setHgap(10);
        adminDetailsPane.setVgap(10);
        adminDetailsPane.setPadding(new Insets(10));

        organiseAdminDetailsForm();

        HBox actionButtons = new HBox(10, createNewAdminButton, updateAdminButton);
        actionButtons.setAlignment(CENTER);
        actionButtons.setPadding(new Insets(10));

        HBox navigationButtons = new HBox(10, backButton, logoutButton);
        navigationButtons.setAlignment(CENTER);
        navigationButtons.setPadding(new Insets(10));

        centerPanel.getChildren().addAll(detailsLabel, adminDetailsPane, actionButtons, navigationButtons);
        centerPanel.setPadding(new Insets(20));
        centerPanel.setAlignment(CENTER);
        centerPanel.getStyleClass().add("center-panel");

        setCenter(centerPanel);
    }

    private void organiseAdminDetailsForm() {
        adminDetailsPane.addRow(0, new Label("Admin Id:"), adminIdField);
        adminDetailsPane.addRow(1, new Label("Email Address:"), adminEmailField);
        adminDetailsPane.addRow(2, new Label("First Name:"), firstNameField);
        adminDetailsPane.addRow(3, new Label("Last Name:"), lastNameField);
        adminDetailsPane.addRow(4, new Label("Password:"), passwordField);
    }

    @Override
    public void styleCoreUIComponents() {
        setPrefSize(900, 550);
        getStyleClass().add("manage-admin-view");

        leftPanel.getStyleClass().add("left-panel");
        adminListView.setPrefWidth(250);
        adminListView.getStyleClass().add("admin-list");

        centerPanel.getStyleClass().add("center-panel");
        adminDetailsPane.getStyleClass().add("details-pane");

        createNewAdminButton.getStyleClass().add("action-button");
        updateAdminButton.getStyleClass().add("action-button");
        selectAdminButton.getStyleClass().add("action-button");
        deleteAdminButton.getStyleClass().add("action-button");
        refreshAdminListButton.getStyleClass().add("action-button");

        backButton.getStyleClass().add("navigate-button");
        logoutButton.getStyleClass().add("navigate-button");

        adminIdField.getStyleClass().add("read-only-field");
        firstNameField.getStyleClass().add("input-field");
        lastNameField.getStyleClass().add("input-field");
        adminEmailField.getStyleClass().add("input-field");
        passwordField.getStyleClass().add("input-field");
    }

    public void clearFields() {
        adminIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        adminEmailField.clear();
        passwordField.clear();
    }
}
