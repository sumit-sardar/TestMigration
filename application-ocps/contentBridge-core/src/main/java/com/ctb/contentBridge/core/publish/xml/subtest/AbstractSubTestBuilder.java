package com.ctb.contentBridge.core.publish.xml.subtest;




import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;


public abstract class AbstractSubTestBuilder implements XMLConstants {
    public static final String ITEM_TYPE = "ItemType";
    public static final String OBJECTIVE_ID = "ObjectiveID";
    public static final String SUPPRESS_SCORE = "SuppressScore";

    public SubTestHolder build(Element rootElement) {
        String frameworkCode;
        String productDisplayName;
        String extTstItemSetId;
        String testName;
        String version;
        String itemSetLevel;
        String grade;
        int timeLimit; // in seconds
        int breakTime;
        String itemSetDisplayName;
        Long startItemNumber = new Long(1);
        String itemSetDescription;
        String itemSetForm;
        String scoreLookupId;
        String scoreTypeCode;
        String contentArea;
        String forwardOnly = "F";
        String adaptive = "F";

        boolean isTD = rootElement.getName().equals( ELEMENT_NAME_TD );
        if (!rootElement.getName().equals( ELEMENT_NAME_TS ) && !isTD ) {
            throw new SystemException("Invalid root element: " + rootElement.getName());
        }
        frameworkCode = BuilderUtils.extractAttributeOptional(rootElement, FRAMEWORK_CODE);
        productDisplayName = BuilderUtils.extractAttributeOptional(rootElement, PRODUCT_NAME);
        contentArea = BuilderUtils.extractAttributeOptional(rootElement, CONTENT_AREA);

        extTstItemSetId = BuilderUtils.extractAttributeMandatory(rootElement, ID);
        testName = BuilderUtils.extractAttributeMandatory(rootElement, TITLE);
        version = BuilderUtils.extractAttributeOptional(rootElement, VERSION);
        
        if (BuilderUtils.extractAttributeOptional(rootElement, STARTNUMBER).length()> 0)
        {startItemNumber = new Long(BuilderUtils.extractAttributeOptional(rootElement, STARTNUMBER));}
        
        grade = BuilderUtils.extractAttributeOptional(rootElement, GRADE);
        itemSetLevel = BuilderUtils.extractAttributeOptional(rootElement, LEVEL );
        if ( isTD ) {
        	forwardOnly = BuilderUtils.extractAttributeMandatory(rootElement, FORWARD_ONLY);
        	adaptive = BuilderUtils.extractAttributeMandatory(rootElement, ADAPTIVE);
        	timeLimit = BuilderUtils.extractIntegerAttributeMandatory(rootElement, TIME_LIMIT) * 60;
        }
        else
        {
            String TSTimeLimit = BuilderUtils.extractAttributeOptional(rootElement, TIME_LIMIT);
            if ( TSTimeLimit.length() == 0 )
                timeLimit = 0;
            else
                timeLimit = Integer.valueOf( TSTimeLimit ).intValue() * 60;
        }
        breakTime = 0;
        itemSetDisplayName = testName;
        try {
            itemSetDescription = BuilderUtils.extractChildElementValue(rootElement, DESCRIPTION);
        } catch (SystemException se) {
            itemSetDescription = "";
            se.printStackTrace();
        }
        itemSetForm = BuilderUtils.extractAttributeOptional(rootElement, FORM);
        scoreLookupId = BuilderUtils.extractAttributeOptional(rootElement, SCORE_LOOKUP_ID);
        scoreTypeCode = BuilderUtils.extractAttributeOptional(rootElement, SCORE_TYPE_CODE);
       
        SubTestHolder testHolder = new SubTestHolder(frameworkCode, productDisplayName,
                extTstItemSetId, testName, version, itemSetLevel, grade, timeLimit, breakTime,
                itemSetDisplayName, itemSetDescription, itemSetForm, scoreLookupId, scoreTypeCode, contentArea, startItemNumber,forwardOnly, adaptive);
        
        testHolder.setSample( BuilderUtils.extractAttributeOptional( rootElement, "Type").equals( "sample" ) );

        addItems(testHolder, rootElement, scoreTypeCode);

        return testHolder;
    }
    
    public static String replaceAll( String src, String toBeReplace, String replaceWith )
    {
    	String result = src;
    	int index = 0;
    	int difference = replaceWith.length();
    	while ( ( index = result.indexOf( toBeReplace, index )) >= 0 )
    	{
    		result = result.substring( 0, index ) + replaceWith + result.substring( index + toBeReplace.length() );
    		index += difference;
    	}
    	return result;
    }

    protected abstract void addItems(SubTestHolder testHolder, Element rootElement,
            String scoreTypeCode);
}