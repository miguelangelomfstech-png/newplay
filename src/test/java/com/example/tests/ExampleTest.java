package com.example.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExampleTest {

  @Test
  public void homepageHasTitle() {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
      Page page = browser.newPage();
      page.navigate("https://example.com");
      String title = page.title();
      Assertions.assertTrue(title.contains("Example Domain"));
      browser.close();
    }
  }
}
