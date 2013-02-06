package com.ctb.contentBridge.core.util;


import java.io.*;
import java.util.*;

import com.ctb.contentBridge.core.exception.BusinessException;

import org.apache.log4j.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 30, 2003
 * Time: 6:23:01 PM
 * To change this template use Options | File Templates.
 */
public class ParseFilterProperties extends Properties {
	private static Logger logger = Logger.getLogger(ParseFilterProperties.class);

    public static String DEFAULT_PROP_FILE = "conf/filter.properties";
    public static String MEDIA_IMAGES_BASEPATH = "media.images.basepath";
    public static String MEDIA_TEMP_DIRECTORY = "media.dir.temp";
    // defaults needed for below constants
    public static String MEDIA_XML_BASEPATH = "media.xml.basepath";
    public static String XSL_FOR_SR_XML_TO_PDF = "media.xsl.sr";
    public static String XSL_FOR_CR_XML_TO_PDF = "media.xsl.cr";
    public static String XSL_FOR_RUBRIC_XML_TO_PDF = "media.xsl.rubric";
    public static String R2_DTD = "r2.dtd";

    private static ParseFilterProperties instance = null;
    ;

    public ParseFilterProperties() throws BusinessException {
        loadParserFilterProperties(DEFAULT_PROP_FILE);
    }

    public ParseFilterProperties(String propFileName) throws BusinessException {
        loadParserFilterProperties(propFileName);
    }

    public ParseFilterProperties(Properties defaults) {
        super(defaults);
    }

    private void loadParserFilterProperties(String propsFileName) throws BusinessException {

        try {
            StreamResource resource = new StreamResource();

            load(resource.getStream(propsFileName));
            logger.debug("Loading filter.properties:" + propsFileName);
        } catch (IOException e) {
            throw new BusinessException("Resource Missing: Could not locate filter.properties file");
        }
    }

    public String getTempDirectory() {
        return getProperty(MEDIA_TEMP_DIRECTORY);
    }

    public String getImagesBasePath() {
        return getProperty(MEDIA_IMAGES_BASEPATH);
    }

    public File getImagesBaseDir() {
        return new File(getProperty(MEDIA_IMAGES_BASEPATH));
    }

    public String getImagesBaseCanonicalPath() throws BusinessException {
        File file = new File(getProperty(MEDIA_IMAGES_BASEPATH));

        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new BusinessException("Could not get canonical path for images base directory");
        }
    }

    public File getImagesBaseCanonicalFile() throws BusinessException {
        File file = new File(getProperty(MEDIA_IMAGES_BASEPATH));

        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            throw new BusinessException("Could not get canonical path for images base directory");
        }
    }

    public String getXMLBasePath() {
        return getProperty(MEDIA_XML_BASEPATH);
    }

    public String getXSLforSRToPDF() {
        return getProperty(XSL_FOR_SR_XML_TO_PDF);
    }

    public String getXSLforCRToPDF() {
        return getProperty(XSL_FOR_CR_XML_TO_PDF);
    }

    public String getXSLforRubricToPDF() {
        return getProperty(XSL_FOR_RUBRIC_XML_TO_PDF);
    }

    public String getDTDforR2App() {
        return getProperty(R2_DTD);
    }

    public static ParseFilterProperties instance() throws BusinessException {
        if (instance == null) {
            instance = new ParseFilterProperties();
        }
        return instance;
    }
}
