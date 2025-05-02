package sms.gradle.utils.checks.datepicker;

import java.time.LocalDate;

public class HasValue implements DatePickerCheck {

    @Override
    public boolean isValid(LocalDate date) {
        return date != null;
    }

    @Override
    public String getErrorMessage() {
        return "Date cannot be empty.";
    }
}
