package sms.gradle.view.Components;

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

    public ModuleRow(
            final String moduleName,
            final String moduleLecturer,
            final String moduleRoomNumber,
            final int moduleGrade) {

        setUpModuleRowLayout();

        gradeCircle = new GradeCircle(moduleGrade);

        VBox moduleDetailsContainer = createModuleDetailsContainer(moduleName, moduleLecturer, moduleRoomNumber);

        getChildren().addAll(moduleDetailsContainer, gradeCircle);

        setUpToolTips(moduleName, moduleLecturer, moduleRoomNumber);
    }

    private void setUpModuleRowLayout() {
        setSpacing(20);
        setPadding(new Insets(10));
    }

    private VBox createModuleDetailsContainer(
            final String moduleName, final String moduleLecturer, final String moduleRoomNumber) {

        final Label moduleNameLabel = new Label("Module Name, ID: " + moduleName);
        final Label moduleLecturerLabel = new Label("Lecturer: " + moduleLecturer);
        final Label moduleRoomNumberLabel = new Label("Room No: " + moduleRoomNumber);

        VBox container = new VBox(5);
        container.getChildren().addAll(moduleLecturerLabel, moduleNameLabel, moduleRoomNumberLabel);
        return container;
    }

    private void setUpToolTips(final String moduleName, final String moduleLecturer, final String moduleRoomNumber) {
        Tooltip toolTip = (new Tooltip(
                "Module: " + moduleName + "\nLecturer: " + moduleLecturer + "\nRoom No: " + moduleRoomNumber));
        Tooltip.install(this, toolTip);
    }
}
