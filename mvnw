#!/usr/bin/env bash
set -euo pipefail

# Lightweight Maven wrapper:
# - if 'mvn' is available on PATH, run it
# - otherwise, if Docker is available, run Maven inside the official Maven image
# - otherwise, print an actionable message

if command -v mvn >/dev/null 2>&1; then
  exec mvn "$@"
fi

if command -v docker >/dev/null 2>&1; then
  # Use the Playwright image so browser binaries are available for Playwright tests.
  # Install maven inside the image on-the-fly and run the requested mvn command.
  # Try Playwright image first (browsers available). If it fails, fall back to the official Maven image.
  docker run --rm -v "$PWD":/work -w /work mcr.microsoft.com/playwright:latest \
    bash -lc "apt-get update -qq && apt-get install -y -qq maven && mvn $*" || \
  docker run --rm -v "$PWD":/work -w /work maven:3.9.5-eclipse-temurin-17 mvn "$@"
else
  echo "Maven not found and Docker not available."
  echo "Install Maven (https://maven.apache.org/install.html) or install Docker and try again."
  exit 1
fi
