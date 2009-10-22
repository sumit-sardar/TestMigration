package com.ctb.util.userManagement; 



import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.bean.request.SortParams.SortType;
import com.ctb.exception.request.InvalidFilterFieldException;
import com.ctb.exception.request.InvalidSortFieldException;
import java.util.StringTokenizer;

/** 
 * DynamicSQLUtils.java
 * @author Tata Consultency Services
 * 
 */ 


public class DynamicSQLUtils 
{
    static final long serialVersionUID = 1L;
    
	/**
	 * Generates a where clause for SQL based on filter params
	 * @param filter
	 * @return String
	 */
    public static String generateWhereClauseForFilter(FilterParams filter, boolean isRoleFilter) 
        throws InvalidFilterFieldException
    {
        StringBuffer result = new StringBuffer();
        String filterValue = "";
        FilterParam filterParam = null;
        FilterType fieldType = null;
        FilterParam [] filterParams = filter.getFilterParams();
                
        for (int i = 0; i < filterParams.length-1; i++) {
            filterParam = filterParams[i];
            if (filterParam != null) {
                String fieldName = filterParam.getField();
                fieldType = filterParam.getType();
                if (!FilterType.EQUALS.equals(fieldType))
                    throw new InvalidFilterFieldException("Field type '"+fieldType+"' is not supported");
                filterValue = (String) filterParam.getArgument()[0];
                if (fieldName.equals("FirstName")) 
                    result.append(" and  upper(u.first_name) = upper('").append(escapeString(filterValue)).append("')");
               else if (fieldName.equals("LastName")) 
                    result.append(" and upper(u.last_name) = upper('").append(escapeString(filterValue)).append("')");
               else if (fieldName.equals("UserName")) 
                    result.append(" and upper(u.user_name) = upper('").append(escapeString(filterValue)).append("')");
               else if (fieldName.equals("Email")) 
                    result.append(" and upper(u.email) = upper('").append(escapeString(filterValue)).append("')");
               /*else 
                    if (!fieldName.equals("Role")) //As role field is a dropdown so it will never be null.
                      throw new InvalidFilterFieldException("Field name '"+fieldName+"' is not supported");*/
            }
        }
        
        filterParam = filterParams[filterParams.length-1];
        fieldType = filterParam.getType();
        if (!isRoleFilter) { 
            
            if (!FilterType.CONTAINS.equals(fieldType))
                throw new InvalidFilterFieldException("Field type '"+fieldType+"' is not supported");
            filterValue = (String) filterParam.getArgument()[0];
            filterValue = splitString(filterValue);
            result.append(" and upper(r.role_name) in (").append(filterValue).append(")");
            
        } else {
            
            if (!FilterType.EQUALS.equals(fieldType))
                throw new InvalidFilterFieldException("Field type '"+fieldType+"' is not supported");
            Integer filterIntValue = (Integer) filterParam.getArgument()[0];
            result.append(" and r.role_id = ").append(filterIntValue);
            
        }
        
        
        return result.toString();
    }
   
   private static String splitString(String input){
    StringTokenizer strToken = null;
    StringBuffer buff = new StringBuffer();
    if(input != null){
        strToken = new StringTokenizer(input,",");
        while(strToken.hasMoreElements()) {
         buff.append("'");   
         buff.append(strToken.nextElement().toString());
             if(strToken.hasMoreElements()) {
                buff.append("',"); 
             } else {
                buff.append("'"); 
             }  
        }
     }
      return buff.toString();  
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
                if (sortName.equals("DisplayUserName")) {
                    if (hasSomeField)
                        result.append(",");
                    result.append(" u.last_name ").append(sortType.getType());
                    result.append(", u.first_name ").append(sortType.getType());
                    hasSomeField = true;
                }
                else if (sortName.equals("UserName")) {
                    if (hasSomeField)
                        result.append(",");
                    result.append(" u.user_name ").append(sortType.getType());
                    hasSomeField = true;
                }
                else if (sortName.equals("Email")) {
                    if (hasSomeField)
                        result.append(",");
                    result.append(" u.email ").append(sortType.getType());
                    hasSomeField = true;
                }
                else if (sortName.equals("RoleName")) {
                    if (hasSomeField)
                        result.append(",");
                    // Here the alias of the column has been used instead of the actual column name 
                    // as we used INITCAP() on the field value
                       
                    result.append(" roleName ").append(sortType.getType());
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
    
    /**
	 * Retrive role from filter params
	 * @param filter
	 * @return String
	 */
    public static String getRoleFromFilter(FilterParams filter) throws InvalidFilterFieldException {
        FilterParam [] filterParams = filter.getFilterParams();
        String role = null;
        String fieldName = null;
        boolean flag = false;
        for (int i = 0; i < filterParams.length; i++) {
            FilterParam filterParam = filterParams[i];
            if (filterParam != null) {
                fieldName = filterParam.getField();
                FilterType fieldType = filterParam.getType();
                if (!FilterType.EQUALS.equals(fieldType))
                    throw new InvalidFilterFieldException("Field type '"+fieldType+"' is not supported");
                String filterValue = (String) filterParam.getArgument()[0];
                 if (fieldName.equals("Role")){
                    role = filterValue;
                    flag = true;
                    break;
                 } 
            }
        }
        if(!flag)
            throw new InvalidFilterFieldException("Field name '"+fieldName+"' is not supported");
        return role;
        
    }
    
    /**
	 * Retrive role from filter params
	 * @param filter
	 * @return String
	 */
    public static String generateWhereClauseForUserSuggesion(String fieldName, String fieldValue){
        StringBuffer whereClause = new StringBuffer();
        if (fieldName.equals("{actionForm.userProfile.firstName}")) 
            whereClause.append(" AND upper(u.first_name) like upper('").append(escapeString(fieldValue)).append("%')");
       else if (fieldName.equals("{actionForm.userProfile.lastName}")) 
            whereClause.append(" AND upper(u.last_name) like upper('").append(escapeString(fieldValue)).append("%')");
       else if (fieldName.equals("{actionForm.userProfile.loginId}")) 
            whereClause.append(" AND upper(u.user_name) like upper('").append(escapeString(fieldValue)).append("%')");  
       else if (fieldName.equals("{actionForm.userProfile.email}")) 
            whereClause.append(" AND upper(u.email) like upper('").append(escapeString(fieldValue)).append("%')");        
            
        return whereClause.toString();
        
    }
    
    /**
	 * Retrive role from filter params
	 * @param filter
	 * @return String
	 */
    public static String generateSelectClauseForUserSuggesion(String fieldName){
        StringBuffer selectClause = new StringBuffer();
        if (fieldName.equals("{actionForm.userProfile.firstName}")) 
            selectClause.append(" u.first_name as firstName ");
        else if (fieldName.equals("{actionForm.userProfile.lastName}")) 
            selectClause.append(" u.last_name as lastName ");
        else if (fieldName.equals("{actionForm.userProfile.loginId}")) 
            selectClause.append(" u.user_name as userName "); 
        else if (fieldName.equals("{actionForm.userProfile.email}")) 
            selectClause.append(" u.email as email ");
               
        return selectClause.toString();
        
    }
    
    /**
     * generate while clause to retirve the active test administrator
     * @return String 
     */
    public static String generateWhileClauseForActiveTestAdmin () {
        String clause = "and testAdmin.activation_status = 'AC'";
        return clause;
    }
}