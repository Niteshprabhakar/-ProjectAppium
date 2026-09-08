package com.lawclerk.qa.appium.screens.ra;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Screen object for the RA (Remote Associate) app's login screen. Locator
 * values are placeholders - confirm the real accessibilityIdentifier/testID
 * values with mobile dev before pointing this at the app (see LAW-881).
 */
public class RaLoginScreen extends BaseScreen {

    private static final By EMAIL_INPUT = AppiumBy.accessibilityId("ra-login-email");
    private static final By PASSWORD_INPUT = AppiumBy.accessibilityId("ra-login-password");
    private static final By SUBMIT_BUTTON = AppiumBy.accessibilityId("ra-login-submit");
    private static final By ERROR_MESSAGE = AppiumBy.accessibilityId("ra-login-error");

    public RaLoginScreen(IOSDriver driver) {
        super(driver);
    }

    public RaLoginScreen enterEmail(String email) {
        type(EMAIL_INPUT, email);
        return this;
    }

    public RaLoginScreen enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    /** Submits valid credentials and follows the app to the dashboard. */
    public RaDashboardScreen submit() {
        click(SUBMIT_BUTTON);
        return new RaDashboardScreen(driver);
    }

    /** Submits credentials expected to fail and waits for the error to render. */
    public RaLoginScreen submitExpectingFailure() {
        click(SUBMIT_BUTTON);
        wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE));
        return this;
    }

    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }

    public String getErrorMessage() {
        return textOf(ERROR_MESSAGE);
    }
}
