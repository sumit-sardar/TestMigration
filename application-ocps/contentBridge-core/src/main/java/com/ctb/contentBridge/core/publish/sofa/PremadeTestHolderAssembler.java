package com.ctb.contentBridge.core.publish.sofa;


import java.util.*;

import org.jdom.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;




public class PremadeTestHolderAssembler {
    public static final String SCORE_TYPE_CODE = "ScoreTypeCode";
    public static final String SCORE_LOOKUP_ID = "ScoreLookupID";
    public static final String FORM = "Form";
    public static final String DESCRIPTION = "Description";
    private static final String TIME_LIMIT = "TimeLimit";
    public static final String GRADE = "Grade";
    public static final String VERSION = "Version";
    public static final String TITLE = "Title";
    public static final String ID = "ID";
    public static final String PRODUCT_NAME = "ProductName";
    public static final String SUBTEST = "SubTest";
    public static final String FRAMEWORK_CODE = "FrameworkCode";
    public static final String MAX_PANEL_WIDTH = "MaxpanelWidth";
    public static final String INCLUDE_ACKNOWLEDGMENT = "IncludeAcknowledgment";
    public static final String CTBS4_OBJECTIVE = "*";
    PremadeTestHolder testHolder;

    public PremadeTestHolderAssembler() {}

    public PremadeTestHolder parseSubTest(Element rootElement) {
        String frameworkCode;
        String productDisplayName;
        String extTstItemSetId;
        String testName;
        String version;
        String itemSetLevel;
        String grade;
        int timeLimit;  // in seconds
        int breakTime;
        String itemSetDisplayName;
        String itemSetDescription;
        String itemSetForm;
        String scoreLookupId;
        String scoreTypeCode;

        if (!rootElement.getName().equals(SUBTEST)) {
            throw new SystemException("Invalid root element: "
                    + rootElement.getName());
        }
        frameworkCode = extractAttributeMandatory(rootElement, FRAMEWORK_CODE);
        productDisplayName = extractAttributeMandatory(rootElement, PRODUCT_NAME);
        extTstItemSetId = extractAttributeMandatory(rootElement, ID);
        testName = extractAttributeMandatory(rootElement, TITLE);
        version = extractAttributeOptional(rootElement, VERSION);
        grade = extractAttributeMandatory(rootElement, GRADE);
        if (grade.trim().length() == 0) {
            throw new SystemException("The " + GRADE
                    + " attribute cannot be empty");
        }
        itemSetLevel = grade;
        timeLimit = extractIntegerAttributeMandatory(rootElement, TIME_LIMIT)
                * 60;
        breakTime = 0;
        itemSetDisplayName = testName;
        itemSetDescription = extractChildElementValue(rootElement, DESCRIPTION);
 //       itemSetForm = extractAttributeMandatory(rootElement, FORM);
        itemSetForm = extractAttributeOptional(rootElement, FORM);
        scoreLookupId = extractAttributeMandatory(rootElement, SCORE_LOOKUP_ID);
        scoreTypeCode = extractAttributeMandatory(rootElement, SCORE_TYPE_CODE);

        testHolder = new PremadeTestHolder(frameworkCode, productDisplayName,
                extTstItemSetId, testName, version, itemSetLevel, grade,
                timeLimit, breakTime, itemSetDisplayName, itemSetDescription,
                itemSetForm, scoreLookupId);
        addItems(testHolder, rootElement, scoreTypeCode);

        return testHolder;
    }

    private void addItems(PremadeTestHolder testHolder, Element rootElement, String scoreTypeCode) {
        Iterator iter = rootElement.getChildren("ItemSet").iterator();

        while (iter.hasNext()) {
            Element itemSetElement = (Element) iter.next();
            Iterator items = itemSetElement.getChildren("Item").iterator();

            while (items.hasNext()) {
                Element itemElement = (Element) items.next();
                String itemID = extractAttributeMandatory(itemElement, ID);
                String objectiveID = extractAttributeMandatory(itemElement,
                        "ObjectiveID");
                String suppress = BuilderUtils.extractAttributeOptional( itemElement, "SuppressScore");
                String fieldTest = BuilderUtils.extractAttributeOptional( itemElement, "FieldTest");
                if (objectiveID.equals(CTBS4_OBJECTIVE)) {
                    testHolder.addItem(new PremadeTestHolder.TestItem(itemID,
                            scoreTypeCode, fieldTest, suppress));
                } else {
                    testHolder.addItem(new PremadeTestHolder.TestItem(itemID, fieldTest, suppress));
                }
            }
        }
    }

    private String extractAttributeMandatory(Element element, String attributeName) {
        String value = element.getAttributeValue(attributeName);

        if (value == null) {
            throw new SystemException("Attribute " + attributeName
                    + " of element <" + element.getName() + "> is mandatory.");
        }
        return value;
    }

    private int extractIntegerAttributeMandatory(Element element, String attributeName) {
        String stringValue = extractAttributeMandatory(element, attributeName);
        int value = 0;

        try {
            value = Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            throw new SystemException("Attribute " + attributeName
                    + " does not contain an integer number: " + stringValue);
        }
        return value;
    }

    private String extractAttributeOptional(Element element, String attributeName) {
        String value = element.getAttributeValue(attributeName);

        if (value == null) {
            value = "";
        }
        return value;
    }

    private String extractChildElementValue(Element element, String childElementName) {
        Element child = element.getChild(childElementName);

        if (child == null) {
            throw new SystemException("Element " + childElementName
                    + " not found under element " + element.getName());
        }
        return child.getText();
    }

}
