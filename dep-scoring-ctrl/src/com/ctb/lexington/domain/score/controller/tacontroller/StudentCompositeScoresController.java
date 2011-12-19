package com.ctb.lexington.domain.score.controller.tacontroller;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.CurriculumData.Composite;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import java.sql.Connection;

import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABECompositeFactData;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABECompositeFactMapper;
import com.ctb.lexington.db.mapper.StudentPredictedScoresMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
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
    private IrsTABECompositeFactMapper mapper;

    public StudentCompositeScoresController(Connection conn, StsTotalStudentScoreData totalData, StudentPredictedScoresData predData, CurriculumData currData, ContextData context) {
        this.totalData = totalData;
        this.predData = predData;
        this.currData = currData;
        this.context = context;
        mapper = new IrsTABECompositeFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTABECompositeFactData [] facts = getCompositeFactBeans();
        for(int i=0;i<facts.length;i++) {
            IrsTABECompositeFactData newFact = facts[i];
            mapper.delete(newFact);
            if(new Long(1).equals(newFact.getCurrentResultid()))  {
                mapper.insert(newFact);
            }
        }
    }
    
    public IrsTABECompositeFactData [] getCompositeFactBeans() {
        if(totalData.size() > 0) {
            Composite [] composites = currData.getComposites();
            ArrayList facts = new ArrayList();
            for(int i=0;i<composites.length;i++) {
               StsTotalStudentScoreDetail total = totalData.get(composites[i].getCompositeName());
               if(total != null && total.getScaleScore() != null)  {
                   IrsTABECompositeFactData newFact = new IrsTABECompositeFactData();
                   newFact.setAssessmentid(context.getAssessmentId());
                   newFact.setCompositeid(composites[i].getCompositeId());
                   newFact.setCurrentResultid(context.getCurrentResultId());
                   if(total.getGradeEquivalent() != null) {
                    newFact.setGradeEquivalent( total.getGradeEquivalent().replace('+', '9') ) ;
                   }
                   newFact.setGradeid(context.getGradeId());
                   newFact.setNationalPercentile((total.getNationalPercentile()==null)?null:new Long(total.getNationalPercentile().longValue()));
                   newFact.setNationalStanine((total.getNationalStanine()==null)?null:new Long(total.getNationalStanine().longValue()));
                   newFact.setNormalCurveEquivalent((total.getNormalCurveEquivalent()==null)?null:new Long(total.getNormalCurveEquivalent().longValue()));
                   if(total.getScaleScore() != null && "Total Mathematics".equals(composites[i].getCompositeName())) {
                        if (total.getScaleScore().longValue() <= 313)
                            newFact.setNrsLevelid(new Long(1));
                        else if (total.getScaleScore().longValue() <= 441)
                            newFact.setNrsLevelid(new Long(2));
                        else if (total.getScaleScore().longValue() <= 505)
                            newFact.setNrsLevelid(new Long(3));
                        else if (total.getScaleScore().longValue() <= 565)
                            newFact.setNrsLevelid(new Long(4));
                        else if (total.getScaleScore().longValue() <= 594)
                            newFact.setNrsLevelid(new Long(5));
                        else newFact.setNrsLevelid(new Long(6));
                   } else {
                        newFact.setNrsLevelid(new Long(0));
                   }
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
                   newFact.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
                   newFact.setTestStartTimestamp(context.getTestStartTimestamp());
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
                   facts.add(newFact);
               } else {
            	   IrsTABECompositeFactData newFact = new IrsTABECompositeFactData();
            	   newFact.setCompositeid(composites[i].getCompositeId());
            	   newFact.setSessionid(context.getSessionId());
            	   newFact.setStudentid(context.getStudentId());
            	   newFact.setCurrentResultid(new Long (2));
            	   facts.add(newFact);
               }
            }
            return (IrsTABECompositeFactData []) facts.toArray(new IrsTABECompositeFactData[0]);
        } else {
            return new IrsTABECompositeFactData[0];
        }
    }
}
