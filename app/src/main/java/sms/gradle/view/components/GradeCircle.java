package sms.gradle.view.components;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GradeCircle extends StackPane {
    private final Circle circle;
    private final Label gradeLabel;

    public GradeCircle(int grade) {
        circle = new Circle(30);
        circle.setFill(Color.DODGERBLUE);
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(3);

        gradeLabel = new Label(grade + "%");

        gradeLabel.setTextFill(Color.WHITE);
        gradeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        getChildren().addAll(circle, gradeLabel);

        Tooltip tooltip = new Tooltip("Module Grade: " + grade + "%");
        Tooltip.install(this, tooltip);
    }
}
