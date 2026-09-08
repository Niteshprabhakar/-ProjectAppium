package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Screen object for the HA (Hiring Attorney) app's login screen. Locator
 * values are placeholders - confirm the real accessibilityIdentifier/testID
 * values with mobile dev before pointing this at the app (see LAW-881).
 */
public class HaLoginScreen extends BaseScreen {

    private static final By EMAIL_INPUT = AppiumBy.accessibilityId("ha-login-email");
    private static final By PASSWORD_INPUT = AppiumBy.accessibilityId("ha-login-password");
    private static final By SUBMIT_BUTTON = AppiumBy.accessibilityId("ha-login-submit");
    private static final By ERROR_MESSAGE = AppiumBy.accessibilityId("ha-login-error");

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

    /** Submits valid credentials and follows the app to the dashboard. */
    public HaDashboardScreen submit() {
        click(SUBMIT_BUTTON);
        return new HaDashboardScreen(driver);
    }

    /** Submits credentials expected to fail and waits for the error to render. */
    public HaLoginScreen submitExpectingFailure() {
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
