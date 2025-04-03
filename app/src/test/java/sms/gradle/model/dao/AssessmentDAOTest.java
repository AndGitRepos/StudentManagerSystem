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
import sms.gradle.model.entities.Assessment;

public class AssessmentDAOTest {
    @Mock
    private DatabaseConnection mockDbConnection;

    private MockedStatic<DatabaseConnection> mockedStatic;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private final Date dueDate = java.sql.Date.valueOf("2025-10-10");

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
    public void testAddAssessment() throws SQLException {
        Assessment assessment = new Assessment(1, "Test Assessment", "Test Description", dueDate, 2);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        AssessmentDAO.addAssessment(assessment);

        verify(mockPreparedStatement).setString(1, assessment.getName());
        verify(mockPreparedStatement).setString(2, assessment.getDescription());
        verify(mockPreparedStatement).setDate(3, assessment.getDueDate());
        verify(mockPreparedStatement).setInt(4, assessment.getModuleId());
    }

    @Test
    public void testFindById() throws SQLException {
        int assessmentId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(assessmentId);
        when(mockResultSet.getString("name")).thenReturn("Test Assessment");
        when(mockResultSet.getString("description")).thenReturn("Test Description");
        when(mockResultSet.getDate("due_date")).thenReturn(dueDate);
        when(mockResultSet.getInt("module_id")).thenReturn(2);

        Optional<Assessment> result = AssessmentDAO.findById(assessmentId);

        verify(mockPreparedStatement).setInt(1, assessmentId);
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
        verify(mockResultSet).getString("name");
        verify(mockResultSet).getString("description");
        verify(mockResultSet).getDate("due_date");
        verify(mockResultSet).getInt("module_id");

        assertTrue(result.isPresent());
        assertEquals(assessmentId, result.get().getId());
        assertEquals("Test Assessment", result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals(dueDate, result.get().getDueDate());
        assertEquals(2, result.get().getModuleId());
    }

    @Test
    public void testFindByName() throws SQLException {
        String assessmentName = "Test Assessment";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn(assessmentName);
        when(mockResultSet.getString("description")).thenReturn("Test Description");
        when(mockResultSet.getDate("due_date")).thenReturn(dueDate);
        when(mockResultSet.getInt("module_id")).thenReturn(2);

        Optional<Assessment> result = AssessmentDAO.findByName(assessmentName);

        verify(mockPreparedStatement).setString(1, assessmentName);
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
        verify(mockResultSet).getString("name");
        verify(mockResultSet).getString("description");
        verify(mockResultSet).getDate("due_date");
        verify(mockResultSet).getInt("module_id");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals(assessmentName, result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals(dueDate, result.get().getDueDate());
        assertEquals(2, result.get().getModuleId());
    }

    @Test
    public void testFindByDueDate() throws SQLException {
        Date dueDate = this.dueDate;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Module");
        when(mockResultSet.getString("description")).thenReturn("Test Description");
        when(mockResultSet.getDate("due_date")).thenReturn(dueDate);
        when(mockResultSet.getInt("module_id")).thenReturn(2);

        Optional<Assessment> result = AssessmentDAO.findByDueDate(dueDate);

        verify(mockPreparedStatement).setDate(1, dueDate);
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
        verify(mockResultSet).getString("name");
        verify(mockResultSet).getString("description");
        verify(mockResultSet).getDate("due_date");
        verify(mockResultSet).getInt("module_id");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Test Module", result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals(dueDate, result.get().getDueDate());
        assertEquals(2, result.get().getModuleId());
    }

    @Test
    public void testFindByModuleId() throws SQLException {
        int moduleId = 2;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Assessment");
        when(mockResultSet.getString("description")).thenReturn("Test Description");
        when(mockResultSet.getDate("due_date")).thenReturn(dueDate);
        when(mockResultSet.getInt("module_id")).thenReturn(moduleId);

        Optional<Assessment> result = AssessmentDAO.findByModuleId(moduleId);

        verify(mockPreparedStatement).setInt(1, moduleId);
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
        verify(mockResultSet).getString("name");
        verify(mockResultSet).getString("description");
        verify(mockResultSet).getDate("due_date");
        verify(mockResultSet).getInt("module_id");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Test Assessment", result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals(dueDate, result.get().getDueDate());
        assertEquals(moduleId, result.get().getModuleId());
    }

    @Test
    public void testFindAll() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("Test Module One", "Test Module Two");
        when(mockResultSet.getString("description")).thenReturn("Test Description One", "Test Description Two");
        when(mockResultSet.getDate("due_date")).thenReturn(dueDate, dueDate);
        when(mockResultSet.getInt("module_id")).thenReturn(2, 4);

        List<Assessment> results = AssessmentDAO.findAll();

        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getString("name");
        verify(mockResultSet, times(2)).getString("description");
        verify(mockResultSet, times(2)).getDate("due_date");
        verify(mockResultSet, times(2)).getInt("module_id");

        assertEquals(2, results.size());
        assertEquals(1, results.get(0).getId());
        assertEquals("Test Module One", results.get(0).getName());
        assertEquals("Test Description One", results.get(0).getDescription());
        assertEquals(dueDate, results.get(0).getDueDate());
        assertEquals(2, results.get(0).getModuleId());

        assertEquals(2, results.get(1).getId());
        assertEquals("Test Module Two", results.get(1).getName());
        assertEquals("Test Description Two", results.get(1).getDescription());
        assertEquals(dueDate, results.get(1).getDueDate());
        assertEquals(4, results.get(1).getModuleId());
    }

    @Test
    public void testUpdate() throws SQLException {
        Assessment assessment = new Assessment(1, "Test Assessment", "Test Description", dueDate, 2);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = AssessmentDAO.update(assessment);

        verify(mockPreparedStatement).setString(1, assessment.getName());
        verify(mockPreparedStatement).setString(2, assessment.getDescription());
        verify(mockPreparedStatement).setDate(3, assessment.getDueDate());
        verify(mockPreparedStatement).setInt(4, assessment.getModuleId());
        verify(mockPreparedStatement).setInt(5, assessment.getId());

        assertEquals(1, result);
    }

    @Test
    public void testDelete() throws SQLException {
        int assessmentId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = AssessmentDAO.delete(assessmentId);

        verify(mockPreparedStatement).setInt(1, assessmentId);

        assertEquals(1, result);
    }
}
