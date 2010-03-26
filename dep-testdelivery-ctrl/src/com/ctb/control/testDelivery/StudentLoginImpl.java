package com.ctb.control.testDelivery; 

import com.bea.control.*;
import java.io.Serializable;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.xmlbeans.XmlObject;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testDelivery.assessmentDeliveryData.ItemIdEidMap;
import com.ctb.bean.testDelivery.login.AccomodationsData;
import com.ctb.bean.testDelivery.login.AuthenticationData;
import com.ctb.bean.testDelivery.login.ItemResponseData;
import com.ctb.bean.testDelivery.login.ManifestData;
import com.ctb.control.db.AssessmentDeliveryDBBeanBeanInfo;
import com.ctb.control.db.AuthenticateStudent;
import com.ctb.control.db.AssessmentDeliveryDB;
import com.ctb.control.db.AuthenticateStudentBean;
import com.ctb.control.db.AuthenticateStudentBeanBeanInfo;
import com.ctb.exception.testDelivery.AuthenticationFailureException;
import com.ctb.exception.testDelivery.KeyEnteredResponsesException;
import com.ctb.exception.testDelivery.LocatorSubtestNotCompletedException;
import com.ctb.exception.testDelivery.OutsideTestWindowException;
import com.ctb.exception.testDelivery.TestSessionCompletedException;
import com.ctb.exception.testDelivery.TestSessionInProgressException;
import com.ctb.exception.testDelivery.TestSessionNotScheduledException;
import com.ctb.util.DateUtils;
import com.ctb.util.OASLogger;
import com.ctb.util.SimpleCache;
import com.ctb.util.testDelivery.Constants;
import java.math.BigInteger;
import java.sql.Clob;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;

import noNamespace.BaseType;
import noNamespace.EntryType;
import noNamespace.StereotypeType;
import noNamespace.TmssvcRequestDocument;
import noNamespace.TmssvcRequestDocument.TmssvcRequest;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.LoginRequest;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco.ScoUnitType;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Status.StatusCode;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.TestingSessionData.LmsStudentAccommodations;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.TestingSessionData.LmsStudentAccommodations.StereotypeStyle;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ast;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Ov;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Rv;
//import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Sp;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Tutorial;
//import weblogic.knex.jdom.CDATA;

