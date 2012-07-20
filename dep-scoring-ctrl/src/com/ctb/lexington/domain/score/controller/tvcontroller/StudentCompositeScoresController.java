package com.ctb.lexington.domain.score.controller.tvcontroller;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.CurriculumData.Composite;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import java.sql.Connection;

import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVCompositeFactData;
import com.ctb.lexington.db.mapper.tvmapper.IrsTVCompositeFactMapper;
import com.ctb.lexington.db.mapper.StudentPredictedScoresMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author ncohen
 * @version $Id$
 */
public class StudentCompositeScoresController {
    private StsTotalStudentScoreData totalData;
    private StudentPredictedScoresData predData;
    private CurriculumData currData;
    private ContextData context;
    private IrsTVCompositeFactMapper mapper;

    public StudentCompositeScoresController(Connection conn, StsTotalStudentScoreData totalData, StudentPredictedScoresData predData, CurriculumData currData, ContextData context) {
        this.totalData = totalData;
        this.predData = predData;
        this.currData = currData;
        this.context = context;
        mapper = new IrsTVCompositeFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTVCompositeFactData [] facts = getCompositeFactBeans();
        for(int i=0;i<facts.length;i++) {
            IrsTVCompositeFactData newFact = facts[i];
            mapper.delete(newFact);
            if(new Long(1).equals(context.getCurrentResultId()))  {
                mapper.insert(newFact);
            }
        }
    }
    
    public IrsTVCompositeFactData [] getCompositeFactBeans() {
        if(totalData.size() > 0) {
            Composite [] composites = currData.getComposites();
            ArrayList facts = new ArrayList();
            for(int i=0;i<composites.length;i++) {
               StsTotalStudentScoreDetail total = totalData.get(composites[i].getCompositeName());
               if(total != null && ("T".equals(total.getValidScore()) || "Y".equals(total.getValidScore())))  {
                   IrsTVCompositeFactData newFact = new IrsTVCompositeFactData();
                   newFact.setAssessmentid(context.getAssessmentId());
                   newFact.setCompositeid(composites[i].getCompositeId());
                   newFact.setCurrentResultid(context.getCurrentResultId());
                   if(total.getGradeEquivalent() != null) {
                        Float ge = new Float(Float.parseFloat(total.getGradeEquivalent().replaceAll("13","12.9").replace('+', '9')));
                        
                        DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
                        float gedec = new Float(df2.format(ge)).floatValue();
                        
                        newFact.setGradeEquivalent(new Float(gedec));
                   }
                   newFact.setGradeid(context.getGradeId());
                   newFact.setNationalPercentile((total.getNationalPercentile()==null)?null:new Long(total.getNationalPercentile().longValue()));
                   newFact.setNationalStanine((total.getNationalStanine()==null)?null:new Long(total.getNationalStanine().longValue()));
                   newFact.setNormalCurveEquivalent((total.getNormalCurveEquivalent()==null)?null:new Long(total.getNormalCurveEquivalent().longValue()));
                   newFact.setNrsLevelid(new Long(0));
                   newFact.setOrgNodeid(context.getOrgNodeId());
                   newFact.setPercentageMastery((total.getPercentObjectiveMastery()==null)?null:total.getPercentObjectiveMastery());
                   newFact.setPointsAttempted(total.getPointsAttempted());
                   newFact.setPointsObtained(total.getPointsObtained());
                   newFact.setPointsPossible(composites[i].getCompositePointsPossible());
                   newFact.setProgramid(context.getProgramId());
                   newFact.setRecLevelid((total.getRecommendedLevelId() == null)?new Long(6):total.getRecommendedLevelId());
                   newFact.setScaleScore((total.getScaleScore()==null)?null:new Long(total.getScaleScore().longValue()));
                   newFact.setSessionid(context.getSessionId());
                   newFact.setStudentid(context.getStudentId());
                   newFact.setTestStartTimestamp(context.getTestStartTimestamp());
                   newFact.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
                   newFact.setAttr1id(context.getDemographicData().getAttr1Id());
                   newFact.setAttr2id(context.getDemographicData().getAttr2Id());
                   newFact.setAttr3id(context.getDemographicData().getAttr3Id());
                   newFact.setAttr4id(context.getDemographicData().getAttr4Id());
                   newFact.setAttr5id(context.getDemographicData().getAttr5Id());
                   newFact.setAttr6id(context.getDemographicData().getAttr6Id());
                   newFact.setAttr7id(context.getDemographicData().getAttr7Id());
                   newFact.setAttr8id(context.getDemographicData().getAttr8Id());
                   newFact.setAttr9id(context.getDemographicData().getAttr9Id());
                   newFact.setAttr10id(context.getDemographicData().getAttr10Id());
                   newFact.setAttr11id(context.getDemographicData().getAttr11Id());
                   newFact.setAttr12id(context.getDemographicData().getAttr12Id());
                   newFact.setAttr13id(context.getDemographicData().getAttr13Id());
                   newFact.setAttr14id(context.getDemographicData().getAttr14Id());
                   newFact.setAttr15id(context.getDemographicData().getAttr15Id());
                   newFact.setAttr16id(context.getDemographicData().getAttr16Id());
                   if (currData.getContentAreas().length > 0) {
                        newFact.setFormid(new Long("B".equals(currData.getContentAreas()[0].getSubtestForm())?4:
                                          "G".equals(currData.getContentAreas()[0].getSubtestForm())?5:
                                          "1".equals(currData.getContentAreas()[0].getSubtestForm())?4:6));
                        newFact.setLevelid(new Long("13".equals(currData.getContentAreas()[0].getSubtestLevel())?7:
                                          "14".equals(currData.getContentAreas()[0].getSubtestLevel())?8:
                                          "15".equals(currData.getContentAreas()[0].getSubtestLevel())?9:
                                          "16".equals(currData.getContentAreas()[0].getSubtestLevel())?10:
                                          "17".equals(currData.getContentAreas()[0].getSubtestLevel())?11:
                                          "18".equals(currData.getContentAreas()[0].getSubtestLevel())?12:
                                          "19".equals(currData.getContentAreas()[0].getSubtestLevel())?13:
                                          "19-20".equals(currData.getContentAreas()[0].getSubtestLevel())?14:
                                          "19/20".equals(currData.getContentAreas()[0].getSubtestLevel())?14:
                                          "20".equals(currData.getContentAreas()[0].getSubtestLevel())?14:
                                          "12".equals(currData.getContentAreas()[0].getSubtestLevel())?30:
                                          "21".equals(currData.getContentAreas()[0].getSubtestLevel())?31:
                                          "22".equals(currData.getContentAreas()[0].getSubtestLevel())?32:
                                          "21-22".equals(currData.getContentAreas()[0].getSubtestLevel())?32:
                                          "21/22".equals(currData.getContentAreas()[0].getSubtestLevel())?32:15));
                   }
                   newFact.setCompositeIndex(new Long(1));
                   facts.add(newFact);
               }
            }
            return (IrsTVCompositeFactData []) facts.toArray(new IrsTVCompositeFactData[0]);
        } else {
            return new IrsTVCompositeFactData[0];
        }
    }
}