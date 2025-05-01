package sms.gradle.controller.admin;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AssessmentDAO;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Module;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;

public class ManageAssessmentsController {
    private static final Logger LOGGER = LogManager.getLogger();

    private ManageAssessmentsController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getManageAssessmentsStage();
    }

    private static void updateAssessmentDetails(Assessment selectedAssessment) {
        final TextField assessmentIdField = Common.getNode(getViewStage(), "#assessmentIdField");
        final TextField nameField = Common.getNode(getViewStage(), "#nameField");
        final TextField descriptionField = Common.getNode(getViewStage(), "#descriptionField");
        final DatePicker dueDatePicker = Common.getNode(getViewStage(), "#dueDatePicker");

        assessmentIdField.setText(String.valueOf(selectedAssessment.getId()));
        nameField.setText(selectedAssessment.getName());
        descriptionField.setText(selectedAssessment.getDescription());
        dueDatePicker.setValue(selectedAssessment.getDueDate().toLocalDate());
    }

    private static void updateLinkedModuleDetails(Module linkedModule) {
        LOGGER.debug("Updating linked module");
        try {
            final Label linkedModuleNameLabel = Common.getNode(getViewStage(), "#linkedModuleNameLabel");
            final Label linkedModuleIdLabel = Common.getNode(getViewStage(), "#linkedModuleIdLabel");
            final Label linkedModuleDescriptionLabel = Common.getNode(getViewStage(), "#linkedModuleDescriptionLabel");
            final Label linkedModuleLecturerLabel = Common.getNode(getViewStage(), "#linkedModuleLecturerLabel");
            final Label linkedModuleCourseNameLabel = Common.getNode(getViewStage(), "#linkedModuleCourseNameLabel");

            linkedModuleNameLabel.setText(linkedModule.getName());
            linkedModuleIdLabel.setText("(" + linkedModule.getId() + ")");
            linkedModuleDescriptionLabel.setText(linkedModule.getDescription());
            linkedModuleLecturerLabel.setText(linkedModule.getLecturer());
            linkedModuleCourseNameLabel.setText(
                    CourseDAO.findById(linkedModule.getCourseId()).get().getName());
        } catch (SQLException e) {
            LOGGER.error("Failed to update linked modules list: ", e);
        }
    }

    private static void updateUnlinkedModulesListView() {
        LOGGER.debug("Updating unlinked modules list");

        final ListView<Module> unlinkedModulesListView = Common.getNode(getViewStage(), "#unlinkedModulesListView");
        unlinkedModulesListView.getItems().clear();

        try {
            List<Module> allModules = ModuleDAO.findAll();
            for (Module module : allModules) {
                // Don't add the currently linked module to the list
                if (module.getId() != getModuleIdFromLinkedModule()) {
                    unlinkedModulesListView.getItems().add(module);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update unlinked modules list: ", e);
        }
    }

    private static int getModuleIdFromLinkedModule() {
        final Label linkedModuleIdLabel = Common.getNode(getViewStage(), "#linkedModuleIdLabel");
        final String labelText = linkedModuleIdLabel.getText();

        if (labelText == null || labelText.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(labelText.substring(1, labelText.length() - 1));
    }

    /**
     * Updates the list of assessments with all the assessments in the database
     * @param event The action event that triggered this method
     */
    public static void updateListOfAssessments(ActionEvent event) {
        LOGGER.debug("Updating list of assessments");

        final ListView<Assessment> assessmentsList = Common.getNode(getViewStage(), "#assessmentsListView");
        assessmentsList.getItems().clear();
        try {
            AssessmentDAO.findAll()
                    .forEach(assessment -> assessmentsList.getItems().add(assessment));
        } catch (SQLException e) {
            LOGGER.error("Failed to update list of students: ", e);
        }
    }

    /**
     * Populates form fields with selected assessment's information and updates module lists.
     *
     * @param event The action event that triggered this method
     */
    public static void selectAssessment(ActionEvent event) {
        LOGGER.debug("Selecting assessment");

        final ListView<Assessment> assessmentsListView = Common.getNode(getViewStage(), "#assessmentsListView");
        final Assessment selectedAssessment =
                assessmentsListView.getSelectionModel().getSelectedItem();
        Module linkedModule;

        if (selectedAssessment == null) {
            LOGGER.debug("No assessment selected. Ignoring.");
            return;
        }

        LOGGER.debug("Selected assessment: {}", selectedAssessment);

        try {
            linkedModule = ModuleDAO.findById(selectedAssessment.getModuleId()).get();
        } catch (SQLException e) {
            LOGGER.error("Failed to get linked module from currently selected assessment");
            return;
        }

        updateAssessmentDetails(selectedAssessment);
        updateLinkedModuleDetails(linkedModule);
        updateUnlinkedModulesListView();
    }

    /**
     * Creates a new assessment in the database with the provided information.
     *
     * @param event The action event that triggered this method
     */
    public static void createNewAssessment(ActionEvent event) {
        LOGGER.debug("Creating new assessment");

        final TextField nameField = Common.getNode(getViewStage(), "#nameField");
        final TextField descriptionField = Common.getNode(getViewStage(), "#descriptionField");
        final DatePicker dueDatePicker = Common.getNode(getViewStage(), "#dueDatePicker");

        List<Module> modules;
        try {
            modules = ModuleDAO.findAll();
        } catch (SQLException e) {
            LOGGER.error("Failed to get list of modules: ", e);
            return;
        }

        if (modules.isEmpty()) {
            // TODO: Display user error
            LOGGER.info("No modules found. Cannot create assessment");
            return;
        }

        final Assessment newAssessment = new Assessment(
                0,
                nameField.getText(),
                descriptionField.getText(),
                Date.valueOf(dueDatePicker.getValue()),
                getModuleIdFromLinkedModule());

        try {
            AssessmentDAO.addAssessment(newAssessment);
            LOGGER.debug("New assessment created: {}", newAssessment);
        } catch (SQLException e) {
            LOGGER.error("Failed to create new assessment: ", e);
        }
    }

    /**
     * Deletes the selected assessment from the database.
     *
     * @param event The action event that triggered this method
     */
    public static void deleteAssessment(ActionEvent event) {
        LOGGER.debug("Deleting assessment");
        final ListView<Assessment> assessmentsListView = Common.getNode(getViewStage(), "#assessmentsListView");
        final Assessment selectedAssessment =
                assessmentsListView.getSelectionModel().getSelectedItem();

        if (selectedAssessment == null) {
            LOGGER.debug("No assessment selected. Ignoring.");
            return;
        }

        LOGGER.debug("Deleting assessment: {}", selectedAssessment);
        try {
            AssessmentDAO.delete(selectedAssessment.getId());
            assessmentsListView.getItems().remove(selectedAssessment);
        } catch (SQLException e) {
            LOGGER.error("Failed to delete assessment: ", e);
        }
    }

    public static void swapLinkedModule(ActionEvent event) {
        LOGGER.debug("Swapping linked module");
        ListView<Module> unlinkedModulesListView = Common.getNode(getViewStage(), "#unlinkedModulesListView");
        final Module selectedModule =
                unlinkedModulesListView.getSelectionModel().getSelectedItem();

        if (selectedModule == null) {
            LOGGER.debug("No module selected. Ignoring.");
            return;
        }

        LOGGER.debug("Selected module: {}", selectedModule);
        updateLinkedModuleDetails(selectedModule);
        updateUnlinkedModulesListView();
    }

    public static void updateAssessment(ActionEvent event) {
        LOGGER.debug("Updating assessment");

        final TextField assessmentIdField = Common.getNode(getViewStage(), "#assessmentIdField");
        final TextField nameField = Common.getNode(getViewStage(), "#nameField");
        final TextField descriptionField = Common.getNode(getViewStage(), "#descriptionField");
        final DatePicker dueDatePicker = Common.getNode(getViewStage(), "#dueDatePicker");

        final int moduleIdFromLinkedModule = getModuleIdFromLinkedModule();

        if (moduleIdFromLinkedModule == 0) {
            LOGGER.debug("No module selected. Ignoring.");
            // TODO: Show an error alert to the user
            return;
        }

        final Assessment updatedAssessment = new Assessment(
                Integer.parseInt(assessmentIdField.getText()),
                nameField.getText(),
                descriptionField.getText(),
                Date.valueOf(dueDatePicker.getValue()),
                moduleIdFromLinkedModule);

        try {
            AssessmentDAO.update(updatedAssessment);
            updateListOfAssessments(null);
            LOGGER.debug("Assessment updated: {}", updatedAssessment);
        } catch (SQLException e) {
            LOGGER.error("Failed to update assessment: ", e);
        }
    }

    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Handling back button");
        ViewFactory.getInstance().getManageAssessmentsStage().hide();
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }
}
