package sms.gradle.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import sms.gradle.model.entities.Admin;

public class AdminDAOTest {

    @Mock
    private DatabaseConnection mockDbConnection;

    private MockedStatic<DatabaseConnection> mockStaticDbConnection;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockStaticDbConnection = mockStatic(DatabaseConnection.class);
        mockStaticDbConnection.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() {
        mockStaticDbConnection.close();
    }

    @Test
    public void testAddAdmin() throws SQLException {
        Admin admin = new Admin(0, "James", "Bond", "jamesbond@gmail.com");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        AdminDAO.addAdmin(admin, "AHashedPassword");

        verify(mockPreparedStatement).setString(1, admin.getFirstName());
        verify(mockPreparedStatement).setString(2, admin.getLastName());
        verify(mockPreparedStatement).setString(3, admin.getEmail());
        verify(mockPreparedStatement).setString(4, "AHashedPassword");
    }

    @Test
    public void testFindById() throws SQLException {
        int adminId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("first_name")).thenReturn("James");
        when(mockResultSet.getString("last_name")).thenReturn("Bond");
        when(mockResultSet.getString("email")).thenReturn("jamesbond@gmail.com");

        Optional<Admin> result = AdminDAO.findById(adminId);

        verify(mockPreparedStatement).setInt(1, adminId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("James", result.get().getFirstName());
        assertEquals("Bond", result.get().getLastName());
        assertEquals("jamesbond@gmail.com", result.get().getEmail());
    }

    @Test
    public void testFindByEmail() throws SQLException {
        String email = "jamesbond@gmail.com";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("first_name")).thenReturn("James");
        when(mockResultSet.getString("last_name")).thenReturn("Bond");
        when(mockResultSet.getString("email")).thenReturn("jamesbond@gmail.com");

        Optional<Admin> result = AdminDAO.findByEmail(email);

        verify(mockPreparedStatement).setString(1, email);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("James", result.get().getFirstName());
        assertEquals("Bond", result.get().getLastName());
        assertEquals("jamesbond@gmail.com", result.get().getEmail());
    }

    @Test
    public void testFindAll() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("first_name")).thenReturn("James", "Will");
        when(mockResultSet.getString("last_name")).thenReturn("Bond", "Byers");
        when(mockResultSet.getString("email")).thenReturn("jamesbond@gmail.com", "willbyers@gmail.com");

        List<Admin> admins = AdminDAO.findAll();

        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getString("first_name");
        verify(mockResultSet, times(2)).getString("last_name");
        verify(mockResultSet, times(2)).getString("email");

        assertEquals(2, admins.size());
        assertEquals(1, admins.get(0).getId());
        assertEquals("James", admins.get(0).getFirstName());
        assertEquals("Bond", admins.get(0).getLastName());
        assertEquals("jamesbond@gmail.com", admins.get(0).getEmail());

        assertEquals(2, admins.get(1).getId());
        assertEquals("Will", admins.get(1).getFirstName());
        assertEquals("Byers", admins.get(1).getLastName());
        assertEquals("willbyers@gmail.com", admins.get(1).getEmail());
    }

    @Test
    public void testUpdateAdmin() throws SQLException {
        Admin admin = new Admin(1, "James", "Bond", "jamesbond@gmail.com");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = AdminDAO.update(admin);

        assertEquals(1, result);
        verify(mockPreparedStatement).setString(1, admin.getFirstName());
        verify(mockPreparedStatement).setString(2, admin.getLastName());
        verify(mockPreparedStatement).setString(3, admin.getEmail());
        verify(mockPreparedStatement).setInt(4, admin.getId());
    }

    @Test
    public void testDeleteAdmin() throws SQLException {
        int adminId = 1;
        when(mockConnection.prepareStatement("SELECT COUNT(*) FROM admins")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(2);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        int result = AdminDAO.delete(adminId);

        assertEquals(1, result);
        verify(mockPreparedStatement).setInt(1, adminId);
    }
}
