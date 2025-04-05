package sms.gradle.view;

/*
 * Base interface for all views using Template pattern
 */
public interface CoreViewInterface {

    // e.g. labels, text fields + areas & buttons
    void initialiseCoreUIComponents();

    // e.g. positioning, padding & spacing
    void layoutCoreUIComponents();

    // e.g. colours, fonts & shades
    void styleCoreUIComponents();
}
