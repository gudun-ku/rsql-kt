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

## Project Structure

- `src/main/kotlin`: Main source code
- `src/main/antlr`: ANTLR grammars
- `src/test/kotlin`: Test sources

## Contributing

Contributions are welcome! Please open issues or submit pull requests.

## License

[MIT](LICENSE)
