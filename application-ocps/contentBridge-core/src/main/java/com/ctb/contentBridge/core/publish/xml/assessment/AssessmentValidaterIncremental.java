package com.ctb.contentBridge.core.publish.xml.assessment;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.xml.XMLConstants;

public class AssessmentValidaterIncremental extends AssessmentValidaterTerranova {
    public static String[] attributeNames = {XMLConstants.FRAMEWORK_CODE,
            XMLConstants.PRODUCT_NAME, XMLConstants.FORM};

    public void validate(Element assessmentElement) {
    }

}