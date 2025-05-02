package sms.gradle.utils;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Common {
    private static final Logger LOGGER = LogManager.getLogger();

    private Common() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Generates a SHA-256 hash of the given input string
     * @param input The input string to hash
     * @return The SHA-256 hash of the input string
     */
    public static String generateSha256Hash(final String input) {
        LOGGER.debug("Generating SHA-256 hash for input: {}", input);
        return Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
    }

    /**
     * Creates a styled label
     * @param text The text to display
     * @param style The style to apply
     * @return The styled label
     */
    public static Label createStyledLabel(final String text, final String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    /**
     * Gets a node from a stage using a CSS selector
     * @param <T> The type of node to return
     * @param stage The stage to search in
     * @param selector The CSS selector to search for
     * @return The node matching the selector
     */
    @SuppressWarnings("unchecked")
    public static <T extends Node> T getNode(final Stage stage, final String selector) {
        T lookupResult = (T) stage.getScene().lookup(selector);
        if (lookupResult == null) {
            LOGGER.error("Failed to find node with selector: {}", selector);
        }
        return lookupResult;
    }
}
