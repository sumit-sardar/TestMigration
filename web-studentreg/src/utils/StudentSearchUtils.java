
package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.control.studentManagement.StudentManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;

import dto.StudentProfileInformation;



public class StudentSearchUtils {

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
                	//System.out.println("==>1"+student.getTestSessionName());
                    StudentProfileInformation studentDetail = new StudentProfileInformation(student);
                   // System.out.println("==>2"+studentDetail.getTestSessionName());
                    studentList.add(studentDetail);
                }
            }
        }
        return studentList;
    }
    
    
    /**
     * searchAllStudentsAtAndBelow
     */    
    public static ManageStudentData searchAllStudentsAtAndBelow(String userName, StudentManagement studentManagement,Integer productId, PageParams page, SortParams sort)
    {   
        ManageStudentData msData = null;
        try {    
            msData = studentManagement.findStudentsAtAndBelowTopOrgNodesWithDynamicSQLForScoring(userName,productId, null, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return msData;
    }

    /**
     * searchStudentsByProfile
     */    
    public static ManageStudentData searchStudentsByProfile(String userName, StudentManagement studentManagement,Integer productId, FilterParams filter, PageParams page, SortParams sort)
    {   
        ManageStudentData msData = null;
        
        try {    
            msData = studentManagement.findStudentsAtAndBelowTopOrgNodesWithDynamicSQLForScoring(userName, productId ,filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return msData;
    }
    
    
    
    
  
    
    
    
    /**
     * student count for org node
     */
     public static Integer getStudentsCountForOrgNode(String userName, StudentManagement studentManagement, Integer orgNodeId)
    {    
        Integer studentCount = 0;
        try {    
       	 studentCount = studentManagement.getStudentsCountForOrgNode(userName, orgNodeId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return studentCount;
    }
     
     /**
      * searchStudentsByOrgNode with minimal information
      */
      public static ManageStudentData getStudentsMinimalInfoForScoring(String userName, StudentManagement studentManagement, Integer orgNodeId,
                                                            FilterParams filter, PageParams page, SortParams sort)
     {    
         ManageStudentData msData = null;
         try {    
             msData = studentManagement.getStudentsMinimalInfoForScoring(userName, orgNodeId, sort);
         }
         catch (CTBBusinessException be) {
             be.printStackTrace();
         }        
         return msData;
     }
      
      /**
       * buildStudentList
       * @param accomodationMap 
       */    
      public static List<StudentProfileInformation> buildStudentListForScoring(ManageStudentData msData) 
      {
          ArrayList<StudentProfileInformation> studentList = new ArrayList<StudentProfileInformation>();
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


	public static ArrayList<StudentProfileInformation> getStudentsForSelectedOrgNode(	String userName, StudentManagement studentManagement,Integer orgNodeId, Integer testAdminId) {
		 ManageStudentData msData = null;
		 ArrayList<StudentProfileInformation> studentList = new ArrayList<StudentProfileInformation>();
         try {    
             msData = studentManagement.getStudentsForSelectedOrgNode(userName, orgNodeId, testAdminId);
             if( msData!=null){
            	 ManageStudent[] students = msData.getManageStudents();
            	 for (ManageStudent student : students) {
            		 if (student != null) {
                         StudentProfileInformation studentDetail = new StudentProfileInformation();
                         studentDetail.setStudentId(student.getId());
                         studentDetail.setGrade(student.getGrade());
                         String gender =student.getGender();
                         if(gender == null) student.setGender("Unknown");
                         else if ( gender.equalsIgnoreCase("M"))student.setGender("Male");
                         else if ( gender.equalsIgnoreCase("F"))student.setGender("Female");
                         else student.setGender("Unknown");
                         studentDetail.setGender(student.getGender());
                         studentDetail.setOrgNodeNamesStr(student.getOrganizationNames());
                         studentDetail.setIsSessionStudent(student.getIsSessionStudent());
                         studentDetail.setUserName(student.getLoginId());
                         studentDetail.setStudentName(student.getStudentName());
                         studentDetail.setStudentNumber(student.getStudentIdNumber());
                         studentDetail.setOrgNodeNamesList(student.getOrgNameList());
                         studentDetail.setOrgNodeIdList(student.getOrgIdList());
                         studentDetail.setOrgIdNameList(student.getOrgIdNameList());
                         
                         
                         studentList.add(studentDetail);                  		
                     }
					
				}
            	 
             }
         }
         catch (CTBBusinessException be) {
             be.printStackTrace();
         }        
         return studentList;
	}
	
	
	  public static ManageStudentData searchAllStudentsAtAndBelowByOrgNode(String userName, StudentManagement studentManagement,Integer orgNodeId)
	    {   
	        ManageStudentData msData = null;
	        try {    
	            msData =studentManagement.getStudentsAtAndBelowForSelectedOrgNode(userName, orgNodeId );
	        }
	        catch (CTBBusinessException be) {
	            be.printStackTrace();
	        }        
	        return msData;
	    }


	public static List<StudentProfileInformation> buildStudentListForForReportingGrid(ManageStudentData msData) {
		
		 ArrayList<StudentProfileInformation> studentList = new ArrayList<StudentProfileInformation>();
         try {    
           
             if( msData!=null){
            	 ManageStudent[] students = msData.getManageStudents();
            	 for (ManageStudent student : students) {
            		 if (student != null) {
                         StudentProfileInformation studentDetail = new StudentProfileInformation();
                         studentDetail.setStudentId(student.getId());
                         studentDetail.setGrade(student.getGrade());
                         String gender =student.getGender();
                         if(gender == null) student.setGender("Unknown");
                         else if ( gender.equalsIgnoreCase("M"))student.setGender("Male");
                         else if ( gender.equalsIgnoreCase("F"))student.setGender("Female");
                         else student.setGender("Unknown");
                         studentDetail.setGender(student.getGender());
                         studentDetail.setOrgNodeNamesStr(student.getOrganizationNames());
                         studentDetail.setIsSessionStudent(student.getIsSessionStudent());
                         studentDetail.setUserName(student.getLoginId());
                         studentDetail.setStudentName(student.getStudentName());
                         studentDetail.setStudentNumber(student.getStudentIdNumber());
                         studentDetail.setOrgNodeNamesList(student.getOrgNameList());
                         studentDetail.setOrgNodeIdList(student.getOrgIdList());
                         studentDetail.setOrgIdNameList(student.getOrgIdNameList());
                         studentList.add(studentDetail);                  		
                     }
					
				}
            	 
             }
         }
         catch (Exception e) {
             e.printStackTrace();
         }        
         return studentList;
	}
}
