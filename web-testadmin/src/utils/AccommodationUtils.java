package utils; 

import com.ctb.bean.testAdmin.SessionStudent;

public class AccommodationUtils 
{ 
    public static final String CALCULATOR = "Calculator";    
    public static final String COLOR_FONT = "Color/Font";    
    public static final String SCREEN_READER = "ScreenReader";    
    public static final String TEST_PAUSE = "TestPause";    
    public static final String UNTIMED_TEST = "UntimedTest";    
     
    public static String getDisplayString(SessionStudent ss) 
    {
        String str = "";
        if ("T".equals(ss.getCalculator())) {
            if ("true".equals(ss.getHasColorFontAccommodations()) ||
                "T".equals(ss.getScreenReader()) ||
                "T".equals(ss.getTestPause()) ||
                "T".equals(ss.getUntimedTest()))
                str = CALCULATOR + ", ";
            else
                str = CALCULATOR;
        }
        if ("true".equals(ss.getHasColorFontAccommodations())) {
            if ("T".equals(ss.getScreenReader()) ||
                "T".equals(ss.getTestPause()) ||
                "T".equals(ss.getUntimedTest()))
                str = COLOR_FONT + ", ";
            else
                str = COLOR_FONT;
        }
        if ("T".equals(ss.getScreenReader())) {
            if ("T".equals(ss.getTestPause()) ||
                "T".equals(ss.getUntimedTest()))
                str = SCREEN_READER + ", ";
            else
                str = SCREEN_READER;
        }
        if ("T".equals(ss.getTestPause())) {
            if ("T".equals(ss.getUntimedTest()))
                str = TEST_PAUSE + ", ";
            else
                str = TEST_PAUSE;
        }
        if ("T".equals(ss.getUntimedTest())) {
            str = UNTIMED_TEST;
        }
        return str + ".";
    }
} 
