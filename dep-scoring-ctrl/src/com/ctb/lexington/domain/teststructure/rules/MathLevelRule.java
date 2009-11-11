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
public class MathLevelRule extends LevelRule {
	public MathLevelRule() {
		subtestNames = new String[] {
				SubtestNames.MATH_COMPUTATION,
				SubtestNames.APPLIED_MATH};
	}
	
	public String message() {
		return "If you select Math Computation and Applied Math, then they must be of the same level.";
	}

}
