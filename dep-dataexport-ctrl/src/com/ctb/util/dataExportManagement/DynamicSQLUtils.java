package com.ctb.util.dataExportManagement; 


import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.bean.request.SortParams.SortType;
import com.ctb.exception.request.InvalidFilterFieldException;
import com.ctb.exception.request.InvalidSortFieldException;

/** 
 * DynamicSQLUtils.java
 * @author John_Wang
 */ 


public class DynamicSQLUtils 
{
    static final long serialVersionUID = 1L;
    
	/**
	 * Generates a where clause for SQL based on filter params
	 * @param filter
	 * @return String
	 */
    public static String generateWhereClauseForFilter(FilterParams filter) 
        throws InvalidFilterFieldException
    {
        StringBuffer result = new StringBuffer();
        
        FilterParam [] filterParams = filter.getFilterParams();
        for (int i = 0; i < filterParams.length; i++) {
            FilterParam filterParam = filterParams[i];
            if (filterParam != null) {
                String fieldName = filterParam.getField();
                FilterType fieldType = filterParam.getType();
                if (!FilterType.EQUALS.equals(fieldType))
                    throw new InvalidFilterFieldException("Field type '"+fieldType+"' is not supported");
                String filterValue = (String) filterParam.getArgument()[0];
                if (fieldName.equals("FirstName")) 
                    result.append(" and upper(stu.first_name) = upper('").append(escapeString(filterValue)).append("')");
                else if (fieldName.equals("MiddleName")) 
                    result.append(" and upper(stu.middle_name) = upper('").append(escapeString(filterValue)).append("')");
                else if (fieldName.equals("LastName")) 
                    result.append(" and upper(stu.last_name) = upper('").append(escapeString(filterValue)).append("')");
                else if (fieldName.equals("LoginId")) 
                    result.append(" and upper(stu.user_name) = upper('").append(escapeString(filterValue)).append("')");
                else if (fieldName.equals("StudentIdNumber")) 
                    result.append(" and upper(stu.ext_pin1) = upper('").append(escapeString(filterValue)).append("')");
                else if (fieldName.equals("Grade")) 
                    result.append(" and stu.grade = '").append(filterValue).append("'");
               /*else if (fieldName.equals("ScoringStatus")) 
                    result.append(" and stu.scoringStatus = '").append(filterValue).append("'");*/
                else if (fieldName.equals("Gender")) 
                    result.append(" and stu.gender = '").append(filterValue).append("'");
                else
                	if (!fieldName.equals("ScoringStatus"))  {
                		throw new InvalidFilterFieldException("Field name '"+fieldName+"' is not supported");
                	}
            }
        }
        
        return result.toString();
    }
    
    private static String escapeString(String input) {
        if (input == null || "".equals(input.trim()))
            return input;
        
        if (input.indexOf("'") >=0)
            return replace(input, "'", "''");
        else
            return input;
        
    }
    
    /**
     * Replace occurrences of a substring.
     *
     * StringHelper.replace("1-2-3", "-", "|");<br>
     * result: "1|2|3"<br>
     * StringHelper.replace("-1--2-", "-", "|");<br>
     * result: "|1||2|"<br>
     * StringHelper.replace("123", "", "|");<br>
     * result: "123"<br>
     * StringHelper.replace("1-2---3----4", "--", "|");<br>
     * result: "1-2|-3||4"<br>
     * StringHelper.replace("1-2---3----4", "--", "---");<br>
     * result: "1-2----3------4"<br>
     *
     * @param s String to be modified.
     * @param find String to find.
     * @param replace String to replace.
     * @return a string with all the occurrences of the string to find replaced.
     * @throws NullPointerException if s is null.
     *
     */
    public static String replace(String s, String find, String replace){
        int findLength;
        // the next statement has the side effect of throwing a null pointer
        // exception if s is null.
        int stringLength = s.length();
        if (find == null || (findLength = find.length()) == 0){
            // If there is nothing to find, we won't try and find it.
            return s;
        }
        if (replace == null){
            // a null string and an empty string are the same
            // for replacement purposes.
            replace = "";
        }
        int replaceLength = replace.length();

        // We need to figure out how long our resulting string will be.
        // This is required because without it, the possible resizing
        // and copying of memory structures could lead to an unacceptable runtime.
        // In the worst case it would have to be resized n times with each
        // resize having a O(n) copy leading to an O(n^2) algorithm.
        int length;
        if (findLength == replaceLength){
            // special case in which we don't need to count the replacements
            // because the count falls out of the length formula.
            length = stringLength;
        } else {
            int count;
            int start;
            int end;

            // Scan s and count the number of times we find our target.
            count = 0;
            start = 0;
            while((end = s.indexOf(find, start)) != -1){
                count++;
                start = end + findLength;
            }
            if (count == 0){
                // special case in which on first pass, we find there is nothing
                // to be replaced.  No need to do a second pass or create a string buffer.
                return s;
            }
            length = stringLength - (count * (findLength - replaceLength));
        }

        int start = 0;
        int end = s.indexOf(find, start);
        if (end == -1){
            // nothing was found in the string to replace.
            // we can get this if the find and replace strings
            // are the same length because we didn't check before.
            // in this case, we will return the original string
            return s;
        }
        // it looks like we actually have something to replace
        // *sigh* allocate memory for it.
        StringBuffer sb = new StringBuffer(length);

        // Scan s and do the replacements
        while (end != -1){
            sb.append(s.substring(start, end));
            sb.append(replace);
            start = end + findLength;
            end = s.indexOf(find, start);
        }
        end = stringLength;
        sb.append(s.substring(start, end));

        return (sb.toString());
    }    

	/**
	 * Generates an order by clause for SQL based on sort params
	 * @param sort
	 * @return String
	 */
    public static String generateOrderByClauseForSorter(SortParams sort) 
        throws InvalidSortFieldException
    {
        StringBuffer result = new StringBuffer();
        
        SortParam [] sortParams = sort.getSortParams();
        boolean hasSomeField = false;
        for (int i = 0; i < sortParams.length; i++) {
            SortParam sortParam = sortParams[i];
            if (sortParam != null) {
                String sortName = sortParam.getField();
                SortType sortType = sortParam.getType();
                if (sortName.equals("StudentName")) {
                    if (hasSomeField)
                        result.append(",");
                    result.append(" stu.last_name ").append(sortType.getType());
                    result.append(", stu.first_name ").append(sortType.getType());
                    result.append(", stu.middle_name ").append(sortType.getType());
                    hasSomeField = true;
                }
                else if (sortName.equals("LoginId")) {
                    if (hasSomeField)
                        result.append(",");
                    result.append(" stu.user_name ").append(sortType.getType());
                    hasSomeField = true;
                }
                else if (sortName.equals("Grade")) {
                    if (hasSomeField)
                        result.append(",");
                    result.append(" stu.grade ").append(sortType.getType());
                    hasSomeField = true;
                }
                else if (sortName.equals("TestSessionName")) {
                    if (hasSomeField)
                        result.append(",");
                    result.append(" tadmin.test_admin_name ").append(sortType.getType());
                    hasSomeField = true;
                } 
                else if (sortName.equals("StudentIdNumber")) {
                    if (hasSomeField)
                        result.append(",");
                    result.append(" stu.ext_pin1 ").append(sortType.getType());
                    hasSomeField = true;
                }
                else 
                    throw new InvalidSortFieldException("Field name '"+sortName+"' is not supported");
            }
        }
        if (hasSomeField)
            return " order by " + result.toString();
        else
            return "";
    }


}