package com.ctb.lexington.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.ctb.lexington.data.FilterSelectionVO;
import com.ctb.lexington.data.SortSelectionVO;
import com.ctb.lexington.data.StudentDetailVO;

/**
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 * @author Tai Truong
 * @version
 *
 */

public class StudentSortFilterPager implements Serializable 
{
	private SortSelectionVO sort = null;
	private List filterList = null;
    private int pageNumber = 0;
    private int pageSize = 0;
    
    public StudentSortFilterPager() {
		this.sort = new SortSelectionVO(SortSelectionVO.LAST_NAME_SORT, true);
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
        
        if (sort.equals(SortSelectionVO.LAST_NAME_SORT))
            sortByLastName(list, ascending);
        if (sort.equals(SortSelectionVO.FIRST_NAME_SORT))
            sortByFirstName(list, ascending);
        if (sort.equals(SortSelectionVO.LOGIN_NAME_SORT))
            sortByLoginName(list, ascending);
        if (sort.equals(SortSelectionVO.PASSWORD_SORT))
            sortByPassword(list, ascending);
        if (sort.equals(SortSelectionVO.VALIDATION_STATUS_SORT))
            sortByValidationStatus(list, ascending);
        if (sort.equals(SortSelectionVO.ONLINE_TEST__STATUS_SORT))
            sortByOnlineTestStatus(list, ascending);
    }
    private void sortByLastName(List list, boolean ascending) {
        StudentInfoComparators sic = new StudentInfoComparators();
        if (ascending) 
            Collections.sort(list, sic.getStudentVOComparator("17"));
        else
            Collections.sort(list, sic.getStudentVOComparator("18"));
    }    
    private void sortByFirstName(List list, boolean ascending) {
        StudentInfoComparators sic = new StudentInfoComparators();
        if (ascending) 
            Collections.sort(list, sic.getStudentVOComparator("19"));
        else
            Collections.sort(list, sic.getStudentVOComparator("20"));
    }    
    private void sortByLoginName(List list, boolean ascending) {
        StudentInfoComparators sic = new StudentInfoComparators();
        if (ascending) 
            Collections.sort(list, sic.getStudentVOComparator("21"));
        else
            Collections.sort(list, sic.getStudentVOComparator("22"));
    }    
    private void sortByPassword(List list, boolean ascending) {
        StudentInfoComparators sic = new StudentInfoComparators();
        if (ascending) 
            Collections.sort(list, sic.getStudentVOComparator("23"));
        else
            Collections.sort(list, sic.getStudentVOComparator("24"));
    }    
    private void sortByValidationStatus(List list, boolean ascending) {
        StudentInfoComparators sic = new StudentInfoComparators();
        if (ascending) 
            Collections.sort(list, sic.getStudentVOComparator("25"));
        else
            Collections.sort(list, sic.getStudentVOComparator("26"));
    }    
    private void sortByOnlineTestStatus(List list, boolean ascending) {
        StudentInfoComparators sic = new StudentInfoComparators();
        if (ascending) 
            Collections.sort(list, sic.getStudentVOComparator("27"));
        else
            Collections.sort(list, sic.getStudentVOComparator("28"));
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
	            StudentDetailVO student = (StudentDetailVO)srcList.get(i);
	            pageList.add(student.clone());
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
            StudentDetailVO student = (StudentDetailVO) iter.next();
            if (matchedAllFilters(student)) {
                result.add(student.clone());
            }
        }
        return result;
    }
    public boolean matchedAllFilters(StudentDetailVO student) {
        for (Iterator iter = this.filterList.iterator(); iter.hasNext();) {
            FilterSelectionVO filter = (FilterSelectionVO) iter.next();
            String filterKey = filter.getFilterKey();
            String operator = filter.getOperator();
            String operands = filter.getOperand1();
            String value = null;
            if (filterKey.equals("lastName"))
                value = student.getLastName();
            if (filterKey.equals("firstName"))
                value = student.getFirstName();
            if (filterKey.equals("loginName"))
                value = student.getUserName();
            if (filterKey.equals("password"))
                value = student.getPassword();
            if (filterKey.equals("validation"))
                value = student.getValidationStatus();
            if (filterKey.equals("testStatus"))
                value = student.getTestCompletionStatus();  

            boolean matched = true;
            if (filterKey.equals("validation")) {
	            if (! matchedFilter_ValidationStatus(operator, value)) {
	                matched = false;
	            }
            }
            else
            if (filterKey.equals("testStatus")) {
	            if (! matchedFilter_OnlineTestStatus(operator, value)) {
	                matched = false;
	            }
            }
            else {
                value = fixedString(value);
                StringTokenizer st = new StringTokenizer(operands, ",");
                boolean result = (st.countTokens() == 0);
                while (st.hasMoreTokens()) {
                    String operand = st.nextToken();
                    operand = fixedString(operand);
		            result = result | matchedFilter_InputText(operator, operand, value);
                }
                matched = result;
            }
            if (! matched) {
                return false;
            }
        }
        return true;
    }

    public boolean matchedFilter_InputText(String operator, String operand, String value) {
		if (operator.equals("Show All") || operand.equals("")) {
		    return true;            
		}
		else
		if (operator.equals("Contains")) {
			if (value.indexOf(operand) < 0) {
				return false;
			}
		}
		else 
		if (operator.equals("Begin with")) {
			if (!value.startsWith(operand)) {
				return false;
			}
		}
		else 
		if (operator.equals("Is exactly")) {
			if (!value.equals(operand)) {
				return false;
			}
		}
        return true;
    }    
    public boolean matchedFilter_ValidationStatus(String operator, String value) {
		if (operator.equals("Show All")) {
		    return true;
		}
		else
		if (operator.equals("Valid")) {
			if (!value.equals("VA")) {
				return false;
			}
		}
		else
		if (operator.equals("Invalid")) {
			if (!value.equals("IN")) {
				return false;
			}
		}
        return true;
    }    
    public boolean matchedFilter_OnlineTestStatus(String operator, String value) {
		if (operator.equals("Show All")) {
		    return true;
		}
		else
		if (operator.equals("Completed")) {
			if (!value.equals("CO")) {
				return false;
			}		    
		}
		else
		if (operator.equals("In Completed")) {
			if (!value.equals("IC")) {
				return false;
			}		    
		}
		else
		if (operator.equals("In Progress")) {
			if (!value.equals("IP")) {
				return false;
			}		    
		}
		else
		if (operator.equals("Not Taken")) {
			if (!value.equals("NT")) {
				return false;
			}		    
		}
		else
		if (operator.equals("Scheduled")) {
			if (!value.equals("SC")) {
				return false;
			}		    
		}
		else
		if (operator.equals("Student Stop")) {
			if (!value.equals("IS")) {
				return false;
			}		    
		}
		else
		if (operator.equals("System Stop")) {
			if (!value.equals("IN")) {
				return false;
			}		    
		}
		else
		if (operator.equals("Test Locked")) {
			if (!value.equals("CL")) {
				return false;
			}		    
		}
	    return true;
    }    
    public String fixedString(String str) {
        String ret = new String("");
        if (str != null) {
            ret = str.trim().toLowerCase();
        }
    	return ret;
    }
}
