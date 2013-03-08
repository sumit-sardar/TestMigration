package com.ctb.contentBridge.core.publish.xml.assessment;

import java.util.List;

import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;



/**
 * @author wmli
 */
public class AssessmentBuilder implements XMLConstants {

    public AssessmentHolder build(Element rootElement) {
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
        String itemSetDescription;
        String itemSetForm;
        String commodityCode;
        Long productId = new Long(0);

        if (!rootElement
            .getName()
            .equals(XMLConstants.ELEMENT_NAME_ASSESSMENT)) {
            throw new SystemException(
                "Invalid root element: " + rootElement.getName());
        }
        frameworkCode =
            BuilderUtils.extractAttributeOptional(rootElement, FRAMEWORK_CODE);
        productDisplayName =
            BuilderUtils.extractAttributeOptional(rootElement, PRODUCT_NAME);

        extTstItemSetId =
            BuilderUtils.extractAttributeMandatory(rootElement, ID);
        testName = BuilderUtils.extractAttributeMandatory(rootElement, TITLE);
        version = BuilderUtils.extractAttributeOptional(rootElement, VERSION);
        grade = BuilderUtils.extractAttributeOptional(rootElement, GRADE);
        
        //itemSetLevel = grade;
        itemSetLevel = BuilderUtils.extractAttributeOptional(rootElement, LEVEL);
        commodityCode = BuilderUtils.extractAttributeOptional(rootElement, "CommodityCode");
        String timeLimitString =
            BuilderUtils.extractAttributeOptional(rootElement, TIME_LIMIT);
        timeLimit =
            (timeLimitString.equals(""))
                ? 0
                : Integer.parseInt(timeLimitString) * 60;
        if ( timeLimit == 0 )
            timeLimit = addChildTimeLimit( rootElement ) * 60;
        breakTime = 0;
        itemSetDisplayName = testName;
        // todo - mws - test this with Assessment description child elements
        try {
            itemSetDescription =
                BuilderUtils.extractChildElementValue(rootElement, DESCRIPTION);
    //        itemSetDescription = AbstractSubTestBuilder.replaceAll( itemSetDescription, "'", "&apos;" );
        } catch (SystemException se) {
            itemSetDescription = "";
          //      BuilderUtils.extractAttributeMandatory(rootElement, DESCRIPTION);
        }


        itemSetForm = BuilderUtils.extractAttributeOptional(rootElement, FORM);

        AssessmentHolder assessmentHolder =
            new AssessmentHolder(
                productId,
                frameworkCode,
                productDisplayName,
                extTstItemSetId,
                testName,
                version,
                itemSetLevel,
                grade,
                timeLimit,
                breakTime,
                itemSetDisplayName,
                itemSetDescription,
                itemSetForm,
                commodityCode);

        return assessmentHolder;
    }
    
    public int addChildTimeLimit( Element assessment )
    {
        int timeLimit = 0;
        List children = assessment.getChildren( "SchedulableUnit" );
        for ( int i = 0; i < children.size(); i++ )
        {
            Element SchedulableUnit = ( Element )children.get( i );
            String time = SchedulableUnit.getAttributeValue( "TimeLimit" );
            if ( time != null )
                timeLimit += Integer.valueOf( time ).intValue();
        }
        return timeLimit;
    }
}
