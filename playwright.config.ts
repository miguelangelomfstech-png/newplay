import { PlaywrightTestConfig } from '@playwright/test';

const config: PlaywrightTestConfig = {
  testDir: 'tests',
  timeout: 30 * 1000,
  retries: 0,
  expect: {
    timeout: 5000
  },
  use: {
    headless: true,
    viewport: { width: 1280, height: 720 },
    ignoreHTTPSErrors: true,
    video: 'retain-on-failure',
    screenshot: 'only-on-failure',
    trace: 'on-first-retry'
  },
  projects: [
    { name: 'chromium', use: { browserName: 'chromium' } },
    { name: 'firefox', use: { browserName: 'firefox' } },
    { name: 'webkit', use: { browserName: 'webkit' } }
  ],
  // Reporters: list for console, html for interactive report, junit for CI (Jenkins)
  reporter: [['list'], ['html', { open: 'never' }], ['junit', { outputFile: 'test-results/junit/results.xml' }]]
};

export default config;
