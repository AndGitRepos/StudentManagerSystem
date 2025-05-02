package sms.gradle.utils.checks;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import sms.gradle.utils.Common;
import sms.gradle.utils.checks.datepicker.DatePickerCheck;
import sms.gradle.utils.checks.textfield.TextFieldCheck;

@Getter
public class NodeValidator {
    String userFriendlyName;
    String selector;
    List<TextFieldCheck> textValidators;
    List<DatePickerCheck> dateValidators;
    List<String> currentErrors;

    public NodeValidator(
            String userFriendlyName,
            String selector,
            List<TextFieldCheck> textValidators,
            List<DatePickerCheck> dateValidators) {
        this.userFriendlyName = userFriendlyName;
        this.selector = selector;
        this.textValidators = textValidators;
        this.dateValidators = dateValidators;
        this.currentErrors = new ArrayList<>();
    }

    public boolean checkValidity(Stage viewStage) {
        currentErrors.clear();

        if (textValidators.size() > 0) {
            TextField textField = Common.getNode(viewStage, selector);
            for (TextFieldCheck textValidator : textValidators) {
                if (!textValidator.isValid(textField.getText())) {
                    currentErrors.add(userFriendlyName + ": " + textValidator.getErrorMessage());
                }
            }
        } else if (dateValidators.size() > 0) {
            DatePicker datePicker = Common.getNode(viewStage, selector);
            for (DatePickerCheck dateValidator : dateValidators) {
                if (!dateValidator.isValid(datePicker.getValue())) {
                    currentErrors.add(userFriendlyName + ": " + dateValidator.getErrorMessage());
                }
            }
        }

        return currentErrors.size() == 0;
    }
}
