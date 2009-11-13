package com.ctb.control.testDelivery; 


import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface StudentTestDeliveryAudit 
{ 

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    void saveAudit(int testRosterId, int itemSetId, java.lang.String auditFileString) throws com.ctb.exception.testDelivery.TestDeliveryAuditException;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.AdssvcResponseDocument saveAuditFile(noNamespace.AdssvcRequestDocument document, java.lang.String auditFileString);
} 
