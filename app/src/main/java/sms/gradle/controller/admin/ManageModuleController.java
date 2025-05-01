package sms.gradle.controller.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.Module;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;

public final class ManageModuleController {
    private static final Logger LOGGER = LogManager.getLogger();

    private ManageModuleController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getManageModulesStage();
    }

    private static void updateModuleDetails(Module selectedModule) {
        final TextField moduleIdField = Common.getNode(getViewStage(), "#moduleIdField");
        final TextField nameField = Common.getNode(getViewStage(), "#nameField");
        final TextField descriptionField = Common.getNode(getViewStage(), "#descriptionField");
        final TextField lecturerField = Common.getNode(getViewStage(), "#lecturerField");

        moduleIdField.setText(String.valueOf(selectedModule.getId()));
        nameField.setText(selectedModule.getName());
        descriptionField.setText(selectedModule.getDescription());
        lecturerField.setText(selectedModule.getLecturer());
    }

    private static void updateLinkedCourseDetails(Course linkedCourse) {
        LOGGER.debug("Updating linked course details");

        final Label linkedCourseNameLabel = Common.getNode(getViewStage(), "#linkedCourseNameLabel");
        final Label linkedCourseIdLabel = Common.getNode(getViewStage(), "#linkedCourseIdLabel");
        final Label linkedCourseDescriptionLabel = Common.getNode(getViewStage(), "#linkedCourseDescriptionLabel");

        linkedCourseNameLabel.setText(linkedCourse.getName());
        linkedCourseIdLabel.setText("(" + linkedCourse.getId() + ")");
        linkedCourseDescriptionLabel.setText(linkedCourse.getDescription());
    }

    private static void updateUnlinkedCoursesListView() {
        LOGGER.debug("Updating unlinked modules list");

        final ListView<Course> unlinkedCoursesListView = Common.getNode(getViewStage(), "#unlinkedCoursesListView");
        unlinkedCoursesListView.getItems().clear();

        try {
            final List<Course> allCourses = CourseDAO.findAll();
            for (Course course : allCourses) {
                // Don't add the currently linked course to the list
                if (course.getId() != getCourseIdFromLinkedCourse()) {
                    unlinkedCoursesListView.getItems().add(course);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update unlinked courses list: ", e);
        }
    }

    private static int getCourseIdFromLinkedCourse() {
        final Label linkedCourseIdLabel = Common.getNode(getViewStage(), "#linkedCourseIdLabel");
        final String labelText = linkedCourseIdLabel.getText();

        if (labelText == null || labelText.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(labelText.substring(1, labelText.length() - 1));
    }

    /**
     * Updates the list of modules with all the modules in the database
     * @param event The action event that triggered this method
     */
    public static void updateListOfModules(ActionEvent event) {
        LOGGER.debug("Updating list of modules");

        final ListView<Module> modulesList = Common.getNode(getViewStage(), "#modulesListView");
        modulesList.getItems().clear();
        try {
            modulesList.getItems().addAll(ModuleDAO.findAll());
        } catch (SQLException e) {
            LOGGER.error("Failed to update list of modules: ", e);
        }
    }

    /**
     * Populates form fields with selected module's information and updates module lists.
     *
     * @param event The action event that triggered this method
     */
    public static void selectModule(ActionEvent event) {
        LOGGER.debug("Selecting module");

        final ListView<Module> modulesListView = Common.getNode(getViewStage(), "#modulesListView");
        final Module selectedModule = modulesListView.getSelectionModel().getSelectedItem();
        Optional<Course> linkedCourse;

        if (selectedModule == null) {
            LOGGER.debug("No module selected. Ignoring.");
            return;
        }

        LOGGER.debug("Selected module: {}", selectedModule);

        try {
            final int linkedCourseId =
                    ModuleDAO.findById(selectedModule.getId()).get().getCourseId();
            linkedCourse = CourseDAO.findById(linkedCourseId);
        } catch (SQLException e) {
            LOGGER.error("Failed to get linked course from currently selected module");
            return;
        }

        if (linkedCourse.isEmpty()) {
            LOGGER.debug("Module does not have a linked course");
            return;
        }

        updateModuleDetails(selectedModule);
        updateLinkedCourseDetails(linkedCourse.get());
        updateUnlinkedCoursesListView();
    }

    /**
     * Creates a new module in the database with the provided information.
     *
     * @param event The action event that triggered this method
     */
    public static void createNewModule(ActionEvent event) {
        LOGGER.debug("Creating new module");

        final TextField nameField = Common.getNode(getViewStage(), "#nameField");
        final TextField descriptionField = Common.getNode(getViewStage(), "#descriptionField");
        final TextField lecturerField = Common.getNode(getViewStage(), "#lecturerField");

        List<Course> courses;
        try {
            courses = CourseDAO.findAll();
        } catch (SQLException e) {
            LOGGER.error("Failed to get list of courses: ", e);
            return;
        }

        if (courses.isEmpty()) {
            // TODO: Display user error
            LOGGER.info("No courses found. Cannot create assessment");
            return;
        }

        final Module newModule =
                new Module(0, nameField.getText(), descriptionField.getText(), lecturerField.getText(), 1);

        try {
            ModuleDAO.addModule(newModule);
            LOGGER.debug("New module created: {}", newModule);
        } catch (SQLException e) {
            LOGGER.error("Failed to create new module: ", e);
        }
    }

    /**
     * Deletes the selected module from the database.
     *
     * @param event The action event that triggered this method
     */
    public static void deleteModule(ActionEvent event) {
        LOGGER.debug("Deleting module");
        final ListView<Module> modulesListView = Common.getNode(getViewStage(), "#modulesListView");
        final Module selectedModule = modulesListView.getSelectionModel().getSelectedItem();

        if (selectedModule == null) {
            LOGGER.debug("No module selected. Ignoring.");
            return;
        }

        LOGGER.debug("Deleting module: {}", selectedModule);
        try {
            ModuleDAO.delete(selectedModule.getId());
            modulesListView.getItems().remove(selectedModule);
        } catch (SQLException e) {
            LOGGER.error("Failed to delete module: ", e);
        }
    }

    public static void swapLinkedCourse(ActionEvent event) {
        LOGGER.debug("Swapping linked module");
        final ListView<Course> unlinkedCoursesListView = Common.getNode(getViewStage(), "#unlinkedCoursesListView");
        final Course selectedCourse =
                unlinkedCoursesListView.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            LOGGER.debug("No module selected. Ignoring.");
            return;
        }

        LOGGER.debug("Selected course: {}", selectedCourse);
        updateLinkedCourseDetails(selectedCourse);
        updateUnlinkedCoursesListView();
    }

    public static void updateModule(ActionEvent event) {
        LOGGER.debug("Updating module");

        final TextField moduleIdField = Common.getNode(getViewStage(), "#moduleIdField");
        final TextField nameField = Common.getNode(getViewStage(), "#nameField");
        final TextField descriptionField = Common.getNode(getViewStage(), "#descriptionField");
        final TextField lecturerField = Common.getNode(getViewStage(), "#lecturerField");

        final int courseIdFromLinkedCourse = getCourseIdFromLinkedCourse();

        if (courseIdFromLinkedCourse == 0) {
            LOGGER.debug("No module selected. Ignoring.");
            // TODO: Show an error alert to the user
            return;
        }

        final Module updatedModule = new Module(
                Integer.parseInt(moduleIdField.getText()),
                nameField.getText(),
                descriptionField.getText(),
                lecturerField.getText(),
                courseIdFromLinkedCourse);

        try {
            ModuleDAO.update(updatedModule);
            updateListOfModules(null);
            LOGGER.debug("Module updated: {}", updatedModule);
        } catch (SQLException e) {
            LOGGER.error("Failed to update module: ", e);
        }
    }

    public static void handleOnShowEvent(WindowEvent event) {
        updateListOfModules(new ActionEvent());
    }

    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Handling back button");
        ViewFactory.getInstance().getManageModulesStage().hide();
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }
}
