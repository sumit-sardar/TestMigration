package com.ctb.cprocessor;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.hibernate.HibernateSession;
import com.ctb.xmlProcessing.item.ItemProcessor;
import com.ctb.reporting.*;

public class CommandProcessorLoadItems implements CommandProcessor {
    private static Logger logger =
        Logger.getLogger(CommandProcessorLoadItems.class);
    private final ItemProcessor itemProcessor;
    private final Element element;
    private final Connection targetConnection;

    public CommandProcessorLoadItems(
        Element rootElement,
        ItemProcessor processor,
        Connection connection) {
        this.element = rootElement;
        this.itemProcessor = processor;
        this.targetConnection = connection;
    }

    public Report process() {
        Element[] items = CommandProcessorUtility.getItems(this.element);
        return processItems(items);
    }

    private ItemSetReport processItems(Element[] items) {
        ItemSetReport report = ReportFactory.createItemSetReport(true);
        for (int i = 0; i < items.length; i++) {
            report.addSubReport(processItem(items[i]));
        }
        if (!report.isSuccess())
            report.setException(new Exception("Some item(s) failed."));
        return report;
    }

    private AbstractItemReport processItem(Element itemElement) {
        try {
            this.itemProcessor.process(itemElement);
			HibernateSession.currentSession().flush();
            targetConnection.commit();
        } catch (Exception e) {
            CommandProcessorUtility.handleItemException(e, logger);
        } 
 //       finally 
        {
            return ItemProcessorReport.getCurrentReport();
        }
    }

}
