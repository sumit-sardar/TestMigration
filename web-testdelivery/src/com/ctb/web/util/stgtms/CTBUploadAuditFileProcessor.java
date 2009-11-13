package com.ctb.web.util.stgtms; 

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import com.ctb.control.testDelivery.StudentTestDeliveryAudit;
import java.math.BigInteger;
import noNamespace.AdsSvcDocument;
import noNamespace.AdsSvcDocument.AdsSvc;
import noNamespace.AdssvcRequestDocument;
import noNamespace.AdssvcRequestDocument.AdssvcRequest;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.WriteToAuditFile.Tsd;
import noNamespace.AdssvcResponseDocument;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.TmsStatus;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.WriteToAuditFile;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.WriteToAuditFile.Tsd.Status;
import noNamespace.StudentFeedbackDataDocument;

public class CTBUploadAuditFileProcessor 
{
    public static String processUploadAuditFile(String requestXML, StudentTestDeliveryAudit auditControl, String auditFile) throws XmlException {
        // construct document from XML string
        AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(requestXML);
        // construct output XML string from response document
        return auditControl.saveAuditFile(document, auditFile).toString();
    }
    
    public static String processWriteToAuditFile(String requestXML) throws XmlException {
        // construct document from XML string
        AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(requestXML);

        AdssvcRequest saveRequest = document.getAdssvcRequest();
        AdssvcResponseDocument response = AdssvcResponseDocument.Factory.newInstance();
        WriteToAuditFile saveResponse = response.addNewAdssvcResponse().addNewWriteToAuditFile();
        Tsd tsd = saveRequest.getWriteToAuditFile().getTsd();
        String lsid = tsd.getLsid();
        String testRosterId = lsid.substring(0, lsid.indexOf(":"));
        String itemSetId = tsd.getScid();
        saveResponse.addNewTsd();
        saveResponse.getTsd().setLsid(lsid);
        saveResponse.getTsd().setScid(String.valueOf(itemSetId));
        saveResponse.getTsd().setStatus(Status.OK);
        
        // construct output XML string from response document
        return response.toString();
    }
    
    
    public static String processGetStatus() throws XmlException {
        AdssvcResponseDocument response = AdssvcResponseDocument.Factory.newInstance();
        TmsStatus tmsStatus = response.addNewAdssvcResponse().addNewTmsStatus();
        tmsStatus.setStatus(AdssvcResponseDocument.AdssvcResponse.TmsStatus.Status.OK);
        // construct output XML string from response document
        return response.toString();
    }
    
    
} 

