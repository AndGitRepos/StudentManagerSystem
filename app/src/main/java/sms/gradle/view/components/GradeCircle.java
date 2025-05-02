package sms.gradle.view.components;

import javafx.geometry.Pos;
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

    /**
     * Constructor for GradeCircle
     *
     * @param grade The grade score to be displayed
     * @throws IllegalArgumentException if the grade is not between 0 and 100
     */
    public GradeCircle(final int grade) {

        getStylesheets().add(getClass().getResource("/styles/components.css").toExternalForm());

        gradeValidation(grade);
        circle = new Circle(100);

        styleCircleColourByGrade(grade);

        gradeLabel = new Label("Grade:\n" + grade + "%");
        gradeLabel.setTextFill(Color.WHITE);
        gradeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        gradeLabel.setAlignment(Pos.CENTER);
        gradeLabel.setWrapText(true);
        gradeLabel.setPrefHeight(80);
        gradeLabel.setPrefWidth(180);

        getChildren().addAll(circle, gradeLabel);

        Tooltip tooltip = new Tooltip("Module Grade: " + grade + "%");
        Tooltip.install(this, tooltip);
    }

    /**
     * Validates the grade score
     *
     * @param grade The grade score to be validated
     * @throws IllegalArgumentException if the grade is not between 0 and 100
     */
    private void gradeValidation(final int grade) {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        }
    }

    /**
     * Styles the circle colour based on the grade score
     *
     * @param grade The grade score to be styled
     */
    private void styleCircleColourByGrade(final int grade) {

        if (grade >= EXCEPTIONAL_GRADE) {
            circle.setFill(Color.PALEGREEN);
        } else if (grade >= MERIT_GRADE) {
            circle.setFill(Color.PALETURQUOISE);
        } else if (grade >= PASSING_GRADE) {
            circle.setFill(Color.PALEGOLDENROD);
        } else {
            circle.setFill(Color.LIGHTSALMON);
        }

        // White border
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(5);
    }
}
