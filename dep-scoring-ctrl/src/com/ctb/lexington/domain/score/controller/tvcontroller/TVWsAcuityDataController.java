package com.ctb.lexington.domain.score.controller.tvcontroller;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.WsReportingDataTV;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.irsdata.irstvdata.WsTVContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstvdata.WsTVPrimObjFactData;
import com.ctb.lexington.domain.teststructure.MasteryLevel;

/**
 * @author TCS
 * New controller to design score objects to provide to Acuity. 
 */

public class TVWsAcuityDataController {
	
	public TVWsAcuityDataController(CurriculumData currData, ContextData context, StsTestResultFactData factData, WsReportingDataTV wsTvData, StudentScoreSummaryData studentScoreSummaryData) {
		getContentAreaFactBeans(currData, factData, wsTvData);
		getPrimObjFactBeans(currData, studentScoreSummaryData, wsTvData);
    }
	
	// Set the content area related data
	public void getContentAreaFactBeans(CurriculumData currData, StsTestResultFactData factData, WsReportingDataTV wsTvData) {
        ContentArea [] contentAreas = currData.getContentAreas();
        ArrayList<WsTVContentAreaFactData> contentAreaFact = new ArrayList<WsTVContentAreaFactData>();
            for(int i=0;i<contentAreas.length;i++) {
               StsTestResultFactDetails fact = factData.get(contentAreas[i].getContentAreaName());
               if(fact != null &&
                    ("T".equals(fact.getValidScore()) || "Y".equals(fact.getValidScore()))) {
            	   WsTVContentAreaFactData newFact = new WsTVContentAreaFactData();
                   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
                   newFact.setContentAreaName(contentAreas[i].getContentAreaName());
                   if(fact.getGradeEquivalent() != null) {
                        newFact.setGradeEquivalent( new Float(Float.parseFloat(fact.getGradeEquivalent().replaceAll("13","12.9").replace('+', '9'))));
                   }
                   newFact.setNationalPercentile((fact.getNationalPercentile()==null)?null:new Long(fact.getNationalPercentile().longValue()));
                   newFact.setNationalStanine((fact.getNationalStanine()==null)?null:new Long(fact.getNationalStanine().longValue()));
                   newFact.setNormalCurveEquivalent((fact.getNormalCurveEquivalent()==null)?null:new Long(fact.getNormalCurveEquivalent().longValue()));
                   newFact.setPercentageMastery((fact.getPercentObjectiveMastery()==null)?null:fact.getPercentObjectiveMastery());
                   newFact.setPercentObtained(fact.getPercentObtained());
                   newFact.setScaleScore((fact.getScaleScore()==null)?null:new Long(fact.getScaleScore().longValue()));
                   newFact.setSemScore((fact.getStandardErrorOfMeasurement() == null) ? null : new Long(fact.getStandardErrorOfMeasurement().longValue()));
                   contentAreaFact.add(newFact);
               }
            }
            wsTvData.setIrsTVContentAreaFactData((WsTVContentAreaFactData []) contentAreaFact.toArray(new WsTVContentAreaFactData[0]));
    }
	
	// Set the Objectives(primary only) related data
	public void getPrimObjFactBeans(CurriculumData currData, StudentScoreSummaryData studentScoreSummaryData, WsReportingDataTV wsTvData) {
        PrimaryObjective [] prims = currData.getPrimaryObjectives();
        WsTVPrimObjFactData [] primObjFacts = new WsTVPrimObjFactData[prims.length];
            for(int i=0;i<prims.length;i++) {
                PrimaryObjective prim = currData.getPrimObjById(prims[i].getPrimaryObjectiveId());
                primObjFacts[i] = new WsTVPrimObjFactData();
                primObjFacts[i].setPrimObjid(new Long(Long.parseLong(String.valueOf(prims[i].getProductId()) + String.valueOf(prims[i].getPrimaryObjectiveId()))));
                primObjFacts[i].setPrimObjName(prim.getPrimaryObjectiveName());
                StudentScoreSummaryDetails details = studentScoreSummaryData.get(prims[i].getPrimaryObjectiveId());
                if(details != null) {
                    if(details != null && details.getPercentObtained() != null) {
                        MasteryLevel mastery = MasteryLevel.masteryLevelForPercentage(details.getPercentObtained().intValue());
                        details.setMasteryLevel(mastery.getCode());
                    }
                    if(details.getMasteryLevel().equals("NM") || details.getMasteryLevel().equals("NON-MASTERY") || details.getMasteryLevel().equals("Not Mastered")) {
                    	primObjFacts[i].setMasteryLevel("Non-Mastery");
                    } else if (details.getMasteryLevel().equals("PM") || details.getMasteryLevel().equals("PARTIAL MASTERY") || details.getMasteryLevel().equals("Partially Mastered")) {
                    	primObjFacts[i].setMasteryLevel("Partial Mastery");
                    } else if (details.getMasteryLevel().equals("M") || details.getMasteryLevel().equals("MASTERY") || details.getMasteryLevel().equals("Mastered")) {
                    	primObjFacts[i].setMasteryLevel("Mastery");
                    }
                    primObjFacts[i].setMasteryLevel(details.getMasteryLevel());
                }
                primObjFacts[i].setStudentScore(details.getPercentObtained());
                primObjFacts[i].setNationalAverage(prim.getNationalAverage() == null?new BigDecimal(0):prim.getNationalAverage());
            }
        wsTvData.setIrsTVPrimObjFactData(primObjFacts);
    }

}
