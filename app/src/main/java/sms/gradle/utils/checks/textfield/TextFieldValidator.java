package sms.gradle.utils.checks.textfield;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class TextFieldValidator {

    private final List<TextFieldCheck> checks;

    public TextFieldValidator(List<TextFieldCheck> checks) {
        this.checks = checks;
    }

    public boolean validate(TextField textField) {
        String input = textField.getText();

        // Collect all error messages from the checks
        List<String> errorMessages = checks.stream()
                .filter(check -> !check.isValid(input))
                .map(TextFieldCheck::getErrorMessage)
                .collect(Collectors.toList());

        if (errorMessages.isEmpty()) {
            textField.setStyle("");
            textField.setTooltip(null);
            return true;
        } else {
            textField.setStyle("-fx-border-color: red;");

            // Format error message to start with "Error: (X)" where X is the number of errors
            String errorMessage = "Error: (" + errorMessages.size() + ")\n\n" + String.join("\n", errorMessages);
            textField.setTooltip(new Tooltip(errorMessage));

            return false;
        }
    }
}
