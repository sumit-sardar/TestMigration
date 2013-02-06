package com.ctb.contentBridge.core.publish.cprocessor;

import java.io.File;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.hibernate.HibernateSession;
import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.MapperFactory;
import com.ctb.contentBridge.core.publish.report.HierarchyReport;
import com.ctb.contentBridge.core.publish.report.ItemProcessorReport;
import com.ctb.contentBridge.core.publish.report.ItemSetReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.report.ReportFactory;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessor;


public class CommandProcessorRoundTrip implements CommandProcessor {
    private static Logger logger =
        Logger.getLogger(CommandProcessorRoundTrip.class);
    //private File mappingFile;
    private ItemMap itemMap;
    final private ItemProcessor itemProcessor;
    final private Connection targetConnection;

    public CommandProcessorRoundTrip(
        File mappingFile,
        ItemProcessor itemProcessor,
        Connection connection) {
        this(itemProcessor, connection);
        try {
            MapperFactory.loadItemMapFromFile(mappingFile);
        } catch (Exception e) {
            throw new SystemException(
                "Cannot load mapping file: " + mappingFile.getPath());
        }
        this.itemMap = MapperFactory.getItemMap();

    }

    private CommandProcessorRoundTrip(
        ItemProcessor itemProcessor,
        Connection targetConnection) {
        this.targetConnection = targetConnection;
        this.itemProcessor = itemProcessor;

    }

    public CommandProcessorRoundTrip(
        ItemMap itemMap,
        ItemProcessor itemProcessor,
        Connection connection) {
        this(itemProcessor, connection);
        this.itemMap = itemMap;
    }

    public Report process() {
        ItemSetReport itemSetReport = ReportFactory.createItemSetReport(true);
        for (Iterator iter = this.itemMap.getAllItemIDs(); iter.hasNext();) {
            Element element = new Element("Item");
            element.setAttribute("ID", (String) iter.next());
            processSingleItem(element, itemSetReport);
        }

        HierarchyReport hierarchyReport = new HierarchyReport();
        hierarchyReport.setObjectives(MapperFactory.getObjectives());
        hierarchyReport.setItemMap(this.itemMap);
        hierarchyReport.setSuccess(true);
        hierarchyReport.addSubReport(itemSetReport);

        return hierarchyReport;
    }

    private void processSingleItem(
        Element element,
        ItemSetReport itemSetReport) {
        try {
            itemProcessor.process(element);
			HibernateSession.currentSession().flush();
            targetConnection.commit();
        } catch (Exception e) {
            try {
                targetConnection.rollback();
            } catch (SQLException e1) {
                logger.error(
                    "Failed to rollback item processing for round trip",
                    e1);
            }
            CommandProcessorUtility.handleItemException(e, logger);
        } finally {
            itemSetReport.addSubReport(
                ItemProcessorReport.getCurrentReport());
        }
    }
}
