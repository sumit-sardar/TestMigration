package com.ctb.lexington.domain.score.controller.tvcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimeZone;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.httpclient.protocol.Protocol;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.OrgNodeData;
import com.ctb.lexington.db.data.ReportingLevels;
import com.ctb.lexington.db.data.ScoreMoveData;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StudentData;
import com.ctb.lexington.db.data.StudentDemographicData;
import com.ctb.lexington.db.data.StudentItemResponseData;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.UserData;
import com.ctb.lexington.db.irsdata.IrsDemographicData;
import com.ctb.lexington.domain.score.controller.AdminController;
import com.ctb.lexington.domain.score.controller.CurriculumController;
import com.ctb.lexington.domain.score.controller.OrgNodeController;
import com.ctb.lexington.domain.score.controller.StudentController;
import com.ctb.lexington.domain.score.controller.TestResultController;
import com.ctb.lexington.domain.teststructure.ValidationStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.AuthenticatedUser;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.CompositeScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ContentAreaScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ItemScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.PrimaryObjScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ScoringStatus;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.SecondaryObjScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.StudentScore;

public class TVTestResultController implements TestResultController {
    private Connection conn;
    private ScoreMoveData data;
    private ReportingLevels reportingLevels;

    public TVTestResultController(Connection conn, ScoreMoveData data, ReportingLevels reportingLevels) {
        this.conn = conn;
        this.data = data;
        this.reportingLevels = reportingLevels;
    }

