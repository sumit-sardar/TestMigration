package com.ctb.contentBridge.core.publish.xml.assessment;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.xml.AbstractXMLElementValidater;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.ValidaterUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;

/**
 * @author wmli
 */
public class AssessmentValidaterTerranova extends AbstractXMLElementValidater {

    public static String[] attributeNames = { XMLConstants.FRAMEWORK_CODE,
            XMLConstants.PRODUCT_ID};

    public void validate(Element assessmentElement) {
        List errorList = new ArrayList();
        cumulateErrors(errorList, validateRequiredField(assessmentElement,
                attributeNames));
        if (errorList.size() > 0) { throw new BusinessException("\n"
                + StringUtils.join(errorList.iterator(), "\n")); }
    }

    /**
     * Validate no sample and research questions in test body.
     * 
     * @param assessmentElement
     * @param subTestBodyElements
     * @return
     */
    public List validateNoSampleAndResearchQuestion(Element assessmentElement,
            List subTestBodyElements) {
        
        List sampleRQTypeInBodyErrorList = new ArrayList();
        for (Iterator iter = subTestBodyElements.iterator(); iter.hasNext();) {
            Element subTestElement = (Element) iter.next();
            Set uniqueItemTypes = ValidaterUtils
                    .getUniqueItemType(subTestElement);

            if ((uniqueItemTypes.contains(XMLConstants.SAMPLE))
                    || (uniqueItemTypes.contains(XMLConstants.ITEM_TYPE_RQ))) {

                sampleRQTypeInBodyErrorList.add("SubTest ["
                        + subTestElement.getAttributeValue(XMLConstants.ID)
                        + "] should not includes sample or research questions.");
            }
        }

        return sampleRQTypeInBodyErrorList;

    }

    public List validateSubTestConsistency(Element assessmentElement,
            List subTestElements, List attributeNames) {
        Map inconsistence = new HashMap();
        // extract all the option value and concat them into a string
        String assessmentAttributeValues = extractAttributeValues(
                assessmentElement, attributeNames);
        for (Iterator iter = subTestElements.iterator(); iter.hasNext();) {
            Element subTestElement = (Element) iter.next();
            String subTestAttributeValues = extractAttributeValues(
                    subTestElement, attributeNames);
            if (!assessmentAttributeValues
                    .equalsIgnoreCase(subTestAttributeValues)) {
                inconsistence.put(subTestElement
                        .getAttributeValue(XMLConstants.ID),
                        subTestAttributeValues);
            }
        }
        if (inconsistence.size() > 0) {
            String message = "Values of one or more of these XML attributes are inconsistent between the SubTests and the Assessment: "
                    + attributeNames + "\n";
            message += "Values in the Assessment [" + assessmentAttributeValues
                    + "]\n";
            List keys = new ArrayList(inconsistence.keySet());
            Collections.sort(keys);
            for (Iterator iter = keys.iterator(); iter.hasNext();) {
                String subTestId = (String) iter.next();
                message += "Values in SubTest with ID [" + subTestId + "]: ["
                        + ((String) inconsistence.get(subTestId)) + "]\n";
            }
            return createErrorList(message);
        } else {
            return null;
        }
    }

    public List validateSampleSubTest(Element sampleSubTestElement) {
        List sampleErrorList = new ArrayList();
        cumulateErrors(sampleErrorList,
                validateIsSampleSubTest(sampleSubTestElement));
        cumulateErrors(sampleErrorList,
                validateZeroTimeLimit(sampleSubTestElement));
        cumulateErrors(sampleErrorList, validateZeroGrade(sampleSubTestElement));
        return sampleErrorList;
    }

    public List validateIsSampleSubTest(Element sampleSubTestElement) {
        Set uniqueItemTypes = ValidaterUtils
                .getUniqueItemType(sampleSubTestElement);
        if ((uniqueItemTypes.size() > 0)
                && (!uniqueItemTypes.contains(XMLConstants.SAMPLE))) {
            return createErrorList("First SubTest is not a Sample SubTest.");
        } else {
            return null;
        }
    }

    public List validateResearchSurveySubTest(Element researchSubTestElement) {
        ArrayList researchErrorList = new ArrayList();
        cumulateErrors(researchErrorList,
                validateIsResearchSubTest(researchSubTestElement));
        cumulateErrors(researchErrorList,
                validateZeroTimeLimit(researchSubTestElement));
        cumulateErrors(researchErrorList,
                validateZeroGrade(researchSubTestElement));
        return researchErrorList;
    }

    public List validateIsResearchSubTest(Element researchSubTestElement) {
        if (!BuilderUtils.extractAttributeOptional(researchSubTestElement,
                XMLConstants.CONTENT_AREA).equalsIgnoreCase(
                XMLConstants.CONTENT_AREA_RESEARCH)) { return createErrorList("Content Area for the research SubTest should be [RESEARCH]"); }
        Set uniqueItemTypes = ValidaterUtils
                .getUniqueItemType(researchSubTestElement);
        if ((uniqueItemTypes.size() == 1)
                && (!uniqueItemTypes.contains(XMLConstants.ITEM_TYPE_RQ))) {
            return createErrorList("Last SubTest is not a Research SubTest");
        } else {
            return null;
        }
    }

    public List validateZeroGrade(Element researchSubTestElement) {
        String grade = BuilderUtils.extractAttributeMandatory(
                researchSubTestElement, XMLConstants.GRADE);
        if (!grade.equals("0")) {
            return createErrorList("SubTests containing sample questions or research questions must have grade set to 0.");
        } else {
            return null;
        }
    }

    public List validateZeroTimeLimit(Element element) {
        int sampleTimeLimit = BuilderUtils.extractIntegerAttributeMandatory(
                element, XMLConstants.TIME_LIMIT);
        if (sampleTimeLimit != 0) {
            return createErrorList("Time limits on samples and the research survey subtests must be set to 0.");
        } else {
            return null;
        }
    }
}
