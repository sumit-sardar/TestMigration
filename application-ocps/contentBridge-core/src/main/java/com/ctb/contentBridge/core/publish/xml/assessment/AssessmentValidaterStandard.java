/*
 * Created on Aug 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.xml.assessment;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;


/**
 * @author Wen-Jin_Chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AssessmentValidaterStandard extends AssessmentValidaterTerranova
{
    public static String[] testAttributeNames = { XMLConstants.FRAMEWORK_CODE,  XMLConstants.PRODUCT_ID};
    public AssessmentValidaterStandard() 
    {
        super();
    }
    
    public void validate(Element assessmentElement) 
    {
        List errorList = new ArrayList();
        cumulateErrors(errorList, validateNoGradeAndLevel( assessmentElement ));
        if ( errorList.size() > 0) {
            throw new SystemException("\n" + StringUtils.join(errorList.iterator(), "\n"));
        }
    }

    public List validateNoGradeAndLevel(Element element) {
        if ( element.getAttributeValue( XMLConstants.GRADE ) == null 
                && element.getAttributeValue( XMLConstants.LEVEL ) == null )
        {
            return createErrorList("Grade or Level need to be present.");
        }
        return null;
    }
}
