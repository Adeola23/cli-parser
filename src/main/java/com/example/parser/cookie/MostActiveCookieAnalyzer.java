package com.example.parser.cookie;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class MostActiveCookieAnalyzer {

    public List<String> getMostActiveCookies(CookieAnalysisRequest request) throws IOException {
        LocalDate targetDate = LocalDate.parse(request.dateStr());
        Map<String, Integer> cookieCountMap = new HashMap<>();
        int maxCount = 0;

        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of(request.filePath()));
        } catch (IOException e) {
            throw new IOException("Failed to read the log file", e);
        }

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split(",", 2);
            if (parts.length != 2)
                continue;

            String cookie = parts[0].trim();
            String timestamp = parts[1].trim();

            if (timestamp.length() < 10)
                continue;

            try {
                LocalDate logDate = LocalDate.parse(timestamp.substring(0, 10));
                if (logDate.equals(targetDate)) {
                    int count = cookieCountMap.getOrDefault(cookie, 0) + 1;
                    cookieCountMap.put(cookie, count);
                    maxCount = Math.max(maxCount, count);
                }
            } catch (DateTimeParseException e) {
                log.warn("Malformed date in log file: {}", timestamp, e);
            }
        }

        List<String> mostActiveCookies = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : cookieCountMap.entrySet()) {
            if (entry.getValue() == maxCount) {
                mostActiveCookies.add(entry.getKey());
            }
        }

        return mostActiveCookies;
    }
}
