package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

/**
 * Screen object for the HA app's post-login dashboard. Locator confirmed via
 * a live UI tree dump (see LAW-881).
 */
public class HaDashboardScreen extends BaseScreen {

    private static final By DASHBOARD_ROOT = AppiumBy.accessibilityId("homeScrollView");

    public HaDashboardScreen(IOSDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(DASHBOARD_ROOT);
    }

    /**
     * Opens the hamburger drawer (Messages, Applicants, Manage Conflicts, etc). The icon carries
     * no accessibilityLabel - confirmed via a live UI tree dump (LAW-881) - so it's reached by a
     * fixed point tap instead of a real locator. Position matches the header icon's frame at the
     * configured device/point-width (x=0,y=59,w=36,h=36 on the default iPhone 14 profile).
     */
    public HaDrawerScreen openMenu() {
        tapPoint(18, 77);
        return new HaDrawerScreen(driver);
    }

    private static final By APPLICANTS_TILE = AppiumBy.accessibilityId("APPLICANTS");

    /** Opens the Applicants screen from its dashboard tile. */
    public HaApplicantsScreen openApplicants() {
        click(APPLICANTS_TILE);
        return new HaApplicantsScreen(driver);
    }

    // Real name confirmed via a live UI tree dump (LAW-881): a literal newline between the two
    // words, not a space - "REVIEW" then a line break then "NOTIFICATIONS".
    private static final By NOTIFICATIONS_TILE = AppiumBy.accessibilityId("REVIEW \nNOTIFICATIONS");

    /** Opens the Notifications screen from its dashboard tile. */
    public HaNotificationsScreen openNotifications() {
        click(NOTIFICATIONS_TILE);
        return new HaNotificationsScreen(driver);
    }

    private static final By HIRE_PART_TIME_TILE = AppiumBy.accessibilityId("HIRE PART-TIME, (Independent Contractors)");

    /**
     * Opens the "What would you like to do?" bottom sheet, picks "Hire an Hourly Associate" (which
     * expands an in-place accordion rather than navigating), then picks "CREATE A NEW HOURLY
     * OPPORTUNITY" from it to actually reach the posting form. Every option in this whole sheet -
     * including the accordion's own sub-options - has zero accessibility exposure (LAW-975 - not
     * filed as a bug since VoiceOver may still handle a native/RN component differently than
     * XCUITest's snapshot can see; unblocking automation here was an explicit, deliberate call),
     * so each is reached by a coordinate tap estimated from a screenshot (device 390pt wide,
     * screenshot 1170px wide, ratio 3). Confirmed via a live run that this exact tap sequence
     * lands on the real "Hire an Hourly Associate" posting form (Title/Area of Law/Description/...).
     */
    public void startHiringAnHourlyAssociate() {
        click(HIRE_PART_TIME_TILE);
        sleep(2000);
        tapPoint(196, 643);
        sleep(2000);
        tapPoint(196, 603);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for the bottom sheet to render", e);
        }
    }
}
