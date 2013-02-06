package com.ctb.contentBridge.core.publish.itemmap.csv;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.Objectives;

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
            throw new BusinessException(
                "Conflicted mapping. Item already mapped to ["
                    + objectiveId
                    + "] remapped to ["
                    + entry.getObjectiveId()
                    + "]");
        }
    }

}
