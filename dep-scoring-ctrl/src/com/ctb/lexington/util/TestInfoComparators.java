/*
 * Created on Mar 09, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.ctb.lexington.util;
 
import java.util.Comparator;
import com.ctb.lexington.data.TestInfo;

/**
 * @author Tai Truong
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestInfoComparators {

	public TestInfoComparators() {	}
	
	public Comparator getTestComparator(String columnIndex) {
		if (columnIndex == "1") return new TestNameAscendingComparator();
		if (columnIndex == "2") return new TestNameDescendingComparator();
		if (columnIndex == "3") return new LevelNameAscendingComparator();
		if (columnIndex == "4") return new LevelNameDescendingComparator();
		return new TestNameAscendingComparator();
	}
	
	private class TestNameAscendingComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			TestInfo left = (TestInfo) o1;
			TestInfo right = (TestInfo) o2;
			String leftName = left.getName() != null ? left.getName() : ""; 
			String rightName = right.getName() != null ? right.getName() : "";
			int result = leftName.compareTo(rightName);
			if (result == 0 ) 
				result = left.getLevel().compareTo(right.getLevel());
			return result;
		}
	}

	private class TestNameDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			return new TestNameAscendingComparator().compare(o2, o1);
		}
	}
	
	private class LevelNameAscendingComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			TestInfo left = (TestInfo) o1;
			TestInfo right = (TestInfo) o2;
			String leftLevel = left.getLevel() != null ? left.getLevel() : ""; 
			String rightLevel = right.getLevel() != null ? right.getLevel() : "";
			int result = leftLevel.compareTo(rightLevel);
			if (result == 0 ) 
				result = left.getName().compareTo(right.getName());
			return result;
		}
	}

	private class LevelNameDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			return new LevelNameAscendingComparator().compare(o2, o1);
		}
	}
}
