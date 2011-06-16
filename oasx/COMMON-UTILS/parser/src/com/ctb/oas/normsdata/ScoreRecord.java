package com.ctb.oas.normsdata;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScoreRecord {
    private Map values = new HashMap();
    private static Properties properties = new Properties();
    private static String propertyFileName = "conf/schema.properties";

    static {
        try {
            properties.load(new FileInputStream(propertyFileName));
        }
        catch (IOException e) {
            throw new RuntimeException("Could not load SQL properties");
        }
    }

    public static final String NORMS_YEAR_VALUE = (String) properties.get("NormsYearValue");
    public static final String TERRANOVA_FRAMEWORK_CODE = (String) properties.get("TerraNovaFrameworkCode");
    public static final String TABE_FRAMEWORK_CODE = (String) properties.get("TABEFrameworkCode");
    public static final String TABE9_BATTERY_NAME = (String) properties.get("TABE9_Battery_Name");
    public static final String TABE9_SURVEY_NAME = (String) properties.get("TABE9_Survey_Name");
    public static final String TABE10_BATTERY_NAME = (String) properties.get("TABE10_Battery_Name");
    public static final String TABE10_SURVEY_NAME = (String) properties.get("TABE10_Survey_Name");
    public static final String TERRANOVA_PRODUCT_NAME = (String) properties.get("TerraNova_Product_Name");
    public static final String TABE_FORM_VALUE = (String) properties.get("TABE_FORM");
    public static final String TABE_ADULT_CODE = "A";
    public static final String TABE_JUVENILE_CODE = "J";
    public static final String EXTENDED_FLAG_VALUE = (String) properties.get("ExtendedFlag_Value");
    
    public static final String LASLINKS_FRAMEWORK_CODE = (String) properties.get("LasLinksFrameworkCode");
    public static final String LASLINKS_PRODUCT_NAME = (String) properties.get("LasLinks_Product_Name");

    public void putValue(Column column, Object value) {
        values.put(column, value);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < Column.ALL.length; i++) {
            Column column = Column.ALL[i];
            Object value = values.get(column);
            if (value != null)
                buffer.append(value.toString());
            if (i != Column.ALL.length - 1)
                buffer.append(',');
        }
        return buffer.toString();
    }
}