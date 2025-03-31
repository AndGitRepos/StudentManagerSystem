package sms.gradle.model.entities;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseEnrollment {
    private final int id;
    private int studentId;
    private int courseId;
    Date enrollmentDate;
}
