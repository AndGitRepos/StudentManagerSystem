package sms.gradle.view.Frames.admin;

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
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.ManageAdminsController;
import sms.gradle.model.entities.Admin;
import sms.gradle.view.CoreViewInterface;

@Getter
public class ManageAdminView extends BorderPane implements CoreViewInterface {

    private static final Logger LOGGER = LogManager.getLogger();

    private ListView<Admin> adminListView;
    private Button refreshAdminListButton;
    private Button selectAdminButton;
    private Button deleteAdminButton;

    private GridPane adminDetailsPane;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField adminIdField;
    private TextField adminEmailField;
    private PasswordField passwordField;

    private Button createNewAdminButton;
    private Button updateAdminButton;
    private Button backButton;
    private Button logoutButton;

    private VBox centerPanel;

    public ManageAdminView() {
        LOGGER.debug("Initialising Manage Admin View");

        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
    }

    public void initialiseCoreUIComponents() {
        initialiseAdminListComponents();
        initialiseAdminDetailsComponents();
        initialiseActionButtons();
        centerPanel = new VBox(15);
    }

    private void initialiseAdminListComponents() {
        adminListView = new ListView<>();
        adminListView.setId("adminListView");

        refreshAdminListButton = new Button("Refresh");
        refreshAdminListButton.setId("refreshAdminListButton");
        refreshAdminListButton.setOnAction(ManageAdminsController::refreshListOfAdmins);

        deleteAdminButton = new Button("Delete");
        deleteAdminButton.setId("deleteAdminButton");
        deleteAdminButton.setOnAction(ManageAdminsController::deleteAdmin);

        selectAdminButton = new Button("Select");
        selectAdminButton.setId("selectAdminButton");
        selectAdminButton.setOnAction(ManageAdminsController::selectAdmin);
    }

    private void initialiseAdminDetailsComponents() {

        adminDetailsPane = new GridPane();

        adminIdField = new TextField();
        adminIdField.setId("adminIdField");
        adminIdField.setEditable(false);

        firstNameField = new TextField();
        firstNameField.setId("firstNameField");
        firstNameField.setPromptText("Enter First Name");

        lastNameField = new TextField();
        lastNameField.setId("lastNameField");
        lastNameField.setPromptText("Enter Last Name");

        adminEmailField = new TextField();
        adminEmailField.setId("adminEmailField");
        adminEmailField.setPromptText("Enter Email Address");

        passwordField = new PasswordField();
        passwordField.setId("passwordField");
        passwordField.setPromptText("Enter Password");
    }

    private void initialiseActionButtons() {

        createNewAdminButton = new Button("Create New Admin");
        createNewAdminButton.setId("createNewAdminButton");
        createNewAdminButton.setOnAction(ManageAdminsController::createNewAdmin);

        updateAdminButton = new Button("Update Admin");
        updateAdminButton.setId("updateAdminButton");
        updateAdminButton.setOnAction(ManageAdminsController::updateAdmin);

        backButton = new Button("Back");
        backButton.setId("backButton");
        backButton.setOnAction(ManageAdminsController::handleBack);

        logoutButton = new Button("Logout");
        logoutButton.setId("logoutButton");
        logoutButton.setOnAction(ManageAdminsController::handleLogout);
    }

    @Override
    public void layoutCoreUIComponents() {
        organiseLeftPanel();
        organiseCenterPanel();
    }

    private void organiseLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));

        Label adminListLabel = new Label("Admin List");
        adminListLabel.getStyleClass().add("section-header");

        HBox displayActions = new HBox(10, selectAdminButton, deleteAdminButton);
        HBox displayControls = new HBox(10, refreshAdminListButton);

        leftPanel.getChildren().addAll(adminListLabel, displayActions, displayControls, adminListView);

        leftPanel.setMinWidth(350);
        leftPanel.setPrefWidth(450);
        leftPanel.setPadding(new Insets(20));

        setLeft(leftPanel);
    }

    public void organiseCenterPanel() {
        adminDetailsPane.setHgap(10);
        adminDetailsPane.setVgap(10);
        adminDetailsPane.setPadding(new Insets(10));

        Label detailsLabel = new Label("Admin Details");
        detailsLabel.getStyleClass().add("section-header");

        organiseAdminDetailsForm();

        HBox actionButtons = makeActionButtonsContainer();

        HBox navigationButtons = makeNavigationButtonsContainer();

        centerPanel.getChildren().addAll(detailsLabel, adminDetailsPane, actionButtons, navigationButtons);

        centerPanel.setPadding(new Insets(10));
        centerPanel.setAlignment(CENTER);

        setCenter(centerPanel);
    }

    private void organiseAdminDetailsForm() {
        adminDetailsPane.addRow(0, new Label("Admin Id:"), adminIdField);
        adminDetailsPane.addRow(1, new Label("Email Address:"), adminEmailField);
        adminDetailsPane.addRow(2, new Label("First Name:"), firstNameField);
        adminDetailsPane.addRow(3, new Label("Last Name:"), lastNameField);
        adminDetailsPane.addRow(4, new Label("Password:"), passwordField);
    }

    private HBox makeActionButtonsContainer() {
        HBox actionButtons = new HBox(10, createNewAdminButton, updateAdminButton);
        actionButtons.setAlignment(CENTER);
        actionButtons.setPadding(new Insets(10));
        return actionButtons;
    }

    private HBox makeNavigationButtonsContainer() {
        HBox navigationButtons = new HBox(10, backButton, logoutButton);
        navigationButtons.setAlignment(CENTER);
        navigationButtons.setPadding(new Insets(10));
        return navigationButtons;
    }

    @Override
    public void styleCoreUIComponents() {
        stylesForContainer();
        stylesForList();
        stylesForForm();
        stylesForButton();
        stylesForInputField();
    }

    private void stylesForContainer() {
        setPrefSize(750, 550);
        getStyleClass().add("manage-admin-view");
    }

    private void stylesForList() {
        adminListView.setPrefWidth(250);
        adminListView.getStyleClass().add("admin-list");
    }

    private void stylesForForm() {
        adminDetailsPane.getStyleClass().add("details-pane");
    }

    private void stylesForButton() {
        createNewAdminButton.getStyleClass().add("action-button");
        updateAdminButton.getStyleClass().add("action-button");
        selectAdminButton.getStyleClass().add("action-button");
        deleteAdminButton.getStyleClass().add("action-button");
        refreshAdminListButton.getStyleClass().add("action-button");

        backButton.getStyleClass().add("navigation-button");
        logoutButton.getStyleClass().add("navigation-button");
    }

    private void stylesForInputField() {
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
