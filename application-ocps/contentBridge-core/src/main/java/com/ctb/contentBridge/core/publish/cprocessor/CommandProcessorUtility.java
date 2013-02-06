package com.ctb.contentBridge.core.publish.cprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.report.ItemProcessorReport;
import com.ctb.contentBridge.core.publish.xml.XMLUtils;


public class CommandProcessorUtility {
    private static Logger logger =
        Logger.getLogger(CommandProcessorUtility.class);

    static void verifyRootElementName(
        String elementName,
        String expectedElementName) throws BusinessException {
        if (!(elementName.equals(expectedElementName)))
            throw new BusinessException(
                expectedElementName + " not found in XML Document");
        logger.info(expectedElementName + " found in XML Document");
    }

    static void handleItemException(Exception e, Logger logger) {
        //TODO - mws - move this to decorator and add to factory
        ItemProcessorReport.getCurrentReport().setException(e);
        ItemProcessorReport.getCurrentReport().setSuccess(false);
        logger.error("Item Processing failed", e);
    }

    public static Element[] getItems(Element element) throws BusinessException {
        String rootNodeName = element.getName();
        if (rootNodeName.equals("Item")) {
            return new Element[] { element };
        } else if (rootNodeName.equals("ItemSet")) {
            List itemList = element.getChildren("Item");
            return (Element[]) itemList.toArray(new Element[itemList.size()]);
        } else {
            throw new BusinessException(
                "Cannot process XML document with root node: " + rootNodeName);
        }
    }
    
    public static List getItemsUnderTD(Element element) throws BusinessException {
        return XMLUtils.getItemSubElementsInTD( element );
    }

    public static Element getUniqueItemSet(Element subTestElement) throws BusinessException {
        List itemSets = subTestElement.getChildren("ItemSet");
        if (itemSets.size() != 1)
            throw new BusinessException(
                "SubTest contains "
                    + itemSets.size()
                    + " ItemSet elements instead of 1.");

        return (Element) itemSets.iterator().next();
    }

    public static List mapItemIDsInXML(Element subtest, Map mappeditemIds) throws BusinessException {
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
                throw new BusinessException(
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
