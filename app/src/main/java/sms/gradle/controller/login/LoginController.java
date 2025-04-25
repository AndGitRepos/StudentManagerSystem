package sms.gradle.controller.login;

import java.sql.SQLException;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    private static Scene getLoginScene() {
        return ViewFactory.getInstance().getLoginStage().getScene();
    }

    public static void handleLoginAttempt() {

        Scene loginScene = getLoginScene();

        TextField usernameField = (TextField) loginScene.lookup("#username_field");
        PasswordField passwordField = (PasswordField) loginScene.lookup("#password_field");

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

    private static void handleSuccessLogin(User user) {
        Session.getInstance().setUser(user);
        changeToUserDashboard(user);
        resetLoginForm();
    }

    private static void changeToUserDashboard(User user) {
        if (user.getType() == UserType.ADMIN) {
            ViewFactory.getInstance().changeToAdminDashboardStage();
        } else {
            ViewFactory.getInstance().changeToStudentDashboardStage();
        }
    }

    private static void displayLoginFailureError(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
        resetLoginForm();
    }

    private static void resetLoginForm() {
        PasswordField passwordField = (PasswordField) getLoginScene().lookup("#password_field");
        passwordField.clear();
    }
}
