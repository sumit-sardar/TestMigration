/*
 * Created on Aug 2, 2004
 *
 */
package com.ctb.lexington.domain.teststructure;

import com.ctb.lexington.domain.teststructure.rules.SubtestRuleFactory;

/**
 * @author arathore
 *
 */
public class TabeSubtestRules extends SubtestRules{
	public TabeSubtestRules(Subtests subtests) {
		this.subtests = subtests;
		errorRules.add(SubtestRuleFactory.mathLevelRule());
		errorRules.add(SubtestRuleFactory.readingLevelRule());
		errorRules.add(SubtestRuleFactory.languageLevelRule());
		errorRules.add(SubtestRuleFactory.readingDependencyRule());
		errorRules.add(SubtestRuleFactory.languageDependencyRule());
        errorRules.add(SubtestRuleFactory.atleastOneSubtestRule());
		
		warningRules.add(SubtestRuleFactory.mathSubtestRule());
		warningRules.add(SubtestRuleFactory.scoreCalculatableRule());		
	}
}