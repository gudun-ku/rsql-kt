# rsql-kt

A Kotlin implementation of an RSQL parser (based on [Jirutka RSQL parser](https://github.com/jirutka/rsql-parser)), leveraging ANTLR4. This library enables parsing and handling of RSQL queries in JVM applications.

## Features

- RSQL parsing and AST generation
- Built with Kotlin and ANTLR4
- JUnit 5 test suite

## Getting Started

### Prerequisites

- JDK 21+
- [Gradle](https://gradle.org/) (wrapper included)
- Internet connection (for dependency resolution)

### Building the Project

To build the project and generate sources from ANTLR grammars, run:

```bash
./gradlew build
```

### Running Tests

To execute the test suite:

```bash
./gradlew test
```

### Usage

Add this library as a dependency in your Kotlin/JVM project. Example usage and API documentation can be found in the source code under `src/main/kotlin/com/beloushkin/rsql`.

## Performance Tests

You can evaluate the performance of the RSQL expression parser using the included performance tests.

### How to Run Performance Tests

Run the following command from the project root:

```bash
./gradlew test --tests "com.beloushkin.rsql.parser.RSQLParserPerfTest"
```

### CSV Performance Report

After running the performance tests, a CSV report will be generated at:

```
build/reports/rsql_perf_report.csv
```

This file contains the following columns:
- `expr_length`: Length of the RSQL expression
- `parse_time_ms`: Minimum parse time (ms) over 5 runs
- `expr`: The (truncated) RSQL expression

You can open this file in any spreadsheet tool to analyze parser performance.

## Project Structure

- `src/main/kotlin`: Main source code
- `src/main/antlr`: ANTLR grammars
- `src/test/kotlin`: Test sources

## Contributing

Contributions are welcome! Please open issues or submit pull requests.

## License

[MIT](LICENSE)
