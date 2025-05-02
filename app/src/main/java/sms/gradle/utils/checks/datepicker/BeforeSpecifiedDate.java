package sms.gradle.utils.checks.datepicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BeforeSpecifiedDate implements DatePickerCheck {

    private final LocalDate specifiedDate;

    public BeforeSpecifiedDate(LocalDate specifiedDate) {
        this.specifiedDate = specifiedDate;
    }

    @Override
    public boolean isValid(LocalDate date) {
        if (date == null) {
            return false;
        }

        return date.isBefore(specifiedDate);
    }

    @Override
    public String getErrorMessage() {
        return "Must be before " + specifiedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
