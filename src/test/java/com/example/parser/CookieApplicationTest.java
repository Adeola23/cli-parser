package com.example.parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import com.example.parser.cookie.CookieAnalysisRequest;
import com.example.parser.cookie.CookieApplication;
import com.example.parser.cookie.MostActiveCookieAnalyzer;
import com.example.parser.utils.CliParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import nl.altindag.log.LogCaptor;

@ExtendWith(MockitoExtension.class)
class CookieApplicationTest {

    @Mock
    private CliParser cliParser;

    @Mock
    private MostActiveCookieAnalyzer mostActiveCookieAnalyzer;

    @Mock
    private Logger logger;

    @InjectMocks
    private CookieApplication cookieApplication;

    private String[] testArgs;
    private CookieAnalysisRequest testRequest;
    private List<String> expectedCookies;

    @BeforeEach
    void setUp() {
        testArgs = new String[]{"-f", "cookie_log.csv", "-d", "2018-12-09"};
        testRequest = new CookieAnalysisRequest("cookie_log.csv", "2018-12-09");
        expectedCookies = Arrays.asList("AtY0laUfhglK3lC7", "SAZuXPGUrfbcn5UA");
    }

    @Test
    @DisplayName("Successful execution returns most active cookies and logs info")  
    void runSuccessfulExecutionReturnsMostActiveCookies() throws Exception {
        when(cliParser.parse(testArgs)).thenReturn(testRequest);
        when(mostActiveCookieAnalyzer.getMostActiveCookies(testRequest)).thenReturn(expectedCookies);

        LogCaptor logCaptor = LogCaptor.forClass(CookieApplication.class);

        List<String> result = cookieApplication.run(testArgs);

        assertNotNull(result);
        assertEquals(expectedCookies, result);
        verify(cliParser).parse(testArgs);
        verify(mostActiveCookieAnalyzer).getMostActiveCookies(testRequest);
        for (String cookie : expectedCookies) {
            assertTrue(logCaptor.getInfoLogs().contains("Most active cookie: " + cookie));
        }
    }


    @Test
    @DisplayName("IOException during file reading throws RuntimeException and logs error")
    void runIOExceptionThrowsRuntimeExceptionAndLogsError() throws Exception {
        String errorMessage = "File not found";
        IOException ioException = new IOException(errorMessage);
        when(cliParser.parse(testArgs)).thenReturn(testRequest);
        when(mostActiveCookieAnalyzer.getMostActiveCookies(testRequest)).thenThrow(ioException);

        LogCaptor logCaptor = LogCaptor.forClass(CookieApplication.class);
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> cookieApplication.run(testArgs));
        
        assertEquals("Failed to process cookie data", exception.getMessage());
        assertEquals(ioException, exception.getCause());
        assertTrue(logCaptor.getErrorLogs().stream()
            .anyMatch(log -> log.contains("Error reading the log file: " + errorMessage)));
        verify(mostActiveCookieAnalyzer).getMostActiveCookies(testRequest);
    }

}