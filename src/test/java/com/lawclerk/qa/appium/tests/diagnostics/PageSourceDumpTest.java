package com.lawclerk.qa.appium.tests.diagnostics;

import com.lawclerk.qa.appium.base.BaseTest;
import com.lawclerk.qa.appium.config.AppTarget;
import com.lawclerk.qa.appium.config.ConfigReader;
import com.lawclerk.qa.appium.screens.ha.HaDashboardScreen;
import com.lawclerk.qa.appium.screens.ha.HaLoginScreen;
import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Duration;

/**
 * Throwaway, not part of any suite - logs in as HA, lands on the dashboard,
 * and dumps the live accessibility tree to {@code target/page-sources/} so
 * real locators for the next screen (Post Opportunity) can be read off a
 * live device instead of guessed. Same technique used to confirm the
 * Login/MPIN/dashboard locators in Phase 1 (see LAW-881). Delete this class
 * once the Post Opportunity locators are confirmed and wired into a real
 * screen object - it is a discovery tool, not test coverage.
 */
@Tag("diagnostics")
class PageSourceDumpTest extends BaseTest {

    private static final Path DUMP_DIR = Paths.get("target", "page-sources");

    @Override
    protected AppTarget app() {
        return AppTarget.HA;
    }

    @Test
    void dumpDashboardPageSource() {
        HaDashboardScreen dashboard = new HaLoginScreen(driver)
                .enterEmail(ConfigReader.getTestUserEmail(AppTarget.HA))
                .enterPassword(ConfigReader.getTestUserPassword(AppTarget.HA))
                .submit()
                .enterMpin(ConfigReader.getTestUserMpin(AppTarget.HA))
                .submit();

        if (!dashboard.isLoaded()) {
            throw new IllegalStateException("Dashboard never loaded - can't dump a screen that isn't there");
        }

        dump("ha-dashboard");

        // Confirmed via the ha-dashboard dump: the visible "HIRE PART-TIME, (Independent
        // Contractors)" tile is the entry point into the Hourly Associate posting flow.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(ConfigReader.getDefaultTimeoutMs()));
        wait.until(ExpectedConditions.elementToBeClickable(
                        AppiumBy.accessibilityId("HIRE PART-TIME, (Independent Contractors)")))
                .click();
        // The bottom sheet's own content (buttons/text) isn't in the tree yet immediately after
        // the tap - a first dump here caught the sheet as a childless leaf node mid-animation.
        sleep(Duration.ofSeconds(3));
        dump("ha-post-opportunity-step1");
        screenshot("ha-post-opportunity-step1");
    }

    // Messaging discovery (drawer -> inbox -> chat, empty-send behavior, and the reopen/accordion
    // investigation) has all been removed from here - those locators and findings are confirmed
    // and now live in HaDrawerScreen/HaMessagingInboxScreen/HaChatScreen, exercised by
    // HaMessagingTest (6 tests, all passing against a real device). See
    // target/page-sources/ha-{drawer-open,messaging-*,empty-send-*,reopen-*}.* for the original
    // dumps/screenshots if that history is ever needed again.

    private static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for the bottom sheet to render", e);
        }
    }

    private void screenshot(String name) {
        try {
            File source = driver.getScreenshotAs(OutputType.FILE);
            Files.createDirectories(DUMP_DIR);
            Path dest = DUMP_DIR.resolve(name + ".png");
            Files.copy(source.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Saved screenshot to " + dest.toAbsolutePath());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save screenshot", e);
        }
    }

    private void dump(String name) {
        try {
            Files.createDirectories(DUMP_DIR);
            Path dest = DUMP_DIR.resolve(name + ".xml");
            Files.writeString(dest, driver.getPageSource(), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Dumped page source to " + dest.toAbsolutePath());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write page source dump", e);
        }
    }
}
