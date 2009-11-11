/*
 * Created on Aug 4, 2004
 *
 */
package com.ctb.lexington.domain.teststructure.rules;

import java.util.HashMap;
import java.util.Map;

/**
 * @author arathore
 *
 */
public class SubtestRuleFactory {
	private static Map rules = new HashMap();
	private static final String mathRuleClassName = "com.ctb.lexington.domain.teststructure.rules.MathSubtestsRule";
	private static final String scoreCalculatableRule = "com.ctb.lexington.domain.teststructure.rules.ScoreCalculatableRule";
	private static final String languageLevelRuleClassName = "com.ctb.lexington.domain.teststructure.rules.LanguageLevelRule";
	private static final String readingLevelRuleClassName = "com.ctb.lexington.domain.teststructure.rules.ReadingLevelRule";
	private static final String mathLevelRuleClassName = "com.ctb.lexington.domain.teststructure.rules.MathLevelRule";
	private static final String terranovaLevelRuleClassName = "com.ctb.lexington.domain.teststructure.rules.TerranovaLevelRule";
	private static final String atleastOneSubtestRuleClassName = "com.ctb.lexington.domain.teststructure.rules.AtleastOneSubtestRule";
	private static String readingDependencyRuleClassName = "com.ctb.lexington.domain.teststructure.rules.ReadingDependencyRule";
	private static String languageDependencyRuleClassName = "com.ctb.lexington.domain.teststructure.rules.LanguageDependencyRule";
	
	public static Rule mathSubtestRule() {
		return rule(mathRuleClassName);
	}
	
	public static Rule scoreCalculatableRule() {
		return rule(scoreCalculatableRule);
	}
	
	public static Rule languageLevelRule() {
		return rule(languageLevelRuleClassName);
	}

	public static Rule readingLevelRule() {
		return rule(readingLevelRuleClassName);
	}

	public static Rule  mathLevelRule() {
		return rule(mathLevelRuleClassName);
	}

	public static Rule readingDependencyRule() {
		return rule(readingDependencyRuleClassName);
	}

	public static Rule  languageDependencyRule() {
		return rule(languageDependencyRuleClassName);
	}

    public static Rule terranovaLevelRule() {
        return rule(terranovaLevelRuleClassName);
    }
    
    public static Rule atleastOneSubtestRule(){
        return rule(atleastOneSubtestRuleClassName);
    }

	private static Rule rule(String ruleClassName) {
		Rule result = (Rule) rules.get(ruleClassName);
		if (result == null) {
			try {
				result = (Rule) Class.forName(ruleClassName).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			rules.put(ruleClassName, result);
		}
		return result;
	}

}