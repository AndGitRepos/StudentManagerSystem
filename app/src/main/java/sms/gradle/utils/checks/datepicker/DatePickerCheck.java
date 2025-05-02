package sms.gradle.utils.checks.datepicker;

import java.time.LocalDate;

public interface DatePickerCheck {
    boolean isValid(LocalDate date);

    String getErrorMessage();
}
