package com.ctb.util.web.sanitizer; 

public class JavaScriptSanitizer 
{ 

    /*
     * ?{actionForm.filter_tab}="><script>alert('hi')</script>
     *
     */ 
    public static String sanitizeString( String dirtyString ) {
        String cleanString = dirtyString;
        
        if(cleanString != null) {
            cleanString = cleanString.replaceAll( ";" , "&#59;" );
            cleanString = cleanString.replaceAll( "<" , "&lt;" );
            cleanString = cleanString.replaceAll( ">" , "&gt;" );
            cleanString = cleanString.replaceAll( "\\\"" , "&quot;" );
            cleanString = cleanString.replaceAll( "'" , "&#39;" );
            cleanString = cleanString.replaceAll( "%" , "&#37;" );
            cleanString = cleanString.replaceAll( "&" , "&amp;" );
            cleanString = cleanString.replaceAll( "\\+" , "&#43;" );
            cleanString = cleanString.replaceAll( "\\(" , "&#40;" );
            cleanString = cleanString.replaceAll( "\\)" , "&#41;" );
        }
        return cleanString;        
    }

    public static String sanitizeInput( String inputStr ) {
        inputStr = sanitizeString(inputStr);
    
        if (inputStr != null && (
        	inputStr.indexOf("demo.testfire.net") >= 0 || 
            inputStr.indexOf("WF_XSRF.html") >= 0 || 
            inputStr.indexOf("%27+%2B+%27") >= 0 || 
            inputStr.indexOf("'+'") >= 0 ||
            inputStr.indexOf("'%2B'") >= 0 ||
            inputStr.indexOf("%27%2B%27") >= 0 ||
            inputStr.indexOf("%27+%27") >= 0 ||
            inputStr.indexOf("+and+") >= 0 || 
            inputStr.indexOf("%2Band%2B") >= 0 || 
            inputStr.indexOf("+%7C%7C+") >= 0 || 
            inputStr.indexOf("%2B%7C%7C%2B") >= 0 || 
            inputStr.indexOf("%2B||%2B") >= 0 || 
            inputStr.indexOf("+||+") >= 0 ||
            inputStr.indexOf("javascript:") >= 0 ||
            inputStr.indexOf("javascript%3A") >= 0 ||
            inputStr.matches(".*script.*\\(.*") || 
            inputStr.matches(".*<.*script.*>.*"))) {
            // XSS or SQL Injection attack detected!
            inputStr = "";
        }
        return inputStr;        
    }
} 
