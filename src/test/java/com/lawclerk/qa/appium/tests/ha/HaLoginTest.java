package com.lawclerk.qa.appium.tests.ha;

import com.lawclerk.qa.appium.base.BaseTest;
import com.lawclerk.qa.appium.config.AppTarget;
import com.lawclerk.qa.appium.config.ConfigReader;
import com.lawclerk.qa.appium.screens.ha.HaDashboardScreen;
import com.lawclerk.qa.appium.screens.ha.HaLoginScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * HA login smoke coverage - the first real test through the full pipe
 * (BrowserStack -> XCUITest -> HA app). Run with: mvn test -Dgroups=smoke
 */
@Tag("mobile")
@Tag("ios")
@Tag("ha")
class HaLoginTest extends BaseTest {

    private HaLoginScreen loginScreen;

    @Override
    protected AppTarget app() {
        return AppTarget.HA;
    }

    @BeforeEach
    void openLoginScreen() {
        loginScreen = new HaLoginScreen(driver);
    }

    @Test
    @Tag("smoke")
    @Tag("regression")
    @DisplayName("Valid credentials log the Hiring Attorney in and land on the dashboard")
    void validLogin_redirectsToDashboard() {
        HaDashboardScreen dashboard = loginScreen
                .enterEmail(ConfigReader.getTestUserEmail(AppTarget.HA))
                .enterPassword(ConfigReader.getTestUserPassword(AppTarget.HA))
                .submit()
                .enterMpin(ConfigReader.getTestUserMpin(AppTarget.HA))
                .submit();

        assertTrue(dashboard.isLoaded(), "Expected the HA dashboard to load after a valid login");
    }

    @Test
    @Tag("regression")
    @Tag("negative")
    @DisplayName("An incorrect password is rejected with an error message")
    void invalidPassword_showsError() {
        loginScreen.enterEmail(ConfigReader.getTestUserEmail(AppTarget.HA))
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
