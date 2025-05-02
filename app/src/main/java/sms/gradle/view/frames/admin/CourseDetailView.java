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
import sms.gradle.controller.admin.CourseDetailViewController;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Student;
import sms.gradle.view.CoreViewInterface;

/**
 * Course View that displays detailed information about a course and its students.
 * This view is shown when the "View Course" button is clicked in the StaffDashboardView.
 */
public final class CourseDetailView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final double SIDEBAR_WIDTH_PERCENT = 0.25;
    private static final double STATS_PANEL_HEIGHT_PERCENT = 0.15;

    // Sidebar components
    private VBox sidebar = new VBox(15);
    private Label courseNameLabel = new Label("Course Name");
    private Label courseDescriptionLabel = new Label("Select a course to view details");
    private Button moduleDetailButton = new Button("View Module");
    private Button assessmentDetailButton = new Button("View Assessment");
    private Button backButton = new Button("Back");
    private Button logoutButton = new Button("Logout");
    private VBox moduleListContainer = new VBox(5);
    private ScrollPane moduleScrollPane = new ScrollPane(moduleListContainer);
    private HBox selectedModuleRow = null;
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

    // Current course ID
    private int currentCourseId = -1;

    public CourseDetailView() {
        LOGGER.debug("Initialising Course View");
        getStylesheets().add(getClass().getResource("/styles/manager.css").toExternalForm());
        initialiseCoreUIComponents();
        layoutCoreUIComponents();
        styleCoreUIComponents();
        assignButtonActions();
        // Module and assessment data will be loaded when a course is selected

        setupResponsiveBehavior();

        LOGGER.debug("Course View initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        // Main information panel components
        viewStudentButton.setDisable(true);
        moduleDetailButton.setDisable(true);
        assessmentDetailButton.setDisable(true);

        studentScrollPane.setFitToWidth(true);

        headerRow = createStudentRow("ID", "Name", "Email", "Join Date");
        headerRow.getStyleClass().add("student-header-row");

        moduleScrollPane.setFitToWidth(true);

        assessmentScrollPane.setFitToWidth(true);
    }

    @Override
    public void layoutCoreUIComponents() {
        sidebar.getChildren()
                .addAll(
                        courseNameLabel,
                        courseDescriptionLabel,
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
        getStyleClass().add("manage-course-view");

        // Sidebar
        sidebar.getStyleClass().add("left-panel");
        courseNameLabel.getStyleClass().add("section-header");
        courseNameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        courseDescriptionLabel.getStyleClass().add("course-description-label");
        courseDescriptionLabel.setWrapText(true);
        courseDescriptionLabel.setMaxWidth(Double.MAX_VALUE);
        courseDescriptionLabel.setPrefWidth(200);
        moduleDetailButton.getStyleClass().add("action-button");
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

            studentScrollPane.setPrefHeight(availableHeight * 0.8);
        });
    }

    /**
     * Sets the current course ID and updates the course information and student list
     * @param courseId The ID of the course to display
     */
    public void setCourseId(int courseId) {
        this.currentCourseId = courseId;
        updateCourseInformation();
        updateStudentList();
        updateModuleList();
        updateAssessmentList();
    }

    /**
     * Updates the course name and description labels with data from the controller
     */
    private void updateCourseInformation() {
        if (currentCourseId <= 0) {
            LOGGER.warn("No course ID set, cannot load course information");
            courseNameLabel.setText("Course Name");
            courseDescriptionLabel.setText("Select a course to view details");
            return;
        }

        String[] courseInfo = CourseDetailViewController.loadCourseInformation(currentCourseId);

        if (courseInfo != null) {
            courseNameLabel.setText(courseInfo[0]);
            // Check if description is empty or null and provide a default message
            String description = courseInfo[1];
            if (description == null || description.trim().isEmpty()) {
                description = "No description available for this course.";
            }
            courseDescriptionLabel.setText(description);
            LOGGER.debug("Updated course information for course ID: {}", currentCourseId);
        } else {
            LOGGER.warn("Course not found for ID: {}", currentCourseId);
            courseNameLabel.setText("Course Not Found");
            courseDescriptionLabel.setText("No description available");
        }
    }

    private void assignButtonActions() {
        moduleDetailButton.setOnAction(CourseDetailViewController::handleViewModuleDetailButton);
        assessmentDetailButton.setOnAction(CourseDetailViewController::handleViewAssessmentDetailButton);
        backButton.setOnAction(CourseDetailViewController::handleBackButton);
        logoutButton.setOnAction(CourseDetailViewController::handleLogoutButton);
        viewStudentButton.setOnAction(CourseDetailViewController::handleViewStudentDetailButton);
    }

    /**
     * Updates the student list with students enrolled in the current course
     */
    private void updateStudentList() {
        clearStudentSelection();
        studentListContainer.getChildren().clear();

        if (currentCourseId <= 0) {
            LOGGER.warn("No course ID set, cannot load students");
            return;
        }

        List<Student> students = CourseDetailViewController.loadStudentsForCourse(currentCourseId);
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
        updateCourseStatistics();
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
     * Clears all selections (modules, assessments, students)
     */
    public void clearSelections() {
        if (selectedModuleRow != null) {
            selectedModuleRow.setStyle("-fx-background-color: transparent;");
            selectedModuleRow = null;
            moduleDetailButton.setDisable(true);
        }
        if (selectedAssessmentRow != null) {
            selectedAssessmentRow.setStyle("-fx-background-color: transparent;");
            selectedAssessmentRow = null;
            assessmentDetailButton.setDisable(true);
        }
        clearStudentSelection();
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
     * Gets the currently selected assessment row
     * @return The selected assessment row, or null if no assessment is selected
     */
    public HBox getSelectedAssessmentRow() {
        return selectedAssessmentRow;
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

    /**
     * Updates the course statistics panel with data from the controller
     */
    private void updateCourseStatistics() {
        if (currentCourseId <= 0) {
            LOGGER.warn("No course ID set, cannot update statistics");
            totalStudentsLabel.setText("Total Students: 0");
            averageGradeLabel.setText("Average Grade: 0.0");
            passRateLabel.setText("Pass Rate: 0%");
            return;
        }

        double[] stats = CourseDetailViewController.calculateCourseStatistics(currentCourseId);
        int totalStudents = (int) stats[0];
        double averageGrade = stats[1];
        double passRate = stats[2];

        totalStudentsLabel.setText("Total Students: " + totalStudents);
        averageGradeLabel.setText("Average Grade: " + String.format("%.1f", averageGrade));
        passRateLabel.setText("Pass Rate: " + String.format("%.0f%%", passRate));

        LOGGER.debug(
                "Updated statistics for course ID {}: {} students, {} avg grade, {}% pass rate",
                currentCourseId, totalStudents, averageGrade, passRate);
    }

    /**
     * Updates the module list with modules from the current course
     */
    public void updateModuleList() {
        moduleListContainer.getChildren().clear();

        if (currentCourseId <= 0) {
            LOGGER.warn("No course ID set, cannot load modules");
            return;
        }

        List<sms.gradle.model.entities.Module> modules =
                CourseDetailViewController.loadModulesForCourse(currentCourseId);

        if (modules.isEmpty()) {
            HBox noModulesRow = createSidebarRow("No modules found");
            moduleListContainer.getChildren().add(noModulesRow);
            return;
        }

        for (sms.gradle.model.entities.Module module : modules) {
            HBox moduleRow = createSidebarRow(module.getName());
            moduleRow.setUserData(module.getId());
            moduleListContainer.getChildren().add(moduleRow);
        }

        LOGGER.debug("Updated module list with {} modules", modules.size());
    }

    /**
     * Updates the assessment list with assessments from the current course's modules
     */
    public void updateAssessmentList() {
        assessmentListContainer.getChildren().clear();

        if (currentCourseId <= 0) {
            LOGGER.warn("No course ID set, cannot load assessments");
            return;
        }

        List<Assessment> assessments = CourseDetailViewController.loadAssessmentsForCourse(currentCourseId);

        if (assessments.isEmpty()) {
            HBox noAssessmentsRow = createSidebarRow("No assessments found");
            assessmentListContainer.getChildren().add(noAssessmentsRow);
            return;
        }

        for (Assessment assessment : assessments) {
            HBox assessmentRow = createSidebarRow(assessment.getName());
            assessmentRow.setUserData(assessment.getId());
            assessmentListContainer.getChildren().add(assessmentRow);
        }

        LOGGER.debug("Updated assessment list with {} assessments", assessments.size());
    }
}
