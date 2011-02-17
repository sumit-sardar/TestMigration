package com.ctb.cprocessor;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.ctb.common.tools.SystemException;
import com.ctb.mapping.MapperFactory;
import com.ctb.reporting.HierarchyReport;
import com.ctb.reporting.ItemMapReport;
import com.ctb.reporting.ItemSetReport;
import com.ctb.reporting.Report;
import com.ctb.reporting.ReportFactory;
import com.ctb.itemmap.csv.MappingEntry;
import com.ctb.itemmap.csv.MappingProcessor;
import com.ctb.itemmap.csv.MappingUtils;
import com.ctb.itemmap.csv.MappingWriter;
import com.ctb.itemmap.csv.MappingWriterItemIdsFilterDecorator;

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
