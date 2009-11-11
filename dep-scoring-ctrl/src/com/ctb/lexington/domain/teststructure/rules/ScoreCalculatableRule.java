/*
 * Created on Aug 3, 2004
 *
 */
package com.ctb.lexington.domain.teststructure.rules;

import com.ctb.lexington.domain.teststructure.SubtestNames;
import com.ctb.lexington.domain.teststructure.Subtests;

/**
 * @author arathore
 *
 */
public class ScoreCalculatableRule implements Rule {
	public boolean validate(Subtests subtests) {
		return subtests.isPresent(SubtestNames.READING) 
				&& subtests.isPresent(SubtestNames.LANGUAGE) 
				&& SubtestRuleFactory.mathSubtestRule().validate(subtests);
	}
	
	public String message() {
		return "Reading, Language, Math Computation and Applied Mathematics must" +
				" be selected for this test to qualify for a total score.";
	}
}
