package sms.gradle.utils.Session;

import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Session {
    private static final Session instance = new Session();
    private Optional<User> user;

    private static final Logger LOGGER = LogManager.getLogger();

    private Session() {
        user = Optional.empty();
    }

    public static Session getInstance() {
        return instance;
    }

    public Optional<User> getUser() {
        LOGGER.debug("Getting session user: {}", user);
        return user;
    }

    public void setUser(User user) {
        LOGGER.debug("Setting session user: {}", user);
        this.user = Optional.of(user);
    }

    public void clearSession() {
        LOGGER.debug("Clearing session");
        user = Optional.empty();
    }
}
