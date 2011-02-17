/*
 * Created on Dec 10, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.util.Map;
import java.util.Set;

import org.jdom.Element;


public interface StimulusComparer {

	/**
	 * Creates a Map that contains Sets Stimulus Ids that have the 
	 * same stimulus - keyed by the stimulus
	 * 
	 * @return a Map keed by String containing Sets of strings
	 */
	Map getAllMatchingGroups();
	
	/**
	 * Finds all Stimulus Ids that match a given stimulus
	 * 
	 * @param stimulus the Stimulus
	 * 
	 * @return set of Stimulus ids 
	 */
	Set getMatchingGroup(Element stimulus);
}
