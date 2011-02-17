/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.subtest;

import java.util.Iterator;

import org.jdom.Element;

import com.ctb.common.tools.OASConstants;
import com.ctb.xmlProcessing.BuilderUtils;
import com.ctb.xmlProcessing.XMLConstants;
import com.ctb.xmlProcessing.XMLUtils;

/**
 * @author wmli TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class SubTestBuilderIncremental extends AbstractSubTestBuilder {
    
    public SubTestHolder build(Element rootElement) {
        SubTestHolder subtest = super.build(rootElement);
        
        String itemSetLevel = BuilderUtils.extractAttributeOptional(rootElement, LEVEL);
        subtest.setItemSetLevel(itemSetLevel);
        
        return subtest;
    }
    protected void addItems(SubTestHolder testHolder, Element rootElement, String scoreTypeCode) {
        Iterator items = XMLUtils.getItemSubElementsInItemSets(rootElement);
        while (items.hasNext()) {
            Element itemElement = (Element) items.next();
            String itemID = BuilderUtils.extractAttributeMandatory(itemElement, ID);
            String objectiveID = BuilderUtils.extractAttributeMandatory(itemElement, OBJECTIVE_ID);
            String itemType = BuilderUtils.extractAttributeOptional(itemElement, ITEM_TYPE,
                    OASConstants.ITEM_TYPE_SR);
            String suppressed = BuilderUtils.extractAttributeMandatory(itemElement, SUPPRESS_SCORE);
            String fieldTest = BuilderUtils.extractAttributeOptional( itemElement, "FieldTest");
            
            if (suppressed.equalsIgnoreCase(XMLConstants.YES)) {
                testHolder.addItem(new SubTestHolder.TestItem(itemID, fieldTest, suppressed));
            } else {
                testHolder.addItem(new SubTestHolder.TestItem(itemID, "*", fieldTest, suppressed));
            }
        }
 /*       Iterator sampleitems = XMLUtils.getItemSubElementsInSampleSets(rootElement);
        while (sampleitems.hasNext()) {
            Element itemElement = (Element) sampleitems.next();
            String itemID = BuilderUtils.extractAttributeMandatory(itemElement, ID);
            testHolder.addItem(new SubTestHolder.TestItem(itemID, "No", "Yes"));
        } */
    }
}