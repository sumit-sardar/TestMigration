package com.ctb.contentBridge.core.publish.xml.subtest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.ValidaterUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;
import com.ctb.contentBridge.core.publish.xml.itemset.ItemSetValidaterTerranovaResearch;



/**
 * @author wmli
 */
public class SubTestValidaterTerranova extends AbstractSubTestValidater {
    private Object XMLConstant;
    public static String[] attributeNames = {XMLConstants.ID,
            XMLConstants.TITLE};

    public void validate(Element element) {
        List errorList = new ArrayList();
        cumulateErrors(errorList, validateRequiredField(element, attributeNames));
        cumulateErrors(errorList, validateUniqueChild(element));
        cumulateErrors(errorList, validateNoDuplicatedItems(element));
        cumulateErrors(errorList, validateItemTypeConsistency(element));
        if (errorList.size() > 0) {
            throw new SystemException("\n" + StringUtils.join(errorList.iterator(), "\n"));
        }
    }

    private List validateItemTypeConsistency(Element element) {
        if (ValidaterUtils.getItemElements(element).size() == 0)
            return null;
        Set itemTypeUniqueSet = ValidaterUtils.getUniqueItemType(element);
        List itemTypeErrorList = new ArrayList();
        cumulateErrors(itemTypeErrorList, validateNoMixedItemTypeWithSampleOrRQ(itemTypeUniqueSet));
        cumulateErrors(itemTypeErrorList, validateSubTestConstraints(element, itemTypeUniqueSet));
        return itemTypeErrorList;
    }

    private List validateNoMixedItemTypeWithSampleOrRQ(Set itemTypeUniqueSet) {
        if (itemTypeUniqueSet.contains(XMLConstants.ITEM_TYPE_RQ)
                || itemTypeUniqueSet.contains(XMLConstants.SAMPLE)) {
            if (itemTypeUniqueSet.size() > 1) {
                return createErrorList("All the items in the subtest must be sample or research items");
            }
        }
        return null;
    }

    private List validateSubTestConstraints(Element element, Set itemTypeUniqueSet) {
        List subTestErrorList = new ArrayList();
        if (BuilderUtils.extractAttributeOptional(element, XMLConstants.CONTENT_AREA)
                .equalsIgnoreCase(XMLConstants.CONTENT_AREA_RESEARCH)) {
            // validate that only RQ type in research subtest
            cumulateErrors(subTestErrorList, validateOnlyOneItemTypeInSubTest(itemTypeUniqueSet,
                    XMLConstants.ITEM_TYPE_RQ,
                    "All items in research SubTest should have ItemType set to [RQ]."));

            ItemSetValidaterTerranovaResearch isv = new ItemSetValidaterTerranovaResearch();
            isv.validate(element.getChild(XMLConstants.ELEMENT_NAME_ITEMSET));
            cumulateErrors(subTestErrorList, isv.getErrorList());

        } 
        return subTestErrorList;
    }

    private List validateOnlyOneItemTypeInSubTest(Set itemTypeUniqueSet, String itemType,
            String message) {
        if (!itemTypeUniqueSet.contains(itemType)) {
            return createErrorList(message);
        } else {
            return null;
        }
    }
}