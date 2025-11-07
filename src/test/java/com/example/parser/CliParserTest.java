package com.example.parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import com.example.parser.cookie.CookieAnalysisRequest;
import com.example.parser.utils.CliParser;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class CliParserTest {

   private CliParser cliParser;

    @BeforeEach
    void setUp() {
        cliParser = new CliParser();
    }

    @Test
    @DisplayName("Should parse valid arguments successfully")
    void parseValidArgumentsReturnsRequestObject(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("cookies.csv");
        java.nio.file.Files.createFile(testFile);
        String[] args = {"-f", testFile.toString(), "-d", "2023-12-25"};

        CookieAnalysisRequest request = cliParser.parse(args);

        assertNotNull(request);
        assertEquals(testFile.toString(), request.filePath());
        assertEquals("2023-12-25", request.dateStr());
    }

    @Test
    @DisplayName("Should throw RuntimeException when file option is missing")
    void parseMissingFileOptionThrowsRuntimeException() {
        String[] args = {"-d", "2023-12-25"};

        assertThrows(RuntimeException.class, () -> cliParser.parse(args));
    }

    @Test
    @DisplayName("Should throw RuntimeException when date option is missing")
    void parseMissingDateOptionThrowsRuntimeException(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("cookies.csv");
        java.nio.file.Files.createFile(testFile);
        String[] args = {"-f", testFile.toString()};

        assertThrows(RuntimeException.class, () -> cliParser.parse(args));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid date format")
    void parseInvalidDateFormatThrowsIllegalArgumentException(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("cookies.csv");
        java.nio.file.Files.createFile(testFile);
        String[] args = {"-f", testFile.toString(), "-d", "2023/12/25"};

        assertThrows(IllegalArgumentException.class, () -> cliParser.parse(args));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for malformed date")
    void parseMalformedDateThrowsIllegalArgumentException(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("cookies.csv");
        java.nio.file.Files.createFile(testFile);
        String[] args = {"-f", testFile.toString(), "-d", "not-a-date"};

        assertThrows(IllegalArgumentException.class, () -> cliParser.parse(args));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid date values")
    void parseInvalidDateValuesThrowsIllegalArgumentException(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("cookies.csv");
        java.nio.file.Files.createFile(testFile);
        String[] args = {"-f", testFile.toString(), "-d", "2023-13-45"};

        assertThrows(IllegalArgumentException.class, () -> cliParser.parse(args));
    }

    @Test
    @DisplayName("Should handle empty arguments array")
    void parseEmptyArgumentsThrowsRuntimeException() {
        String[] args = {};

        assertThrows(RuntimeException.class, () -> cliParser.parse(args));
    }

    @Test
    @DisplayName("Should handle unknown options")
    void parseUnknownOptionThrowsRuntimeException(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("cookies.csv");
        java.nio.file.Files.createFile(testFile);
        String[] args = {"-f", testFile.toString(), "-d", "2023-12-25", "-x", "unknown"};

        assertThrows(RuntimeException.class, () -> cliParser.parse(args));
    }
}
