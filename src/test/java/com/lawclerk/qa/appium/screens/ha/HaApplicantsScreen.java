package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

/**
 * Screen object for the HA app's "Applicants" screen (opened from the
 * dashboard's APPLICANTS tile). Reviewing/managing who's applied to an
 * opportunity - this is the closest the app has to an "assignments" area.
 *
 * <p>Two cascading dropdowns - Opportunity Type (fixed values: "Full-Time
 * Job", "Hourly Associate", "Flat Fee Project") and Opportunity (dynamic,
 * real account data) - both render as an accessibility element literally
 * named {@code "SELECT"} before a value is chosen, so they're targeted by
 * their preceding label instead of by name. Neither picker sheet has a
 * close/backdrop control, so both are dismissed the same way: tapping the
 * dimmed area above the sheet - confirmed via a live UI tree dump (LAW-881).
 */
public class HaApplicantsScreen extends BaseScreen {

    private static final By OPPORTUNITY_TYPE_FIELD = AppiumBy.xpath(
            "//XCUIElementTypeStaticText[@name='Opportunity Type*']/following-sibling::XCUIElementTypeOther[1]");
    private static final By OPPORTUNITY_FIELD = AppiumBy.xpath(
            "//XCUIElementTypeStaticText[@name='Opportunity*']/following-sibling::XCUIElementTypeOther[1]");

    public HaApplicantsScreen(IOSDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(OPPORTUNITY_TYPE_FIELD);
    }

    /** {@code type} must be one of the app's fixed values, e.g. "Hourly Associate". */
    public HaApplicantsScreen selectOpportunityType(String type) {
        click(OPPORTUNITY_TYPE_FIELD);
        click(AppiumBy.accessibilityId(type));
        dismissPickerSheet();
        return this;
    }

    /** {@code opportunityName} is real account data - confirm it exists for the chosen type. */
    public HaApplicantsScreen selectOpportunity(String opportunityName) {
        click(OPPORTUNITY_FIELD);
        click(AppiumBy.accessibilityId(opportunityName));
        dismissPickerSheet();
        return this;
    }

    public boolean isEmptyState() {
        return isVisible(AppiumBy.accessibilityId("There Are Currently No Applicants."));
    }

    public boolean hasApplicants() {
        return isVisible(AppiumBy.xpath("(//XCUIElementTypeOther[@name='REVIEW PROFILE'])[1]"));
    }

    /** First applicant card's read-only profile action - never DECLINE, see class docs on the profile screen. */
    public HaApplicantProfileScreen reviewFirstApplicant() {
        click(AppiumBy.xpath("(//XCUIElementTypeOther[@name='REVIEW PROFILE'])[1]"));
        return new HaApplicantProfileScreen(driver);
    }

    private void dismissPickerSheet() {
        tapPoint(195, 100);
    }
}
