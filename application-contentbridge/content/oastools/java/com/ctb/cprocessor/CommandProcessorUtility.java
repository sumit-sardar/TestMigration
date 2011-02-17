package com.ctb.cprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.common.tools.SystemException;
import com.ctb.reporting.ItemProcessorReport;
import com.ctb.xmlProcessing.XMLUtils;

public class CommandProcessorUtility {
    private static Logger logger =
        Logger.getLogger(CommandProcessorUtility.class);

    static void verifyRootElementName(
        String elementName,
        String expectedElementName) {
        if (!(elementName.equals(expectedElementName)))
            throw new InvalidRootElementException(
                expectedElementName + " not found in XML Document");
        logger.info(expectedElementName + " found in XML Document");
    }

    static void handleItemException(Exception e, Logger logger) {
        //TODO - mws - move this to decorator and add to factory
        ItemProcessorReport.getCurrentReport().setException(e);
        ItemProcessorReport.getCurrentReport().setSuccess(false);
        logger.error("Item Processing failed", e);
    }

    public static Element[] getItems(Element element) {
        String rootNodeName = element.getName();
        if (rootNodeName.equals("Item")) {
            return new Element[] { element };
        } else if (rootNodeName.equals("ItemSet")) {
            List itemList = element.getChildren("Item");
            return (Element[]) itemList.toArray(new Element[itemList.size()]);
        } else {
            throw new InvalidRootElementException(
                "Cannot process XML document with root node: " + rootNodeName);
        }
    }
    
    public static List getItemsUnderTD(Element element) {
        return XMLUtils.getItemSubElementsInTD( element );
    }

    public static Element getUniqueItemSet(Element subTestElement) {
        List itemSets = subTestElement.getChildren("ItemSet");
        if (itemSets.size() != 1)
            throw new SystemException(
                "SubTest contains "
                    + itemSets.size()
                    + " ItemSet elements instead of 1.");

        return (Element) itemSets.iterator().next();
    }

    public static List mapItemIDsInXML(Element subtest, Map mappeditemIds) {
        logger.debug("SubTest: Mapping Item IDs in SubTest XML");
        List mappedItemIds = new ArrayList();
        Iterator itemElements = XMLUtils.getItemSubElementsInItemSets(subtest);

        while (itemElements.hasNext()) {
            Element itemElement = (Element) itemElements.next();
            
            String itemType = itemElement.getAttributeValue("ItemType");
            if (itemType.equals("NI"))
            	continue;

            String curItemId = itemElement.getAttributeValue("ID");
            String mappedItemId = (String) mappeditemIds.get(curItemId);

            if (mappedItemId == null) {
                throw new SystemException(
                    "Item ["
                        + curItemId
                        + "] is part of the SubTest XML but was not processed by the Item Processor");
            }

            itemElement.setAttribute("ID", mappedItemId);
            mappedItemIds.add(mappedItemId);

            logger.debug(
                "SubTest: Item ["
                    + curItemId
                    + "] mapped to ["
                    + mappedItemId
                    + "]");

        }

        return mappedItemIds;
    }
}
