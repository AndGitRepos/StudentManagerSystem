package sms.gradle.utils.checks;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ChecksProcessor {

    public static boolean checkValidationError(Stage currentViewStage, List<NodeValidator> nodeValidators) {
        List<String> errorMessages = new ArrayList<>();

        for (NodeValidator nodeValidator : nodeValidators) {
            if (!nodeValidator.checkValidity(currentViewStage)) {
                errorMessages.addAll(nodeValidator.getCurrentErrors());
            }
        }

        if (errorMessages.size() > 0) {
            displayValidationErrors(errorMessages);
            return false;
        }

        return true;
    }

    /**
     * Displays validation errors in an alert dialog
     * @param errorMessages List of error messages to display
     */
    private static void displayValidationErrors(List<String> errorMessages) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Please fix the following issues:");

        StringBuilder content = new StringBuilder();
        for (String error : errorMessages) {
            content.append("• ").append(error).append("\n");
        }

        alert.setContentText(content.toString());
        alert.showAndWait();
    }
}
