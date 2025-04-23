module sms.gradle {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.google.common;
    requires com.h2database;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires javafx.graphics;

    exports sms.gradle;
    exports sms.gradle.model.dao;
    exports sms.gradle.model.entities;
    exports sms.gradle.view;

    opens sms.gradle to
            javafx.graphics;
}
