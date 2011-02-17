package com.ctb.cprocessor;

import java.io.File;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.common.tools.SystemException;
import com.ctb.hibernate.HibernateSession;
import com.ctb.xmlProcessing.item.ItemProcessor;
import com.ctb.mapping.MapperFactory;
import com.ctb.mapping.ItemMap;
import com.ctb.reporting.HierarchyReport;
import com.ctb.reporting.ItemProcessorReport;
import com.ctb.reporting.ItemSetReport;
import com.ctb.reporting.Report;
import com.ctb.reporting.ReportFactory;

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
