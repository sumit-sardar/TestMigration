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
public class ReadingDependencyRule extends DependencyRule {
	public ReadingDependencyRule() {
		masterTest = SubtestNames.READING;
		slaveTests = new String[] {SubtestNames.VOCABULARY};
	}
	
	public String message() {
		return "Reading must be chosen if Vocabulary is needed.";
	}

}
