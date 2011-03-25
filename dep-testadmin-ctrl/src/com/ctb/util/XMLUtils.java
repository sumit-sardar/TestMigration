package com.ctb.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * This class provides basic xml utility functions.
 * 
 * @author TCS
 * 
 */
public class XMLUtils {

	/**
	 * Method takes input XML, build using jdom sax parser and return jdom document
	 * 
	 * @param input -
	 *            XML String
	 * @param encoding -
	 *            encoding format
	 * @return Document - Document of XML String
	 * @throws Exception
	 */
	public static Document parse(String input, String encoding)
			throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		InputStream is = new ByteArrayInputStream(input.getBytes(encoding));
		doc = builder.build(is);
		return doc;

	}

	/**
	 * Method takes input XML, build using  jdom sax parser and return jdom document
	 * 
	 * @param input -
	 *            XML String
	 * @param encoding -
	 *            encoding format
	 * @return Document - Document of XML String
	 * @throws Exception
	 */
	public static Document parse(String input) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		InputStream is = new ByteArrayInputStream(input.getBytes());
		doc = builder.build(is);
		return doc;

	}

	/**
	 * Method process hex formatted input XML string to actual string.
	 * 
	 * @param inputString -
	 *            Hex formatted input XML string
	 * @return - return actual input XML string
	 */
	public static String processHexString(String inputString) {
		StringBuilder outPutString = new StringBuilder("");
		String temp = "";
		int index = inputString.indexOf("%");
		while (index > -1) {
			temp = inputString.substring(index + 1, index + 3);
			outPutString = outPutString.append(inputString.substring(0, index))
					.append(convertHexToChar(temp));
			inputString = inputString.substring(index + 3);
			index = inputString.indexOf("%");
		}
		return outPutString.toString();
	}

	/**
	 * Method extract all jdom element from source element based on the pattern
	 * and returns list of extracted element.
	 * 
	 * @param pattern -
	 *            pattern to search
	 * @param element -
	 *            source jdom element
	 * @return - lest of searched element
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> extractAllElement(String pattern,
			Element element) throws Exception {
		ArrayList<Element> results = new ArrayList<Element>();
		List<Element> children = element.getChildren();
		for (Element elem : children) {
			if (pattern.equals(elem.getName())) {
				results.add(elem);
			}
			results.addAll(extractAllElement(pattern, elem));
		}

		return results;

	}

	/**
	 * This method convert hex value of character to character.
	 * 
	 * @param hexchar -
	 *            hex value of a character
	 * 
	 * @return String - character
	 */
	private static String convertHexToChar(String hexchar) {
		try {
			int val = Integer.parseInt(hexchar, 16);
			Character charVal = (char) val;
			return charVal.toString();
		} catch (Exception e) {
			return hexchar;
		}

	}

}
