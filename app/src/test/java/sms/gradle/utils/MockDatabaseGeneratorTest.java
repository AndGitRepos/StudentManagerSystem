package sms.gradle.utils;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import sms.gradle.model.dao.AssessmentDAO;
import sms.gradle.model.dao.CourseDAO;
import sms.gradle.model.dao.CourseEnrollmentDAO;
import sms.gradle.model.dao.ModuleDAO;
import sms.gradle.model.dao.ResultDAO;
import sms.gradle.model.dao.StudentDAO;
import sms.gradle.model.entities.Assessment;
import sms.gradle.model.entities.Course;
import sms.gradle.model.entities.CourseEnrollment;
import sms.gradle.model.entities.Module;
import sms.gradle.model.entities.Student;

public class MockDatabaseGeneratorTest {
    @Test
    public void testPopulateTablesWithExistingData() throws SQLException {
        try (MockedStatic<StudentDAO> mockedStudentDAO = mockStatic(StudentDAO.class);
                MockedStatic<CourseDAO> mockedCourseDAO = mockStatic(CourseDAO.class);
                MockedStatic<ModuleDAO> mockedModuleDAO = mockStatic(ModuleDAO.class);
                MockedStatic<AssessmentDAO> mockedAssessmentDAO = mockStatic(AssessmentDAO.class);
                MockedStatic<CourseEnrollmentDAO> mockedCourseEnrollmentDAO = mockStatic(CourseEnrollmentDAO.class);
                MockedStatic<ResultDAO> mockedResultDAO = mockStatic(ResultDAO.class)) {

            // Mock existing students
            List<Student> existingStudents = new ArrayList<>();
            Student existingStudent = new Student(
                    1,
                    "John",
                    "Doe",
                    "student5@sms.com",
                    new Date(System.currentTimeMillis()),
                    new Date(System.currentTimeMillis()));
            existingStudents.add(existingStudent);
            mockedStudentDAO.when(StudentDAO::findAll).thenReturn(existingStudents);

            // Mock student email check
            mockedStudentDAO.when(() -> StudentDAO.findByEmail(anyString())).thenReturn(Optional.empty());
            mockedStudentDAO
                    .when(() -> StudentDAO.findByEmail("student5@sms.com"))
                    .thenReturn(Optional.of(existingStudent));

            // Mock course operations
            List<Course> existingCourses = new ArrayList<>();
            Course existingCourse = new Course(1, "Computer Science", "Description0");
            existingCourses.add(existingCourse);
            mockedCourseDAO.when(CourseDAO::findAll).thenReturn(existingCourses);
            mockedCourseDAO.when(() -> CourseDAO.findByName(anyString())).thenReturn(Optional.empty());
            mockedCourseDAO
                    .when(() -> CourseDAO.findByName("Computer Science"))
                    .thenReturn(Optional.of(existingCourse));

            // Mock module operations
            List<Module> existingModules = new ArrayList<>();
            Module existingModule = new Module(1, "Introduction", "Description-1", "Smith", 1);
            existingModules.add(existingModule);
            mockedModuleDAO.when(ModuleDAO::findAll).thenReturn(existingModules);
            mockedModuleDAO.when(() -> ModuleDAO.findByCourseId(anyInt())).thenReturn(existingModules);

            // Mock assessment operations
            List<Assessment> existingAssessments = new ArrayList<>();
            Assessment existingAssessment =
                    new Assessment(1, "Assessment-1-1", "Description", new Date(System.currentTimeMillis()), 1);
            existingAssessments.add(existingAssessment);
            mockedAssessmentDAO
                    .when(() -> AssessmentDAO.findByModuleId(anyInt()))
                    .thenReturn(existingAssessments);

            // Mock enrollment operations
            List<CourseEnrollment> existingEnrollments = new ArrayList<>();
            CourseEnrollment existingEnrollment = new CourseEnrollment(1, 1, 1, new Date(System.currentTimeMillis()));
            existingEnrollments.add(existingEnrollment);
            mockedCourseEnrollmentDAO
                    .when(() -> CourseEnrollmentDAO.findByStudentId(anyInt()))
                    .thenReturn(existingEnrollments);

            // Mock result operations
            mockedResultDAO
                    .when(() -> ResultDAO.findByStudentAndAssessment(anyInt(), anyInt()))
                    .thenReturn(Optional.empty());

            // Execute the method
            MockDatabaseGenerator.populateTables();

            // Verify that the method checked for existing data
            mockedStudentDAO.verify(StudentDAO::findAll, times(2));
            mockedCourseDAO.verify(CourseDAO::findAll, times(2));
            mockedModuleDAO.verify(ModuleDAO::findAll);

            // Verify that the method checked for existing student emails
            mockedStudentDAO.verify(() -> StudentDAO.findByEmail("student6@sms.com"));

            // Verify that the method checked for existing courses
            mockedCourseDAO.verify(() -> CourseDAO.findByName("Computer Science"));
        }
    }
}
