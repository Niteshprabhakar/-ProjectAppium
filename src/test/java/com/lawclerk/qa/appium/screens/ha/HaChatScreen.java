package com.lawclerk.qa.appium.screens.ha;

import com.lawclerk.qa.appium.screens.BaseScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

/**
 * Screen object for the HA app's chat conversation screen (opened from a
 * conversation preview in {@link HaMessagingInboxScreen}). The back and send
 * controls carry real testIDs ({@code chatBackButton}, {@code chatSendButton})
 * - confirmed via a live UI tree dump (LAW-881). The compose field itself has
 * no accessibilityIdentifier, so it's located by element type instead, same
 * approach as {@link HaLoginScreen}'s email/password fields - it's the only
 * text field on this screen.
 */
public class HaChatScreen extends BaseScreen {

    private static final By SEND_BUTTON = AppiumBy.accessibilityId("chatSendButton");
    private static final By BACK_BUTTON = AppiumBy.accessibilityId("chatBackButton");
    private static final By MESSAGE_INPUT = AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeTextField'");

    public HaChatScreen(IOSDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(SEND_BUTTON);
    }

    public HaChatScreen typeMessage(String message) {
        type(MESSAGE_INPUT, message);
        return this;
    }

    public HaChatScreen send() {
        click(SEND_BUTTON);
        return this;
    }

    public boolean isSendButtonEnabled() {
        return find(SEND_BUTTON).isEnabled();
    }

    /** True once a bubble containing {@code text} appears in the transcript. */
    public boolean isMessageVisible(String text) {
        return isVisible(AppiumBy.iOSNsPredicateString(
                "type == 'XCUIElementTypeStaticText' AND name == '" + text + "'"));
    }

    public HaMessagingInboxScreen goBack() {
        click(BACK_BUTTON);
        return new HaMessagingInboxScreen(driver);
    }
}
