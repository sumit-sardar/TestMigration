package utils; 

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.ManageBulkStudentData;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.control.studentManagement.StudentManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;
import dto.StudentProfileInformation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentSearchUtils 
{ 
    private static int MAX_ITEMS = 10;
    
    /**
     * getTopOrgNodeIdsForUser
     */    
    public static Integer[] getTopOrgNodeIdsForUser(StudentManagement studentManagement, String userName)
    {    
        Integer[] orgNodeIds = null;
        /*
        try {    
            orgNodeIds = studentManagement.getTopOrgNodeIdsForUser(userName);   
        }      
        catch (CTBBusinessException e) {
            e.printStackTrace();
        }
        */
        return orgNodeIds;
    }
    
    /**
     * findStudentSuggestions
     */    
    public static String findStudentSuggestions(StudentManagement studentManagement, String userName, String controlId, String lookup)
    {   
        String[] results = null;
        String suggestions = "";
        
        try {    
            //results = studentManagement.findStudentSuggestions(userName, controlId, lookup);
            if (results != null) {
                int count = 0;
                for (int i=0 ; ((i<results.length) && (count < MAX_ITEMS))  ; i++) {
                    if (count>0) {
                        suggestions += ",";
                    }
                    suggestions += (results[i].trim());
                    count++;
                }
            }                
        }
        catch (Exception be) {
            be.printStackTrace();
        }        
        return suggestions;
    }
    
    /**
     * buildStudentList
     */    
    public static List buildStudentList(ManageStudentData msData) 
    {
        ArrayList studentList = new ArrayList();
        if (msData != null) {
            ManageStudent[] students = msData.getManageStudents();
            for (int i=0 ; i<students.length ; i++) {
                ManageStudent student = (ManageStudent)students[i];
                if (student != null) {
                    StudentProfileInformation studentDetail = new StudentProfileInformation(student);
                    studentList.add(studentDetail);
                }
            }
        }
        return studentList;
    }
    /**
     * buildStudentList
     */    
    public static String buildStudentListString(ManageStudentData msData) 
    {
        String studentList = "";
        if (msData != null) {
            ManageStudent[] students = msData.getManageStudents();
            for (int i=0 ; i<students.length ; i++) {
                ManageStudent student = (ManageStudent)students[i];
                if (student != null) {
                	if(i == 0)
                		studentList = studentList + student.getId();
                	else
                		studentList = studentList + "," + student.getId();
                }
            }
        }
        return studentList;
    }
    
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
     * searchAllStudentsAtAndBelow
     */    
    public static ManageStudentData searchAllStudentsAtAndBelow(String userName, StudentManagement studentManagement, PageParams page, SortParams sort)
    {   
        ManageStudentData msData = null;
        try {    
            msData = studentManagement.findStudentsAtAndBelowTopOrgNodesWithDynamicSQL(userName, null, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return msData;
    }

    /**
     * searchStudentsByProfile
     */    
    public static ManageStudentData searchStudentsByProfile(String userName, StudentManagement studentManagement, FilterParams filter, PageParams page, SortParams sort)
    {   
        ManageStudentData msData = null;
        try {    
            msData = studentManagement.findStudentsAtAndBelowTopOrgNodesWithDynamicSQL(userName, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return msData;
    }
    
    /**
     * searchStudentsByOrgNode
     */    
    public static ManageStudentData searchStudentsByOrgNode(String userName, StudentManagement studentManagement, Integer orgNodeId,
                                                           FilterParams filter, PageParams page, SortParams sort)
    {    
        ManageStudentData msData = null;
        try {    
            msData = studentManagement.findStudentsForOrgNode(userName, orgNodeId, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return msData;
    }

    /**
     * getStudentProfileInformation
     */    
    public static StudentProfileInformation getStudentProfileInformation(StudentManagement studentManagement, String userName, Integer studentId)
    {    
        StudentProfileInformation studentProfileInfo = new StudentProfileInformation();         
        try {    
            ManageStudent student = studentManagement.getManageStudent(userName, studentId);   
            studentProfileInfo = new StudentProfileInformation(student);   
        }      
        catch (Exception e) {
        	studentProfileInfo = null;   //Changes for Defect 60478
            e.printStackTrace();
        }
        return studentProfileInfo;
    }
    
    /**
     * searchBulkStudentsByOrgNode Bulk accommodation
     */    
    public static ManageBulkStudentData searchBulkStudentsByOrgNode(String userName, StudentManagement studentManagement, Integer orgNodeId,
                                                           FilterParams filter,FilterParams demoFilter, PageParams page, SortParams sort)
    {    
    	ManageBulkStudentData msData = null;
        try {    
            msData = studentManagement.findBulkStudentsForOrgNode(userName, orgNodeId, filter,demoFilter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return msData;
    }
    
} 
