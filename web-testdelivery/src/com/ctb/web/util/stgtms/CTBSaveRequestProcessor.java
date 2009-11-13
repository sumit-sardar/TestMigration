package com.ctb.web.util.stgtms; 

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import com.ctb.control.testDelivery.StudentTestData;
import java.math.BigInteger;
import noNamespace.AdsSvcDocument;
import noNamespace.AdsSvcDocument.AdsSvc;
import noNamespace.AdssvcRequestDocument;
import noNamespace.AdssvcResponseDocument;
import noNamespace.StudentFeedbackDataDocument;

public class CTBSaveRequestProcessor 
{
    public static String processSaveRequest(String requestXML, StudentTestData saveControl) throws XmlException {
        // construct document from XML string
        AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(requestXML);
        // construct output XML string from response document
        return saveControl.ctbSave(document).toString();
    }
    
    public static String processCompleteTutorial(String requestXML, StudentTestData saveControl) throws XmlException {
        // construct document from XML string
        AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(requestXML);
        // construct output XML string from response document
        return saveControl.ctbCompleteTutorial(document).toString();
    }
    
    
    public static String processFeedbackRequest(String requestXML, StudentTestData saveControl) throws XmlException {
        // construct document from XML string
        AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(requestXML);
        // construct output XML string from response document
        try {
            StudentFeedbackDataDocument response = saveControl.ctbFeedback(document);
            //System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            //e.printStackTrace();
            AdsSvcDocument responseDocument = AdsSvcDocument.Factory.newInstance();
            AdsSvc response = responseDocument.addNewAdsSvc();
            response.addNewResponse().setCode(new BigInteger("500"));
            response.getResponse().setMethod("get_feedback_data");
            response.getResponse().setStatus("ERROR");
            XmlObject error = XmlObject.Factory.parse("<![CDATA[A System Error has occurred.]]>");
            response.getResponse().addNewErrMsg().set(error);
            return responseDocument.toString();
        }
    }
} 
