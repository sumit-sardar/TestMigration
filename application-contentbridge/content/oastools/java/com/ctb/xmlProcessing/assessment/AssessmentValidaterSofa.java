package com.ctb.xmlProcessing.assessment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

import com.ctb.common.tools.SystemException;
import com.ctb.xmlProcessing.AbstractXMLElementValidater;
import com.ctb.xmlProcessing.XMLConstants;

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
