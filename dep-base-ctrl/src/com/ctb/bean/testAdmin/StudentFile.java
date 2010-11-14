package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

public class StudentFile extends CTBBean{

    static final long serialVersionUID = 1L;
    private StudentFileRow[] studentFileRows;
    
    public StudentFileRow[] getStudentFileRows() {
			return studentFileRows;
    }

    public void setStudentFileRows(StudentFileRow[] studentFileRows) {
        this.studentFileRows = studentFileRows;
    }
 
} 
