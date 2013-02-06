/*
 * Created on Feb 2, 2004
 *
 */
package com.ctb.contentBridge.core.publish.tools;

import java.io.File;

import org.apache.commons.lang.StringUtils;

public class OCSConfig extends Config {
    private String[] emailTo = { "bobby_magee@mcgraw-hill.com" };
    private String emailFrom = "bobby_magee@mcgraw-hill.com";
    public static final String DEFAULT_SMTP_HOST = "mhemtymsg01.mhe.mhc";
    private String smtpHost = DEFAULT_SMTP_HOST;
    private boolean emailEnabled = true;
    private boolean isTest = false;

    public OCSConfig() {
        this(userPropertiesFile);
    }

    public OCSConfig(File file) {
        load(file);
    }

    protected void readProperties() {
        emailTo =
            StringUtils.split(
                getRequiredProperty(properties, "email.to", file),
                ",");
        emailFrom = getRequiredProperty(properties, "email.from", file);
        smtpHost =
            getOptionalProperty(properties, "email.smtpHost", file, smtpHost);
        emailEnabled =
            getBooleanPropertyOptional(
                properties,
                "email.enabled",
                file,
                emailEnabled);
        isTest =
            getBooleanPropertyOptional(properties, "email.test", file, isTest);
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public String[] getEmailTo() {
        return emailTo;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public boolean isEnabled() {
        return emailEnabled;
    }

    public boolean isTest() {
        return isTest;
    }

    public String getOptionalProperty(String key, String defaultValue) {
        return getOptionalProperty(properties, key, file, defaultValue);
    }
}
