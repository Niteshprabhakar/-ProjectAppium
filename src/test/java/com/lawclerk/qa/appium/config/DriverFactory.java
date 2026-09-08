package com.lawclerk.qa.appium.config;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Starts an Appium session on BrowserStack App Automate for the given app.
 * BrowserStack hosts the real iOS device/simulator, so no local Xcode or
 * Simulator is required (dev machines here are Windows - see LAW-881).
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static IOSDriver create(AppTarget target) {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformVersion(ConfigReader.getPlatformVersion())
                .setDeviceName(ConfigReader.getDeviceName())
                .setApp(ConfigReader.getAppUrl(target))
                .setBundleId(ConfigReader.getBundleId(target))
                .amend("bstack:options", browserStackOptions(target));

        try {
            return new IOSDriver(new URL(ConfigReader.getHubUrl()), options);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid BrowserStack hub URL: " + ConfigReader.getHubUrl(), e);
        }
    }

    private static Map<String, Object> browserStackOptions(AppTarget target) {
        Map<String, Object> options = new HashMap<>();
        options.put("userName", ConfigReader.getBrowserStackUsername());
        options.put("accessKey", ConfigReader.getBrowserStackAccessKey());
        options.put("projectName", "LAWCLERK Mobile");
        options.put("buildName", ConfigReader.getBuildName());
        options.put("sessionName", target.name() + " session");
        return options;
    }
}
