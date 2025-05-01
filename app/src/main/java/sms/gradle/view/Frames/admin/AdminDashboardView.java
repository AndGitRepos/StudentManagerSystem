package sms.gradle.view.frames.admin;

import static javafx.geometry.Pos.CENTER;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.AdminDashboardController;
import sms.gradle.view.CoreViewInterface;

public class AdminDashboardView extends VBox implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private Label welcomeLabel = new Label("Welcome to Admin Dashboard");
    private Button manageAssessmentsButton = new Button("Manage Assessments");
    private Button manageModulesButton = new Button("Manage Modules");
    private Button manageCoursesButton = new Button("Manage Courses");
    private Button manageStudentsButton = new Button("Manage Students");
    private Button manageAdminButton = new Button("Manage Admin");
    private Button viewCourseButton = new Button("View Course");
    private Button logoutButton = new Button("Logout");
    private VBox courseListContainer = new VBox(5);
    private ScrollPane courseScrollPane = new ScrollPane(courseListContainer);

    public AdminDashboardView() {
        LOGGER.debug("Initialising Admin Dashboard View");
        getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        assignButtonActions();
        LOGGER.debug("Admin Dashboard View initialised");
    }

    private void assignButtonActions() {
        viewCourseButton.setOnAction(AdminDashboardController::handleViewCourseButton);
        manageAssessmentsButton.setOnAction(AdminDashboardController::handleManageAssessmentsButton);
        manageModulesButton.setOnAction(AdminDashboardController::handleManageModulesButton);
        manageCoursesButton.setOnAction(AdminDashboardController::handleManageCoursesButton);
        manageStudentsButton.setOnAction(AdminDashboardController::handleManageStudentsButton);
        manageAdminButton.setOnAction(AdminDashboardController::handleManageAdminButton);
        logoutButton.setOnAction(AdminDashboardController::handleLogoutButton);
    }

    @Override
    public void initialiseCoreUIComponents() {
        courseScrollPane.setPrefViewportHeight(200);
        courseScrollPane.setPrefViewportWidth(100);
        courseScrollPane.setFitToWidth(true);

        viewCourseButton.setPrefWidth(200);
        manageAssessmentsButton.setPrefWidth(200);
        manageModulesButton.setPrefWidth(200);
        manageCoursesButton.setPrefWidth(200);
        manageStudentsButton.setPrefWidth(200);
        manageAdminButton.setPrefWidth(200);
        logoutButton.setPrefWidth(200);
    }

    @Override
    public void styleCoreUIComponents() {
        getStyleClass().add("admin-dashboard-container");
        welcomeLabel.getStyleClass().add("welcome-label");

        courseScrollPane.getStyleClass().add("course-scroll-pane");
        courseListContainer.getStyleClass().add("course-list-container");

        viewCourseButton.getStyleClass().add("menu-button");
        manageAssessmentsButton.getStyleClass().add("menu-button");
        manageModulesButton.getStyleClass().add("menu-button");
        manageCoursesButton.getStyleClass().add("menu-button");
        manageStudentsButton.getStyleClass().add("menu-button");
        manageAdminButton.getStyleClass().add("menu-button");
        logoutButton.getStyleClass().add("menu-button");
    }

    @Override
    public void layoutCoreUIComponents() {
        VBox menu = new VBox(10);
        menu.setAlignment(CENTER);
        menu.setPadding(new Insets(20));

        menu.getChildren()
                .addAll(
                        welcomeLabel,
                        courseScrollPane,
                        viewCourseButton,
                        manageCoursesButton,
                        manageModulesButton,
                        manageAssessmentsButton,
                        manageStudentsButton,
                        manageAdminButton,
                        logoutButton);

        this.getChildren().add(menu);
        this.setAlignment(CENTER);
    }
}
