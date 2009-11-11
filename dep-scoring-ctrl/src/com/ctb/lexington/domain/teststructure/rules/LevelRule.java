/*
 * Created on Aug 4, 2004
 *
 */
package com.ctb.lexington.domain.teststructure.rules;

import com.ctb.lexington.domain.teststructure.Subtests;
import com.ctb.lexington.util.Stringx;

/**
 * @author arathore
 *
 */
public abstract class LevelRule implements Rule {
	protected String[] subtestNames;
	
	public boolean validate(Subtests subtests) {
		return validate(subtests, subtestNames);
	}

    protected boolean validate(Subtests currentSubtests, String[] ruleSubtestNames) {
        boolean accumulator = true;
		String expectedLevel = "";

		for (int i = 0; i < ruleSubtestNames.length; i++) {
			String name = ruleSubtestNames[i];
			if (currentSubtests.isPresent(name)) {
				String level = currentSubtests.subtest(name).getLevel();
				if (Stringx.isEmpty(expectedLevel)) expectedLevel = level;
				accumulator = accumulator && (expectedLevel == null || expectedLevel.equals(level));
			}
		}
		return accumulator;
    }
}