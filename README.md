# Student Management System

## Description
A Java-based student management system built with JavaFX for the GUI. The application uses modern Java features and follows best practices for code organization and testing.

Within this application you can login as a Student to view: courses, modules, assessments and grades.
You can also login as an Admin to: create student accounts, enroll students in courses and track overall course metrics.

## Prerequisites
- Java Development Kit (JDK) 21 or later
- Gradle (wrapper included in the repository)

## Building and Running

### Build the Project
```bash
# Clean and build the project
./gradlew clean build

# Build without running tests
./gradlew build -x test

# Create a fat JAR (includes all dependencies)
./gradlew fatJar
```

### Run the Application
```bash
# Run using Gradle
./gradlew run
```
```bash
# Build and run fatJar (All dependencies compiled within a single Jar)
./gradlew fatJar

java -jar app/build/libs/StudentManagementSystem-fat.jar
```
### Testing
```bash
# Run all tests
./gradlew test

# Run tests with detailed output
./gradlew test --info
```

### Logging

This application uses Log4j2 for processing logs.
Log4j2 allows for filtering logs through a hierarchy of levels: `TRACE -> DEBUG -> INFO -> WARN -> ERROR`
By default this filtering level is set to `WARN` for our application.
To modify this when testing you can modify the value stored within src/main/resources/log4j2.xml
```xml
<!-- Filtering any logs below WARN (INFO, DEBUG and TRACE logs not shown) -->
<Root level="warn">
</Root>
```
```xml
<!-- Filtering any logs below DEBUG (TRACE logs not shown) -->
<Root level="debug">
</Root>
```

Before implementing any logs please review the [Logging Best practices](https://logging.apache.org/log4j/2.x/manual/api.html#best-practice)


## CSS Styling

This application uses [JavaFX CSS](https://www.tutorialspoint.com/javafx/javafx_css.htm) for styling visual components, with JavaFX-specific properties and prefixes. CSS (Cascading Style Sheets) is a formatting tool used to manage spacing, colours, fonts and other visual components for elements within an application UI. 

### CSS Classes

1. `common.css` - Contains core/base styling implemented by other classes and used across views
2. `components.css` - Contains styling for grade circle and module row components
3. `dashboard.css` - Contains styling for student-dashboard and admin-dashboard views
4. `login.css` - Contains styling for components of login view (input fields, error messages, login form)
5. `manager.css` - Contains styling for admin management views

### Implementation Guide

#### Populate Views with JavaFX CSS  

````java 
// Add to the constructor: 
getStylesheets().add(getClass().getResource("/styles/{inputFileName}.css").toExternalForm());

// Apply to element:
element.getStyleClass().add("element-class-name");

````

````css
/* CSS Selector Example: */
.element-class-name {
    -fx-background-color: red;
    -fx-font-size: bold;
}

````

### Directory Location: 
CSS Files are located at
````cs
StudentManagerSystem/app/src/main/resources/styles
````

#### Documentation

Full documentation of the CSS Styling selectors and functions syntax can be found in the [JavaFX CSS Reference Guide](https://openjfx.io/javadoc/24/javafx.graphics/javafx/scene/doc-files/cssref.html)

## Code Formatting
This project uses Spotless with Palantir Java Format for consistent code formatting. The formatter is automatically integrated into the build process.

### Formatting Commands
```bash
# Check code formatting
./gradlew spotlessCheck

# Apply code formatting
./gradlew spotlessApply
```

The formatter handles:
- Import ordering
- Removal of unused imports
- Consistent code style using Palantir Java Format
- Automatic formatting during build

## Git Commands Quick Reference

### Basic Git Workflow
```bash
# Clone the repository
git clone [repository-url]

# Create and switch to a new branch
git checkout -b feature/your-feature-name

# Add changes
git add .

# Commit changes
git commit -m "descriptive commit message"

# Push changes
git push origin feature/your-feature-name

# Update your local main branch
git checkout main
git pull origin main

# Merge main into your feature branch
git checkout feature/your-feature-name
git merge main
```

### Git Best Practices
1. Keep commits atomic and focused
2. Write meaningful commit messages
3. Pull changes from main regularly
4. Create feature branches for new work
5. Use meaningful branch names (e.g., feature/, bugfix/, etc.)


If you see this, the demo works