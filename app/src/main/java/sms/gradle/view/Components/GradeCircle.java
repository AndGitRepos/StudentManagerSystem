package sms.gradle.view.Components;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Display Module Grade icon in circular manner, colour-coded by grade score
 */
public class GradeCircle extends StackPane {

    private final Circle circle;
    private final Label gradeLabel;
    private static final int EXCEPTIONAL_GRADE = 80;
    private static final int MERIT_GRADE = 70;
    private static final int PASSING_GRADE = 50;

    public GradeCircle(final int grade) {

        getStylesheets().add(getClass().getResource("/styles/components.css").toExternalForm());

        gradeValidation(grade);
        circle = new Circle(30);

        styleCircleColourByGrade(grade);

        gradeLabel = new Label(grade + "%");
        gradeLabel.setTextFill(Color.WHITE);

        getChildren().addAll(circle, gradeLabel);

        Tooltip tooltip = new Tooltip("Module Grade: " + grade + "%");
        Tooltip.install(this, tooltip);
    }

    private void gradeValidation(final int grade) {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        }
    }

    private void styleCircleColourByGrade(final int grade) {

        if (grade >= EXCEPTIONAL_GRADE) {
            circle.setFill(Color.GREEN);
        } else if (grade >= MERIT_GRADE) {
            circle.setFill(Color.BLUE);
        } else if (grade >= PASSING_GRADE) {
            circle.setFill(Color.ORANGE);
        } else {
            circle.setFill(Color.RED);
        }

        // White border
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(3);
    }
}
