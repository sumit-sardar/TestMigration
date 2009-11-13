package com.ctb.control.testDelivery; 


import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface StudentLogin 
{ 

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.TmssvcResponseDocument login(noNamespace.TmssvcRequestDocument document);


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.TmssvcResponseDocument ctbLogin(noNamespace.TmssvcRequestDocument document);
} 
