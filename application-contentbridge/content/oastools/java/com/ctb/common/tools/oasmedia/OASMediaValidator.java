package com.ctb.common.tools.oasmedia;

import org.jdom.Element;

/**
 * User: mwshort
 * Date: Dec 12, 2003
 * Time: 12:24:59 PM
 * 
 *
 */
public class OASMediaValidator {
    public static final String VALIDATION_MESSAGE = "Assessment Elements must form " +
            "the root node for OAS Media Generation";
    private static final String ASSESSMENT_ELEMENT_NAME = "Assessment";
    public boolean validForMediaMediaGeneration(Element assessmentRoot) {
        boolean returnValue = assessmentRoot.getName().equals(ASSESSMENT_ELEMENT_NAME);
        return returnValue;
    }

}
