package com.lawclerk.qa.appium.screens;

import com.lawclerk.qa.appium.config.ConfigReader;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

/**
 * Common helpers shared by all screen objects. Appium doesn't auto-wait like
 * Playwright, so every lookup goes through an explicit WebDriverWait rather
 * than a hard sleep.
 */
public abstract class BaseScreen {

    protected final IOSDriver driver;
    protected final WebDriverWait wait;

    protected BaseScreen(IOSDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofMillis(ConfigReader.getDefaultTimeoutMs()));
    }

    protected WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        find(locator).click();
    }

    protected void type(By locator, String value) {
        WebElement element = find(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String textOf(By locator) {
        return find(locator).getText();
    }

    /**
     * {@code find} already waits for the element to report visible, so this only needs to know
     * whether that wait succeeded - re-checking {@code isDisplayed()} on the returned element is
     * a second round-trip that can race a React Native re-render and throw a stale-element error.
     */
    protected boolean isVisible(By locator) {
        try {
            find(locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Immediate existence check, no wait - for deciding which of two states a screen is in. */
    protected boolean exists(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /** Clicks the element if it shows up within {@code timeout}; otherwise does nothing. */
    protected void clickIfPresent(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (TimeoutException ignored) {
            // nothing to dismiss
        }
    }

    /**
     * Taps a fixed point instead of an element. Only for controls with no accessibilityLabel and
     * no other way to reach them (e.g. the HA dashboard's hamburger menu icon) - point coordinates
     * don't survive a device/screen-size change the way an accessibility id does, so prefer a real
     * locator whenever one exists.
     */
    protected void tapPoint(int x, int y) {
        driver.executeScript("mobile: tap", Map.of("x", x, "y", y));
    }
}
