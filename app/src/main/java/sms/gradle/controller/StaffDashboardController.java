package sms.gradle.controller;

import javafx.event.ActionEvent;
import sms.gradle.view.ViewFactory;

public final class StaffDashboardController {

    public static void handleViewCourseButton(ActionEvent event) {
        ViewFactory.getInstance().changeToManageCourseStage();
    }

    public static void handleManageAssessmentsButton(ActionEvent event) {
        // TODO - Once Manage assessments page has been implemented add call to change to and remove print
        System.out.print("Handle Manage Assessment Button called");
    }

    public static void handleManageModulesButton(ActionEvent event) {
        // TODO - Once Manage modules page has been implemented add call to change to and remove print
        System.out.print("Handle Manage Modules Button called");
    }

    public static void handleManageCoursesButton(ActionEvent event) {
        // TODO - Once Manage courses page has been implemented add call to change to and remove print
        System.out.print("Handle Manage Courses Button called");
    }

    public static void handleManageStudentsButton(ActionEvent event) {
        // TODO - Once Manage students page has been implemented add call to change to and remove print
        System.out.print("Handle Manage Students Button called");
    }

    public static void handleManageStaffButton(ActionEvent event) {
        // TODO - Once Manage staff page has been implemented add call to change to and remove print
        System.out.print("Handle Manage Staff Button called");
    }

    public static void handleLogoutButton(ActionEvent event) {
        ViewFactory.getInstance().changeToLoginStage();
    }
}
