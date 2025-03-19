package sms.gradle.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sms.gradle.model.entities.Student;

public class StudentDAO {

    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    /**
     * Adds a student to the database with the given password.
     * @param student the student to add
     * @param password the password of the student
     * @throws SQLException if a database access error occurs or the connection is closed
     */
    public void addStudent(Student student, String password) throws SQLException {
        try (Statement statement = databaseConnection.getConnection().createStatement()) {
            statement.execute(
                    """
                    INSERT INTO students (
                    first_name,
                    last_name,
                    email,
                    password,
                    date_of_birth,
                    join_date
                ) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')
            """
                            .formatted(
                                    student.getFirstName(),
                                    student.getLastName(),
                                    student.getEmail(),
                                    password,
                                    student.getDateOfBirth(),
                                    student.getJoinDate()));
        }
    }

    /**
     * Gets all students from the database.
     * @return a list of all students in the database or <code>null</code> if no students exist
     * @throws SQLException if a database access error occurs or the connection is closed
     */
    public ArrayList<Student> getStudents() throws SQLException {
        ArrayList<Student> students = new ArrayList<>();

        try (Statement statement = databaseConnection.getConnection().createStatement()) {
            ResultSet results = statement.executeQuery("SELECT * FROM students");
            while (results.next()) {
                students.add(new Student(
                        results.getInt("id"),
                        results.getString("first_name"),
                        results.getString("last_name"),
                        results.getString("email"),
                        results.getDate("date_of_birth"),
                        results.getDate("join_date")));
            }
        }

        if (!students.isEmpty()) return students;

        return null;
    }

    /**
     *
     * Gets a specific student from the database by their id.
     * @param id the id of the student to get
     * @return the <code>Student</code> with the given id or <code>null</code> of no student with the given id exists
     * @throws SQLException
     */
    public Student getStudentById(int id) throws SQLException {
        try (Statement statement = databaseConnection.getConnection().createStatement()) {
            ResultSet results = statement.executeQuery("SELECT * FROM students WHERE id = " + id);
            if (results.next()) {
                return new Student(
                        results.getInt("id"),
                        results.getString("first_name"),
                        results.getString("last_name"),
                        results.getString("email"),
                        results.getDate("date_of_birth"),
                        results.getDate("join_date"));
            }
        }

        return null;
    }
}
