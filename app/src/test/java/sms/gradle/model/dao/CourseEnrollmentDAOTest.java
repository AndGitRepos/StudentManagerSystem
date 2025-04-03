package sms.gradle.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import sms.gradle.model.entities.CourseEnrollment;

public class CourseEnrollmentDAOTest {
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
    public void testAddCourseEnrollment() throws SQLException {
        CourseEnrollment courseEnrollment = new CourseEnrollment(1, 2, 3, new Date(5000));
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        CourseEnrollmentDAO.addCourseEnrollment(courseEnrollment);

        verify(mockPreparedStatement).setInt(1, courseEnrollment.getStudentId());
        verify(mockPreparedStatement).setInt(2, courseEnrollment.getCourseId());
        verify(mockPreparedStatement).setDate(3, courseEnrollment.getEnrollmentDate());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testFindById() throws SQLException {
        int enrollmentId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("student_id")).thenReturn(2);
        when(mockResultSet.getInt("course_id")).thenReturn(3);
        when(mockResultSet.getDate("enrollment_date")).thenReturn(new Date(5000));

        Optional<CourseEnrollment> result = CourseEnrollmentDAO.findById(enrollmentId);

        verify(mockPreparedStatement).setInt(1, enrollmentId);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
        verify(mockResultSet).getInt("student_id");
        verify(mockResultSet).getInt("course_id");
        verify(mockResultSet).getDate("enrollment_date");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals(2, result.get().getStudentId());
        assertEquals(3, result.get().getCourseId());
        assertEquals(new Date(5000), result.get().getEnrollmentDate());
    }

    @Test
    public void testFindByStudentId() throws SQLException {
        int studentId = 2;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getInt("student_id")).thenReturn(2, 2);
        when(mockResultSet.getInt("course_id")).thenReturn(3, 5);
        when(mockResultSet.getDate("enrollment_date")).thenReturn(new Date(5000), new Date(6000));

        List<CourseEnrollment> result = CourseEnrollmentDAO.findByStudentId(studentId);

        verify(mockPreparedStatement).setInt(1, studentId);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getInt("student_id");
        verify(mockResultSet, times(2)).getInt("course_id");
        verify(mockResultSet, times(2)).getDate("enrollment_date");

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(0).getStudentId());
        assertEquals(3, result.get(0).getCourseId());
        assertEquals(new Date(5000), result.get(0).getEnrollmentDate());

        assertEquals(2, result.get(1).getId());
        assertEquals(2, result.get(1).getStudentId());
        assertEquals(5, result.get(1).getCourseId());
        assertEquals(new Date(6000), result.get(1).getEnrollmentDate());
    }

    @Test
    public void testFindByCourseId() throws SQLException {
        int courseId = 3;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getInt("student_id")).thenReturn(2, 5);
        when(mockResultSet.getInt("course_id")).thenReturn(3, 3);
        when(mockResultSet.getDate("enrollment_date")).thenReturn(new Date(5000), new Date(6000));

        List<CourseEnrollment> result = CourseEnrollmentDAO.findByCourseId(courseId);

        verify(mockPreparedStatement).setInt(1, courseId);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getInt("student_id");
        verify(mockResultSet, times(2)).getInt("course_id");
        verify(mockResultSet, times(2)).getDate("enrollment_date");

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(0).getStudentId());
        assertEquals(3, result.get(0).getCourseId());
        assertEquals(new Date(5000), result.get(0).getEnrollmentDate());

        assertEquals(2, result.get(1).getId());
        assertEquals(5, result.get(1).getStudentId());
        assertEquals(3, result.get(1).getCourseId());
        assertEquals(new Date(6000), result.get(1).getEnrollmentDate());
    }

    @Test
    public void testFindAll() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getInt("student_id")).thenReturn(2, 5);
        when(mockResultSet.getInt("course_id")).thenReturn(10, 15);
        when(mockResultSet.getDate("enrollment_date")).thenReturn(new Date(5000), new Date(6000));

        List<CourseEnrollment> result = CourseEnrollmentDAO.findAll();

        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getInt("student_id");
        verify(mockResultSet, times(2)).getInt("course_id");
        verify(mockResultSet, times(2)).getDate("enrollment_date");

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(0).getStudentId());
        assertEquals(10, result.get(0).getCourseId());
        assertEquals(new Date(5000), result.get(0).getEnrollmentDate());

        assertEquals(2, result.get(1).getId());
        assertEquals(5, result.get(1).getStudentId());
        assertEquals(15, result.get(1).getCourseId());
        assertEquals(new Date(6000), result.get(1).getEnrollmentDate());
    }

    @Test
    public void testUpdate() throws SQLException {
        CourseEnrollment courseEnrollment = new CourseEnrollment(1, 2, 3, new Date(5000));
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = CourseEnrollmentDAO.update(courseEnrollment);

        verify(mockPreparedStatement).setInt(1, courseEnrollment.getStudentId());
        verify(mockPreparedStatement).setInt(2, courseEnrollment.getCourseId());
        verify(mockPreparedStatement).setDate(3, courseEnrollment.getEnrollmentDate());
        verify(mockPreparedStatement).setInt(4, courseEnrollment.getId());
        verify(mockPreparedStatement).executeUpdate();

        assertEquals(1, result);
    }

    @Test
    public void testDelete() throws SQLException {
        int enrollmentId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = CourseEnrollmentDAO.delete(enrollmentId);

        verify(mockPreparedStatement).setInt(1, enrollmentId);
        verify(mockPreparedStatement).executeUpdate();

        assertEquals(1, result);
    }
}
