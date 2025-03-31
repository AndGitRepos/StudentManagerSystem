package sms.gradle.model.entities;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student {
    private final int id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dateOfBirth;
    private Date joinDate;
}
