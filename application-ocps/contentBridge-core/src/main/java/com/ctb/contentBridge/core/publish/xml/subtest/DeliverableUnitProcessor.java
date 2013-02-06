/*
 * Created on Nov 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.xml.subtest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessorUtility;
import com.ctb.contentBridge.core.publish.iknowxml.R2XmlTools;
import com.ctb.contentBridge.core.publish.report.AbstractXMLElementReport;
import com.ctb.contentBridge.core.publish.report.DeliverableUnitReport;
import com.ctb.contentBridge.core.publish.report.ReportFactory;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;
import com.ctb.contentBridge.core.publish.xml.XMLElementProcessor;
import com.ctb.contentBridge.core.publish.xml.XMLElementProcessors;
import com.ctb.contentBridge.core.publish.xml.assessment.AssessmentProcessor;
import com.ctb.contentBridge.core.publish.xml.itemset.TestItemProcessor;


/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeliverableUnitProcessor extends SubTestProcessor
{
	public ArrayList unicodeList;
	private Connection connection;
	private static ThreadLocal _current = new ThreadLocal();
	
    public static Long getContentSize() {
        return ( Long ) _current.get();
    }
    public static void setContentSize( Long size ) {
        _current.set( size );
    }
    
    public static void addSizeToContent( long size )
    {
        Long sizeLong = getContentSize();
        if ( sizeLong == null )
            setContentSize( new Long( size ) );
        else
            setContentSize( new Long( size + sizeLong.longValue() ) );
    }
    
    
    public DeliverableUnitProcessor( AbstractSubTestBuilder builder, AbstractSubTestValidater validater,
            AbstractSubTestMediaGenerator mediaGenerator, SubTestDBValidater dbvalidater,
            SubTestWriter writer, ArrayList unicodeList, Connection conn) 
    {
        super( builder, validater, mediaGenerator, dbvalidater, writer );
        this.unicodeList = unicodeList;
        this.connection = conn;
    }
    
    protected AbstractXMLElementReport validateElement(Element element) {
        DeliverableUnitReport report = ReportFactory.createDeliverableUnitReport( true );

        report.setSubTestLevel( BuilderUtils.extractAttributeOptional(element, XMLConstants.LEVEL) );
        report.setSubTestName( BuilderUtils.extractAttributeOptional(element, XMLConstants.TITLE) );
        report.setId( BuilderUtils.extractAttributeMandatory(element, XMLConstants.ID) );

        try {
            this.subTestValidater.validate(element);
        } catch (Exception e) {
            report.setException(e);
        }
        return report;
    }
    
    public AbstractXMLElementReport process(Element element) 
    {
        AbstractXMLElementReport report = validateElement(element);
        if (!report.isSuccess())
            return report;

        List childReports = new ArrayList();
        List children = element.getChildren();

        for (Iterator iter = children.iterator(); iter.hasNext();) 
        {
            Element child = (Element) iter.next();
            XMLElementProcessor childProcessor =
                XMLElementProcessors.getProcessor(child.getName());
            if (childProcessor != null) 
            {
                if ( childProcessor instanceof TestItemProcessor )
                {
                    TestItemProcessor aTestItemProcessor = ( TestItemProcessor )childProcessor;
                    List childList = aTestItemProcessor.processItem( child );
                    childReports.add( childList );
                }
                else
                    childReports.add(childProcessor.process(child));
            }
        }

        return processCurrentElement(element, report, childReports);
    }
    
    /**
     * Process the current element
     * 
     * @param subTestElement the subtest element
     * @param List of all the ItemSetReport
     */
    protected AbstractXMLElementReport processCurrentElement(Element subTestElement,
            AbstractXMLElementReport report, List subReports) 
    {
        report.addSubReport(subReports);
        Element publishedSubtest = null;
        System.out.println("Inside DeliverableUnitProcessor.processCurrentElement");
        System.out.println("report.isSuccess():	" + report.isSuccess());
        if (!report.isSuccess())
            return report;

        try {
            // make copy of the element of media generation
            Element subTestElementCopy = R2XmlTools.deepCopyElement(subTestElement);

            CommandProcessorUtility.mapItemIDsInXML(subTestElementCopy, report.getItemMappings());

            System.out.println("Build SubTestHolder...");
            SubTestHolder subTestHolder = this.subTestBuilder.build(subTestElementCopy);
            subTestHolder.isAddOn = AssessmentProcessor.getIsAddOn();
            subTestHolder.content_size = getContentSize();
            subTestHolder.setSubTestType( true );
            ((DeliverableUnitReport) report).setId(subTestHolder.getExtTstItemSetId());
            subTestHolder.setProductID( AssessmentProcessor.getProductID());
            System.out.println("Validate SubTestHolder...");
            this.subTestValidater.validate(subTestElementCopy);
            this.subTestDBValidater.validate(subTestHolder);
                        
            subTestHolder.setMedia( null );
            ((DeliverableUnitReport)report).setItemSetIds(this.subTestWriter.write(subTestHolder));
            
            SubtestPublisher subtest = new SubtestPublisher();
            System.out.println("Call publishSubtest...");
            publishedSubtest = subtest.publishSubtest(subTestElement, ((DeliverableUnitReport)report).getItemSetId(), unicodeList, connection);
            setContentSize( new Long( 0 ));		
            report.setSuccess(true);
        } catch (Exception e) {
            report.setSuccess(false);
            report.setException(e);
            logger.error("Subtest Processing failed", e);
        }

        return report;
    }

}
