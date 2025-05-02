package sms.gradle.utils;

import java.security.SecureRandom;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import sms.gradle.model.entities.Result;
import sms.gradle.model.entities.Student;

public final class MockDatabaseGenerator {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final List<String> FIRST_NAMES = List.of(
            "John",
            "Jane",
            "Michael",
            "Emily",
            "David",
            "Sarah",
            "Robert",
            "Emma",
            "William",
            "Olivia",
            "James",
            "Ava",
            "Alexander",
            "Sophia",
            "Benjamin",
            "Isabella",
            "Daniel",
            "Mia",
            "Matthew",
            "Charlotte",
            "Ethan",
            "Abigail",
            "Joseph",
            "Harper",
            "Jackson",
            "Amelia",
            "Aiden",
            "Evelyn",
            "Samuel",
            "Elizabeth",
            "Lucas",
            "Sofia",
            "Henry",
            "Avery",
            "Sebastian",
            "Ella",
            "Owen",
            "Scarlett",
            "Gabriel",
            "Grace",
            "Carter",
            "Chloe",
            "Jayden",
            "Riley",
            "Wyatt",
            "Lily",
            "Luke",
            "Aria",
            "Dylan",
            "Zoey",
            "Levi",
            "Hannah",
            "Nathan",
            "Lillian",
            "Isaac",
            "Addison",
            "Julian",
            "Nora",
            "Elijah",
            "Audrey",
            "Liam",
            "Brooklyn",
            "Mason",
            "Savannah",
            "Logan",
            "Claire",
            "Oliver",
            "Skylar",
            "Caleb",
            "Eleanor");

    public static final List<String> LAST_NAMES = List.of(
            "Smith",
            "Johnson",
            "Williams",
            "Brown",
            "Jones",
            "Garcia",
            "Miller",
            "Davis",
            "Rodriguez",
            "Martinez",
            "Hernandez",
            "Lopez",
            "Gonzalez",
            "Wilson",
            "Anderson",
            "Thomas",
            "Taylor",
            "Moore",
            "Jackson",
            "Martin",
            "Lee",
            "Perez",
            "Thompson",
            "White",
            "Harris",
            "Sanchez",
            "Clark",
            "Ramirez",
            "Lewis",
            "Robinson",
            "Walker",
            "Young",
            "Allen",
            "King",
            "Wright",
            "Scott",
            "Torres",
            "Nguyen",
            "Hill",
            "Flores",
            "Green",
            "Adams",
            "Nelson",
            "Baker",
            "Hall",
            "Rivera",
            "Campbell",
            "Mitchell",
            "Carter",
            "Roberts",
            "Phillips",
            "Evans",
            "Turner",
            "Torres",
            "Parker",
            "Collins",
            "Edwards",
            "Stewart",
            "Flores",
            "Morris");

    public static final List<String> COURSE_NAMES = List.of(
            "Computer Science",
            "Mathematics",
            "Physics",
            "Chemistry",
            "Biology",
            "English",
            "History",
            "Geography",
            "Literature",
            "Art",
            "Music",
            "Physical Education",
            "Economics",
            "Psychology",
            "Sociology",
            "Political Science",
            "Philosophy",
            "Business",
            "Marketing",
            "Finance",
            "Information Technology",
            "Engineering",
            "Medicine",
            "Nursing",
            "Law",
            "Education",
            "Communication",
            "Environmental Science",
            "Agriculture",
            "Architecture",
            "Film Studies",
            "Culinary Arts",
            "Journalism",
            "Social Work",
            "Counseling",
            "Health Science",
            "Nutrition",
            "Dentistry",
            "Veterinary Medicine",
            "Pharmacy",
            "Astronomy",
            "Astrophysics",
            "Astrobiology",
            "Astrochemistry",
            "Astrogeology",
            "Astrology",
            "Astrocartography",
            "Astroentomology",
            "Astroethnology",
            "Astroephemery");

    public static final List<String> MODULE_NAMES = List.of(
            "Introduction",
            "Fundamentals",
            "Advanced",
            "Practical",
            "Theory",
            "Application",
            "Design",
            "Implementation",
            "Analysis",
            "Evaluation",
            "Management",
            "Strategy",
            "Planning",
            "Control",
            "Measurement",
            "Simulation",
            "Optimization",
            "Evaluation",
            "Innovation",
            "Integration",
            "Communication",
            "Collaboration",
            "Leadership",
            "Teamwork",
            "Problem Solving",
            "Critical Thinking",
            "Creativity",
            "Innovation",
            "Risk Management",
            "Quality Control",
            "Process Improvement",
            "Cost Analysis",
            "Budgeting",
            "Resource Management",
            "Supply Chain",
            "Logistics",
            "Inventory Management",
            "Order Fulfillment",
            "Warehouse Management",
            "Transportation Management",
            "Project Management",
            "Risk Management",
            "Compliance Management",
            "Regulatory Management",
            "Risk Assessment",
            "Risk Mitigation",
            "Risk Transfer",
            "Risk Communication",
            "Risk Awareness");

