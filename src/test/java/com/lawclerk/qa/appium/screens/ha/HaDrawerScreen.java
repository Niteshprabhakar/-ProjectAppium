package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

/**
 * Screen object for the HA app's hamburger drawer (Dashboard, Hire Full-Time,
 * Hire Part-Time, Applicants, Manage Conflicts, Messages, Logout). Every item
 * carries a real accessibilityLabel matching its visible text - confirmed via
 * a live UI tree dump (LAW-881), unlike the Post Opportunity bottom sheet
 * (LAW-975).
 */
public class HaDrawerScreen extends BaseScreen {

    private static final By MESSAGES_ITEM = AppiumBy.accessibilityId("Messages");

    public HaDrawerScreen(IOSDriver driver) {
        super(driver);
    }

    public HaMessagingInboxScreen openMessages() {
        click(MESSAGES_ITEM);
        return new HaMessagingInboxScreen(driver);
    }
}
