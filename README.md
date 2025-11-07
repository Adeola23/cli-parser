```markdown
# Cookie Analyzer

Small Java CLI tool to find the most active cookie(s) for a given date from a CSV log.

Highlights
- Small, dependency-free CLI packaged as a standalone JAR.
- Built with Java 17 (recommended) and Maven (Maven wrapper included).
- Fast analyzer for large CSV logs.

Quick pointers (this repository)
- Entry point: `com.example.parser.ParserApplication` (src/main/java/com/example/parser/ParserApplication.java)
- CLI parsing: `com.example.parser.utils.CliParser` (src/main/java/com/example/parser/utils/CliParser.java)
- Application: `com.example.parser.cookie.CookieApplication` (src/main/java/com/example/parser/cookie/CookieApplication.java)
- Analyzer: `com.example.parser.cookie.MostActiveCookieAnalyzer` (src/main/java/com/example/parser/cookie/MostActiveCookieAnalyzer.java)
- Request model: `com.example.parser.cookie.CookieAnalysisRequest` (src/main/java/com/example/parser/cookie/CookieAnalysisRequest.java)

Files of interest
- Sample input (example CSV): `cookie_log.csv` (repo root)
- Convenience runner script: `cookie-analyzer` (repo root, executable script)
- Packaged JAR (Maven default output): `target/quant-cast-0.0.1-SNAPSHOT.jar`
- Tests: `src/test/java/com/example/parser/` (includes `CliParserTest`, `CookieApplicationTest`, `MostActiveCookieAnalyzerTest`, ...)

Requirements
- JDK 17+ (project compiled with Java 17)
- Maven (you can use the included wrapper `./mvnw`)

Build & Run (from the project root)
```sh
# build (uses wrapper if present)
./mvnw clean package

# run tests
./mvnw test

# make runner executable (only needed once)
chmod +x cookie-analyzer

# run using the convenience script (example)
./cookie-analyzer -f cookie_log.csv -d 2018-12-09
```

Usage
```sh
# from project root
./cookie-analyzer -f <path/to/cookie_log.csv> -d YYYY-MM-DD
```

Example
Input (repo root `cookie_log.csv`)
- CSV format: cookie,timestamp (ISO 8601)

Command
```sh
./cookie-analyzer -f cookie_log.csv -d 2018-12-09
```

Expected Output
```
<one-or-more-cookie-ids>
```

Notes
- If you prefer running the jar directly:
```sh
java -jar target/quant-cast-0.0.1-SNAPSHOT.jar -f cookie_log.csv -d 2018-12-09
```

Troubleshooting
- If the `cookie-analyzer` script fails with permissions, run:
```sh
chmod +x cookie-analyzer
```
- If you get Java version errors, confirm you are using Java 17+:
```sh
java -version
```

Where to look in the code
- CLI parsing and argument validation: `src/main/java/com/example/parser/utils/CliParser.java`
- Application bootstrap: `src/main/java/com/example/parser/ParserApplication.java` and `src/main/java/com/example/parser/cookie/CookieApplication.java`
- Core analysis logic: `src/main/java/com/example/parser/cookie/MostActiveCookieAnalyzer.java`
- Tests: `src/test/java/com/example/parser/`
- 