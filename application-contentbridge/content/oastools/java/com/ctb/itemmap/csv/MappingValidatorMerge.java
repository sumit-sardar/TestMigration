package com.ctb.itemmap.csv;

import com.ctb.common.tools.SystemException;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.Objectives;
import com.ctb.reporting.ItemMapReport;

/**
 * @author wmli
 */
public class MappingValidatorMerge implements MappingValidator {
    protected Objectives objectives;
    protected ItemMap itemMap;

    public MappingValidatorMerge(Objectives objectives, ItemMap itemMap) {
        this.objectives = objectives;
        this.itemMap = itemMap;
    }

    public void validate(String mapping, MappingEntry entry) {
        // check if mapping existed
        // 1. mapping not existed - insert
        // 2. mapping existed - mapped to same objective - no change
        // 3. mapping existed - mapped to difference objective - overwrite mapping

        //		check if the objective id is valid
        if (objectives.objectiveFromCurriculumId(entry.getObjectiveId())
            == null) {
            throw new SystemException(
                "Objective Id not existed: " + entry.getObjectiveId());
        }

        String objectiveId = itemMap.curriculumId(entry.getItemId());

        // case 1
        if (objectiveId == null) {
            ItemMapReport.getCurrentReport().setDescription(
                "Mapping does not exist. Will map to ["
                    + entry.getObjectiveId()
                    + "] " + getMappedAnswerKeyStr(entry));
        } else {
            // case 2
            if (objectiveId.equals(entry.getObjectiveId())) {
                ItemMapReport.getCurrentReport().setDescription(
                    "Mapping exsted. Mapped to ["
                        + entry.getObjectiveId()
                        + "] "
                        + getMappedAnswerKeyStr(entry));
            } else {
                ItemMapReport.getCurrentReport().setDescription(
                    "Conflicted mapping. Item already mapped to ["
                        + objectiveId
                        + "] remapped to ["
                        + entry.getObjectiveId()
                        + "] " + getMappedAnswerKeyStr(entry));
            }
        }
    }

    private String getMappedAnswerKeyStr(MappingEntry entry) {
        if (!entry.getAnswerKey().equals(""))
            return "Answer Key: [" + entry.getAnswerKey() + "]";
        return "";
    }
}
