package com.ctb.web.util; 

import java.util.zip.Adler32;

public class Utils 
{ 
    public static long getChecksum(byte[] fileContent)
    {
        long value = 0L;
        try {
            Adler32 adler = new Adler32();
            adler.update(fileContent);
            value = adler.getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return value;
    }
} 
