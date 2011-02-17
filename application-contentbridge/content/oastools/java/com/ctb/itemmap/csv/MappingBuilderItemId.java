package com.ctb.itemmap.csv;

import com.ctb.mapping.Mapper;

/**
 * @author wmli
 */
public class MappingBuilderItemId implements MappingBuilder {
    Mapper mapper;

    public MappingBuilderItemId(Mapper mapper) {
        this.mapper = mapper;
    }

    // build the mapping entry from the given item id.
    public MappingEntry build(String mapping) {
        String objectiveId = mapper.curriculumId(mapping);

        MappingEntry mappingEntry = null;

        if (objectiveId != null) {
            mappingEntry =
                new MappingEntry(
                    "",
                    "",
                    mapping,
                    objectiveId,
                    "",
                    mapper.getObjectives().getHierarchyObjectiveList(
                        objectiveId));
        } else {
            mappingEntry = new MappingEntry("", "", mapping, "", "", null);
        }

        return mappingEntry;
    }
}
