package com.lawclerk.qa.appium.base;

import com.lawclerk.qa.appium.config.AppTarget;
import com.lawclerk.qa.appium.config.DriverFactory;
import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for iOS test classes. Each test method gets its own Appium
 * session against BrowserStack ({@code @BeforeEach}/{@code @AfterEach}) -
 * unlike a browser context, an app session can't be cheaply reset mid-test.
 *
 * <p>On failure, a screenshot is saved to {@code target/screenshots} to
 * support the "evidence" field of the bug report format.
 */
public abstract class BaseTest {

    private static final Logger LOGGER = Logger.getLogger(BaseTest.class.getName());
    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");

    protected IOSDriver driver;

    @RegisterExtension
    final TestWatcher screenshotOnFailure = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext extensionContext, Throwable cause) {
            captureScreenshot(extensionContext.getDisplayName());
        }
    };

    protected abstract AppTarget app();

    @BeforeEach
    void startSession() {
        driver = DriverFactory.create(app());
    }

    @AfterEach
    void endSession() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void captureScreenshot(String testName) {
        if (driver == null) {
            return;
        }
        try {
            File source = driver.getScreenshotAs(OutputType.FILE);
            Files.createDirectories(SCREENSHOT_DIR);
            Path dest = SCREENSHOT_DIR.resolve(sanitize(testName) + ".png");
            Files.copy(source.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info(() -> "Saved failure screenshot: " + dest.toAbsolutePath());
        } catch (IOException | RuntimeException e) {
            LOGGER.log(Level.WARNING, "Could not capture failure screenshot", e);
        }
    }

    private static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
