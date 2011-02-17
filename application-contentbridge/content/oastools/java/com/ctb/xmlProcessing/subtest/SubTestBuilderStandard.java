/*
 * Created on Aug 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.subtest;

import java.util.Iterator;

import org.jdom.Element;

import com.ctb.common.tools.OASConstants;
import com.ctb.xmlProcessing.BuilderUtils;
import com.ctb.xmlProcessing.XMLConstants;
import com.ctb.xmlProcessing.XMLUtils;
import com.ctb.xmlProcessing.assessment.AssessmentProcessor;

/**
 * @author Wen-Jin_Chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTestBuilderStandard extends SubTestBuilderTerranova
{
    public SubTestBuilderStandard() 
    {
        super();
    }

    public SubTestHolder build(Element rootElement) 
    {
        SubTestHolder aSubTestHolder = super.build( rootElement );
        String productIDString = BuilderUtils.extractAttributeMandatory( rootElement, XMLConstants.PRODUCT_ID);
        aSubTestHolder.setProductID( AssessmentProcessor.getProductID() );
        return aSubTestHolder;
    }
    
    protected void addItems(
            SubTestHolder testHolder,
            Element rootElement,
            String scoreTypeCode) 
    {
        if ( testHolder.isTDType() )
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
