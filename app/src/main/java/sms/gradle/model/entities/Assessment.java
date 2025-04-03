package sms.gradle.model.entities;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Assessment {
    private int id;
    private String name;
    private String description;
    private Date dueDate;
    private int moduleId;
}
