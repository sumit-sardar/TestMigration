package com.ctb.common.tools;


import java.io.*;

import org.apache.commons.lang.*;
import org.jdom.*;
import org.jdom.output.*;

import com.ctb.util.iknowxml.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Aug 6, 2003
 * Time: 4:55:32 PM
 * To change this template use Options | File Templates.
 * Flash Generator Usage guide:
 *
 *       File assessmentXML = new File("assessment.xml");
 *       R2DocumentBuilder builder = new R2DocumentBuilder("org.apache.xerces.parsers.SAXParser",false);
 *       Element rootElement = null;
 *       try {
 *           Document document = builder.build(new FileInputStream(assessmentXML));
 *           rootElement = document.getRootElement();
 *       } catch (JDOMException e) {
 *           throw new SystemException("Error parsing xml: " + e.getMessage(),e);
 *       } catch (IOException e) {
 *           throw new SystemException("Error reading xml: " + e.getMessage(),e);
 *       }
 *
 *       ImageValidation validator = new ImageValidation(rootElement);
 *       if (!validator.validateArt())
 *           throw new SystemException("Could not validate art files for assessment: " + validator);
 *
 *       File myFlashTemplate = new File("myFlashTemplate.swt");
 *       FlashGenerator generator = new FlashGenerator(myFlashTemplate);
 *       byte[] myFlashMovie = generator.generate(rootElement);
 *
 */
public class FlashGenerator {

    File swtFile = null;
    XMLOutputter outputter = new R2XmlOutputter();

    public FlashGenerator(File swtFile, XMLOutputter outputter) {
        this.swtFile = swtFile;
        if (outputter != null) {
            this.outputter = outputter;
        }
    }

    /**
     * Creates a FlashGenerator wrapper/gateway
     * @param swtFileName proper name to the flash template used in this instance of media generation
     */
    public FlashGenerator(String swtFileName) {
        this(new File(swtFileName), null);
    }

    /**
     * Creates a FlashGenerator wrapper/gateway
     * @param swtFile proper name to the flash template used in this instance of media generation
     */
    public FlashGenerator(File swtFile) {
        this(swtFile, null);
    }

    /**
     * Creates a FlashGenerator wrapper/gateway without a default flash template
     */
    public FlashGenerator() {}

    /**
     * Uses template specified at Construction of the instance to create a flash movie from JDOM element
     * @param element
     * @return byte array for a flash movie
     */
    public byte[] generate(Element element) {
        if (swtFile == null) {
            throw new SystemException("No Flash Template specified");
        }
        return generate(new ByteArrayInputStream(R2XmlTools.xmlToByteArray(element,
                outputter)));
    }

    /**
     * Uses JDOM element and flash template stream to create the Flash Movie
     * @param element
     * @param templateIs
     * @return byte array for Flash movie
     */
    public byte[] generate(Element element, InputStream templateIs) {
        return generate(R2XmlTools.getInputStream(element), templateIs);
    }

    /**
     * Uses template specified at Construction of the instance to create a flash movie from input xml
     * @param is
     * @return byte array for Flash movie
     */
    public byte[] generate(InputStream is) {
        if (swtFile == null) {
            throw new SystemException("No Flash Template specified");
        }
        try {
            return generate(is, new FileInputStream(swtFile));
        } catch (FileNotFoundException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     * Uses JDOM element and flash template stream to create the Flash Movie
     * @param xmlIs xml Stream properly preparsed, see R2DocumentBuilder and R2XmlTools
     * @param templateIs stream for the flash template
     * @return byte array of Flash Movie
     */
    public byte[] generate(InputStream xmlIs, InputStream templateIs) {
        byte[] bytes = filterUspace(xmlIs);
        JGenWrapper jGen = new JGenWrapper();

        return jGen.generateFlashMovie(new ByteArrayInputStream(bytes),
                templateIs);
    }

    /**
     * Jgenerate can not handle custom entities and NCR values, convert &#160; to ' '
     * @param is XML inputstream to be converted
     * @return byte array without the &#160;
     */
    private static byte[] filterUspace(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer buff = new StringBuffer();

        try {
            while (reader.ready()) {
                String inString = reader.readLine();

                inString = StringUtils.replace(inString, "&#160;", " ");
                buff.append(inString);
            }
        } catch (IOException e) {
            throw new SystemException("Error reading lines from byte array: "
                    + e.getMessage(),
                    e);
        }
        return buff.toString().getBytes();
    }
}
