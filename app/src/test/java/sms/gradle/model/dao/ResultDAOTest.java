package sms.gradle.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
import sms.gradle.model.entities.Result;

public class ResultDAOTest {
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
    public void testAddResult() throws SQLException {
        Result result = new Result(1, 9, 4, 90);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        ResultDAO.addResult(result);

        verify(mockPreparedStatement).setInt(1, result.getStudentId());
        verify(mockPreparedStatement).setInt(2, result.getAssessmentId());
        verify(mockPreparedStatement).setInt(3, result.getGrade());
    }

    @Test
    public void testFindById() throws SQLException {
        int resultId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("student_id")).thenReturn(9);
        when(mockResultSet.getInt("assessment_id")).thenReturn(4);
        when(mockResultSet.getInt("grade")).thenReturn(90);

        Optional<Result> result = ResultDAO.findById(resultId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals(9, result.get().getStudentId());
        assertEquals(4, result.get().getAssessmentId());
        assertEquals(90, result.get().getGrade());
    }

    @Test
    public void testFindByStudentId() throws SQLException {
        int studentId = 2;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);

        when(mockResultSet.getInt("id")).thenReturn(1, 9);
        when(mockResultSet.getInt("student_id")).thenReturn(2, 2);
        when(mockResultSet.getInt("assessment_id")).thenReturn(4, 6);
        when(mockResultSet.getInt("grade")).thenReturn(90, 84);

        List<Result> results = ResultDAO.findByStudentId(studentId);

        verify(mockPreparedStatement).setInt(1, studentId);

        assertEquals(2, results.size());
        assertEquals(90, results.get(0).getGrade());
        assertEquals(84, results.get(1).getGrade());
    }

    @Test
    public void testFindByAssessmentId() throws SQLException {
        int assessmentId = 3;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);

        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("student_id")).thenReturn(4);
        when(mockResultSet.getInt("assessment_id")).thenReturn(3);
        when(mockResultSet.getInt("grade")).thenReturn(95);

        List<Result> results = ResultDAO.findByAssessmentId(assessmentId);

        verify(mockPreparedStatement).setInt(1, assessmentId);

        assertEquals(1, results.size());
        assertEquals(95, results.get(0).getGrade());
    }

    @Test
    public void testFindAll() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);

        when(mockResultSet.getInt("id")).thenReturn(1, 4);
        when(mockResultSet.getInt("student_id")).thenReturn(7, 4);
        when(mockResultSet.getInt("assessment_id")).thenReturn(6, 3);
        when(mockResultSet.getInt("grade")).thenReturn(75, 95);

        List<Result> results = ResultDAO.findAll();

        assertEquals(2, results.size());
        assertEquals(75, results.get(0).getGrade());
        assertEquals(95, results.get(1).getGrade());
    }

    @Test
    public void testUpdate() throws SQLException {
        Result result = new Result(1, 5, 7, 80);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int updateResult = ResultDAO.update(result);

        assertEquals(1, updateResult);

        verify(mockPreparedStatement).setInt(1, result.getStudentId());
        verify(mockPreparedStatement).setInt(2, result.getAssessmentId());
        verify(mockPreparedStatement).setInt(3, result.getGrade());
        verify(mockPreparedStatement).setInt(4, result.getId());
    }

    @Test
    public void testDelete() throws SQLException {
        int resultId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int deleteResult = ResultDAO.delete(resultId);

        verify(mockPreparedStatement).setInt(1, resultId);

        assertEquals(1, deleteResult);
    }
}
