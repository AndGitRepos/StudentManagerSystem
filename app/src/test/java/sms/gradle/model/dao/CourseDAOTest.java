package sms.gradle.model.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import sms.gradle.model.entities.Course;

public class CourseDAOTest {

    @Mock
    private DatabaseConnection mockDbConnection;

    private MockedStatic<DatabaseConnection> mockedStatic;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(DatabaseConnection.class);
        mockedStatic.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
    }

    @After
    public void tearDown() {
        mockedStatic.close();
    }

    @Test
    public void testAddCourse() throws SQLException {
        Course course = new Course(0, "Intro to Programming", "Coding");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        CourseDAO.addCourse(course);

        verify(mockPreparedStatement).setString(1, course.getName());
        verify(mockPreparedStatement).setString(2, course.getDescription());
    }

    @Test
    public void testFindById() throws SQLException {
        int courseId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Computer Science");
        when(mockResultSet.getString("description")).thenReturn("Computers");

        Optional<Course> result = CourseDAO.findById(courseId);

        verify(mockPreparedStatement).setInt(1, courseId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Computer Science", result.get().getName());
        assertEquals("Computers", result.get().getDescription());
    }

    @Test
    public void testFindByName() throws SQLException {
        String courseName = "Computer Science";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Computer Science");
        when(mockResultSet.getString("description")).thenReturn("Computers");

        Optional<Course> result = CourseDAO.findByName(courseName);

        verify(mockPreparedStatement).setString(1, courseName);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Computer Science", result.get().getName());
        assertEquals("Computers", result.get().getDescription());
    }

    @Test
    public void testFindAll() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("Computer Science", "Mathematics");
        when(mockResultSet.getString("description")).thenReturn("Computers", "Addition");

        List<Course> courses = CourseDAO.findAll();

        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getString("name");
        verify(mockResultSet, times(2)).getString("description");

        assertEquals(2, courses.size());
        assertEquals(1, courses.get(0).getId());
        assertEquals("Computer Science", courses.get(0).getName());
        assertEquals("Computers", courses.get(0).getDescription());
        assertEquals(2, courses.get(1).getId());
        assertEquals("Mathematics", courses.get(1).getName());
        assertEquals("Addition", courses.get(1).getDescription());
    }

    @Test
    public void testUpdateCourse() throws SQLException {
        Course course = new Course(1, "name", "description");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = CourseDAO.update(course);

        assertEquals(1, result);
        verify(mockPreparedStatement).setString(1, course.getName());
        verify(mockPreparedStatement).setString(2, course.getDescription());
        verify(mockPreparedStatement).setInt(3, course.getId());
    }

    @Test
    public void testDeleteCourse() throws SQLException {
        int courseId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = CourseDAO.delete(courseId);

        assertEquals(1, result);
        verify(mockPreparedStatement).setInt(1, courseId);
    }
}
