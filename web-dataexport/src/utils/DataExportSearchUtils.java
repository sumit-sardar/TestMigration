
package utils;

import java.util.ArrayList;
import java.util.List;

import com.ctb.bean.dataExportManagement.ManageJob;
import com.ctb.bean.dataExportManagement.ManageJobData;
import com.ctb.bean.dataExportManagement.ManageStudent;
import com.ctb.bean.dataExportManagement.ManageStudentData;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.ManageTestSession;
import com.ctb.bean.testAdmin.ManageTestSessionData;
import com.ctb.control.dataExportManagement.DataExportManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;



public class DataExportSearchUtils {
	
    public static final String FILTERTYPE_COMPLETED = "Completed";
    public static final String FILTERTYPE_INCOMPLETE = "Incomplete";
    public static final String FILTERTYPE_INPROGRESS = "In Progress";
    public static final String FILTERTYPE_NOTTAKEN = "Not taken";
    public static final String FILTERTYPE_SCHEDULED = "Scheduled";
    public static final String FILTERTYPE_STUDENTSTOP = "Student stop";
    public static final String FILTERTYPE_SYSTEMSTOP = "System stop";
    public static final String FILTERTYPE_TESTLOCKED = "Test locked";
    public static final String FILTERTYPE_TESTABANDONED = "Session abandoned";
    public static final String FILTERTYPE_STUDENTPAUSE = "Student pause";
    
    
 
    public static final String STATUS_COMPLETED = "CO";
    public static final String STATUS_INCOMPLETE = "IC";
    public static final String STATUS_INPROGRESS = "IP";
    public static final String STATUS_NOTTAKEN = "NT";
    public static final String STATUS_SCHEDULED = "SC";
    public static final String STATUS_STUDENTSTOP = "IS";
    public static final String STATUS_SYSTEMSTOP = "IN";
    public static final String STATUS_TESTLOCKED = "CL";
    public static final String STATUS_TESTABANDONED = "AB";
    public static final String STATUS_STUDENTPAUSE = "SP";

