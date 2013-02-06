package com.ctb.contentBridge.core.publish.xml.itemset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.xml.AbstractXMLElementValidater;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.ValidaterUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;


public abstract class ItemSetValidater extends AbstractXMLElementValidater {
    protected List errorList = new ArrayList();

    protected List validateItemsSuppressScore(Element itemSetElement, String expectedSuppressScore) {
        List itemsSuppressScore = new ArrayList();
        for (Iterator iter = ValidaterUtils.getItemElements(itemSetElement).iterator(); iter
                .hasNext();) {
            Element itemElement = (Element) iter.next();
            String itemId = itemElement.getAttributeValue(XMLConstants.ID);
            String suppressScore = BuilderUtils.extractAttributeOptional(itemElement,
                    XMLConstants.SUPRESS_SCORE, XMLConstants.NO);
  /*          if (!suppressScore.equals(expectedSuppressScore)) {
                itemsSuppressScore.add("Items [" + itemId
                        + "] in should has SuppressScore set to [" + expectedSuppressScore + "]."); 
            }*/
        }
        return itemsSuppressScore;
    }

    public List getErrorList() {
        return errorList;
    }

}