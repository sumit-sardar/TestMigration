package com.ctb.bean.testDelivery.studentTestData; 

import java.sql.Clob;

public class AuditFile 
{ 
    private int testDeliveryAuditId;
    private Clob auditFile;
    
    public int getTestDeliveryAuditId() {
        return this.testDeliveryAuditId;
    }
    
    public void setTestDeliveryAuditId(int testDeliveryAuditId) {
        this.testDeliveryAuditId = testDeliveryAuditId;
    }
    
    public Clob getAuditFile() {
        return this.auditFile;
    }
    
    public void setAuditFile(Clob auditFile) {
        this.auditFile = auditFile;
    }
} 
