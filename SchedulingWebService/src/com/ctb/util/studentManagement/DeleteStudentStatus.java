package com.ctb.util.studentManagement; 

/** 
 * Data representing status returned to caller of deleteStudent()
 * @author John_Wang
 */ 

public class DeleteStudentStatus extends Status
{ 
    public DeleteStudentStatus(String statusCode) {
        super(statusCode);    
    }
    
    public static final DeleteStudentStatus DELETED = new DeleteStudentStatus("DELETED");
    public static final DeleteStudentStatus INACTIVATED = new DeleteStudentStatus("INACTIVATED");
    
} 
