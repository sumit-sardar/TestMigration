package com.ctb.xmlProcessing;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import com.ctb.common.tools.SystemException;

/**
 * @author wmli
 */
public class ValidaterUtils {
    private static final String ITEM_ELEMENT_XPATH = ".//Item";
    private static final String TEST_ITEM_ELEMENT_XPATH = ".//TestItem//Item";
    
    public static List getItemElements(Element subTestElement) 
    {
        try {
            XPath itemXPath = null;
            if ( subTestElement.getName().equals( XMLConstants.ELEMENT_NAME_ITEMSET ))
                itemXPath = XPath.newInstance(ITEM_ELEMENT_XPATH);
            else if ( subTestElement.getName().equals( XMLConstants.ELEMENT_NAME_TD ))
                itemXPath = XPath.newInstance( TEST_ITEM_ELEMENT_XPATH );
            else 
                itemXPath = XPath.newInstance(ITEM_ELEMENT_XPATH);
            return itemXPath.selectNodes(subTestElement);
        } catch (JDOMException je) {
            throw new SystemException(je.getMessage(), je);
        }
    }

    public static Set getUniqueItemType(Element element) {
        Set itemTypeUniqueSet = new HashSet();

        for (Iterator iter = getItemElements(element).iterator();
            iter.hasNext();
            ) {
            Element itemElement = (Element) iter.next();

            if (BuilderUtils
                .extractAttributeOptional(itemElement, XMLConstants.SAMPLE)
                .equals(XMLConstants.YES)) {
                itemTypeUniqueSet.add(XMLConstants.SAMPLE);
            } else {
                itemTypeUniqueSet.add(
                    BuilderUtils.extractAttributeOptional(
                        itemElement,
                        XMLConstants.ITEM_TYPE));

            }
        }
        return itemTypeUniqueSet;
    }

}
