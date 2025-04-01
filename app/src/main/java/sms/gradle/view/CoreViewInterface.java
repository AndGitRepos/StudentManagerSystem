package sms.gradle.view;

// Base interface for all views
// Uses Template method design pattern
public interface CoreViewInterface {

    // initialises UI components - labels, text fields + areas buttons etc.
    void componentsInitialised();

    // components positioning / padding / spacing
    void componentsLayout();

    // colours, fonts, shades to components
    void componentsStyling();
}
