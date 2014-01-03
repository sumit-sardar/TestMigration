package comm.ctb.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * This class provides XML utility functions.
 * 
 * @author TCS
 * 
 */
public class XMLUtils {

	/**
	 * Method takes input XML, build using  jdom sax parser and return jdom document
	 * 
	 * @param input - XML String
	 * @return Document - Document of XML String
	 * @throws Exception
	 */
	public static Document parseXML(String inputXML) throws Exception {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();		
		InputStream is = new ByteArrayInputStream(inputXML.getBytes());
		doc = builder.build(is);
		return doc;

	}
	
	
	/**
	 * Method takes input XML File, build using  jdom sax parser and return jdom document
	 * 
	 * @param input - XML File
	 * @return Document - Document for XML File
	 * @throws Exception
	 */
	public static Document parseXML(File inFile) throws Exception {
		Document doc = null;
		//File inFile = new File(fileName);
		SAXBuilder builder = new SAXBuilder();		
		doc = (Document) builder.build(inFile);
		return doc;

	}
	
	/**
	 * Method extract all jdom element from source element based on the pattern
	 * and returns list of extracted element.
	 * 
	 * @param pattern - pattern to search
	 * @param element - source jdom element
	 * @return - lest of searched element
	 * @throws Exception
	 */	

	public static List extractAllElement(String pattern, Element element)
	throws Exception {
		// TODO: this will only work with simple './/name' queries as is . . .
		ArrayList results = new ArrayList();
		pattern = pattern.substring(pattern.indexOf(".//") + 3);
		List children = element.getChildren();
		//@SuppressWarnings("unchecked")
		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			Element elem = (Element)iterator.next();
			if (pattern.equals(elem.getName())) {
				results.add(elem);
			}
			results.addAll(extractAllElement(".//" + pattern, elem));
		}
		return results;		
	}
	
	/**
	 * Method extract a single jdom element from source element based on the pattern
	 * and returns the extracted element.
	 * 
	 * @param pattern - pattern to search
	 * @param element - source jdom element
	 * @return - extracted element
	 * @throws Exception
	 */	

	public static Element extractSingleElement(String pattern, Element element)
	throws Exception {
		// TODO: this will only work with simple './/name' queries as is . . .
		pattern = pattern.substring(pattern.indexOf(".//") + 3);		
		List children = element.getChildren();
		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			Element elem = (Element)iterator.next();
			if (pattern.equals(elem.getName())) {
				return elem;
			} else {
				Element child = extractSingleElement(".//" + pattern, elem);
				if (child != null)
					return child;
			}
		}
		return null;
	}
}
