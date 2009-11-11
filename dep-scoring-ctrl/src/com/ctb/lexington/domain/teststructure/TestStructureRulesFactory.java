/*
 * Created on Aug 5, 2004
 *
 */
package com.ctb.lexington.domain.teststructure;

/**
 * @author arathore
 *
 */
public class TestStructureRulesFactory {
	public static SubtestRules rulesFor(String productType, Subtests subtests) {
		SubtestRules result = null;
		if (productType.equals("TB"))
			result = new TabeSubtestRules(subtests);
		else if (productType.equals("TV"))
		    result = new TerraNovaSubtestRules(subtests); 
		if (result == null) {
			throw new IllegalArgumentException("The test type " + productType + " is not valid!");
		}
		return result;
	}
}
