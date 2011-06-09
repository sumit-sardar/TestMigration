package com.ctb.tms.bean.login;

import java.util.Date;
import java.util.TimeZone;

import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd;

import com.ctb.tms.exception.testDelivery.AuthenticationFailureException;
import com.ctb.tms.exception.testDelivery.KeyEnteredResponsesException;
import com.ctb.tms.exception.testDelivery.OutsideTestWindowException;
import com.ctb.tms.exception.testDelivery.TestSessionCompletedException;
import com.ctb.tms.exception.testDelivery.TestSessionInProgressException;
import com.ctb.tms.exception.testDelivery.TestSessionNotScheduledException;
import com.ctb.tms.util.Constants;
import com.ctb.tms.util.DateUtils;

public class RosterData {
	TmssvcResponseDocument document;
	AuthenticationData authData;
	
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
	/*
	        boolean timeBefore = DateUtils.timeBefore(now, authData.getDailyStartTime()) &&
	            !(DateUtils.timeBefore(authData.getDailyEndTime(), authData.getDailyStartTime()) && DateUtils.timeBefore(now, authData.getDailyEndTime()));
	        boolean timeAfter = DateUtils.timeAfter(now, authData.getDailyEndTime()) &&
	            !(DateUtils.timeAfter(authData.getDailyStartTime(), authData.getDailyEndTime()) && DateUtils.timeAfter(now, authData.getDailyStartTime()));
	*/
	        // does the roster have appropriate completion status?
	        String statusCode = authData.getRosterTestCompletionStatus();
	        if(statusCode.equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS))
	            throw new TestSessionCompletedException();
	        if(statusCode.equals(Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS))
	            throw new TestSessionInProgressException();
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
        } catch (TestSessionInProgressException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_IN_PROGRESS_STATUS); 
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
	
}
