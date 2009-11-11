package com.ctb.control.scoring; 


import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface ScoringControl 
{ 

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    java.lang.String scoreCompletedRostersForAdmin(java.lang.Integer testAdminId);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    java.lang.String scoreCompletedRostersForProduct(java.lang.Integer productId);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    java.lang.String invokeScoring(java.lang.String testRosterId);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    java.lang.String scoreCompletedRostersForCustomer(java.lang.Integer customerId);
} 
