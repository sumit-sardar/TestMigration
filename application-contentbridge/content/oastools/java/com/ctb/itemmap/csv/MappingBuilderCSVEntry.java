package com.ctb.itemmap.csv;

import com.ctb.mapping.Objectives;

/**
 * @author wmli
 */
public class MappingBuilderCSVEntry implements MappingBuilder {
    Objectives objectives;

    public MappingBuilderCSVEntry(Objectives objectives) {
        this.objectives = objectives;
    }

    public MappingEntry build(String mapping) {
        MappingEntry entry = new MappingEntry(mapping, objectives);
        return entry;
    }

}
