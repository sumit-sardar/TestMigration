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
            cleanString = cleanString.replaceAll( "<" , "&lt;" );
            cleanString = cleanString.replaceAll( ">" , "&gt;" );
            cleanString = cleanString.replaceAll( "\\\"" , "&quot;" );
            cleanString = cleanString.replaceAll( "'" , "&#39;" );
            cleanString = cleanString.replaceAll( "%" , "&#37;" );
            cleanString = cleanString.replaceAll( "&" , "&amp;" );
            cleanString = cleanString.replaceAll( "\\+" , "&#43;" );
            cleanString = cleanString.replaceAll( ";" , "&#59;" );
            cleanString = cleanString.replaceAll( "\\(" , "&#40;" );
            cleanString = cleanString.replaceAll( "\\)" , "&#41;" );
        }
        return cleanString;        
    }
    
} 
