/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.sampleset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import com.ctb.common.tools.SystemException;
import com.ctb.xmlProcessing.BuilderUtils;
import com.ctb.xmlProcessing.ValidaterUtils;
import com.ctb.xmlProcessing.XMLConstants;
import com.ctb.xmlProcessing.itemset.ItemSetValidater;

public class SampleSetValidater extends ItemSetValidater {
    public void validate(Element sampleSetElement) {
        cumulateErrors(errorList, validateSampleItems(sampleSetElement));
        cumulateErrors(errorList, validateItemsSuppressScore(sampleSetElement, XMLConstants.YES));
    }

    private List validateSampleItems(Element element) {
        // validate that all items in the itemset are valid sample items.
        for (Iterator iter = ValidaterUtils.getItemElements(element).iterator(); iter.hasNext();) {
            Element itemElement = (Element) iter.next();
            String itemId = itemElement.getAttributeValue(XMLConstants.ID);
            try {
                List itemErrorList = new ArrayList();
                cumulateErrors(itemErrorList, validateDistractorRationaleExistedInItem(itemElement));
                return itemErrorList;
            } catch (JDOMException e) {
                throw new SystemException(e.getMessage(), e);
            }
        }
        return null;
    }

    private List validateDistractorRationaleExistedInItem(Element itemElement) throws JDOMException {
        String itemId = itemElement.getAttributeValue(XMLConstants.ID);
        List distractorErrorList = new ArrayList();
        XPath distractorRationaleXPath = XPath.newInstance(".//DistractorRationale");
        int distractorRationaleCount = 0;
        for (Iterator iterator = distractorRationaleXPath.selectNodes(itemElement).iterator(); iterator
                .hasNext();) {
            Element distractorRationale = (Element) iterator.next();
            distractorRationaleCount++;
            try {
                cumulateErrors(distractorErrorList,
                        validateDisplayMessageExistedInDistractorRationale(itemId,
                                distractorRationale));
            } catch (SystemException se) {
                cumulateErrors(distractorErrorList, createErrorList("Item [" + itemId + "]: "
                        + se.getMessage()));
            }
        }
        cumulateErrors(distractorErrorList, validateDistractorRationaleExisted(itemId,
                distractorRationaleCount));
        return distractorErrorList;
    }

    private List validateDistractorRationaleExisted(String itemId, int distractorRationaleCount) {
        if (distractorRationaleCount == 0) {
            return createErrorList("Item [" + itemId
                    + "]: Must include DistractorRationale element in Sample item.");
        } else {
            return null;
        }
    }

    private List validateDisplayMessageExistedInDistractorRationale(String itemId,
            Element distractorRationale) {
        if (BuilderUtils.extractAttributeMandatory(distractorRationale,
                XMLConstants.DISPLAY_MESSAGE).equals("")) {
            return createErrorList("Item ["
                    + itemId
                    + "]: Must include a DistractorRationale element with non-null DisplayMessage in Sample item");
        } else {
            return null;
        }
    }

}