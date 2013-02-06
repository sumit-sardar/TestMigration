package com.ctb.contentBridge.core.publish.cprocessor;

import java.util.Iterator;
import java.util.List;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingUtils;
import com.ctb.contentBridge.core.publish.report.ItemMapReport;
import com.ctb.contentBridge.core.publish.report.ItemSetReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.report.ReportFactory;

/**
 * @author wmli
 */
public class CommandProcessorTestMapCompare implements CommandProcessor {
    private List srcMappingEntries;
    private List tgtMappingEntries;

    public CommandProcessorTestMapCompare(
        List srcMappingEntries,
        List tgtMappingEntries) {
        this.srcMappingEntries = srcMappingEntries;
        this.tgtMappingEntries = tgtMappingEntries;
    }

    public Report process() {
        ItemSetReport itemSetReport = ReportFactory.createItemSetReport(true);

        if (srcMappingEntries.size() != tgtMappingEntries.size()) {
            throw new BusinessException(
                "The number of entries in the two csv files do not match: original csv ["
                    + srcMappingEntries.size()
                    + "] new csv ["
                    + tgtMappingEntries.size()
                    + "]. ");
        }

        for (int i = 0; i < srcMappingEntries.size(); i++) {
            ItemMapReport itemMapReport = new ItemMapReport();
            itemMapReport.setOperation("compare");
            itemMapReport.setSuccess(true);

            itemSetReport.addSubReport(itemMapReport);

            List srcList =
                MappingUtils.getValuesForCommaDelimitedList(
                    (String) srcMappingEntries.get(i));

            List tgtList =
                MappingUtils.getValuesForCommaDelimitedList(
                    (String) tgtMappingEntries.get(i));

            String itemId = (String) srcList.get(2);

            itemMapReport.setID(itemId);

            List different = MappingUtils.compareValuesList(srcList, tgtList);

            if (different.size() > 0) {
                StringBuffer buffer = new StringBuffer("\n");
                for (Iterator iter = different.iterator(); iter.hasNext();) {
                    String diff = (String) iter.next();
                    buffer.append("\t");
                    buffer.append(diff);
                    buffer.append("\n");
                }

                itemMapReport.setDescription(buffer.toString());
            }
        }

        return itemSetReport;
    }
}
