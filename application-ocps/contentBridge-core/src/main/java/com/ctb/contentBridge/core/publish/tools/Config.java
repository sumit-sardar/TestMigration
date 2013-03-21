package com.ctb.contentBridge.core.publish.tools;

import java.io.*;
import java.util.*;

import com.ctb.contentBridge.core.exception.SystemException;

abstract public class Config {
    public static final File userPropertiesFile = new File("PROPERTIES_FILE_PATH");
    public static final String CONFIG_TRUE = "true";
    public static final String CONFIG_FALSE = "false";
	protected Properties properties;
	protected File file;
	
    /**
     * @deprecated use Config(File)
     * @param configDir
     * @param environment
     * @throws IOException
     */
    protected void load(File configDir, String environment) {
        environment = environment.toLowerCase();
        if (environment.equals("automated_test")) {
            load(userPropertiesFile);
        } else {
            File propertiesInConfigDir =
                new File(configDir, environment + ".properties");
            File propertiesInCurrentDir =
                new File(".", environment + ".properties");
            File propertiesFile = propertiesInConfigDir;

            if (!propertiesFile.exists()) {
                propertiesFile = propertiesInCurrentDir;
            }
            if (!propertiesFile.exists()) {
                throw new IllegalArgumentException(
                    "Could not load properties for environment '"
                        + environment
                        + "'"
                        + ": Missing file "
                        + propertiesInConfigDir.getAbsolutePath()
                        + " or "
                        + propertiesInCurrentDir.getAbsolutePath());
            }
            load(propertiesFile);
        }
    }

    protected void load(File file) {
    	this.file = file;
        properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        }
        readProperties();
    }

    abstract protected void readProperties();

	protected String getRequiredProperty(
        Properties properties,
        String key,
        File file) {
        String property = properties.getProperty(key);

        if (property == null) {
            throw new RuntimeException(
                "Missing property '"
                    + key
                    + "' in file "
                    + file.getAbsolutePath());
        }
        return property;
    }

	protected String getOptionalProperty(
        Properties properties,
        String key,
        File file,
        String defaultValue) {
        String property = properties.getProperty(key);
        if (property == null)
            return defaultValue;
        return property;
    }

    protected boolean getBooleanPropertyOptional(
        Properties properties,
        String key,
        File file,
        boolean defaultValue) {
        String property = properties.getProperty(key);
        if (property == null)
            return defaultValue;
        if (property.equals(CONFIG_TRUE))
            return true;
        if (property.equals(CONFIG_FALSE))
            return false;
        return defaultValue;
    }

}
