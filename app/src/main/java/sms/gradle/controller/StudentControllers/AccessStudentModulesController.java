package sms.gradle.controller.StudentControllers;

import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.Module;
import sms.gradle.utils.Common;
import sms.gradle.utils.session.Session;
import sms.gradle.view.ViewFactory;

@Getter
public final class AccessStudentModulesController {

    private static final Logger LOGGER = LogManager.getLogger();

    private AccessStudentModulesController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getStudentModulesStage();
    }

    /**
     * Creates a visual card representation of a module using JavaFX components.
     * The card contains the module's name, ID, lecturer, and description in a styled VBox layout.
     *
     * @param module The Module object containing the data to be displayed
     * @return VBox containing the formatted module information as a card
     */
    private static VBox createModuleCard(final Module module) {
        final VBox card = new VBox(10);
        card.getStyleClass().add("module-card");
        card.setPadding(new Insets(15));

        final Label moduleNameLabel = new Label(module.getName());
        moduleNameLabel.getStyleClass().add("module-name");

        final Label moduleIdLabel = new Label("ID: " + module.getId());
        final Label moduleLecturerLabel = new Label("Lecturer: " + module.getLecturer());
        final Label moduleDescriptionLabel = new Label("Description: " + module.getDescription());
        moduleDescriptionLabel.setWrapText(true);

        card.getChildren().addAll(moduleNameLabel, moduleIdLabel, moduleLecturerLabel, moduleDescriptionLabel);

        return card;
    }

    /**
     * Adds a module to the grid pane at the specified row and column.
     * The module card is created using the createModuleCard method.
     *
     * @param modulesGrid The GridPane where the module card will be added
     * @param module The Module object containing the data to be displayed
     * @param row The row index where the module card will be added
     * @param column The column index where the module card will be added
     */
    private static void updateModuleToGrid(
            final GridPane modulesGrid, final Module module, final int row, final int column) {
        final VBox moduleCard = createModuleCard(module);
        modulesGrid.add(moduleCard, column, row);
        LOGGER.debug("Added module {} to grid, positioned: ({}, {})", module.getName(), row, column);
    }

    /**
     * Displays all modules in the grid pane
     *
     * @param modules list of modules to be displayed
     * @param modulesGrid grid pane where modules will be displayed
     */
    private static void displayModules(final List<Module> modules, final GridPane modulesGrid) {
        modulesGrid.getChildren().clear();
        int row = 0;
        int column = 0;

        for (Module module : modules) {
            updateModuleToGrid(modulesGrid, module, row, column);
            row += column / 2; // incrementing row every column reset
            column = (column + 1) % 3; // incrementing column up to 2 then resetting to 0
        }
    }

    /**
     * Displays all modules for selected course database for current student
     *
     * @param view instance of modules view
     */
    public static void handleRefreshModulesButton(ActionEvent event) {
        LOGGER.debug("Refresh Button Clicked");
        try {
            final GridPane modulesGrid = Common.getNode(getViewStage(), "#modulesGrid");
            final Label courseHeader = Common.getNode(getViewStage(), "#courseNameHeading");
            final int courseId = Session.getInstance().getSelectedCourseId();

            CourseDAO.findById(courseId)
                    .map(Course::getName)
                    .ifPresent(name -> courseHeader.setText(name + " Modules"));

            final List<Module> modules = ModuleDAO.findByCourseId(courseId);
            displayModules(modules, modulesGrid);

            LOGGER.debug("Refreshed {} modules for course ID: {}", modules.size(), courseId);
        } catch (SQLException e) {
            LOGGER.info("Failed in refreshing modules", e);
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Loading Modules Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed in loading modules. Try again");
            alert.showAndWait();
        }
    }

    /**
     * Handles the event when the student modules stage is shown.
     * Refreshes the modules display when the stage is shown.
     *
     * @param event The WindowEvent triggered when the stage is shown
     */
    public static void handleOnShowEvent(WindowEvent event) {
        handleRefreshModulesButton(new ActionEvent());
    }

    /**
     * Handles the event when the back button is clicked.
     * Hides the current stage and changes to the student dashboard stage.
     *
     * @param event The ActionEvent triggered by clicking the back button
     */
    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Clicked back button");
        ViewFactory.getInstance().getStudentModulesStage().hide();
        ViewFactory.getInstance().changeToStudentDashboardStage();
    }

    /**
     * Handles the event when the signout button is clicked.
     * Hides the current stage and changes to the login stage.
     *
     * @param event The ActionEvent triggered by clicking the signout button
     */
    public static void handleSignoutButton(ActionEvent event) {
        LOGGER.debug("Clicked signout button");
        ViewFactory.getInstance().getStudentModulesStage().hide();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
