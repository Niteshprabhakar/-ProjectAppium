package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

import java.time.Duration;

/**
 * Screen object for the HA app's "Applicant Messages" inbox. Each conversation
 * row is a real accessibility element named {@code "<TYPE>, <subject>"} (e.g.
 * "HOURLY, helloo") - confirmed via a live UI tree dump (LAW-881). Row content
 * is real account data and changes over time, so rows are targeted by
 * position/pattern rather than by literal subject text.
 *
 * <p>Tapping a row only expands it in place to a preview
 * ({@code "<sender>, <last message>, <unread count>, <date>"}), which is the
 * element that actually navigates into the chat screen; the preview is a
 * sibling of the row it belongs to, sharing the same parent node.
 *
 * <p>Tapping an <em>already-expanded</em> row toggles it back closed instead
 * of opening it - confirmed the hard way: an earlier version of this method
 * assumed the row always starts collapsed, which held on a fresh inbox visit
 * but broke the second time the same conversation was reopened in one test
 * (the preview it landed on belonged to a different, newly-shifted-into-place
 * conversation entirely). {@link #openFirstConversation()} now checks whether
 * the sibling preview already exists before deciding whether to tap the row.
 */
public class HaMessagingInboxScreen extends BaseScreen {

    private static final String ROW_PATTERN =
            "(//XCUIElementTypeOther[@accessible='true' and contains(@name, ', ')])";
    private static final By FIRST_ROW = AppiumBy.xpath(ROW_PATTERN + "[1]");
    private static final By FIRST_ROW_PREVIEW = AppiumBy.xpath(ROW_PATTERN + "[1]/following-sibling::*[1]");

    public HaMessagingInboxScreen(IOSDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(FIRST_ROW);
    }

    /** Opens the first conversation in the inbox, regardless of its subject/content. */
    public HaChatScreen openFirstConversation() {
        if (!exists(FIRST_ROW_PREVIEW)) {
            click(FIRST_ROW);
        }
        click(FIRST_ROW_PREVIEW);
        return new HaChatScreen(driver);
    }
}
