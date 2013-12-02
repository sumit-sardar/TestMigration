package com.ctb.lexington.domain.score.controller.tscontroller;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.CurriculumData.Composite;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import java.sql.Connection;

import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCCompositeFactData;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCCompositeFactMapper;
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
    //private StudentPredictedScoresData predData;
    private CurriculumData currData;
    private ContextData context;
    private IrsTASCCompositeFactMapper mapper;

    public StudentCompositeScoresController(Connection conn, StsTotalStudentScoreData totalData, /*StudentPredictedScoresData predData,*/ CurriculumData currData, ContextData context) {
        this.totalData = totalData;
        //this.predData = predData;
        this.currData = currData;
        this.context = context;
        mapper = new IrsTASCCompositeFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTASCCompositeFactData [] facts = getCompositeFactBeans();
        for(int i=0;i<facts.length;i++) {
            IrsTASCCompositeFactData newFact = facts[i];
            mapper.delete(newFact);
            if(new Long(1).equals(newFact.getCurrentResultid()))  {
                mapper.insert(newFact);
            }
        }
    }
    
    public IrsTASCCompositeFactData [] getCompositeFactBeans() {
        if(totalData.size() > 0) {
            Composite [] composites = currData.getComposites();
            ArrayList facts = new ArrayList();
            for(int i=0;i<composites.length;i++) {
               StsTotalStudentScoreDetail total = totalData.get(composites[i].getCompositeName());
               if(total != null && ("T".equals(total.getValidScore()) || "Y".equals(total.getValidScore())))  {
                   IrsTASCCompositeFactData newFact = new IrsTASCCompositeFactData();
                   newFact.setAssessmentid(context.getAssessmentId());
                   newFact.setCompositeid(composites[i].getCompositeId());
                   newFact.setCurrentResultid(context.getCurrentResultId());
                   /*if(total.getGradeEquivalent() != null) {
                    newFact.setGradeEquivalent( total.getGradeEquivalent().replace('+', '9') ) ;
                   }*/
                   newFact.setGradeid(context.getGradeId());
                   newFact.setNationalPercentile((total.getNationalPercentile()==null)?null:new Long(total.getNationalPercentile().longValue()));
                   //newFact.setNationalStanine((total.getNationalStanine()==null)?null:new Long(total.getNationalStanine().longValue()));
                   newFact.setNormalCurveEquivalent((total.getNormalCurveEquivalent()==null)?null:new Long(total.getNormalCurveEquivalent().longValue()));
                   newFact.setOrgNodeid(context.getOrgNodeId());
                   //newFact.setPercentageMastery((total.getPercentObjectiveMastery()==null)?null:total.getPercentObjectiveMastery());
                   newFact.setProficiencyLevel(Long.valueOf(total.getProficencyLevel().toBigInteger().longValue()));
                   newFact.setPointsAttempted(total.getPointsAttempted());
                   newFact.setPointsObtained(total.getPointsObtained());
                   newFact.setPointsPossible(total.getPointsPossible());
                   newFact.setProgramid(context.getProgramId());
                   newFact.setRecLevelid((total.getRecommendedLevelId() == null)?new Long(6):total.getRecommendedLevelId());
                   newFact.setScaleScore((total.getScaleScore()==null)?null:new Long(total.getScaleScore().longValue()));
                   newFact.setSessionid(context.getSessionId());
                   newFact.setStudentid(context.getStudentId());
                   newFact.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
                   newFact.setTestStartTimestamp(context.getTestStartTimestamp());
                   newFact.setScaleScoreRangeForProficiency(total.getScaleScoreRangeForProficiency());
                   
                   /*newFact.setAttr1id(context.getDemographicData().getAttr1Id());
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
                   newFact.setAttr17id(context.getDemographicData().getAttr17Id());
                   newFact.setAttr18id(context.getDemographicData().getAttr18Id());
                   newFact.setAttr19id(context.getDemographicData().getAttr19Id());
                   newFact.setAttr20id(Long.parseLong(context.getDemographicData().getAttr20Id()));
                   newFact.setAttr21id(Long.parseLong(context.getDemographicData().getAttr21Id()));
                   newFact.setAttr22id(context.getDemographicData().getAttr22Id());
                   newFact.setAttr24id(context.getDemographicData().getAttr24Id());
                   newFact.setAttr25id(context.getDemographicData().getAttr25Id());
                   newFact.setAttr26id(context.getDemographicData().getAttr26Id());
                   newFact.setAttr27id(context.getDemographicData().getAttr27Id());
                   newFact.setAttr28id(context.getDemographicData().getAttr28Id());
                   newFact.setAttr29id(context.getDemographicData().getAttr29Id());
                   newFact.setAttr30id(context.getDemographicData().getAttr30Id());
                   newFact.setAttr36id(context.getDemographicData().getAttr36Id());
                   newFact.setAttr37id(context.getDemographicData().getAttr37Id());*/
                   
                   facts.add(newFact);
               } else {
            	   IrsTASCCompositeFactData newFact = new IrsTASCCompositeFactData();
            	   newFact.setCompositeid(composites[i].getCompositeId());
            	   newFact.setSessionid(context.getSessionId());
                   newFact.setStudentid(context.getStudentId());
                   newFact.setCurrentResultid(new Long (2));
                   facts.add(newFact);
               }
            }
            return (IrsTASCCompositeFactData []) facts.toArray(new IrsTASCCompositeFactData[0]);
        } else {
            return new IrsTASCCompositeFactData[0];
        }
    }
}
