package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;

/**
 * Screen object for the HA (Hiring Attorney) app's login screen. The email
 * and password fields carry no accessibilityIdentifier in the real app, so
 * they're located by element type instead (confirmed via a live UI tree
 * dump - see LAW-881). A failed login surfaces a native iOS alert, not an
 * in-app error element.
 */
public class HaLoginScreen extends BaseScreen {

    private static final By EMAIL_INPUT = AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeTextField'");
    private static final By PASSWORD_INPUT = AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeSecureTextField'");
    private static final By SUBMIT_BUTTON = AppiumBy.accessibilityId("LOGIN");
    private static final By RETURN_KEY = AppiumBy.accessibilityId("Return");

    public HaLoginScreen(IOSDriver driver) {
        super(driver);
    }

    public HaLoginScreen enterEmail(String email) {
        type(EMAIL_INPUT, email);
        return this;
    }

    public HaLoginScreen enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    /** Submits valid credentials and moves on to the MPIN step. */
    public HaMpinScreen submit() {
        dismissKeyboard();
        click(SUBMIT_BUTTON);
        return new HaMpinScreen(driver);
    }

    /** Submits credentials expected to fail and waits for the native error alert. */
    public HaLoginScreen submitExpectingFailure() {
        dismissKeyboard();
        click(SUBMIT_BUTTON);
        wait.until(d -> isErrorDisplayed());
        return this;
    }

    /**
     * The submit button sits under the keyboard, so it must be dismissed first or the tap misses
     * it. The app doesn't handle {@code mobile: hideKeyboard}'s default strategies, so the
     * keyboard's own Return key is tapped directly instead (confirmed via a live UI tree dump -
     * this does not submit the form itself, just dismisses the keyboard).
     */
    private void dismissKeyboard() {
        try {
            driver.findElement(RETURN_KEY).click();
        } catch (NoSuchElementException ignored) {
            // keyboard already dismissed
        }
    }

    public boolean isErrorDisplayed() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return driver.switchTo().alert().getText();
    }
}
