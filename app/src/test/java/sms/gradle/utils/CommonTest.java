package sms.gradle.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CommonTest {

    @Test
    public void testGenerateSha256Hash() {
        final String input = "test";
        final String expected = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        final String actual = Common.generateSha256Hash(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateSha256HashWithEmptyInput() {
        final String input = "";
        final String expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        final String actual = Common.generateSha256Hash(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateSha256HashWithSpecialCharacters() {
        final String input = "!@#$%^&*()_+";
        final String expected = "36d3e1bc65f8b67935ae60f542abef3e55c5bbbd547854966400cc4f022566cb";
        final String actual = Common.generateSha256Hash(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateSha256HashWithLongInput() {
        final String input = "This is a test, this is a test. Long String Test, Long String Test.";
        final String expected = "0dffa635b474e7bf210b8ff31ad8252b7acb7f12d29d6a26e7da6f43d86c11da";
        final String actual = Common.generateSha256Hash(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateSha256HashWithNullInput() {
        assertThrows(NullPointerException.class, () -> Common.generateSha256Hash(null));
    }

    @Test
    public void testGenerateSha256HashWithSpaceInput() {
        final String input = " ";
        final String expected = "36a9e7f1c95b82ffb99743e0c5c4ce95d83c9a430aac59f84ef3cbfab6145068";
        final String actual = Common.generateSha256Hash(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testSameInputProducesSameHash() {
        final String input = "test";
        final String hash1 = Common.generateSha256Hash(input);
        final String hash2 = Common.generateSha256Hash(input);
        assertEquals(hash1, hash2);
    }

    @Test
    public void testDifferentInputsProduceUniqueHashes() {
        final String input1 = "test1";
        final String input2 = "test2";
        final String hash1 = Common.generateSha256Hash(input1);
        final String hash2 = Common.generateSha256Hash(input2);
        assertNotEquals(hash1, hash2);
    }
}
