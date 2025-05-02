package sms.gradle.utils.checks.textfield;

public class ContainsAtLeastOneSymbolCheck implements TextFieldCheck {

    /**
     * @param text The text to check
     * @return true if the text contains at least one symbol, false otherwise
     */
    @Override
    public boolean isValid(String text) {
        return text.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    @Override
    public String getErrorMessage() {
        return "Must contain at least one symbol.";
    }
}
