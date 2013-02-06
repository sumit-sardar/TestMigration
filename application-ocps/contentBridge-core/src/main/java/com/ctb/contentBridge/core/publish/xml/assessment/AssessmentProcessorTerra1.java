/*
 * Created on Aug 20, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.xml.assessment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetSampleSetRecord;
import com.ctb.contentBridge.core.publish.report.AbstractXMLElementReport;
import com.ctb.contentBridge.core.publish.report.SubTestProcessorReport;
import com.ctb.contentBridge.core.publish.xml.XMLElementValidater;


/**
 * @author wmli TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class AssessmentProcessorTerra1 extends AssessmentProcessor {

    public AssessmentProcessorTerra1(AssessmentBuilder builder, XMLElementValidater validater,
            AssessmentWriterOAS writer) {
        super(builder, validater, writer);
    }

    protected AbstractXMLElementReport validateElement(Element element) {
        return super.validateElement(element);
    }

    protected AbstractXMLElementReport processCurrentElement(Element element,
            AbstractXMLElementReport report, List childReports) {
        return super.processCurrentElement(element, report, childReports);
    }

    protected void captureRelationship(List subReports, AssessmentHolder assessmentHolder) {
        List itemSetSubtestReprots = new ArrayList();
        List sampleSetSubtestReports = new ArrayList();

        for (Iterator iter = subReports.iterator(); iter.hasNext();) {
            SubTestProcessorReport subTestProcessorReport = (SubTestProcessorReport) iter.next();
            assessmentHolder.addSubTestId(subTestProcessorReport.getItemSetIds());

            if (subTestProcessorReport.isSample()) {
                sampleSetSubtestReports.add(subTestProcessorReport);
            } else {
                itemSetSubtestReprots.add(subTestProcessorReport);
            }
        }

        for (Iterator iter = itemSetSubtestReprots.iterator(); iter.hasNext();) {
            SubTestProcessorReport itemSetSubtestReport = (SubTestProcessorReport) iter.next();

            for (Iterator iterator = sampleSetSubtestReports.iterator(); iterator.hasNext();) {
                SubTestProcessorReport sampleSetSubtestReport = (SubTestProcessorReport) iterator
                        .next();

                if (itemSetSubtestReport.getId().equals(sampleSetSubtestReport.getId())) {
                    ItemSetSampleSetRecord record = new ItemSetSampleSetRecord();

                    record.setItemSetId(itemSetSubtestReport.getItemSetId());
                    record.setSampleItemSetId(sampleSetSubtestReport.getItemSetId());

                    record.setSubtestLevel(itemSetSubtestReport.getSubTestLevel());
                    record.setSubtestName(itemSetSubtestReport.getSubTestName());

                    record.setTestType(assessmentHolder.getFrameworkCode() + " " + assessmentHolder.getProductDisplayName());

                    assessmentHolder.addItemSampleSet(record);

                }
            }
        }
    }

}