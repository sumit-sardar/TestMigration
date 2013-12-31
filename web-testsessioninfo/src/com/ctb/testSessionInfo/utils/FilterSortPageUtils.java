package com.ctb.testSessionInfo.utils; 

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.bean.request.SortParams.SortType;
import com.ctb.testSessionInfo.dto.TestRosterFilter;
import com.ctb.testSessionInfo.dto.TestStatusFilter;
import com.ctb.widgets.bean.ColumnSortEntry;
import java.util.ArrayList;
import java.util.Iterator;
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

    public static final String FILTERTYPE_SHOWALL = "Show all";
    public static final String FILTERTYPE_COMPLETED = "Completed";
    public static final String FILTERTYPE_INCOMPLETE = "Incomplete";
    public static final String FILTERTYPE_INPROGRESS = "In progress";
    public static final String FILTERTYPE_NOTTAKEN = "Not taken";
    public static final String FILTERTYPE_SCHEDULED = "Scheduled";
    public static final String FILTERTYPE_STUDENTSTOP = "Student stop";
    public static final String FILTERTYPE_SYSTEMSTOP = "System stop";
    public static final String FILTERTYPE_TESTLOCKED = "Test locked";
    public static final String FILTERTYPE_TESTABANDONED = "Session abandoned";
    public static final String FILTERTYPE_STUDENTPAUSE = "Student pause";
    public static final String FILTERTYPE_VALID = "Valid";
    public static final String FILTERTYPE_INVALID = "Invalid";
    public static final String FILTERTYPE_PARTIALLY_INVALID = "Partially Invalid";
    
    public static final String TESTSESSION_DEFAULT_FILTER_COLUMN = "TestAdminStatus";
    public static final String TESTSESSION_DEFAULT_FILTER_VALUE = "CU";
  
    // Sort
    public static final String TESTSESSION_DEFAULT_SORT = "LoginEndDate";
    public static final String ORGNODE_DEFAULT_SORT = "OrgNodeName";
    public static final String TESTROSTER_DEFAULT_SORT = "LastName";
    public static final String TEST_DEFAULT_SORT = "ItemSetName";
    public static final String STUDENT_DEFAULT_SORT = "LastName";
    public static final String PROGRAM_DEFAULT_SORT = "ProgramName";
    public static final String STATUS_DEFAULT_SORT = "SessionName";
    
    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";

    // Page
    public static final int PAGESIZE_3 = 3;
    public static final int PAGESIZE_5 = 5;
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
    
    public static SortParams buildSortParams(String sortName, String sortOrderBy)
    {
        SortParams sort = new SortParams();
        SortType sortType = sortOrderBy.equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;        
        
        SortParam[] sortParams = new SortParam[1];
        sortParams[0] = new SortParam(sortName, sortType);
        sort.setSortParams(sortParams);
   
        return sort;
    }
 
    public static SortParams buildSortParams(String[] sortNames, String[] sortOrderBys)
    {
        SortParams sort = new SortParams();
        SortParam[] sortParams = new SortParam[sortNames.length];
        for(int i=0; i<sortNames.length; i++){
            SortType sortType = sortOrderBys[i].equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;        
            sortParams[i] = new SortParam((String)sortNames[i], sortType);
        }
        sort.setSortParams(sortParams);

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
    
    public static FilterParams buildTestRosterFilterParams(TestRosterFilter trf)
    {
        if (trf == null)
            trf = new TestRosterFilter();            
        
        ArrayList filters = new ArrayList();
        String value;
        FilterType type;
        FilterParam fp;
        
        value =  trf.getLastName();        
        type = convertFilterType(trf.getLastNameFilterType());
        fp = buildFilterParamSingleValue("LastName", value, type);
        if (fp != null)
            filters.add(fp);
        
        value =  trf.getFirstName();        
        type = convertFilterType(trf.getFirstNameFilterType());
        fp = buildFilterParamSingleValue("FirstName", value, type);        
        if (fp != null)
            filters.add(fp);

        value =  trf.getLoginName();        
        type = convertFilterType(trf.getLoginNameFilterType());
        fp = buildFilterParamSingleValue("UserName", value, type);
        if (fp != null)
            filters.add(fp);

        value =  trf.getPassword();        
        type = convertFilterType(trf.getPasswordFilterType());
        fp = buildFilterParamSingleValue("Password", value, type);
        if (fp != null)
            filters.add(fp);

        FilterParam validationStatusFilter = null;
        value = trf.getValidationStatusFilterType();
        if ((value != null) && (! value.equals(FILTERTYPE_SHOWALL))) {
            String[] fieldValues = new String[1];
            fieldValues[0] = validationStatus_StringToCode(value);
            validationStatusFilter = new FilterParam("ValidationStatus", fieldValues, FilterType.EQUALS);
            filters.add(validationStatusFilter);
        }

        FilterParam testStatusFilter = null;
        value = trf.getTestStatusFilterType();
        if ((value != null) && (! value.equals(FILTERTYPE_SHOWALL))) {
            String[] fieldValues = new String[1];
            fieldValues[0] = testStatus_StringToCode(value);
            testStatusFilter = new FilterParam("TestCompletionStatus", fieldValues, FilterType.EQUALS);
            filters.add(testStatusFilter);
        }
        
        FilterParams filter = null;
        if (filters.size() > 0) {
            filter = new FilterParams();
            filter.setFilterParams((FilterParam[])filters.toArray(new FilterParam[0]));        
        }
    
        return filter;
    }

    public static FilterParams buildTestStatusFilterParams(TestStatusFilter tsf)
    {
        if (tsf == null)
            tsf = new TestStatusFilter();            
        
        ArrayList filters = new ArrayList();
        String value;
        FilterType type;
        FilterParam fp;
        
        value =  tsf.getSessionName();        
        type = convertFilterType(tsf.getSessionNameFilterType());
        fp = buildFilterParamSingleValue("SessionName", value, type);
        if (fp != null)
            filters.add(fp);
        
        value =  tsf.getSessionNumber();        
        type = convertFilterType(tsf.getSessionNumberFilterType());
        fp = buildFilterParamSingleValue("SessionNumber", value, type);        
        if (fp != null)
            filters.add(fp);

        value =  tsf.getLoginId();        
        type = convertFilterType(tsf.getLoginIdFilterType());
        fp = buildFilterParamSingleValue("LoginId", value, type);
        if (fp != null)
            filters.add(fp);

        value =  tsf.getPassword();        
        type = convertFilterType(tsf.getPasswordFilterType());
        fp = buildFilterParamSingleValue("Password", value, type);
        if (fp != null)
            filters.add(fp);
            
        value =  tsf.getAccessCode();        
        type = convertFilterType(tsf.getAccessCodeFilterType());
        fp = buildFilterParamSingleValue("AccessCode", value, type);
        if (fp != null)
            filters.add(fp);
            
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

    public static FilterParam buildFilterParamSingleValue(String fieldName, String fieldValue, FilterType type)
    {    
        FilterParam fp = null;
        if ((fieldName != null) && (fieldValue != null) && (type != null)) {
            fieldValue = fieldValue.trim();
            if (fieldValue.length() > 0) {            
                String[] fieldValues = new String[1];
                fieldValues[0] = fieldValue;                    
                fp = new FilterParam(fieldName, fieldValues, type);
            }
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

    public static String customStatus_ToString(String src)
    {
        if (src == null) 
            src = "Regular";
        return src;
    }
    
    public static String validationStatus_CodeToString(String src)
    {
        if (src == null) 
            return "";
        if (src.equals("IN")) 
            return FILTERTYPE_INVALID;
        if (src.equals("VA")) 
            return FILTERTYPE_VALID;
        if (src.equals("PI")) 
            return FILTERTYPE_PARTIALLY_INVALID;
        return null;
    }
    
    public static String validationStatus_StringToCode(String src)
    {
        if (src == null) 
            return "";
        if (src.equals(FILTERTYPE_INVALID)) 
            return "IN";
        if (src.equals(FILTERTYPE_VALID)) 
            return "VA";
        if (src.equals(FILTERTYPE_PARTIALLY_INVALID)) 
            return "PI";
        return null;
    }
    
    public static String testStatus_CodeToString(String src)
    {
        if (src == null) 
            return "";
        if (src.equals("SC")) 
            return FILTERTYPE_SCHEDULED;
        if (src.equals("CO")) 
            return FILTERTYPE_COMPLETED;
        if (src.equals("IN")) 
            return FILTERTYPE_SYSTEMSTOP;
        if (src.equals("NT")) 
            return FILTERTYPE_NOTTAKEN;
        if (src.equals("IP")) 
            return FILTERTYPE_INPROGRESS;
        if (src.equals("IC")) 
            return FILTERTYPE_INCOMPLETE;
        if (src.equals("IS")) 
            return FILTERTYPE_STUDENTSTOP;
        if (src.equals("CL")) 
            return FILTERTYPE_TESTLOCKED;
        if (src.equals("OC")) 
            return FILTERTYPE_COMPLETED;
        if (src.equals("AB")) 
            return FILTERTYPE_TESTABANDONED;
        if (src.equals("SP")) 
            return FILTERTYPE_STUDENTPAUSE;
        return null;
    }
    public static String testStatus_StringToCode(String src)
    {
        if (src == null) 
            return "";
        if (src.equals(FILTERTYPE_SCHEDULED)) 
            return "SC";
        if (src.equals(FILTERTYPE_COMPLETED)) 
            return "CO";
        if (src.equals(FILTERTYPE_SYSTEMSTOP)) 
            return "IN";
        if (src.equals(FILTERTYPE_NOTTAKEN)) 
            return "NT";
        if (src.equals(FILTERTYPE_INPROGRESS)) 
            return "IP";
        if (src.equals(FILTERTYPE_INCOMPLETE)) 
            return "IC";
        if (src.equals(FILTERTYPE_STUDENTSTOP)) 
            return "IS";
        if (src.equals(FILTERTYPE_TESTLOCKED)) 
            return "CL";
        if (src.equals(FILTERTYPE_COMPLETED)) 
            return "OC";
        if (src.equals(FILTERTYPE_TESTABANDONED)) 
            return "AB";
        if (src.equals(FILTERTYPE_STUDENTPAUSE)) 
            return "SP";
        return null;
    }
    
} 
