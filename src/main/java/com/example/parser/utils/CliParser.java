package com.example.parser.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;
import com.example.parser.cookie.CookieAnalysisRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

@Slf4j
@Component
public class CliParser {
    private static final String ARG_FILE = "f";
    private static final String ARG_DATE = "d";
    private static final String ARG_HELP = "h";


    private Options createOptions() {
        Options options = new Options();
        
        options.addOption(Option.builder(ARG_FILE)
                .longOpt("file")
                .hasArg()
                .required()
                .desc("Path to the log file")
                .get());
                
        options.addOption(Option.builder(ARG_DATE)
                .longOpt("date")
                .hasArg()
                .required()
                .desc("Date in yyyy-MM-dd format")
                .get());

        options.addOption(Option.builder(ARG_HELP)
                .longOpt("help")
                .desc("Show this help message")
                .get());
                
        return options;
    }

    public CookieAnalysisRequest parse(String[] args) {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption(ARG_HELP)) {
                printHelp();
                System.exit(0);
            }

            String filePath = cmd.getOptionValue(ARG_FILE);
            String dateStr = cmd.getOptionValue(ARG_DATE);

            validateFilePath(filePath);
            validateDate(dateStr);
            
            return new CookieAnalysisRequest(filePath, dateStr);

        } catch (ParseException exp) {
            log.error("Error: {}", exp.getMessage());
            printHelp();
            throw new RuntimeException("Failed to parse command line arguments", exp);
        }
    }

    private void validateDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd: " + dateStr);
        }
    }

    private void validateFilePath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
    }

    private void printHelp() {
            log.info("Usage: java -jar CookieAnalyzer.jar [options]");
            log.info("Options:");
            log.info("  -f, --file <file_path>    Path to the log file (required)");
            log.info("  -d, --date <yyyy-MM-dd>  Date in yyyy-MM-dd format (required)");
            log.info("  -h, --help               Show this help message");
            log.info("");
            log.info("Example:");
            log.info("  java -jar CookieAnalyzer.jar -f cookies.csv -d 2023-12-25");
    }
}