import { test, expect } from '@playwright/test';

const loginHtml = `
<!doctype html>
<html>
  <body>
    <form id="login">
      <label>Username: <input name="username" id="username" /></label>
      <label>Password: <input name="password" id="password" type="password"/></label>
      <button id="submit" type="submit">Login</button>
    </form>
    <div id="message" />

    <script>
      const form = document.getElementById('login');
      form.addEventListener('submit', (e) => {
        e.preventDefault();
        const u = document.getElementById('username').value;
        const p = document.getElementById('password').value;
        const msg = document.getElementById('message');
        // fake auth: user / pass
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
`;

test('login success shows success message', async ({ page }) => {
  await page.setContent(loginHtml);
  await page.fill('#username', 'user');
  await page.fill('#password', 'pass');
  await page.click('#submit');
  await expect(page.locator('#message')).toHaveText('Login successful');
  await expect(page.locator('#message')).toHaveAttribute('data-success', 'true');
});

test('login failure shows invalid message', async ({ page }) => {
  await page.setContent(loginHtml);
  await page.fill('#username', 'wrong');
  await page.fill('#password', 'credentials');
  await page.click('#submit');
  await expect(page.locator('#message')).toHaveText('Invalid credentials');
  await expect(page.locator('#message')).toHaveAttribute('data-success', 'false');
});
