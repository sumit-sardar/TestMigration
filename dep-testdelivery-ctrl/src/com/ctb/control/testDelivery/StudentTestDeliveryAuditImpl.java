package com.ctb.control.testDelivery;
import com.bea.control.*;
import com.ctb.bean.testDelivery.studentTestData.AuditFile;
import com.ctb.exception.testDelivery.TestDeliveryAuditException;
import com.ctb.util.OASLogger;
import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.logging.Level;

import noNamespace.AdssvcRequestDocument;
import noNamespace.AdssvcRequestDocument.AdssvcRequest;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.UploadAuditFile.Tsd;
import noNamespace.AdssvcResponseDocument;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.UploadAuditFile;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.UploadAuditFile.Tsd.Status;
import org.apache.beehive.controls.api.bean.ControlImplementation;

/**
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation()
public class StudentTestDeliveryAuditImpl implements StudentTestDeliveryAudit, Serializable
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.TestDeliveryAudit auditor;

    static final long serialVersionUID = 1L;

    /**
     * @common:operation
     */
    public void saveAudit(int testRosterId, int itemSetId, String auditFileString) throws TestDeliveryAuditException
    {
        try {
            auditor.storeTestDeliveryAuditFile(testRosterId, itemSetId, auditFileString);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new TestDeliveryAuditException();
        }
        
    }
    
    /**
     * @common:operation
     */
    public AdssvcResponseDocument saveAuditFile(AdssvcRequestDocument document, String auditFileString)
    {
        AdssvcRequest saveRequest = document.getAdssvcRequest();
        //OASLogger.getLogger("TestDelivery").debug(saveRequest.toString());
        AdssvcResponseDocument response = AdssvcResponseDocument.Factory.newInstance();
        UploadAuditFile saveResponse = response.addNewAdssvcResponse().addNewUploadAuditFile();
        Tsd tsd = saveRequest.getUploadAuditFile().getTsd();
        String lsid = tsd.getLsid();
        String testRosterId = lsid.substring(0, lsid.indexOf(":"));
        String itemSetId = tsd.getScid();
        saveResponse.addNewTsd();
        saveResponse.getTsd().setLsid(lsid);
        saveResponse.getTsd().setScid(String.valueOf(itemSetId));
        saveResponse.getTsd().setStatus(Status.OK);
        try {
            auditor.storeTestDeliveryAuditFile(Integer.parseInt(testRosterId), Integer.parseInt(itemSetId), auditFileString);
        } catch (Exception e) {
            e.printStackTrace();
            handleError(tsd, saveResponse.getTsd(), Status.OTHER_ERROR, "other_error");
        }
        
        OASLogger.getLogger("TestDelivery").debug(saveResponse.getTsd().toString());
           
        return response;
        
    }
    
    private void handleError(Tsd requestTsd, noNamespace.AdssvcResponseDocument.AdssvcResponse.UploadAuditFile.Tsd errorTsd, noNamespace.AdssvcResponseDocument.AdssvcResponse.UploadAuditFile.Tsd.Status.Enum status, String message) {
        errorTsd.setStatus(status);
        errorTsd.addNewError();
        errorTsd.getError().setMethod("upload_audit_file");
        errorTsd.getError().setStatus(message);
        errorTsd.getError().setErrorElement(requestTsd.toString());
    }
    
    
} 
