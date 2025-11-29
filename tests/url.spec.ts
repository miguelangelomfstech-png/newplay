import { test, expect } from '@playwright/test';

test('homepage URL contains example.com', async ({ page }) => {
  await page.goto('https://example.com');
  expect(page.url()).toContain('example.com');
});
