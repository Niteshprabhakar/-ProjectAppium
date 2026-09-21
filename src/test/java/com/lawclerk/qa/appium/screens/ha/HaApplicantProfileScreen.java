package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

/**
 * Screen object for an individual applicant's profile (opened via "REVIEW
 * PROFILE" from {@link HaApplicantsScreen}).
 *
 * <p>Deliberately exposes no way to tap ASSIGN or DECLINE. Both would mutate
 * real seeded applicant/opportunity data (a real hire or a real rejection),
 * and as of LAW-976 neither has an accessibility label anyway - confirmed via
 * a live UI tree dump, a full-file search for "assign"/"decline" returns zero
 * matches despite both buttons being visible on screen. No workaround (such
 * as a coordinate tap) belongs here; this class only covers the read-only
 * part of the screen.
 */
public class HaApplicantProfileScreen extends BaseScreen {

    private static final By VIEW_RESUME = AppiumBy.accessibilityId("VIEW RESUME");
    private static final By CHAT = AppiumBy.accessibilityId("CHAT");

    public HaApplicantProfileScreen(IOSDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(VIEW_RESUME) && isVisible(CHAT);
    }

    /**
     * Back arrow carries no accessibilityLabel - confirmed via a live UI tree dump (LAW-881) -
     * so it's reached by a fixed point tap, matching its frame (x=16,y=45,w=25,h=48).
     *
     * <p>Waits for {@link #isLoaded()} first: a first version tapped immediately after
     * {@code reviewFirstApplicant()} returned and missed - the profile screen was still
     * transitioning in, so the coordinate tap landed before the back control was interactive.
     * A screenshot taken moments later showed the profile fully rendered, confirming the
     * navigation itself was fine and the tap was simply too early.
     */
    public HaApplicantsScreen goBack() {
        wait.until(d -> isLoaded());
        tapPoint(28, 69);
        return new HaApplicantsScreen(driver);
    }
}
