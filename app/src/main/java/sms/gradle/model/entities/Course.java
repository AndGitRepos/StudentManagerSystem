package sms.gradle.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Course {
    private final int id;
    private String name;
    private String description;
}
