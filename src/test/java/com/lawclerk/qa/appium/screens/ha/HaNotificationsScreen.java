package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

/**
 * Screen object for the HA app's Notifications screen (opened from the
 * dashboard's "REVIEW NOTIFICATIONS" tile). The back control carries a real
 * testID ({@code notificationBackButton}) - confirmed via a live UI tree dump
 * (LAW-881), unlike the drawer/hamburger icon and the Applicants profile back
 * arrow.
 *
 * <p>The header's own accessibility name doubles as the unread count, e.g.
 * {@code "Notifications (9)"} with unread items or plain {@code "Notifications"}
 * with none. Tapping any notification row clears the count entirely (all
 * marked read at once) rather than navigating anywhere or clearing just that
 * one row - confirmed via a live run: tapping the first of nine unread rows
 * changed the header straight to "Notifications" with no number.
 *
 * <p>Row text is a small, recurring, system-generated vocabulary ("New
 * Applicant for ...", "... posted to marketplace") and the same text repeats
 * across many rows, so rows are targeted by position, excluding the named
 * back button, rather than by literal text.
 */
public class HaNotificationsScreen extends BaseScreen {

    private static final By BACK_BUTTON = AppiumBy.accessibilityId("notificationBackButton");
    private static final By HEADER = AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Notifications')]");
    private static final By FIRST_NOTIFICATION = AppiumBy.xpath(
            "(//XCUIElementTypeOther[@accessible='true' and @name!='notificationBackButton'])[1]");

    public HaNotificationsScreen(IOSDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(BACK_BUTTON) && isVisible(HEADER);
    }

    public boolean hasUnreadCount() {
        return textOf(HEADER).contains("(");
    }

    public HaNotificationsScreen openFirstNotification() {
        click(FIRST_NOTIFICATION);
        return this;
    }

    public HaDashboardScreen goBack() {
        click(BACK_BUTTON);
        return new HaDashboardScreen(driver);
    }
}
