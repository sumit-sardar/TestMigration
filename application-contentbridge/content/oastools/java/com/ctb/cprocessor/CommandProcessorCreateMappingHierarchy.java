package com.ctb.cprocessor;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.xmlProcessing.item.ItemProcessor;
import com.ctb.xmlProcessing.item.ItemProcessorFactory;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.Mapper;
import com.ctb.mapping.MapperFactory;
import com.ctb.mapping.Objectives;
import com.ctb.reporting.HierarchyReport;
import com.ctb.reporting.ItemProcessorReport;
import com.ctb.reporting.ItemSetReport;
import com.ctb.reporting.Report;
import com.ctb.reporting.ReportFactory;

public class CommandProcessorCreateMappingHierarchy
    implements CommandProcessor {
    private static Logger logger =
        Logger.getLogger(CommandProcessorCreateMappingHierarchy.class);
    private final ItemProcessor itemProcessor;
    private final ItemMap itemMap;
    private final Objectives objectives;

    public CommandProcessorCreateMappingHierarchy(
        Objectives objectives,
        ItemMap itemMap) {
        this.objectives = objectives;
        this.itemMap = itemMap;
        this.itemProcessor =
            ItemProcessorFactory.getItemProcessorGenerateMappingHierarchy();
    }

    public Report process() {
        ItemSetReport itemSetReport = ReportFactory.createItemSetReport(true);

        if (MapperFactory.getItemMap() != null) {
            for (Iterator iter = MapperFactory.getItemMap().getAllItemIDs();
                iter.hasNext();
                ) {
                Element element = new Element("Item");
                element.setAttribute("ID", (String) iter.next());
                try {
                    itemProcessor.process(element);
                    addProjectedMappedId();
                } catch (Exception e) {
                    CommandProcessorUtility.handleItemException(e, logger);
                } finally {
                    itemSetReport.addSubReport(
                        ItemProcessorReport.getCurrentReport());
                }
            } //end for
        }

        HierarchyReport report =
            new HierarchyReport(this.objectives, this.itemMap);
        report.addSubReport(itemSetReport);
        report.setSuccess(true);
        return report;
    }

    private void addProjectedMappedId() {
        if (MapperFactory.getItemMap() != null) {
            Mapper mapper =
                MapperFactory.newMapper(this.objectives, this.itemMap);
            ItemProcessorReport report = ItemProcessorReport.getCurrentReport();
            report.setNewID(mapper.mappedItemId(report.getID()));
        }
    }
}