    private static final int NUMBER_OF_MODULES_PER_COURSE = 5;
    private static final int NUMBER_OF_ASSESSMENTS_PER_MODULE = 3;
    private static final int NUMBER_OF_ENROLLMENTS_PER_STUDENT = 2;

    private static final int NUMBER_OF_STUDENTS = 10;
    private static final int NUMBER_OF_COURSES = MockDatabaseGenerator.COURSE_NAMES.size();

    /**
     * Populates database tables with sample data, checking for existing data to avoid conflicts.
     * This method coordinates the population of all tables by calling specialized methods for each entity type.
     *
     * @throws SQLException if a database access error occurs
     */
    public static void populateTables() throws SQLException {
        LOGGER.info("Populating tables");

        populateStudents();
        populateCourses();
        populateModules();
        populateAssessments();
        populateEnrollmentsAndResults();

        LOGGER.info("Finished populating tables");
    }

    /**
     * Populates the students table with sample data, checking for existing emails to avoid conflicts.
     *
     * @throws SQLException if a database access error occurs
     */
    private static void populateStudents() throws SQLException {
        LOGGER.debug("Populating students table");
        SecureRandom random = new SecureRandom();

        // Find the highest student number from existing emails to avoid conflicts
        int startingStudentNum = getNextAvailableStudentNumber();

        for (int i = 0; i < NUMBER_OF_STUDENTS; i++) {
            int studentNum = startingStudentNum + i;
            String firstName =
                    MockDatabaseGenerator.FIRST_NAMES.get(random.nextInt(MockDatabaseGenerator.FIRST_NAMES.size()));
            String lastName =
                    MockDatabaseGenerator.LAST_NAMES.get(random.nextInt(MockDatabaseGenerator.LAST_NAMES.size()));
            String email = generateUniqueStudentEmail(studentNum);

            if (StudentDAO.findByEmail(email).isPresent()) {
                LOGGER.debug("Student with email {} already exists, skipping", email);
                continue;
            }

            StudentDAO.addStudent(
                    new Student(
                            0,
                            firstName,
                            lastName,
                            email,
                            new Date(System.currentTimeMillis()),
                            new Date(System.currentTimeMillis())),
                    Common.generateSha256Hash(firstName + lastName));
        }
        LOGGER.debug("Populated students table");
    }

    /**
     * Generates a unique student email based on a student number.
     *
     * @param studentNum The student number to use in the email
     * @return A unique email address
     */
    private static String generateUniqueStudentEmail(int studentNum) {
        return "student" + studentNum + "@sms.com";
    }

    /**
     * Determines the next available student number by analyzing existing email addresses.
     *
     * @return The next available student number
     * @throws SQLException if a database access error occurs
     */
    private static int getNextAvailableStudentNumber() throws SQLException {
        List<Student> existingStudents = StudentDAO.findAll();
        int highestStudentNum = -1;

        for (Student student : existingStudents) {
            String email = student.getEmail();
            if (email != null && email.matches("student\\d+@sms\\.com")) {
                try {
                    int studentNum = Integer.parseInt(email.substring(7, email.indexOf("@")));
                    highestStudentNum = Math.max(highestStudentNum, studentNum);
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    LOGGER.debug("Skipping email that doesn't match pattern: {}", email);
                }
            }
        }

        return highestStudentNum + 1;
    }

    /**
     * Populates the courses table with sample data, checking for existing courses to avoid conflicts.
     *
     * @throws SQLException if a database access error occurs
     */
    private static void populateCourses() throws SQLException {
        LOGGER.debug("Populating courses table");

        for (int courseNum = 0; courseNum < NUMBER_OF_COURSES; courseNum++) {
            String courseName = MockDatabaseGenerator.COURSE_NAMES.get(courseNum);

            if (CourseDAO.findByName(courseName).isPresent()) {
                LOGGER.debug("Course with name {} already exists, skipping", courseName);
                continue;
            }

            CourseDAO.addCourse(new Course(0, courseName, "Description" + courseNum));
        }
        LOGGER.debug("Populated courses table");
    }

