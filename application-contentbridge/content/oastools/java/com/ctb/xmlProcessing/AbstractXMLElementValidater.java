package com.ctb.xmlProcessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 * @author wmli
 */
public abstract class AbstractXMLElementValidater
    implements XMLElementValidater {

    protected List validateRequiredField(
        Element element,
        String[] attributeNames) {
        List missingRequiredAttributes = new ArrayList();

        for (int i = 0; i < attributeNames.length; i++) {
            if (element.getAttributeValue(attributeNames[i]) == null) {
                missingRequiredAttributes.add(attributeNames[i]);
            }
        }

        if (missingRequiredAttributes.size() > 0) {
            return createErrorList(
                "Missing required attributes: " + missingRequiredAttributes);
        } else {
            return null;
        }
    }

    public List createErrorList(String errorMessage) {
        return Arrays.asList(new String[] { errorMessage });
    }

    public List createErrorList(String[] errorMessages) {
        return Arrays.asList(errorMessages);
    }

    public void cumulateErrors(List errorList, List additionalErrors) {
        if (additionalErrors != null)
            errorList.addAll(additionalErrors);
    }

    protected String extractAttributeValues(
        Element element,
        List attributeNames) {
        String attributeValues = "";
        for (Iterator iter = attributeNames.iterator(); iter.hasNext();) {
            String attrName = (String) iter.next();
            if (attributeNames.indexOf(attrName) > 0) {
                attributeValues += ",";
            }

            attributeValues
                += BuilderUtils.extractAttributeOptional(element, attrName);
        }

        return attributeValues;
    }

}
