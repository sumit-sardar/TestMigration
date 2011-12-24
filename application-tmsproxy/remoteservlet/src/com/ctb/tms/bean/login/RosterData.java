package com.ctb.tms.bean.login;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import noNamespace.AdssvcRequestDocument;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData;
import noNamespace.BaseType;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ast;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Ov;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Rv;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.ctb.tms.exception.testDelivery.AuthenticationFailureException;
import com.ctb.tms.exception.testDelivery.KeyEnteredResponsesException;
import com.ctb.tms.exception.testDelivery.OutsideTestWindowException;
import com.ctb.tms.exception.testDelivery.TestSessionCompletedException;
import com.ctb.tms.exception.testDelivery.TestSessionNotScheduledException;
import com.ctb.tms.util.Constants;
import com.ctb.tms.util.DateUtils;

public class RosterData extends ReplicationObject {
	String document;
	AuthenticationData authData;
	
	static Logger logger = Logger.getLogger(RosterData.class);
	
	public RosterData() {
		
	}
	
	public TmssvcResponseDocument getDocument() {
		XmlOptions xmlOptions = new XmlOptions(); 
		xmlOptions.setCharacterEncoding("UTF-8");
        xmlOptions.setUnsynchronized();
		try {
			return TmssvcResponseDocument.Factory.parse(this.document, xmlOptions);
		} catch (XmlException e) {
			return null;
		}
	}
	
