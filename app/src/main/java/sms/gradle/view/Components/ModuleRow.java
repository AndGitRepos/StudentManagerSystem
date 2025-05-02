package sms.gradle.view.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Displays student module details - name, lecturer, room, and grade
 */
public class ModuleRow extends HBox {

    private final GradeCircle gradeCircle;

    /**
     * Creates a new module row
     *
     * @param moduleName The name of the module
     * @param moduleLecturer The lecturer of the module
     * @param moduleRoomNumber The room number of the module
     * @param moduleGrade The grade of the module
     */
    public ModuleRow(
            final String moduleName,
            final String moduleLecturer,
            final String moduleRoomNumber,
            final int moduleGrade) {

        getStylesheets().add(getClass().getResource("/styles/components.css").toExternalForm());

        setUpModuleRowLayout();

        gradeCircle = new GradeCircle(moduleGrade);

        VBox moduleDetailsContainer = createModuleDetailsContainer(moduleName, moduleLecturer, moduleRoomNumber);

        getChildren().addAll(moduleDetailsContainer, gradeCircle);

        setUpToolTips(moduleName, moduleLecturer, moduleRoomNumber);
    }

    /**
     * Sets up the layout properties for the module row.
     * Configures spacing between elements and padding around the row.
     */
    private void setUpModuleRowLayout() {
        setSpacing(20);
        setPadding(new Insets(10));
    }

    /**
     * Creates a container for module details (name, lecturer, room number).
     *
     * @param moduleName The name of the module
     * @param moduleLecturer The lecturer of the module
     * @param moduleRoomNumber The room number of the module
     * @return A VBox containing the module details
     */
    private VBox createModuleDetailsContainer(
            final String moduleName, final String moduleLecturer, final String moduleRoomNumber) {

        final Label moduleNameLabel = new Label("Module Name, ID: " + moduleName);
        final Label moduleLecturerLabel = new Label("Lecturer: " + moduleLecturer);
        final Label moduleRoomNumberLabel = new Label("Room No: " + moduleRoomNumber);

        VBox container = new VBox(5);
        container.getChildren().addAll(moduleLecturerLabel, moduleNameLabel, moduleRoomNumberLabel);
        return container;
    }

    /**
     * Sets up a tooltip for the module row displaying module details.
     *
     * @param moduleName The name of the module
     * @param moduleLecturer The lecturer of the module
     * @param moduleRoomNumber The room number of the module
     */
    private void setUpToolTips(final String moduleName, final String moduleLecturer, final String moduleRoomNumber) {
        Tooltip toolTip = (new Tooltip(
                "Module: " + moduleName + "\nLecturer: " + moduleLecturer + "\nRoom No: " + moduleRoomNumber));
        Tooltip.install(this, toolTip);
    }
}
