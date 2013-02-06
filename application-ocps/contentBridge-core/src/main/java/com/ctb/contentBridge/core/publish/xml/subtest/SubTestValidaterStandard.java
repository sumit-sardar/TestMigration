/*
 * Created on Aug 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.xml.subtest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;


/**
 * @author Wen-Jin_Chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTestValidaterStandard extends SubTestValidaterTerranova 
{
    public SubTestValidaterStandard() 
    {
        super();
    }

    public void validate(Element element) 
    {
        List errorList = new ArrayList();
        if ( element.getName().equals( XMLConstants.ELEMENT_NAME_TD ))
        {
	        cumulateErrors(errorList, validateUniqueChild(element));
	        cumulateErrors(errorList, validateNoDuplicatedItems(element));
        }
        cumulateErrors(errorList, validateNoGradeAndLevel(element));
        if (errorList.size() > 0) {
            throw new BusinessException("\n" + StringUtils.join(errorList.iterator(), "\n"));
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
