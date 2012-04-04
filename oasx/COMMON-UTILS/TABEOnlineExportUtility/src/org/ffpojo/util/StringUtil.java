package org.ffpojo.util;

public class StringUtil {

	public static enum Direction {
		LEFT,
		RIGHT
	}
	
	public static String fillToLength(String s, int length, char fillWith, Direction inDirection, Direction justifyDir) {
		if (length < 0) {
			return s;
		} else {
			int actualLength = s.length();
			StringBuffer sbuf = new StringBuffer(s);
			if (actualLength < length) {
				StringBuffer sbufDifference = new StringBuffer();
				int difference = length - actualLength;
				for (int i = 0; i < difference; i++) {
					sbufDifference.append(String.valueOf(fillWith));
				}
				if (inDirection == Direction.LEFT) {
					sbuf = new StringBuffer(sbufDifference);
					sbuf.append(s);
				} else if (inDirection == Direction.RIGHT) {
					sbuf.append(sbufDifference);
				}
				if(justifyDir == Direction.LEFT){
					sbuf = left(sbuf.toString(), length);
				}else{
					sbuf = right(sbuf.toString(), length);
				}
				
				
			} else {
				sbuf.setLength(length);
			}
			
	
			return sbuf.toString();
		}
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().equals("");
	}
	 public static StringBuffer right( String text, int length ) 
	    {
	
		int blankCount = length - text.length();
		StringBuffer buf = new StringBuffer(length);
		if ( text.length() >= length ) {
			
			return buf.append(text);
		}
		for (int i = 0; i < blankCount; i++) {
		    buf.append(' ');
		}
		buf.append(text);
		return buf;
	    }

	  
	    public static StringBuffer left( String text, int length ) 
	    {
		if ( text.length() >= length ) {
			
			
		}
		int blankCount = length - text.length();
		StringBuffer buf = new StringBuffer(length);
		if ( text.length() >= length ) {
			
			return buf.append(text);
		}
		buf.append(text);
		for (int i = 0; i < blankCount; i++) {
		    buf.append(' ');
		}
		return buf;
	    }

}
