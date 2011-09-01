package com.ctb.control.testDelivery; 


import java.sql.SQLException;

import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import com.ctb.exception.CTBBusinessException;

import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface StudentLogin 
{ 

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.TmssvcResponseDocument ctbLogin(noNamespace.TmssvcRequestDocument document);
    
    public byte[] studentMusicFile(Integer musicId) throws CTBBusinessException, SQLException;
} 
