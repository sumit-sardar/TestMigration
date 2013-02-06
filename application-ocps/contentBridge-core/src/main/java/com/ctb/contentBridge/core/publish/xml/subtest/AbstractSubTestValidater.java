package com.ctb.contentBridge.core.publish.xml.subtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.xml.AbstractXMLElementValidater;
import com.ctb.contentBridge.core.publish.xml.ValidaterUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;


/**
 * @author wmli
 */
public abstract class AbstractSubTestValidater
    extends AbstractXMLElementValidater {

    protected List validateUniqueChild(Element element) {
        return null;
    }

    protected List validateNoDuplicatedItems(Element subTestElement) throws BusinessException {
        List duplicatedItems = getDuplicatedItemList(subTestElement);

        // throw exception for the duplicated items.
        Collections.sort(duplicatedItems);
        if (duplicatedItems.size() > 0) {
            return createErrorList(
                "Duplicated items in subTest: " + duplicatedItems);
        } else {
            return null;
        }
    }

    /**
     * Count the number of item show up in the subtest
     * @throws BusinessException 
     */

    private List getDuplicatedItemList(Element subTestElement) throws BusinessException {
        Map itemMap = countItems(subTestElement);

        List duplicatedItems = new ArrayList();
        // list all the items with counter greater then one.
        for (Iterator iter = itemMap.keySet().iterator(); iter.hasNext();) {
            String itemId = (String) iter.next();

            if (((Integer) itemMap.get(itemId)).intValue() > 1) {
                duplicatedItems.add(itemId);
            }
        }
        return duplicatedItems;
    }

    /**
     * Count the number of item show up in the subtest
     * @throws BusinessException 
     */
    private Map countItems(Element subTestElement) throws BusinessException {
        HashMap itemMap = new HashMap();

        for (Iterator iter =
            ValidaterUtils.getItemElements(subTestElement).iterator();
            iter.hasNext();
            ) {
            Element itemElement = (Element) iter.next();

            String itemId = itemElement.getAttributeValue(XMLConstants.ID);

            Integer counter;
            if ((counter = (Integer) itemMap.get(itemId)) == null) {
                itemMap.put(itemId, new Integer(1));
            } else {
                itemMap.put(itemId, new Integer(counter.intValue() + 1));
            }
        }

        return itemMap;
    }
}
