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
}
