package com.ctb.itemmap.csv;

import com.ctb.common.tools.SystemException;

/**
 * @author wmli
 */
public class MappingValidatorItemId implements MappingValidator {
    public void validate(String mapping, MappingEntry entry) {
    	if (entry.getObjectiveId().equals("")) {
			throw new SystemException("Cannot find mapping in item_map.txt");
    	}
    }

}
