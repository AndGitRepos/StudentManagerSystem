package sms.gradle.view.frames.admin;

import java.sql.SQLException;
import java.util.ArrayList;
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
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.controller.admin.CourseDetailViewController;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.CourseEnrollmentDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.CourseEnrollment;
import sms.gradle.model.entities.Student;
import sms.gradle.view.CoreViewInterface;

/**
 * Course View that displays detailed information about a course and its students.
 * This view is shown when the "View Course" button is clicked in the StaffDashboardView.
 */
@Getter
public final class CourseDetailView extends BorderPane implements CoreViewInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final double SIDEBAR_WIDTH_PERCENT = 0.25;
    private static final double STATS_PANEL_HEIGHT_PERCENT = 0.15;

    // Sidebar components
    private VBox sidebar;
    private Label courseNameLabel;
    private Label courseDescriptionLabel;
    private Button moduleDetailButton;
    private Button assessmentDetailButton;
    private Button backButton;
    private Button logoutButton;
    private ScrollPane moduleScrollPane;
    private VBox moduleListContainer;
    private HBox selectedModuleRow = null;
    private ScrollPane assessmentScrollPane;
    private VBox assessmentListContainer;
    private HBox selectedAssessmentRow = null;

    // Stats panel components
    private HBox statsPanel;
    private Label totalStudentsLabel;
    private Label averageGradeLabel;
    private Label passRateLabel;

    // Main information panel components
    private VBox mainInfoPanel;
    private ScrollPane studentScrollPane;
    private VBox studentListContainer;
    private HBox selectedStudentRow = null;
    private Button viewStudentButton;
    private VBox studentSection;
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
        addSampleStudentData(); // TODO: remove when properly implemented
        addSampleScrollPaneItems(); // TODO: remove when properly implemented

        setupResponsiveBehavior();

        LOGGER.debug("Course View initialised");
    }

    @Override
    public void initialiseCoreUIComponents() {
        // Sidebar components
        sidebar = new VBox(15);
        courseNameLabel = new Label("Course Name");
        courseDescriptionLabel = new Label("Course Description: This is a placeholder for the course description.");
        moduleDetailButton = new Button("View Module");
        assessmentDetailButton = new Button("View Assessment");
        backButton = new Button("Back");
        logoutButton = new Button("Logout");

        // Stats panel components
        statsPanel = new HBox(30);
        totalStudentsLabel = new Label("Total Students: 0");
        averageGradeLabel = new Label("Average Grade: 0.0");
        passRateLabel = new Label("Pass Rate: 0%");

        // Main information panel components
        mainInfoPanel = new VBox(10);
        studentListContainer = new VBox(5);
        studentScrollPane = new ScrollPane(studentListContainer);
        viewStudentButton = new Button("View Student Details");
        viewStudentButton.setDisable(true);
        studentScrollPane.setFitToWidth(true);
        studentSection = new VBox(0);
        headerRow = createStudentRow("ID", "Name", "Email", "Join Date");
        headerRow.getStyleClass().add("student-header-row");

        // Module scroll components
        moduleListContainer = new VBox(5);
        moduleScrollPane = new ScrollPane(moduleListContainer);
        moduleScrollPane.setFitToWidth(true);

        // Assessment scroll components
        assessmentListContainer = new VBox(5);
        assessmentScrollPane = new ScrollPane(assessmentListContainer);
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
     * Sets the current course ID and updates the course information and student list
     * @param courseId The ID of the course to display
     */
    public void setCourseId(int courseId) {
        this.currentCourseId = courseId;
        updateCourseInformation();
        updateStudentList();
    }

    /**
     * Updates the course name and description labels with data from CourseDAO
     */
    private void updateCourseInformation() {
        if (currentCourseId <= 0) {
            LOGGER.warn("No course ID set, cannot load course information");
            return;
        }

        try {
            CourseDAO.findById(currentCourseId)
                    .ifPresentOrElse(
                            course -> {
                                courseNameLabel.setText(course.getName());
                                courseDescriptionLabel.setText(course.getDescription());
                                LOGGER.debug("Updated course information for course ID: {}", currentCourseId);
                            },
                            () -> {
                                LOGGER.warn("Course not found for ID: {}", currentCourseId);
                                courseNameLabel.setText("Course Not Found");
                                courseDescriptionLabel.setText("No description available");
                            });
        } catch (SQLException e) {
            LOGGER.error("Error loading course information for course ID: {}", currentCourseId, e);
            courseNameLabel.setText("Error Loading Course");
            courseDescriptionLabel.setText("Could not load course description");
        }
    }

    private void assignButtonActions() {
        moduleDetailButton.setOnAction(CourseDetailViewController::handleViewModuleDetailButton);
        assessmentDetailButton.setOnAction(CourseDetailViewController::handleViewAssessmentDetailButton);
        backButton.setOnAction(CourseDetailViewController::handleBackButton);
        logoutButton.setOnAction(CourseDetailViewController::handleLogoutButton);
        String selectedId = getSelectedStudentId();
        if (selectedId != null) {
            LOGGER.debug("View Student button clicked for student: " + selectedId);
        }
    }

    /**
     * Updates the student list with students enrolled in the current course
     */
    private void updateStudentList() {
        clearStudentSelection();
        studentListContainer.getChildren().clear();

        if (currentCourseId <= 0) {
            LOGGER.warn("No course ID set, cannot load students");
            addSampleStudentData();
            return;
        }

        try {
            List<CourseEnrollment> enrollments = CourseEnrollmentDAO.findByCourseId(currentCourseId);
            List<Student> students = new ArrayList<>();

            for (CourseEnrollment enrollment : enrollments) {
                StudentDAO.findById(enrollment.getStudentId()).ifPresent(students::add);
            }

            totalStudentsLabel.setText("Total Students: " + students.size());

            for (Student student : students) {
                String fullName = student.getFirstName() + " " + student.getLastName();
                String joinDate =
                        student.getJoinDate() != null ? student.getJoinDate().toString() : "N/A";

                studentListContainer
                        .getChildren()
                        .add(createStudentRow(String.valueOf(student.getId()), fullName, student.getEmail(), joinDate));
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading students for course ID: " + currentCourseId, e);
        }
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
            } else {
                if (selectedAssessmentRow != null) {
                    selectedAssessmentRow.setStyle("-fx-background-color: transparent;");
                }
                selectedAssessmentRow = row;
                row.setStyle("-fx-background-color: #cce0ff;");
            }
        });

        row.getStyleClass().add("sidebar-row");
        row.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(row, Priority.ALWAYS);

        return row;
    }

    public void clearSelections() {
        if (selectedModuleRow != null) {
            selectedModuleRow.setStyle("-fx-background-color: transparent;");
            selectedModuleRow = null;
        }
        if (selectedAssessmentRow != null) {
            selectedAssessmentRow.setStyle("-fx-background-color: transparent;");
            selectedAssessmentRow = null;
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
     * Gets the ID of the currently selected student
     * @return The ID of the selected student, or null if no student is selected
     */
    private String getSelectedStudentId() {
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

    /**
     * Adds sample student data to the student list container.
     * This is used only when no course ID is set.
     * TODO: remove when properly implemented
     */
    private void addSampleStudentData() {
        studentListContainer
                .getChildren()
                .addAll(
                        createStudentRow("S12345", "John Doe", "john.doe@example.com", "2023-01-15"),
                        createStudentRow("S12346", "Jane Smith", "jane.smith@example.com", "2023-02-20"),
                        createStudentRow("S12347", "Bob Johnson", "bob.johnson@example.com", "2023-01-10"),
                        createStudentRow("S12348", "Alice Brown", "alice.brown@example.com", "2023-03-05"),
                        createStudentRow("S12346", "Jane Smith", "jane.smith@example.com", "2023-02-20"),
                        createStudentRow("S12347", "Bob Johnson", "bob.johnson@example.com", "2023-01-10"),
                        createStudentRow("S12348", "Alice Brown", "alice.brown@example.com", "2023-03-05"),
                        createStudentRow("S12346", "Jane Smith", "jane.smith@example.com", "2023-02-20"),
                        createStudentRow("S12347", "Bob Johnson", "bob.johnson@example.com", "2023-01-10"),
                        createStudentRow("S12348", "Alice Brown", "alice.brown@example.com", "2023-03-05"),
                        createStudentRow("S12346", "Jane Smith", "jane.smith@example.com", "2023-02-20"),
                        createStudentRow("S12347", "Bob Johnson", "bob.johnson@example.com", "2023-01-10"),
                        createStudentRow("S12348", "Alice Brown", "alice.brown@example.com", "2023-03-05"),
                        createStudentRow("S12349", "Charlie Davis", "charlie.davis@example.com", "2023-02-28"));
    }

    /**
     * Adds sample items to the assessment and module list containers.
     * This is used only when no course ID is set.
     * TODO: remove when properly implemented
     */
    private void addSampleScrollPaneItems() {
        for (int i = 1; i <= 5; i++) {
            HBox moduleRow = createSidebarRow("Module " + i);
            moduleListContainer.getChildren().add(moduleRow);
        }

        for (int i = 1; i <= 5; i++) {
            HBox assessmentRow = createSidebarRow("Assessment " + i);
            assessmentListContainer.getChildren().add(assessmentRow);
        }
    }
}
