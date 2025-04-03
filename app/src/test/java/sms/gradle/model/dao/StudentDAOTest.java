package sms.gradle.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import sms.gradle.model.entities.Student;

public class StudentDAOTest {

    @Mock
    private DatabaseConnection mockDbConnection;

    private MockedStatic<DatabaseConnection> mockedStatic;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(DatabaseConnection.class);
        mockedStatic.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() {
        mockedStatic.close();
    }

    @Test
    public void testAddStudent() throws SQLException {
        Date dateOfBirth = Date.valueOf("2003-10-16");
        Date joinDate = Date.valueOf("2022-09-14");
        String password = "Password123";
        Student student = new Student(0, "John", "Smith", "johnsmith@gmail.com", dateOfBirth, joinDate);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        StudentDAO.addStudent(student, password);

        verify(mockPreparedStatement).setString(1, student.getFirstName());
        verify(mockPreparedStatement).setString(2, student.getLastName());
        verify(mockPreparedStatement).setString(3, student.getEmail());
        verify(mockPreparedStatement).setString(4, password);
        verify(mockPreparedStatement).setDate(5, student.getDateOfBirth());
        verify(mockPreparedStatement).setDate(6, student.getJoinDate());
    }

    @Test
    public void testFindById() throws SQLException {
        int studentId = 1;
        Date dateOfBirth = Date.valueOf("2003-10-16");
        Date joinDate = Date.valueOf("2022-09-14");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("first_name")).thenReturn("John");
        when(mockResultSet.getString("last_name")).thenReturn("Smith");
        when(mockResultSet.getString("email")).thenReturn("johnsmith@gmail.com");
        when(mockResultSet.getDate("date_of_birth")).thenReturn(dateOfBirth);
        when(mockResultSet.getDate("join_date")).thenReturn(joinDate);

        Optional<Student> result = StudentDAO.findById(studentId);

        verify(mockPreparedStatement).setInt(1, studentId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Smith", result.get().getLastName());
        assertEquals("johnsmith@gmail.com", result.get().getEmail());
        assertEquals(dateOfBirth, result.get().getDateOfBirth());
        assertEquals(joinDate, result.get().getJoinDate());
    }

    @Test
    public void testFindByEmail() throws SQLException {
        String email = "johnsmith@gmail.com";
        Date dateOfBirth = Date.valueOf("2003-10-16");
        Date joinDate = Date.valueOf("2022-09-14");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("first_name")).thenReturn("John");
        when(mockResultSet.getString("last_name")).thenReturn("Smith");
        when(mockResultSet.getString("email")).thenReturn("johnsmith@gmail.com");
        when(mockResultSet.getDate("date_of_birth")).thenReturn(dateOfBirth);
        when(mockResultSet.getDate("join_date")).thenReturn(joinDate);

        Optional<Student> result = StudentDAO.findByEmail(email);

        verify(mockPreparedStatement).setString(1, email);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Smith", result.get().getLastName());
        assertEquals("johnsmith@gmail.com", result.get().getEmail());
        assertEquals(dateOfBirth, result.get().getDateOfBirth());
        assertEquals(joinDate, result.get().getJoinDate());
    }

    @Test
    public void testFindAll() throws SQLException {
        Date dateOfBirth1 = Date.valueOf("2003-10-16");
        Date joinDate1 = Date.valueOf("2022-09-14");
        Date dateOfBirth2 = Date.valueOf("1962-07-03");
        Date joinDate2 = Date.valueOf("2018-09-13");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("first_name")).thenReturn("John", "Tom");
        when(mockResultSet.getString("last_name")).thenReturn("Smith", "Cruise");
        when(mockResultSet.getString("email")).thenReturn("johnsmith@gmail.com", "tomcruise@gmail.com");
        when(mockResultSet.getDate("date_of_birth")).thenReturn(dateOfBirth1, dateOfBirth2);
        when(mockResultSet.getDate("join_date")).thenReturn(joinDate1, joinDate2);

        List<Student> students = StudentDAO.findAll();

        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getString("first_name");
        verify(mockResultSet, times(2)).getString("last_name");
        verify(mockResultSet, times(2)).getString("email");
        verify(mockResultSet, times(2)).getDate("date_of_birth");
        verify(mockResultSet, times(2)).getDate("join_date");

        assertEquals(2, students.size());
        assertEquals(1, students.get(0).getId());
        assertEquals("John", students.get(0).getFirstName());
        assertEquals("Smith", students.get(0).getLastName());
        assertEquals("johnsmith@gmail.com", students.get(0).getEmail());
        assertEquals(dateOfBirth1, students.get(0).getDateOfBirth());
        assertEquals(joinDate1, students.get(0).getJoinDate());

        assertEquals(2, students.get(1).getId());
        assertEquals("Tom", students.get(1).getFirstName());
        assertEquals("Cruise", students.get(1).getLastName());
        assertEquals("tomcruise@gmail.com", students.get(1).getEmail());
        assertEquals(dateOfBirth2, students.get(1).getDateOfBirth());
        assertEquals(joinDate2, students.get(1).getJoinDate());
    }

    @Test
    public void testUpdateStudent() throws SQLException {
        Date dateOfBirth = Date.valueOf("2003-10-16");
        Date joinDate = Date.valueOf("2022-09-14");
        Student student = new Student(1, "John", "Smith", "johnsmith@gmail.com", dateOfBirth, joinDate);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = StudentDAO.update(student);

        assertEquals(1, result);
        verify(mockPreparedStatement).setString(1, student.getFirstName());
        verify(mockPreparedStatement).setString(2, student.getLastName());
        verify(mockPreparedStatement).setString(3, student.getEmail());
        verify(mockPreparedStatement).setDate(4, student.getDateOfBirth());
        verify(mockPreparedStatement).setDate(5, student.getJoinDate());
        verify(mockPreparedStatement).setInt(6, student.getId());
    }

    @Test
    public void testDeleteStudent() throws SQLException {
        int studentId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = StudentDAO.delete(studentId);

        assertEquals(1, result);
        verify(mockPreparedStatement).setInt(1, studentId);
    }
}
