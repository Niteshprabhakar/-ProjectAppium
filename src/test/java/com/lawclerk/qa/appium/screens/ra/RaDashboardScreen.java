package com.lawclerk.qa.appium.screens.ra;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

/**
 * Screen object for the RA app's post-login dashboard. Locator value is a
 * placeholder - confirm with mobile dev (see LAW-881).
 */
public class RaDashboardScreen extends BaseScreen {

    private static final By DASHBOARD_ROOT = AppiumBy.accessibilityId("ra-dashboard-root");

    public RaDashboardScreen(IOSDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(DASHBOARD_ROOT);
    }
}
