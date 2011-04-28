package utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import sun.net.www.http.HttpClient;



public class ItemPlayerUtils {
	public static HttpClient client;
	
	public static String getMIMEType(String ext) {
		String mimeType = "image/gif";
		if ("swf".equals(ext))
			mimeType = "application/x-shockwave-flash";
		if ("gif".equals(ext))
			mimeType = "image/gif";
		if ("jpg".equals(ext))
			mimeType = "image/jpg";
		return mimeType;
	}
	public static String doUTF8Chars( String input )
	{
		final int lineFeed = 10;
		final int carriageReturn = 13;
		final int tab = 9;
		final int plusSign = 43;
		final int maxASCII = 127;
		final int space = 127;
		StringBuffer retVal = new StringBuffer( input.length() * 2 );
		boolean isPreviousCharSpace = false;
		String s;
		for(int i = 0; i < input.length(); i++)
		{
			char c = input.charAt( i );
			int intc = c;
			if( intc != tab && intc != lineFeed && intc != carriageReturn )
			{
				if( intc <= maxASCII && intc != plusSign )
				{
					if( intc == space )
					{
						if( !isPreviousCharSpace )
						{
							retVal.append( c );
							isPreviousCharSpace = true;
						}
					}
					else
					{
						isPreviousCharSpace = false;
						retVal.append( c );
					}
				}
				else
				{
					isPreviousCharSpace = false;
					retVal.append( "&#" ).append( intc ).append( ';' );
				}
			}
		}
		s = retVal.toString();
		s = replaceAll( s, "&#+;", "&#x002B;" );
		s = replaceAll( s, "+", "&#x002B;" );
		//Defect# 64272: added for "<" Defect. 
		s = s.replaceAll("&#x003C", "&LT;");
		s = s.replaceAll("&lt;", "&LT;");
		//System.out.println("****ItemXML****" + s);
		return s;
	}
	
	public static String replaceAll( String src, String toBeReplace, String replaceWith )
	{
		String result = src;
		int index = 0;
		int difference = replaceWith.length();
		while ( ( index = result.indexOf( toBeReplace, index )) >= 0 )
		{
			result = result.substring( 0, index ) + replaceWith + result.substring( index + toBeReplace.length() );
			index += difference;
		}
		return result;
	}
	
	public static synchronized void writeResponse(HttpServletResponse response, String xml) {
		writeResponse(response, xml, null);
	}
	
	/**
	 * write xml content to response
	 *
	 */
	public static void writeResponse(HttpServletResponse response, String xml, String mseq) {
		try {
			//if((mseq == null || lastMseq == null) || !mseq.equals(lastMseq)) {
				response.setContentType("text/xml");
				response.setStatus(response.SC_OK);
				PrintWriter out = response.getWriter();
				out.println(xml);
				out.flush();
				out.close();
				response.flushBuffer();
			//	lastMseq = mseq;
			//}
		} catch (Exception e) {
			// do nothing, response already written
		}
	}
	
	public static String parseTag(String tagName, String xml) {
		String tagValue = "-";
		if (xml != null) {
			int index = xml.indexOf(tagName);
			if (index > 0) {
				int startIndex = index + tagName.length() + 1;
				int endIndex = startIndex;
				while (true) {
					int ch = xml.charAt(endIndex);
					if ((ch == 34) || (ch == 39) || (endIndex > xml.length()-1))
						break;
					endIndex++;
				}
				tagValue = xml.substring(startIndex, endIndex);
			}
		}
		return tagValue;
	}
	
}