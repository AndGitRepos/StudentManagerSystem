package sms.gradle.controller.StudentControllers;

import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.entities.Module;
import sms.gradle.utils.session.Session;
import sms.gradle.view.ViewFactory;
import sms.gradle.view.frames.student.AccessStudentModulesView;

@Getter
public final class AccessStudentModulesController {

    private static final Logger LOGGER = LogManager.getLogger();

    private AccessStudentModulesController() {
        throw new UnsupportedOperationException("All methods in controller class are static");
    }

    /**
     * Displays all modules for selected course database for current student
     *
     * @param view instance of modules view
     */
    public static void loadModules(AccessStudentModulesView view) {

        try {
            int courseId = Session.getInstance().getUser().get().getId();

            CourseDAO.findById(courseId).ifPresent(course -> view.setCourseNameHeader(course.getName()));

            List<Module> modules = ModuleDAO.findByCourseId(courseId);
            displayModules(view, modules);

            LOGGER.debug("Displayed {} modules for course ID: {}", modules.size(), courseId);
        } catch (SQLException e) {
            LOGGER.info("Failed in displaying modules", e);
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No module selected");
            alert.setHeaderText(null);
            alert.setContentText("First select a course from the list");
            alert.showAndWait();
        }
    }

    public static void displayModules(AccessStudentModulesView view, List<Module> modules) {
        view.clearModules();
        int row = 0;
        int column = 0;

        for (Module module : modules) {
            view.updateModuleToGrid(module, row, column);
            column++;
            if (column > 2) {
                column = 0;
                row++;
            }
        }
    }

    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Clicked back button");
        ViewFactory.getInstance().changeToStudentDashboardStage();
    }

    public static void handleSignoutButton(ActionEvent event) {
        LOGGER.debug("Clicked signout button");
        ViewFactory.getInstance().changeToLoginStage();
    }
}
