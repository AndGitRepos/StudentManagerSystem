package sms.gradle.utils.Session;

import java.util.Optional;

public class Session {
    private static final Session instance = new Session();
    private Optional<User> user;

    private Session() {
        user = Optional.empty();
    }

    public static Session getInstance() {
        return instance;
    }

    public Optional<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = Optional.of(user);
    }

    public void clearSession() {
        user = Optional.empty();
    }
}
