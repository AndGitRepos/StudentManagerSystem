package sms.gradle.utils.checks.textfield;

public interface TextFieldCheck {
    boolean isValid(String text);

    String getErrorMessage();
}
