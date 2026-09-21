package com.lawclerk.qa.appium.tests.ha;

import com.lawclerk.qa.appium.base.BaseTest;
import com.lawclerk.qa.appium.config.AppTarget;
import com.lawclerk.qa.appium.config.ConfigReader;
import com.lawclerk.qa.appium.screens.ha.HaDashboardScreen;
import com.lawclerk.qa.appium.screens.ha.HaLoginScreen;
import com.lawclerk.qa.appium.screens.ha.HaNotificationsScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * HA Phase 2 coverage: Notifications (dashboard -&gt; notification feed).
 * Unread count is real, changing account data - confirmed via a live run
 * that opening the screen alone does NOT clear it, but tapping any row does,
 * for the whole feed at once - so {@link #openingANotificationClearsTheUnreadBadge()}
 * asserts the post-tap outcome rather than a specific starting count.
 */
@Tag("mobile")
@Tag("ios")
@Tag("ha")
class HaNotificationsTest extends BaseTest {

    private HaDashboardScreen dashboard;

    @Override
    protected AppTarget app() {
        return AppTarget.HA;
    }

    @BeforeEach
    void logIn() {
        dashboard = new HaLoginScreen(driver)
                .enterEmail(ConfigReader.getTestUserEmail(AppTarget.HA))
                .enterPassword(ConfigReader.getTestUserPassword(AppTarget.HA))
                .submit()
                .enterMpin(ConfigReader.getTestUserMpin(AppTarget.HA))
                .submit();
    }

    @Test
    @Tag("smoke")
    @Tag("regression")
    @DisplayName("The Notifications screen opens from the dashboard")
    void notificationsScreenOpensFromDashboard() {
        HaNotificationsScreen notifications = dashboard.openNotifications();

        assertTrue(notifications.isLoaded(), "Expected the Notifications header/back button to be visible");
    }

    @Test
    @Tag("regression")
    @DisplayName("Opening a notification clears the unread badge")
    void openingANotificationClearsTheUnreadBadge() {
        HaNotificationsScreen notifications = dashboard.openNotifications()
                .openFirstNotification();

        assertFalse(notifications.hasUnreadCount(), "Expected no unread count in the header after opening a notification");
    }

    @Test
    @Tag("regression")
    @DisplayName("The back button returns to the dashboard")
    void backButtonReturnsToDashboard() {
        HaDashboardScreen dashboardAgain = dashboard.openNotifications().goBack();

        assertTrue(dashboardAgain.isLoaded(), "Expected to land back on the dashboard");
    }
}
