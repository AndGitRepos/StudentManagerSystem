package sms.gradle.utils.session;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SessionTest {
    @Test
    void testIsValidSession() {
        Session session = Session.getInstance();
        assertFalse(session.getUser().isPresent());
    }

    @Test
    void testSetUser() {
        Session session = Session.getInstance();
        User user = new User(1, UserType.ADMIN, "John", "Smith", "smith@sms.com");
        session.setUser(user);
        assertTrue(session.getUser().isPresent());

        assertEquals(user, session.getUser().get());
    }

    @Test
    void testClearSession() {
        Session session = Session.getInstance();
        User user = new User(1, UserType.ADMIN, "John", "Smith", "smith@sms.com");
        session.setUser(user);
        assertTrue(session.getUser().isPresent());
        assertEquals(user, session.getUser().get());
        session.clearSession();
        assertFalse(session.getUser().isPresent());
    }

    @Test
    void testGetInstance() {
        Session session1 = Session.getInstance();
        Session session2 = Session.getInstance();
        assertEquals(session1, session2);
    }

    @Test
    void testGetUser() {
        Session session = Session.getInstance();
        User user = new User(1, UserType.ADMIN, "John", "Smith", "smith@sms.com");
        session.setUser(user);
        assertTrue(session.getUser().isPresent());
        assertEquals(user, session.getUser().get());
    }
}
