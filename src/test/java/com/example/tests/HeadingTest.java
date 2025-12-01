package com.example.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeadingTest {

  @Test
  public void homepageHasH1() {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
      Page page = browser.newPage();
      page.navigate("https://example.com");
      String h1 = page.locator("h1").innerText();
      Assertions.assertEquals("Example Domain", h1.trim());
      browser.close();
    }
  }
}
