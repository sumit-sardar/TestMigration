package com.ctb.web.util.stgtms; 

import org.apache.xmlbeans.XmlException;
import com.ctb.control.testDelivery.StudentLogin;
import com.ctb.web.util.TMSConstants;
import java.math.BigInteger;
import noNamespace.TmssvcRequestDocument;
import noNamespace.TmssvcRequestDocument.TmssvcRequest;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.LoginRequest;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;

public class CTBLoginRequestProcessor 
{
    
    public static String processLoginRequest(String requestXML, StudentLogin loginControl, String sdsId) throws XmlException {
        // construct document from XML string
        TmssvcRequestDocument document = TmssvcRequestDocument.Factory.parse(requestXML);
        
        // set remote address as SDS id
        document.getTmssvcRequest().getLoginRequest().setSdsId(sdsId);
        
        // construct output XML string from response document
        return loginControl.ctbLogin(document).toString();
    }
} 
