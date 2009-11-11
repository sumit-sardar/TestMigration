/*
 * Created on Aug 2, 2004
 *
 */
package com.ctb.lexington.domain.teststructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ctb.lexington.domain.teststructure.rules.Rule;

/**
 * @author arathore
 *
 */
public abstract class SubtestRules {
	protected Subtests subtests;
	protected List errorRules = new ArrayList();
	protected List warningRules = new ArrayList();
	private List invalidErrorRules = new ArrayList();
	private List invalidWarningRules = new ArrayList();
	
	public boolean hasNoErrors() {
		return isValid(errorRules, invalidErrorRules);
	}
	
	public boolean hasNoWarnings() {
		return isValid(warningRules, invalidWarningRules);
	}
	
	public boolean isValid(List rules, List collector) {
		boolean accumalatedResult = true;
		for (Iterator i = rules.iterator(); i.hasNext();) {
			Rule rule = (Rule) i.next();
			boolean isRuleValid = rule.validate(subtests);
			if (!isRuleValid) collector.add(rule);
			accumalatedResult = accumalatedResult && isRuleValid;
		}
		return accumalatedResult;
	}
	
	public String errorMessage() {
		return message(invalidErrorRules);
	}
	
	public String warningMessage() {
		return message(invalidWarningRules);
	}
	
	public String message(List messages) {
		if (messages.isEmpty()) return "";
		StringBuffer result = new StringBuffer();
		for (Iterator i = messages.iterator(); i.hasNext();)
			result.append(((Rule) i.next()).message()+"<br/>");
		return result.toString();
	}
}
