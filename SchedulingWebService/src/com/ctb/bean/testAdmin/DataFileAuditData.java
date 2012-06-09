package com.ctb.bean.testAdmin; 
import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

public class DataFileAuditData extends CTBBeanData
{ 
    static final long serialVersionUID = 1L;
    private DataFileAudit[] dataFileAudit;
    /**
    * @return the dataFileAudit
    */
    public DataFileAudit[] getDataFileAudit() {
        
        CTBBean [] beans = this.getBeans();
		DataFileAudit[] result = new DataFileAudit[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (DataFileAudit) beans[i];   
        return result;
    }
    /**
    * @param dataFileAudit the dataFileAudit to set
    */
    public void setDataFileAudit(DataFileAudit[] dataFileAudit,Integer pageSize) {
    
        this.setBeans(dataFileAudit, pageSize);
    }
} 
