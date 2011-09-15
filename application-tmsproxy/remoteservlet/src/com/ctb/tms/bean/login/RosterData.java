package com.ctb.tms.bean.login;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import noNamespace.BaseType;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ast;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Ov;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Rv;

import com.ctb.tms.exception.testDelivery.AuthenticationFailureException;
import com.ctb.tms.exception.testDelivery.KeyEnteredResponsesException;
import com.ctb.tms.exception.testDelivery.OutsideTestWindowException;
import com.ctb.tms.exception.testDelivery.TestSessionCompletedException;
import com.ctb.tms.exception.testDelivery.TestSessionInProgressException;
import com.ctb.tms.exception.testDelivery.TestSessionNotScheduledException;
import com.ctb.tms.util.Constants;
import com.ctb.tms.util.DateUtils;

public class RosterData implements Serializable {
	TmssvcResponseDocument document;
	AuthenticationData authData;
	//Manifest manifest;
	
	public TmssvcResponseDocument getDocument() {
		return this.document;
	}
	
	public TmssvcResponseDocument getLoginDocument() {
		TmssvcResponseDocument response = this.document;
		try {
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
            response = TmssvcResponseDocument.Factory.newInstance();
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.AUTHENTICATION_FAILURE_STATUS); 
        } catch (KeyEnteredResponsesException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.KEY_ENTERED_RESPONSES_STATUS); 
        } catch (OutsideTestWindowException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.OUTSIDE_TEST_WINDOW_STATUS); 
        } catch (TestSessionCompletedException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_COMPLETED_STATUS); 
        } catch (TestSessionNotScheduledException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_NOT_SCHEDULED_OR_INTERRUPTED_STATUS); 
        } catch (Exception e) {
        	e.printStackTrace();
            response = TmssvcResponseDocument.Factory.newInstance();
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
		this.document = document;
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
	
	public static void generateRestartData(LoginResponse loginResponse, ManifestData manifestData, ItemResponseData [] itemResponseData, ConsolidatedRestartData restartData) throws SQLException {
		Tsd tsd = restartData.addNewTsd();        
		tsd.setScid(String.valueOf(manifestData.getId()));
		tsd.setLsid(loginResponse.getLsid());
		if (manifestData.getScratchpadContentStr()== null) manifestData.setScratchpadContentStr("");
		tsd.addSp(manifestData.getScratchpadContentStr());
		Ast ast = tsd.addNewAst();
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
			totalDur = totalDur + data.getResponseElapsedTime();
			Rv rv = ist.addNewRv();
			if ("SR".equals(data.getItemType())) {
				rv.setT(BaseType.IDENTIFIER);
				rv.setV(data.getResponse());
			}
			else { 
				rv.setT(BaseType.STRING);
				String crResponse = "";
				String crResponseClob = data.getConstructedResponse();
				if (crResponseClob != null) {
					int length = (int) crResponseClob.length();
					crResponse = crResponseClob.substring(1, length);
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
	
			if(data.getResponseSeqNum() > maxRSN) {
				ast.setCurEid(""+itemResponseData[i].getEid());
				maxRSN = data.getResponseSeqNum();
			}
		}
		manifestData.setTotalTime(totalDur);
		int remSec = (manifestData.getScoDurationMinutes() * 60) - manifestData.getTotalTime();
		ast.setRemSec(Float.parseFloat(String.valueOf(remSec)));
	}
	
	public static ItemResponseData[] generateItemResponseData(ManifestData manifest, noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd[] tsda) {
		ArrayList irdList = new ArrayList();
		for(int i=0;i<tsda.length;i++) {
			noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd tsd = tsda[i];
			if(manifest.getId() == Integer.parseInt(tsd.getScid())) {
				noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist[] ista = tsd.getIstArray();
				for(int j=0;j<ista.length;j++) {
					noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist ist = ista[j];
			     //   if(ist != null && ist.getRvArray(0) != null && ist.getRvArray(0).getVArray(0) != null) {
			        if(ist != null && ist.getRvArray() != null && ist.getRvArray().length >0 ) {
			            if( ist.getRvArray(0).getVArray() != null && ist.getRvArray(0).getVArray().length >0){
			                if(ist.getRvArray(0).getVArray(0) != null){
			                    BaseType.Enum responseType = ist.getRvArray(0).getT();
			                    String xmlResponse = ist.getRvArray(0).getVArray(0).xmlText();
			                    String response = "";
			                    String studentMarked = ist.getMrk() ? "T" : "F";
			                    if(xmlResponse != null && xmlResponse.length() > 0) {
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
			                    }
			                    ItemResponseData ird = new ItemResponseData();
		                    	ird.setEid(Integer.parseInt(ist.getEid()));
		                    	ird.setItemId(ist.getIid());
		                    	ird.setResponse(response);
		                    	ird.setResponseElapsedTime((int)ist.getDur());
		                    	ird.setResponseSeqNum(tsd.getMseq().intValue());
		                    	ird.setStudentMarked(studentMarked);
		                    	ird.setConstructedResponse(response);
		                    	// TODO: fix this
		                    	ird.setItemType("SR");
		                    	irdList.add(ird);
			                 }
			            } else { 
			                String response = "";                   
			                String studentMarked = ist.getMrk() ? "T" : "F";
			                ItemResponseData ird = new ItemResponseData();
	                    	ird.setEid(Integer.parseInt(ist.getEid()));
	                    	ird.setItemId(ist.getIid());
	                    	ird.setResponse(response);
	                    	ird.setResponseElapsedTime((int)ist.getDur());
	                    	ird.setResponseSeqNum(tsd.getMseq().intValue());
	                    	ird.setStudentMarked(studentMarked);
	                    	ird.setConstructedResponse(response);
	                    	// TODO: fix this
	                    	ird.setItemType("SR");
	                    	irdList.add(ird);                                          
			            }       
			        }
				}
			}
		}
		return (ItemResponseData[]) irdList.toArray(new ItemResponseData[0]);
	}
}
