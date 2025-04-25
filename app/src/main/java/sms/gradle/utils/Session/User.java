package sms.gradle.utils.session;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private UserType type;
    private String firstName;
    private String lastName;
    private String email;
}
