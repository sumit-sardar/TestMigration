package com.ctb.contentBridge.core.publish.xml.subtest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;


/**
 * @author wmli
 */
public class SubTestValidaterSofa extends AbstractSubTestValidater {
    public static String[] attributeNames =
        {
            XMLConstants.FRAMEWORK_CODE,
            XMLConstants.PRODUCT_NAME,
            XMLConstants.GRADE,
            XMLConstants.SCORE_LOOKUP_ID,
            XMLConstants.SCORE_TYPE_CODE };

    public void validate(Element element) {
        List errorList = new ArrayList();

        cumulateErrors(
            errorList,
            validateRequiredField(element, attributeNames));
        cumulateErrors(errorList, validateUniqueChild(element));
        cumulateErrors(errorList, validateNoDuplicatedItems(element));

        if (errorList.size() > 0) {
            throw new BusinessException(
			"\n" + StringUtils.join(errorList.iterator(), "\n"));
        }
    }
}
