package com.lawclerk.qa.appium.tests.ra;

import com.lawclerk.qa.appium.base.BaseTest;
import com.lawclerk.qa.appium.config.AppTarget;
import com.lawclerk.qa.appium.config.ConfigReader;
import com.lawclerk.qa.appium.screens.ra.RaDashboardScreen;
import com.lawclerk.qa.appium.screens.ra.RaLoginScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RA login smoke coverage - the first real test through the full pipe
 * (BrowserStack -> XCUITest -> RA app). Run with: mvn test -Dgroups=smoke
 */
@Tag("mobile")
@Tag("ios")
@Tag("ra")
@Disabled("Pending RA app build, BrowserStack app_url, and confirmed accessibility IDs - see LAW-881")
class RaLoginTest extends BaseTest {

    private RaLoginScreen loginScreen;

    @Override
    protected AppTarget app() {
        return AppTarget.RA;
    }

    @BeforeEach
    void openLoginScreen() {
        loginScreen = new RaLoginScreen(driver);
    }

    @Test
    @Tag("smoke")
    @Tag("regression")
    @DisplayName("Valid credentials log the Remote Associate in and land on the dashboard")
    void validLogin_redirectsToDashboard() {
        RaDashboardScreen dashboard = loginScreen
                .enterEmail(ConfigReader.getTestUserEmail(AppTarget.RA))
                .enterPassword(ConfigReader.getTestUserPassword(AppTarget.RA))
                .submit();

        assertTrue(dashboard.isLoaded(), "Expected the RA dashboard to load after a valid login");
    }

    @Test
    @Tag("regression")
    @Tag("negative")
    @DisplayName("An incorrect password is rejected with an error message")
    void invalidPassword_showsError() {
        loginScreen.enterEmail(ConfigReader.getTestUserEmail(AppTarget.RA))
                .enterPassword("wrong-password")
                .submitExpectingFailure();

        assertTrue(loginScreen.isErrorDisplayed(), "Expected an error message for an invalid password");
    }

    @Test
    @Tag("regression")
    @Tag("boundary")
    @DisplayName("Empty credentials do not submit successfully")
    void emptyCredentials_showsError() {
        loginScreen.enterEmail("").enterPassword("").submitExpectingFailure();

        assertTrue(loginScreen.isErrorDisplayed(), "Expected a validation error for empty credentials");
    }
}
