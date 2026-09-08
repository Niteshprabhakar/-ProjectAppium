# LAWCLERK QA Mobile Automation Framework (Appium + Java, iOS via BrowserStack)

Appium + Java + JUnit 5 iOS automation for the **HA** (Hiring Attorney) and
**RA** (Remote Associate) mobile apps, run against BrowserStack App Automate.
See `qa/CLAUDE.md` for shared QA conventions, `qa/playwright` for the web UI
suite this mirrors, and Linear ticket LAW-881 for the full framework plan and
open dependencies (app builds, bundle IDs, testIDs).

Dev machines here are Windows, and Appium's iOS driver (XCUITest) requires
Xcode + Simulator, which only run on macOS. BrowserStack hosts the real
device remotely instead, so no local Mac is needed.

## Structure

```
qa/appium/
├── pom.xml
└── src/test/
    ├── java/com/lawclerk/qa/appium/
    │   ├── base/BaseTest.java          # Appium session lifecycle, screenshot-on-failure
    │   ├── config/
    │   │   ├── AppTarget.java          # HA / RA enum
    │   │   ├── ConfigReader.java       # config.properties + -D overrides; BrowserStack creds from env only
    │   │   └── DriverFactory.java      # builds XCUITestOptions + bstack:options, connects to the BrowserStack hub
    │   ├── screens/
    │   │   ├── BaseScreen.java         # explicit-wait helpers (Appium doesn't auto-wait)
    │   │   ├── ha/                     # HaLoginScreen, HaDashboardScreen
    │   │   └── ra/                     # RaLoginScreen, RaDashboardScreen
    │   └── tests/
    │       ├── ha/HaLoginTest.java     # @Disabled until a real HA build is wired in
    │       └── ra/RaLoginTest.java     # @Disabled until a real RA build is wired in
    └── resources/config.properties     # device/platform/hub config, per-app bundleId/app_url, test creds
```

## One-time setup

1. Get a BrowserStack App Automate account (confirm with Nitin whether the
   company already has one before starting a trial).
2. Export credentials as environment variables - never put these in
   `config.properties` or commit them:
   ```bash
   export BROWSERSTACK_USERNAME=your_username
   export BROWSERSTACK_ACCESS_KEY=your_access_key
   ```
3. Get the HA and RA `.ipa` builds and bundle IDs from mobile dev, then
   upload each `.ipa` to BrowserStack to get an `app_url`:
   ```bash
   curl -u "$BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY" \
     -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
     -F "file=@/path/to/HA.ipa"
   ```
4. Fill in `src/test/resources/config.properties` with the returned
   `app_url` and bundle ID for each app (`app.ha.url`, `app.ha.bundleId`,
   `app.ra.url`, `app.ra.bundleId`).
5. Confirm the real accessibility IDs/testIDs for each screen with mobile dev
   and update the screen object locators (currently placeholders, e.g.
   `ha-login-email`).
6. Remove `@Disabled` from `HaLoginTest`/`RaLoginTest` once the above is in
   place.

## Running tests

```bash
mvn test                              # everything (once tests are enabled)
mvn test -Dgroups=smoke               # only @Tag("smoke")
mvn test -Dgroups=regression          # only @Tag("regression")
mvn test -Ddevice.name="iPhone 15" -Dplatform.version=17
```

`device.name`, `platform.version`, `bstack.hub.url`, `bstack.build.name`, and
any `config.properties` key can be overridden on the command line without
editing files.
