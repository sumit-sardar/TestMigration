/*
 * Created on Nov 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.itemset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.ctb.reporting.AbstractXMLElementReport;
import com.ctb.reporting.ItemProcessorReport;
import com.ctb.xmlProcessing.XMLConstants;
import com.ctb.xmlProcessing.XMLElementProcessor;
import com.ctb.xmlProcessing.XMLElementProcessors;
import com.ctb.xmlProcessing.item.ItemProcessor;
import com.ctb.xmlProcessing.item.ItemProcessorGeneric;

/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestItemProcessor implements ItemProcessor
{
    private static ThreadLocal _current = new ThreadLocal();
    
    public TestItemProcessor() 
    {
        super();
    }
    
    public static HashMap getCurrentItemAttributeMap() {
        return (HashMap) _current.get();
    }
    
    public static void setCurrentItemAttributeMap(HashMap report) {
        _current.set(report);
    }

    /* (non-Javadoc)
     * @see com.ctb.xmlProcessing.item.ItemProcessor#process(org.jdom.Element)
     */
    public AbstractXMLElementReport process( Element testitemElement ) 
    {
        return null;
    }
    
    public List processItem( Element testitemElement ) 
    {
        HashMap thisItemMap = new HashMap();
        thisItemMap.put( "Sample", testitemElement.getAttributeValue( "Sample" ));
        thisItemMap.put( "SuppressScore", testitemElement.getAttributeValue( "SuppressScore" ));
        thisItemMap.put( "FieldTest", testitemElement.getAttributeValue( "FieldTest" ));
        thisItemMap.put( "ScaleScore", testitemElement.getAttributeValue( "ScaleScore" ));
        setCurrentItemAttributeMap( thisItemMap );
        List childReports = new ArrayList();
        List child = testitemElement.getChildren( XMLConstants.ELEMENT_NAME_ITEM );
        
        for ( int i = 0; i < child.size(); i++ )
        {
            Element item = ( Element )child.get( i );
            XMLElementProcessor childProcessor =
                XMLElementProcessors.getProcessor( item.getName() );
            childReports.add( childProcessor.process( item ) );
        }
        return childReports;
    }
}
