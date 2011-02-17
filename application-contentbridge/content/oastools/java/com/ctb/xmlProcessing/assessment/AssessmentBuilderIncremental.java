package com.ctb.xmlProcessing.assessment;

import org.jdom.Element;

import com.ctb.common.tools.SystemException;
import com.ctb.xmlProcessing.BuilderUtils;
import com.ctb.xmlProcessing.XMLConstants;

import com.ctb.xmlProcessing.utils.ProductConfig;

public class AssessmentBuilderIncremental extends AssessmentBuilder implements XMLConstants {
    private ProductConfig productConfig;

    public AssessmentBuilderIncremental(ProductConfig productConfig) {
        this.productConfig = productConfig;
    }

    public AssessmentHolder build(Element rootElement) {
        String frameworkCode;
        String productDisplayName;
        String extTstItemSetId;
        String testName;
        String version;
        String itemSetLevel;
        String grade;
        int timeLimit; // in seconds
        int breakTime;
        String itemSetDisplayName;
        String itemSetDescription;
        String itemSetForm;
        String commodityCode;
        Long productId = new Long(0);

        if (!rootElement.getName().equals(XMLConstants.ELEMENT_NAME_ASSESSMENT)) {
            throw new SystemException("Invalid root element: " + rootElement.getName());
        }

        frameworkCode = BuilderUtils.extractAttributeOptional(rootElement, FRAMEWORK_CODE);
        productDisplayName = BuilderUtils.extractAttributeOptional(rootElement, PRODUCT_NAME);
        itemSetForm = BuilderUtils.extractAttributeOptional(rootElement, FORM);

        // compose the
        extTstItemSetId = BuilderUtils.genExtAssessmentId(rootElement, productConfig);
        testName = BuilderUtils.genName(rootElement,productConfig);

        version = BuilderUtils.extractAttributeOptional(rootElement, VERSION);

        grade = BuilderUtils.extractAttributeOptional(rootElement, VERSION);

        itemSetLevel = BuilderUtils.extractAttributeOptional(rootElement, LEVEL);
        commodityCode = BuilderUtils.extractAttributeOptional(rootElement, "CommodityCode");

        String timeLimitString = BuilderUtils.extractAttributeOptional(rootElement, TIME_LIMIT);
        timeLimit = 0;

        breakTime = 0;
        itemSetDisplayName = testName;

        // todo - mws - test this with Assessment description child elements
        try {
            itemSetDescription = BuilderUtils.extractChildElementValue(rootElement, DESCRIPTION);  
 //           itemSetDescription = AbstractSubTestBuilder.replaceAll( itemSetDescription, "'", "&apos;" );
        } catch (SystemException se) {
            itemSetDescription = "";
                // BuilderUtils.extractAttributeMandatory(rootElement, DESCRIPTION);
        }

        AssessmentHolder assessmentHolder = new AssessmentHolder(productId, frameworkCode,
                productDisplayName, extTstItemSetId, testName, version, itemSetLevel, grade,
                timeLimit, breakTime, itemSetDisplayName, itemSetDescription, itemSetForm, commodityCode);

        return assessmentHolder;
    }
}