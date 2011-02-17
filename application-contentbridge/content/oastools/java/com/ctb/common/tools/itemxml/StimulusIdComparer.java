/*
 * Created on Dec 5, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.util.Iterator;
import java.util.Set;


public interface StimulusIdComparer {
	Set[] getAllMatchingGroups();
	Set getMatchingGroup(String id);
	Iterator stimulusIdIterator();
}
