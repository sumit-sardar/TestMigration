/*
 * Created on Nov 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.xml.subtest;

import org.jdom.Element;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessorUtility;
import com.ctb.contentBridge.core.publish.iknowxml.R2XmlTools;
import com.ctb.contentBridge.core.publish.report.AbstractXMLElementReport;
import com.ctb.contentBridge.core.publish.report.DeliverableUnitReport;
import com.ctb.contentBridge.core.publish.report.ReportFactory;
import com.ctb.contentBridge.core.publish.report.SchedulableUnitReport;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;
import com.ctb.contentBridge.core.publish.xml.assessment.AssessmentProcessor;


/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SchedulableUnitProcessor extends SubTestProcessor
{

    public SchedulableUnitProcessor( AbstractSubTestBuilder builder, AbstractSubTestValidater validater,
            AbstractSubTestMediaGenerator mediaGenerator, SubTestDBValidater dbvalidater,
            SubTestWriter writer) 
    {
        super( builder, validater, mediaGenerator, dbvalidater, writer );
    }
    
    protected AbstractXMLElementReport validateElement(Element element) {
        SchedulableUnitReport report = ReportFactory.createSchedulableUnitReport();

        // check if the subtest holding sample set
        report.setSample( false );
        report.setSubTestName( BuilderUtils.extractAttributeOptional(element, XMLConstants.TITLE) );
        report.setId( BuilderUtils.extractAttributeMandatory(element, XMLConstants.ID) );

        try {
            this.subTestValidater.validate(element);
        } catch (Exception e) {
            report.setException(e);
        }
        return report;
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
        if (!report.isSuccess())
            return report;

        try {
            // make copy of the element of media generation
            Element subTestElementCopy = R2XmlTools.deepCopyElement(subTestElement);

            CommandProcessorUtility.mapItemIDsInXML(subTestElementCopy, report.getItemMappings());

            SubTestHolder subTestHolder = this.subTestBuilder.build(subTestElementCopy);
            subTestHolder.isAddOn = AssessmentProcessor.getIsAddOn();
            subTestHolder.setSubTestType( false );
            ((SchedulableUnitReport) report).setId(subTestHolder.getExtTstItemSetId());
            subTestHolder.setProductID( AssessmentProcessor.getProductID());
            addDeliverableUnitIDs( subTestHolder, report.getSubReports( DeliverableUnitReport.class ) );
            this.subTestValidater.validate(subTestElementCopy);
            this.subTestDBValidater.validate(subTestHolder);
            subTestHolder.setMedia( null );
            ((SchedulableUnitReport) report)
                    .setItemSetIds(this.subTestWriter.write(subTestHolder));
            report.setSuccess(true);
        } catch (Exception e) {
            report.setSuccess(false);
            report.setException(e);
            logger.error("Subtest Processing failed", e);
        }

        return report;
    }
    
    protected void addDeliverableUnitIDs( SubTestHolder subTestHolder, List subReports )
    {
        for ( int i = 0; i < subReports.size(); i++ )
        {
            DeliverableUnitReport report = ( DeliverableUnitReport )subReports.get( i );
            subTestHolder.childSubTestIds.add( report.getItemSetId() );
        }
    }
}
