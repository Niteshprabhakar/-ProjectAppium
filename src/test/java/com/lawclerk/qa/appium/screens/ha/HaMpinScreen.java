package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

/**
 * Screen object for the HA app's MPIN entry step, shown after a successful
 * email/password login. Each digit is its own field with a real
 * accessibilityIdentifier (confirmed via a live UI tree dump - LAW-881). The
 * submit button sits under the on-screen keypad, so the keyboard's "Done"
 * accessory must be dismissed first or the tap misses it.
 */
public class HaMpinScreen extends BaseScreen {

    private static final By DIGIT_1 = AppiumBy.accessibilityId("mpinDigit1");
    private static final By DIGIT_2 = AppiumBy.accessibilityId("mpinDigit2");
    private static final By DIGIT_3 = AppiumBy.accessibilityId("mpinDigit3");
    private static final By DIGIT_4 = AppiumBy.accessibilityId("mpinDigit4");
    private static final By DONE_KEY = AppiumBy.accessibilityId("Done");
    private static final By SUBMIT_BUTTON = AppiumBy.accessibilityId("mpinLoginButton");
    private static final By CONFLICT_NOTIFICATION_CLOSE = AppiumBy.accessibilityId("CLOSE");

    public HaMpinScreen(IOSDriver driver) {
        super(driver);
    }

    public HaMpinScreen enterMpin(String mpin) {
        type(DIGIT_1, String.valueOf(mpin.charAt(0)));
        type(DIGIT_2, String.valueOf(mpin.charAt(1)));
        type(DIGIT_3, String.valueOf(mpin.charAt(2)));
        type(DIGIT_4, String.valueOf(mpin.charAt(3)));
        return this;
    }

    public HaDashboardScreen submit() {
        try {
            driver.findElement(DONE_KEY).click();
        } catch (NoSuchElementException ignored) {
            // no keyboard accessory toolbar present
        }
        wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON)).click();
        // Some accounts have a pending-conflict opportunity assignment that shows a blocking
        // native alert on login - dismiss it if present so the dashboard becomes reachable.
        clickIfPresent(CONFLICT_NOTIFICATION_CLOSE, Duration.ofSeconds(5));
        return new HaDashboardScreen(driver);
    }
}
