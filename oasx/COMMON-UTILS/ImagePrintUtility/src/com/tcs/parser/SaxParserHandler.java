package com.tcs.parser;

import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParserHandler extends DefaultHandler
{
	public String htmlString = "";

	String attributeValue = null;
	String xPosition = null;
	boolean frame = false;
	boolean textWidget = false;
	boolean blodTag = false;
	boolean italicTag = false;
	boolean footnoteTag = false;
	boolean answerPanel = false;

	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException
			{
		if (qName.equalsIgnoreCase("item_canvas"))
		{
			this.htmlString += "<tr><td align=\"left\"><b>Question</b></td><td align=\"left\"><b>Answer</b></td></tr><tr>";
		} else if ((qName.equalsIgnoreCase("panel")) || (qName.equalsIgnoreCase("scrolling_text_panel"))) {
			this.htmlString += "<tr><td><div style=\"float:left;margin:0 3px;list-style:none;\">";
			this.xPosition = attributes.getValue("x");
			if ((this.xPosition != null) && (new Integer(this.xPosition).intValue() > 400)) {
				this.answerPanel = true;
				this.htmlString += "<h1>ANSWER </h1>";
			}
		} else if (qName.equalsIgnoreCase("text_widget")) {
			this.attributeValue = attributes.getValue("text_magnification");

			if (this.attributeValue == null) {
				this.htmlString += "<p>";
				this.textWidget = true;
			}
			else {
				this.textWidget = false;
			}
		} else if (qName.equalsIgnoreCase("b")) {
			this.htmlString += "<b>";
			this.blodTag = true;
		} else if (qName.equalsIgnoreCase("i")) {
			this.htmlString += "<i>";
			this.italicTag = false;
		} else if (qName.equalsIgnoreCase("footnote")) {
			this.footnoteTag = true;
		} else if (qName.equalsIgnoreCase("stimulus_tabs_panel")) {
			this.htmlString += "<div id=\"tabPanel\" style=\"float:left;\">";
		} else if (qName.equalsIgnoreCase("stimulus_tab")) {
			this.htmlString = 
					(this.htmlString + "<tr><td><div style=\"float:left;margin:0 3px;list-style:none; border:1px solid;height:24px;width:120px;text-align:center;\"><a href=\"#\" style=\"color:black;text-decoration:none;\"><b>" + 
							attributes.getValue("title") + 
							"</b>" + 
							"</a>" + 
							"</div>" + 
							"<div id=\"tab_content\" style=\"width:500px;border:1px solid;\">" + 
							"<br />");
		}
		else if (qName.equalsIgnoreCase("scrolling_text_widget")) {
			this.htmlString += "<div style=\"float:left;list-style:none;\">";
		} else if (qName.equalsIgnoreCase("multi_line_answer")) {
			this.htmlString += "<div style=\"float:left;list-style:none;\">";
		} else if (qName.equalsIgnoreCase("scrolling_text_panel")) {
			this.htmlString += "<tr><td><div style=\"width:500px;border:1px solid;\">";
		}
		else if (qName.equalsIgnoreCase("image_widget")) {
			this.htmlString += "<img src=";
		}
			}

	public void endElement(String uri, String localName, String qName)
			throws SAXException
			{
		if (qName.equalsIgnoreCase("item_canvas")) {
			this.htmlString += "</tr>";
		} else if ((qName.equalsIgnoreCase("panel")) || (qName.equalsIgnoreCase("scrolling_text_panel"))) {
			this.htmlString += "</div></td></tr>";
			this.attributeValue = null;
			this.xPosition = null;
			this.answerPanel = false;
		} else if (qName.equalsIgnoreCase("text_widget")) {
			if (this.textWidget) {
				this.htmlString += "</p>";
			}
			this.textWidget = false;
		} else if (qName.equalsIgnoreCase("b")) {
			this.htmlString += "</b>";
			this.blodTag = false;
		} else if (qName.equalsIgnoreCase("i")) {
			this.htmlString += "</i>";
			this.italicTag = false;
		} else if (qName.equalsIgnoreCase("footnote")) {
			this.footnoteTag = false;
		} else if (qName.equalsIgnoreCase("stimulus_tabs_panel")) {
			this.htmlString += "</div>";
		} else if (qName.equalsIgnoreCase("stimulus_tab")) {
			this.htmlString += "</div></td><td></td></tr>";
		} else if (qName.equalsIgnoreCase("scrolling_text_widget")) {
			this.htmlString += "</div>";
		} else if (qName.equalsIgnoreCase("multi_line_answer")) {
			this.htmlString += "</div>";
		} else if (qName.equalsIgnoreCase("scrolling_text_panel")) {
			this.htmlString += "</div></td><td></td></tr>";
		}
			}

	public void characters(char[] ch, int start, int length)
			throws SAXException
			{
		if ((!this.footnoteTag) && 
				(this.textWidget))
			this.htmlString += StringEscapeUtils.escapeXml(new String(ch, start, length));
			}
}

