package com.ctb.tms.rdb; 

import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.naming.InitialContext;

import noNamespace.BaseType;
import noNamespace.EntryType;
import noNamespace.StereotypeType;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ast;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Ov;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Rv;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco.ScoUnitType;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.TestingSessionData.LmsStudentAccommodations;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.TestingSessionData.LmsStudentAccommodations.StereotypeStyle;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Tutorial;

import com.ctb.tms.bean.login.AccommodationsData;
import com.ctb.tms.bean.login.AuthenticationData;
import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.bean.login.TestProduct;
import com.ctb.tms.exception.testDelivery.AuthenticationFailureException;
import com.ctb.tms.exception.testDelivery.KeyEnteredResponsesException;
import com.ctb.tms.exception.testDelivery.LocatorSubtestNotCompletedException;
import com.ctb.tms.exception.testDelivery.OutsideTestWindowException;
import com.ctb.tms.exception.testDelivery.TestSessionCompletedException;
import com.ctb.tms.exception.testDelivery.TestSessionInProgressException;
import com.ctb.tms.exception.testDelivery.TestSessionNotScheduledException;
import com.ctb.tms.util.Constants;
import com.ctb.tms.util.DateUtils;

public class OASDBSource
{ 
	private static volatile boolean haveDataSource = true;
	private static String OASDatabaseURL = "jdbc:oracle:thin:@nj09mhe0393-vip:1521:oasr5t1";
	private static String OASDatabaseUser = "oas";
	private static String OASDatabaseUserPassword = "oaspr5r";
	private static String OASDatabaseJDBCDriver = "oracle.jdbc.driver.OracleDriver";
	
	{
		try {
			ResourceBundle rb = ResourceBundle.getBundle("env");
			OASDatabaseJDBCDriver = rb.getString("oas.db.driver");
			OASDatabaseURL = rb.getString("oas.db.url");
			OASDatabaseUser = rb.getString("oas.db.user");
			OASDatabaseUserPassword = rb.getString("oas.db.password");
			haveDataSource = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static StudentCredentials[] getActiveRosters(Connection conn) {
		return TMSJDBC.getActiveRosters(conn);
	}
	
    public static RosterData getRosterData(Connection conn, StudentCredentials creds)  throws Exception {
    	String username = creds.getUsername();
    	String password = creds.getPassword();
    	String testAccessCode = creds.getAccesscode();
    	TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance();
        LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
        loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.OK_STATUS);
        response.getTmssvcResponse().setMethod("login_response");

    	// might be more than one roster for these creds, due to random passwords
    	AuthenticationData [] authDataArray = TMSJDBC.authenticateStudent(conn, username, password);
        AuthenticationData authData = null;
        boolean authenticated = false;
        int testRosterId = -1;
        String lsid = null;
        ManifestData [] manifestData = new ManifestData [0];
        for(int a=0;a<authDataArray.length && !authenticated;a++) {
            authData = authDataArray[a];
            testRosterId = authData.getTestRosterId();
            lsid = String.valueOf(testRosterId) + ":" + testAccessCode;
            loginResponse.setLsid(lsid);
            manifestData = TMSJDBC.getManifest(conn, testRosterId, testAccessCode);
            if(manifestData.length > 0) {
                authenticated = true;
                for (int i = 0; i < manifestData.length; i++) {
                    /*
                     * Retrieve scratchpad contents for this subtest.
                     */
                    manifestData[i].setScratchpadContent(TMSJDBC.getScratchpadContent(conn, testRosterId, manifestData[i].getId()));
                }
            }
        }
            
        if(authData.getRosterTestCompletionStatus().equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) ||
        		authData.getRosterTestCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS)) {
                loginResponse.setRestartFlag(true);
            } else {
            	loginResponse.setRestartFlag(false);
            }
        loginResponse.setRestartNumber(new BigInteger(String.valueOf(authData.getRestartNumber())));
        
            TestProduct testProduct = TMSJDBC.getProductForTestAdmin(conn, authData.getTestAdminId());
            //AuthenticateStudent authenticator = authenticatorFactory.create();

