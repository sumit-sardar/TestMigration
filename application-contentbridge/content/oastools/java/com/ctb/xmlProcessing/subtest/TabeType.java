package com.ctb.xmlProcessing.subtest;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Aug 24, 2004
 * Time: 9:00:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class TabeType {

    public static final String APPLIED_MATH_TITLE = "Applied Mathematics";
    public static final String MATH_COMP_TITLE= "Math Computation";
    public static final String READING_TITLE= "Reading";
    public static final String LANGUAGE_TITLE = "Language";
    public static final String LOCATOR_TITLE = "Locator";
    public static final String VOCAB_TITLE = "Vocabulary";
    public static final String SPELLING_TITLE = "Spelling";
    public static final String LANG_MECH_TITLE = "Language Mechanics";

    public static final String BATTERY_NAME = "Online Battery";
    public static final String SURVEY_NAME = "Online Survey";
    public static final String LOCATOR_NAME = "Online Locator";
    public static final String UNITTEST_NAME = "Unit Test";

    public static final TabeType APPLIED_MATH = new TabeType(APPLIED_MATH_TITLE,BATTERY_NAME);
    public static final TabeType MATH_COMP = new TabeType(MATH_COMP_TITLE,BATTERY_NAME);
    public static final TabeType READING = new TabeType(READING_TITLE,BATTERY_NAME);
    public static final TabeType LANGUAGE = new TabeType(LANGUAGE_TITLE,BATTERY_NAME);
    public static final TabeType VOCAB = new TabeType(VOCAB_TITLE,BATTERY_NAME);
    public static final TabeType SPELLING = new TabeType(SPELLING_TITLE,BATTERY_NAME);
    public static final TabeType LANG_MECH = new TabeType(LANG_MECH_TITLE,BATTERY_NAME);

//    public static final TabeType APPLIED_MATH_SURVEY = new TabeType(APPLIED_MATH_TITLE,SURVEY_NAME);
//    public static final TabeType MATH_COMP_SURVEY = new TabeType(MATH_COMP_TITLE,SURVEY_NAME);
//    public static final TabeType READING_SURVEY = new TabeType(READING_TITLE,SURVEY_NAME);
//    public static final TabeType LANGUAGE_SURVEY = new TabeType(LANGUAGE_TITLE,SURVEY_NAME);
//    public static final TabeType VOCAB_SURVEY = new TabeType(VOCAB_TITLE,SURVEY_NAME);
//    public static final TabeType SPELLING_SURVEY = new TabeType(SPELLING_TITLE,SURVEY_NAME);
//    public static final TabeType LANG_MECH_SURVEY = new TabeType(LANG_MECH_TITLE,SURVEY_NAME);


    public static final TabeType LOCATOR = new TabeType(LOCATOR_TITLE,LOCATOR_NAME);

    public static final Set levels = new HashSet();
    public static final Set types = new HashSet();
    public static final Set productNames = new HashSet();

    static {
        levels.add("A");
        levels.add("E");
        levels.add("D");
        levels.add("M");
        levels.add("L");
        types.add(LOCATOR);
        types.add(MATH_COMP);
        types.add(APPLIED_MATH);
        types.add(READING);
        types.add(LANGUAGE);
        types.add(VOCAB);
        types.add(SPELLING);
        types.add(LANG_MECH);
        productNames.add(BATTERY_NAME);
        productNames.add(LOCATOR_NAME);
        productNames.add(SURVEY_NAME);
        productNames.add(UNITTEST_NAME);
    }

    final private String title;
    final private String productName;

    private TabeType(String title, String productName) {
        this.title = title;
        this.productName = productName;
    }

    public static boolean isValidTitle(String title) {
        TabeType type = new TabeType(title,null);
        return types.contains(type);
    }

    public static boolean isValidLevel(String level) {
        return levels.contains(level);
    }

    public String getTitle() {
        return title;
    }

    public String getProductName() {
        return productName;
    }


    public int hashCode() {
        return title.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean equals(Object obj) {
        TabeType other = (TabeType)obj;
        return other.title.equals(this.title);
    }

    public static boolean isValidProductName(String attributeValue) {
        return productNames.contains(attributeValue);
    }

    public static String getProductNamesAsString() {
        StringBuffer buf = new StringBuffer();
        for (Iterator iterator = productNames.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            buf.append(s);
            if (iterator.hasNext())
                buf.append(",");
        }
        return buf.toString();
    }

}
