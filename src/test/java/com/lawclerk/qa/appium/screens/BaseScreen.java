package com.lawclerk.qa.appium.screens;

import com.lawclerk.qa.appium.config.ConfigReader;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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

    protected boolean isVisible(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
