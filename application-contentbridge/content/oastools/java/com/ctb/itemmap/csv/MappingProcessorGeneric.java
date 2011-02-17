package com.ctb.itemmap.csv;

import java.util.HashMap;
import java.util.Map;

import com.ctb.common.tools.SystemException;
import com.ctb.reporting.ItemMapReport;

public class MappingProcessorGeneric implements MappingProcessor {
    private Map entries;
    MappingBuilder builder;
    MappingValidator validator;

    public MappingProcessorGeneric(
        MappingBuilder builder,
        MappingValidator validator) {
        this.builder = builder;
        this.validator = validator;

        entries = new HashMap();
    }

    public void processMapping(String mapping) {
        ItemMapReport itemMapReport = new ItemMapReport();
        ItemMapReport.setCurrentReport(itemMapReport);
        itemMapReport.setSuccess(true);

        MappingEntry entry = builder.build(mapping);
        entries.put(entry.getItemId(), entry);
        itemMapReport.setID(entry.getItemId());

        try {
            validator.validate(mapping, entry);
        } catch (SystemException e) {
            itemMapReport.setException(e);
            itemMapReport.setSuccess(false);
        }
    }

    public Map getEntries() {
        return entries;
    }

}
