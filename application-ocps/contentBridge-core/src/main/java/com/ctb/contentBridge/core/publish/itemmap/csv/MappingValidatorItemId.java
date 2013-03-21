package com.ctb.contentBridge.core.publish.itemmap.csv;

import com.ctb.contentBridge.core.exception.SystemException;

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
