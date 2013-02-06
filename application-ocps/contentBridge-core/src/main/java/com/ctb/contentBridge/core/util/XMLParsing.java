// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 2/22/2010 1:11:20 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   XMLParsing.java

package com.ctb.contentBridge.core.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.apache.log4j.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import com.ctb.contentBridge.core.exception.SystemException;

// Referenced classes of package com.stgglobal.ads.common:
//            AdsCommon, ExceptionHandler

public class XMLParsing extends DefaultHandler {

	public String set_Attribute_values(String strXmlFile)
			throws SystemException {
		arrList = new ArrayList();
		InputSource is1 = new InputSource(new StringReader(strXmlFile));
		DefaultHandler handler = new XMLParsing();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(is1, handler);
		} catch (SAXException t) {
			throw new SystemException(t);
		} catch (ParserConfigurationException t) {
			throw new SystemException(t);
		} catch (IOException t) {
			throw new SystemException(t);
		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		return "Valid";
	}

	public ArrayList get_Attribute_Values() {
		return arrList;
	}

	public void startElement(String namespaceURI, String lName, String qName,
			Attributes attrs) {
		if ("".equals(lName))
			lName = qName;
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				String aName = attrs.getQName(i);
				if (lName.equals("selection_number") || lName.equals("order"))
					FLAG1++;
				if (lName.equals("image_widget") || lName.equals("assessment")
						|| lName.equals("itemref")
						|| lName.equals("item_delivery_package")) {
					if (aName.toLowerCase().equals("src")
							|| aName.toLowerCase().equals("ident")
							|| aName.toLowerCase().equals("linkrefid")
							|| aName.toLowerCase().equals("iid")
							|| aName.toLowerCase().equals(
									"security_classification_id"))
						arrList.add(attrs.getValue(i));
					else if (aName.toLowerCase().equals(
							"starting_question_number"))
						FLAG = Integer.parseInt(attrs.getValue(i));
				} else if (lName.equals("asset")) {
					if (aName.toLowerCase().equals("ident")
							|| aName.toLowerCase().equals("imagtype")
							|| aName.toLowerCase().equals("pkgtype"))
						arrList.add(attrs.getValue(i));
				} else if (aName.toLowerCase().equals("uri"))
					arrList.add(attrs.getValue(i));
			}

		}
	}

	public static ArrayList arrList = null;
	public static int FLAG = -1;
	public static int FLAG1 = 0;
	private static Logger logger = Logger.getLogger("XMLParsing");
	private static FileAppender fileAppender = null;
}