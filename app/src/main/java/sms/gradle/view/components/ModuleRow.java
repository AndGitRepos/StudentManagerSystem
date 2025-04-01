package sms.gradle.view.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ModuleRow extends HBox {

    private final Label moduleNameLabel;
    private final Label moduleLecturerLabel;
    private final Label moduleRoomNumberLabel;
    private final GradeCircle gradeCircle;

    public ModuleRow(String moduleName, String moduleLecturer, String moduleRoomNumber, int studentModuleScore) {

        setSpacing(20);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5;");

        moduleNameLabel = new Label("Module Name, ID: " + moduleName);

        moduleLecturerLabel = new Label("Lecturer: " + moduleLecturer);

        moduleRoomNumberLabel = new Label("Room No: " + moduleRoomNumber);

        VBox informationBox = new VBox(5);
        informationBox.getChildren().addAll(moduleLecturerLabel, moduleNameLabel, moduleRoomNumberLabel);

        gradeCircle = new GradeCircle(studentModuleScore);

        getChildren().addAll(informationBox, gradeCircle);

        Tooltip toolTip = (new Tooltip(
                "Module: " + moduleName + "\nLecturer: " + moduleLecturer + "\nRoom No: " + moduleRoomNumber));
        Tooltip.install(this, toolTip);
    }
}
