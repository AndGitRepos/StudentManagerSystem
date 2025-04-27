package sms.gradle.controller.admin;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
        ListView<Student> studentList = Common.getNode(getViewStage(), "#studentListView");
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
        ListView<Student> studentList = Common.getNode(getViewStage(), "#studentListView");
        Student selectedStudent = studentList.getSelectionModel().getSelectedItem();
        LOGGER.debug("Selected student {}", selectedStudent);

        ListView<Course> enrolledCourses = Common.getNode(getViewStage(), "#enrolledCoursesListView");
        ListView<Course> availableCourses = Common.getNode(getViewStage(), "#availableCoursesListView");

        TextField studentIdField = Common.getNode(getViewStage(), "#studentIdField");
        TextField firstNameField = Common.getNode(getViewStage(), "#firstNameField");
        TextField lastNameField = Common.getNode(getViewStage(), "#lastNameField");
        DatePicker dateOfBirthPicker = Common.getNode(getViewStage(), "#dateOfBirthPicker");
        DatePicker joinDatePicker = Common.getNode(getViewStage(), "#joinDatePicker");
        TextField emailField = Common.getNode(getViewStage(), "#emailField");
        PasswordField passwordField = Common.getNode(getViewStage(), "#passwordField");

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

    /**
     * Creates a new student in the database with the provided information.
     *
     * @param event The action event that triggered this method
     */
    public static void createNewStudent(ActionEvent event) {
        LOGGER.debug("Creating new student");
        TextField firstNameField = Common.getNode(getViewStage(), "#firstNameField");
        TextField lastNameField = Common.getNode(getViewStage(), "#lastNameField");
        DatePicker dateOfBirthPicker = Common.getNode(getViewStage(), "#dateOfBirthPicker");
        DatePicker joinDatePicker = Common.getNode(getViewStage(), "#joinDatePicker");
        TextField emailField = Common.getNode(getViewStage(), "#emailField");
        PasswordField passwordField = Common.getNode(getViewStage(), "#passwordField");

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
        ListView<Student> studentList = Common.getNode(getViewStage(), "#studentListView");
        Student selectedStudent = studentList.getSelectionModel().getSelectedItem();
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

    public static void enrollCourse(ActionEvent event) {
        TextField studentIdField = Common.getNode(getViewStage(), "#studentIdField");
        ListView<Course> enrolledCourses = Common.getNode(getViewStage(), "#enrolledCoursesListView");
        ListView<Course> availableCourses = Common.getNode(getViewStage(), "#availableCoursesListView");

        Course selectedCourse = availableCourses.getSelectionModel().getSelectedItem();
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

    public static void unenrollCourse(ActionEvent event) {
        TextField studentIdField = Common.getNode(getViewStage(), "#studentIdField");
        ListView<Course> enrolledCourses = Common.getNode(getViewStage(), "#enrolledCoursesListView");
        ListView<Course> availableCourses = Common.getNode(getViewStage(), "#availableCoursesListView");

        Course selectedCourse = enrolledCourses.getSelectionModel().getSelectedItem();
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
            Optional<CourseEnrollment> courseEnrollment =
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
}
