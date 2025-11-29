# Playwright TypeScript Project

Quickstart:

1. Install dependencies

```powershell
cd .\newplay
npm install
npx playwright install
```

2. Run tests

```powershell
npm test
# or run headed
npm run test:headed
```

Notes:
- This project uses Playwright Test with TypeScript.
- CI workflow is provided in `.github/workflows/playwright.yml`.
- Recommended VS Code extension: `ms-playwright.playwright`.

Recording & converting browser flows (Playwright Recorder)
-------------------------------------------------------

To record a manual flow and convert it into a Playwright Test:

1. Launch the recorder (PowerShell):

```powershell
npx playwright codegen https://www.linkedin.com
```

2. Interact with the page in the Recorder window. The recorder will generate code in the right panel.
3. Copy the generated code and paste it into `tests/linkedin.spec.ts` inside the RECORDER_PASTE_AREA.
4. Edit the pasted script to replace fragile selectors with stable ones, add expect() assertions, and remove any sensitive data (credentials).
5. Run the test suite:

```powershell
npm test -- --grep "@linkedin"
```

If you want, paste the recorded code here (or upload the file) and I will convert and harden it into a robust Playwright Test for you.
