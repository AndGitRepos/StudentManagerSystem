package sms.gradle.controller.login;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.AdminDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Admin;
import sms.gradle.model.entities.Student;
import sms.gradle.utils.Common;
import sms.gradle.utils.session.Session;
import sms.gradle.utils.session.User;
import sms.gradle.utils.session.UserType;
import sms.gradle.view.ViewFactory;

public final class LoginController {
    private static final Logger LOGGER = LogManager.getLogger();

    private LoginController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getLoginStage();
    }

    /**
     * Fills the login form with default admin credentials for testing purposes.
     * Sets the username field to "admin@sms.com" and password field to "admin".
     *
     * @param event The ActionEvent triggered by clicking the fill admin details button
     */
    public static void handleFillAdminDetails(ActionEvent event) {
        TextField usernameField = Common.getNode(getViewStage(), "#username_field");
        PasswordField passwordField = Common.getNode(getViewStage(), "#password_field");

        usernameField.setText("admin@sms.com");
        passwordField.setText("admin");
    }

    /**
     * Fills the login form with a random student's credentials for testing purposes.
     * Retrieves a list of students from the database, selects a random student,
     * and sets the username field to the student's email and password field to the student's first and last name.
     *
     * @param event The ActionEvent triggered by clicking the fill student details button
     */
    public static void handleFillStudentDetails(ActionEvent event) {
        TextField usernameField = Common.getNode(getViewStage(), "#username_field");
        PasswordField passwordField = Common.getNode(getViewStage(), "#password_field");

        try {
            List<Student> students = StudentDAO.findAll();
            SecureRandom secureRandom = new SecureRandom();
            if (!students.isEmpty()) {
                Student randomStudent = students.get(secureRandom.nextInt(students.size()));
                usernameField.setText(randomStudent.getEmail());
                passwordField.setText(randomStudent.getFirstName() + randomStudent.getLastName());
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find students", e);
        }
    }

    /**
     * Handles the login attempt by retrieving the username and password from the form,
     * authenticating the user, and either displaying a success message or an error message.
     *
     * @param event The ActionEvent triggered by clicking the login button
     */
    public static void handleLoginAttempt(ActionEvent event) {
        TextField usernameField = Common.getNode(getViewStage(), "#username_field");
        PasswordField passwordField = Common.getNode(getViewStage(), "#password_field");

        String email = usernameField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            displayLoginFailureError("Enter password and username first");
            return;
        }

        try {
            String hashedPassword = Common.generateSha256Hash(password);
            User user = authenticateUser(email, hashedPassword);

            if (user != null) {
                handleSuccessLogin(user);
            } else {
                displayLoginFailureError("Invalid Login Details!");
            }
        } catch (Exception e) {
            displayLoginFailureError("Login error occured! Please retry.");
        }
    }

    /**
     * Authenticates the user by checking the provided email and hashed password against the database.
     * If the user is found, creates a new User object with the user's details and returns it.
     * If the user is not found, returns null.
     *
     * @param email The user's email address
     * @param hashedPassword The hashed password provided by the user
     * @return A User object if the user is authenticated, null otherwise
     * @throws SQLException If there is an error accessing the database
     */
    private static User authenticateUser(String email, String hashedPassword) throws SQLException {
        if (AdminDAO.verifyPassword(email, hashedPassword)) {
            Optional<Admin> admin = AdminDAO.findByEmail(email);

            return admin.map(a -> new User(a.getId(), UserType.ADMIN, a.getFirstName(), a.getLastName(), a.getEmail()))
                    .orElse(null);
        }

        if (StudentDAO.verifyPassword(email, hashedPassword)) {
            Optional<Student> student = StudentDAO.findByEmail(email);

            return student.map(
                            s -> new User(s.getId(), UserType.STUDENT, s.getFirstName(), s.getLastName(), s.getEmail()))
                    .orElse(null);
        }
        return null;
    }

    /**
     * Handles the successful login by setting the user in the session, changing the view stage,
     * and resetting the login form.
     *
     * @param user The User object representing the authenticated user
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static void handleSuccessLogin(User user) {
        Session.getInstance().setUser(user);
        changeToUserDashboard(user);
        resetLoginForm();
    }

    /**
     * Changes the view to the appropriate dashboard based on the user type.
     * Hides the login stage and displays either the admin or student dashboard.
     *
     * @param user The authenticated User object containing the user type information
     */
    private static void changeToUserDashboard(User user) {
        ViewFactory.getInstance().getLoginStage().hide();
        if (user.getType() == UserType.ADMIN) {
            ViewFactory.getInstance().changeToAdminDashboardStage();
        } else {
            ViewFactory.getInstance().changeToStudentDashboardStage();
        }
    }

    /**
     * Displays an error message in a dialog box when the login attempt fails.
     * Resets the login form after displaying the error message.
     *
     * @param errorMessage The error message to be displayed
     */
    private static void displayLoginFailureError(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
        resetLoginForm();
    }

    /**
     * Resets the login form by clearing the username and password fields.
     */
    private static void resetLoginForm() {
        PasswordField passwordField = Common.getNode(getViewStage(), "#password_field");
        passwordField.clear();
    }
}
