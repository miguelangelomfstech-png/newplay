package com.example.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinkedInTest {

  @Test
  public void linkedinPublicChecks() {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
      Page page = browser.newPage();
      page.navigate("https://www.linkedin.com");

      // title check
      String title = page.title();
      Assertions.assertTrue(title.toLowerCase().contains("linkedin"));

      // Check for sign-in link (varies by locale). Accept several candidates.
      Locator signIn = page.locator("a:has-text(\"Sign in\"), a[href*='login'], a:has-text(\"Entrar\")");
      Assertions.assertTrue(signIn.first().isVisible());

      // Check hero/search presence: ensure at least one candidate selector is visible
      String[] candidates = new String[]{"#main-content", "header", "form", "input[type='search']", "input[placeholder*='Search']"};
      boolean foundVisible = false;
      for (String sel : candidates) {
        Locator loc = page.locator(sel);
        int count = loc.count();
        for (int i = 0; i < count; i++) {
          if (loc.nth(i).isVisible()) {
            foundVisible = true;
            break;
          }
        }
        if (foundVisible) break;
      }
      Assertions.assertTrue(foundVisible, "Expected at least one hero/search selector to be visible");

      browser.close();
    }
  }
}
