package com.lawclerk.qa.appium.tests.ha;

import com.lawclerk.qa.appium.base.BaseTest;
import com.lawclerk.qa.appium.config.AppTarget;
import com.lawclerk.qa.appium.config.ConfigReader;
import com.lawclerk.qa.appium.screens.ha.HaApplicantProfileScreen;
import com.lawclerk.qa.appium.screens.ha.HaApplicantsScreen;
import com.lawclerk.qa.appium.screens.ha.HaDashboardScreen;
import com.lawclerk.qa.appium.screens.ha.HaLoginScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * HA Phase 2 coverage: Applicants (dashboard -&gt; Opportunity Type -&gt;
 * Opportunity -&gt; applicant list -&gt; profile). This is the closest the app
 * has to an "assignments" area - see {@link HaApplicantsScreen} and
 * {@link HaApplicantProfileScreen} for why ASSIGN/DECLINE are out of scope
 * for automation entirely (LAW-976: not accessible; also destructive on real
 * seeded data even if they were).
 *
 * <p>"Hello" is a real, currently-populated opportunity in the test account
 * used deliberately for {@link #populatedOpportunityShowsApplicants()} and
 * {@link #reviewProfileOpensApplicantDetails()} - unlike the Messaging tests,
 * this can't be done generically by position, since which opportunities have
 * applicants changes over time on this shared account (confirmed: "Test Push
 * Ra App " had zero applicants earlier in the same session and had gained one
 * by the next run - other QA/dev activity on the account, not a code issue).
 * If "Hello" stops resolving or loses its applicants in the future, that's
 * data drift - pick another currently-populated opportunity, not a fix here.
 * Because of that same drift, there's no reliable "always empty" opportunity
 * to test the empty state against, so that case isn't covered as its own
 * scenario - see {@link #selectingAnOpportunityReachesAValidState()}.
 */
@Tag("mobile")
@Tag("ios")
@Tag("ha")
class HaApplicantsTest extends BaseTest {

    private static final String POPULATED_OPPORTUNITY = "Hello";

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
    @DisplayName("The Applicants screen opens from the dashboard")
    void applicantsScreenOpensFromDashboard() {
        HaApplicantsScreen applicants = dashboard.openApplicants();

        assertTrue(applicants.isLoaded(), "Expected the Opportunity Type field to be visible");
    }

    @Test
    @Tag("regression")
    @DisplayName("Selecting an opportunity reaches a valid state, not a stuck/blank screen")
    void selectingAnOpportunityReachesAValidState() {
        HaApplicantsScreen applicants = dashboard.openApplicants()
                .selectOpportunityType("Hourly Associate")
                .selectOpportunity(POPULATED_OPPORTUNITY);

        assertTrue(applicants.isEmptyState() || applicants.hasApplicants(),
                "Expected either the empty-applicants message or at least one applicant card");
    }

    @Test
    @Tag("regression")
    @DisplayName("A populated opportunity shows at least one applicant")
    void populatedOpportunityShowsApplicants() {
        HaApplicantsScreen applicants = dashboard.openApplicants()
                .selectOpportunityType("Hourly Associate")
                .selectOpportunity(POPULATED_OPPORTUNITY);

        assertTrue(applicants.hasApplicants(), "Expected at least one applicant card with a REVIEW PROFILE action");
    }

    @Test
    @Tag("regression")
    @DisplayName("Reviewing an applicant opens their profile")
    void reviewProfileOpensApplicantDetails() {
        HaApplicantProfileScreen profile = dashboard.openApplicants()
                .selectOpportunityType("Hourly Associate")
                .selectOpportunity(POPULATED_OPPORTUNITY)
                .reviewFirstApplicant();

        assertTrue(profile.isLoaded(), "Expected the applicant profile's VIEW RESUME/CHAT actions to be visible");
    }

    @Test
    @Tag("regression")
    @DisplayName("Leaving an applicant profile returns to the Applicants screen")
    void backFromProfileReturnsToApplicants() {
        HaApplicantsScreen applicants = dashboard.openApplicants()
                .selectOpportunityType("Hourly Associate")
                .selectOpportunity(POPULATED_OPPORTUNITY)
                .reviewFirstApplicant()
                .goBack();

        assertTrue(applicants.isLoaded(), "Expected to land back on the Applicants screen");
    }
}
