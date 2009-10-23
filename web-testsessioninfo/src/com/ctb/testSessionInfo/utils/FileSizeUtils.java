package com.ctb.testSessionInfo.utils; 

import javax.servlet.http.HttpServletRequest;

public class FileSizeUtils 
{ 
    private static final double BYTES_IN_A_MB = 1048576;
    
    // JSB - this method incorporates reducing size by half for encryption/compression
    public static Double convertBytesToDisplayMBs(Integer bytesInteger)
    {
        int bytes = 10;
        if(bytesInteger != null)
            bytes = bytesInteger.intValue();
        double tenTimesMbs = (bytes/BYTES_IN_A_MB) * 10;
        //tenTimesMbs = tenTimesMbs * 0.5;   // adjust for encryption/compression
        int roundedTenTimesMbs = Math.round(Math.round(tenTimesMbs));
        int wholeMbs = roundedTenTimesMbs/10;
        int tenthsMbs = roundedTenTimesMbs;
        if(wholeMbs != 0)
            tenthsMbs = tenthsMbs % (wholeMbs * 10);
        if(wholeMbs == 0 && tenthsMbs == 0 )
            tenthsMbs = 1;
        return new Double(String.valueOf(wholeMbs) + "." + String.valueOf(tenthsMbs));
     }
     
     public static String getDisplayValue(double mbs){
        double tenTimesMbs = mbs * 10.0;
        int roundedTenTimesMbs = Math.round(Math.round(tenTimesMbs));
        int wholeMbs = roundedTenTimesMbs/10;
        int tenthsMbs = roundedTenTimesMbs;
        if(wholeMbs != 0)
            tenthsMbs = tenthsMbs % (wholeMbs * 10);
        if(wholeMbs == 0 && tenthsMbs == 0 )
            tenthsMbs = 1;
            
        return String.valueOf(wholeMbs) + "." + String.valueOf(tenthsMbs);
     }
} 
