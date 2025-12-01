# Playwright Java tests (Maven)

This folder contains a Java Maven project that runs Playwright tests using JUnit 5.

Quickstart

1. Install Java 11+ and Maven.
2. From the project root run:

```powershell
mvn test
```

Notes
- The first run may download Playwright browser binaries. If you need to pre-install browsers, run:

```powershell
# inside a Playwright-enabled container or after Maven downloads dependencies
mvn -DskipTests=true test-compile
# Then run a small java snippet or use Playwright CLI to install browsers if needed
```

- Test reports and artifacts are generated under `playwright-report/` and `test-results/`.
