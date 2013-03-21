package com.ctb.contentBridge.core.publish.itemmap.csv;

import java.util.List;

import com.ctb.contentBridge.core.publish.mapping.Objectives;
import com.ctb.contentBridge.core.util.RegexUtils;


/**
 * @author wmli
 */
public class MappingBuilderItemObjectivePair implements MappingBuilder {
    Objectives objectives;
    public MappingBuilderItemObjectivePair(Objectives objectives) {
        this.objectives = objectives;
    }

    public MappingEntry build(String mapping) {
        List entry =
            RegexUtils.getAllMatchedGroups("([^\",]*,?|\"[^\"]*\",?)", mapping, ",");

        String index = (String) entry.get(0);
        String itemId = (String) entry.get(1);
        
		String objectiveId = "";
        if (entry.size() == 3)
        	objectiveId = (String) entry.get(2);
        	
        List hierarchy = objectives.getHierarchyObjectiveList(objectiveId);

        MappingEntry mappingEntry =
            new MappingEntry(index, "", itemId, objectiveId, "", hierarchy);

        return mappingEntry;
    }

}
