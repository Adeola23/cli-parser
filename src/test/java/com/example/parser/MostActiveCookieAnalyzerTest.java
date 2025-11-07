package com.example.parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import com.example.parser.cookie.CookieAnalysisRequest;
import com.example.parser.cookie.MostActiveCookieAnalyzer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;


class MostActiveCookieAnalyzerTest {

    @TempDir
    Path tempDir;
    private Path testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("test_cookies.csv");
    }

    private void createTestFile(String content) throws IOException {
        Files.writeString(testFile, content);
    }

    @Test
    @DisplayName("Single most active cookie - should return one cookie with highest frequency")
    void testSingleMostActiveCookie() throws IOException {
        String csvContent = """
            cookie,timestamp
            AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00
            SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00
            AtY0laUfhglK3lC7,2018-12-09T07:25:00+00:00
            SAZuXPGUrfbcn5UA,2018-12-08T22:03:00+00:00
            """;
        
        createTestFile(csvContent);
        CookieAnalysisRequest request = new CookieAnalysisRequest(testFile.toString(), "2018-12-09");
        MostActiveCookieAnalyzer analyzer = new MostActiveCookieAnalyzer();
        List<String> result = analyzer.getMostActiveCookies(request);

        assertEquals(1, result.size());
        assertTrue(result.contains("AtY0laUfhglK3lC7"));
    }

    @Test
    @DisplayName("Multiple most active cookies - should return all cookies with same highest frequency")
    void testMultipleMostActiveCookies() throws IOException {
        String csvContent = """
            cookie,timestamp
            cookieA,2018-12-09T14:19:00+00:00
            cookieB,2018-12-09T10:13:00+00:00
            cookieA,2018-12-09T07:25:00+00:00
            cookieB,2018-12-09T22:03:00+00:00
            cookieC,2018-12-09T15:30:00+00:00
            """;
        
        createTestFile(csvContent);
        CookieAnalysisRequest request = new CookieAnalysisRequest(testFile.toString(), "2018-12-09");
        MostActiveCookieAnalyzer analyzer = new MostActiveCookieAnalyzer();

        List<String> result = analyzer.getMostActiveCookies(request);

        assertEquals(2, result.size());
        assertTrue(result.contains("cookieA"));
        assertTrue(result.contains("cookieB"));
        assertFalse(result.contains("cookieC"));
    }

    @Test
    @DisplayName("No cookies for date - should return empty list when no cookies exist for target date")
    void testNoCookiesForDate() throws IOException {
        String csvContent = """
            cookie,timestamp
            cookieA,2018-12-08T14:19:00+00:00
            cookieB,2018-12-08T10:13:00+00:00
            """;
        
        createTestFile(csvContent);
        CookieAnalysisRequest request = new CookieAnalysisRequest(testFile.toString(), "2018-12-09");
        MostActiveCookieAnalyzer analyzer = new MostActiveCookieAnalyzer();

        List<String> result = analyzer.getMostActiveCookies(request);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Empty file - should handle empty CSV file gracefully")
    void testEmptyFile() throws IOException {
        String csvContent = "cookie,timestamp\n";
        
        createTestFile(csvContent);
        CookieAnalysisRequest request = new CookieAnalysisRequest(testFile.toString(), "2018-12-09");
        MostActiveCookieAnalyzer analyzer = new MostActiveCookieAnalyzer();

        List<String> result = analyzer.getMostActiveCookies(request);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Single cookie - should return single cookie when only one exists for date")
    void testSingleCookie() throws IOException {
        String csvContent = """
            cookie,timestamp
            singleCookie,2018-12-09T14:19:00+00:00
            """;
        
        createTestFile(csvContent);
        CookieAnalysisRequest request = new CookieAnalysisRequest(testFile.toString(), "2018-12-09");
        MostActiveCookieAnalyzer analyzer = new MostActiveCookieAnalyzer();

        List<String> result = analyzer.getMostActiveCookies(request);

        assertEquals(1, result.size());
        assertEquals("singleCookie", result.get(0));
    }

    @Test
    @DisplayName("Invalid timestamps - should skip invalid timestamp formats and process valid ones")
    void testInvalidTimestampsAreSkipped() throws IOException {
        String csvContent = """
            cookie,timestamp
            cookieA,2018-12-09T14:19:00+00:00
            cookieB,invalid-date-format
            cookieC,2018-12-09T15:19:00+00:00
            cookieD,2018-13-45T99:99:00+00:00  // Invalid date
            cookieA,2018-12-09T16:19:00+00:00
            """;
        
        createTestFile(csvContent);
        CookieAnalysisRequest request = new CookieAnalysisRequest(testFile.toString(), "2018-12-09");
        MostActiveCookieAnalyzer analyzer = new MostActiveCookieAnalyzer();

        List<String> result = analyzer.getMostActiveCookies(request);

        assertEquals(1, result.size());
        assertEquals("cookieA", result.get(0));
    }

    @Test
    @DisplayName("Malformed lines - should skip malformed CSV lines and process valid ones")
    void testMalformedLinesAreSkipped() throws IOException {
        String csvContent = """
            cookie,timestamp
            validCookie,2018-12-09T14:19:00+00:00
            invalidLineWithoutComma
            anotherValid,2018-12-09T15:19:00+00:00
            ,,tooManyCommas,2018-12-09T16:19:00+00:00
            validCookie,2018-12-09T17:19:00+00:00
            """;
        
        createTestFile(csvContent);
        CookieAnalysisRequest request = new CookieAnalysisRequest(testFile.toString(), "2018-12-09");
        MostActiveCookieAnalyzer analyzer = new MostActiveCookieAnalyzer();

        List<String> result = analyzer.getMostActiveCookies(request);

        assertEquals(1, result.size());
        assertEquals("validCookie", result.get(0));
    }
}