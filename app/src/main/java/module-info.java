module sms.gradle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires com.google.common;
    requires com.h2database;
    requires transitive javafx.graphics;

    exports sms.gradle;
    exports sms.gradle.model.dao;
    exports sms.gradle.model.entities;
    exports sms.gradle.view;

    opens sms.gradle to
            javafx.graphics;
}
