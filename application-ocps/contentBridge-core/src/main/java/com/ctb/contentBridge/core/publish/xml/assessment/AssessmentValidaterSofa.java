package com.ctb.contentBridge.core.publish.xml.assessment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.AbstractXMLElementValidater;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;


/**
 * @author wmli
 */
public class AssessmentValidaterSofa extends AbstractXMLElementValidater {
    public static String[] attributeNames =
        {
            XMLConstants.FRAMEWORK_CODE,
            XMLConstants.PRODUCT_NAME,
            XMLConstants.GRADE };

    public void validate(Element assessmentElement) {
        List errorList = new ArrayList();
        cumulateErrors(
            errorList,
            validateRequiredField(assessmentElement, attributeNames));

        if (errorList.size() > 0) {
            throw new SystemException(
                "\n" + StringUtils.join(errorList.iterator(), "\n"));
        }
    }
}