/**
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation()
public class StudentLoginImpl implements StudentLogin, Serializable
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.TestAdminItemSet testAdminItemSet;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Product product;

    /**
     * @common:control
     */
    //@org.apache.beehive.controls.api.bean.Control()
    //private com.ctb.control.db.testDelivery.AssessmentDeliveryFactory assessmentDeliveryFactory;


    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
   private AuthenticateStudent authenticator;
    //private com.ctb.control.db.testDelivery.AuthenticateStudentFactory authenticatorFactory;

    static final long serialVersionUID = 1L;
    
    private static final String CACHE_TYPE_ITEM_MAP = "CACHE_TYPE_ITEM_MAP";
    

    /**
     * @common:operation
     */
    public TmssvcResponseDocument login(TmssvcRequestDocument document)
    {
        TmssvcRequest loginRequest = document.getTmssvcRequest();
        TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance();
        LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
        loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.OK_STATUS);
        response.getTmssvcResponse().setMethod("login_response");
        try {
            //AuthenticateStudent authenticator = authenticatorFactory.getClass();
            // might be more than one roster for these creds, due to random passwords
            AuthenticationData [] authDataArray = authenticator.authenticateStudent(loginRequest.getLoginRequest().getUserName(), loginRequest.getLoginRequest().getPassword());
            AuthenticationData authData = null;
            boolean authenticated = false;
            String testAccessCode = loginRequest.getLoginRequest().getAccessCode();
            int testRosterId = -1;
            String lsid = null;
            ManifestData [] manifestData = new ManifestData [0];
            for(int a=0;a<authDataArray.length && !authenticated;a++) {
                authData = authDataArray[a];
                if(authData != null) {
                    OASLogger.getLogger("TestDelivery").debug(authData.toString());
                } else {
                    throw new AuthenticationFailureException();
                }
                testRosterId = authData.getTestRosterId();
                lsid = String.valueOf(testRosterId) + ":" + testAccessCode;
                loginResponse.setLsid(lsid);
                manifestData = authenticator.getManifest(testRosterId, testAccessCode);
                if(manifestData.length > 0) {
                    authenticated = true;
                }
            }
            if(manifestData.length <= 0) {
                response = TmssvcResponseDocument.Factory.newInstance();
                loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
                loginResponse.setLsid(lsid);
                loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.AUTHENTICATION_FAILURE_STATUS);
                throw new AuthenticationFailureException();
            } else {
                validateAuthenticationData(loginRequest.getLoginRequest(), loginResponse, authData);
                copyAuthenticationDataToResponse(loginResponse, authData);
                AccomodationsData accomData = authenticator.getAccomodations(testRosterId);
                if(accomData != null) {
                    OASLogger.getLogger("TestDelivery").debug(accomData.toString());
                    copyAccomodationsDataToResponse(loginResponse, accomData);
                }
                boolean subtestsRemaining = false;
                for(int i=0;i<manifestData.length;i++) {
                    if(Constants.StudentTestCompletionStatus.SCHEDULED_STATUS.equals(manifestData[i].getCompletionStatus()) ||
                       Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS.equals(manifestData[i].getCompletionStatus()) ||
                       Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
                       Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
                       Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS.equals(manifestData[i].getCompletionStatus())) {
                        subtestsRemaining = true;
                    }
                }
                if(!subtestsRemaining) {
                    response = TmssvcResponseDocument.Factory.newInstance();
                    loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
                    loginResponse.setLsid(lsid);
                    loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_COMPLETED_STATUS);
                    throw new TestSessionCompletedException();
                }
                if(manifestData != null) OASLogger.getLogger("TestDelivery").debug(manifestData.toString());
                copyManifestDataToResponse(loginResponse, manifestData, testRosterId, authData.getTestAdminId(), loginRequest.getLoginRequest().getAccessCode());
                authenticator.setRosterCompletionStatus(testRosterId, Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS, "ON", loginResponse.getRestartNumber().intValue() + 1, new Date(), -1);
            }
            
        } catch (AuthenticationFailureException afe) {
            loginResponse.getStatus().setStatusCode(Constants.StudentLoginResponseStatus.AUTHENTICATION_FAILURE_STATUS); 
        } catch (KeyEnteredResponsesException afe) {
            loginResponse.getStatus().setStatusCode(Constants.StudentLoginResponseStatus.KEY_ENTERED_RESPONSES_STATUS); 
        } catch (OutsideTestWindowException afe) {
            loginResponse.getStatus().setStatusCode(Constants.StudentLoginResponseStatus.OUTSIDE_TEST_WINDOW_STATUS); 
        } catch (TestSessionCompletedException afe) {
            loginResponse.getStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_COMPLETED_STATUS); 
        } catch (TestSessionInProgressException afe) {
            loginResponse.getStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_IN_PROGRESS_STATUS); 
        } catch (TestSessionNotScheduledException afe) {
            loginResponse.getStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_NOT_SCHEDULED_OR_INTERRUPTED_STATUS); 
        } catch (Exception e) {
            e.printStackTrace();
            loginResponse.getStatus().setStatusCode(Constants.StudentLoginResponseStatus.INTERNAL_SERVER_ERROR_STATUS);
        }
        return response;
    }
    
    /**
     * @common:operation
     */
    public TmssvcResponseDocument ctbLogin(TmssvcRequestDocument document)
    {
        TmssvcRequest loginRequest = document.getTmssvcRequest();
        OASLogger.getLogger("TestDelivery").debug("ctbLogin()\n"+loginRequest.toString());
        TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance();
        LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
        loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.OK_STATUS);
        response.getTmssvcResponse().setMethod("login_response");
        try {
           // AuthenticateStudent authenticator = authenticatorFactory.create();
            // might be more than one roster for these creds, due to random passwords
        	System.out.println("########### Login ID:"+ loginRequest.getLoginRequest().getUserName()+" Password:"+loginRequest.getLoginRequest().getPassword());
        	AuthenticationData [] authDataArray = authenticator.authenticateStudent(loginRequest.getLoginRequest().getUserName(), loginRequest.getLoginRequest().getPassword());
            AuthenticationData authData = null;
            boolean authenticated = false;
            String testAccessCode = loginRequest.getLoginRequest().getAccessCode();
            System.out.println("########### TestAccessCode:"+ testAccessCode);
            int testRosterId = -1;
            String lsid = null;
            ManifestData [] manifestData = new ManifestData [0];
            for(int a=0;a<authDataArray.length && !authenticated;a++) {
                authData = authDataArray[a];
                if(authData != null) {
                    OASLogger.getLogger("TestDelivery").debug(authData.toString());
                } else {
                    throw new AuthenticationFailureException();
                }
                testRosterId = authData.getTestRosterId();
                lsid = String.valueOf(testRosterId) + ":" + testAccessCode;
                loginResponse.setLsid(lsid);
                manifestData = authenticator.getManifest(testRosterId, testAccessCode);
                if(manifestData.length > 0) {
                    authenticated = true;
                    for (int i = 0; i < manifestData.length; i++) {
                        /*
                         * Retrieve scratchpad contents for this subtest.
                         */
                        manifestData[i].setScratchpadContent(
                                    authenticator.getScratchpadContent(
                                            testRosterId, 
                                            manifestData[i].getId()));
                    }
                }
            }
            if(manifestData.length <= 0) {
                response = TmssvcResponseDocument.Factory.newInstance();
                loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
                loginResponse.setLsid(lsid);
                loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.AUTHENTICATION_FAILURE_STATUS);
                throw new AuthenticationFailureException();
            } else {
                validateAuthenticationData(loginRequest.getLoginRequest(), loginResponse, authData);
                
                /* Set the Random Number Distractor 
                    in Login ressponse for TABE product
				 */

				 if (authData.getRandomDistractorSeedNumber() != null) {


					 loginResponse.setRandomDistractorSeedNumber(
							 new BigInteger(String.valueOf( authData.
									 getRandomDistractorSeedNumber())));


				 }  else {

					 if (manifestData[0].getRandomDistractorStatus() != null && 
							 manifestData[0].getRandomDistractorStatus().equals("Y")) {

						 Integer ranodmSeedNumber = 
							 getRandomDistractorOfRoster(testRosterId);

						 loginResponse.setRandomDistractorSeedNumber(
								 new BigInteger( String.valueOf(ranodmSeedNumber.intValue())));
					 }

				 }
                copyAuthenticationDataToResponse(loginResponse, authData);
                AccomodationsData accomData = authenticator.getAccomodations(testRosterId);
                
                //ISTEP2010CR001 : For get controller of speed of the speech(TTS)
                //Defect #60524 : 503 error
                if(accomData != null) {
                	
                
	                if (accomData.getScreenReader() != null && accomData.getScreenReader().equals("T")) {
	                    
	                    String speechControllerFlag = authenticator.
	                                getSpeechControllerAccommodation(testRosterId);
	                        if (speechControllerFlag != null && 
	                                speechControllerFlag.equals("T")) {
	                    
	                            if (authData.getTtsSpeedStatus() != null) {
	                                
	                                loginResponse.setTtsSpeedValue(authData.getTtsSpeedStatus());
	                                
	                            } else {
	                              
	                               loginResponse.setTtsSpeedValue(Constants.SpeechController.
	                                    DEFAULT_TTS_SPEED_VALUE );
	                                    
	                        }
	                        
	                    }
	                }
                }
                
                if(accomData != null) {
                    OASLogger.getLogger("TestDelivery").debug(accomData.toString());
                    copyAccomodationsDataToResponse(loginResponse, accomData);
                }
                boolean subtestsRemaining = false;
                for(int i=0;i<manifestData.length&&!subtestsRemaining;i++) {
                    if(Constants.StudentTestCompletionStatus.SCHEDULED_STATUS.equals(manifestData[i].getCompletionStatus()) ||
                       Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS.equals(manifestData[i].getCompletionStatus()) ||
                       Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
                       Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
                       Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS.equals(manifestData[i].getCompletionStatus())) {
                        subtestsRemaining = true;
                        if(loginResponse.getRestartFlag()) {
                            manifestData[i].setTotalTime(authenticator.getTotalElapsedTimeForSubtest(new Integer(testRosterId), new Integer(manifestData[i].getId())).intValue());
                            int remSec = (manifestData[i].getScoDurationMinutes() * 60) - manifestData[i].getTotalTime();
                            ItemResponseData [] itemResponseData = authenticator.getRestartItemResponses(testRosterId, manifestData[i].getId());
                            copyRestartDataToResponse(lsid, testRosterId, manifestData[i].getId(), loginResponse, itemResponseData, remSec, Integer.parseInt(manifestData[i].getAdsid()), manifestData[i].getScratchpadContentStr());
                        }
                    }
                }
                if(!subtestsRemaining) {
                    response = TmssvcResponseDocument.Factory.newInstance();
                    loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
                    loginResponse.setLsid(lsid);
                    loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_COMPLETED_STATUS);
                    throw new TestSessionCompletedException();
                }
                if(manifestData != null) OASLogger.getLogger("TestDelivery").debug(manifestData.toString());
                copyManifestDataToResponse(loginResponse, manifestData, testRosterId, authData.getTestAdminId(), loginRequest.getLoginRequest().getAccessCode());

                String tutorialResource = authenticator.getTutorialResource(testRosterId);
                boolean wasTutorialTaken = authenticator.wasTutotrialTaken(testRosterId);
                if (tutorialResource!= null && !tutorialResource.trim().equals("")) {
                    Tutorial tutorial =loginResponse.addNewTutorial();
                    tutorial.setTutorialUrl(tutorialResource);
                    tutorial.setDeliverTutorial(new BigInteger(wasTutorialTaken ? "0":"1"));
                }
                authenticator.setRosterCompletionStatus(testRosterId, Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS, "ON", loginResponse.getRestartNumber().intValue() + 1, new Date(), -1);
            }
        } catch (AuthenticationFailureException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.AUTHENTICATION_FAILURE_STATUS); 
        } catch (KeyEnteredResponsesException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.KEY_ENTERED_RESPONSES_STATUS); 
        } catch (OutsideTestWindowException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.OUTSIDE_TEST_WINDOW_STATUS); 
        } catch (TestSessionCompletedException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_COMPLETED_STATUS); 
        } catch (TestSessionInProgressException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_IN_PROGRESS_STATUS); 
        } catch (TestSessionNotScheduledException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.TEST_SESSION_NOT_SCHEDULED_OR_INTERRUPTED_STATUS); 
        } catch (LocatorSubtestNotCompletedException afe) {
            response = TmssvcResponseDocument.Factory.newInstance();
            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.LOCATOR_SUBTEST_NOT_COMPLETED_STATUS); 
        } catch (Exception e) {
            e.printStackTrace();
            response = TmssvcResponseDocument.Factory.newInstance();
            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.INTERNAL_SERVER_ERROR_STATUS);
        }
        return response;
    }
    
    private void copyRestartDataToResponse(String lsid, 
                                    int testRosterId, 
                                    int subtestItemSetId, 
                                    LoginResponse loginResponse, 
                                    ItemResponseData [] itemResponseData, 
                                    int remSec, 
                                    int adsAssessmentId, 
                                    String scratchpadContent) throws SQLException
    {
        ConsolidatedRestartData restartData = loginResponse.addNewConsolidatedRestartData();
        Tsd tsd = restartData.addNewTsd();        
        tsd.setScid(String.valueOf(subtestItemSetId));
        tsd.setLsid(lsid);
        if (scratchpadContent == null) scratchpadContent = "";
     //   tsd.addSp("<![CDATA[" + scratchpadContent + "]]>");
        tsd.addSp(scratchpadContent);
        Ast ast = tsd.addNewAst();
        ast.setRemSec(Float.parseFloat(String.valueOf(remSec)));
        int maxRSN = 0;
        for(int i=0;i<itemResponseData.length;i++) {
            ItemResponseData data = itemResponseData[i];
            tsd.addNewIst();
            Ist ist = tsd.getIstArray(i);
            ist.setIid(data.getItemId());
//            ist.setEid(String.valueOf(data.getResponseSeqNum()));
            ist.setEid(""+data.getEid());
            ist.setCst(Ist.Cst.UNKNOWN);
            ist.setMrk("T".equals(data.getStudentMarked())?"1":"0");
//            ist.setAwd("1");
            ist.setDur(data.getResponseElapsedTime());
            Rv rv = ist.addNewRv();
            if ("SR".equals(data.getItemType())) {
                rv.setT(BaseType.IDENTIFIER);
                rv.setV(data.getResponse());
            }
            else { 
                rv.setT(BaseType.STRING);
                String crResponse = "";
                Clob crResponseClob = data.getConstructedResponse();
                if (crResponseClob != null) {
                    int length = (int) crResponseClob.length();
                    crResponse = crResponseClob.getSubString(1, length);
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

//            ast.setCurEid(itemResponseData[i].getItemId());
        }
    }
/*    
    private int getEid(String itemId, int adsAssessmentId) {
        Integer eid = (Integer) SimpleCache.checkCache(CACHE_TYPE_ITEM_MAP, itemId);
        if (eid == null) {
            AssessmentDelivery  assessmentDelivery = assessmentDeliveryFactory.create();
            OASLogger.getLogger("TestDelivery").debug("******* call ads.getItemIdEidMap adsAssessmentId="+adsAssessmentId+" itemId="+itemId);
            ItemIdEidMap [] map = assessmentDelivery.getItemIdEidMap(adsAssessmentId);
            for (int i=0; i < map.length; i++) {
                SimpleCache.cacheResult(CACHE_TYPE_ITEM_MAP, map[i].getItemId(), new Integer(map[i].getEid()));
                OASLogger.getLogger("TestDelivery").debug("******* save to cache itemId="+map[i].getItemId()+" eid="+map[i].getEid());
            }
            eid = (Integer) SimpleCache.checkCache(CACHE_TYPE_ITEM_MAP, itemId);
        }
        return eid.intValue();
        
        
    }
*/    
    private void validateAuthenticationData(LoginRequest request, LoginResponse response, AuthenticationData authData) throws AuthenticationFailureException, KeyEnteredResponsesException, OutsideTestWindowException, TestSessionCompletedException, TestSessionInProgressException, TestSessionNotScheduledException, LocatorSubtestNotCompletedException, SQLException {
        // were credentials correct?
        if(authData == null) 
            throw new AuthenticationFailureException();
        // has someone already done key entry?
        if(authData.getCaptureMethod() != null && !authData.getCaptureMethod().equals(Constants.RosterCaptureMethod.CAPTURE_METHOD_ONLINE))
            throw new KeyEnteredResponsesException();  

        TestProduct testProduct = product.getProductForTestAdmin(new Integer(authData.getTestAdminId()));
        //AuthenticateStudent authenticator = authenticatorFactory.create();

        boolean isTabe = false;
        if ("TB".equals(testProduct.getProductType()))
            isTabe = true;
        if (isTabe) {
            ManifestData [] locatorSubtests = authenticator.getTABELocatorManifest(authData.getTestRosterId());
            boolean hasLocator = false;
            if (locatorSubtests!= null && locatorSubtests.length >0)
                hasLocator = true;
            if (hasLocator) {
                
                ManifestData [] completeSubtests = authenticator.getManifestByRoster(authData.getTestRosterId());
                if (!request.getAccessCode().equalsIgnoreCase(completeSubtests[0].getAccessCode()))
                {
                    for (int i=0; i<locatorSubtests.length; i++) {
                        if (!"CO".equals(locatorSubtests[i].getCompletionStatus()))
                            throw new LocatorSubtestNotCompletedException();
                    }
                          
                }              
            
            }
        }
                               
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
        // set restart flag if session was previously interrupted
        if(statusCode.equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) ||
            statusCode.equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS)) {
            response.setRestartFlag(true);
        } else {
            response.setRestartFlag(false);
        }
        response.setRestartNumber(new BigInteger(String.valueOf(authData.getRestartNumber())));
        
        String logoURI = authenticator.getProductLogo(testProduct.getProductId());
        if (logoURI == null || "".equals(logoURI))
            logoURI = "/resources/logo.swf";
        response.addNewBranding().setTdclogo(logoURI);
    }
    
    private void copyAuthenticationDataToResponse(LoginResponse response, AuthenticationData authData) throws AuthenticationFailureException, KeyEnteredResponsesException, OutsideTestWindowException, TestSessionCompletedException, TestSessionInProgressException, TestSessionNotScheduledException {
        response.addNewTestingSessionData();
        response.getTestingSessionData().addNewCmiCore();
        response.getTestingSessionData().getCmiCore().setStudentId(String.valueOf(authData.getStudentId()));
        response.getTestingSessionData().getCmiCore().setStudentLastName(authData.getStudentLastName());
        response.getTestingSessionData().getCmiCore().setStudentFirstName(authData.getStudentFirstName());
        response.getTestingSessionData().getCmiCore().setStudentMiddleName(authData.getStudentMiddleName());
    }
    
    /**
	 *  Set  the Random Distractor in Login Response
	 *  Add the logic to set and get seed no from 
	 *  test_roster for TABE
	 */
    private Integer getRandomDistractorOfRoster(int testRosterId) 
            throws SQLException  {

		//AuthenticateStudent authenticator = authenticatorFactory.create();
		Integer randomNumber = generateRandomNumber();
		authenticator.updateTestRosterWithRDSeed(
                testRosterId,randomNumber.intValue());
		return randomNumber;

	}
    
    private void copyAccomodationsDataToResponse(LoginResponse response, AccomodationsData accomData) {
        if(accomData != null) {
            response.getTestingSessionData().addNewLmsStudentAccommodations();
            LmsStudentAccommodations accommodations = response.getTestingSessionData().getLmsStudentAccommodations();
            accommodations.setCalculator( "T".equals(accomData.getCalculator()) ?  true : false );
            accommodations.setMagnifier( "T".equals(accomData.getScreenMagnifier()) ?  true : false );
            accommodations.setScreenReader( "T".equals(accomData.getScreenReader()) ?  true : false );
            accommodations.setUntimed( "T".equals(accomData.getUntimedTest()) ?  true : false );
            accommodations.setRestBreak( "T".equals(accomData.getTestPause()) ?  true : false );
            //set the boolean value in accommodations.setHighlighter depends upon wheather the highlighter is true or false.
            accommodations.setHighlighter("T".equals(accomData.getHighlighter()) ?  true : false );
            accommodations.addNewStereotypeStyle();
            StereotypeStyle directionsStereotype = accommodations.getStereotypeStyleArray(0);
            directionsStereotype.setStereotype(StereotypeType.DIRECTIONS);
            if(accomData.getQuestionBackgroundColor() != null) 
                directionsStereotype.setBgcolor(accomData.getQuestionBackgroundColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontColor() != null)
                directionsStereotype.setFontColor(accomData.getQuestionFontColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontSize() == 0)
                directionsStereotype.setFontMagnification(new Float(1.0).floatValue());
            else
                directionsStereotype.setFontMagnification(accomData.getQuestionFontSize());
            accommodations.addNewStereotypeStyle();
            StereotypeStyle stimulusStereotype = accommodations.getStereotypeStyleArray(1);
            stimulusStereotype.setStereotype(StereotypeType.STIMULUS);
            if(accomData.getQuestionBackgroundColor() != null)
                stimulusStereotype.setBgcolor(accomData.getQuestionBackgroundColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontColor() != null)
                stimulusStereotype.setFontColor(accomData.getQuestionFontColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontSize() == 0)
                stimulusStereotype.setFontMagnification(new Float(1.0).floatValue());
            else
                stimulusStereotype.setFontMagnification(accomData.getQuestionFontSize());
            accommodations.addNewStereotypeStyle();
            StereotypeStyle stemStereotype = accommodations.getStereotypeStyleArray(2);
            stemStereotype.setStereotype(StereotypeType.STEM);
            if(accomData.getQuestionBackgroundColor() != null)
                stemStereotype.setBgcolor(accomData.getQuestionBackgroundColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontColor() != null)
                stemStereotype.setFontColor(accomData.getQuestionFontColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontSize() == 0)
                stemStereotype.setFontMagnification(new Float(1.0).floatValue());
            else
                stemStereotype.setFontMagnification(accomData.getQuestionFontSize());
            accommodations.addNewStereotypeStyle();
            StereotypeStyle answerStereotype = accommodations.getStereotypeStyleArray(3);
            answerStereotype.setStereotype(StereotypeType.ANSWER_AREA);
            if(accomData.getAnswerBackgroundColor() != null)
                answerStereotype.setBgcolor(accomData.getAnswerBackgroundColor().replaceAll("#","0x"));
            if(accomData.getAnswerFontColor() != null)
                answerStereotype.setFontColor(accomData.getAnswerFontColor().replaceAll("#","0x"));
            if(accomData.getAnswerFontSize() == 0)
                answerStereotype.setFontMagnification(new Float(1.0).floatValue());
            else
                answerStereotype.setFontMagnification(accomData.getAnswerFontSize());
        }
    }
    
    private void copyManifestDataToResponse(LoginResponse response, ManifestData [] manifestData, int testRosterId, int testAdminId, String accessCode) throws SQLException {
        response.addNewManifest();
        Manifest manifest = response.getManifest();
        String isUltimateAccessCode = authenticator.isUltimateAccessCode(new Integer(testRosterId), new Integer(testAdminId), accessCode);
        
        if(response.getRestartFlag() && "T".equals(isUltimateAccessCode)) {
	        ArrayList a = new ArrayList();
	        for(int i=0;i<manifestData.length;i++) {
	        	ManifestData data = manifestData[i];
	        	if(Constants.StudentTestCompletionStatus.COMPLETED_STATUS.equals(data.getCompletionStatus())){
	        		continue;
	        	}else{
	        		a.add(data);
	        	}
	        }
	        manifestData = (ManifestData [])a.toArray();
        }
        
        for(int i=0;i<manifestData.length;i++) {
        	ManifestData data = manifestData[i];
        /*	if(response.getRestartFlag() && "T".equals(isUltimateAccessCode) 
        				&& Constants.StudentTestCompletionStatus.COMPLETED_STATUS.equals(data.getCompletionStatus())){
	            System.out.println("***** In If");
	            System.out.println("RestartFlag: "+response.getRestartFlag()+", isUltimateAccessCode: "
	            			+isUltimateAccessCode+", CompletionStatus: "+data.getCompletionStatus());
        		continue;
        	}else{*/
        		System.out.println("***** In Else");
        		System.out.println("RestartFlag: "+response.getRestartFlag()+", isUltimateAccessCode: "
	            			+isUltimateAccessCode+", CompletionStatus: "+data.getCompletionStatus());
        		manifest.setTitle(data.getTestTitle());
	            manifest.addNewSco();
	            Sco sco = manifest.getScoArray(i);
	            if(data.getAdminForceLogout().equals("T") &&
	                ((i >= manifestData.length - 1) || (data.getScoParentId() != manifestData[i+1].getScoParentId()))) {
	                sco.setForceLogout(true);
	            } else {
	                sco.setForceLogout(false);
	            }
	            if(data.getTotalTime() > 0) {
	                sco.setCmiCoreEntry(EntryType.RESUME);
	            } else {
	                sco.setCmiCoreEntry(EntryType.AB_INITIO);
	            }
	            sco.setId(String.valueOf(data.getId()));
	            sco.setScoDurationMinutes(new BigInteger(String.valueOf(data.getScoDurationMinutes())));
	            // scoUnitQuestionNumberOffset will be used to control multi-part subtest numbering
	            sco.setScoUnitQuestionNumberOffset(String.valueOf(0));
	            sco.setScoUnitType(ScoUnitType.SUBTEST);
	            sco.setTitle(data.getTitle());
	            sco.setAsmtHash(data.getAsmtHash());
	            sco.setAsmtEncryptionKey(data.getAsmtEncryptionKey());
	            sco.setItemEncryptionKey(data.getItemEncryptionKey());
	            sco.setAdsid(data.getAdsid());
	            int hours = (int) Math.floor(data.getTotalTime() / 3600);
	            int minutes = (int) Math.floor((data.getTotalTime() - (hours * 3600)) / 60);
	            int seconds = data.getTotalTime() - (hours * 3600) - (minutes * 60);
	            sco.setCmiCoreTotalTime(hours + ":" + minutes + ":" + seconds);
        	//}
        }
        //AuthenticateStudent authenticator = authenticatorFactory.create();
        if("T".equals(isUltimateAccessCode)) {
            if(manifestData.length > 0 && "T".equals(manifestData[0].getShowStudentFeedback())) {
                manifest.addNewFeedback();
                manifest.getFeedback().setId("STUDENT_FEEDBACK");
            }
        }
        manifest.addNewTerminator();
        manifest.getTerminator().setId("SEE_YOU_LATER");  
    }
	
    /**
     * Changes For Random Distractor
     */
	private static Integer generateRandomNumber () {

		final String NUM_ARRAY   = "1234567890";
		String alphaNumArray = NUM_ARRAY;

		int index = 0;

		Random rnd = new Random();

		boolean validRandom = false;
		String seed = "";
		while(!validRandom) {

			for(int i = 0; i < 3; i++) {

				index = rnd.nextInt();

				if (index < 0) {
					index = index * -1;
				}

				// make sure the index is a value within the length of our array
				if(index != 0) {
					index = index % alphaNumArray.length();
				}

				seed = seed.concat(String.valueOf(alphaNumArray.charAt(index)));
			}

			if (isNumOdd(seed)) {

				validRandom = true;
				if(verifyContainsCharFrom(NUM_ARRAY,seed)) {
					validRandom = true;
				}
			} else {

				seed = "";

			}

		}
		return Integer.valueOf(seed);

	}
    
     /**
	 *  Verify the characters of random distractor seed  
	 */
     
	private static boolean verifyContainsCharFrom(String charArray,String seed) {
		boolean verified = false;
		int j = 0;

		while(!verified && (j < seed.length())) {
			if(charArray.indexOf(String.valueOf(seed.charAt(j))) != -1) {
				verified = true;
			}
			j++;
		}
		return verified;
	}
    
    /**
     * Checking for odd Number
     */
    private static boolean isNumOdd(String seed) {

		return Integer.valueOf(String.valueOf(seed.charAt(seed.length() - 1))).
				intValue() % 2 == 0 ? false:true;
	}
} 
