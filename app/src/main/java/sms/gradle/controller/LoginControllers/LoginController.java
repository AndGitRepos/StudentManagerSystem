package sms.gradle.controller.LoginControllers;

import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sms.gradle.model.dao.AdminDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.utils.Common;
import sms.gradle.utils.Session.Session;
import sms.gradle.utils.Session.User;
import sms.gradle.utils.Session.UserType;
import sms.gradle.view.Frames.LoginUI;
import sms.gradle.view.ViewFactory;

public class LoginController {

    private final LoginUI loginUI;

    public LoginController(LoginUI loginUI) {
        this.loginUI = loginUI;
    }

    public void handleLoginAttempt() {

        String email = loginUI.getUsernameField().getText();
        String password = loginUI.getPasswordField().getText();

        if (!haveValidInput(email, password)) {
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
            handleError(e);
        }
    }

    private boolean haveValidInput(String email, String password) {
        return (!email.isEmpty() && !password.isEmpty());
    }

    private User authenticateUser(String email, String hashedPassword) throws SQLException {

        if (AdminDAO.verifyPassword(email, hashedPassword)) {
            var admin = AdminDAO.findByEmail(email).orElseThrow();
            return new User(admin.getId(), UserType.ADMIN, admin.getFirstName(), admin.getLastName(), admin.getEmail());
        }

        if (StudentDAO.verifyPassword(email, hashedPassword)) {
            var student = StudentDAO.findByEmail(email).orElseThrow();
            return new User(
                    student.getId(),
                    UserType.STUDENT,
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail());
        }
        return null;
    }

    private void handleSuccessLogin(User user) {
        Session.getInstance().setUser(user);
        changeToUserDashboard(user);
        resetLoginForm();
    }

    private void changeToUserDashboard(User user) {
        if (user.getType() == UserType.ADMIN) {
            ViewFactory.getInstance().changeToStaffDashboardStage();
        } else {
            ViewFactory.getInstance().changeToStudentDashboardStage();
        }
    }

    private void displayLoginFailureError(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
        resetLoginForm();
    }

    private void handleError(Exception e) {
        displayLoginFailureError("Login error occured! Please retry.");
    }

    private void resetLoginForm() {
        loginUI.completelyClearFields();
    }
}
