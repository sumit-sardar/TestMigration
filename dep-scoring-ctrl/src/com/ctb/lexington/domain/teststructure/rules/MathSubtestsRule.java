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
public class MathSubtestsRule implements Rule {
	public boolean validate(Subtests subtests) {
		return subtests.isPresent(SubtestNames.MATH_COMPUTATION)
				&& subtests.isPresent(SubtestNames.APPLIED_MATH);
	}
	
	
	public String message() {
		return "The selected test structure does not contain both the math subtests. " +
				"This test will not have a total math score.";
	}
}
