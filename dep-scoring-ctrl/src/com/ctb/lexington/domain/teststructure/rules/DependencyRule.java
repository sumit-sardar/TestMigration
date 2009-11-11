/*
 * Created on Aug 31, 2004
 *
 */
package com.ctb.lexington.domain.teststructure.rules;

import com.ctb.lexington.domain.teststructure.Subtests;

/**
 * @author arathore
 *
 */
public abstract class DependencyRule implements Rule{
	protected String masterTest;
	protected String[] slaveTests;
	
	public boolean validate(Subtests subtests) {
		if (!subtests.areAnyPresent(slaveTests)) return true;
		return (subtests.isPresent(masterTest));
	}
}
