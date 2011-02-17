/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.itemset;

import java.util.ArrayList;

import java.util.List;

import org.jdom.Element;


import com.ctb.xmlProcessing.XMLConstants;

/**
 * @author wmli TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class ItemSetValidaterTerranova extends ItemSetValidater {
    public void validate(Element itemSetElement) {
        cumulateErrors(errorList, validateItemsHaveThinkCode(itemSetElement));
        cumulateErrors(errorList, validateItemsSuppressScore(itemSetElement, XMLConstants.NO));
    }

    private List validateItemsHaveThinkCode(Element itemSetElement) {
        List missingThinkCode = new ArrayList();
        return missingThinkCode;
/*        for (Iterator iter = ValidaterUtils.getItemElements(itemSetElement).iterator(); iter
                .hasNext();) {
            Element itemElement = (Element) iter.next();
            String itemId = itemElement.getAttributeValue(XMLConstants.ID);
            ;
            if (itemElement.getChild(XMLConstants.THINK_CODE) == null) {
                missingThinkCode.add("Items [" + itemId
                        + "] in the test body should contain ThinkCode element");
            }
        }
        return missingThinkCode; */
    }
}