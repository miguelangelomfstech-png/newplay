@echo off
REM Lightweight Maven wrapper for Windows (cmd/powershell)
REM Uses local mvn if available, otherwise runs Maven inside the official Maven Docker image

where mvn >nul 2>&1
if %ERRORLEVEL%==0 (
  mvn %*
  exit /b %ERRORLEVEL%
)

where docker >nul 2>&1
if %ERRORLEVEL%==0 (
  REM Use Playwright image and install maven inside it, so browser tests run correctly
  REM Try Playwright image first (browsers available). If it fails, fall back to the official Maven image.
  docker run --rm -v "%cd%":/work -w /work mcr.microsoft.com/playwright:latest bash -lc "apt-get update -qq && apt-get install -y -qq maven && mvn %*" || \
    docker run --rm -v "%cd%":/work -w /work maven:3.9.5-eclipse-temurin-17 mvn %*
  exit /b %ERRORLEVEL%
)

echo Maven not found and Docker not available.
echo Install Maven (https://maven.apache.org/install.html) or install Docker and try again.
exit /b 1
