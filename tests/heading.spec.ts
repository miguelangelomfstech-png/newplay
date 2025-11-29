import { test, expect } from '@playwright/test';

test('homepage has an H1 with "Example Domain"', async ({ page }) => {
  await page.goto('https://example.com');
  const h1 = await page.locator('h1').innerText();
  expect(h1).toBe('Example Domain');
});
