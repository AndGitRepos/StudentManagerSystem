package sms.gradle.controller.admin;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.CourseEnrollmentDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.CourseEnrollment;
import sms.gradle.model.entities.Student;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;

public final class ManageStudentController {
    private static final Logger LOGGER = LogManager.getLogger();

    private ManageStudentController() {
        throw new UnsupportedOperationException("This is a controller class and cannot be instantiated");
    }

    private static Stage getViewStage() {
        return ViewFactory.getInstance().getManageStudentStage();
    }

    /**
     * Updates the list of students with all the students in the database
     *
     * @param event The action event that triggered this method
     */
    public static void updateListOfStudents(ActionEvent event) {
        LOGGER.debug("Updating list of students");
        final ListView<Student> studentList = Common.getNode(getViewStage(), "#studentListView");
        studentList.getItems().clear();
        try {
            StudentDAO.findAll().forEach(student -> studentList.getItems().add(student));
        } catch (SQLException e) {
            LOGGER.error("Failed to update list of students: ", e);
        }
    }

    /**
     * Populates form fields with selected student's information and updates course lists.
     *
     * @param event The action event that triggered this method
     */
    public static void selectStudent(ActionEvent event) {
        final ListView<Student> studentList = Common.getNode(getViewStage(), "#studentListView");
        final Student selectedStudent = studentList.getSelectionModel().getSelectedItem();
        LOGGER.debug("Selected student {}", selectedStudent);

        final ListView<Course> enrolledCourses = Common.getNode(getViewStage(), "#enrolledCoursesListView");
        final ListView<Course> availableCourses = Common.getNode(getViewStage(), "#availableCoursesListView");

        final TextField studentIdField = Common.getNode(getViewStage(), "#studentIdField");
        final TextField firstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField lastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final DatePicker dateOfBirthPicker = Common.getNode(getViewStage(), "#dateOfBirthPicker");
        final DatePicker joinDatePicker = Common.getNode(getViewStage(), "#joinDatePicker");
        final TextField emailField = Common.getNode(getViewStage(), "#emailField");
        final PasswordField passwordField = Common.getNode(getViewStage(), "#passwordField");

        if (selectedStudent == null) {
            return;
        }

        LOGGER.debug("Selected student: {}", selectedStudent);
        studentIdField.setText(String.valueOf(selectedStudent.getId()));
        firstNameField.setText(selectedStudent.getFirstName());
        lastNameField.setText(selectedStudent.getLastName());
        dateOfBirthPicker.setValue(selectedStudent.getDateOfBirth().toLocalDate());
        joinDatePicker.setValue(selectedStudent.getJoinDate().toLocalDate());
        emailField.setText(selectedStudent.getEmail());
        passwordField.setText("");

        // Update enrolled courses list
        enrolledCourses.getItems().clear();
        try {
            LOGGER.debug("Updating enrolled courses list");
            final List<CourseEnrollment> courseEnrollments =
                    CourseEnrollmentDAO.findByStudentId(selectedStudent.getId());
            for (CourseEnrollment courseEnrollment : courseEnrollments) {
                CourseDAO.findById(courseEnrollment.getCourseId())
                        .ifPresent(course -> enrolledCourses.getItems().add(course));
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update enrolled courses list: ", e);
        }

        // Update available courses list
        availableCourses.getItems().clear();
        try {
            LOGGER.debug("Updating available courses list");
            final List<Course> allCourses = CourseDAO.findAll();
            for (Course course : allCourses) {
                if (!enrolledCourses.getItems().contains(course)) {
                    availableCourses.getItems().add(course);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update available courses list: ", e);
        }
    }

    /**
     * Updates the student in the database with the provided information.
     *
     * @param event The action event that triggered this method
     */
    public static void updateStudent(ActionEvent event) {
        final TextField studentIdField = Common.getNode(getViewStage(), "#studentIdField");
        final TextField firstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField lastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final DatePicker dateOfBirthPicker = Common.getNode(getViewStage(), "#dateOfBirthPicker");
        final DatePicker joinDatePicker = Common.getNode(getViewStage(), "#joinDatePicker");
        final TextField emailField = Common.getNode(getViewStage(), "#emailField");
        final PasswordField passwordField = Common.getNode(getViewStage(), "#passwordField");

        try {
            final Student updatedStudent = new Student(
                    Integer.parseInt(studentIdField.getText()),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    java.sql.Date.valueOf(dateOfBirthPicker.getValue()),
                    java.sql.Date.valueOf(joinDatePicker.getValue()));
            StudentDAO.update(updatedStudent, Common.generateSha256Hash(passwordField.getText()));
        } catch (SQLException e) {
            LOGGER.error("Failed to update student: ", e);
        }
    }

    /**
     * Creates a new student in the database with the provided information.
     *
     * @param event The action event that triggered this method
     */
    public static void createNewStudent(ActionEvent event) {
        LOGGER.debug("Creating new student");
        final TextField firstNameField = Common.getNode(getViewStage(), "#firstNameField");
        final TextField lastNameField = Common.getNode(getViewStage(), "#lastNameField");
        final DatePicker dateOfBirthPicker = Common.getNode(getViewStage(), "#dateOfBirthPicker");
        final DatePicker joinDatePicker = Common.getNode(getViewStage(), "#joinDatePicker");
        final TextField emailField = Common.getNode(getViewStage(), "#emailField");
        final PasswordField passwordField = Common.getNode(getViewStage(), "#passwordField");

        try {
            final Student newStudent = new Student(
                    0,
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    java.sql.Date.valueOf(dateOfBirthPicker.getValue()),
                    java.sql.Date.valueOf(joinDatePicker.getValue()));
            StudentDAO.addStudent(newStudent, Common.generateSha256Hash(passwordField.getText()));
            LOGGER.debug("New student created: {}", newStudent);
        } catch (SQLException e) {
            LOGGER.error("Failed to create new student: ", e);
        }
    }

    /**
     * Deletes the selected student from the database.
     *
     * @param event The action event that triggered this method
     */
    public static void deleteStudent(ActionEvent event) {
        final ListView<Student> studentList = Common.getNode(getViewStage(), "#studentListView");
        final Student selectedStudent = studentList.getSelectionModel().getSelectedItem();
        LOGGER.debug("Deleting student: {}", selectedStudent);

        if (selectedStudent == null) {
            return;
        }

        LOGGER.debug("Deleting student: {}", selectedStudent);
        try {
            StudentDAO.delete(selectedStudent.getId());
            studentList.getItems().remove(selectedStudent);
        } catch (SQLException e) {
            LOGGER.error("Failed to delete student: ", e);
        }
    }

    /**
     * Enrolls the selected student in a course from the available courses list.
     * Takes the selected course from the available courses list view and adds it to the
     * enrolled courses list view. Creates a new CourseEnrollment record in the database.
     *
     * @param event The action event that triggered this method
     */
    public static void enrollCourse(ActionEvent event) {
        final TextField studentIdField = Common.getNode(getViewStage(), "#studentIdField");
        final ListView<Course> enrolledCourses = Common.getNode(getViewStage(), "#enrolledCoursesListView");
        final ListView<Course> availableCourses = Common.getNode(getViewStage(), "#availableCoursesListView");

        final Course selectedCourse = availableCourses.getSelectionModel().getSelectedItem();
        LOGGER.debug("Enrolling student {} in course: {}", studentIdField.getText(), selectedCourse);

        if (selectedCourse == null) {
            return;
        }

        int studentId = 0;
        try {
            studentId = Integer.parseInt(studentIdField.getText());
        } catch (NumberFormatException e) {
            LOGGER.error("Failed to convert student ID to integer", e);
            return;
        }

        try {
            CourseEnrollmentDAO.addCourseEnrollment(
                    new CourseEnrollment(0, studentId, selectedCourse.getId(), new Date(System.currentTimeMillis())));
            enrolledCourses.getItems().add(selectedCourse);
            availableCourses.getItems().remove(selectedCourse);
        } catch (SQLException e) {
            LOGGER.error("Failed to enroll student into course", e);
        }
    }

    /**
     * Unenrolls the selected student from a course from the enrolled courses list.
     * Takes the selected course from the enrolled courses list view and removes it from the
     * enrolled courses list view. Deletes the corresponding CourseEnrollment record from the database.
     *
     * @param event The action event that triggered this method
     */
    public static void unenrollCourse(ActionEvent event) {
        final TextField studentIdField = Common.getNode(getViewStage(), "#studentIdField");
        final ListView<Course> enrolledCourses = Common.getNode(getViewStage(), "#enrolledCoursesListView");
        final ListView<Course> availableCourses = Common.getNode(getViewStage(), "#availableCoursesListView");

        final Course selectedCourse = enrolledCourses.getSelectionModel().getSelectedItem();
        LOGGER.debug("Enrolling student {} in course: {}", studentIdField.getText(), selectedCourse);

        if (selectedCourse == null) {
            return;
        }

        int studentId = 0;
        try {
            studentId = Integer.parseInt(studentIdField.getText());
        } catch (NumberFormatException e) {
            LOGGER.error("Failed to convert student ID to integer", e);
            return;
        }

        try {
            final Optional<CourseEnrollment> courseEnrollment =
                    CourseEnrollmentDAO.findByStudentAndCourseId(studentId, selectedCourse.getId());
            if (courseEnrollment.isEmpty()) {
                LOGGER.warn(
                        "Failed to find course enrollment between student {} and course {}",
                        studentId,
                        selectedCourse.getId());
                return;
            }

            CourseEnrollmentDAO.delete(courseEnrollment.get().getId());
            availableCourses.getItems().add(selectedCourse);
            enrolledCourses.getItems().remove(selectedCourse);
        } catch (SQLException e) {
            LOGGER.error("Failed to unenroll student from course", e);
        }
    }

    /**
     * Hides the current stage and changes to the admin dashboard stage.
     *
     * @param event The action event that triggered this method
     */
    public static void handleOnShowEvent(WindowEvent event) {
        updateListOfStudents(new ActionEvent());
    }

    /**
     * Hides the current stage and changes to the admin dashboard stage.
     *
     * @param event The action event that triggered this method
     */
    public static void handleBackButton(ActionEvent event) {
        LOGGER.debug("Handling back button");
        ViewFactory.getInstance().getManageStudentStage().hide();
        ViewFactory.getInstance().changeToAdminDashboardStage();
    }
}
