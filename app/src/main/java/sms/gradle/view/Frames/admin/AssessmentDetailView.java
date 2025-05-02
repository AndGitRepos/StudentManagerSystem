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
import sms.gradle.controller.admin.AssessmentDetailViewController;
import sms.gradle.model.dao.AssessmentDAO;
import sms.gradle.model.entities.Module;
import sms.gradle.model.entities.Student;
import sms.gradle.view.CoreViewInterface;

/**
 * Assessment View that displays detailed information about an assessment and its submissions.
 * This view is shown when the "View Assessment" button is clicked in the ModuleDetailView.
 */
public final class AssessmentDetailView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final double SIDEBAR_WIDTH_PERCENT = 0.25;
    private static final double STATS_PANEL_HEIGHT_PERCENT = 0.15;

    // Sidebar components
    private VBox sidebar = new VBox(15);
    private Label assessmentNameLabel = new Label("Assessment Name");
    private Label assessmentDescriptionLabel = new Label("Select an assessment to view details");
    private Label dueDateLabel = new Label("Due Date: ");
    private VBox moduleListContainer = new VBox(5);
    private ScrollPane moduleScrollPane = new ScrollPane(moduleListContainer);
    private HBox selectedModuleRow = null;
    private Button moduleDetailButton = new Button("View Module");
    private Button backButton = new Button("Back");
    private Button logoutButton = new Button("Logout");

    // Stats panel components
    private HBox statsPanel = new HBox(30);
    private Label totalSubmissionsLabel = new Label("Total Submissions: 0");
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

    // Current assessment ID
    private int currentAssessmentId = -1;

    public AssessmentDetailView() {
        LOGGER.debug("Initialising Assessment View");
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        assignButtonActions();

        setupResponsiveBehavior();

        LOGGER.debug("Assessment View initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        // Main information panel components
        viewStudentButton.setDisable(true);
        moduleDetailButton.setDisable(true);

        studentScrollPane.setFitToWidth(true);
        moduleScrollPane.setFitToWidth(true);

        headerRow = createStudentRow("ID", "Name", "Email", "Submission Date");
        headerRow.getStyleClass().add("student-header-row");
    }

    @Override
    public void layoutCoreUIComponents() {
        sidebar.getChildren()
                .addAll(
                        assessmentNameLabel,
                        assessmentDescriptionLabel,
                        dueDateLabel,
                        new Label("Modules:"),
                        moduleScrollPane,
                        moduleDetailButton,
                        backButton,
                        logoutButton);
        sidebar.setPadding(new Insets(20));
        sidebar.setAlignment(Pos.TOP_CENTER);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(6, spacer);

        statsPanel.getChildren().addAll(totalSubmissionsLabel, averageGradeLabel, passRateLabel);
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
        getStyleClass().add("manage-assessment-view");

        // Sidebar
        sidebar.getStyleClass().add("left-panel");
        assessmentNameLabel.getStyleClass().add("section-header");
        assessmentNameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        assessmentDescriptionLabel.getStyleClass().add("assessment-description-label");
        assessmentDescriptionLabel.setWrapText(true);
        assessmentDescriptionLabel.setMaxWidth(Double.MAX_VALUE);
        assessmentDescriptionLabel.setPrefWidth(200);
        dueDateLabel.getStyleClass().add("assessment-due-date-label");
        moduleDetailButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("navigate-button");
        logoutButton.getStyleClass().add("navigate-button");

        // Stats panel
        statsPanel.getStyleClass().add("details-pane");
        totalSubmissionsLabel.getStyleClass().add("stats-label");
        averageGradeLabel.getStyleClass().add("stats-label");
        passRateLabel.getStyleClass().add("stats-label");

        // Main information panel
        mainInfoPanel.getStyleClass().add("center-panel");
        studentScrollPane.getStyleClass().add("scroll-pane");
        studentListContainer.getStyleClass().add("student-list-container");

        // Scroll panes
        moduleScrollPane.getStyleClass().add("sidebar-scroll-pane");
        moduleListContainer.getStyleClass().add("sidebar-list-container");
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
            backButton.setPrefWidth(buttonWidth);
            logoutButton.setPrefWidth(buttonWidth);
        });

        heightProperty().addListener((obs, oldVal, newVal) -> {
            double statsPanelHeight = newVal.doubleValue() * STATS_PANEL_HEIGHT_PERCENT;
            statsPanel.setPrefHeight(statsPanelHeight);

            double availableHeight = newVal.doubleValue() - statsPanelHeight - 200;
            double moduleHeight = availableHeight * 0.3;

            moduleScrollPane.setPrefHeight(moduleHeight);
            studentScrollPane.setPrefHeight(availableHeight * 0.8);
        });
    }

    /**
     * Sets the current assessment ID and updates the assessment information and student list
     * @param assessmentId The ID of the assessment to display
     */
    public void setAssessmentId(int assessmentId) {
        this.currentAssessmentId = assessmentId;
        updateAssessmentInformation();
        updateStudentList();
        updateModuleList();

        // Log the update for debugging
        LOGGER.debug("Assessment ID set to: {}", assessmentId);
    }

    /**
     * Updates the assessment name and description labels with data from AssessmentDAO
     */
    private void updateAssessmentInformation() {
        if (currentAssessmentId <= 0) {
            LOGGER.warn("No assessment ID set, cannot load assessment information");
            assessmentNameLabel.setText("Assessment Name");
            assessmentDescriptionLabel.setText("Select an assessment to view details");
            dueDateLabel.setText("Due Date: ");
            return;
        }

        try {
            AssessmentDAO.findById(currentAssessmentId)
                    .ifPresentOrElse(
                            assessment -> {
                                assessmentNameLabel.setText(assessment.getName());
                                assessmentDescriptionLabel.setText(assessment.getDescription());
                                dueDateLabel.setText("Due Date: "
                                        + (assessment.getDueDate() != null
                                                ? assessment.getDueDate().toString()
                                                : "Not set"));
                                LOGGER.debug(
                                        "Updated assessment information for assessment ID: {}", currentAssessmentId);
                            },
                            () -> {
                                LOGGER.warn("Assessment not found for ID: {}", currentAssessmentId);
                                assessmentNameLabel.setText("Assessment Not Found");
                                assessmentDescriptionLabel.setText("No description available");
                                dueDateLabel.setText("Due Date: Not set");
                            });
        } catch (Exception e) {
            LOGGER.error("Error loading assessment information for assessment ID: {}", currentAssessmentId, e);
            assessmentNameLabel.setText("Error Loading Assessment");
            assessmentDescriptionLabel.setText("Could not load assessment description");
            dueDateLabel.setText("Due Date: Not set");
        }
    }

    private void assignButtonActions() {
        moduleDetailButton.setOnAction(AssessmentDetailViewController::handleViewModuleDetailButton);
        backButton.setOnAction(AssessmentDetailViewController::handleBackButton);
        logoutButton.setOnAction(AssessmentDetailViewController::handleLogoutButton);
        viewStudentButton.setOnAction(AssessmentDetailViewController::handleViewStudentDetailButton);
    }

    /**
     * Updates the student list with students who have submitted the current assessment
     */
    private void updateStudentList() {
        clearStudentSelection();
        studentListContainer.getChildren().clear();

        if (currentAssessmentId <= 0) {
            LOGGER.warn("No assessment ID set, cannot load students");
            return;
        }

        List<Student> students = AssessmentDetailViewController.loadStudentsForAssessment(currentAssessmentId);
        totalSubmissionsLabel.setText("Total Submissions: " + students.size());

        LOGGER.debug("Loaded {} students for assessment ID: {}", students.size(), currentAssessmentId);

        if (students.isEmpty()) {
            Label noStudentsLabel = new Label("No submissions found");
            noStudentsLabel.getStyleClass().add("no-data-label");
            studentListContainer.getChildren().add(noStudentsLabel);
            return;
        }

        for (Student student : students) {
            String fullName = student.getFirstName() + " " + student.getLastName();
            String submissionDate = "N/A"; // TODO: Replace with actual submission date when available

            studentListContainer
                    .getChildren()
                    .add(createStudentRow(
                            String.valueOf(student.getId()), fullName, student.getEmail(), submissionDate));
        }

        updateAssessmentStatistics();
    }

    /**
     * Updates the module list in the sidebar
     */
    private void updateModuleList() {
        clearModuleSelection();
        moduleListContainer.getChildren().clear();

        if (currentAssessmentId <= 0) {
            LOGGER.warn("No assessment ID set, cannot load modules");
            return;
        }

        List<Module> modules = AssessmentDetailViewController.loadModulesForAssessment(currentAssessmentId);

        LOGGER.debug("Loaded {} modules for assessment ID: {}", modules.size(), currentAssessmentId);

        if (modules.isEmpty()) {
            HBox noModulesRow = createSidebarRow("No modules found");
            moduleListContainer.getChildren().add(noModulesRow);
            return;
        }

        for (Module module : modules) {
            HBox moduleRow = createSidebarRow(module.getName());
            moduleRow.setUserData(module.getId());
            moduleListContainer.getChildren().add(moduleRow);
        }
    }

    /**
     * Updates the assessment statistics panel with data from the controller
     */
    private void updateAssessmentStatistics() {
        if (currentAssessmentId <= 0) {
            LOGGER.warn("No assessment ID set, cannot update statistics");
            totalSubmissionsLabel.setText("Total Submissions: 0");
            averageGradeLabel.setText("Average Grade: 0.0");
            passRateLabel.setText("Pass Rate: 0%");
            return;
        }

        double[] stats = AssessmentDetailViewController.calculateAssessmentStatistics(currentAssessmentId);
        int totalSubmissions = (int) stats[0];
        double averageGrade = stats[1];
        double passRate = stats[2];

        totalSubmissionsLabel.setText("Total Submissions: " + totalSubmissions);
        averageGradeLabel.setText("Average Grade: " + String.format("%.1f", averageGrade));
        passRateLabel.setText("Pass Rate: " + String.format("%.0f%%", passRate));

        LOGGER.debug(
                "Updated statistics for assessment ID {}: {} submissions, {} avg grade, {}% pass rate",
                currentAssessmentId, totalSubmissions, averageGrade, passRate);
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
            if (row != selectedModuleRow) {
                row.setStyle("-fx-background-color: #f0f0f0;");
            }
        });

        row.setOnMouseExited(e -> {
            if (row != selectedModuleRow) {
                row.setStyle("-fx-background-color: transparent;");
            }
        });

        row.setOnMouseClicked(e -> {
            LOGGER.debug(text + " selected");

            if (selectedModuleRow != null) {
                selectedModuleRow.setStyle("-fx-background-color: transparent;");
            }
            selectedModuleRow = row;
            row.setStyle("-fx-background-color: #cce0ff;");
            moduleDetailButton.setDisable(false);
        });

        row.getStyleClass().add("sidebar-row");
        row.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(row, Priority.ALWAYS);

        return row;
    }

    /**
     * Creates a row for the student list with the given information.
     *
     * @param id Student ID
     * @param name Student name
     * @param email Student email
     * @param submissionDate Student submission date
     * @return HBox containing the student information
     */
    private HBox createStudentRow(String id, String name, String email, String submissionDate) {
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
        Label submissionDateLabel = new Label(submissionDate);

        HBox.setHgrow(idLabel, Priority.ALWAYS);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        HBox.setHgrow(emailLabel, Priority.ALWAYS);
        HBox.setHgrow(submissionDateLabel, Priority.ALWAYS);

        idLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        emailLabel.setMaxWidth(Double.MAX_VALUE);
        submissionDateLabel.setMaxWidth(Double.MAX_VALUE);

        idLabel.setMinWidth(50);
        nameLabel.setMinWidth(100);
        emailLabel.setMinWidth(150);
        submissionDateLabel.setMinWidth(80);

        idLabel.prefWidthProperty().bind(row.widthProperty().multiply(idWidth / totalWidth));
        nameLabel.prefWidthProperty().bind(row.widthProperty().multiply(nameWidth / totalWidth));
        emailLabel.prefWidthProperty().bind(row.widthProperty().multiply(emailWidth / totalWidth));
        submissionDateLabel.prefWidthProperty().bind(row.widthProperty().multiply(dateWidth / totalWidth));

        idLabel.getStyleClass().add("student-details");
        nameLabel.getStyleClass().add("student-name");
        emailLabel.getStyleClass().add("student-details");
        submissionDateLabel.getStyleClass().add("student-details");

        row.getChildren().addAll(idLabel, nameLabel, emailLabel, submissionDateLabel);

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
     * Gets the currently selected module row
     * @return The selected module row, or null if no module is selected
     */
    public HBox getSelectedModuleRow() {
        return selectedModuleRow;
    }

    /**
     * Clears all selections (modules and students)
     */
    public void clearSelections() {
        clearModuleSelection();
        clearStudentSelection();
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
