package sms.gradle.view.frames.admin;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.ModuleDetailViewController;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Student;
import sms.gradle.view.CoreViewInterface;

/**
 * Module View that displays detailed information about a module and its students.
 * This view is shown when the "View Module" button is clicked in the CourseDetailView.
 */
public final class ModuleDetailView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final double SIDEBAR_WIDTH_PERCENT = 0.25;
    private static final double STATS_PANEL_HEIGHT_PERCENT = 0.15;

    // Sidebar components
    private VBox sidebar = new VBox(15);
    private Label moduleNameLabel = new Label("Module Name");
    private Label moduleDescriptionLabel = new Label("Select a module to view details");
    private Label moduleLecturerLabel = new Label("Lecturer: ");
    private Button assessmentDetailButton = new Button("View Assessment");
    private Button backButton = new Button("Back");
    private Button logoutButton = new Button("Logout");
    private VBox assessmentListContainer = new VBox(5);
    private ScrollPane assessmentScrollPane = new ScrollPane(assessmentListContainer);
    private HBox selectedAssessmentRow = null;

    // Stats panel components
    private HBox statsPanel = new HBox(30);
    private Label totalStudentsLabel = new Label("Total Students: 0");
    private Label averageGradeLabel = new Label("Average Grade: 0.0");
    private Label passRateLabel = new Label("Pass Rate: 0%");

    // Main information panel components
    private VBox mainInfoPanel = new VBox(10);
    private VBox studentListContainer = new VBox(5);
    private ScrollPane studentScrollPane = new ScrollPane(studentListContainer);
    private HBox selectedStudentRow = null;
    private Button viewStudentButton = new Button("View Student Details");
    private VBox studentSection = new VBox(0);
    private HBox headerRow;

    // Current module ID
    private int currentModuleId = -1;

    public ModuleDetailView() {
        LOGGER.debug("Initialising Module View");
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        assignButtonActions();

        setupResponsiveBehavior();

        LOGGER.debug("Module View initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        // Main information panel components
        viewStudentButton.setDisable(true);
        studentScrollPane.setFitToWidth(true);

        headerRow = createStudentRow("ID", "Name", "Email", "Join Date");
        headerRow.getStyleClass().add("student-header-row");

        assessmentScrollPane.setFitToWidth(true);
    }

    @Override
    public void layoutCoreUIComponents() {
        sidebar.getChildren()
                .addAll(
                        moduleNameLabel,
                        moduleDescriptionLabel,
                        moduleLecturerLabel,
                        new Label("Assessments:"),
                        assessmentScrollPane,
                        assessmentDetailButton,
                        backButton,
                        logoutButton);
        sidebar.setPadding(new Insets(20));
        sidebar.setAlignment(Pos.TOP_CENTER);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(5, spacer);

        statsPanel.getChildren().addAll(totalStudentsLabel, averageGradeLabel, passRateLabel);
        statsPanel.setPadding(new Insets(20));
        statsPanel.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(statsPanel, Priority.ALWAYS);

        VBox mainContent = new VBox();
        mainContent.getChildren().addAll(statsPanel, mainInfoPanel);
        VBox.setVgrow(mainInfoPanel, Priority.ALWAYS);

        studentSection = new VBox(0);
        studentSection.getChildren().addAll(headerRow, studentScrollPane, viewStudentButton);
        studentSection.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(studentScrollPane, Priority.ALWAYS);

        mainInfoPanel.getChildren().add(studentSection);
        mainInfoPanel.setPadding(new Insets(20));
        VBox.setVgrow(studentSection, Priority.ALWAYS);

        setLeft(sidebar);
        setCenter(mainContent);
    }

    @Override
    public void styleCoreUIComponents() {
        getStyleClass().add("manage-module-view");

        // Sidebar
        sidebar.getStyleClass().add("left-panel");
        moduleNameLabel.getStyleClass().add("section-header");
        moduleNameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        moduleDescriptionLabel.getStyleClass().add("module-description-label");
        moduleDescriptionLabel.setWrapText(true);
        moduleDescriptionLabel.setMaxWidth(Double.MAX_VALUE);
        moduleDescriptionLabel.setPrefWidth(200);
        moduleLecturerLabel.getStyleClass().add("module-lecturer-label");
        assessmentDetailButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigate-button");
        logoutButton.getStyleClass().add("navigate-button");

        // Stats panel
        statsPanel.getStyleClass().add("details-pane");
        totalStudentsLabel.getStyleClass().add("stats-label");
        averageGradeLabel.getStyleClass().add("stats-label");
        passRateLabel.getStyleClass().add("stats-label");

        // Main information panel
        mainInfoPanel.getStyleClass().add("center-panel");
        studentScrollPane.getStyleClass().add("scroll-pane");
        studentListContainer.getStyleClass().add("student-list-container");

        // Scroll panes
        assessmentScrollPane.getStyleClass().add("sidebar-scroll-pane");
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
            assessmentDetailButton.setPrefWidth(buttonWidth);
            backButton.setPrefWidth(buttonWidth);
            logoutButton.setPrefWidth(buttonWidth);
        });

        heightProperty().addListener((obs, oldVal, newVal) -> {
            double statsPanelHeight = newVal.doubleValue() * STATS_PANEL_HEIGHT_PERCENT;
            statsPanel.setPrefHeight(statsPanelHeight);

            double availableHeight = newVal.doubleValue() - statsPanelHeight - 200;
            double assessmentHeight = availableHeight * 0.3;

            assessmentScrollPane.setPrefHeight(assessmentHeight);

            studentScrollPane.setPrefHeight(availableHeight * 0.8);
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
     * Sets the current module ID and updates the module information and student list
     * @param moduleId The ID of the module to display
     */
    public void setModuleId(int moduleId) {
        this.currentModuleId = moduleId;
        updateModuleInformation();
        updateStudentList();
        updateAssessmentList();
    }

    /**
     * Updates the module name and description labels with data from ModuleDAO
     */
    private void updateModuleInformation() {
        if (currentModuleId <= 0) {
            LOGGER.warn("No module ID set, cannot load module information");
            moduleNameLabel.setText("Module Name");
            moduleDescriptionLabel.setText("Select a module to view details");
            moduleLecturerLabel.setText("Lecturer: ");
            return;
        }

        try {
            ModuleDAO.findById(currentModuleId)
                    .ifPresentOrElse(
                            module -> {
                                moduleNameLabel.setText(module.getName());
                                moduleDescriptionLabel.setText(module.getDescription());
                                moduleLecturerLabel.setText("Lecturer: " + module.getLecturer());
                                LOGGER.debug("Updated module information for module ID: {}", currentModuleId);
                            },
                            () -> {
                                LOGGER.warn("Module not found for ID: {}", currentModuleId);
                                moduleNameLabel.setText("Module Not Found");
                                moduleDescriptionLabel.setText("No description available");
                                moduleLecturerLabel.setText("Lecturer: Unknown");
                            });
        } catch (Exception e) {
            LOGGER.error("Error loading module information for module ID: {}", currentModuleId, e);
            moduleNameLabel.setText("Error Loading Module");
            moduleDescriptionLabel.setText("Could not load module description");
            moduleLecturerLabel.setText("Lecturer: Unknown");
        }
    }

    private void assignButtonActions() {
        assessmentDetailButton.setOnAction(ModuleDetailViewController::handleViewAssessmentDetailButton);
        backButton.setOnAction(ModuleDetailViewController::handleBackButton);
        logoutButton.setOnAction(ModuleDetailViewController::handleLogoutButton);
        viewStudentButton.setOnAction(ModuleDetailViewController::handleViewStudentDetailButton);
    }

    /**
     * Updates the student list with students enrolled in the current module
     */
    private void updateStudentList() {
        clearStudentSelection();
        studentListContainer.getChildren().clear();

        if (currentModuleId <= 0) {
            LOGGER.warn("No module ID set, cannot load students");
            return;
        }

        List<Student> students = ModuleDetailViewController.loadStudentsForModule(currentModuleId);
        totalStudentsLabel.setText("Total Students: " + students.size());

        for (Student student : students) {
            String fullName = student.getFirstName() + " " + student.getLastName();
            String joinDate =
                    student.getJoinDate() != null ? student.getJoinDate().toString() : "N/A";

            studentListContainer
                    .getChildren()
                    .add(createStudentRow(String.valueOf(student.getId()), fullName, student.getEmail(), joinDate));
        }

        // Update statistics
        updateModuleStatistics();
    }

    /**
     * Updates the assessment list with assessments for the current module
     */
    private void updateAssessmentList() {
        clearAssessmentSelection();
        assessmentListContainer.getChildren().clear();

        if (currentModuleId <= 0) {
            LOGGER.warn("No module ID set, cannot load assessments");
            return;
        }

        List<Assessment> assessments = ModuleDetailViewController.loadAssessmentsForModule(currentModuleId);

        if (assessments.isEmpty()) {
            assessmentListContainer.getChildren().add(createSidebarRow("No assessments found"));
            return;
        }

        for (Assessment assessment : assessments) {
            String dueDate =
                    assessment.getDueDate() != null ? assessment.getDueDate().toString() : "N/A";
            assessmentListContainer
                    .getChildren()
                    .add(createSidebarRow(assessment.getName() + " (" + dueDate + ")", assessment.getId()));
        }
    }

    /**
     * Updates the module statistics panel with data from the controller
     */
    private void updateModuleStatistics() {
        if (currentModuleId <= 0) {
            LOGGER.warn("No module ID set, cannot update statistics");
            totalStudentsLabel.setText("Total Students: 0");
            averageGradeLabel.setText("Average Grade: 0.0");
            passRateLabel.setText("Pass Rate: 0%");
            return;
        }

        double[] stats = ModuleDetailViewController.calculateModuleStatistics(currentModuleId);
        int totalStudents = (int) stats[0];
        double averageGrade = stats[1];
        double passRate = stats[2];

        totalStudentsLabel.setText("Total Students: " + totalStudents);
        averageGradeLabel.setText("Average Grade: " + String.format("%.1f", averageGrade));
        passRateLabel.setText("Pass Rate: " + String.format("%.0f%%", passRate));

        LOGGER.debug(
                "Updated statistics for module ID {}: {} students, {} avg grade, {}% pass rate",
                currentModuleId, totalStudents, averageGrade, passRate);
    }

    private HBox createSidebarRow(String text, Integer assessmentId) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(5));

        // Store the assessment ID as user data for later retrieval
        row.setUserData(assessmentId);

        Label label = new Label(text);
        HBox.setHgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);
        row.getChildren().add(label);

        row.setOnMouseEntered(e -> {
            if (row != selectedAssessmentRow) {
                row.setStyle("-fx-background-color: #f0f0f0;");
            }
        });

        row.setOnMouseExited(e -> {
            if (row != selectedAssessmentRow) {
                row.setStyle("-fx-background-color: transparent;");
            }
        });

        row.setOnMouseClicked(e -> {
            LOGGER.debug(text + " selected");

            if (selectedAssessmentRow != null) {
                selectedAssessmentRow.setStyle("-fx-background-color: transparent;");
            }
            selectedAssessmentRow = row;
            row.setStyle("-fx-background-color: #cce0ff;");

            assessmentDetailButton.setDisable(false);
        });

        row.getStyleClass().add("sidebar-row");
        row.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(row, Priority.ALWAYS);

        return row;
    }

    private HBox createSidebarRow(String text) {
        return createSidebarRow(text, null);
    }

    public void clearSelections() {
        clearAssessmentSelection();
        clearStudentSelection();
    }

    /**
     * Clears the current assessment selection
     */
    private void clearAssessmentSelection() {
        if (selectedAssessmentRow != null) {
            selectedAssessmentRow.setStyle("-fx-background-color: transparent;");
            selectedAssessmentRow = null;
            assessmentDetailButton.setDisable(true);
        }
    }

    /**
     * Creates a row for the student list with the given information.
     *
     * @param id Student ID
     * @param name Student name
     * @param email Student email
     * @param joinDate Student join date
     * @return HBox containing the student information
     */
    private HBox createStudentRow(String id, String name, String email, String joinDate) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 5, 10, 5));
        row.getStyleClass().add("student-row");
        row.setSpacing(20);

        double totalWidth = 100.0;
        double idWidth = totalWidth * 0.15;
        double nameWidth = totalWidth * 0.25;
        double emailWidth = totalWidth * 0.40;
        double dateWidth = totalWidth * 0.20;

        Label idLabel = new Label(id);
        Label nameLabel = new Label(name);
        Label emailLabel = new Label(email);
        Label joinDateLabel = new Label(joinDate);

        HBox.setHgrow(idLabel, Priority.ALWAYS);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        HBox.setHgrow(emailLabel, Priority.ALWAYS);
        HBox.setHgrow(joinDateLabel, Priority.ALWAYS);

        idLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        emailLabel.setMaxWidth(Double.MAX_VALUE);
        joinDateLabel.setMaxWidth(Double.MAX_VALUE);

        idLabel.setMinWidth(50);
        nameLabel.setMinWidth(100);
        emailLabel.setMinWidth(150);
        joinDateLabel.setMinWidth(80);

        idLabel.prefWidthProperty().bind(row.widthProperty().multiply(idWidth / totalWidth));
        nameLabel.prefWidthProperty().bind(row.widthProperty().multiply(nameWidth / totalWidth));
        emailLabel.prefWidthProperty().bind(row.widthProperty().multiply(emailWidth / totalWidth));
        joinDateLabel.prefWidthProperty().bind(row.widthProperty().multiply(dateWidth / totalWidth));

        idLabel.getStyleClass().add("student-details");
        nameLabel.getStyleClass().add("student-name");
        emailLabel.getStyleClass().add("student-details");
        joinDateLabel.getStyleClass().add("student-details");

        row.getChildren().addAll(idLabel, nameLabel, emailLabel, joinDateLabel);

        row.setOnMouseEntered(e -> {
            if (row != selectedStudentRow) {
                row.setStyle("-fx-background-color: #f0f0f0;");
            }
        });

        row.setOnMouseExited(e -> {
            if (row != selectedStudentRow) {
                row.setStyle("-fx-background-color: transparent;");
            }
        });

        row.setOnMouseClicked(e -> {
            if (selectedStudentRow != null) {
                selectedStudentRow.setStyle("-fx-background-color: transparent;");
            }

            selectedStudentRow = row;
            row.setStyle("-fx-background-color: #cce0ff;");

            viewStudentButton.setDisable(false);

            LOGGER.debug("Selected student ID: " + id);
        });

        return row;
    }

    /**
     * Gets the currently selected assessment row
     * @return The selected assessment row, or null if no assessment is selected
     */
    public HBox getSelectedAssessmentRow() {
        return selectedAssessmentRow;
    }

    /**
     * Gets the ID of the currently selected student
     * @return The ID of the selected student, or null if no student is selected
     */
    /**
     * Gets the ID of the currently selected student
     * @return The ID of the selected student, or null if no student is selected
     */
    public String getSelectedStudentId() {
        if (selectedStudentRow == null) {
            return null;
        }

        HBox row = selectedStudentRow;
        if (!row.getChildren().isEmpty() && row.getChildren().get(0) instanceof Label) {
            return ((Label) row.getChildren().get(0)).getText();
        }

        return null;
    }

    /**
     * Clears the current student selection and disables the view button
     */
    private void clearStudentSelection() {
        if (selectedStudentRow != null) {
            selectedStudentRow.setStyle("-fx-background-color: transparent;");
            selectedStudentRow = null;
            viewStudentButton.setDisable(true);
        }
    }
}
