/*
 * Created on June 3, 2005
 *
 */
package com.ctb.lexington.util;
 
import java.util.Comparator;
import java.util.Date;

import com.ctb.lexington.data.Stimulus;
import com.ctb.lexington.data.SortSelectionVO;

/**
 * @author Jon Becker
 *
 */
public class StimulusComparators {

	public StimulusComparators() {	}
	
	public Comparator getComparator(String field, boolean ascending) {
		if (field.equals(SortSelectionVO.STIMULUS_NAME_SORT) && ascending) return new StimulusNameAscendingComparator();
		if (field.equals(SortSelectionVO.STIMULUS_NAME_SORT) && !ascending) return new StimulusNameDescendingComparator();
		if (field.equals(SortSelectionVO.STIMULUS_TYPE_SORT) && ascending) return new StimulusTypeAscendingComparator();
		if (field.equals(SortSelectionVO.STIMULUS_TYPE_SORT) && !ascending) return new StimulusTypeDescendingComparator();
		if (field.equals(SortSelectionVO.STIMULUS_CREATED_BY_SORT) && ascending) return new StimulusCreatedByAscendingComparator();
		if (field.equals(SortSelectionVO.STIMULUS_CREATED_BY_SORT) && !ascending) return new StimulusCreatedByDescendingComparator();
		if (field.equals(SortSelectionVO.STIMULUS_UPDATED_DATE_SORT) && ascending) return new StimulusUpdatedDateAscendingComparator();
		if (field.equals(SortSelectionVO.STIMULUS_UPDATED_DATE_SORT) && !ascending) return new StimulusUpdatedDateDescendingComparator();
		return new StimulusNameAscendingComparator();
	}
	 
	private class StimulusNameAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			Stimulus left = (Stimulus) o1;
			Stimulus right = (Stimulus) o2;
			return compareNames(left, right);
		}
	}
	
	private class StimulusNameDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			Stimulus left = (Stimulus) o1;
			Stimulus right = (Stimulus) o2;
			return compareNames(right, left);
		}
	}

	private class StimulusTypeAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			Stimulus left = (Stimulus) o1;
			Stimulus right = (Stimulus) o2;
			int result = compareTypes(left, right);
			if(result == 0)
				result = compareNames(left, right);
			return result;
		}
	}

	private class StimulusTypeDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			Stimulus left = (Stimulus) o1;
			Stimulus right = (Stimulus) o2;
			int result = compareTypes(right, left);
			if(result == 0)
				result = compareNames(left, right);
			return result;
		}
	}

	private class StimulusCreatedByAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			Stimulus left = (Stimulus) o1;
			Stimulus right = (Stimulus) o2;
			int result = compareCreatedBy(left, right);
			if(result == 0)
				result = compareNames(left, right);
			return result;
		}
	}
	
	private class StimulusCreatedByDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			Stimulus left = (Stimulus) o1;
			Stimulus right = (Stimulus) o2;
			int result = compareCreatedBy(right, left);
			if(result == 0)
				result = compareNames(left, right);
			return result;
		}
	}
	
	private class StimulusUpdatedDateAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			Stimulus left = (Stimulus) o1;
			Stimulus right = (Stimulus) o2;
			int result = compareUpdatedDate(left, right);
			if(result == 0)
				result = compareNames(left, right);
			return result;
		}
	}
	
	private class StimulusUpdatedDateDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			Stimulus left = (Stimulus) o1;
			Stimulus right = (Stimulus) o2;
			int result = compareUpdatedDate(right, left);
			if(result == 0)
				result = compareNames(left, right);
			return result;
		}
	}
	
	private int compareNames(Stimulus left_, Stimulus right_){
		String leftName = left_.getName() != null ? left_.getName().toLowerCase() : "";		
		String rightName = right_.getName() != null ? right_.getName().toLowerCase() : "";
		return leftName.compareTo(rightName);
	}
	
	private int compareTypes(Stimulus left_, Stimulus right_){
		String leftType = left_.getTypeName() != null ? left_.getTypeName().toLowerCase() : "";		
		String rightType = right_.getTypeName() != null ? right_.getTypeName().toLowerCase() : "";
		return leftType.compareTo(rightType);
	}
	
	private int compareCreatedBy(Stimulus left_, Stimulus right_){
		String leftCreatedBy = left_.getCreatedBy() != null ? left_.getCreatedBy().toLowerCase() : "";		
		String rightCreatedBy = right_.getCreatedBy() != null ? right_.getCreatedBy().toLowerCase() : "";
		return leftCreatedBy.compareTo(rightCreatedBy);
	}
	
	private int compareUpdatedDate(Stimulus left_, Stimulus right_){
		Date leftUpdatedDate = left_.getUpdatedDate();		
		Date rightUpdatedDate = right_.getUpdatedDate();	
		return leftUpdatedDate.compareTo(rightUpdatedDate);
	}
}
