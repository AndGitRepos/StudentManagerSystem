package sms.gradle.view.frames.admin;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.StudentDetailViewController;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Module;
import sms.gradle.model.entities.Student;
import sms.gradle.view.CoreViewInterface;

/**
 * Student View that displays detailed information about a student and their modules/assessments.
 * This view is shown when the "View Student" button is clicked in other views.
 */
public final class StudentDetailView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final double SIDEBAR_WIDTH_PERCENT = 0.25;
    private static final double STATS_PANEL_HEIGHT_PERCENT = 0.15;

    // Sidebar components
    private VBox sidebar = new VBox(15);
    private Label studentNameLabel = new Label("Student Name");
    private VBox moduleListContainer = new VBox(5);
    private ScrollPane moduleScrollPane = new ScrollPane(moduleListContainer);
    private HBox selectedModuleRow = null;
    private Button moduleDetailButton = new Button("View Module");
    private VBox assessmentListContainer = new VBox(5);
    private ScrollPane assessmentScrollPane = new ScrollPane(assessmentListContainer);
    private HBox selectedAssessmentRow = null;
    private Button assessmentDetailButton = new Button("View Assessment");
    private Button backButton = new Button("Back");
    private Button logoutButton = new Button("Logout");

    // Stats panel components
    private HBox statsPanel = new HBox(30);
    private Label totalModulesLabel = new Label("Total Modules: 0");
    private Label averageGradeLabel = new Label("Average Grade: 0.0");
    private Label completionRateLabel = new Label("Completion Rate: 0%");

    // Main information panel components
    private VBox mainInfoPanel = new VBox(15);
    private GridPane studentDetailsGrid = new GridPane();
    private Label studentIdLabel = new Label("Student ID:");
    private Label studentIdValueLabel = new Label("");
    private Label firstNameLabel = new Label("First Name:");
    private Label firstNameValueLabel = new Label("");
    private Label lastNameLabel = new Label("Last Name:");
    private Label lastNameValueLabel = new Label("");
    private Label emailLabel = new Label("Email:");
    private Label emailValueLabel = new Label("");
    private Label joinDateLabel = new Label("Join Date:");
    private Label joinDateValueLabel = new Label("");
    private Label modulesHeaderLabel = new Label("Enrolled Modules");
    private VBox enrolledModulesContainer = new VBox(5);
    private ScrollPane enrolledModulesScrollPane = new ScrollPane(enrolledModulesContainer);

    // Current student ID
    private int currentStudentId = -1;

    public StudentDetailView() {
        LOGGER.debug("Initialising Student View");
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        assignButtonActions();

        setupResponsiveBehavior();

        LOGGER.debug("Student View initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        // Initialize buttons as disabled until selection is made
        moduleDetailButton.setDisable(true);
        assessmentDetailButton.setDisable(true);

        // Set up scroll panes
        moduleScrollPane.setFitToWidth(true);
        assessmentScrollPane.setFitToWidth(true);
        enrolledModulesScrollPane.setFitToWidth(true);

        // Set up grid for student details
        studentDetailsGrid.setHgap(10);
        studentDetailsGrid.setVgap(10);
        studentDetailsGrid.setPadding(new Insets(10));
    }

    @Override
    public void layoutCoreUIComponents() {
        // Sidebar layout
        sidebar.getChildren()
                .addAll(
                        studentNameLabel,
                        new Label("Modules:"),
                        moduleScrollPane,
                        moduleDetailButton,
                        new Label("Assessments:"),
                        assessmentScrollPane,
                        assessmentDetailButton,
                        backButton,
                        logoutButton);
        sidebar.setPadding(new Insets(20));
        sidebar.setAlignment(Pos.TOP_CENTER);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(7, spacer);

        // Stats panel layout
        statsPanel.getChildren().addAll(totalModulesLabel, averageGradeLabel, completionRateLabel);
        statsPanel.setPadding(new Insets(20));
        statsPanel.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(statsPanel, Priority.ALWAYS);

        // Student details grid layout
        studentDetailsGrid.add(studentIdLabel, 0, 0);
        studentDetailsGrid.add(studentIdValueLabel, 1, 0);
        studentDetailsGrid.add(firstNameLabel, 0, 1);
        studentDetailsGrid.add(firstNameValueLabel, 1, 1);
        studentDetailsGrid.add(lastNameLabel, 0, 2);
        studentDetailsGrid.add(lastNameValueLabel, 1, 2);
        studentDetailsGrid.add(emailLabel, 0, 3);
        studentDetailsGrid.add(emailValueLabel, 1, 3);
        studentDetailsGrid.add(joinDateLabel, 0, 4);
        studentDetailsGrid.add(joinDateValueLabel, 1, 4);

        // Main panel layout
        mainInfoPanel.getChildren().addAll(studentDetailsGrid, modulesHeaderLabel, enrolledModulesScrollPane);
        mainInfoPanel.setPadding(new Insets(20));
        VBox.setVgrow(enrolledModulesScrollPane, Priority.ALWAYS);

        // Main content layout
        VBox mainContent = new VBox();
        mainContent.getChildren().addAll(statsPanel, mainInfoPanel);
        VBox.setVgrow(mainInfoPanel, Priority.ALWAYS);

        // Set main layout
        setLeft(sidebar);
        setCenter(mainContent);
    }

    @Override
    public void styleCoreUIComponents() {
        getStyleClass().add("manage-student-view");

        // Sidebar
        sidebar.getStyleClass().add("left-panel");
        studentNameLabel.getStyleClass().add("section-header");
        studentNameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        moduleDetailButton.getStyleClass().add("action-button");
        assessmentDetailButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigate-button");
        logoutButton.getStyleClass().add("navigate-button");

        // Stats panel
        statsPanel.getStyleClass().add("details-pane");
        totalModulesLabel.getStyleClass().add("stats-label");
        averageGradeLabel.getStyleClass().add("stats-label");
        completionRateLabel.getStyleClass().add("stats-label");

        // Student details
        studentDetailsGrid.getStyleClass().add("student-details-grid");
        studentIdLabel.getStyleClass().add("detail-label");
        firstNameLabel.getStyleClass().add("detail-label");
        lastNameLabel.getStyleClass().add("detail-label");
        emailLabel.getStyleClass().add("detail-label");
        joinDateLabel.getStyleClass().add("detail-label");

        studentIdValueLabel.getStyleClass().add("detail-value");
        firstNameValueLabel.getStyleClass().add("detail-value");
        lastNameValueLabel.getStyleClass().add("detail-value");
        emailValueLabel.getStyleClass().add("detail-value");
        joinDateValueLabel.getStyleClass().add("detail-value");

        modulesHeaderLabel.getStyleClass().add("section-header");
        modulesHeaderLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        // Main information panel
        mainInfoPanel.getStyleClass().add("center-panel");
        enrolledModulesScrollPane.getStyleClass().add("scroll-pane");
        enrolledModulesContainer.getStyleClass().add("module-list-container");

        // Scroll panes
        moduleScrollPane.getStyleClass().add("sidebar-scroll-pane");
        assessmentScrollPane.getStyleClass().add("sidebar-scroll-pane");
        moduleListContainer.getStyleClass().add("sidebar-list-container");
        assessmentListContainer.getStyleClass().add("sidebar-list-container");
    }

    /**
     * Sets up responsive behaviour for the view components
     */
    private void setupResponsiveBehavior() {
        widthProperty().addListener((obs, oldVal, newVal) -> {
            double sidebarWidth = newVal.doubleValue() * SIDEBAR_WIDTH_PERCENT;
            sidebar.setPrefWidth(sidebarWidth);

            double buttonWidth = sidebarWidth * 0.9;
            moduleDetailButton.setPrefWidth(buttonWidth);
            assessmentDetailButton.setPrefWidth(buttonWidth);
            backButton.setPrefWidth(buttonWidth);
            logoutButton.setPrefWidth(buttonWidth);
        });

        heightProperty().addListener((obs, oldVal, newVal) -> {
            double statsPanelHeight = newVal.doubleValue() * STATS_PANEL_HEIGHT_PERCENT;
            statsPanel.setPrefHeight(statsPanelHeight);

            double availableHeight = newVal.doubleValue() - statsPanelHeight - 200;
            double moduleHeight = availableHeight * 0.25;
            double assessmentHeight = availableHeight * 0.25;

            moduleScrollPane.setPrefHeight(moduleHeight);
            assessmentScrollPane.setPrefHeight(assessmentHeight);
            enrolledModulesScrollPane.setPrefHeight(availableHeight * 0.4);
        });

        sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                // When the view is added to a scene, maximize it
                newScene.windowProperty().addListener((prop, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        maximizeToScreen();
                    }
                });
            }
        });
    }

    /**
     * Maximizes the view to fill the available screen space without going into fullscreen mode.
     * This method is called when the view is added to a scene and window.
     */
    public void maximizeToScreen() {
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

        if (getScene() != null && getScene().getWindow() != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) getScene().getWindow();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            stage.setFullScreen(false);

            LOGGER.debug("View maximized to screen dimensions: {}x{}", bounds.getWidth(), bounds.getHeight());
        }
    }

    /**
     * Sets the current student ID and updates the student information and module/assessment lists
     * @param studentId The ID of the student to display
     */
    public void setStudentId(int studentId) {
        this.currentStudentId = studentId;
        updateStudentInformation();
        updateModuleList();
        updateAssessmentList();
        updateEnrolledModulesList();

        // Log the update for debugging
        LOGGER.debug("Student ID set to: {}", studentId);
    }

    /**
     * Updates the student information labels with data from StudentDAO
     */
    private void updateStudentInformation() {
        if (currentStudentId <= 0) {
            LOGGER.warn("No student ID set, cannot load student information");
            studentNameLabel.setText("Student Name");
            studentIdValueLabel.setText("");
            firstNameValueLabel.setText("");
            lastNameValueLabel.setText("");
            emailValueLabel.setText("");
            joinDateValueLabel.setText("");
            return;
        }

        try {
            Student student = StudentDetailViewController.loadStudent(currentStudentId);

            if (student != null) {
                String fullName = student.getFirstName() + " " + student.getLastName();
                studentNameLabel.setText(fullName);
                studentIdValueLabel.setText(String.valueOf(student.getId()));
                firstNameValueLabel.setText(student.getFirstName());
                lastNameValueLabel.setText(student.getLastName());
                emailValueLabel.setText(student.getEmail());
                joinDateValueLabel.setText(
                        student.getJoinDate() != null ? student.getJoinDate().toString() : "Not set");

                // Log the student details for debugging
                LOGGER.debug("Updated student information for student ID: {}", currentStudentId);
                LOGGER.debug("Student name: {}", fullName);
                LOGGER.debug("Student email: {}", student.getEmail());
            } else {
                LOGGER.warn("Student not found for ID: {}", currentStudentId);
                studentNameLabel.setText("Student Not Found");
                studentIdValueLabel.setText("");
                firstNameValueLabel.setText("");
                lastNameValueLabel.setText("");
                emailValueLabel.setText("");
                joinDateValueLabel.setText("");
            }
        } catch (Exception e) {
            LOGGER.error("Error loading student information for student ID: {}", currentStudentId, e);
            studentNameLabel.setText("Error Loading Student");
            studentIdValueLabel.setText("");
            firstNameValueLabel.setText("");
            lastNameValueLabel.setText("");
            emailValueLabel.setText("");
            joinDateValueLabel.setText("");
        }
    }

    private void assignButtonActions() {
        moduleDetailButton.setOnAction(StudentDetailViewController::handleViewModuleDetailButton);
        assessmentDetailButton.setOnAction(StudentDetailViewController::handleViewAssessmentDetailButton);
        backButton.setOnAction(StudentDetailViewController::handleBackButton);
        logoutButton.setOnAction(StudentDetailViewController::handleLogoutButton);
    }

    /**
     * Updates the module list in the sidebar
     */
    private void updateModuleList() {
        clearModuleSelection();
        moduleListContainer.getChildren().clear();

        if (currentStudentId <= 0) {
            LOGGER.warn("No student ID set, cannot load modules");
            return;
        }

        List<Module> modules = StudentDetailViewController.loadModulesForStudent(currentStudentId);

        if (modules.isEmpty()) {
            HBox noModulesRow = createSidebarRow("No modules found");
            moduleListContainer.getChildren().add(noModulesRow);
            return;
        }

        for (Module module : modules) {
            HBox moduleRow = createSidebarRow(module.getName());
            // Store the module ID as a property for later retrieval
            moduleRow.setUserData(module.getId());
            moduleListContainer.getChildren().add(moduleRow);
        }

        LOGGER.debug("Updated module list with {} modules", modules.size());
    }

    /**
     * Updates the assessment list in the sidebar
     */
    private void updateAssessmentList() {
        clearAssessmentSelection();
        assessmentListContainer.getChildren().clear();

        if (currentStudentId <= 0) {
            LOGGER.warn("No student ID set, cannot load assessments");
            return;
        }

        List<Assessment> assessments = StudentDetailViewController.loadAssessmentsForStudent(currentStudentId);

        if (assessments.isEmpty()) {
            HBox noAssessmentsRow = createSidebarRow("No assessments found");
            assessmentListContainer.getChildren().add(noAssessmentsRow);
            return;
        }

        for (Assessment assessment : assessments) {
            HBox assessmentRow = createSidebarRow(assessment.getName());
            // Store the assessment ID as a property for later retrieval
            assessmentRow.setUserData(assessment.getId());
            assessmentListContainer.getChildren().add(assessmentRow);
        }

        LOGGER.debug("Updated assessment list with {} assessments", assessments.size());
    }

    /**
     * Updates the enrolled modules list in the main panel
     */
    private void updateEnrolledModulesList() {
        enrolledModulesContainer.getChildren().clear();

        if (currentStudentId <= 0) {
            LOGGER.warn("No student ID set, cannot load enrolled modules");
            return;
        }

        List<Module> modules = StudentDetailViewController.loadModulesForStudent(currentStudentId);
        totalModulesLabel.setText("Total Modules: " + modules.size());

        LOGGER.debug("Loaded {} modules for enrolled modules list", modules.size());

        if (modules.isEmpty()) {
            Label noModulesLabel = new Label("No modules enrolled");
            noModulesLabel.getStyleClass().add("no-data-label");
            enrolledModulesContainer.getChildren().add(noModulesLabel);
            return;
        }

        for (Module module : modules) {
            HBox moduleRow = createEnrolledModuleRow(module);
            enrolledModulesContainer.getChildren().add(moduleRow);
            LOGGER.debug("Added module to enrolled list: {}", module.getName());
        }

        updateStudentStatistics();
    }

    /**
     * Creates a row for the enrolled modules list
     * @param module The module to display
     * @return HBox containing the module information
     */
    private HBox createEnrolledModuleRow(Module module) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10));
        row.getStyleClass().add("enrolled-module-row");

        VBox moduleInfo = new VBox(5);
        Label nameLabel = new Label(module.getName());
        nameLabel.getStyleClass().add("module-name");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        Label descriptionLabel = new Label(module.getDescription());
        descriptionLabel.getStyleClass().add("module-description");
        descriptionLabel.setWrapText(true);

        Label lecturerLabel = new Label("Lecturer: " + module.getLecturer());
        lecturerLabel.getStyleClass().add("module-lecturer");

        moduleInfo.getChildren().addAll(nameLabel, descriptionLabel, lecturerLabel);

        row.getChildren().add(moduleInfo);
        HBox.setHgrow(moduleInfo, Priority.ALWAYS);

        return row;
    }

    /**
     * Updates the student statistics panel with data from the controller
     */
    private void updateStudentStatistics() {
        if (currentStudentId <= 0) {
            LOGGER.warn("No student ID set, cannot update statistics");
            totalModulesLabel.setText("Total Modules: 0");
            averageGradeLabel.setText("Average Grade: 0.0");
            completionRateLabel.setText("Completion Rate: 0%");
            return;
        }

        double[] stats = StudentDetailViewController.calculateStudentStatistics(currentStudentId);
        int totalModules = (int) stats[0];
        double averageGrade = stats[1];
        double completionRate = stats[2];

        totalModulesLabel.setText("Total Modules: " + totalModules);
        averageGradeLabel.setText("Average Grade: " + String.format("%.1f", averageGrade));
        completionRateLabel.setText("Completion Rate: " + String.format("%.0f%%", completionRate));

        LOGGER.debug(
                "Updated statistics for student ID {}: {} modules, {} avg grade, {}% completion rate",
                currentStudentId, totalModules, averageGrade, completionRate);
    }

    private HBox createSidebarRow(String text) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(5));

        Label label = new Label(text);
        HBox.setHgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);
        row.getChildren().add(label);

        row.setOnMouseEntered(e -> {
            if (row != selectedModuleRow && row != selectedAssessmentRow) {
                row.setStyle("-fx-background-color: #f0f0f0;");
            }
        });

        row.setOnMouseExited(e -> {
            if (row != selectedModuleRow && row != selectedAssessmentRow) {
                row.setStyle("-fx-background-color: transparent;");
            }
        });

        row.setOnMouseClicked(e -> {
            LOGGER.debug(text + " selected");

            if (moduleListContainer.getChildren().contains(row)) {
                if (selectedModuleRow != null) {
                    selectedModuleRow.setStyle("-fx-background-color: transparent;");
                }
                selectedModuleRow = row;
                row.setStyle("-fx-background-color: #cce0ff;");
                moduleDetailButton.setDisable(false);
            } else {
                if (selectedAssessmentRow != null) {
                    selectedAssessmentRow.setStyle("-fx-background-color: transparent;");
                }
                selectedAssessmentRow = row;
                row.setStyle("-fx-background-color: #cce0ff;");
                assessmentDetailButton.setDisable(false);
            }
        });

        row.getStyleClass().add("sidebar-row");
        row.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(row, Priority.ALWAYS);

        return row;
    }

    /**
     * Clears all selections (modules and assessments)
     */
    public void clearSelections() {
        clearModuleSelection();
        clearAssessmentSelection();
    }

    /**
     * Clears the current module selection and disables the view button
     */
    private void clearModuleSelection() {
        if (selectedModuleRow != null) {
            selectedModuleRow.setStyle("-fx-background-color: transparent;");
            selectedModuleRow = null;
            moduleDetailButton.setDisable(true);
        }
    }

    /**
     * Clears the current assessment selection and disables the view button
     */
    private void clearAssessmentSelection() {
        if (selectedAssessmentRow != null) {
            selectedAssessmentRow.setStyle("-fx-background-color: transparent;");
            selectedAssessmentRow = null;
            assessmentDetailButton.setDisable(true);
        }
    }

    /**
     * Gets the currently selected module row
     * @return The selected module row, or null if no module is selected
     */
    public HBox getSelectedModuleRow() {
        return selectedModuleRow;
    }

    /**
     * Gets the currently selected assessment row
     * @return The selected assessment row, or null if no assessment is selected
     */
    public HBox getSelectedAssessmentRow() {
        return selectedAssessmentRow;
    }
}
