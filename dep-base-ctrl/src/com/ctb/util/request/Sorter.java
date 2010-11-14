package com.ctb.util.request; 

import com.ctb.bean.CTBBean;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.bean.request.SortParams.SortType;
import com.ctb.exception.request.InvalidSortFieldException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Sorts an array of CTBBeans according to a SortParam.
 * Output list is ordered according to the sort spec. 
 * Currently only Integer-, String-, or Date-typed bean 
 * fields may be used for sorting.
 * 
 * @author Nate_Cohen
 */
public class Sorter 
{
    static final long serialVersionUID = 1L;
	/**
	 * Sorts a list according to the SortParam
     * 
	 * @param list - the data to be sorted
	 * @param sort - specifies sort criteria
	 * @throws InvalidSortFieldException
	 * @throws Exception
	 */
    public static void sort(List list, SortParam sort) throws InvalidSortFieldException, Exception{
        Collections.sort(list, new CTBBeanComparator(sort.getField(), sort.getType()));
    }
    
    /**
     * Allows Collections.sort to sort lists of CTBBean types
     * according to a SortParam.
     * 
     * @author Nate_Cohen
     */
    private static class CTBBeanComparator implements Comparator {
        private String field;
        private SortType type;
        
        public CTBBeanComparator(String field, SortType type) {
            this.field = field;
            this.type = type;
        }
        
        public int compare(Object o1, Object o2) {
            CTBBean t1 = (CTBBean) o1;
            CTBBean t2 = (CTBBean) o2;
            int result = 0;
            Object value1 = null;
            Object value2 = null;
            try {
                value1 = o1.getClass().getMethod("get" + field, null).invoke(o1, null);
                value2 = o2.getClass().getMethod("get" + field, null).invoke(o2, null);
            } catch (Exception e) {
                throw new RuntimeException("CTBBeanComparator: compare: invalid field: " + field);
            }
            if(this.type.equals(SortType.ALPHAASC)) {
                if(value1 == null) {
                    return -1;
                } else if (value2 == null) {
                    return 1;
                } else if(value1 instanceof String) {
                    result = ((String) value1).compareToIgnoreCase((String) value2);
                } else if(value1 instanceof Integer) {
                    result = ((Integer) value1).compareTo((Integer) value2);
                } else if(value1 instanceof Date) {
                    result = ((Date) value1).compareTo((Date) value2);
                } else {
                    throw new RuntimeException("CTBBeanComparator: compare: invalid sort arguments: " + value1 + " " + value2);
                }
            } else if(this.type.equals(SortType.ALPHADESC)) {
                if(value1 == null) {
                    return 1;
                } else if (value2 == null) {
                    return -1;
                } else if(value1 instanceof String) {
                    result = -1 * ((String) value1).compareToIgnoreCase((String) value2);
                } else if(value1 instanceof Integer) {
                    result = -1 * ((Integer) value1).compareTo((Integer) value2);
                } else if(value1 instanceof Date) {
                    result = -1 * ((Date) value1).compareTo((Date) value2);
                } else {
                    throw new RuntimeException("CTBBeanComparator: compare: invalid sort arguments: " + value1 + " " + value2);
                }
            } else if(this.type.equals(SortType.NUMASC)) {
                if(value1 == null) {
                    return -1;
                } else if (value2 == null) {
                    return 1;
                } else if(value1 instanceof String) {
                    result = ((String) value1).compareToIgnoreCase((String) value2);
                } else if(value1 instanceof Integer) {
                    result = ((Integer) value1).compareTo((Integer) value2);
                } else if(value1 instanceof Date) {
                    result = ((Date) value1).compareTo((Date) value2);
                } else {
                    throw new RuntimeException("CTBBeanComparator: compare: invalid sort arguments: " + value1 + " " + value2);
                }
            } else if(this.type.equals(SortType.NUMDESC)) {
                if(value1 == null) {
                    return 1;
                } else if (value2 == null) {
                    return -1;
                } else if(value1 instanceof String) {
                    result = -1 * ((String) value1).compareToIgnoreCase((String) value2);
                } else if(value1 instanceof Integer) {
                    result = -1 * ((Integer) value1).compareTo((Integer) value2);
                } else if(value1 instanceof Date) {
                    result = -1 * ((Date) value1).compareTo((Date) value2);
                } else {
                    throw new RuntimeException("CTBBeanComparator: compare: invalid sort arguments: " + value1 + " " + value2);
                }
            } else {
                throw new RuntimeException("CTBBeanComparator: compare: invalid sort type: " + this.type);
            }
            return result;
        }
    }
} 
