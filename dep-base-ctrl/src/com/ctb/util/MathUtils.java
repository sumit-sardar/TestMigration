package com.ctb.util; 

public class MathUtils 
{ 
    static final long serialVersionUID = 1L;
    public static Integer intDiv(Integer num, Integer denom) {
        double quotient = num.doubleValue() / denom.doubleValue();
        float ceiled = new Float(Math.ceil(quotient)).floatValue();
        int rounded = Math.round(ceiled);
        return new Integer(rounded);
    }
} 
