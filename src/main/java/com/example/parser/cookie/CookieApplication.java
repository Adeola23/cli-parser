package com.example.parser.cookie;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.parser.utils.CliParser;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class CookieApplication {

    @Autowired
    private CliParser cliParser;

    @Autowired
    private MostActiveCookieAnalyzer mostActiveCookieAnalyzer;

    public List<String> run(String[] args) {
        try {
            CookieAnalysisRequest cookieAnalysisRequest = cliParser.parse(args);

            List<String> mostActiveCookies = mostActiveCookieAnalyzer.getMostActiveCookies(cookieAnalysisRequest);

            for (String cookie : mostActiveCookies) {
                log.info("Most active cookie: {}", cookie);
            }
            return mostActiveCookies;
        } catch (IOException e) {
            log.error("Error reading the log file: {}", e.getMessage());
            throw new RuntimeException("Failed to process cookie data", e);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new RuntimeException("Failed to process cookie data", e);
        }
    }
}
