package com.ctb.contentBridge.core.publish.xml.item;

import org.jdom.Element;
import org.apache.log4j.Logger;

import com.ctb.contentBridge.core.publish.report.AbstractXMLElementReport;
import com.ctb.contentBridge.core.publish.report.ItemProcessorReport;
import com.ctb.contentBridge.core.publish.report.ReportFactory;

public class ItemProcessorReportingDecorator implements ItemProcessor {

    private final Logger logger =
        Logger.getLogger(ItemProcessorReportingDecorator.class);
    private final ItemProcessor processor;

    public ItemProcessorReportingDecorator(ItemProcessor concreteItemProcessor) {
        this.processor = concreteItemProcessor;
    }

    public AbstractXMLElementReport process(Element itemElement) {
        ItemProcessorReport report = ReportFactory.createItemProcessorReport();
        
        try {
            String id = setItemIdInReport(itemElement, report);
			this.processor.process(itemElement);
            report.setSuccess(true);
            logger.info(
                "Item [" + id + "] " + report.getOperation() + " successful");
            return report;
        } catch (Exception e) {
            return handleException(report, e);
        }
    }

    private String setItemIdInReport(
        Element itemElement,
        ItemProcessorReport report) {
        String id = itemElement.getAttributeValue("ID");
        report.setID(id);
        logger.debug("Processing Item [" + id + "] ...");
        return id;
    }

    private ItemProcessorReport handleException(ItemProcessorReport report, Exception e) {
        report.setException(e);
        report.setSuccess(false);
        logger.error("Item Processing failed", e);
        return report;
    }
}
