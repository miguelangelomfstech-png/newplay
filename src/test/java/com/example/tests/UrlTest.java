package com.example.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UrlTest {

  @Test
  public void homepageUrlContainsExampleDotCom() {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
      Page page = browser.newPage();
      page.navigate("https://example.com");
      Assertions.assertTrue(page.url().contains("example.com"));
      browser.close();
    }
  }
}
