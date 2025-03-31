package sms.gradle.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Admin {
    private final int id;
    private String firstName;
    private String lastName;
    private String email;
}
