package com.lawclerk.qa.appium.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Central place to read run configuration. Precedence: JVM system property
 * (e.g. {@code -Ddevice.name=...}) over {@code config.properties} over the
 * supplied default, mirroring qa/playwright's ConfigReader.
 *
 * <p>BrowserStack credentials are the one exception: they are read only from
 * the environment and never fall back to a default, since they must never be
 * hardcoded in a config file (see qa/CLAUDE.md credentials rule).
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load config.properties", e);
        }
        return properties;
    }

    public static String get(String key, String defaultValue) {
        return System.getProperty(key, PROPERTIES.getProperty(key, defaultValue));
    }

    public static String getBrowserStackUsername() {
        return requireEnv("BROWSERSTACK_USERNAME");
    }

    public static String getBrowserStackAccessKey() {
        return requireEnv("BROWSERSTACK_ACCESS_KEY");
    }

    private static String requireEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    name + " is not set. Export it before running tests - BrowserStack credentials "
                            + "are never read from config files.");
        }
        return value;
    }

    public static String getHubUrl() {
        return get("bstack.hub.url", "https://hub-cloud.browserstack.com/wd/hub");
    }

    public static String getBuildName() {
        return get("bstack.build.name", "lawclerk-mobile-local");
    }

    public static String getDeviceName() {
        return get("device.name", "iPhone 14");
    }

    public static String getPlatformVersion() {
        return get("platform.version", "17");
    }

    public static int getDefaultTimeoutMs() {
        return Integer.parseInt(get("timeout.ms", "30000"));
    }

    public static String getBundleId(AppTarget target) {
        return get("app." + target.key() + ".bundleId", "");
    }

    public static String getAppUrl(AppTarget target) {
        return get("app." + target.key() + ".url", "");
    }

    public static String getTestUserEmail(AppTarget target) {
        return get("test." + target.key() + ".user.email", "qa." + target.key() + ".user@example.com");
    }

    public static String getTestUserPassword(AppTarget target) {
        return get("test." + target.key() + ".user.password", "ChangeMe123!");
    }
}
