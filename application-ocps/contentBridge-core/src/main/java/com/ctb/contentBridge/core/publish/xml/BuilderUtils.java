package com.ctb.contentBridge.core.publish.xml;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.dao.ProductConfig;
import com.ctb.contentBridge.core.publish.dao.ProductTypeInfo;
/**
 * @author wmli
 */
public class BuilderUtils {
    private static Logger logger = Logger.getLogger(BuilderUtils.class);

    public static String extractMandatoryNonEmptyAttribute(Element element, String attributeName) {
        String value = extractAttributeMandatory(element, attributeName);
        if (value.trim().length() == 0) {
            logger
                    .error("Empty " + attributeName + ": "
                            + new XMLOutputter().outputString(element));
            throw new SystemException("The " + attributeName + " attribute of <"
                    + element.getName() + "> cannot be empty");
        }

        return value;
    }

    public static String extractAttributeMandatory(Element element, String attributeName) {
        String value = element.getAttributeValue(attributeName);

        if (value == null) {
            logger.error("Missing " + attributeName + ": "
                    + new XMLOutputter().outputString(element));
            throw new SystemException("Attribute " + attributeName + " of element <"
                    + element.getName() + "> is mandatory.");
        }
        return value;
    }

    public static int extractIntegerAttributeMandatory(Element element, String attributeName) {
        String stringValue = extractAttributeMandatory(element, attributeName);
        int value = 0;

        try {
            value = Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            logger.error("Missing " + attributeName + ": "
                    + new XMLOutputter().outputString(element));
            throw new SystemException("Attribute " + attributeName
                    + " does not contain an integer number: " + stringValue);
        }
        return value;
    }

    public static String extractAttributeOptional(Element element, String attributeName) {
        String value = element.getAttributeValue(attributeName);

        if (value == null) {
            value = "";
        }
        return value.trim();
    }

    public static String extractAttributeOptional(Element element, String attributeName,
            String defaultValue) {
        if (element == null)
            return defaultValue;

        String value = element.getAttributeValue(attributeName);

        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static String extractChildElementValue(Element element, String childElementName) {
        Element child = element.getChild(childElementName);

        if (child == null) {
            logger.error("Missing " + childElementName + ": "
                    + new XMLOutputter().outputString(element));
            throw new SystemException("Element " + childElementName + " not found under element "
                    + element.getName());
        }
        return child.getText();
    }

    public static String genExtAssessmentId(Element rootElement, ProductConfig config) {
        String frameworkCode = BuilderUtils.extractAttributeOptional(rootElement,
                XMLConstants.FRAMEWORK_CODE);
        String productName = BuilderUtils.extractAttributeOptional(rootElement,
                XMLConstants.PRODUCT_NAME);
        String itemSetForm = BuilderUtils.extractAttributeOptional(rootElement, XMLConstants.FORM);

        // compose the id
        return frameworkCode + "_"
                + config.findProductType(frameworkCode, productName).getAbbreviation() + "_"
                + itemSetForm;
    }

    public static String genExtSubtestId(Element rootElement, ProductConfig config) {
        String frameworkCode = BuilderUtils.extractAttributeOptional(rootElement,
                XMLConstants.FRAMEWORK_CODE);
        String productName = BuilderUtils.extractAttributeOptional(rootElement,
                XMLConstants.PRODUCT_NAME);
        String itemSetForm = BuilderUtils.extractAttributeOptional(rootElement, XMLConstants.FORM);
        String itemSetLevel = BuilderUtils
                .extractAttributeOptional(rootElement, XMLConstants.LEVEL);
        String contentArea = BuilderUtils.extractAttributeOptional(rootElement,
                XMLConstants.CONTENT_AREA);

        ProductTypeInfo productType = config.findProductType(frameworkCode, productName);
        // compose the id
        return frameworkCode + "_" + productType.getAbbreviation() + "_" + itemSetForm + "_"
                + itemSetLevel + "_" + productType.findContentArea(contentArea).getAbbreviation();
    }

    public static String genName(Element rootElement, ProductConfig config) {
        String frameworkCode = BuilderUtils.extractAttributeOptional(rootElement,
                XMLConstants.FRAMEWORK_CODE);
        String productName = BuilderUtils.extractAttributeOptional(rootElement,
                XMLConstants.PRODUCT_NAME);

        ProductTypeInfo productType = config.findProductType(frameworkCode, productName);

        if (productType.getTitle() == null)
            throw new SystemException("Title for incremental test build not set in properties file. ["
                    + productType.getAbbreviation() + ":" + frameworkCode + ":" + productName + "]");

        // compose the id
        return productType.getTitle();

    }

}