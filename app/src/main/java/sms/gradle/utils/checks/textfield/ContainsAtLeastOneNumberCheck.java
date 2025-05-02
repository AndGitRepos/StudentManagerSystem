package sms.gradle.utils.checks.textfield;

public class ContainsAtLeastOneNumberCheck implements TextFieldCheck {

    /**
     * @param text The text to check
     * @return true if the text contains at least one number, false otherwise
     */
    @Override
    public boolean isValid(String text) {
        return text.matches(".*\\d.*");
    }

    @Override
    public String getErrorMessage() {
        return "Must contain at least one number.";
    }
}
