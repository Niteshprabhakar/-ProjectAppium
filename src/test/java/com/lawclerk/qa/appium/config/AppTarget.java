package com.lawclerk.qa.appium.config;

/** The two LAWCLERK iOS apps under test. */
public enum AppTarget {
    HA("ha"),
    RA("ra");

    private final String key;

    AppTarget(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
