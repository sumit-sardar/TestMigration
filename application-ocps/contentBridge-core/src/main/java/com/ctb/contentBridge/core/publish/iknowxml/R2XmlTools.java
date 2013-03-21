package com.ctb.contentBridge.core.publish.iknowxml;


import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.xpath.*;
import org.xml.sax.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.sax.element.FieldOperation;



/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 8, 2003
 * Time: 5:07:21 PM
 * To change this template use Options | File Templates.
 */
public class R2XmlTools {
    static public void replaceAttributes(Document doc, String expression, FieldOperation fo) throws JDOMException, SAXException {
        XPath xpath = XPath.newInstance(expression);
        List fileNodes = xpath.selectNodes(doc);

        for (Iterator iter = fileNodes.iterator(); iter.hasNext();) {
            Object node = iter.next();

            if (!(node instanceof Attribute)) {
                continue;
            }
            Attribute att = (Attribute) node;

            att.setValue(fo.process(att.getValue()));
        }
    }

    static public void replaceAttributes(Element element, String expression, FieldOperation fo) throws JDOMException, SAXException {
        XPath xpath = XPath.newInstance(expression);
        List fileNodes = xpath.selectNodes(element);

        for (Iterator iter = fileNodes.iterator(); iter.hasNext();) {
            Object node = iter.next();

            if (!(node instanceof Attribute)) {
                continue;
            }
            Attribute att = (Attribute) node;

            att.setValue(fo.process(att.getValue()));
        }
    }

    static public void writeXmlToStream(Document doc, OutputStream os) throws SystemException {
        writeXmlToStream(doc, os, new R2XmlOutputter());
    }

    static public void writeXmlToStream(Document doc, OutputStream os, XMLOutputter outputter) throws SystemException {

        try {
            Writer writer = new OutputStreamWriter(os, "UTF-8");

            writeXmlToWriter(doc, writer, outputter);
            writer.close();
        } catch (IOException e) {
            throw new SystemException("Could not write document from DocumentHolder to OutputStream: "
                    + e.getMessage());
        }
    }

    static public void writeXmlToStream(Element element, OutputStream os) throws SystemException {
        writeXmlToStream(element, os, new R2XmlOutputter());
    }

    static public void writeXmlToStream(Element element, OutputStream os, XMLOutputter outputter) throws SystemException {
        try {
            Writer writer = new OutputStreamWriter(os, "UTF-8");

            writeXmlToWriter(element, writer, outputter);
            writer.close();
        } catch (IOException e) {
            throw new SystemException("Could not write document from DocumentHolder to OutputStream: "
                    + e.getMessage());
        }
    }

    static public void writeXmlToWriter(Document doc, Writer writer) throws SystemException {
        writeXmlToWriter(doc, writer, new R2XmlOutputter());
    }

    static public void writeXmlToWriter(Document doc, Writer writer, XMLOutputter outputter) throws SystemException {

        try {
            outputter.output(doc, writer);
        } catch (IOException e) {
            throw new SystemException("Could not write document from DocumentHolder to Writer: "
                    + e.getMessage());
        }
    }

    static public void writeXmlToWriter(Element element, Writer writer) throws SystemException {
        writeXmlToWriter(element, writer, new R2XmlOutputter());
    }

    static public void writeXmlToWriter(Element element, Writer writer, XMLOutputter outputter) throws SystemException {
        try {
            outputter.output(element, writer);
        } catch (IOException e) {
            throw new SystemException("Could not write document from DocumentHolder to Writer: "
                    + e.getMessage());
        }
    }

    static public InputStream getInputStream(Element element) throws SystemException {
        return new ByteArrayInputStream(R2XmlTools.xmlToByteArray(element));
    }

    static public InputStream getInputStream(Document document) throws SystemException {
        return new ByteArrayInputStream(R2XmlTools.xmlToByteArray(document));
    }

    static public byte[] xmlToByteArray(Document doc) throws SystemException {
        return xmlToByteArray(doc, new R2XmlOutputter());
    }

    static public byte[] xmlToByteArray(Document doc, XMLOutputter outputter) throws SystemException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        writeXmlToStream(doc, os, outputter);
        return os.toByteArray();
    }

    static public byte[] xmlToByteArray(Element element, XMLOutputter outputter) throws SystemException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        writeXmlToStream(element, os, outputter);
        return os.toByteArray();
    }

    static public byte[] xmlToByteArray(Element element) throws SystemException {
        return xmlToByteArray(element, new R2XmlOutputter());
    }

    static public char[] xmlToCharArray(Element element) throws SystemException {
        return xmlToCharArray(element, new R2XmlOutputter());
    }

    static public char[] xmlToCharArray(Element element, XMLOutputter outputter) throws SystemException {
        CharArrayWriter writer = new CharArrayWriter();

        writeXmlToWriter(element, writer, outputter);
        return writer.toCharArray();
    }

    static public Element deepCopyElement(Element element) throws SystemException {
        return deepCopyElement(element, new R2XmlOutputter());
    }

    static public Element deepCopyElement(Element element, XMLOutputter outputter) throws SystemException {
        byte[] bytes = xmlToByteArray(element, outputter);
        R2DocumentBuilder builder = new R2DocumentBuilder("org.apache.xerces.parsers.SAXParser",
                false);
        Document document = null;

        try {
            document = builder.build(new InputStreamReader(new ByteArrayInputStream(bytes),
                    "UTF-8"));
        } catch (JDOMException e) {
            throw new SystemException("JDOM document build error: "
                    + e.getMessage());
        } catch (IOException e) {
            throw new SystemException("Unable to read serialized Element: "
                    + e.getMessage());
        }
        return document.getRootElement();
    }

    /**
     * Runs a generic Sax parse to simulate OAS parsing of the XML CLOB
     * @param chars
     * @throws SystemException 
     */
    public static void validateXml(char[] chars) throws SystemException {
        try {
            SAXBuilder builder = new SAXBuilder(false);

            builder.build(new CharArrayReader(chars));
        } catch (JDOMException e) {
            throw new SystemException(e.getMessage());
        } catch (IOException e) {
            throw new SystemException("Could not read byte array to validate OAS parsing: "
                    + e.getMessage());
        }
    }
}
