package sms.gradle.controller.LoginControllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sms.gradle.utils.Session.UserType;

@Getter
@AllArgsConstructor
public class LoginAuthenticationOutcome {
    private final Boolean success;
    private final UserType userType;
    private final Object entity;

    public Boolean isSuccess() {
        return success;
    }

    public UserType getUserType() {
        return userType;
    }

    public Object getEntity() {
        return entity;
    }
}
