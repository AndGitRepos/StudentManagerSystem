package sms.gradle.view.Frames;

import static javafx.geometry.Pos.CENTER;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.AdminControllers.StaffDashboardController;
import sms.gradle.view.CoreViewInterface;

@Getter
public class StaffDashboardView extends VBox implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private Label welcomeLabel;
    private Button manageAssessmentsButton;
    private Button manageModulesButton;
    private Button manageCoursesButton;
    private Button manageStudentsButton;
    private Button manageAdminButton;
    private Button viewCourseButton;
    private Button logoutButton;
    private ScrollPane courseScrollPane;
    private VBox courseListContainer;

    public StaffDashboardView() {
        LOGGER.debug("Initialising Staff Dashboard View");
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        assignButtonActions();
        LOGGER.debug("Staff Dashboard View initialised");
    }

    private void assignButtonActions() {
        viewCourseButton.setOnAction(StaffDashboardController::handleViewCourseButton);
        manageAssessmentsButton.setOnAction(StaffDashboardController::handleManageAssessmentsButton);
        manageModulesButton.setOnAction(StaffDashboardController::handleManageModulesButton);
        manageCoursesButton.setOnAction(StaffDashboardController::handleManageCoursesButton);
        manageStudentsButton.setOnAction(StaffDashboardController::handleManageStudentsButton);
        manageAdminButton.setOnAction(StaffDashboardController::handleManageAdminButton);
        logoutButton.setOnAction(StaffDashboardController::handleLogoutButton);
    }

    @Override
    public void initialiseCoreUIComponents() {
        welcomeLabel = new Label("Welcome to Staff Dashboard");

        courseListContainer = new VBox(5);
        courseScrollPane = new ScrollPane(courseListContainer);
        courseScrollPane.setPrefViewportHeight(200);
        courseScrollPane.setPrefViewportWidth(100);
        courseScrollPane.setFitToWidth(true);

        viewCourseButton = new Button("View Course");
        manageAssessmentsButton = new Button("Manage Assessments");
        manageModulesButton = new Button("Manage Modules");
        manageCoursesButton = new Button("Manage Courses");
        manageStudentsButton = new Button("Manage Students");
        manageAdminButton = new Button("Manage Staff");
        logoutButton = new Button("Logout");

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
        getStyleClass().add("staff-dashboard-container");
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