	public TmssvcResponseDocument getLoginDocument() {
		XmlOptions xmlOptions = new XmlOptions(); 
		xmlOptions.setCharacterEncoding("UTF-8");
        xmlOptions.setUnsynchronized();
		TmssvcResponseDocument response = getDocument();
		try {
			response.getTmssvcResponse().getLoginResponse().setRestartNumber(BigInteger.valueOf(this.authData.getRestartNumber()));
			
			// were credentials correct?
	        if(authData == null) 
	            throw new AuthenticationFailureException();
	        // has someone already done key entry?
	        if(authData.getCaptureMethod() != null && !authData.getCaptureMethod().equals(Constants.RosterCaptureMethod.CAPTURE_METHOD_ONLINE))
	            throw new KeyEnteredResponsesException();  
	                               
	        // are we outside the test window?
	        Date now = new Date(System.currentTimeMillis());
	        now = DateUtils.getAdjustedDate(now, TimeZone.getDefault().getID(), "GMT", now);
	
	        Date windowStartDateTime = (Date) authData.getWindowStartDate().clone();
	        windowStartDateTime.setHours(authData.getDailyStartTime().getHours());
	        windowStartDateTime.setMinutes(authData.getDailyStartTime().getMinutes());
	        windowStartDateTime.setSeconds(authData.getDailyStartTime().getSeconds());
	
	        Date windowEndDateTime = (Date) authData.getWindowEndDate().clone();
	        windowEndDateTime.setHours(authData.getDailyEndTime().getHours());
	        windowEndDateTime.setMinutes(authData.getDailyEndTime().getMinutes());
	        windowEndDateTime.setSeconds(authData.getDailyEndTime().getSeconds());
	        
	        TimeZone timeZone = TimeZone.getTimeZone(authData.getTimeZone());
	        int startDateOffset = timeZone.getOffset(windowStartDateTime.getTime());
	        int endDateOffset = timeZone.getOffset(windowEndDateTime.getTime());
	        int nowOffset = timeZone.getOffset(now.getTime());
	        
	        windowStartDateTime = new Date(windowStartDateTime.getTime() + startDateOffset - nowOffset);
	        windowEndDateTime = new Date(windowEndDateTime.getTime() + endDateOffset - nowOffset);
	
	        boolean dateBefore = now.compareTo(windowStartDateTime) < 0;
	        boolean dateAfter = now.compareTo(windowEndDateTime) > 0;
	
	        boolean timeBefore = DateUtils.timeBefore(now, windowStartDateTime) &&
	            !(DateUtils.timeBefore(windowEndDateTime, windowStartDateTime) && DateUtils.timeBefore(now, windowEndDateTime));
	        boolean timeAfter = DateUtils.timeAfter(now, windowEndDateTime) &&
	            !(DateUtils.timeAfter(windowStartDateTime, windowEndDateTime) && DateUtils.timeAfter(now, windowStartDateTime));

	        // does the roster have appropriate completion status?
	        String statusCode = authData.getRosterTestCompletionStatus();
	        if(statusCode.equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS))
	            throw new TestSessionCompletedException();
	        if(statusCode.equals(Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS))
	            // do nothing on IP status, allow zero-time restart
	        	//throw new TestSessionInProgressException();
	        	statusCode = Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS;
	        	authData.setRosterTestCompletionStatus(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS);
	        if( dateBefore || dateAfter || timeBefore || timeAfter)
	            throw new OutsideTestWindowException();
	        if(!statusCode.equals(Constants.StudentTestCompletionStatus.SCHEDULED_STATUS) && 
	            !statusCode.equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) &&
	            !statusCode.equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS))
	            throw new TestSessionNotScheduledException();
		} catch (AuthenticationFailureException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.AUTHENTICATION_FAILURE_STATUS); 
        } catch (KeyEnteredResponsesException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.KEY_ENTERED_RESPONSES_STATUS); 
        } catch (OutsideTestWindowException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.OUTSIDE_TEST_WINDOW_STATUS); 
        } catch (TestSessionCompletedException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_COMPLETED_STATUS); 
        } catch (TestSessionNotScheduledException afe) {
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_NOT_SCHEDULED_OR_INTERRUPTED_STATUS); 
        } catch (Exception e) {
        	e.printStackTrace();
            response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.INTERNAL_SERVER_ERROR_STATUS);
        }
        /*ConsolidatedRestartData restartData = response.getTmssvcResponse().getLoginResponse().getConsolidatedRestartData();
        if(restartData != null) {
	        Tsd[] restartManifest = restartData.getTsdArray();
	        if (restartManifest == null || restartManifest.length < 1) {
	        	response.getTmssvcResponse().getLoginResponse().setConsolidatedRestartData(null);
	        }
        }*/
		return response;
	}
	public void setDocument(TmssvcResponseDocument document) {
		XmlOptions xmlOptions = new XmlOptions(); 
		xmlOptions.setCharacterEncoding("UTF-8");
        xmlOptions.setUnsynchronized();
		this.document = document.xmlText(xmlOptions);
	}
	public AuthenticationData getAuthData() {
		return authData;
	}
	public void setAuthData(AuthenticationData authData) {
		this.authData = authData;
	}

	/*public Manifest getManifest() {
		return manifest;
	}

	public void setManifest(Manifest manifest) {
		this.manifest = manifest;
	}*/
	
	private static ItemResponseData[] sortItemResponseData(ItemResponseData[] startList) {
		HashMap<Integer, ItemResponseData> sortedMap = new HashMap<Integer, ItemResponseData>(startList.length);
		for(int i=0;i<startList.length;i++) {
			ItemResponseData val = startList[i];
			sortedMap.put(new Integer(val.getResponseSeqNum()), val);
		}
		Integer [] keys = sortedMap.keySet().toArray(new Integer[0]);
		Arrays.sort(keys);
		ArrayList<ItemResponseData> finalList = new ArrayList<ItemResponseData>(keys.length);
		for(int i=0;i<keys.length;i++) {
			finalList.add(sortedMap.get(keys[i]));
		}
		return finalList.toArray(new ItemResponseData[0]);
	}
	
	public static void generateRestartData(LoginResponse loginResponse, ManifestData manifestData, ItemResponseData[] itemResponseData, ConsolidatedRestartData restartData) throws SQLException {
		itemResponseData = sortItemResponseData(itemResponseData);
		Tsd tsd = restartData.addNewTsd();        
		tsd.setScid(String.valueOf(manifestData.getId()));
		tsd.setLsid(loginResponse.getLsid());
		if (manifestData.getScratchpadContent()== null) manifestData.setScratchpadContent("");
		tsd.addSp(manifestData.getScratchpadContent());
		Ast ast = tsd.addNewAst();
		int remSec = (manifestData.getScoDurationMinutes() * 60) - manifestData.getTotalTime();
		ast.setRemSec((float) remSec);
		int maxRSN = 0;
		int totalDur = 0;
		for(int i=0;i<itemResponseData.length;i++) {
			ItemResponseData data = itemResponseData[i];
			tsd.addNewIst();
			Ist ist = tsd.getIstArray(i);
			ist.setIid(data.getItemId());
			ist.setEid(""+data.getEid());
			ist.setCst(Ist.Cst.UNKNOWN);
			ist.setMrk("T".equals(data.getStudentMarked())?"1":"0");
			ist.setDur(data.getResponseElapsedTime());
			ist.setMseq(new BigInteger(data.getResponseSeqNum()));
			totalDur = totalDur + data.getResponseElapsedTime();
			Rv rv = ist.addNewRv();
			if ("SR".equals(data.getItemType())) {
				rv.setT(BaseType.IDENTIFIER);
				if(data.getResponse() == null) data.setResponse("");
				rv.setV(data.getResponse());
			}
			else { 
				rv.setT(BaseType.STRING);
				String crResponse = "";
				String crResponseClob = data.getConstructedResponse();
				if (crResponseClob != null && crResponseClob.length() > 0) {
					crResponse = crResponseClob.substring(0, crResponseClob.length());
				}
				rv.setV(crResponse);
			}
			rv.setN("RESPONSE");
	
			Ov ov = ist.addNewOv();
			ov.setN("SCORE");
			ov.setT(BaseType.INTEGER);
	
			if ("SR".equals(data.getItemType()))
				ov.setV(""+data.getScore());
			else
				ov.setV("");
	
			if(Integer.valueOf(data.getResponseSeqNum()) > maxRSN) {
				ast.setCurEid(""+data.getEid());
				maxRSN = Integer.valueOf(data.getResponseSeqNum());
			}
			logger.info("\n*****  RosterData: generateRestartData: Added response to restart data: " + tsd.xmlText());
		}
		manifestData.setTotalTime(totalDur);
	}
	
	public static ItemResponseData[] generateItemResponseData(String testRosterId, ManifestData manifest, ItemResponseData[] tsda) throws XmlException {
		HashMap irdMap = new HashMap(tsda.length);
		HashMap itemMap = new HashMap(tsda.length);
		HashMap audioResponseMap = new HashMap(tsda.length);
		for(int i=0;i<tsda.length;i++) {
			logger.debug("generateItemResponseData: Tsd " + i);
			
			AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd tsd = ItemResponseData.IrdToAdsTsd(tsda[i]);
			
			if(manifest.getId() == Integer.parseInt(tsd.getScid())) {
				noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist[] ista = tsd.getIstArray();
				//for(int j=0;j<ista.length;j++) {	
					//logger.debug("generateItemResponseData: Ist " + j);
					SaveTestingSessionData.Tsd.Ist ist = ista[0];
					BigInteger mapMseq = (BigInteger) itemMap.get(ist.getIid());
					boolean catHeartbeat = ist.getIid().indexOf("TABECAT") >=0 && !ist.getSendCatSave();
					if(ist.getAudioItem()) {
			    		if(ist.getRvArray() != null && ist.getRvArray().length > 0) {
			    			noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist.Rv rv = ist.getRvArray(0);
			    			if(rv.getVArray() != null && rv.getVArray().length > 0) {
			    				String v = rv.getVArray(0);
			    				if(v != null && !"".equals(v.trim())) {
			    					String response = testRosterId + "_" + ist.getIid();
			    					audioResponseMap.put(ist.getIid(), response);
			    					ItemResponseData ird = (ItemResponseData) irdMap.get(ist.getIid());
			    					if(ird != null) ird.setConstructedResponse(response);
			    				}
			    			}
			    		}
			    	}
					if((mapMseq == null || tsd.getMseq().intValue() > mapMseq.intValue()) && !catHeartbeat) {
						itemMap.put(ist.getIid(), tsd.getMseq());
						
						//   if(ist != null && ist.getRvArray(0) != null && ist.getRvArray(0).getVArray(0) != null) {
				        if(ist != null && ist.getRvArray() != null && ist.getRvArray().length >0 ) {
				            if( ist.getRvArray(0).getVArray() != null && ist.getRvArray(0).getVArray().length >0){
				                if(ist.getRvArray(0).getVArray(0) != null){
				                    BaseType.Enum responseType = ist.getRvArray(0).getT();
				                    String xmlResponse = ist.getRvArray(0).getVArray(0);
				                    String response = xmlResponse;
				                    String studentMarked = ist.getMrk() ? "T" : "F";
				                    boolean audio = ist.getAudioItem();
				                    String itemType = "SR";
				                    if(audio) {
				                    	itemType = "CR";
				                    	response = (String) audioResponseMap.get(ist.getIid());
				                    	if(response == null) response = "";
				                    } else if(xmlResponse != null && xmlResponse.length() > 0) {
				                    	response = xmlResponse;
				                    	// strip xml
				                        int start = xmlResponse.indexOf(">");
				                        if(start >= 0) {
				                            response = xmlResponse.substring(start + 1);
				                            int end = response.lastIndexOf("</");
				                            if(end != -1)
				                                response = response.substring(0, end);
				                        } else {
				                            response = xmlResponse;
				                        }
				                        // strip CDATA
				                        start = response.indexOf("[CDATA[");
				                        if(start >= 0) {
				                            response = response.substring(start + 7);
				                            int end = response.lastIndexOf("]]");
				                            if(end != -1)
				                                response = response.substring(0, end);
				                        }
				                        if(response.length() > 1) itemType = "CR";
				                    }
				                    ItemResponseData ird = new ItemResponseData();
			                    	ird.setEid(Integer.parseInt(ist.getEid()));
			                    	ird.setItemId(ist.getIid());
			                    	ird.setResponse(response);
			                    	String score = "0";
			                    	if(ist.getOvArray() != null && ist.getOvArray().length > 0 && ist.getOvArray(0) != null && ist.getOvArray(0).getVArray() != null && ist.getOvArray(0).getVArray().length > 0 && ist.getOvArray(0).getVArray(0) != null) {
			                    		score = ist.getOvArray(0).getVArray(0);
			                    	}
			                    	if(score.trim().equals("")) score = "0";
			                    	ird.setScore(Integer.parseInt(score));
			                    	ird.setResponseElapsedTime((int)ist.getDur());
			                    	ird.setResponseSeqNum(String.valueOf(tsd.getMseq()));
			                    	ird.setStudentMarked(studentMarked);
			                    	ird.setConstructedResponse(response);
			                    	ird.setAudioItem(ist.getAudioItem());
			                    	// TODO (complete): fix this
			                    	ird.setItemType(BaseType.STRING.equals(responseType)?"CR":"SR");
			                    	ird.setResponseType(responseType.toString());
			                    	ird.setTestRosterId(Integer.parseInt(testRosterId));
			                    	irdMap.put(ird.getItemId(), ird);
			                    	logger.info("\n*****  RosterData: generateItemResponseData: constructed restart item response " + ird.getTestRosterId() + ", seqnum: " + ird.getResponseSeqNum() + ", item type: " + ird.getItemType() + ", response type: " + ird.getResponseType() + ", elapsed time: " + ird.getResponseElapsedTime() + ", response: " + ird.getResponse() + ", CR response: " + ird.getConstructedResponse());
				                 }
				            } else { 
				                String response = "";                   
				                String studentMarked = ist.getMrk() ? "T" : "F";
				                ItemResponseData ird = new ItemResponseData();
		                    	ird.setEid(Integer.parseInt(ist.getEid()));
		                    	ird.setItemId(ist.getIid());
		                    	ird.setResponse(response);
		                    	ird.setResponseElapsedTime((int)ist.getDur());
		                    	ird.setResponseSeqNum(String.valueOf(tsd.getMseq()));
		                    	ird.setStudentMarked(studentMarked);
		                    	ird.setConstructedResponse(response);
		                    	// TODO: fix this
		                    	ird.setItemType("SR");
		                    	ird.setTestRosterId(Integer.parseInt(testRosterId));
		                    	irdMap.put(ird.getItemId(), ird);
		                    	logger.info("\n*****  RosterData: generateItemResponseData: constructed restart item response " + ird.getTestRosterId() + ", seqnum: " + ird.getResponseSeqNum() + ", item type: " + ird.getItemType() + ", response type: " + ird.getResponseType() + ", elapsed time: " + ird.getResponseElapsedTime() + ", response: " + ird.getResponse() + ", CR response: " + ird.getConstructedResponse());
				            }       
				        }
					}
				//}
			}
		}
		return (ItemResponseData[]) irdMap.values().toArray(new ItemResponseData[0]);
	}
}
