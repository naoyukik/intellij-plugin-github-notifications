# Technology Stack

## Core Technologies

- **Language**: Kotlin (Targeting JVM 21)
- **Platform**: IntelliJ Platform SDK
- **Build System**: Gradle (Kotlin DSL)

## Libraries & Frameworks

- **Serialization**: `kotlinx.serialization`
- **Testing**:
    - JUnit 5 (Platform)
    - Kotest (Assertions & Runner)
    - MockK (Mocking)
    - IntelliJ Platform Test Framework
- **HTTP Client**: (To be determined/verified - likely using IntelliJ's built-in `HttpRequests` or a standard library
  like `java.net.http` or `OkHttp` if explicitly added). *Note: The project analysis
  shows `http-client.private.env.json` implying usage of IntelliJ's HTTP Client tool for testing, but the code likely
  uses a library.*

## Code Quality & Tooling

- **Static Analysis**: Detekt
- **Quality Gate**: Qodana
- **Code Coverage**: Kover
- **Version Control**: Git
- **CI/CD**: GitHub Actions (implied by `.github/workflows`)
