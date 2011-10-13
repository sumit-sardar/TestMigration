package com.ctb.xmlProcessing.assessment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.reporting.AbstractXMLElementReport;
import com.ctb.reporting.AssessmentProcessorReport;
import com.ctb.reporting.ReportFactory;
import com.ctb.reporting.SubTestProcessorReport;
import com.ctb.xmlProcessing.AbstractXMLElementProcessor;
import com.ctb.xmlProcessing.BuilderUtils;
import com.ctb.xmlProcessing.XMLConstants;
import com.ctb.xmlProcessing.XMLElementValidater;

/**
 * @author wmli
 */
public class AssessmentProcessor extends AbstractXMLElementProcessor {
    private static Logger logger = Logger.getLogger(AssessmentProcessor.class);

    private final AssessmentBuilder assessmentBuilder;
    private final XMLElementValidater assessmentValidater;
    private final AssessmentWriterOAS assessmentWriter;
    private static ThreadLocal _current = new ThreadLocal();
    private String extTstItemSetId; //FOR Content Download 

    public static Long getProductID() 
    {	
        HashMap theMap = ( HashMap )_current.get();
        return (Long) theMap.get( XMLConstants.PRODUCT_ID );
    }
    
    public static Boolean getIsAddOn()
    {
        HashMap theMap = ( HashMap )_current.get();
        return (Boolean) theMap.get( "AddOn" );
    }
    
    public static String getCommodityCode()
    {
        HashMap theMap = ( HashMap )_current.get();
        return ( String ) theMap.get( "CommodityCode" );
    }
    
    public static void setThreadData(HashMap theMap) {
        _current.set( theMap );
    }
	// FOR Content Download 
    public String getExtTstItemSetId(){
    	return extTstItemSetId;
    }
    
    public AssessmentProcessor(AssessmentBuilder builder, XMLElementValidater validater,
            AssessmentWriterOAS writer) {
        this.assessmentBuilder = builder;
        this.assessmentValidater = validater;
        this.assessmentWriter = writer;

    }

    protected AbstractXMLElementReport validateElement(Element element) 
    {
        String addOnString = BuilderUtils.extractAttributeOptional( element, "ParallelForms", "no");
        Boolean addOn = new Boolean( addOnString.equals( "yes" ));
        String productIDString = BuilderUtils.extractAttributeMandatory( element, XMLConstants.PRODUCT_ID);
        Long productId = Long.valueOf( productIDString );
        HashMap theMap = new HashMap();
        theMap.put( "AddOn", addOn );
        theMap.put( XMLConstants.PRODUCT_ID, productId );
        theMap.put( "CommodityCode", BuilderUtils.extractAttributeOptional( element, "CommodityCode" ) );
        setThreadData( theMap );
        AssessmentProcessorReport report = ReportFactory.createAssessmentReport();
        report.setId(BuilderUtils.extractAttributeMandatory(element, XMLConstants.ID));
        try {
            assessmentValidater.validate(element);
        } catch (Exception e) {
            report.setException(e);
        }

        return report;
    }

    protected AbstractXMLElementReport processCurrentElement(Element assessmentElement,
            AbstractXMLElementReport report, List subReports) {

        report.addSubReport(subReports);
        if (!report.isSuccess())
            return report;

        try {
            AssessmentHolder assessmentHolder = this.assessmentBuilder.build(assessmentElement);
            assessmentHolder.isAddOn = getIsAddOn();
            assessmentHolder.setCommodityCode(getCommodityCode());
            captureRelationship(subReports, assessmentHolder);
            this.extTstItemSetId = assessmentHolder.getExtTstItemSetId(); //FOR Content Download 
            this.assessmentWriter.writeAssessment(assessmentHolder);
            report.setSuccess(true);
        } catch (Exception e) {
            logger.error("Assessment Processing failed.", e);
            report.setException(e);
        }
        return report;
    }

    protected void captureRelationship(List subReports, AssessmentHolder assessmentHolder) {
        for (Iterator iter = subReports.iterator(); iter.hasNext();) {
            SubTestProcessorReport subTestProcessorReport = (SubTestProcessorReport) iter.next();

            assessmentHolder.addSubTestId(subTestProcessorReport.getItemSetIds());
        }
    }
}