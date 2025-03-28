package sms.gradle.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Module {
    private final int id;
    private String name;
    private String description;
    private String lecturer;
    private int courseId;
}
