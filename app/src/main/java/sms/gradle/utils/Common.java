package sms.gradle.utils;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public final class Common {
    private Common() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String generateSha256Hash(final String input) {
        return Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
    }
}
