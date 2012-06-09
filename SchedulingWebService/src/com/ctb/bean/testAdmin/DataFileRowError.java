package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

public class DataFileRowError extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private  Integer errorCount;
    
    private String errorType;
    
    private Integer columnIndex;
    
    private String columnName;
    
    
    public  String getColumnName() {
			return columnName;
    }

    public  void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public  Integer getErrorCount() {
			return errorCount;
    }

    public  void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }
    
    public String getErrorType() {
			return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    } 
    
    public Integer getColumnIndex() {
			return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

} 
