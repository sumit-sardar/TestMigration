package com.ctb.lexington.domain.score.controller.tacontroller;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.CurriculumData.Composite;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import java.sql.Connection;

import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEPredSubjectFactData;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABECompositeFactMapper;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEPredSubjectFactMapper;
import com.ctb.lexington.db.mapper.StudentPredictedScoresMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ncohen
 * @version $Id$
 */
public class StudentPredictedScoresController {
    private StudentPredictedScoresData predData;
    private CurriculumData currData;
    private ContextData context;
    private IrsTABEPredSubjectFactMapper mapper;
    private Long formid;

    public StudentPredictedScoresController(Connection conn, StudentPredictedScoresData predData, CurriculumData currData, ContextData context) {
        this.predData = predData;
        this.currData = currData;
        this.context = context;
        if(currData != null && currData.getContentAreas() != null && currData.getContentAreas().length > 0) {
            this.formid = new Long("CAT".equals(currData.getContentAreas()[0].getSubtestForm())?13:14);
        } else {
            this.formid = new Long(14);
        }
        mapper = new IrsTABEPredSubjectFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTABEPredSubjectFactData [] facts = getPredFactBeans();
        for(int i=0;i<facts.length;i++) {
            IrsTABEPredSubjectFactData newFact = facts[i];
            mapper.delete(newFact);
            if(new Long(1).equals(context.getCurrentResultId()))  {
                mapper.insert(newFact);
            }
        }
    }
    
    public IrsTABEPredSubjectFactData [] getPredFactBeans() {
        if(predData != null) {
            ArrayList facts = new ArrayList();
                if(predData.getExpectedGedAverage() != null) {
                    IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                    newFact.setPredSubjectid(new Long(1));
                    newFact.setPredictedGed(new Long(predData.getExpectedGedAverage().toBigInteger().intValue()));
                    if(newFact.getPredictedGed().longValue() <= 380)
                        newFact.setRecActivityid(new Long(1));
                    else if(newFact.getPredictedGed().longValue() <= 440)
                        newFact.setRecActivityid(new Long(2));
                    else newFact.setRecActivityid(new Long(3));
                    facts.add(newFact);
                } else { //Changed for deleting the data from fact table if the student is invalidated for adaptive
                	IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                	newFact.setPredSubjectid(new Long(1));
                	newFact.setCurrentResultid(new Long (2));
                	facts.add(newFact);
                }
                if(predData.getExpectedGedMath() != null) {
                    IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                    newFact.setPredSubjectid(new Long(2));
                    newFact.setPredictedGed(new Long(predData.getExpectedGedMath().toBigInteger().intValue()));
                    if(newFact.getPredictedGed().longValue() <= 330)
                        newFact.setRecActivityid(new Long(1));
                    else if(newFact.getPredictedGed().longValue() <= 410)
                        newFact.setRecActivityid(new Long(2));
                    else newFact.setRecActivityid(new Long(3));
                    facts.add(newFact);
                } else {
                	IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                	newFact.setPredSubjectid(new Long(2));
                	newFact.setCurrentResultid(new Long (2));
                	facts.add(newFact);
                }
                if(predData.getExpectedGedReading() != null) {
                    IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                    newFact.setPredSubjectid(new Long(3));
                    newFact.setPredictedGed(new Long(predData.getExpectedGedReading().toBigInteger().intValue()));
                    if(newFact.getPredictedGed().longValue() <= 320)
                        newFact.setRecActivityid(new Long(1));
                    else if(newFact.getPredictedGed().longValue() <= 410)
                        newFact.setRecActivityid(new Long(2));
                    else newFact.setRecActivityid(new Long(3));
                    facts.add(newFact);
                } else {
                	IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                	newFact.setPredSubjectid(new Long(3));
                	newFact.setCurrentResultid(new Long (2));
                	facts.add(newFact);
                }
                if(predData.getExpectedGedScience() != null) {
                    IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                    newFact.setPredSubjectid(new Long(4));
                    newFact.setPredictedGed(new Long(predData.getExpectedGedScience().toBigInteger().intValue()));
                    if(newFact.getPredictedGed().longValue() <= 320)
                        newFact.setRecActivityid(new Long(1));
                    else if(newFact.getPredictedGed().longValue() <= 400)
                        newFact.setRecActivityid(new Long(2));
                    else newFact.setRecActivityid(new Long(3));
                    facts.add(newFact);
                } else {
                	IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                	newFact.setPredSubjectid(new Long(4));
                	newFact.setCurrentResultid(new Long (2));
                	facts.add(newFact);
                }
                if(predData.getExpectedGedSocialStudies() != null) {
                    IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                    newFact.setPredSubjectid(new Long(5));
                    newFact.setPredictedGed(new Long(predData.getExpectedGedSocialStudies().toBigInteger().intValue()));
                    if(newFact.getPredictedGed().longValue() <= 320)
                        newFact.setRecActivityid(new Long(1));
                    else if(newFact.getPredictedGed().longValue() <= 400)
                        newFact.setRecActivityid(new Long(2));
                    else newFact.setRecActivityid(new Long(3));
                    facts.add(newFact);
                } else {
                	IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                	newFact.setPredSubjectid(new Long(5));
                	newFact.setCurrentResultid(new Long (2));
                	facts.add(newFact);
                }
                if(predData.getExpectedGedWriting() != null) {
                    IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                    newFact.setPredSubjectid(new Long(6));
                    newFact.setPredictedGed(new Long(predData.getExpectedGedWriting().toBigInteger().intValue()));
                    if(newFact.getPredictedGed().longValue() <= 350)
                        newFact.setRecActivityid(new Long(1));
                    else if(newFact.getPredictedGed().longValue() <= 400)
                        newFact.setRecActivityid(new Long(2));
                    else newFact.setRecActivityid(new Long(3));
                    facts.add(newFact);
                } else {
                	IrsTABEPredSubjectFactData newFact = new IrsTABEPredSubjectFactData();
                	newFact.setPredSubjectid(new Long(6));
                	newFact.setCurrentResultid(new Long (2));
                	facts.add(newFact);
                }
            Iterator factIter = facts.iterator();
            while(factIter.hasNext()) {
                   IrsTABEPredSubjectFactData newFact = (IrsTABEPredSubjectFactData) factIter.next();
                   newFact.setAssessmentid(context.getAssessmentId());
                   newFact.setFormid(this.formid);
                   newFact.setCurrentResultid(context.getCurrentResultId());
                   newFact.setGradeid(context.getGradeId());
                   newFact.setOrgNodeid(context.getOrgNodeId());
                   newFact.setProgramid(context.getProgramId());
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
            }
            return (IrsTABEPredSubjectFactData []) facts.toArray(new IrsTABEPredSubjectFactData[0]);
        } else {
            return new IrsTABEPredSubjectFactData[0];
        }
    }
}