    /**
     * Populates the modules table with sample data, checking for existing modules to avoid conflicts.
     *
     * @throws SQLException if a database access error occurs
     */
    private static void populateModules() throws SQLException {
        LOGGER.debug("Populating modules table");
        SecureRandom random = new SecureRandom();

        List<Course> courses = CourseDAO.findAll();
        if (courses.isEmpty()) {
            LOGGER.warn("No courses found, cannot populate modules");
            return;
        }

        for (Course course : courses) {
            List<Module> existingModules = ModuleDAO.findByCourseId(course.getId());
            int modulesToAdd = NUMBER_OF_MODULES_PER_COURSE - existingModules.size();

            if (modulesToAdd <= 0) {
                LOGGER.debug("Course {} already has enough modules, skipping", course.getName());
                continue;
            }

            for (int i = 0; i < modulesToAdd; i++) {
                String moduleName = MockDatabaseGenerator.MODULE_NAMES.get(
                        random.nextInt(MockDatabaseGenerator.MODULE_NAMES.size()));
                String lecturer =
                        MockDatabaseGenerator.LAST_NAMES.get(random.nextInt(MockDatabaseGenerator.LAST_NAMES.size()));

                String description = "Description-" + course.getId() + "-" + System.currentTimeMillis() + "-" + i;

                ModuleDAO.addModule(new Module(0, moduleName, description, lecturer, course.getId()));
            }
        }
        LOGGER.debug("Populated modules table");
    }

    /**
     * Populates the assessments table with sample data, checking for existing assessments to avoid conflicts.
     *
     * @throws SQLException if a database access error occurs
     */
    private static void populateAssessments() throws SQLException {
        LOGGER.debug("Populating assessments table");

        List<Module> modules = ModuleDAO.findAll();
        if (modules.isEmpty()) {
            LOGGER.warn("No modules found, cannot populate assessments");
            return;
        }

        for (Module module : modules) {
            List<Assessment> existingAssessments = AssessmentDAO.findByModuleId(module.getId());
            int assessmentsToAdd = NUMBER_OF_ASSESSMENTS_PER_MODULE - existingAssessments.size();

            if (assessmentsToAdd <= 0) {
                LOGGER.debug("Module {} already has enough assessments, skipping", module.getName());
                continue;
            }

            for (int i = 0; i < assessmentsToAdd; i++) {
                String name = "Assessment-" + module.getId() + "-" + (existingAssessments.size() + i + 1);
                String description = "Description-" + module.getId() + "-" + System.currentTimeMillis() + "-" + i;

                AssessmentDAO.addAssessment(
                        new Assessment(0, name, description, new Date(System.currentTimeMillis()), module.getId()));
            }
        }
        LOGGER.debug("Populated assessments table");
    }

    /**
     * Populates the course enrollments and results tables with sample data,
     * checking for existing enrollments to avoid conflicts.
     *
     * @throws SQLException if a database access error occurs
     */
    private static void populateEnrollmentsAndResults() throws SQLException {
        LOGGER.debug("Populating enrollments and results tables");
        SecureRandom random = new SecureRandom();

        List<Student> students = StudentDAO.findAll();
        List<Course> courses = CourseDAO.findAll();

        if (students.isEmpty() || courses.isEmpty()) {
            LOGGER.warn("No students or courses found, cannot populate enrollments and results");
            return;
        }

        for (Student student : students) {
            List<CourseEnrollment> existingEnrollments = CourseEnrollmentDAO.findByStudentId(student.getId());
            int enrollmentsToAdd =
                    Math.min(NUMBER_OF_ENROLLMENTS_PER_STUDENT - existingEnrollments.size(), courses.size());

            if (enrollmentsToAdd <= 0) {
                LOGGER.debug("Student {} already has enough enrollments, skipping", student.getEmail());
                continue;
            }

            // Create a list of course IDs the student is not yet enrolled in
            List<Integer> availableCourseIds = new ArrayList<>();
            for (Course course : courses) {
                boolean alreadyEnrolled = existingEnrollments.stream().anyMatch(e -> e.getCourseId() == course.getId());
                if (!alreadyEnrolled) {
                    availableCourseIds.add(course.getId());
                }
            }

            // Enroll the student in random available courses
            for (int i = 0; i < enrollmentsToAdd && !availableCourseIds.isEmpty(); i++) {
                int randomIndex = random.nextInt(availableCourseIds.size());
                int courseId = availableCourseIds.remove(randomIndex);

                CourseEnrollmentDAO.addCourseEnrollment(
                        new CourseEnrollment(0, student.getId(), courseId, new Date(System.currentTimeMillis())));

                addRandomResultsForEnrollment(student.getId(), courseId, random);
            }
        }
        LOGGER.debug("Populated course enrollments and results tables");
    }

    /**
     * Adds random assessment results for a student enrolled in a course.
     *
     * @param studentId The ID of the student
     * @param courseId The ID of the course
     * @param random A SecureRandom instance for generating random values
     * @throws SQLException if a database access error occurs
     */
    private static void addRandomResultsForEnrollment(int studentId, int courseId, SecureRandom random)
            throws SQLException {
        List<Module> modules = ModuleDAO.findByCourseId(courseId);

        for (Module module : modules) {
            List<Assessment> assessments = AssessmentDAO.findByModuleId(module.getId());
            for (Assessment assessment : assessments) {
                if (ResultDAO.findByStudentAndAssessment(studentId, assessment.getId())
                        .isPresent()) {
                    continue;
                }

                ResultDAO.addResult(new Result(0, studentId, assessment.getId(), random.nextInt(100)));
            }
        }
    }
}
