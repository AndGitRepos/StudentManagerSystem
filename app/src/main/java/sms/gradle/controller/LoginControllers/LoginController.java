package sms.gradle.controller.LoginControllers;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import sms.gradle.model.dao.AdminDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Admin;
import sms.gradle.model.entities.Student;
import sms.gradle.utils.Session.Session;
import sms.gradle.utils.Session.User;
import sms.gradle.utils.Session.UserType;
import sms.gradle.view.Frames.LoginUI;
import sms.gradle.view.ViewFactory;

public class LoginController {

    private final LoginUI loginUI;
    private final ProgressIndicator loadingProgressSpinner;

    public LoginController(LoginUI loginUI) {
        this.loginUI = loginUI;
        this.loadingProgressSpinner = new ProgressIndicator();
        initializeController();
    }

    private void initializeController() {

        loadingProgressSpinner.setVisible(false);
        loadingProgressSpinner.setPrefSize(50, 50);

        VBox loadingSpinnerBox = new VBox(10);
        loadingSpinnerBox.setAlignment(Pos.CENTER);
        loadingSpinnerBox.getChildren().add(loadingProgressSpinner);

        loginUI.getChildren().add(loginUI.getChildren().size() - 1, loadingSpinnerBox);
        loginUI.getLoginButton().setOnAction(e -> handleLoginAttempt());
    }

    private void handleLoginAttempt() {

        String email = loginUI.getUsernameField().getText();
        String password = loginUI.getPasswordField().getText();

        if (email.isEmpty() || password.isEmpty()) {
            loginUI.displayLoginError("Please input a username and password");
            return;
        }

        loginUI.getLoginButton().setDisable(true);
        loadingProgressSpinner.setVisible(true);

        Task<LoginAuthenticationOutcome> loginProcess = createLoginProcess(email, password);
        loginProcess.setOnSucceeded(event -> handleAuthenticationOutcome(loginProcess.getValue()));
        loginProcess.setOnFailed(event -> handleFailedLoginAttempt(loginProcess.getException()));

        new Thread(loginProcess).start();
    }

    private Task<LoginAuthenticationOutcome> createLoginProcess(String email, String password) {
        return new Task<>() {

            @Override
            protected LoginAuthenticationOutcome call() throws Exception {
                Thread.sleep(500); // simulate async processing delay

                try {
                    if (StudentDAO.verifyPassword(email, password)) {
                        var student = StudentDAO.findByEmail(email).orElseThrow();
                        return new LoginAuthenticationOutcome(true, UserType.STUDENT, student);
                    }
                    if (email.equals("admin@sms.com") && password.equals("admin")) {
                        var admin = AdminDAO.findByEmail(email).orElseThrow();
                        return new LoginAuthenticationOutcome(true, UserType.ADMIN, admin);
                    }
                    return new LoginAuthenticationOutcome(false, null, null);

                } catch (Exception e) {
                    e.printStackTrace();
                    return new LoginAuthenticationOutcome(false, null, null);
                }
            }
        };
    }

    private void handleAuthenticationOutcome(LoginAuthenticationOutcome outcome) {

        if (outcome.isSuccess()) {
            if (outcome.getUserType() == UserType.STUDENT) {
                Student student = (Student) outcome.getEntity();
                Session.getInstance()
                        .setUser(new User(
                                student.getId(),
                                UserType.STUDENT,
                                student.getFirstName(),
                                student.getLastName(),
                                student.getEmail()));
                ViewFactory.getInstance().changeToStudentDashboardStage();

            } else {
                Admin admin = (Admin) outcome.getEntity();
                Session.getInstance()
                        .setUser(new User(
                                admin.getId(),
                                UserType.ADMIN,
                                admin.getFirstName(),
                                admin.getLastName(),
                                admin.getEmail()));
                ViewFactory.getInstance().changeToStaffDashboardStage();
            }
        } else {
            loginUI.displayLoginError("Login Failed! Please try again");
        }
        resetLoginForm();
    }

    private void handleFailedLoginAttempt(Throwable exception) {
        loginUI.displayLoginError("Login Failed! Please try again");
        resetLoginForm();
    }

    private void resetLoginForm() {
        loginUI.completelyClearFields();
        loginUI.getLoginButton().setDisable(false);
        loadingProgressSpinner.setVisible(false);
    }
}
