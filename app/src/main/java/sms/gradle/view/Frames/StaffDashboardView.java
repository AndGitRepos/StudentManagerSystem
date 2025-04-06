package sms.gradle.view.Frames;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sms.gradle.view.CoreViewInterface;

public class StaffDashboardView extends VBox implements CoreViewInterface {

    private Label welcomeLabel;
    private Button manageAssessmentsButton;
    private Button manageModulesButton;
    private Button manageCoursesButton;
    private Button manageStudentsButton;
    private Button manageStaffButton;
    private Button viewCourseButton;
    private Button logoutButton;

    public StaffDashboardView() {
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
    }

    @Override
    public void initialiseCoreUIComponents() {
        welcomeLabel = new Label("Welcome to Staff Dashboard");

        manageAssessmentsButton = new Button("Manage Assessments");
        manageModulesButton = new Button("Manage Modules");
        manageCoursesButton = new Button("Manage Courses");
        manageStudentsButton = new Button("Manage Students");
        manageStaffButton = new Button("Manage Staff");
        viewCourseButton = new Button("View Course");
        logoutButton = new Button("Logout");

        manageAssessmentsButton.setPrefWidth(200);
        manageModulesButton.setPrefWidth(200);
        manageCoursesButton.setPrefWidth(200);
        manageStudentsButton.setPrefWidth(200);
        manageStaffButton.setPrefWidth(200);
        viewCourseButton.setPrefWidth(200);
        logoutButton.setPrefWidth(200);
    }

    @Override
    public void layoutCoreUIComponents() {
        setAlignment(Pos.TOP_CENTER);
        setSpacing(20);
        setPadding(new Insets(50));

        VBox menuContainer = new VBox(15);
        menuContainer.setAlignment(Pos.CENTER);
        menuContainer
                .getChildren()
                .addAll(
                        manageAssessmentsButton,
                        manageModulesButton,
                        manageCoursesButton,
                        manageStudentsButton,
                        manageStaffButton,
                        viewCourseButton,
                        logoutButton);

        getChildren().addAll(welcomeLabel, menuContainer);
    }

    @Override
    public void styleCoreUIComponents() {
        getStyleClass().add("staff-dashboard-container");
        welcomeLabel.getStyleClass().add("welcome-label");

        manageAssessmentsButton.getStyleClass().add("menu-button");
        manageModulesButton.getStyleClass().add("menu-button");
        manageCoursesButton.getStyleClass().add("menu-button");
        manageStudentsButton.getStyleClass().add("menu-button");
        manageStaffButton.getStyleClass().add("menu-button");
        viewCourseButton.getStyleClass().add("menu-button");
        logoutButton.getStyleClass().add("menu-button");
    }

    public void displayWelcomeMessage(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    public Button getManageAssessmentsButton() {
        return manageAssessmentsButton;
    }

    public Button getManageModulesButton() {
        return manageModulesButton;
    }

    public Button getManageCoursesButton() {
        return manageCoursesButton;
    }

    public Button getManageStudentsButton() {
        return manageStudentsButton;
    }

    public Button getManageStaffButton() {
        return manageStaffButton;
    }

    public Button getViewCourseDetailsButton() {
        return viewCourseButton;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }
}
