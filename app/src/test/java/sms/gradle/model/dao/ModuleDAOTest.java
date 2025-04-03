package sms.gradle.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
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
import sms.gradle.model.entities.Module;

public class ModuleDAOTest {
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
    public void testAddModule() throws SQLException {
        Module module = new Module(1, "Test Module", "Test Description", "Test Lecturer", 2);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        ModuleDAO.addModule(module);

        verify(mockPreparedStatement).setString(1, module.getName());
        verify(mockPreparedStatement).setString(2, module.getDescription());
        verify(mockPreparedStatement).setString(3, module.getLecturer());
        verify(mockPreparedStatement).setInt(4, module.getCourseId());
    }

    @Test
    public void testFindById() throws SQLException {
        int moduleId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(moduleId);
        when(mockResultSet.getString("name")).thenReturn("Test Module");
        when(mockResultSet.getString("description")).thenReturn("Test Description");
        when(mockResultSet.getString("lecturer")).thenReturn("Test Lecturer");
        when(mockResultSet.getInt("course_id")).thenReturn(2);

        Optional<Module> result = ModuleDAO.findById(moduleId);

        verify(mockPreparedStatement).setInt(1, moduleId);
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
        verify(mockResultSet).getString("name");
        verify(mockResultSet).getString("description");
        verify(mockResultSet).getString("lecturer");
        verify(mockResultSet).getInt("course_id");

        assertTrue(result.isPresent());
        assertEquals(moduleId, result.get().getId());
        assertEquals("Test Module", result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals("Test Lecturer", result.get().getLecturer());
        assertEquals(2, result.get().getCourseId());
    }

    @Test
    public void testFindByName() throws SQLException {
        String moduleName = "Test Module";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn(moduleName);
        when(mockResultSet.getString("description")).thenReturn("Test Description");
        when(mockResultSet.getString("lecturer")).thenReturn("Test Lecturer");
        when(mockResultSet.getInt("course_id")).thenReturn(2);

        Optional<Module> result = ModuleDAO.findByName(moduleName);

        verify(mockPreparedStatement).setString(1, moduleName);
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
        verify(mockResultSet).getString("name");
        verify(mockResultSet).getString("description");
        verify(mockResultSet).getString("lecturer");
        verify(mockResultSet).getInt("course_id");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals(moduleName, result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals("Test Lecturer", result.get().getLecturer());
        assertEquals(2, result.get().getCourseId());
    }

    @Test
    public void testFindByLecturer() throws SQLException {
        String lecturerName = "Test Lecturer";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Module");
        when(mockResultSet.getString("description")).thenReturn("Test Description");
        when(mockResultSet.getString("lecturer")).thenReturn(lecturerName);
        when(mockResultSet.getInt("course_id")).thenReturn(2);

        Optional<Module> result = ModuleDAO.findByLecturer(lecturerName);

        verify(mockPreparedStatement).setString(1, lecturerName);
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
        verify(mockResultSet).getString("name");
        verify(mockResultSet).getString("description");
        verify(mockResultSet).getString("lecturer");
        verify(mockResultSet).getInt("course_id");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Test Module", result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals(lecturerName, result.get().getLecturer());
        assertEquals(2, result.get().getCourseId());
    }

    @Test
    public void testFindByCourseId() throws SQLException {
        int courseId = 2;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Module");
        when(mockResultSet.getString("description")).thenReturn("Test Description");
        when(mockResultSet.getString("lecturer")).thenReturn("Test Lecturer");
        when(mockResultSet.getInt("course_id")).thenReturn(courseId);

        Optional<Module> result = ModuleDAO.findByCourseId(courseId);

        verify(mockPreparedStatement).setInt(1, courseId);
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
        verify(mockResultSet).getString("name");
        verify(mockResultSet).getString("description");
        verify(mockResultSet).getString("lecturer");
        verify(mockResultSet).getInt("course_id");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Test Module", result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals("Test Lecturer", result.get().getLecturer());
        assertEquals(courseId, result.get().getCourseId());
    }

    @Test
    public void testFindAll() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("Test Module One", "Test Module Two");
        when(mockResultSet.getString("description")).thenReturn("Test Description One", "Test Description Two");
        when(mockResultSet.getString("lecturer")).thenReturn("Test Lecturer One", "Test Lecturer Two");
        when(mockResultSet.getInt("course_id")).thenReturn(2, 4);

        List<Module> results = ModuleDAO.findAll();

        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getString("name");
        verify(mockResultSet, times(2)).getString("description");
        verify(mockResultSet, times(2)).getString("lecturer");
        verify(mockResultSet, times(2)).getInt("course_id");

        assertEquals(2, results.size());
        assertEquals(1, results.get(0).getId());
        assertEquals("Test Module One", results.get(0).getName());
        assertEquals("Test Description One", results.get(0).getDescription());
        assertEquals("Test Lecturer One", results.get(0).getLecturer());
        assertEquals(2, results.get(0).getCourseId());

        assertEquals(2, results.get(1).getId());
        assertEquals("Test Module Two", results.get(1).getName());
        assertEquals("Test Description Two", results.get(1).getDescription());
        assertEquals("Test Lecturer Two", results.get(1).getLecturer());
        assertEquals(4, results.get(1).getCourseId());
    }

    @Test
    public void testUpdate() throws SQLException {
        Module module = new Module(1, "Test Module", "Test Description", "Test Lecturer", 2);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = ModuleDAO.update(module);

        verify(mockPreparedStatement).setString(1, module.getName());
        verify(mockPreparedStatement).setString(2, module.getDescription());
        verify(mockPreparedStatement).setString(3, module.getLecturer());
        verify(mockPreparedStatement).setInt(4, module.getCourseId());
        verify(mockPreparedStatement).setInt(5, module.getId());

        assertEquals(1, result);
    }

    @Test
    public void testDelete() throws SQLException {
        int moduleId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = ModuleDAO.delete(moduleId);

        verify(mockPreparedStatement).setInt(1, moduleId);

        assertEquals(1, result);
    }
}
