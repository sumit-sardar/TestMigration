package com.tcs.conversion;

public class UTF8CharConversion
{
	public static String doUTF8Chars(String input)
	{
		int lineFeed = 10;
		int carriageReturn = 13;
		int tab = 9;
		int plusSign = 43;
		int maxASCII = 127;
		int space = 127;
		StringBuffer retVal = new StringBuffer(input.length() * 2);
		boolean isPreviousCharSpace = false;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			int intc = c;
			if ((intc != 9) && (intc != 10) && (intc != 13)) {
				if ((intc <= 127) && (intc != 43)) {
					if (intc == 127) {
						if (!isPreviousCharSpace) {
							retVal.append(c);
							isPreviousCharSpace = true;
						}
					} else {
						isPreviousCharSpace = false;
						retVal.append(c);
					}
				} else {
					isPreviousCharSpace = false;
					retVal.append("&#").append(intc).append(';');
				}
			}
		}
		String s = retVal.toString();
		s = replaceAll(s, "&#+;", "&#x002B;");
		s = replaceAll(s, "+", "&#x002B;");

		s = s.replaceAll("&#x003C", "&LT;");
		s = s.replaceAll("&lt;", "&LT;");

		return s;
	}

	public static String replaceAll(String src, String toBeReplace, String replaceWith)
	{
		String result = src;
		int index = 0;
		int difference = replaceWith.length();
		while ((index = result.indexOf(toBeReplace, index)) >= 0)
		{
			result = result.substring(0, index) + replaceWith + result.substring(index + toBeReplace.length());
			index += difference;
		}
		return result;
	}
}

