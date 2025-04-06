package sms.gradle.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private final int id;
    private final int studentId;
    private final int assessmentId;
    private final int grade;
}
