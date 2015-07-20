package com.ctb.lexington.domain.score.controller.tscontroller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.CurriculumData.Composite;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCCompositeFactData;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCCompositeFactMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author ncohen
 * @version $Id$
 */
public class StudentCompositeScoresController {
    private StsTotalStudentScoreData totalData;
    private CurriculumData currData;
    private ContextData context;
    private IrsTASCCompositeFactMapper mapper;

    public StudentCompositeScoresController(Connection conn, StsTotalStudentScoreData totalData, CurriculumData currData, ContextData context) {
        this.totalData = totalData;
        this.currData = currData;
        this.context = context;
        mapper = new IrsTASCCompositeFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTASCCompositeFactData [] facts = getCompositeFactBeans();
        
        SqlMapClient insertSqlMap = null;
        SqlMapClient deleteSqlMap = null;
        ArrayList<Long> contentList = new ArrayList<Long>();
        for(int i=0;i<facts.length;i++) {
        	IrsTASCCompositeFactData newFact = facts[i];
            deleteSqlMap = mapper.deleteBatch(newFact,deleteSqlMap);
            if(new Long(1).equals(newFact.getCurrentResultid()) && !contentList.contains(newFact.getCompositeid()))  {
                contentList.add(newFact.getCompositeid());
            	insertSqlMap = mapper.insertBatch(newFact, insertSqlMap);
            }
        }
        
        mapper.executeItemBatch(deleteSqlMap);
        mapper.executeItemBatch(insertSqlMap);
        contentList.clear();
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
                   newFact.setGradeid(context.getGradeId());
                   newFact.setNationalPercentile((total.getNationalPercentile()==null)?null:new Long(total.getNationalPercentile().longValue()));
                   newFact.setNormalCurveEquivalent((total.getNormalCurveEquivalent()==null)?null:new Long(total.getNormalCurveEquivalent().longValue()));
                   newFact.setOrgNodeid(context.getOrgNodeId());
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
