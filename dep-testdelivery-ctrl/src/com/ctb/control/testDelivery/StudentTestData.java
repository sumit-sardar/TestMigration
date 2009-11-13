package com.ctb.control.testDelivery; 


import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface StudentTestData 
{ 

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.AdssvcResponseDocument save(noNamespace.AdssvcRequestDocument document, boolean responseQueue);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.StudentFeedbackDataDocument feedback(noNamespace.AdssvcRequestDocument document) throws java.lang.Exception;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.StudentFeedbackDataDocument ctbFeedback(noNamespace.AdssvcRequestDocument document) throws java.lang.Exception;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.AdssvcResponseDocument ctbSave(noNamespace.AdssvcRequestDocument document);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    noNamespace.AdssvcResponseDocument ctbCompleteTutorial(noNamespace.AdssvcRequestDocument document);
} 
