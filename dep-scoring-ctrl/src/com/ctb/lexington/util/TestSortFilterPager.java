package com.ctb.lexington.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ctb.lexington.data.FilterSelectionVO;
import com.ctb.lexington.data.SortSelectionVO;
import com.ctb.lexington.data.TestInfo;

/**
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 * @author Tai Truong
 * @version
 *
 */

public class TestSortFilterPager implements Serializable 
{
	private SortSelectionVO sort = null;
	private List filterList = null;
    private int pageNumber = 0;
    private int pageSize = 0;
    
    public TestSortFilterPager() {
		this.sort = new SortSelectionVO(SortSelectionVO.TEST_NAME_SORT, true);
		this.filterList = null;
		this.pageNumber = 1;
		this.pageSize = 5;
    }
    public List gotoPage(List list) {
        return list;
    }
    public SortSelectionVO getSort() {
        return sort;
    }
    public void setSort(SortSelectionVO sort) {
        this.sort = sort;
    }
    public int getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }    
    public List getFilterList() {
        return filterList;
    }
    public void setFilterList(List filterList) {
        this.filterList = filterList;
    }
    public void sort(List list) {
        String sort = this.sort.getSort();
        boolean ascending = this.sort.getAscending();
        
        if (sort.equals(SortSelectionVO.TEST_NAME_SORT))
            sortByTestName(list, ascending);
        if (sort.equals(SortSelectionVO.LEVEL_NAME_SORT))
            sortByLevelName(list, ascending);
    }
    private void sortByTestName(List list, boolean ascending) {
        TestInfoComparators tic = new TestInfoComparators();
        if (ascending) 
            Collections.sort(list, tic.getTestComparator("1"));
        else
            Collections.sort(list, tic.getTestComparator("2"));
    }    
    private void sortByLevelName(List list, boolean ascending) {
        TestInfoComparators tic = new TestInfoComparators();
        if (ascending) 
            Collections.sort(list, tic.getTestComparator("3"));
        else
            Collections.sort(list, tic.getTestComparator("4"));
    }    
    public void setSortRequest(String sortRequest) {
	    if (sortRequest != null) {
	        boolean ascending = this.sort.getAscending();
	        String sort = this.sort.getSort();
	        if (sort.equals(sortRequest)) 
	            ascending = !ascending;
	        else 
	            ascending = true;
	        this.sort.setSort(sortRequest);
	        this.sort.setAscending(ascending);
	    }
    }
    public List getCurrentPage(List srcList) {
	    List pageList = new ArrayList();
        int index = (this.pageNumber - 1) * this.pageSize;
        if ((index >= 0) && (index < srcList.size())) {
            int count = 0;
	        for (int i=index ; i<srcList.size() ; i++) {
	            TestInfo test = (TestInfo)srcList.get(i);
	            pageList.add(test.clone());
	            count++;
	            if (count == this.pageSize)
	                break;
	        }
        }
        return pageList;
    }        
      
    public void saveFilter(List filterList) {
    	this.filterList = filterList;
    }
    public void clearAllfilters() {
        this.filterList = null;
    }
    public List filter(List list) {
        if (this.filterList == null)
            return list;
        
        List result = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            TestInfo test = (TestInfo) iter.next();
            if (matchedAllFilters(test)) {
                result.add(test.clone());
            }
        }
        return result;
    }
    public boolean matchedAllFilters(TestInfo test) {
        for (Iterator iter = this.filterList.iterator(); iter.hasNext();) {
            FilterSelectionVO filter = (FilterSelectionVO) iter.next();
            String filterKey = filter.getFilterKey();
            String operator = filter.getOperator();
            String operands = filter.getOperand1();
            String value = null;
            if (filterKey.equals(SortSelectionVO.TEST_NAME_SORT))
                value = test.getName();
            if (filterKey.equals(SortSelectionVO.LEVEL_NAME_SORT))
                value = test.getLevel();
            
            // implement filter here
        }
        return true;
    }
}
