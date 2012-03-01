package utils; 

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.bean.request.SortParams.SortType;
import com.ctb.widgets.bean.ColumnSortEntry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
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
    public static final String FILTERTYPE_ANY_SCORING_STATUS = "All";
    public static final String FILTERTYPE_ANY_TESTNAME = "Select Test";
    public static final String FILTERTYPE_ANY_CONTENT_AREA = "Any content area";
    
    public static final String FILTERTYPE_SHOWALL = "Show All";
    public static final String FILTERTYPE_COMPLETED = "Completed";
    public static final String FILTERTYPE_INCOMPLETED = "Incompleted";
    public static final String FILTERTYPE_INPROGRESS = "In progress";
    public static final String FILTERTYPE_NOTTAKEN = "Not taken";
    public static final String FILTERTYPE_SCHEDULED = "Scheduled";
    public static final String FILTERTYPE_STUDENTSTOP = "Student stop";
    public static final String FILTERTYPE_SYSTEMSTOP = "System stop";
    public static final String FILTERTYPE_TESTLOCKED = "Test locked";
    public static final String FILTERTYPE_VALID = "Valid";
    public static final String FILTERTYPE_INVALID = "Invalid";
    public static final String FILTERTYPE_INCOMPLETE = "Incomplete";
    public static final String FILTERTYPE_TESTABANDONED = "Session abandoned";
    public static final String FILTERTYPE_STUDENTPAUSE = "Student pause";
    
    public static final String SCORINGITEM_DEFAULT_FILTER_COLUMN = "ScoringItemStatus";
    
    public static final String STUDENTS_WITH_AND_WITHOUT_ACCOMMODATIONS ="Show All";
    public static final String STUDENTS_WITH_ACCOMMODATIONS ="Students with selected accommodations";
    public static final String STUDENTS_WITHOUT_ACCOMMODATIONS ="Students without accommodations";
    
    //sort 
    public static final String TEST_DEFAULT_SORT_COLUMN = "ItemSetLevel";    
    public static final String ORGNODE_DEFAULT_SORT_COLUMN = "OrgNodeName";    
    public static final String STUDENT_DEFAULT_SORT_COLUMN = "TestSessionName";  
    public static final String SCORE_STUDENT_DEFAULT_SORT_COLUMN = "UserName";  
    public static final String PROCTOR_DEFAULT_SORT_COLUMN = "LastName";
    public static final String PROCTOR_DEFAULT_SECONDARY_SORT_COLUMN = "FirstName";    
    public static final String TESTROSTER_DEFAULT_SORT = "LastName";
    public static final String ORGNODE_NAME_SORT = "OrgNodeName";    
    public static final String LAST_NAME_SORT = "LastName";    
    public static final String FIRST_NAME_SORT = "FirstName";    
    public static final String LOGIN_NAME_SORT = "LoginId"; 
    
    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";
    public static final String TESTITEM_DEFAULT_SORT_COLUMN = "ItemSetName";
    public static final String TESTITEM_NUMBER_SORT = "ItemSetOrder"; 

    // Page
    public static final int PAGESIZE_3 = 3;
    public static final int PAGESIZE_5 = 5;
    public static final int PAGESIZE_10 = 10;
    public static final int PAGESIZE_15 = 15;
    public static final int PAGESIZE_20 = 20;
    public static final int MAX_RECORDS = 10000;//50
  
    
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

    public static SortParams buildOrgNameLastNameFirstNameSortParams(String sortName, String sortOrderBy)
    {
        SortParams sort = new SortParams();
        SortType sortType = sortOrderBy.equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;
        SortParam[] sortParams = new SortParam[3];
                
        if(sortName.equals(ORGNODE_NAME_SORT)) {
            sortParams[0] = new SortParam(ORGNODE_NAME_SORT, sortType);
            sortParams[1] = new SortParam(LAST_NAME_SORT, sortType);
            sortParams[2] = new SortParam(FIRST_NAME_SORT, sortType);
        } 
        else
        if(sortName.equals(LAST_NAME_SORT)) {
            sortParams[0] = new SortParam(LAST_NAME_SORT, sortType);
            sortParams[1] = new SortParam(ORGNODE_NAME_SORT, sortType);
            sortParams[2] = new SortParam(FIRST_NAME_SORT, sortType);
        } 
        else
        if(sortName.equals(FIRST_NAME_SORT)) {
            sortParams[0] = new SortParam(FIRST_NAME_SORT, sortType);
            sortParams[1] = new SortParam(ORGNODE_NAME_SORT, sortType);
            sortParams[2] = new SortParam(LAST_NAME_SORT, sortType);
        } 
        else {
            sortParams = new SortParam[4];
            sortParams[0] = new SortParam(sortName, sortType);
            sortParams[1] = new SortParam(ORGNODE_NAME_SORT, sortType);
            sortParams[2] = new SortParam(LAST_NAME_SORT, sortType);
            sortParams[3] = new SortParam(FIRST_NAME_SORT, sortType);
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
        ArrayList commonFilters = new ArrayList();
        String value;
        FilterType type;
        FilterParam fp;

        value =  trf.getLastName();        
        type = convertFilterType(trf.getLastNameFilterType());
        fp = buildFilterParamSingleValue("LastName", value, type);
        if (fp != null)
            commonFilters.add(fp);
        
          
        value =  trf.getFirstName();        
        type = convertFilterType(trf.getFirstNameFilterType());
        fp = buildFilterParamSingleValue("FirstName", value, type);
        if (fp != null)
            commonFilters.add(fp);
        
        value =  trf.getOrganization();        
        type = convertFilterType(trf.getOrganizationFilterType());
        fp = buildFilterParamSingleValue("OrgNodeName", value, type);
        if (fp != null)
            commonFilters.add(fp);

        value =  trf.getFormFilterType();
        if (value.equals(FILTERTYPE_SHOWALL))
            value = null;      
        type = FilterType.EQUALS;
        fp = buildFilterParamSingleValue("ItemSetForm", value, type);
        if (fp != null)
            commonFilters.add(fp);
        
        String [] selectedAccommodations = trf.getSelectedAccommodations();
        List accommodationsFilters = new ArrayList();
        
        if (trf.getAccommodationFilterType().equals(STUDENTS_WITH_ACCOMMODATIONS)) {
            for (int i=0; i<selectedAccommodations.length; i++) {
                FilterParam filterParam = null;
                value = "T";
                type = FilterType.EQUALS;
                String[] fieldValues = new String[1];
                String accommodation = selectedAccommodations[i];
                if (selectedAccommodations[i].equals("Color/Font")) {
                    value = "true";
                    accommodation = "HasColorFontAccommodations";
                }
                fieldValues[0] = value;
                filterParam = new FilterParam(accommodation, fieldValues, type);
                
                if (filterParam != null)
                    accommodationsFilters.add(filterParam);
            }
        }
        else if (trf.getAccommodationFilterType().equals(STUDENTS_WITHOUT_ACCOMMODATIONS)) {

            String [] arg = new String[1];
            arg[0] = "T";
            accommodationsFilters.add(new FilterParam("ScreenReader", arg, FilterType.NOTEQUAL));
            accommodationsFilters.add(new FilterParam("Calculator", arg, FilterType.NOTEQUAL));
            accommodationsFilters.add(new FilterParam("TestPause", arg, FilterType.NOTEQUAL));
            accommodationsFilters.add(new FilterParam("UntimedTest", arg, FilterType.NOTEQUAL));
            String [] arg2 = new String[1];
            arg2[0] = "false";
            accommodationsFilters.add(new FilterParam("HasColorFontAccommodations", arg2, FilterType.EQUALS));
        }
        
        int index = 0;
        Iterator it = null;
        int totalCount = commonFilters.size() + accommodationsFilters.size();        
        FilterParams filter = null;
        if (totalCount > 0) {
            FilterParam[] filterParams = new FilterParam[totalCount];
            it = commonFilters.iterator();
            while (it.hasNext()) {
                filterParams[index]= (FilterParam) it.next();
                index++;
            }
            it = accommodationsFilters.iterator();
            while (it.hasNext()) {
                filterParams[index]= (FilterParam) it.next();
                index++;
            }            
            filter = new FilterParams();
            filter.setFilterParams(filterParams);        
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
   
    
    
    public static SortParams buildStudentSortParams(String sortName, String sortOrderBy)
    {
        SortParams sort = new SortParams();
                
        SortType sortType = sortOrderBy.equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;
        SortParam[] sortParams = new SortParam[2];
        
        sortParams[0] = new SortParam(sortName, sortType);
        
        String secondarySortName = STUDENT_DEFAULT_SORT_COLUMN;
        if (sortName.equals(STUDENT_DEFAULT_SORT_COLUMN))
             secondarySortName = LOGIN_NAME_SORT;
             
        sortParams[1] = new SortParam(secondarySortName, sortType);
        sort.setSortParams(sortParams);
        
        return sort;
    }
    
    public static SortParams buildItemSortParams(String sortName, String sortOrderBy)
    {
        SortParams sort = new SortParams();
                
        SortType sortType = sortOrderBy.equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;
        SortParam[] sortParams = new SortParam[2];
        
        sortParams[0] = new SortParam(sortName, sortType);
        
        String secondarySortName = TESTITEM_DEFAULT_SORT_COLUMN;
        if (sortName.equals(TESTITEM_DEFAULT_SORT_COLUMN))
             secondarySortName = TESTITEM_NUMBER_SORT;
             
        sortParams[1] = new SortParam(secondarySortName, sortType);
        sort.setSortParams(sortParams);
        
        return sort;
    }
    
    public static FilterParams buildFilterParams(String firstName, String middleName, String lastName,
            String loginId, String studentNumber, String grade, String gender, String scoringStatus)
	{
	ArrayList filters = new ArrayList();
	FilterParam fp;
	
	if ((firstName != null) && (firstName.length() > 0)) {
	fp = buildFilterParamSingleValue("FirstName", firstName, FilterType.EQUALS);
	if (fp != null)
	filters.add(fp);
	}
	
	if ((middleName != null) && (middleName.length() > 0)) {
	fp = buildFilterParamSingleValue("MiddleName", middleName, FilterType.EQUALS);
	if (fp != null)
	filters.add(fp);
	}
	
	if ((lastName != null) && (lastName.length() > 0)) {
	fp = buildFilterParamSingleValue("LastName", lastName, FilterType.EQUALS);
	if (fp != null)
	filters.add(fp);
	}
	
	if ((loginId != null) && (loginId.length() > 0)) {
	fp = buildFilterParamSingleValue("LoginId", loginId, FilterType.EQUALS);
	if (fp != null)
	filters.add(fp);
	}
	
	if ((studentNumber != null) && (studentNumber.length() > 0)) {
	fp = buildFilterParamSingleValue("StudentIdNumber", studentNumber, FilterType.EQUALS);
	if (fp != null)
	filters.add(fp);
	}
	
	if ((grade != null) && (grade.length() > 0) && (! grade.equalsIgnoreCase(FILTERTYPE_ANY_GRADE))) {
	fp = buildFilterParamSingleValue("Grade", grade, FilterType.EQUALS);
	if (fp != null)
	filters.add(fp);
	}

	
	
	if ((gender != null) && (gender.length() > 0) && (! gender.equalsIgnoreCase(FILTERTYPE_ANY_GENDER))) {
	fp = buildFilterParamSingleValue("Gender", gender, FilterType.EQUALS);
	if (fp != null)
	filters.add(fp);
	}
	
	if ((scoringStatus!= null) && (scoringStatus.length() > 0) && (!scoringStatus.equalsIgnoreCase(FILTERTYPE_ANY_SCORING_STATUS))) {
		fp = buildFilterParamSingleValue("ScoringStatus", scoringStatus, FilterType.EQUALS);
		if (fp != null)
		filters.add(fp);
		}
	
	FilterParams filter = null;
	if (filters.size() > 0) {
	filter = new FilterParams();
	filter.setFilterParams((FilterParam[])filters.toArray(new FilterParam[0]));        
	}
	
	return filter;
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

    
} 
