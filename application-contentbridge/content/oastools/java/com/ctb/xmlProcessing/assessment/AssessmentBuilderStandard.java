/*
 * Created on Aug 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.assessment;

import org.jdom.Element;

import com.ctb.xmlProcessing.BuilderUtils;
import com.ctb.xmlProcessing.XMLConstants;

/**
 * @author Wen-Jin_Chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AssessmentBuilderStandard extends AssessmentBuilder implements XMLConstants
{
      
    public AssessmentBuilderStandard() 
    {
        super();
    }

    public AssessmentHolder build(Element rootElement) 
    {
        AssessmentHolder theAssessmentHolder = super.build( rootElement );
        String productIDString = BuilderUtils.extractAttributeMandatory( rootElement, PRODUCT_ID);
        Long productId = Long.valueOf( productIDString );
        theAssessmentHolder.setProductID( productId );
        return theAssessmentHolder;
    }
}
