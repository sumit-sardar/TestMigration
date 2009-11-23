package com.ctb.web.util.stgtms; 

import org.apache.xmlbeans.XmlException;
import com.ctb.control.testDelivery.AssessmentDelivery;
import noNamespace.AdssvcRequestDocument;

public class CTBAssessmentDeliveryProcessor 
{ 
    public static String processGetSubtest(String requestXML, AssessmentDelivery adsControl) throws XmlException {
        // construct document from XML string
        AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(requestXML);
        // construct output XML string from response document
        return adsControl.getSubtest(document).toString();
    }

    public static String processDownloadItem(String requestXML, AssessmentDelivery adsControl) throws XmlException {
        // construct document from XML string
        AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(requestXML);
        // construct output XML string from response document
        return adsControl.downloadItem(document).toString();
    }

    
} 
