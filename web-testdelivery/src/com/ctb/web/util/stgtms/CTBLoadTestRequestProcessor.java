package com.ctb.web.util.stgtms;

import noNamespace.TmssvcRequestDocument;
import org.apache.xmlbeans.XmlException;
import com.ctb.control.testDelivery.LoadTest;
import noNamespace.TmssvcRequestDocument.TmssvcRequest;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.RunLoadRequest;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.UploadStatisticsRequest;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.RunLoadResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.UploadStatisticsResponse;

public class CTBLoadTestRequestProcessor {
	
    public static String processLoadTestRequest(String requestXML, LoadTest loadTestControl) throws XmlException {
        // construct document from XML string
        TmssvcRequestDocument document = TmssvcRequestDocument.Factory.parse(requestXML);
        
        String response = loadTestControl.getLoadTestConfig(document).toString();
        
        System.out.println("Response : " + response);
        // construct output XML string from response document
        return response;
    }
    
    public static String processUploadStatisticsRequest(String requestXML, LoadTest loadTestControl) throws XmlException {
        // construct document from XML string
        TmssvcRequestDocument document = TmssvcRequestDocument.Factory.parse(requestXML);
        
        String response = loadTestControl.setLoadTestStatistics(document).toString();
        
        System.out.println("Response : " + response);
        // construct output XML string from response document
        return response;
    }

    public static String processUploadSystemInfoRequest(String requestXML, LoadTest loadTestControl) throws XmlException {
        // construct document from XML string
        TmssvcRequestDocument document = TmssvcRequestDocument.Factory.parse(requestXML);
        
        String response = loadTestControl.uploadSystemInfo(document).toString();
        
        System.out.println("Response : " + response);
        // construct output XML string from response document
        return response;
    }

}
