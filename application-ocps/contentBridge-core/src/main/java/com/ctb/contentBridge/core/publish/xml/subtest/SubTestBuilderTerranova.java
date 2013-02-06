package com.ctb.contentBridge.core.publish.xml.subtest;

import java.util.Iterator;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.tools.OASConstants;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.XMLUtils;


/**
 * @author wmli
 */
public class SubTestBuilderTerranova extends AbstractSubTestBuilder {

    protected void addItems(
        SubTestHolder testHolder,
        Element rootElement,
        String scoreTypeCode) {
        Iterator items = XMLUtils.getItemSubElementsInItemSets(rootElement);

        while (items.hasNext()) {
            Element itemElement = (Element) items.next();
            String itemID =
                BuilderUtils.extractAttributeMandatory(itemElement, ID);
            String objectiveID =
                BuilderUtils.extractAttributeMandatory(
                    itemElement,
                    OBJECTIVE_ID);
            String itemType =
                BuilderUtils.extractAttributeOptional(
                    itemElement,
                    ITEM_TYPE,
                    OASConstants.ITEM_TYPE_SR);

            testHolder.addItem(new SubTestHolder.TestItem(itemID));

        }
    }

}
