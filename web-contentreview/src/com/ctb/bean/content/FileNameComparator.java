package com.ctb.bean.content; 

import java.util.Comparator;

public class FileNameComparator implements Comparator
{ 
    public int compare( Object o1, Object o2 )
    {
        String tab1 = (String)o1;
        tab1 = encodeNumber( tab1 );
        String tab2 = (String)o2;
        tab2 = encodeNumber( tab2 );
        return tab1.compareTo( tab2 );   
    }
    
    public String encodeNumber( String src )
    {
        String result = src;
        int startIndex, endIndex;
        while( ( startIndex = findFirstDigit( result ) ) != -1 )
        {
            endIndex = startIndex + 1;
            for( int i = startIndex + 1; i < result.length(); i++ )
            {
                if ( !Character.isDigit( result.charAt( i )) )
                {
                    endIndex = i;
                    break;
                }
            }
            int min = Character.MIN_RADIX;
            int max = Character.MAX_RADIX;
            String numberString = result.substring( startIndex, endIndex );
            int number = Integer.valueOf( numberString ).intValue() + 10;
            if ( number < Character.MIN_RADIX )
                number = Character.MIN_RADIX;
            else if ( number > Character.MAX_RADIX )
                number = Character.MAX_RADIX;
            char[] numberChar = new char[1];
            numberChar[0] = Character.forDigit( number, Character.MAX_RADIX );
            result = result.substring( 0, startIndex ) + ( new String( numberChar ) )
                        + result.substring( endIndex );
        }
        return result.toUpperCase();
    }
    
    public int findFirstDigit( String src )
    {
        int loc = -1;
        for( int i = 0; i < src.length(); i++ )
        {
            if ( Character.isDigit( src.charAt( i )) )
            {
                loc = i;
                break;
            }
        }
        return loc;
    }
} 
