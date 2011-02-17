package com.ctb.xmlProcessing.assessment;





import org.jdom.Element;


import com.ctb.xmlProcessing.XMLConstants;


public class AssessmentValidaterIncremental extends AssessmentValidaterTerranova {
    public static String[] attributeNames = {XMLConstants.FRAMEWORK_CODE,
            XMLConstants.PRODUCT_NAME, XMLConstants.FORM};

    public void validate(Element assessmentElement) {
    }

}