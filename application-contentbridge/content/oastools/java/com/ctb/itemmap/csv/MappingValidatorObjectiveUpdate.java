package com.ctb.itemmap.csv;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ctb.common.tools.SystemException;
import com.ctb.mapping.Objectives;
import com.ctb.reporting.ItemMapReport;

/**
 * @author wmli
 */
public class MappingValidatorObjectiveUpdate implements MappingValidator {
    Objectives objectives;
    int displayLevel;

    public MappingValidatorObjectiveUpdate(
        Objectives objectives,
        int displayLevel) {
        this.objectives = objectives;
        this.displayLevel = displayLevel;
    }

    public void validate(String mapping, MappingEntry entry) {
        // check if the objective id is valid
        if (objectives.objectiveFromCurriculumId(entry.getObjectiveId())
            == null) {
            throw new SystemException(
                "Objective Id not existed: " + entry.getObjectiveId());
        }

        //		get a new string for the new entry
        String entryString = getEntryString(displayLevel, entry);

        List different =
            MappingUtils.compareValuesList(
                MappingUtils.getValuesForCommaDelimitedList(mapping),
                MappingUtils.getValuesForCommaDelimitedList(entryString));

        if (different.size() > 0) {
            StringBuffer buffer = new StringBuffer("\n");
            for (Iterator iter = different.iterator(); iter.hasNext();) {
                String diff = (String) iter.next();
                buffer.append("\t");
                buffer.append(diff);
                buffer.append("\n");
            }

            ItemMapReport.getCurrentReport().setDescription(buffer.toString());
        }
    }

    private String getEntryString(int displayLevel, MappingEntry entry) {
        return StringUtils.replace(
            entry.toString(),
            MappingEntry.HIERARCHY_TAG,
            entry.getHierarchyString(displayLevel));
    }
}
