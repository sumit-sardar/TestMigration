package com.ctb.contentBridge.core.publish.iknowxml;


import java.io.*;
import java.net.*;

import org.apache.commons.lang.*;
import org.jdom.*;
import org.jdom.input.*;
import org.xml.sax.*;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.sax.element.FieldOperation;
import com.ctb.contentBridge.core.publish.sax.replacement.FrameworkReplacementConfig;
import com.ctb.contentBridge.core.publish.sax.replacement.ReplacementOperation;
import com.ctb.contentBridge.core.publish.sax.replacement.ReplacementSetFactory;
import com.ctb.contentBridge.core.publish.tools.CtbEntityResolver;



/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 8, 2003
 * Time: 3:46:05 PM
 *
 * R2DocumentBuilder provides pre and post parse munging to support product inadequacies in FOP and JGenerate
 * Use this class rather than the JDOM Sax builder class
 */
public class R2DocumentBuilder extends SAXBuilder {

    public static final String SAX_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";
    /**
     * Standard constructor for specifying SAX parser and DTD validation
     * @param parser
     * @param validate
     */
    public R2DocumentBuilder(String parser, boolean validate) {
        super(parser, validate);
        this.setEntityResolver(new CtbEntityResolver());
        this.setIgnoringElementContentWhitespace(true);
    }

    public R2DocumentBuilder() {
        this(SAX_PARSER_NAME, false);
    }

    /**
     * Only allowable method to be used with this parsing approach. All other build methods have been marked
     * Deprecated.
     * @param inputStream
     * @return Document (JDOM) representing the XML file
     * @throws JDOMException
     * @throws IOException
     */
    public Document build(InputStream inputStream) throws JDOMException, IOException {

    	System.setProperty("file.encoding", "ISO-8859-1");
        // Ensure encoding is read as UTF-8: 
        //encoding UTF-8 is removed to support spanish character
        InputStreamReader isr = new InputStreamReader(inputStream);

        // do pre-parse munging
        InputStream is = null;

        try {
            is = preParse(isr);
        } catch (IOException e) {
        	throw new SystemException("Could not perform pre-parsing on R2 xml document: "
                    + e.getMessage());
        }

        // build document
        Document doc = super.build(new InputStreamReader(is));

        is.close();

        // do post-parse munging

        try {
            postParse(doc);
        } catch (SAXException e) {
        	throw new SystemException("Could not perform post-parsing on R2 xml document: "
                    + e.getMessage());
        } catch (SystemException e) {
        	throw new SystemException("Could not perform post-parsing on R2 xml document: "
                    + e.getMessage());
		}

        return doc;
    }

    /**
     * provides hook for manipulation of the character stream prior to building the document
     * @param isr
     * @return
     * @throws IOException
     */
    InputStream preParse(InputStreamReader isr) throws IOException {
        InputStream outIs = filterForNBSPAndUspace(isr);

        return outIs;
    }

    /**
     * provides hook for post parsing of the Document or Element
     * @param doc
     * @throws JDOMException
     * @throws SAXException
     */
    void postParse(Document doc) throws JDOMException, SAXException {
        // fix bad frameworks (CTB Assessement Framework --> CAB), replacementoperation
        // See the badFrameworks.xml file
        FieldOperation badFrameworkOperation = new ReplacementOperation(ReplacementSetFactory.create(FrameworkReplacementConfig.instance().getInputStream()));

        R2XmlTools.replaceAttributes(doc, "//Hierarchy/@CurriculumID",
                badFrameworkOperation);
    }

    /**
     * Removes &nbsp; entities and replaces the &USpace; entity with NCR value &#160;
     * @param isr
     * @return
     * @throws IOException
     */
    InputStream filterForNBSPAndUspace(InputStreamReader isr) throws IOException {

        BufferedReader reader = new BufferedReader(isr);
        StringBuffer buff = new StringBuffer();

        while (reader.ready()) {
            String inString = reader.readLine();

            inString = StringUtils.replace(inString, "&nbsp;", " ");
            inString = StringUtils.replace(inString, "&Uspace;", "&#160;");
            inString = StringUtils.replace(inString, "&USpace;", "&#160;");
            buff.append(inString);
        }
        return new ByteArrayInputStream(buff.toString().getBytes());
    }

    /**
     * deprecated - does not support pre and post parsing for IKNOW products
     * @param file
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public Document build(File file) throws JDOMException, IOException {
        return super.build(file);
    }

    /**
     * deprecated - does not support pre and post parsing for IKNOW products
     * @param url
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public Document build(URL url) throws JDOMException, IOException {
        return super.build(url);
    }

    /**
     * deprecated - does not support pre and post parsing for IKNOW products
     * @param inputStream
     * @param s
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public Document build(InputStream inputStream, String s) throws JDOMException, IOException {
        return super.build(inputStream, s);
    }

    /**
     * deprecated - does not support pre and post parsing for IKNOW products
     * @param reader
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public Document build(Reader reader) throws JDOMException, IOException {
        return super.build(reader);
    }

    /**
     * deprecated - does not support pre and post parsing for IKNOW products
     * @param reader
     * @param s
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public Document build(Reader reader, String s) throws JDOMException, IOException {
        return super.build(reader, s);
    }

    /**
     * deprecated - does not support pre and post parsing for IKNOW products
     * @param s
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public Document build(String s) throws JDOMException, IOException {
        return super.build(s);
    }
}
