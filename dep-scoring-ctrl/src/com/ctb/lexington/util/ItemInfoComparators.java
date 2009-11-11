/*
 * Created on Mar 09, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.ctb.lexington.util;
 
import java.util.Comparator;

import com.ctb.lexington.data.ItemInfo;
import com.ctb.lexington.data.SortSelectionVO;

/**
 * @author Tai Truong
 *
 */
public class ItemInfoComparators {

	public ItemInfoComparators() {	}
	
	public Comparator getComparator(String field, boolean ascending) {
		if (field.equals(SortSelectionVO.ITEM_NAME_SORT) && ascending) return new NameAscendingComparator();
		if (field.equals(SortSelectionVO.ITEM_NAME_SORT) && !ascending) return new NameDescendingComparator();
		if (field.equals(SortSelectionVO.ITEM_TYPE_SORT) && ascending) return new TypeAscendingComparator();
		if (field.equals(SortSelectionVO.ITEM_TYPE_SORT) && !ascending) return new TypeDescendingComparator();
		if (field.equals(SortSelectionVO.ITEM_STATUS_SORT) && ascending) return new StatusAscendingComparator();
		if (field.equals(SortSelectionVO.ITEM_STATUS_SORT) && !ascending) return new StatusDescendingComparator();
		if (field.equals(SortSelectionVO.ITEM_CREATED_BY_SORT) && ascending) return new CreatedByAscendingComparator();
		if (field.equals(SortSelectionVO.ITEM_CREATED_BY_SORT) && !ascending) return new CreatedByDescendingComparator();
		if (field.equals(SortSelectionVO.ITEM_CREATED_DATE_SORT) && ascending) return new DisplayDateAscendingComparator();
		if (field.equals(SortSelectionVO.ITEM_CREATED_DATE_SORT) && !ascending) return new DisplayDateDescendingComparator();
		return new NameAscendingComparator();
	}
	 
	private class NameAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			result = compareNames(left, right);
			if(result == 0)
				result = compareDisplayDates(right, left);
			return result;
		}
	}
	
	private class NameDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			result = compareNames(right, left);
			if(result == 0)
				result = compareDisplayDates(right, left);
			return result;
		}
	}

	private class TypeAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			result = compareTypes(left, right);
			if(result == 0)
				result = compareDisplayDates(right, left);
			return result;
		}
	}

	private class TypeDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			result = compareTypes(right, left);
			if(result == 0)
				result = compareDisplayDates(right, left);
			return result;
		}
	}

	private class StatusAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			result = compareStatuses(left, right);
			if(result == 0)
				result = compareDisplayDates(right, left);
			return result;
		}
	}
	 
	private class StatusDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			result = compareStatuses(right, left);
			if(result == 0)
				result = compareDisplayDates(right, left);
			return result;
		}
	}

	private class CreatedByAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			result = compareCreatedBys(left, right);
			if(result == 0)
				result = compareDisplayDates(right, left);
			return result;
		}
	}
	
	private class CreatedByDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			result = compareCreatedBys(right, left);
			if(result == 0)
				result = compareDisplayDates(right, left);
			return result;
		}
	}
	
	private class DisplayDateAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			return compareDisplayDates(left, right);
		}
	}
	
	private class DisplayDateDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			ItemInfo left = (ItemInfo) o1;
			ItemInfo right = (ItemInfo) o2;
			return compareDisplayDates(right, left);
		}
	}
	
	private int compareNames(ItemInfo left_, ItemInfo right_){
		String leftName = left_.getName() != null ? left_.getName() : "";		
		String rightName = right_.getName() != null ? right_.getName() : "";
		return leftName.toLowerCase().compareTo(rightName.toLowerCase());
	}
	
	private int compareTypes(ItemInfo left_, ItemInfo right_){
		String leftType = left_.getType() != null ? left_.getType() : "";		
		String rightType = right_.getType() != null ? right_.getType() : "";
		return leftType.toLowerCase().compareTo(rightType.toLowerCase());
	}
	
	private int compareStatuses(ItemInfo left_, ItemInfo right_){
		String leftStatus = left_.getStatus() != null ? left_.getStatus() : "";		
		String rightStatus = right_.getStatus() != null ? right_.getStatus() : "";
		return leftStatus.toLowerCase().compareTo(rightStatus.toLowerCase());
	}
	
	private int compareCreatedBys(ItemInfo left_, ItemInfo right_){
		String leftCreatedBy = left_.getCreatedBy() != null ? left_.getCreatedBy() : "";		
		String rightCreatedBy = right_.getCreatedBy() != null ? right_.getCreatedBy() : "";
		return leftCreatedBy.toLowerCase().compareTo(rightCreatedBy.toLowerCase());
	}
	
	private int compareDisplayDates(ItemInfo left_, ItemInfo right_){
		return left_.getDisplayDate().compareTo(right_.getDisplayDate());
	}
}
