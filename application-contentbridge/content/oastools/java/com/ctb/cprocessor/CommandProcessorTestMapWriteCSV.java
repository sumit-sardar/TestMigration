package com.ctb.cprocessor;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.ctb.common.tools.SystemException;
import com.ctb.mapping.FrameworkInfo;
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

public class CommandProcessorTestMapWriteCSV implements CommandProcessor {
    private final FrameworkInfo frameworkInfo;
    private Iterator itemIds;
    private MappingProcessor mappingProcessor;
    private MappingWriter writer;
    private int expectedNumberOfEntries;

    public CommandProcessorTestMapWriteCSV(
        Iterator lines,
        MappingProcessor mappingProcessor,
        MappingWriter writer,
        int expectedNumberOfEntries) {
        this.itemIds = lines;
        this.mappingProcessor = mappingProcessor;
        this.writer = writer;
        this.expectedNumberOfEntries = expectedNumberOfEntries;
        this.frameworkInfo = MapperFactory.getFrameworkInfo();
    }

    public Report process() {
        ItemSetReport itemSetReport = ReportFactory.createItemSetReport(true);

        while (itemIds.hasNext()) {
            try {
                // generate mapping entry from the list of item ids.
                String line = (String) itemIds.next();
                mappingProcessor.processMapping(line);
            } finally {
                ItemMapReport.getCurrentReport().setOperation(
                    "csv mapping udpate");
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

        writeMapping(new TreeSet(mappingProcessor.getEntries().values()));

        if (HierarchyReport.getCurrentReport() != null) {
            HierarchyReport.getCurrentReport().setItemMap(
                MappingUtils.createItemMapFromMappingEntries(
                    mappingProcessor.getEntries().values(),
                    MapperFactory.getObjectives()));

        }

        return itemSetReport;
    }

    private void writeMapping(Collection entries) {
        int displayLevel = getDisplayLevel(entries);

        writeColumnTitles(displayLevel);
        writeEntries(entries, displayLevel);
        writer.close();
    }

    private int getDisplayLevel(Collection entries) {
        int level = 0;

        for (Iterator entryIter = entries.iterator(); entryIter.hasNext();) {
            MappingEntry entry = (MappingEntry) entryIter.next();

            int mappedLevel = entry.getMappedObjectiveLevel();
            if (mappedLevel > level) {
                level = mappedLevel;
            }
        }

        return level == 0 ? 0 : level - 1;
    }

    private void writeColumnTitles(int displayLevel) {
        StringBuffer buffer = new StringBuffer();

        // add the column header for the last 2 hierarchy level.
        buffer.append(this.frameworkInfo.getLevelName(displayLevel));
        buffer.append(",");
        buffer.append(this.frameworkInfo.getLevelName(displayLevel + 1));

        String columnTitles =
            StringUtils.replace(
                MappingUtils.COLUMN_TITLE,
                MappingEntry.HIERARCHY_TAG,
                buffer.toString());
        writer.writeLine(columnTitles);
    }

    private void writeEntries(Collection entries, int displayLevel) {
        for (Iterator entryIter = entries.iterator(); entryIter.hasNext();) {
            MappingEntry entry = (MappingEntry) entryIter.next();
            writer.writeLine(
                StringUtils.replace(
                    entry.toString(),
                    MappingEntry.HIERARCHY_TAG,
                    entry.getHierarchyString(displayLevel)));
        }
    }
}
