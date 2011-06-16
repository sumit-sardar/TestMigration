package com.ctb.oas.normsdata;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ContentAreaLookup {
    private static Properties properties = new Properties();
    private static Properties tabe_properties = new Properties();
    private static Properties lasLinksProperties = new Properties();

    static {
        try {
            properties.load(new FileInputStream("conf/contentarea.properties"));
            tabe_properties.load(new FileInputStream("conf/tabe_content_area.properties"));
            lasLinksProperties.load(new FileInputStream("conf/laslinks_content_area.properties"));
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot load contant area names from properties file", e);
        }
    }

    public static String getContentArea(String code) {
        final String property = properties.getProperty(code);
        if (property == null)
            return null;

        return property.trim();
    }

    public static String getTABEContentArea(String line) {
        final String property = tabe_properties.getProperty(line);
        if (property == null)
            return null;

        return property.trim();
    }
    
    public static String getLasLinksContentArea(String line) {
        final String property = lasLinksProperties.getProperty(line);
        if (property == null)
            return null;

        return property.trim();
    }
}
