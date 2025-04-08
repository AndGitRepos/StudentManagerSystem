package sms.gradle.utils;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import sms.gradle.model.dao.*;
import sms.gradle.model.entities.*;
import sms.gradle.model.entities.Module;

public final class Common {
    private static final int NUMBER_OF_MODULES_PER_COURSE = 5;
    private static final int NUMBER_OF_ASSESSMENTS_PER_MODULE = 3;
    private static final int NUMBER_OF_ENROLLMENTS_PER_STUDENT = 2;

    private static final int NUMBER_OF_STUDENTS = 10;
    private static final int NUMBER_OF_COURSES = DatabaseTableData.COURSE_NAMES.size();
    private static final int NUMBER_OF_MODULES = NUMBER_OF_COURSES * NUMBER_OF_MODULES_PER_COURSE;
    private static final int NUMBER_OF_ASSESSMENTS = NUMBER_OF_ASSESSMENTS_PER_MODULE * NUMBER_OF_MODULES;

    private Common() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String generateSha256Hash(final String input) {
        return Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
    }

    public static void populateTables() throws SQLException {
        SecureRandom random = new SecureRandom();
        for (int studentNum = 0; studentNum < NUMBER_OF_STUDENTS; studentNum++) {
            String firstName = DatabaseTableData.FIRST_NAMES.get(random.nextInt(DatabaseTableData.FIRST_NAMES.size()));
            String lastName = DatabaseTableData.LAST_NAMES.get(random.nextInt(DatabaseTableData.LAST_NAMES.size()));
            StudentDAO.addStudent(
                    new Student(
                            0,
                            firstName,
                            lastName,
                            "student" + studentNum + "@sms.com",
                            new Date(System.currentTimeMillis()),
                            new Date(System.currentTimeMillis())),
                    Common.generateSha256Hash(firstName + lastName));
        }

        for (int courseNum = 0; courseNum < NUMBER_OF_COURSES; courseNum++) {
            CourseDAO.addCourse(
                    new Course(0, DatabaseTableData.COURSE_NAMES.get(courseNum), "Description" + courseNum));
        }

        for (int moduleNum = 1; moduleNum <= NUMBER_OF_MODULES; moduleNum++) {
            int courseId = Math.ceilDiv(moduleNum, NUMBER_OF_MODULES_PER_COURSE);
            ModuleDAO.addModule(new sms.gradle.model.entities.Module(
                    0,
                    DatabaseTableData.MODULE_NAMES.get(random.nextInt(DatabaseTableData.MODULE_NAMES.size())),
                    "Description" + moduleNum,
                    DatabaseTableData.LAST_NAMES.get(random.nextInt(DatabaseTableData.LAST_NAMES.size())),
                    courseId));
        }

        for (int assessmentNum = 1; assessmentNum <= NUMBER_OF_ASSESSMENTS; assessmentNum++) {
            int moduleId = Math.ceilDiv(assessmentNum, NUMBER_OF_ASSESSMENTS_PER_MODULE);
            AssessmentDAO.addAssessment(new Assessment(
                    0,
                    "Assessment" + assessmentNum,
                    "Description" + assessmentNum,
                    new Date(System.currentTimeMillis()),
                    moduleId));
        }

        for (int enrollmentNum = 1;
                enrollmentNum <= NUMBER_OF_ENROLLMENTS_PER_STUDENT * NUMBER_OF_STUDENTS;
                enrollmentNum++) {
            int studentId = Math.ceilDiv(enrollmentNum, NUMBER_OF_ENROLLMENTS_PER_STUDENT);
            int courseId = random.nextInt(NUMBER_OF_COURSES) + 1;
            CourseEnrollmentDAO.addCourseEnrollment(
                    new CourseEnrollment(0, studentId, courseId, new Date(System.currentTimeMillis())));

            List<sms.gradle.model.entities.Module> modules = ModuleDAO.findByCourseId(courseId);
            for (Module module : modules) {
                List<Assessment> assessments = AssessmentDAO.findByModuleId(module.getId());
                for (Assessment assessment : assessments) {
                    if (random.nextInt(10) > 3) {
                        continue;
                    }
                    ResultDAO.addResult(new Result(0, studentId, assessment.getId(), random.nextInt(100)));
                }
            }
        }
    }
}
