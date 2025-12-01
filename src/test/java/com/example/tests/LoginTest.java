package com.example.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTest {

  private final String loginHtml = """
    <!doctype html>
    <html>
      <body>
        <form id=\"login\">
          <label>Username: <input name=\"username\" id=\"username\" /></label>
          <label>Password: <input name=\"password\" id=\"password\" type=\"password\"/></label>
          <button id=\"submit\" type=\"submit\">Login</button>
        </form>
        <div id=\"message\"></div>
        <script>
          const form = document.getElementById('login');
          form.addEventListener('submit', (e) => {
            e.preventDefault();
            const u = document.getElementById('username').value;
            const p = document.getElementById('password').value;
            const msg = document.getElementById('message');
            if (u === 'user' && p === 'pass') {
              msg.textContent = 'Login successful';
              msg.setAttribute('data-success', 'true');
            } else {
              msg.textContent = 'Invalid credentials';
              msg.setAttribute('data-success', 'false');
            }
          });
        </script>
      </body>
    </html>
  """;

  @Test
  public void loginSuccessShowsSuccessMessage() {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
      Page page = browser.newPage();
      page.setContent(loginHtml);
      page.fill("#username", "user");
      page.fill("#password", "pass");
      page.click("#submit");
      Assertions.assertEquals("Login successful", page.locator("#message").innerText());
      Assertions.assertEquals("true", page.locator("#message").getAttribute("data-success"));
      browser.close();
    }
  }

  @Test
  public void loginFailureShowsInvalidMessage() {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
      Page page = browser.newPage();
      page.setContent(loginHtml);
      page.fill("#username", "wrong");
      page.fill("#password", "credentials");
      page.click("#submit");
      Assertions.assertEquals("Invalid credentials", page.locator("#message").innerText());
      Assertions.assertEquals("false", page.locator("#message").getAttribute("data-success"));
      browser.close();
    }
  }
}