            if ("TB".equals(testProduct.getProductType())) {
                for(int i = 0;i<manifestData.length;i++) {
                    	if(!testAccessCode.equalsIgnoreCase(manifestData[i].getAccessCode()) &&
                    			manifestData[i].getTitle().indexOf("locator") >= 0 &&
                    			!manifestData[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS)) {
                    		response = TmssvcResponseDocument.Factory.newInstance();
            	            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
            	            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.LOCATOR_SUBTEST_NOT_COMPLETED_STATUS);
                    	}
                }
            }
            
            String logoURI = TMSJDBC.getProductLogo(conn,testProduct.getProductId());
            if (logoURI == null || "".equals(logoURI))
                logoURI = "/resources/logo.swf";
            loginResponse.addNewBranding().setTdclogo(logoURI);

		 if (authData.getRandomDistractorSeedNumber() != null) {


			 loginResponse.setRandomDistractorSeedNumber(
					 new BigInteger(String.valueOf( authData.
							 getRandomDistractorSeedNumber())));


		 }  else {

			 if (manifestData[0].getRandomDistractorStatus() != null && 
					 manifestData[0].getRandomDistractorStatus().equals("Y")) {

				 Integer ranodmSeedNumber = generateRandomNumber();

				 loginResponse.setRandomDistractorSeedNumber(
						 new BigInteger( String.valueOf(ranodmSeedNumber.intValue())));
			 }

		 }
        copyAuthenticationDataToResponse(loginResponse, authData);
        AccommodationsData accomData = TMSJDBC.getAccommodations(conn, testRosterId);
        
        if(accomData != null) {
            copyAccomodationsDataToResponse(loginResponse, accomData);
        }

        ConsolidatedRestartData restartData = null;
        if (loginResponse.getRestartFlag() && manifestData.length > 0 ){
        	restartData = loginResponse.addNewConsolidatedRestartData();
        }
        //END
        for(int i=0; i<manifestData.length ;i++) {
            if(Constants.StudentTestCompletionStatus.SCHEDULED_STATUS.equals(manifestData[i].getCompletionStatus()) ||
               Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS.equals(manifestData[i].getCompletionStatus()) ||
               Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
               Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
               Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS.equals(manifestData[i].getCompletionStatus())) {
                if(loginResponse.getRestartFlag()) {
                    manifestData[i].setTotalTime(TMSJDBC.getTotalElapsedTimeForSubtest(conn, testRosterId, manifestData[i].getId()));
                    int remSec = (manifestData[i].getScoDurationMinutes() * 60) - manifestData[i].getTotalTime();
                    ItemResponseData [] itemResponseData = TMSJDBC.getRestartItemResponses(conn, testRosterId, manifestData[i].getId());
                    //START Change For deferred defect 63502
                    copyRestartDataToResponse(lsid, testRosterId, manifestData[i].getId(), loginResponse, itemResponseData, remSec, 
                    		Integer.parseInt(manifestData[i].getAdsid()), manifestData[i].getScratchpadContentStr(), restartData);
                    		//END
                }
            }
        }
        copyManifestDataToResponse(conn, loginResponse, manifestData, testRosterId, authData.getTestAdminId(), testAccessCode);

        String tutorialResource = TMSJDBC.getTutorialResource(conn, testRosterId);
        boolean wasTutorialTaken = TMSJDBC.wasTutorialTaken(conn, testRosterId);
        if (tutorialResource!= null && !tutorialResource.trim().equals("")) {
            Tutorial tutorial =loginResponse.addNewTutorial();
            tutorial.setTutorialUrl(tutorialResource);
            tutorial.setDeliverTutorial(new BigInteger(wasTutorialTaken ? "0":"1"));
        }
        RosterData result = new RosterData();
        result.setDocument(response);
        result.setAuthData(authData);
        return result;
    }
    
    //START Change for Deferred defect 63502
    private static void copyRestartDataToResponse(String lsid, 
                                    int testRosterId, 
                                    int subtestItemSetId, 
                                    LoginResponse loginResponse, 
                                    ItemResponseData [] itemResponseData, 
                                    int remSec, 
                                    int adsAssessmentId, 
                                    String scratchpadContent,
                                    ConsolidatedRestartData restartData ) throws SQLException
    {
    //END
        //ConsolidatedRestartData restartData = loginResponse.addNewConsolidatedRestartData();
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
        }
    }
    
    private static void copyAuthenticationDataToResponse(LoginResponse response, AuthenticationData authData) throws AuthenticationFailureException, KeyEnteredResponsesException, OutsideTestWindowException, TestSessionCompletedException, TestSessionInProgressException, TestSessionNotScheduledException {
        response.addNewTestingSessionData();
        response.getTestingSessionData().addNewCmiCore();
        response.getTestingSessionData().getCmiCore().setStudentId(String.valueOf(authData.getStudentId()));
        response.getTestingSessionData().getCmiCore().setStudentLastName(authData.getStudentLastName());
        response.getTestingSessionData().getCmiCore().setStudentFirstName(authData.getStudentFirstName());
        response.getTestingSessionData().getCmiCore().setStudentMiddleName(authData.getStudentMiddleName());
    }
    
    private static void copyAccomodationsDataToResponse(LoginResponse response, AccommodationsData accomData) {
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
    
    private static void copyManifestDataToResponse(Connection conn, LoginResponse response, ManifestData [] manifestData, int testRosterId, int testAdminId, String accessCode) throws SQLException {
        response.addNewManifest();
        Manifest manifest = response.getManifest();
        String isUltimateAccessCode = TMSJDBC.isUltimateAccessCode(conn, testRosterId, testAdminId, accessCode);
        
        if(response.getRestartFlag()) {
	        ArrayList a = new ArrayList();
	        for(int i=0;i<manifestData.length;i++) {
	        	ManifestData data = manifestData[i];
	        	if(Constants.StudentTestCompletionStatus.COMPLETED_STATUS.equals(data.getCompletionStatus())){
	        		continue;
	        	}else{
	        		a.add(data);
	        	}
	        }
	        
	        manifestData = new ManifestData[a.size()];
	        a.toArray(manifestData);
	       // manifestData = (ManifestData [])a.toArray();
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
        		//System.out.println("***** In Else");
        		//System.out.println("RestartFlag: "+response.getRestartFlag()+", isUltimateAccessCode: " +isUltimateAccessCode+", CompletionStatus: "+data.getCompletionStatus());
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
    
	public static Connection getOASConnection() throws Exception {
		Connection newConn = null;
		try {    
			InitialContext ctx = new InitialContext();    
			javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup ("OASDataSource");    
			newConn = ds.getConnection();
			haveDataSource = true;
			System.out.println("*****  Using OASDataSource for DB connection");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			haveDataSource = false;
		}

		if(!haveDataSource) {
			// no OASDataSource available, falling back on local properties
			Properties props = new Properties();
			props.put("user", OASDatabaseUser);
			props.put("password", OASDatabaseUserPassword);
			Driver driver = (Driver) Class.forName(OASDatabaseJDBCDriver).newInstance();
			newConn = driver.connect(OASDatabaseURL, props);
			System.out.println("*****  Using local properties for DB connection");
		}

		return newConn;
	}
} 