    public void run(ValidationStatus rosterValidationStatus) throws IOException, DataException, CTBSystemException, SQLException {
        // gather collected data
    	IrsDemographicData demographicData = getIrsDemographics(data.getDemographicData());
        OrgNodeData orgNodeData = data.getOrgNodeData();
        StudentData studentData = data.getStudentData();
        AdminData adminData = data.getAdminData();
        CurriculumData curriculumData = data.getCurriculumData();
        StudentTestData testData = data.getTestData();
        StudentSubtestScoresData studentSubtestScoresData = data.getStudentSubtestScoresData();
        StudentScoreSummaryData studentScoreSummaryData = data.getStudentScoreSummaryData();
        StudentItemScoreData studentItemScoreData = data.getStudentItemScoreData();
        StsTestResultFactData factData = data.getStsTestResultFactData();
        UserData userData = data.getUserData();
        StsTotalStudentScoreData totalStudentScoreData = data.getStsTotalStudentScoreData();
        StudentPredictedScoresData predictedData = data.getStudentPredictedScoresData();
        StudentItemResponseData studentItemResponseData = data.getStudentItemResponseData();
        // persist context
        new OrgNodeController(conn, orgNodeData, adminData).run();
        new StudentController(conn, studentData).run();
        new AdminController(conn, adminData, curriculumData, new Long(studentData.getGrade().equals("AD")?1:2)).run();
        
        // gather context dim ids for fact records
        ContextData context = new ContextData();
        
        Timestamp completionTime = testData.getMaxCompletionTime(adminData.getTimeZone());
        if(completionTime == null) {
            int tzOffset = TimeZone.getTimeZone(adminData.getTimeZone()).getOffset(System.currentTimeMillis());
            completionTime = new Timestamp(System.currentTimeMillis() + tzOffset);
        }

        context.setTestStartTimestamp(studentData.getStartDateTime());
        context.setTestCompletionTimestamp(completionTime);
        context.setOrgNodeId(orgNodeData.getOrgNodeId());
        context.setStudentId(studentData.getOasStudentId());
        context.setSessionId(adminData.getSessionId());
        context.setCurrentResultId(new Long(rosterValidationStatus.isValid()?1:2));
        context.setGradeId(new Long(
                           studentData.getGrade().equals("3")?3:
                           studentData.getGrade().equals("4")?4:
                           studentData.getGrade().equals("5")?5:
                           studentData.getGrade().equals("6")?6:
                           studentData.getGrade().equals("7")?7:
                           studentData.getGrade().equals("8")?8:
                           studentData.getGrade().equals("9")?9:
                           studentData.getGrade().equals("10")?10:
                           studentData.getGrade().equals("11")?11:
                           studentData.getGrade().equals("12")?12:13));
        context.setAssessmentId(adminData.getAssessmentId());
        context.setAssessmentType(adminData.getAssessmentType());
        context.setProgramId(adminData.getProgramId());
        context.setDemographicData(demographicData);
        
        new CurriculumController(conn, curriculumData, adminData, context).run();
        System.out.println("***** SCORING: Persisted dimension data.");
        
        // persist scores
     //   new StudentPredictedScoresController(conn, predictedData, curriculumData, context).run();
        
        System.out.println("***** SCORING: TestResultController: Persisted predicted fact data.");
        new StudentCompositeScoresController(conn, totalStudentScoreData, predictedData, curriculumData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted composite fact data.");
        new StudentContentAreaScoresController(conn, studentSubtestScoresData, factData, curriculumData, testData, adminData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted content area fact data.");
        new StudentObjectiveScoresController(conn, studentScoreSummaryData, curriculumData, testData, adminData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted objective fact data.");
        new StudentItemScoresController(conn, studentItemScoreData, studentItemResponseData, curriculumData, testData, adminData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted item fact data.");
    
        new StudentResultStatusController(conn, context).run();
        System.out.println("***** SCORING: Marked prior results non-current as necessary.");
        
        // Added for TN Acuity webservice integration
        if(adminData.getProductId() == 3700) {
        	StudentScore studentScore = new StudentScore();
        	studentScore.setStudentId(studentData.getOasStudentId());
        	studentScore.setSessionId(adminData.getSessionId());
        	studentScore.setFormId(testData.get(0).getTestForm());
        	if(testData.get(0).getTestLevel().trim().equals("21-22") || testData.get(0).getTestLevel().trim().equals("21/22")) {
        		studentScore.setLevelId(21);
        	} else {
        		studentScore.setLevelId(Integer.parseInt(testData.get(0).getTestLevel().trim()));
        	}
        	new TVWsAcuityDataController(curriculumData, context, factData, studentScore, studentScoreSummaryData, data.getCaLossHoss(), 
        			adminData, studentItemScoreData, totalStudentScoreData, conn, testData);
        	AuthenticatedUser user_arg = new AuthenticatedUser();
    		user_arg.setUsername("User");
    		user_arg.setPassword("Password");
    		String endPointUrl = data.getUrlData().getWebserviceUrl();
	    	ScoringStatus status = null;
	    	ScoringServiceStub stub = null;
	    	try {
        		//final ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem("./repo", null);
        		// 1.) unregister the current https protocol.  
	            org.apache.commons.httpclient.protocol.Protocol.unregisterProtocol("https");  
	               
	            // 2.) reregister the new https protocol to use the easy ssl protocol socked factory.  
	            org.apache.commons.httpclient.protocol.Protocol.registerProtocol("https",  
	             new Protocol("https", new org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory(), 443));  
	           
        		stub = new ScoringServiceStub(endPointUrl);
        		//stub._getServiceClient().engageModule("logging");
        		status = stub.processStudentScore(user_arg, studentScore);
		  	} catch (Exception e) {
		  		e.printStackTrace();
        	} finally {
        		if(status != null) {
        			System.out.println("status.getStudentId() -> " + status.getStudentId());
    				System.out.println("status.getSessionId() -> " + status.getSessionId());
    				System.out.println("status.getErrorMsg() -> " + status.getErrorMsg());
    				System.out.println("status.getStatus() -> " + status.getStatus());
        		}
        		//displayScoresInRequest(studentScore);
        	}
        	
        	System.out.println("Stop");
        }
        
    }
    
    public void displayScoresInRequest(StudentScore studentScore) {
    	if(studentScore != null) {
    		System.out.println("studentId - " + studentScore.getStudentId());
    		System.out.println("formId - " + studentScore.getFormId());
    		System.out.println("sessionId - " + studentScore.getSessionId());
    		System.out.println("levelId - " + studentScore.getLevelId());
    		if(studentScore.getContentAreaScores() != null) {
    			ContentAreaScore[] caScore = studentScore.getContentAreaScores();
    			for(int i = 0; i < caScore.length; i++) {
    				System.out.println("contentAreaId - " + caScore[i].getContentAreaId());
    				System.out.println("contentAreaName - " + caScore[i].getContentAreaName());
    				System.out.println("scaleScore - " + caScore[i].getScaleScore());
    				System.out.println("sem - " + caScore[i].getSem());
    				System.out.println("minScaleScore - " + caScore[i].getMinScaleScore());
    				System.out.println("maxScaleScore - " + caScore[i].getMaxScaleScore());
    				System.out.println("gradeEquivalent - " + caScore[i].getGradeEquivalent());
    				System.out.println("nationalStanine - " + caScore[i].getNationalStanine());
    				System.out.println("nationalPercentile - " + caScore[i].getNationalPercentile());
    				System.out.println("normalCurveEquivalent - " + caScore[i].getNormCurveEquivalent());
    				System.out.println("percentageMastery - " + caScore[i].getPercentMastery());
    				System.out.println("pointsObtained - " + caScore[i].getPointsObtained());
    				System.out.println("percentObtained - " + caScore[i].getPercentObtained());
    				System.out.println("pointsPossible - " + caScore[i].getPointsPossible());
    				System.out.println("pointsAttempted - " + caScore[i].getPointsAttempted());
    				PrimaryObjScore[] priScore = caScore[i].getPrimaryObjScores();
    				if(priScore != null) {
    					for(int j = 0; j < priScore.length; j++) {
    						System.out.println("primaryObjectiveId - " + priScore[j].getPrimaryObjId());
    						System.out.println("pointsAttempted - " + priScore[j].getPointsAttempted());
    						System.out.println("pointsObtained - " + priScore[j].getPointsObtained());
    						System.out.println("pointsPossible - " + priScore[j].getPointsPossible());
    						System.out.println("percentObtained - " + priScore[j].getPercentObtained());
    						System.out.println("masteryLevel - " + priScore[j].getMasteryLevel());
    						System.out.println("nationalAverage - " + priScore[j].getNationalAverage());
    						System.out.println("lowMmr - " + priScore[j].getLowModMasteryRange());
    						System.out.println("highMmr - " + priScore[j].getHighModMasteryRange());
    						SecondaryObjScore[] secScore = priScore[j].getSecondaryObjScores();
    						if(secScore != null) {
    							for(int k = 0; k < secScore.length; k++) {
    								System.out.println("secondaryObjectiveId - " + secScore[k].getSecondaryObjId());
    								System.out.println("pointsAttempted - " + secScore[k].getPointsAttempted());
    								System.out.println("pointsObtained - " + secScore[k].getPercentObtained());
    								System.out.println("pointsPossible - " + secScore[k].getPointsPossible());
    								System.out.println("percentObtained - " + secScore[k].getPercentObtained());
    								System.out.println("masteryLevel - " + secScore[k].getMasteryLevel());
    								ItemScore[] itemScore = secScore[k].getItemScores();
    								if(itemScore != null) {
    									for(int l = 0; l < itemScore.length; l ++) {
    										System.out.println("itemId - " + itemScore[l].getItemId());
    										System.out.println("pointsObtained - " + itemScore[l].getPointsObtained());
    										System.out.println("pointsPossible - " + itemScore[l].getPointsPossible());
    										System.out.println("nationalAverage - " + itemScore[l].getNationalAverage());
    									}
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    		if(studentScore.getCompositeScores() != null) {
    			CompositeScore[] comp = studentScore.getCompositeScores();
    			for(int i = 0; i < comp.length; i ++) {
    				System.out.println("compositeId - " + comp[i].getCompositeId());
    				System.out.println("compositeName - " + comp[i].getCompositeName());
    				System.out.println("scaleScore - " + comp[i].getScaleScore());
    				System.out.println("gradeEquivalent - " + comp[i].getGradeEquivalent());
    				System.out.println("nationalStanine - " + comp[i].getNationalStanine());
    				System.out.println("nationalPercentile - " + comp[i].getNationalPercentile());
    				System.out.println("normalCurveEquivalent - " + comp[i].getNormCurveEquivalent());
    				System.out.println("pointsObtained - " + comp[i].getPointsObtained());
    				System.out.println("pointsPossible - " + comp[i].getPointsPossible());
    				System.out.println("pointsAttempted - " + comp[i].getPointsAttempted());
    			}
    		}
    	}
    }
    
    public IrsDemographicData getIrsDemographics(StudentDemographicData data){
    		final IrsDemographicData details = new IrsDemographicData();
    		Map rd = data.getResearchData();
        
    		details.setAttr1Id(new Long(4));
    		if(rd.containsKey("ELL")) {
    			ArrayList<String> lell = (ArrayList<String>)rd.get("ELL");
	    		String ELL = lell.get(0);
	    		details.setAttr1Id(new Long(("Yes".equals(ELL) || ("Y".equals(ELL)))?3:
                                    ("True".equals(ELL) || ("T".equals(ELL)))?3:4));
    		}
    	 
    		details.setAttr2Id(new Long(16));
    		if(rd.containsKey("Ethnicity")) {
    			ArrayList<String> lethnicity = (ArrayList<String>)rd.get("Ethnicity");
	    		String ethnicity = lethnicity.get(0);
	    		details.setAttr2Id(new Long(("African American or Black, Not Hispanic".equals(ethnicity))?8:
                              ("American Indian or Alaska Native".equals(ethnicity))?9:
                              ("Asian".equals(ethnicity))?10:
                              ("Pacific Islander".equals(ethnicity))?11:
                              ("Hispanic or Latino".equals(ethnicity))?12:
                              ("White, Not Hispanic".equals(ethnicity))?13:
                              ("Multi-ethnic".equals(ethnicity))?14:
                              ("Other".equals(ethnicity))?15:16));
    		}
    		
    		details.setAttr3Id(new Long(4));
    		if(rd.containsKey("Free Lunch")) {
    			ArrayList<String> lunch = (ArrayList<String>)rd.get("Free Lunch");
	    		String freelunch = lunch.get(0);
	    		details.setAttr3Id(new Long(("Yes".equals(freelunch) || ("Y".equals(freelunch)))?3:
                                    ("True".equals(freelunch) || ("T".equals(freelunch)))?3:4));
    		}
    		details.setAttr4Id(new Long(("Male".equals(data.getGender()))?5:
    								("Female".equals(data.getGender()))?4:
                                    ("M".equals(data.getGender()))?5:
                                    ("F".equals(data.getGender()))?4:6));
    	
    		details.setAttr5Id(new Long(4));
    		if(rd.containsKey("IEP")) {
    			ArrayList<String> iepl = (ArrayList<String>)rd.get("IEP");
	    			String iep = iepl.get(0);
	    			details.setAttr5Id(new Long(("Yes".equals(iep) || ("Y".equals(iep)))?3:
                                    ("True".equals(iep) || ("T".equals(iep)))?3:4));
    		}
                                    
    		details.setAttr6Id(new Long(9));
    		
    		details.setAttr7Id(new Long(4));
    		if(rd.containsKey("LEP")) {
    			ArrayList<String> llep = (ArrayList<String>)rd.get("LEP");
    			String lep = llep.get(0);
    			details.setAttr7Id(new Long(("Yes".equals(lep) || ("Y".equals(lep)))?3:
                                    ("True".equals(lep) || ("T".equals(lep)))?3:4));
    		}
    		
    		details.setAttr8Id(new Long(4));
    		if(rd.containsKey("Migrant")) {        
    			ArrayList<String> mig = (ArrayList<String>)rd.get("Migrant");
    			String migrant = mig.get(0);
    			details.setAttr8Id(new Long(("Yes".equals(migrant) || "Y".equals(migrant))?3:
                                    ("True".equals(migrant) || "T".equals(migrant))?3:4));
    		}
        
    		details.setAttr9Id(new Long(("Y".equals(data.getScreenMagnifier()))?3:
            					("T".equals(data.getScreenMagnifier()))?3:4));
    		details.setAttr11Id(new Long(("Y".equals(data.getScreenReader()))?3:
								("T".equals(data.getScreenReader()))?3:4));
    		details.setAttr12Id(new Long(("Y".equals(data.getCalculator()))?3:
								("T".equals(data.getCalculator()))?3:4));
    		details.setAttr13Id(new Long(("Y".equals(data.getTestPause()))?3:
								("T".equals(data.getTestPause()))?3:4));
    		details.setAttr14Id(new Long(("Y".equals(data.getUntimedTest()))?3:
								("T".equals(data.getUntimedTest()))?3:4));
    		details.setAttr15Id(new Long((data.getQuestionBGColor() != null || data.getQuestionFontColor() != null)?3:4));
    		details.setAttr16Id(new Long((data.getQuestionFontSize() != null)?3:4));
    	
    		details.setAttr10Id(new Long(4));
    		if(rd.containsKey("Section 504")) { 
    			ArrayList<String> section = (ArrayList<String>)rd.get("Section 504");
    			String sec504 = section.get(0);
    			details.setAttr10Id(new Long(("Yes".equals(sec504) || "Y".equals(sec504))?3:
                                    ("True".equals(sec504) || "T".equals(sec504))?3:4));
    		}
         
    	 return details;
    }

}