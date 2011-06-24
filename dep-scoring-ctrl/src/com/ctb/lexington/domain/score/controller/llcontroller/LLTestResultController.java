package com.ctb.lexington.domain.score.controller.llcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.ctb.lexington.data.ItemSetVO;
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
import com.ctb.lexington.domain.score.controller.llcontroller.StudentItemScoresController;
import com.ctb.lexington.domain.score.controller.llcontroller.StudentObjectiveScoresController;
import com.ctb.lexington.domain.score.controller.llcontroller.StudentResultStatusController;
import com.ctb.lexington.domain.teststructure.ValidationStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;

public class LLTestResultController implements TestResultController {
	 private Connection conn;
	 private ScoreMoveData data;
	 private ReportingLevels reportingLevels;
	Integer count;
	Boolean present = false;
	Long attrValue;
	String attrMultipleValue = "";
	 
	 
	 public LLTestResultController(Connection conn, ScoreMoveData data, ReportingLevels reportingLevels) {
	        this.conn = conn;
	        this.data = data;
	        this.reportingLevels = reportingLevels;
	    }
	 
	 
	 
	@Override
	public IrsDemographicData getIrsDemographics(StudentDemographicData data) {
		final IrsDemographicData details = new IrsDemographicData();
		Map rd = data.getResearchData();
		// Ethnicity
		details.setAttr2Id(new Long(34));
		if(rd.containsKey("Ethnicity")) {
			ArrayList<String> Ethnicity = (ArrayList<String>)rd.get("Ethnicity");
			String ethnicity = Ethnicity.get(0);
			details.setAttr2Id(new Long(("American Indian or Alaska Native".equals(ethnicity))?17:
                                ("African American or Black, Not Hispanic".equals(ethnicity))?18:
                                ("Asian".equals(ethnicity))?19:
                                ("Pacific Islander".equals(ethnicity))?20:
                                ("Hispanic or Latino".equals(ethnicity))?21:
                                ("White, Not Hispanic".equals(ethnicity))?22:
                                ("Multiethnic".equals(ethnicity))?23:
                                ("Other".equals(ethnicity))?24:
                                ("mexicano".equals(ethnicity))?25:
                                ("mexicano-americano".equals(ethnicity))?26:
                                ("cubano".equals(ethnicity))?27:
                                ("cubano-americano".equals(ethnicity))?28:
                                ("puertorriqueño".equals(ethnicity))?29:
                                ("dominicano".equals(ethnicity))?30:
                                ("centroamericano".equals(ethnicity))?31:
                                ("sudamericano".equals(ethnicity))?32:
                                ("otro".equals(ethnicity))?33:34));
		}
		
		// Home Language
		details.setAttr17Id(new Long(101));
		if(rd.containsKey("Home Language")) {
			present = false;
			ArrayList<String> Home_Language = (ArrayList<String>)rd.get("Home Language");
			String homeLanguage = Home_Language.get(0);
			attrValue = new Long(("00".equals(homeLanguage))?1:
				            	("01".equals(homeLanguage))?2:
				                ("02".equals(homeLanguage))?3:
				                ("03".equals(homeLanguage))?4:
				                ("04".equals(homeLanguage))?5:
				                ("05".equals(homeLanguage))?6:
				                ("06".equals(homeLanguage))?7:
				                ("07".equals(homeLanguage))?8:
				                ("08".equals(homeLanguage))?9:
				                ("09".equals(homeLanguage))?10:11);
			if(attrValue.intValue() == 11) {
				for(count=10; count<=99; count++) {
					if(homeLanguage.equals(count.toString())){
						attrValue = new Long(count+1);
						present = true;
						break;
					}
				}
				if(!present)
					attrValue = new Long(101);
			}
			details.setAttr17Id(attrValue);
		}
		
		
		// Mobility
		details.setAttr18Id(new Long(13));
		if(rd.containsKey("Mobility")) {
			ArrayList<String> Mobility = (ArrayList<String>)rd.get("Mobility");
				String mobility = Mobility.get(0); 	
	    	attrValue = (new Long(("K".equals(mobility) || ("k".equals(mobility)))?1:2));
			present = false;
			if(attrValue.intValue() == 2){
				if(Integer.valueOf(mobility).intValue()> 0 && Integer.valueOf(mobility).intValue() <=12) {
					int value = Integer.valueOf(mobility).intValue();
					attrValue = new Long(value+1);
					present = true;
				}
				/*for(count=1; count<=12; count++) {
					if(mobility.equals(count.toString())){
						attrValue = new Long(count+1);
						present = true;
						break;
					}
				}*/
				if(!present)
					attrValue = new Long(13);		
			}
			details.setAttr18Id(attrValue);
		}
		
		
		// USA School Enrollment
		details.setAttr19Id(new Long(122));
		if(rd.containsKey("USA School Enrollment")) {
			ArrayList<String> USASchoolEnrollment = (ArrayList<String>)rd.get("USA School Enrollment");
			String enrollment = USASchoolEnrollment.get(0); 	
			present = false;
			if(Integer.valueOf(enrollment).intValue()> 1899 && Integer.valueOf(enrollment).intValue() <=2020) {
				int value = Integer.valueOf(enrollment).intValue();
				attrValue = new Long(value-1899);
				present = true;
			}
			/*for(count=1900; count<=2020; count++) {
				if(enrollment.equals(count.toString())){
					attrValue = new Long(count-1899);
					present = true;
					break;
				}
			}*/
			if(!present)
				attrValue = new Long(count-1899);		
			details.setAttr19Id(attrValue);
		}
		
		
		//Program Participation
		details.setAttr20Id("6");
		if(rd.containsKey("Program Participation")) {
			ArrayList<String> ProgramParticipation = (ArrayList<String>)rd.get("Program Participation");
			String[] programParticipation = new String[ProgramParticipation.size()];
			for(int i=0; i<ProgramParticipation.size();i++){
					programParticipation[i] = ProgramParticipation.get(i).toString();
			}
			
			present = false;
			attrMultipleValue = "";
			for(int i=0;i<programParticipation.length;i++){
				if(programParticipation[i].equals("ESEA Title 1")){
					attrMultipleValue = attrMultipleValue + ",1";
					present = true;
				}
				if(programParticipation[i].equals("English Language Learner (ESEA Title III)")){
					attrMultipleValue = attrMultipleValue + ",2";
					present = true;
				}
				if(programParticipation[i].equals("Gifted and Talented")){
					attrMultipleValue = attrMultipleValue + ",3";
					present = true;
				}
				if(programParticipation[i].equals("Indian Education")){
					attrMultipleValue = attrMultipleValue + ",4";
					present = true;
				}
				if(programParticipation[i].equals("Migrant Education")){
					attrMultipleValue = attrMultipleValue + ",5";
					present = true;
				}
			}
			if(!present)
				attrMultipleValue = "6";
			else
			{
				if(attrMultipleValue.startsWith(","))
					attrMultipleValue = attrMultipleValue.substring(1);
			}
			details.setAttr20Id(attrMultipleValue);
		}
		
		
		// Special Education
		details.setAttr21Id("3");
		if(rd.containsKey("Special Education")) {
			ArrayList<String> SpecialEducation = (ArrayList<String>)rd.get("Special Education");
			String[] specialEducation = new String[SpecialEducation.size()];
			for(int i=0; i<SpecialEducation.size();i++)
				specialEducation[i] = SpecialEducation.get(i).toString();
			present = false;
			attrMultipleValue = "";
			for(int i=0;i<specialEducation.length;i++){
				if(specialEducation[i].equals("IEP")){
					attrMultipleValue = attrMultipleValue + ",1";
					present = true;
				}
				if(specialEducation[i].equals("504")){
					attrMultipleValue = attrMultipleValue + ",2";
					present = true;
				}
			}
			if(!present)
				attrMultipleValue = "3";
			else
			{
				if(attrMultipleValue.startsWith(","))
					attrMultipleValue = attrMultipleValue.substring(1);
			}
			details.setAttr21Id(attrMultipleValue);
		}
		
		
		//Disability
		details.setAttr22Id(new Long(13));
		if(rd.containsKey("Disability")) {
			ArrayList<String> Disability = (ArrayList<String>)rd.get("Disability");
			String disability = Disability.get(0);
			details.setAttr22Id(new Long(("A".equals(disability))?1:
	                                ("D".equals(disability))?2:
	                                ("HI".equals(disability))?3:
	                                ("MU".equals(disability))?4:
	                                ("OI".equals(disability))?5:
	                                ("OHI".equals(disability))?6:
	                                ("SED".equals(disability))?7:
	                                ("LN".equals(disability))?8:
	                                ("SLI".equals(disability))?9:
	                                ("TBI".equals(disability))?10:
	                                ("VI".equals(disability))?11:
	                                ("ME".equals(disability))?12:13));
		}
		
		
		//Accommodations
		details.setAttr23Id("29");
		if(rd.containsKey("Accommodations")) {
			ArrayList<String> Accommodations = (ArrayList<String>)rd.get("Accommodations");
			String[] accommodations = new String[Accommodations.size()];
			for(int i=0; i<Accommodations.size();i++)
				accommodations[i] = Accommodations.get(i).toString();
			present = false;
			attrMultipleValue = "";
			for(int i=0;i<accommodations.length;i++){
				if(accommodations[i].equals("DC-S")){
					attrMultipleValue = attrMultipleValue + ",1";
					present = true;
				}
				if(accommodations[i].equals("DC-L")){
					attrMultipleValue = attrMultipleValue + ",2";
					present = true;
				}
				if(accommodations[i].equals("DC-RD")){
					attrMultipleValue = attrMultipleValue + ",3";
					present = true;
				}
				if(accommodations[i].equals("DC-WR")){
					attrMultipleValue = attrMultipleValue + ",4";
					present = true;
				}
				if(accommodations[i].equals("RQE-S")){
					attrMultipleValue = attrMultipleValue + ",5";
					present = true;
				}
				if(accommodations[i].equals("RQE-L")){
					attrMultipleValue = attrMultipleValue + ",6";
					present = true;
				}
				if(accommodations[i].equals("RQE-RD")){
					attrMultipleValue = attrMultipleValue + ",7";
					present = true;
				}
				if(accommodations[i].equals("RQE-WR")){
					attrMultipleValue = attrMultipleValue + ",8";
					present = true;
				}
				if(accommodations[i].equals("RPE-S")){
					attrMultipleValue = attrMultipleValue + ",9";
					present = true;
				}
				if(accommodations[i].equals("RPE-L")){
					attrMultipleValue = attrMultipleValue + ",10";
					present = true;
				}
				if(accommodations[i].equals("RPE-RD")){
					attrMultipleValue = attrMultipleValue + ",11";
					present = true;
				}
				if(accommodations[i].equals("RPE-WR")){
					attrMultipleValue = attrMultipleValue + ",12";
					present = true;
				}
				if(accommodations[i].equals("RSR-S")){
					attrMultipleValue = attrMultipleValue + ",13";
					present = true;
				}
				if(accommodations[i].equals("RSR-L")){
					attrMultipleValue = attrMultipleValue + ",14";
					present = true;
				}
				if(accommodations[i].equals("RSR-RD")){
					attrMultipleValue = attrMultipleValue + ",15";
					present = true;
				}
				if(accommodations[i].equals("RSR-WR")){
					attrMultipleValue = attrMultipleValue + ",16";
					present = true;
				}
				if(accommodations[i].equals("SA-S")){
					attrMultipleValue = attrMultipleValue + ",17";
					present = true;
				}
				if(accommodations[i].equals("SA-L")){
					attrMultipleValue = attrMultipleValue + ",18";
					present = true;
				}
				if(accommodations[i].equals("SA-RD")){
					attrMultipleValue = attrMultipleValue + ",19";
					present = true;
				}
				if(accommodations[i].equals("SA-WR")){
					attrMultipleValue = attrMultipleValue + ",20";
					present = true;
				}
				if(accommodations[i].equals("ASM-S")){
					attrMultipleValue = attrMultipleValue + ",21";
					present = true;
				}
				if(accommodations[i].equals("ASM-L")){
					attrMultipleValue = attrMultipleValue + ",22";
					present = true;
				}
				if(accommodations[i].equals("ASM-RD")){
					attrMultipleValue = attrMultipleValue + ",23";
					present = true;
				}
				if(accommodations[i].equals("ASM-WR")){
					attrMultipleValue = attrMultipleValue + ",24";
					present = true;
				}
				if(accommodations[i].equals("RDNL-S")){
					attrMultipleValue = attrMultipleValue + ",25";
					present = true;
				}
				if(accommodations[i].equals("RDNL-L")){
					attrMultipleValue = attrMultipleValue + ",26";
					present = true;
				}
				if(accommodations[i].equals("RDNL-RD")){
					attrMultipleValue = attrMultipleValue + ",27";
					present = true;
				}
				if(accommodations[i].equals("RDNL-WR")){
					attrMultipleValue = attrMultipleValue + ",28";
					present = true;
				}
			}
			if(!present)
				attrMultipleValue = "29";
			else
			{
				if(attrMultipleValue.startsWith(","))
					attrMultipleValue = attrMultipleValue.substring(1);
			}
			details.setAttr23Id(attrMultipleValue);
		}
		
		
		// Special Codes
		Integer specialCode = null;
		details.setAttr25Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-K")) {		
			ArrayList K = (ArrayList)rd.get("SPECIAL CODES-K");
			specialCode = Integer.valueOf((String)K.get(0));
			details.setAttr25Id(new Long(calculateValue(specialCode)));
		}
		details.setAttr26Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-L")) {	
			ArrayList L = (ArrayList)rd.get("SPECIAL CODES-L");
			specialCode = Integer.valueOf((String)L.get(0));
			details.setAttr26Id(new Long(calculateValue(specialCode)));
		}
		details.setAttr27Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-M")) {	
			ArrayList M = (ArrayList)rd.get("SPECIAL CODES-M");
			specialCode = Integer.valueOf((String)M.get(0));
			details.setAttr27Id(new Long(calculateValue(specialCode)));
		}
		details.setAttr28Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-N")) {	
			ArrayList N = (ArrayList)rd.get("SPECIAL CODES-N");
			specialCode = Integer.valueOf((String)N.get(0));
			details.setAttr28Id(new Long(calculateValue(specialCode)));
		}
		details.setAttr29Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-O")) {	
			ArrayList O = (ArrayList)rd.get("SPECIAL CODES-O");
			specialCode = Integer.valueOf((String)O.get(0));
			details.setAttr29Id(new Long(calculateValue(specialCode)));
		}
		details.setAttr30Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-P")) {	
			ArrayList P = (ArrayList)rd.get("SPECIAL CODES-P");
			specialCode = Integer.valueOf((String)P.get(0));
			details.setAttr30Id(new Long(calculateValue(specialCode)));
		}
		details.setAttr31Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-Q")) {	
			ArrayList Q = (ArrayList)rd.get("SPECIAL CODES-Q");
			specialCode = Integer.valueOf((String)Q.get(0));
			details.setAttr31Id(new Long(calculateValue(specialCode)));
		}
		details.setAttr32Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-R")) {	
			ArrayList R = (ArrayList)rd.get("SPECIAL CODES-R");
			specialCode = Integer.valueOf((String)R.get(0));
			details.setAttr32Id(new Long(calculateValue(specialCode)));
		}
		details.setAttr33Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-S")) {	
			ArrayList S = (ArrayList)rd.get("SPECIAL CODES-S");
			specialCode = Integer.valueOf((String)S.get(0));
			details.setAttr33Id(new Long(calculateValue(specialCode)));
		}
		details.setAttr34Id(new Long(11));
		if(rd.containsKey("SPECIAL CODES-T")) {	
			ArrayList T = (ArrayList)rd.get("SPECIAL CODES-T");
			specialCode = Integer.valueOf((String)T.get(0));
			details.setAttr34Id(new Long(calculateValue(specialCode)));
		}
		
		details.setAttr9Id(new Long(("Y".equals(data.getScreenMagnifier()))?4:
        					("T".equals(data.getScreenMagnifier()))?4:5));
		details.setAttr11Id(new Long(("Y".equals(data.getScreenReader()))?4:
							("T".equals(data.getScreenReader()))?4:5));
		details.setAttr12Id(new Long(("Y".equals(data.getCalculator()))?4:
							("T".equals(data.getCalculator()))?4:5));
		details.setAttr13Id(new Long(("Y".equals(data.getTestPause()))?4:
							("T".equals(data.getTestPause()))?4:5));
		details.setAttr14Id(new Long(("Y".equals(data.getUntimedTest()))?4:
							("T".equals(data.getUntimedTest()))?4:5));
		details.setAttr15Id(new Long((data.getQuestionBGColor() != null || data.getQuestionFontColor() != null)?4:5));
		details.setAttr16Id(new Long((data.getQuestionFontSize() != null)?4:5));
		details.setAttr35Id(new Long((data.getMusicFileId() != null)?1:2));
		details.setAttr36Id(new Long(("Y".equals(data.getMaskingRuler()))?1:
			("T".equals(data.getMaskingRuler()))?1:2));
		details.setAttr37Id(new Long(("Y".equals(data.getMagnifyingGlass()))?1:
			("T".equals(data.getMagnifyingGlass()))?1:2));		
     
	 return details;
	}
	
	public int calculateValue(int code) {
		if(code >= 0 && code < 10){
			return ++code;
		}
		return 11;
	}
	
	

	@Override
	public void run(ValidationStatus rosterValidationStatus)
			throws IOException, DataException, CTBSystemException, SQLException {
		IrsDemographicData demographicData = getIrsDemographics(data.getDemographicData());
        OrgNodeData orgNodeData = data.getOrgNodeData();
        StudentData studentData = data.getStudentData();
        AdminData adminData = data.getAdminData();
        CurriculumData curriculumData = data.getCurriculumData();
        StudentTestData testData = data.getTestData();
        StudentSubtestScoresData studentSubtestScoresData = data.getStudentSubtestScoresData();
        StudentScoreSummaryData studentScoreSummaryData = data.getStudentScoreSummaryData(); //to be clarified
        StudentItemScoreData studentItemScoreData = data.getStudentItemScoreData();
        StsTestResultFactData factData = data.getStsTestResultFactData();
        UserData userData = data.getUserData();
        StsTotalStudentScoreData totalStudentScoreData = data.getStsTotalStudentScoreData();
        StudentPredictedScoresData predictedData = data.getStudentPredictedScoresData();
        StudentItemResponseData studentItemResponseData = data.getStudentItemResponseData();
        // persist context
        new OrgNodeController(conn, orgNodeData, adminData).run();
        new StudentController(conn, studentData).run();
        new AdminController(conn, adminData, curriculumData, new Long(
                studentData.getGrade().equals("K")?14:
                    studentData.getGrade().equals("1")?15:
                    studentData.getGrade().equals("2")?16:
                    studentData.getGrade().equals("3")?17:
                    studentData.getGrade().equals("4")?18:
                    studentData.getGrade().equals("5")?19:
                    studentData.getGrade().equals("6")?20:
                    studentData.getGrade().equals("7")?21:
                    studentData.getGrade().equals("8")?22:
                    studentData.getGrade().equals("9")?23:
                    studentData.getGrade().equals("10")?24:
                    studentData.getGrade().equals("11")?25:
                    studentData.getGrade().equals("12")?26:27)).run();
        
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
                studentData.getGrade().equals("K")?14:
                studentData.getGrade().equals("1")?15:
                studentData.getGrade().equals("2")?16:
                studentData.getGrade().equals("3")?17:
                studentData.getGrade().equals("4")?18:
                studentData.getGrade().equals("5")?19:
                studentData.getGrade().equals("6")?20:
                studentData.getGrade().equals("7")?21:
                studentData.getGrade().equals("8")?22:
                studentData.getGrade().equals("9")?23:
                studentData.getGrade().equals("10")?24:
                studentData.getGrade().equals("11")?25:
                studentData.getGrade().equals("12")?26:27));
        context.setAssessmentId(adminData.getAssessmentId());
        context.setAssessmentType(adminData.getAssessmentType());
        context.setProgramId(adminData.getProgramId());
        context.setDemographicData(demographicData);
        
      //  new CurriculumController(conn, curriculumData, adminData, context).run();
        System.out.println("***** SCORING: Persisted dimension data.");
        
        // persist scores
        new StudentCompositeScoresController(conn, totalStudentScoreData, curriculumData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted composite fact data.");
        new StudentContentAreaScoresController(conn, studentSubtestScoresData, factData, curriculumData, testData, adminData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted content area fact data.");
        new StudentObjectiveScoresController(conn, studentScoreSummaryData, curriculumData, testData, adminData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted objective fact data."); // to be clarified
        new StudentItemScoresController(conn, studentItemScoreData, studentItemResponseData, curriculumData, testData, adminData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted item fact data.");
    
        new StudentResultStatusController(conn, context).run();
        System.out.println("***** SCORING: Marked prior results non-current as necessary.");

	}

}
