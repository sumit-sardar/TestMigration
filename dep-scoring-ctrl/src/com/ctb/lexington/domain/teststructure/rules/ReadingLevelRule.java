/*
 * Created on Aug 4, 2004
 *
 */
package com.ctb.lexington.domain.teststructure.rules;

import com.ctb.lexington.domain.teststructure.SubtestNames;

/**
 * @author arathore
 *
 */
public class ReadingLevelRule extends LevelRule {
	public ReadingLevelRule() {
		subtestNames = new String[] {
				SubtestNames.VOCABULARY,
				SubtestNames.READING};
	}
	
	public String message() {
		return "If you select Reading and Vocabulary subtests, then they must be of the same level.";
	}
}
