package com.ctb.control.testDelivery; 


import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface AssessmentDelivery 
{ 

    //@TransactionAttribute(TransactionAttributeType.REQUIRED)
    /* Transaction is commented to remove this error : Connection has already been created in this tx context for pool named oasDataSource. Illegal attempt to create connection 
     * from another pool: adsDataSource :*/ 
	noNamespace.AdssvcResponseDocument downloadItem(noNamespace.AdssvcRequestDocument document);

    //@TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.AdssvcResponseDocument getSubtest(noNamespace.AdssvcRequestDocument document);
} 
