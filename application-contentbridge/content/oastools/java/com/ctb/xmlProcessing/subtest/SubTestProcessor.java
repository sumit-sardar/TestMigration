package com.ctb.xmlProcessing.subtest;

import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.common.tools.oasmedia.OASMedia;
import com.ctb.cprocessor.CommandProcessorUtility;
import com.ctb.reporting.AbstractXMLElementReport;
import com.ctb.reporting.ReportFactory;
import com.ctb.reporting.SubTestProcessorReport;
import com.ctb.util.iknowxml.R2XmlTools;
import com.ctb.xmlProcessing.AbstractXMLElementProcessor;
import com.ctb.xmlProcessing.BuilderUtils;
import com.ctb.xmlProcessing.XMLConstants;

/**
 * @author wmli
 */
public class SubTestProcessor extends AbstractXMLElementProcessor {

    public Logger logger = Logger.getLogger(SubTestProcessor.class);

    public final AbstractSubTestBuilder subTestBuilder;
    public final AbstractSubTestValidater subTestValidater;
    public final AbstractSubTestMediaGenerator mediaGenerator;
    public final SubTestWriter subTestWriter;
    public final SubTestDBValidater subTestDBValidater;

    public SubTestProcessor(AbstractSubTestBuilder builder, AbstractSubTestValidater validater,
            AbstractSubTestMediaGenerator mediaGenerator, SubTestDBValidater dbvalidater,
            SubTestWriter writer) {
        this.subTestBuilder = builder;
        this.subTestValidater = validater;
        this.mediaGenerator = mediaGenerator;
        this.subTestDBValidater = dbvalidater;
        this.subTestWriter = writer;
    }

    protected AbstractXMLElementReport validateElement(Element element) {
        SubTestProcessorReport report = ReportFactory.createSubTestReport();

        // check if the subtest holding sample set
        report.setSample( BuilderUtils.extractAttributeOptional(element, "Type").equals( "sample" ) );
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

    /**
     * Process the current element
     * 
     * @param subTestElement the subtest element
     * @param List of all the ItemSetReport
     */
    protected AbstractXMLElementReport processCurrentElement(Element subTestElement,
            AbstractXMLElementReport report, List subReports) {

        report.addSubReport(subReports);
        if (!report.isSuccess())
            return report;

        try {
            // make copy of the element of media generation
            Element subTestElementCopy = R2XmlTools.deepCopyElement(subTestElement);

            CommandProcessorUtility.mapItemIDsInXML(subTestElementCopy, report.getItemMappings());

            SubTestHolder subTestHolder = this.subTestBuilder.build(subTestElementCopy);
            ((SubTestProcessorReport) report).setId(subTestHolder.getExtTstItemSetId());

            this.subTestValidater.validate(subTestElementCopy);
            this.subTestDBValidater.validate(subTestHolder);

            // generate the media.
            // subTestHolder.setMedia(mediaGenerator.generate(subTestElementCopy));
            subTestHolder.setMedia( new SubTestMedia());
            ((SubTestProcessorReport) report)
                    .setItemSetIds(this.subTestWriter.write(subTestHolder));

            report.setSuccess(true);
        } catch (Exception e) {
            report.setSuccess(false);
            report.setException(e);
            logger.error("Subtest Processing failed", e);
        }

        return report;
    }
}