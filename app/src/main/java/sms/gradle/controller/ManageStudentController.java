package sms.gradle.controller;

import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.CourseEnrollmentDAO;
import sms.gradle.model.dao.ResultDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.CourseEnrollment;
import sms.gradle.model.entities.Student;
import sms.gradle.utils.Common;
import sms.gradle.view.ViewFactory;

public final class ManageStudentController {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Updates the list of students with all the students in the database
     * @param event The action event that triggered this method
     */
    public static void updateListOfStudents(ActionEvent event) {
        LOGGER.debug("Updating list of students");
        ListView<Student> studentList = (ListView<Student>)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#studentListView");
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
        LOGGER.debug("Selecting student");
        ListView<Student> studentList = (ListView<Student>)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#studentListView");
        ListView<Course> enrolledCourses = (ListView<Course>)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#enrolledCoursesListView");
        ListView<Course> availableCourses = (ListView<Course>)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#availableCoursesListView");

        Student selectedStudent = studentList.getSelectionModel().getSelectedItem();

        TextField studentIdField = (TextField)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#studentIdField");
        TextField firstNameField = (TextField)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#firstNameField");
        TextField lastNameField = (TextField)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#lastNameField");
        DatePicker dateOfBirthPicker = (DatePicker)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#dateOfBirthPicker");
        DatePicker joinDatePicker = (DatePicker)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#joinDatePicker");
        TextField emailField = (TextField)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#emailField");
        PasswordField passwordField = (PasswordField)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#passwordField");

        if (selectedStudent != null) {
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
                List<CourseEnrollment> courseEnrollments = CourseEnrollmentDAO.findByStudentId(selectedStudent.getId());
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
                List<Course> allCourses = CourseDAO.findAll();
                for (Course course : allCourses) {
                    if (!enrolledCourses.getItems().contains(course)) {
                        availableCourses.getItems().add(course);
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("Failed to update available courses list: ", e);
            }
        }
    }

    /**
     * Creates a new student in the database with the provided information.
     *
     * @param event The action event that triggered this method
     */
    public static void createNewStudent(ActionEvent event) {
        LOGGER.debug("Creating new student");
        TextField firstNameField = (TextField)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#firstNameField");
        TextField lastNameField = (TextField)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#lastNameField");
        DatePicker dateOfBirthPicker = (DatePicker)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#dateOfBirthPicker");
        DatePicker joinDatePicker = (DatePicker)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#joinDatePicker");
        TextField emailField = (TextField)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#emailField");
        PasswordField passwordField = (PasswordField)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#passwordField");
        try {
            Student newStudent = new Student(
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
        LOGGER.debug("Deleting student");
        ListView<Student> studentList = (ListView<Student>)
                ViewFactory.getInstance().getManageStudentStage().getScene().lookup("#studentListView");
        Student selectedStudent = studentList.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            LOGGER.debug("Deleting student: {}", selectedStudent);
            try {
                ResultDAO.deleteByStudentId(selectedStudent.getId());
                CourseEnrollmentDAO.deleteByStudentId(selectedStudent.getId());
                StudentDAO.delete(selectedStudent.getId());
                studentList.getItems().remove(selectedStudent);
            } catch (SQLException e) {
                LOGGER.error("Failed to delete student: ", e);
            }
        }
    }
}
