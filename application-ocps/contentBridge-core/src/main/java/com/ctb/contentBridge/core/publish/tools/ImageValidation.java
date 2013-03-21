package com.ctb.contentBridge.core.publish.tools;


import java.util.*;

import org.jdom.*;
import org.xml.sax.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.iknowxml.R2XmlTools;
import com.ctb.contentBridge.core.publish.sax.replacement.AbsolutePathOperation;



/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Aug 8, 2003
 * Time: 11:36:22 AM
 * To change this template use Options | File Templates.
 */
public class ImageValidation {

    AbsolutePathOperation imagesHolder;

    public ImageValidation(Element element) {
        try {
            AbsolutePathOperation absolutePathOperation = new AbsolutePathOperation();

            this.imagesHolder = absolutePathOperation;
            // R2XmlTools.replaceAttributes(element,"//BMPPrint/@FileName",absolutePathOperation);
            R2XmlTools.replaceAttributes(element, "//EPSPrint/@FileName",
                    absolutePathOperation);
            R2XmlTools.replaceAttributes(element, "//Flash/@FileName",
                    absolutePathOperation);
        } catch (JDOMException e) {
            throw new SystemException("Could not read JDOM document: "
                    + e.getMessage());
        } catch (SAXException e) {
            throw new SystemException("Could not read inputsource: "
                    + e.getMessage());
        }

    }

    public Set getArtFileNames() {
        return imagesHolder.getFilenames();
    }

    public boolean validateArt() {
        return imagesHolder.allFilesExist();
    }

    public Set getInvalidArtFileNames() {
        return imagesHolder.invalidFiles();
    }

    public String toString() {
        return imagesHolder.toString();
    }

}

