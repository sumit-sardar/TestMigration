
package utils;

import java.util.ArrayList;
import java.util.List;

import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.control.studentManagement.StudentManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;



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
    public static List<StudentProfileInformation> buildStudentList(ManageStudentData msData) 
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
    
    public static Integer getCompletedStudentCountForOrgNode(String userName, StudentManagement studentManagement, Integer orgNodeId) throws CTBBusinessException
    {    
        Integer studentCount = 0;
       	 studentCount = studentManagement.getCompletedStudentCountForOrgNode(userName, orgNodeId);
       	 return studentCount;
    }


	public static ManageStudentData getAllCompletedStudentForOrgNode(String userName,	StudentManagement studentManagement, Integer treeOrgNodeId, Integer productId) throws CTBBusinessException {
		
		ManageStudentData msData = studentManagement.getAllCompletedStudentForOrgNode(userName, treeOrgNodeId, productId);
		return msData;
	}

}
