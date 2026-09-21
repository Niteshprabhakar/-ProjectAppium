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

    /**
     * Opens the hamburger drawer (Messages, Applicants, Manage Conflicts, etc). The icon carries
     * no accessibilityLabel - confirmed via a live UI tree dump (LAW-881) - so it's reached by a
     * fixed point tap instead of a real locator. Position matches the header icon's frame at the
     * configured device/point-width (x=0,y=59,w=36,h=36 on the default iPhone 14 profile).
     */
    public HaDrawerScreen openMenu() {
        tapPoint(18, 77);
        return new HaDrawerScreen(driver);
    }
}
