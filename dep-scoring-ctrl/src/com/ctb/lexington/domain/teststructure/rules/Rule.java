/*
 * Created on Aug 3, 2004
 *
 */
package com.ctb.lexington.domain.teststructure.rules;

import com.ctb.lexington.domain.teststructure.Subtests;

/**
 * @author arathore
 *
 */
public interface Rule {
	public boolean validate(Subtests subtests);
	public String message();
}
