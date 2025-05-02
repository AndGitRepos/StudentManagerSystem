package sms.gradle.utils.checks.textfield;

public class MinLengthCheck implements TextFieldCheck {

    private final int minLength;

    public MinLengthCheck(int minLength) {
        this.minLength = minLength;
    }

    /**
     * @param text The text to check
     * @return true if the text is at least <code>minLength</code> characters long, false otherwise
     */
    @Override
    public boolean isValid(String text) {
        return text.length() >= minLength;
    }

    @Override
    public String getErrorMessage() {
        return minLength == 1 ? "Cannot be empty." : "Must be at least " + minLength + " characters long.";
    }
}
