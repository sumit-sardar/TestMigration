package com.ctb.contentBridge.core.publish.cprocessor;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingEntry;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingProcessor;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingUtils;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingWriter;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingWriterItemIdsFilterDecorator;
import com.ctb.contentBridge.core.publish.mapping.MapperFactory;
import com.ctb.contentBridge.core.publish.report.HierarchyReport;
import com.ctb.contentBridge.core.publish.report.ItemMapReport;
import com.ctb.contentBridge.core.publish.report.ItemSetReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.report.ReportFactory;

public class CommandProcessorTestMapMerge implements CommandProcessor {

    private Iterator mappingEntries;
    private Iterator currentItemMaps;
    private MappingProcessor mappingProcessor;
    private MappingWriter writer;
    private int expectedNumberOfEntries;

    public CommandProcessorTestMapMerge(
        Iterator mappingEntries,
        Iterator currentItemMaps,
        MappingProcessor mappingProcessor,
        MappingWriter writer,
        int expectedNumberOfEntries) {
        this.mappingEntries = mappingEntries;
        this.currentItemMaps = currentItemMaps;
        this.mappingProcessor = mappingProcessor;
        this.writer = writer;
        this.expectedNumberOfEntries = expectedNumberOfEntries;
    }

    public Report process() {
        ItemSetReport itemSetReport = ReportFactory.createItemSetReport(true);

        boolean isFirstRow = true;

        while (mappingEntries.hasNext()) {
            String line = (String) mappingEntries.next();

            try {
                mappingProcessor.processMapping(line);
            } finally {
                ItemMapReport.getCurrentReport().setOperation("merge");
                itemSetReport.addSubReport(
                    ItemMapReport.getCurrentReport());
            }
        }

        if (mappingProcessor.getEntries().size() != expectedNumberOfEntries) {
            throw new SystemException(
                "["
                    + expectedNumberOfEntries
                    + "] entries expected. Only ["
                    + mappingProcessor.getEntries().size()
                    + "] found.");
        }

        if (itemSetReport.isSuccess()) {
            writeMapping(mappingProcessor.getEntries());

            if (HierarchyReport.getCurrentReport() != null) {
                HierarchyReport.getCurrentReport().setItemMap(
                    MappingUtils.createItemMapFromMappingEntries(
                        mappingProcessor.getEntries().values(),
                        MapperFactory.getObjectives()));

            }

        }

        return itemSetReport;
    }

    private void writeMapping(Map entries) {
        MappingWriter decoratedWriter =
            new MappingWriterItemIdsFilterDecorator(writer, entries.keySet());

        for (Iterator iter = currentItemMaps; iter.hasNext();) {
            String mapping = (String) iter.next();
            decoratedWriter.writeLine(mapping);
        }

        Set sortedItemIdSet = new TreeSet(entries.keySet());
        for (Iterator iter = sortedItemIdSet.iterator(); iter.hasNext();) {
            String itemId = (String) iter.next();

            writer.writeLine(
                itemId
                    + ","
                    + ((MappingEntry) entries.get(itemId)).getObjectiveId());
        }

        writer.close();

    }
}
