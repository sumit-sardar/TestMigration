package com.ctb.xmlProcessing.subtest;

import java.io.*;
import java.util.ArrayList;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.ctb.common.tools.ADSConfig;
import com.ctb.common.tools.SystemException;
import com.ctb.content.layout.AssessmentLayoutProcessor;
import com.ctb.content.layout.ItemLayoutProcessor;
import com.stgglobal.client.CommonClient;
import com.stgglobal.client.CommonClientServiceLocator;


	/**
	 * @author Sudha_Manimaran
	 *
	 * TODO To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Style - Code Templates
	 */
	public class SubtestPublisher {
		
	    String statusOk = "ok";
	    String status_republish = "Subtest cannot be re-published as state is locked.";
	    String xml = "";
	    
		public Element publishSubtest(Element rootElement, Long itemSetId, ArrayList unicodeList, ADSConfig adsConfig){
				
			try
			{
			//Call transform
			Element	subtestXml = AssessmentLayoutProcessor.generate_ads_publish_request_ForSudha(rootElement, itemSetId, unicodeList);
			XMLOutputter xmloutput = new XMLOutputter();
			xml = xmloutput.outputString(subtestXml);
			
					
		    //Publish Subtest to ADS 
		    CommonClientServiceLocator clientLocator = new CommonClientServiceLocator(adsConfig.getWsClient());
			CommonClient client = clientLocator.getCommonClient();
	     	String responseStr = client.callUploadSubtest(xml);
	     	if (responseStr == null)
	    	{
	    		throw new SystemException("Error in Publishing Subtest. Response is null. " + " XML : "+ xml );
	    	}
	    	
	    	ByteArrayInputStream bais = new ByteArrayInputStream( responseStr.toString().getBytes());
	        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
	        org.jdom.Document itemDoc = saxBuilder.build( bais );
	        Element responseElm = itemDoc.getRootElement();
	    	
	    	Element response = ItemLayoutProcessor.extractSingleElement(".//response", responseElm);
	    	Attribute status = response.getAttribute("status");
	    	String statusStr = status.getValue();
	    	if (!statusStr.equals(statusOk))
	    	{
	    		Element msg = ItemLayoutProcessor.extractSingleElement(".//msg", response);
       			if (!(msg.getText()).equals(status_republish))
       			{
       				throw new SystemException("Error in Publishing Subtest. Status = "+statusStr + " Error message: " + msg.getText()+ " XML : "+ xml);
        		}
           	}
	  
			return subtestXml;
			}
			catch (Exception e)
			{
			throw new SystemException("Error in SubtestPublisher " + e.getMessage()+ " XML : "+ xml);
			}
			
			}
		 }
