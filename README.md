# Student Management System

## Description
A Java-based student management system built with JavaFX for the GUI. The application uses modern Java features and follows best practices for code organization and testing.

Within this application you can login as an Student to view: courses, modules, assessments and grades.
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