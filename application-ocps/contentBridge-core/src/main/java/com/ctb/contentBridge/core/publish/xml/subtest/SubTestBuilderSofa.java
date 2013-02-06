package com.ctb.contentBridge.core.publish.xml.subtest;

import java.util.Iterator;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.sofa.ScorableItemConfig;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;
import com.ctb.contentBridge.core.publish.xml.XMLUtils;


/**
 * @author wmli
 */
public class SubTestBuilderSofa extends AbstractSubTestBuilder {
    private ScorableItemConfig scorableItemConfig = new ScorableItemConfig();
    public SubTestBuilderSofa(ScorableItemConfig scorableItemConfig) {
        this.scorableItemConfig = scorableItemConfig;
    }

    public SubTestBuilderSofa() {
    }

    protected void addItems(
        SubTestHolder testHolder,
        Element rootElement,
        String scoreTypeCode) 
    {
        if ( rootElement.getName().equals( XMLConstants.ELEMENT_NAME_TD) )
        {
            Iterator testItems = XMLUtils.getTestItemSubElementsInItemSets(rootElement);
            while ( testItems.hasNext()) 
            {
                Element testItemElement = (Element) testItems.next();
                Element itemElement = testItemElement.getChild( XMLConstants.ELEMENT_NAME_ITEM );
                String itemID =
                    BuilderUtils.extractAttributeMandatory(itemElement, ID);
                String suppress = BuilderUtils.extractAttributeOptional(
                        testItemElement,
                        XMLConstants.SUPRESS_SCORE,
                        "No");
                String fieldTest = BuilderUtils.extractAttributeOptional(
                        testItemElement,
                        "FieldTest",
                        "No");
                testHolder.addItem(new SubTestHolder.TestItem(itemID, scoreTypeCode, fieldTest, suppress));
            }
        }
    }
}
