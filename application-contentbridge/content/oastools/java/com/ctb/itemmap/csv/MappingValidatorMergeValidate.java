package com.ctb.itemmap.csv;

import com.ctb.common.tools.SystemException;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.Objectives;

/**
 * @author wmli
 */
public class MappingValidatorMergeValidate extends MappingValidatorMerge {
    public MappingValidatorMergeValidate(
        Objectives objectives,
        ItemMap itemMap) {
        super(objectives, itemMap);
    }

    public void validate(String mapping, MappingEntry entry) {
        try {
            super.validate(mapping, entry);
        } catch (SystemException e) {
            throw e;
        }

        String objectiveId = itemMap.curriculumId(entry.getItemId());

        if (objectiveId != null
            && !objectiveId.equals(entry.getObjectiveId())) {
            throw new SystemException(
                "Conflicted mapping. Item already mapped to ["
                    + objectiveId
                    + "] remapped to ["
                    + entry.getObjectiveId()
                    + "]");
        }
    }

}