	 /**
     * buildStudentPagerSummary
     */    
    public static PagerSummary buildStudentPagerSummary(ManageStudentData msData, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);                
        pagerSummary.setTotalObjects(msData.getTotalCount());
        pagerSummary.setTotalPages(msData.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(msData.getFilteredCount());                
        return pagerSummary;
    } 
    
    
    /**
     * buildTestSessionPagerSummary
     */    
    public static PagerSummary buildTestSessionPagerSummary(ManageTestSessionData mstData, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);                
        pagerSummary.setTotalObjects(mstData.getTotalCount());
        pagerSummary.setTotalPages(mstData.getFilteredPages());
       // pagerSummary.setTotalFilteredObjects(mstData.getFilteredCount());                
        return pagerSummary;
    } 
    
    
    
    public static List buildExportStudentList(ManageStudentData msData) 
    {
        ArrayList studentList = new ArrayList();
        if (msData != null) {
            ManageStudent[] students = msData.getManageStudents();
            for (int i=0 ; i<students.length ; i++) {
                ManageStudent student = (ManageStudent)students[i];
                if (student != null) {
                	//System.out.println("==>1"+student.getTestSessionName());
                   // StudentProfileInformation studentDetail = new StudentProfileInformation(student);
                   // System.out.println("==>2"+studentDetail.getTestSessionName());
                	if(student.getTestCompletionStatus() != null)
                		student.setTestCompletionStatus(testStatus_CodeToString(student.getTestCompletionStatus()));
                    studentList.add(student);
                }
            }
        }
        return studentList;
    }
  
    public static List buildTestSessionsWithStudentToBeExportedList(ManageTestSessionData mstData) 
    {
        ArrayList testSessionList = new ArrayList();
        if (mstData != null) {
        	ManageTestSession[] testSessions = mstData.getManageTestSessions();
            for (int i=0 ; i<testSessions.length ; i++) {
            	ManageTestSession testSession = (ManageTestSession)testSessions[i];
                if (testSession != null) {
                	testSessionList.add(testSession);
                }
            }
        }
        return testSessionList;
    }
    public static PagerSummary buildJobPagerSummary(ManageJobData msData, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);                
        pagerSummary.setTotalObjects(msData.getTotalCount());
        pagerSummary.setTotalPages(msData.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(msData.getFilteredCount());                
        return pagerSummary;
    }  
    
    public static List buildExportJobList(ManageJobData msData) 
    {
        ArrayList jobList = new ArrayList();
        if (msData != null) {
            ManageJob[] jobs = msData.getManageJobs();
            for (int i=0 ; i<jobs.length ; i++) {
                ManageJob job = (ManageJob)jobs[i];
                if (job != null) {
                	jobList.add(job);
                }
            }
        }
        return jobList;
    }
   
    public static Integer getSubmitJobIdAndStartExport(DataExportManagement deManagement,Integer userId,Integer studentCount){
    	Integer jobId = null;
	        try {    
	        	 jobId = deManagement.getSubmitJobIdAndStartExport(userId,studentCount);
	        }
	        catch (CTBBusinessException be) {
	            be.printStackTrace();
	        } 
	        return jobId;
	}
	
	
	public static ManageTestSessionData getTestSessionsWithUnexportedStudents(DataExportManagement deManagement, Integer customerId, FilterParams filter, PageParams page,
			SortParams sort) {

		ManageTestSessionData mstData = null;
	        
	        try {    
	        	mstData = deManagement.getTestSessionsWithUnexportedStudents(customerId, filter, page, sort);
	        }
	        catch (CTBBusinessException be) {
	            be.printStackTrace();
	        }        
	        return mstData;
	}


	public static ManageStudentData getIncompleteRosterUnexportedStudents(DataExportManagement deManagement, Integer customerId, FilterParams filter, PageParams page,
			SortParams sort) {

		 ManageStudentData msData = null;
	        
	        try {    
	            msData = deManagement.getIncompleteRosterUnexportedStudents(customerId, filter, page, sort);
	        }
	        catch (CTBBusinessException be) {
	            be.printStackTrace();
	        }        
	        return msData;
	}
	

	public static ManageStudentData getAllUnscoredUnexportedStudentsDetail(List toBeExportedStudentRosterList,DataExportManagement deManagement, Integer customerId, FilterParams filter, PageParams page,
			SortParams sort) {

		 ManageStudentData msData = null;
	        
	        try {    
	            msData = deManagement.getAllUnscoredUnexportedStudentsDetail(toBeExportedStudentRosterList,customerId, filter, page, sort);
	        }
	        catch (CTBBusinessException be) {
	            be.printStackTrace();
	        }        
	        return msData;
	}
	
	public static ManageJobData getDataExportJobStatus(DataExportManagement deManagement, Integer userId, FilterParams filter, PageParams page,
			SortParams sort) {

		ManageJobData msData = null;
	        
	        try {    
	            msData = deManagement.getDataExportJobStatus(userId, filter, page, sort);
	        }
	        catch (CTBBusinessException be) {
	            be.printStackTrace();
	        }        
	        return msData;
	}
	
	
	public static String testStatus_CodeToString(String src)
    {
        if (src == null) {
            return "";
        }
        if (src.equals("SC")) {
            return FILTERTYPE_SCHEDULED;
        }
        if (src.equals("CO")) {
            return FILTERTYPE_COMPLETED;
        }
        if (src.equals("IN")) {
            return FILTERTYPE_SYSTEMSTOP;
        }
        if (src.equals("NT")) {
            return FILTERTYPE_NOTTAKEN;
        }
        if (src.equals("IP")) {
            return FILTERTYPE_INPROGRESS;
        }
        if (src.equals("IC")) {
            return FILTERTYPE_INCOMPLETE;
        }
        if (src.equals("IS")) {
            return FILTERTYPE_STUDENTSTOP;
        }
        if (src.equals("CL")) {
            return FILTERTYPE_TESTLOCKED;
        }
        if (src.equals("OC")) {
            return FILTERTYPE_COMPLETED;
        }
        if (src.equals("AB")) {
            return FILTERTYPE_TESTABANDONED;
        }
        if (src.equals("SP")) {
            return FILTERTYPE_STUDENTPAUSE;
        }
        return null;
    }
    public static String testStatus_StringToCode(String src)
    {
        if (src == null) {
            return "";
        }
        if (src.equals(FILTERTYPE_SCHEDULED)) {
            return "SC";
        }
        if (src.equals(FILTERTYPE_COMPLETED)) {
            return "CO";
        }
        if (src.equals(FILTERTYPE_SYSTEMSTOP)) {
            return "IN";
        }
        if (src.equals(FILTERTYPE_NOTTAKEN)) {
            return "NT";
        }
        if (src.equals(FILTERTYPE_INPROGRESS)) {
            return "IP";
        }
        if (src.equals(FILTERTYPE_INCOMPLETE)) {
            return "IC";
        }
        if (src.equals(FILTERTYPE_STUDENTSTOP)) {
            return "IS";
        }
        if (src.equals(FILTERTYPE_TESTLOCKED)) {
            return "CL";
        }
        if (src.equals(FILTERTYPE_COMPLETED)) {
            return "OC";
        }
        if (src.equals(FILTERTYPE_TESTABANDONED)) {
            return "AB";
        }
        if (src.equals(FILTERTYPE_STUDENTPAUSE)) {
            return "SP";
        }
        return null;
    }
}
