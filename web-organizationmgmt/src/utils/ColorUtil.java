package utils; 

import com.ctb.util.CTBConstants;
import java.util.HashMap;

public class ColorUtil { 
    
    /**
     * Getting color from color code
    */
    
    public static String getColor( String msCode ){
    
        String color="";
        HashMap colorMap = new HashMap();
        
        colorMap.put(CTBConstants.WHITE_CODE, initCap(CTBConstants.WHITE));
        colorMap.put( CTBConstants.BLACK_CODE, initCap(CTBConstants.BLACK ));
        colorMap.put(CTBConstants.LIGHT_BLUE_CODE, initCap(CTBConstants.LIGHT_BLUE ));
        colorMap.put(CTBConstants.LIGHT_PINK_CODE, initCap(CTBConstants.LIGHT_PINK ));
        colorMap.put(CTBConstants.LIGHT_YELLOW_CODE, initCap(CTBConstants.LIGHT_YELLOW));
        colorMap.put(CTBConstants.DARK_BLUE_CODE, initCap(CTBConstants.DARK_BLUE ));
        colorMap.put(CTBConstants.DARK_BROWN_CODE, initCap(CTBConstants.DARK_BROWN ));
        colorMap.put(CTBConstants.YELLOW_CODE, initCap(CTBConstants.YELLOW ));
        colorMap.put(CTBConstants.GREEN_CODE, initCap(CTBConstants.GREEN ));
        
        msCode = msCode.trim().toUpperCase();
    
        if(colorMap.containsKey(msCode)){
            color = (String)colorMap.get(msCode);
        }
    
        return color;
    }
    
    
    /*
    * Chane a string in initCap 
    */ 
    
    public static String initCap ( String value ) {

        String str[] = value.split(" ");
		if (str.length > 1) {

			String firstValue = str[0].toLowerCase();
	        char ch = firstValue.charAt(0);
	        String newStringChar = (new Character(ch)).toString().toUpperCase();
	        firstValue = firstValue.substring(1,firstValue.length());
	        firstValue = newStringChar+firstValue;


	        String secondValue = str[1].toLowerCase();
	        ch = secondValue.charAt(0);
	        newStringChar = (new Character(ch)).toString().toUpperCase();
	        secondValue = secondValue.substring(1,secondValue.length());
	        secondValue = newStringChar+secondValue;
	        return firstValue+" "+secondValue;

        } else {

			String firstValue = str[0].toLowerCase();
	        char ch = firstValue.charAt(0);
	        String newStringChar = (new Character(ch)).toString().toUpperCase();
	        firstValue = firstValue.substring(1,firstValue.length());
	        firstValue = newStringChar+firstValue;
	        return firstValue;

		}

    }
    
    
    public static String getGender( String asValue){
        String msGender="M";
        HashMap genderMap = new HashMap();
        
        genderMap.put(CTBConstants.MALE_CODE, CTBConstants.MALE);
        genderMap.put(CTBConstants.FEMALE_CODE, CTBConstants.FEMALE);
        genderMap.put(CTBConstants.UNKNOWN_CODE, CTBConstants.UNKNOWN);
        
        if(genderMap.containsKey(asValue)){
            msGender = ( String )genderMap.get ( asValue );
        }

        return msGender;
    }
        

    public static String getFontValue( String asValue){
        String msFont="1";
        HashMap fontMap = new HashMap();
        
        fontMap.put("1", CTBConstants.STANDARD_FONT);
        fontMap.put("1.5", CTBConstants.LARGER_FONT);
        
        if(fontMap.containsKey(asValue)){
            msFont = ( String )fontMap.get ( asValue );
        }

        return msFont;
    }


} 
