package sms.gradle.utils.checks.datepicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AfterSpecifiedDate implements DatePickerCheck {

    private final LocalDate specifiedDate;

    public AfterSpecifiedDate(LocalDate specifiedDate) {
        this.specifiedDate = specifiedDate;
    }

    @Override
    public boolean isValid(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(specifiedDate);
    }

    @Override
    public String getErrorMessage() {
        return "Must be after " + specifiedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
