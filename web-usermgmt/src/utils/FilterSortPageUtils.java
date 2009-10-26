package utils; 

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.bean.request.SortParams.SortType;
import com.ctb.widgets.bean.ColumnSortEntry;
import dto.Message;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class FilterSortPageUtils 
{
    // Filter
    public static final String FILTERTYPE_AFTER = "After";
    public static final String FILTERTYPE_BEFORE = "Before";
    public static final String FILTERTYPE_BETWEEN = "Between";
    public static final String FILTERTYPE_CONTAINS = "Contains";
    public static final String FILTERTYPE_ENDSWITH = "Ends with";
    public static final String FILTERTYPE_EQUALS = "Is exactly";
    public static final String FILTERTYPE_GREATERTHAN = "Greater than";
    public static final String FILTERTYPE_LESSTHAN = "Less than";
    public static final String FILTERTYPE_BEGINSWITH = "Begins with";

    public static final String FILTERTYPE_ANY_GRADE = "Any grade";
    public static final String FILTERTYPE_ANY_GENDER = "Any gender";
    public static final String FILTERTYPE_SELECT_A_GRADE = "Select a grade";
    public static final String FILTERTYPE_SELECT_A_GENDER = "Select a gender";
        
    //sort 
    public static final String TEST_DEFAULT_SORT_COLUMN = "ItemSetLevel";    
    public static final String ORGNODE_DEFAULT_SORT_COLUMN = "OrgNodeName";    
    public static final String USER_DEFAULT_SORT_COLUMN = "DisplayUserName";    
    public static final String PROCTOR_DEFAULT_SORT_COLUMN = "LastName";
    public static final String PROCTOR_DEFAULT_SECONDARY_SORT_COLUMN = "FirstName";    
    public static final String TESTROSTER_DEFAULT_SORT = "LastName";
    public static final String ORGNODE_NAME_SORT = "OrgNodeName";    
    public static final String LAST_NAME_SORT = "LastName";    
    public static final String FIRST_NAME_SORT = "FirstName";    
    public static final String LOGIN_NAME_SORT = "UserName";   

    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";

    // Page
    public static final int PAGESIZE_3 = 3;
    public static final int PAGESIZE_5 = 5;
    public static final int PAGESIZE_8 = 8;
    public static final int PAGESIZE_10 = 10;
    public static final int PAGESIZE_15 = 15;
    public static final int PAGESIZE_20 = 20;
    public static final int MAX_RECORDS = 10000;
  
    
    public static FilterParams buildFilterParams(String fieldName, String fieldValue)
    {
        FilterParams filter = new FilterParams();

        String[] fieldValues = new String[1];
        fieldValues[0] = fieldValue;
        
        FilterParam[] filterParams = new FilterParam[1];
        filterParams[0] = new FilterParam(fieldName, fieldValues, FilterType.EQUALS);
        filter.setFilterParams(filterParams);        
   
        return filter;
    }
    
    public static FilterParams buildFilterParams(Hashtable fieldHashtable, FilterType filterType)
    {
        FilterParams filter = new FilterParams();
        FilterParam[] filterParams = new FilterParam[fieldHashtable.size()];
        
        Enumeration enu = fieldHashtable.keys();
        int i=0;
        while (enu.hasMoreElements()) {
            String fieldName = (String) enu.nextElement();
            String fieldValue = (String) fieldHashtable.get(fieldName);
            String[] fieldValues = new String[1];
            fieldValues[0] = fieldValue;
            filterParams[i] = new FilterParam(fieldName, fieldValues, filterType);
            i++;
        }

        filter.setFilterParams(filterParams);        
   
        return filter;
    }
    

    public static SortParams buildUserSortParams(String sortName, String sortOrderBy)
    {
        SortParams sort = new SortParams();
                
        SortType sortType = sortOrderBy.equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;
        SortParam[] sortParams = new SortParam[2];
        
        sortParams[0] = new SortParam(sortName, sortType);
        
        String secondarySortName = USER_DEFAULT_SORT_COLUMN;
        if (sortName.equals(USER_DEFAULT_SORT_COLUMN))
             secondarySortName = LOGIN_NAME_SORT;
             
        sortParams[1] = new SortParam(secondarySortName, sortType);
        sort.setSortParams(sortParams);
        
        return sort;
    }
    
    public static SortParams buildSortParams(String sortName, String sortOrderBy, String secondarySortName, String secondarySortOrderBy)
    {
        SortParams sort = new SortParams();
                
        if(secondarySortName != null && secondarySortOrderBy != null) {
            SortType sortType = sortOrderBy.equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;
            SortType secondarySortType = secondarySortOrderBy.equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;
            SortParam[] sortParams = new SortParam[2];
            sortParams[0] = new SortParam(sortName, sortType);
            sortParams[1] = new SortParam(secondarySortName, secondarySortType);
            sort.setSortParams(sortParams);
        } else {
            SortType sortType = sortOrderBy.equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;
            SortParam[] sortParams = new SortParam[1];
            sortParams[0] = new SortParam(sortName, sortType);
            sort.setSortParams(sortParams);
        }
        
        return sort;
    }


    public static PageParams buildPageParams(Integer pageRequested, int pageSize)
    {
        if (pageRequested.intValue() <= 0)
            pageRequested = new Integer(1);
            
        int maxPages = MAX_RECORDS / pageSize; 
        PageParams page = new PageParams(pageRequested.intValue(), pageSize, maxPages);     
     
        return page;
    }
    
    public static FilterParams buildFilterParams(String firstName, String lastName, String loginId, String email, String role)
    {
        ArrayList filters = new ArrayList();
        FilterParam fp;

        if ((firstName != null) && (firstName.length() > 0)) {
            fp = buildFilterParamSingleValue("FirstName", firstName, FilterType.EQUALS);
            if (fp != null)
                filters.add(fp);
        }

        if ((lastName != null) && (lastName.length() > 0)) {
            fp = buildFilterParamSingleValue("LastName", lastName, FilterType.EQUALS);
            if (fp != null)
                filters.add(fp);
        }

        if ((loginId != null) && (loginId.length() > 0)) {
            fp = buildFilterParamSingleValue("UserName", loginId, FilterType.EQUALS);
            if (fp != null)
                filters.add(fp);
        }

        if ((email != null) && (email.length() > 0)) {
            fp = buildFilterParamSingleValue("Email", email, FilterType.EQUALS);
            if (fp != null)
                filters.add(fp);
        }

        if ((role != null) && !(role.equals(Message.ANY_ROLE)) && !(role.equals(Message.SELECT_ROLE)) ) {
            try {
                fp = buildFilterParamSingleValue("RoleId", new Integer(Integer.parseInt(role)), FilterType.EQUALS);
                if (fp != null)
                    filters.add(fp);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        FilterParams filter = null;
        if (filters.size() > 0) {
            filter = new FilterParams();
            filter.setFilterParams((FilterParam[])filters.toArray(new FilterParam[0]));        
        }

        return filter;
    }

    public static FilterParam buildFilterParamMultipleValues(String fieldName, String fieldValue, FilterType type)
    {    
        FilterParam fp = null;
        if ((fieldName != null) && (fieldValue != null) && (type != null)) {
            fieldValue = fieldValue.trim();
            if (fieldValue.length() > 0) {            
                StringTokenizer st = new StringTokenizer(fieldValue, ",");
                String[] fieldValues = new String[st.countTokens()];
                int index = 0;
                while (st.hasMoreTokens()) {
                    String value = st.nextToken().trim();
                    fieldValues[index] = value;                    
                    index++;
                }
                fp = new FilterParam(fieldName, fieldValues, type);
            }
        }
        return fp;
    }

    public static FilterParam buildFilterParamSingleValue(String fieldName, Object fieldValue, FilterType type)
    {    
        FilterParam fp = null;
        if ((fieldName != null) && (fieldValue != null) && (type != null)) {
            if (fieldValue instanceof String ) {
                fieldValue = ((String) fieldValue).trim();
                if (((String) fieldValue).length() <= 0) {
                    return fp;
                }
            }
            Object[] fieldValues = new Object[1];
            fieldValues[0] = fieldValue;
            fp = new FilterParam(fieldName, fieldValues, type);
        }
        return fp;
    }

    public static FilterType convertFilterType(String src)
    {
        FilterType dest = null;
        if (src.equals(FILTERTYPE_AFTER))
            dest = FilterType.AFTER;
        if (src.equals(FILTERTYPE_BEFORE))
            dest = FilterType.BEFORE;
        if (src.equals(FILTERTYPE_BETWEEN))
            dest = FilterType.BETWEEN;
        if (src.equals(FILTERTYPE_CONTAINS))
            dest = FilterType.CONTAINS;
        if (src.equals(FILTERTYPE_ENDSWITH))
            dest = FilterType.ENDSWITH;
        if (src.equals(FILTERTYPE_EQUALS))
            dest = FilterType.EQUALS;
        if (src.equals(FILTERTYPE_GREATERTHAN))
            dest = FilterType.GREATERTHAN;
        if (src.equals(FILTERTYPE_LESSTHAN))
            dest = FilterType.LESSTHAN;
        if (src.equals(FILTERTYPE_BEGINSWITH))
            dest = FilterType.STARTSWITH;
            
        return dest;
    }
 
    
} 
