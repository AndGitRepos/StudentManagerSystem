package sms.gradle.controller;

import java.sql.SQLException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sms.gradle.model.dao.AdminDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Admin;
import sms.gradle.model.entities.Student;
import sms.gradle.utils.Common;
import sms.gradle.utils.Session.Session;
import sms.gradle.utils.Session.User;
import sms.gradle.utils.Session.UserType;
import sms.gradle.view.ViewFactory;

public final class LoginController {

    public static void handleLoginButton(ActionEvent event) {
        TextField emailField =
                (TextField) ViewFactory.getInstance().getLoginStage().getScene().lookup("#email-field");
        PasswordField passwordField = (PasswordField)
                ViewFactory.getInstance().getLoginStage().getScene().lookup("#password-field");
        Label loginErrorLabel =
                (Label) ViewFactory.getInstance().getLoginStage().getScene().lookup("#login-error-label");

        String email = emailField.getText();
        String hashedPassword = Common.generateSha256Hash(passwordField.getText());

        try {
            if (StudentDAO.verifyPassword(email, hashedPassword)) {
                Optional<Student> student = StudentDAO.findByEmail(email);
                if (student.isEmpty()) {
                    loginErrorLabel.setText("Student not found");
                    loginErrorLabel.setVisible(true);
                } else {
                    Session.getInstance()
                            .setUser(new User(
                                    student.get().getId(),
                                    UserType.STUDENT,
                                    student.get().getFirstName(),
                                    student.get().getLastName(),
                                    student.get().getEmail()));
                    ViewFactory.getInstance().changeToStudentDashboardStage();
                }
            } else if (AdminDAO.verifyPassword(email, hashedPassword)) {
                Optional<Admin> admin = AdminDAO.findByEmail(email);
                if (admin.isEmpty()) {
                    loginErrorLabel.setText("Admin not found");
                    loginErrorLabel.setVisible(true);
                } else {
                    Session.getInstance()
                            .setUser(new User(
                                    admin.get().getId(),
                                    UserType.ADMIN,
                                    admin.get().getFirstName(),
                                    admin.get().getLastName(),
                                    admin.get().getEmail()));
                    ViewFactory.getInstance().changeToStaffDashboardStage();
                }
            } else {
                loginErrorLabel.setText("Invalid email or password");
                loginErrorLabel.setVisible(true);
            }
        } catch (SQLException e) {
            System.out.println("Error logging in");
        }
    }
}
