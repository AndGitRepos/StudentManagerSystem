package sms.gradle.utils.checks.textfield;

public class ContainsAtLeastOneCapitalLetterCheck implements TextFieldCheck {

    /**
     * @param text The text to check
     * @return true if the text contains at least one capital letter, false otherwise
     */
    @Override
    public boolean isValid(String text) {
        return text.matches(".*[A-Z].*");
    }

    @Override
    public String getErrorMessage() {
        return "Must contain at least one capital letter.";
    }
}
