package com.ctb.xmlProcessing.itemset;

import org.jdom.Element;

import com.ctb.xmlProcessing.XMLConstants;

public class ItemSetValidaterTabe extends ItemSetValidater {
    public void validate(Element itemSetElement) {
        cumulateErrors(errorList, validateItemsSuppressScore(itemSetElement, XMLConstants.NO));
    }
}