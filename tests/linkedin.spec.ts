import { test, expect } from '@playwright/test';

// TEMPLATE: LinkedIn read-only checks + place to paste recorder output
// Usage:
// 1) Record a flow with Playwright Recorder:
//    npx playwright codegen https://www.linkedin.com
//    - Perform the actions you want to record (navigate, search, open profile, etc.)
//    - Copy the generated code from the recorder
// 2) Paste the generated actions into the "RECORDER_PASTE_AREA" below
// 3) Adjust selectors/timeouts and add robust assertions/fixtures
// 4) Run: npm test --grep @linkedin

// Note: don't record or paste any real credentials here. For login flows use a test account
// and be aware of CAPTCHAs / anti-bot protections.

// --- START: place recorder code below this comment (RECORDER_PASTE_AREA) ---

// Example read-only checks (no login):
test.describe('@linkedin public', () => {
  test('linkedin homepage has signup or login form', async ({ page }) => {
    await page.goto('https://www.linkedin.com');
    // the landing page shows a Sign in / Join now links or input elements
    await expect(page).toHaveTitle(/LinkedIn/i);
    // check for sign in button (may vary by locale)
    const signIn = page.locator('a:has-text("Sign in"), a:has-text("Entrar"), a[href*="login"]');
    await expect(signIn.first()).toBeVisible();
  });

  test('search box or hero area is present', async ({ page }) => {
    await page.goto('https://www.linkedin.com');
    // check for presence of typical hero area or search input
    // Use multiple candidate selectors and assert that at least one is visible.
    const candidates = [
      '#main-content',
      'header',
      'form',
      'input[type="search"]',
      'input[placeholder*="Search"]',
      '[data-test-nav-search]'
    ];

    let foundVisible = false;
    for (const sel of candidates) {
      const locator = page.locator(sel);
      // If locator resolves to multiple elements, check if any of them is visible
      const count = await locator.count();
      for (let i = 0; i < count; i++) {
        if (await locator.nth(i).isVisible()) {
          foundVisible = true;
          break;
        }
      }
      if (foundVisible) break;
    }

    await expect(foundVisible).toBeTruthy();
  });
});

// --- END: recorder paste area ---
