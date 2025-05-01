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

    private static void updateModuleToGrid(
            final GridPane modulesGrid, final Module module, final int row, final int column) {
        final VBox moduleCard = createModuleCard(module);
        modulesGrid.add(moduleCard, column, row);
        LOGGER.debug("Added module {} to grid, positioned: ({}, {})", module.getName(), row, column);
    }

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

    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Clicked back button");
        ViewFactory.getInstance().getStudentModulesStage().hide();
        ViewFactory.getInstance().changeToStudentDashboardStage();
    }

    public static void handleSignoutButton(ActionEvent event) {
        LOGGER.debug("Clicked signout button");
        ViewFactory.getInstance().getStudentModulesStage().hide();
        ViewFactory.getInstance().changeToLoginStage();
    }
}
