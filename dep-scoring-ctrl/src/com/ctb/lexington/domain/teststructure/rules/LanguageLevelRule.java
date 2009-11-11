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
public class LanguageLevelRule extends LevelRule {

	public LanguageLevelRule() {
		subtestNames = new String[] {
				SubtestNames.LANGUAGE,
				SubtestNames.LANGUAGE_MECHANICS,
				SubtestNames.SPELLING};
	}
	
	public String message() {
		return "If you select Language, Language Mechanics and Spelling, then they must be of the same level.";
	}

}
