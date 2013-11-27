/**
 * 
 */
package com.ctb.prism.web.handler;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import weblogic.utils.StringUtils;

import com.ctb.bean.testAdmin.User;
import com.ctb.prism.web.constant.PrismWebServiceConstant;
import com.ctb.prism.web.controller.ContentDetailsTO;
import com.ctb.prism.web.controller.ContentScoreDetailsTO;
import com.ctb.prism.web.controller.ContentScoreTO;
import com.ctb.prism.web.controller.CustHierarchyDetailsTO;
import com.ctb.prism.web.controller.DemoTO;
import com.ctb.prism.web.controller.ItemResponseTO;
import com.ctb.prism.web.controller.ItemResponsesDetailsTO;
import com.ctb.prism.web.controller.ObjectiveScoreDetailsTO;
import com.ctb.prism.web.controller.ObjectiveScoreTO;
import com.ctb.prism.web.controller.OrgDetailsTO;
import com.ctb.prism.web.controller.RosterDetailsTO;
import com.ctb.prism.web.controller.SampleWebservice;
import com.ctb.prism.web.controller.StudentBioTO;
import com.ctb.prism.web.controller.StudentDataLoadTO;
import com.ctb.prism.web.controller.StudentDemoTO;
import com.ctb.prism.web.controller.StudentDetailsTO;
import com.ctb.prism.web.controller.StudentListTO;
import com.ctb.prism.web.controller.StudentSurveyBioTO;
import com.ctb.prism.web.controller.SubtestAccommodationTO;
import com.ctb.prism.web.controller.SubtestAccommodationsTO;
import com.ctb.prism.web.controller.SurveyBioTO;
import com.ctb.prism.web.dbutility.PrismWebServiceDBUtility;
import com.ctb.util.HMACQueryStringEncrypter;
import com.ctb.util.OASLogger;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.thoughtworks.xstream.XStream;

/**
 * @author Ibrahim Alatoum
 * 
 */
public class PrismSOAPHandler implements SOAPHandler<SOAPMessageContext> {

	private String ssoParams = null;
	
	public PrismSOAPHandler (String _ssoParams){
		ssoParams = _ssoParams;
	}
	
	public boolean handleMessage(SOAPMessageContext messageContext)
	{
		System.out.println("Client : handleMessage().");
		SOAPMessage msg = messageContext.getMessage();
		if ((Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))
		{
			try
			{
				SOAPMessage soapMsg = messageContext.getMessage();
				SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnv.addHeader();
				
				//if no header, add one
				if (soapHeader == null)
				{
					soapHeader = soapEnv.addHeader();
				}
				
				QName qname = new QName("http://controller.web.prism.ctb.com/", "SSOParams");
				SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(qname);
				soapHeaderElement.setActor(SOAPConstants.URI_SOAP_ACTOR_NEXT);
				soapHeaderElement.addTextNode(ssoParams);
				soapMsg.saveChanges();
				
				//soapMsg.writeTo(System.out);
				StringBuffer sb = new StringBuffer();
				ByteArrayOutputStream ba = new ByteArrayOutputStream();
				soapMsg.writeTo(ba);
				sb.append("\nMessage Desc:"+ba.toString());
				sb.append("\n");
				System.out.println(sb.toString());
				OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismSOAPHandler.handleMessage : Prism Web Service SOAP message : " + sb.toString());
			}
			catch (SOAPException e)
			{
				System.out.println(e);
				return false;
			}
			catch (Exception e)
			{
				System.out.println(e);
				return false;
			}
		}
		
		//continue other handler chain
		return true;
	}
	
	public boolean handleFault(SOAPMessageContext messageContext)
	{
		System.out.println("Client : handleFault().");
		return true;
	}
	
	public void close(MessageContext messageContext)
	{
		System.out.println("Client : close().");
	}
	
	public Set<QName> getHeaders()
	{
		System.out.println("Client : getHeaders().");
		/*
		Set headers = new HashSet();
		headers.add(new QName("https://", "SSOParamsHeader"));
		return headers;
		*/
		return null;
	}
}
