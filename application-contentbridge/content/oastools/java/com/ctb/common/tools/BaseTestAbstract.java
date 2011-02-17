package com.ctb.common.tools;


import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Element;


public class BaseTestAbstract extends TestCase {
	protected static Logger logger = Logger.getLogger(BaseTestAbstract.class);
	static {
		PropertyConfigurator.configure("conf/log4j.properties");
	}

    public String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public BaseTestAbstract() {
        super("No test");
    }

    public BaseTestAbstract(String s) {
        super(s);
    }

    void assertXPathEquals(String message, String xpath,
            String expected, Element element) throws Exception {
        String value = xpathValue(xpath, element);

        System.out.println("xpath = " + xpath);
        System.out.println("value = " + value);
        assertEquals(message, expected, value);
    }

    private String xpathValue(String xpath, Element element) throws JaxenException {
        String value = new JDOMXPath(xpath).stringValueOf(element);

        return value;
    }

    protected String xpathValueNonNull(String xpath, Element element) throws JaxenException {
        String value = xpathValue(xpath, element);

        if (value == null) {
            value = "";
        }
        return value;
    }

    void assertXPathExists(String message, String xpath, Element element) throws Exception {
        boolean empty = xpathExists(xpath, element);

        assertFalse(message, empty);
    }

    protected boolean xpathExists(String xpath, Element element) throws JaxenException {
        boolean empty = new JDOMXPath(xpath).selectSingleNode(element) == null;

        System.out.println("xpath = " + xpath + ": "
                + (empty ? "absent" : "present"));
        return !empty;
    }

    protected List xpathNodes(String xpath, Element element) throws JaxenException {
        return new JDOMXPath(xpath).selectNodes(element);
    }

    protected String xmlFilename(String itemId) {
        return itemId + ".xml";
    }

    protected File writeTestFile(String filename, String contents) throws IOException {
        File file = new File(UnitTestUtils.runtimeTestDir, filename);

        return IOUtils.writeFileUtf8(file, contents);
    }

    protected File writeTestXmlFile(String contents) throws IOException {
        File file = File.createTempFile("temp", "xml", UnitTestUtils.runtimeTestDir);

        IOUtils.writeFileUtf8(file, contents);
        return file;
    }

}
