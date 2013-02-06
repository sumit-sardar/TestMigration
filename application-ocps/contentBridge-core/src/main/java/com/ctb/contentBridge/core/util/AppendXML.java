// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 2/22/2010 1:04:28 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AppendXML.java

package com.ctb.contentBridge.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.jdom.Attribute;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.util.Base64.Base64EncodeDecode;

public class AppendXML {

	public AppendXML(String inputXML) throws SystemException {
		dbf = null;
		db = null;
		w3cDOMdoc = null;
		ASSET_FLAG = true;
		JDOMdoc = null;
		patternLayout = new PatternLayout();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			db = dbf.newDocumentBuilder();
			w3cDOMdoc = db.parse(new InputSource(new StringReader(inputXML)));
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	public String parseWithDOMHtmlWidget() throws SystemException {
		String xml = null;
		NodeList htmlWidget = null;
		DOMBuilder jdomBuilder = null;
		try {
			jdomBuilder = new DOMBuilder();
			JDOMdoc = jdomBuilder.build(w3cDOMdoc);
			XMLOutputter xop = new XMLOutputter();
			xml = xop.outputString(JDOMdoc);
			htmlWidget = w3cDOMdoc.getElementsByTagName("html_widget");
			if (htmlWidget.getLength() > 0)
				return "true";
			else
				return "false";
		} catch (Exception exception) {
			throw new SystemException(exception);
		}

	}

	public String parseWithDOM(ResultSet resultSet, ArrayList arrlist,
			String obItemId) throws SystemException {
		String xml = null;
		DOMBuilder jdomBuilder = null;

		NodeList htmlWidget = null;
		try {
			jdomBuilder = new DOMBuilder();
			JDOMdoc = jdomBuilder.build(w3cDOMdoc);
			if (ASSET_FLAG)
				JDOMdoc = modifyAttributeValue(JDOMdoc, obItemId);
			XMLOutputter xop = new XMLOutputter();
			xml = xop.outputString(JDOMdoc);
			htmlWidget = w3cDOMdoc.getElementsByTagName("html_widget");

		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		if (ASSET_FLAG) {
			if (htmlWidget.getLength() == 0)
				xml = xml.replaceAll("src", "image_ref");
			// else
			// xml = xml.replaceAll("src", "id");

			ASSET_FLAG = false;

		}
		try {
			w3cDOMdoc = db.parse(new InputSource(new StringReader(xml)));
			if (arrlist != null && htmlWidget.getLength() == 0)
				w3cDOMdoc = insertNode_with_CDATASection(w3cDOMdoc, arrlist,
						resultSet);
			else if (arrlist != null && htmlWidget.getLength() > 0)

				w3cDOMdoc = insertNode_with_CDATASection_for_pkg(w3cDOMdoc,
						arrlist, resultSet);
			JDOMdoc = jdomBuilder.build(w3cDOMdoc);
			XMLOutputter xop = new XMLOutputter();
			xml = xop.outputString(JDOMdoc);
		} catch (Exception e) {
			throw new SystemException(e);
		}

		System.out.println(xml);
		return xml;

	}

	public Document insertNode_with_CDATASection_for_pkg(Document w3cDocument,
			ArrayList arrList1, ResultSet resultSet) throws SystemException {
		try {
			Base64EncodeDecode base64Encode = new Base64EncodeDecode();
			Element assetsNode = w3cDOMdoc.createElement("assets");
			while(resultSet.next()) {
				if(arrList1.contains(resultSet.getString(2))) {
					InputStream inStream = resultSet.getBlob("asset_blob")
							.getBinaryStream();
					int iLength = 0;
					for (iLength = 0; inStream.read() != -1; iLength++)
						;
					String encodedData = new String();
					inStream = resultSet.getBlob(1).getBinaryStream();
					encodedData = base64Encode
							.base64Encoding(inStream, iLength);
					System.out
							.println("HI insertNode_with_CDATASection_for_pkg"
									+ encodedData);

					Element htmlNode = w3cDOMdoc.createElement("htmldata");
					assetsNode.appendChild(htmlNode);
					org.w3c.dom.CDATASection cDATASection = w3cDOMdoc
							.createCDATASection(encodedData);
					htmlNode.appendChild(cDATASection);
					htmlNode.setAttribute("id", resultSet.getString(2));
					htmlNode.setAttribute("type", resultSet.getString(3));
					htmlNode.setAttribute("embedded", "base64");
				}
			}
			/*for (int i = 0; i < arrList1.size(); i++) {
				// resultSet.beforeFirst();
				while (resultSet.next())
					if (resultSet.getString(2).equals(arrList1.get(0))) {

						InputStream inStream = resultSet.getBlob("asset_blob")
								.getBinaryStream();
						int iLength = 0;
						for (iLength = 0; inStream.read() != -1; iLength++)
							;
						String encodedData = new String();
						inStream = resultSet.getBlob(1).getBinaryStream();
						encodedData = base64Encode.base64Encoding(inStream,
								iLength);
						System.out
								.println("HI insertNode_with_CDATASection_for_pkg"
										+ encodedData);

						Element htmlNode = w3cDOMdoc.createElement("htmldata");
						assetsNode.appendChild(htmlNode);
						org.w3c.dom.CDATASection cDATASection = w3cDOMdoc
								.createCDATASection(encodedData);
						htmlNode.appendChild(cDATASection);
						htmlNode.setAttribute("id", resultSet.getString(2));
						htmlNode.setAttribute("type", resultSet.getString(3));
						htmlNode.setAttribute("embedded", "base64");
					}
			}*/

			Element root = w3cDOMdoc.getDocumentElement();
			root.appendChild(assetsNode);
		} catch (SQLException sqlException) {
			throw new SystemException(sqlException);
		}
		/*
		 * catch(IOException ioException) { ExceptionHandler.exception =
		 * ioException; ExceptionHandler.ERROR = "error"; }
		 */
		catch (Exception exception) {
			throw new SystemException(exception);
		} finally {
			try {
				resultSet.close();
			} catch (SQLException sqlException) {
				throw new SystemException(sqlException);
			}
		}
		return w3cDocument;
	}

	public Document insertNode_with_CDATASection(Document w3cDocument,
			ArrayList arrList1, ResultSet resultSet) throws SystemException {
		try {
			Base64EncodeDecode base64Encode = new Base64EncodeDecode();
			Element assetsNode = w3cDOMdoc.createElement("assets");
			while(resultSet.next()) {
				if(arrList1.contains(resultSet.getString(2))) {
					InputStream inStream = resultSet.getBlob(1)
							.getBinaryStream();
					int iLength = 0;
					for (iLength = 0; inStream.read() != -1; iLength++)
						;
					String encodedData = new String();
					inStream = resultSet.getBlob(1).getBinaryStream();
					encodedData = base64Encode
							.base64Encoding(inStream, iLength);
					Element imageNode = w3cDOMdoc.createElement("image");
					assetsNode.appendChild(imageNode);
					org.w3c.dom.CDATASection cDATASection = w3cDOMdoc
							.createCDATASection(encodedData);
					imageNode.appendChild(cDATASection);
					imageNode.setAttribute("id", resultSet.getString(2));
					imageNode.setAttribute("type", resultSet.getString(3));
					imageNode.setAttribute("embedded", "base64");
				}
			}
			/*for (int i = 2; i < arrList1.size(); i++) {
				resultSet.beforeFirst();
				while (resultSet.next())
					if (resultSet.getString(2).equals((String) arrList1.get(i))) {

						InputStream inStream = resultSet.getBlob(1)
								.getBinaryStream();
						int iLength = 0;
						for (iLength = 0; inStream.read() != -1; iLength++)
							;
						String encodedData = new String();
						inStream = resultSet.getBlob(1).getBinaryStream();
						encodedData = base64Encode.base64Encoding(inStream,
								iLength);
						Element imageNode = w3cDOMdoc.createElement("image");
						assetsNode.appendChild(imageNode);
						org.w3c.dom.CDATASection cDATASection = w3cDOMdoc
								.createCDATASection(encodedData);
						imageNode.appendChild(cDATASection);
						imageNode.setAttribute("id", resultSet.getString(2));
						imageNode.setAttribute("type", resultSet.getString(3));
						imageNode.setAttribute("embedded", "base64");
					}
			}*/

			Element root = w3cDOMdoc.getDocumentElement();
			root.appendChild(assetsNode);
		} catch (SQLException sqlException) {
			throw new SystemException(sqlException);
		} catch (IOException ioException) {
			throw new SystemException(ioException);
		} catch (Exception exception) {
			throw new SystemException(exception);
		} finally {
			try {
				resultSet.close();
			} catch (SQLException sqlException) {
				throw new SystemException(sqlException);
			}
		}
		return w3cDocument;
	}

	public org.jdom.Document modifyAttributeValue(org.jdom.Document jdomDoc,
			String obItemId) {
		org.jdom.Element rElement = jdomDoc.getRootElement();
		org.jdom.Element c1_1Element = rElement.getChild("item_model");
		Attribute raAttribute = c1_1Element.getAttribute("eid");
		raAttribute = raAttribute.setValue(obItemId);
		Attribute rAttribute = rElement.getAttribute("eid");
		rAttribute = rAttribute.setValue(obItemId);
		return jdomDoc;
	}

	DocumentBuilderFactory dbf;
	DocumentBuilder db;
	Document w3cDOMdoc;
	boolean ASSET_FLAG;
	org.jdom.Document JDOMdoc;
	private static Logger logger = Logger.getLogger("AppendXML");
	private static FileAppender fileAppender = null;
	PatternLayout patternLayout;
}