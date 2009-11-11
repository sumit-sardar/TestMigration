/*
 * Created on Aug 31, 2004
 *
 */
package com.ctb.lexington.domain.teststructure.rules;

import com.ctb.lexington.domain.teststructure.SubtestNames;

/**
 * @author arathore
 *
 */
public class LanguageDependencyRule extends DependencyRule {
	public LanguageDependencyRule() {
		masterTest = SubtestNames.LANGUAGE;
		slaveTests = new String[] 
						{SubtestNames.LANGUAGE_MECHANICS, 
						 SubtestNames.SPELLING};
	}
	
	public String message() {
		return "Language must be selected if either Language Mechanics or Spelling is needed.";
	}

}
