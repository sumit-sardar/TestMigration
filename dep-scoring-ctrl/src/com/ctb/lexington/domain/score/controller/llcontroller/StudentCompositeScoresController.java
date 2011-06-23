package com.ctb.lexington.domain.score.controller.llcontroller;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.data.CurriculumData.Composite;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLCompositeFactData;
import com.ctb.lexington.db.mapper.llmapper.IrsLLCompositeFactMapper;

public class StudentCompositeScoresController {
	  private StsTotalStudentScoreData totalData;
	    private CurriculumData currData;
	    private ContextData context;
	    private IrsLLCompositeFactMapper mapper;

	    public StudentCompositeScoresController(Connection conn, StsTotalStudentScoreData totalData, CurriculumData currData, ContextData context) {
	        this.totalData = totalData;
	        this.currData = currData;
	        this.context = context;
	        mapper = new IrsLLCompositeFactMapper(conn);
	    }

	    public void run() throws SQLException {
	    	IrsLLCompositeFactData [] facts = getCompositeFactBeans();
	        for(int i=0;i<facts.length;i++) {
	        	IrsLLCompositeFactData newFact = facts[i];
	            mapper.delete(newFact);
	            if(new Long(1).equals(newFact.getCurrentResultid()))  {
	                mapper.insert(newFact);
	            }
	        }
	    }
	    
	    public IrsLLCompositeFactData [] getCompositeFactBeans() {
	        if(totalData.size() > 0) {
	            Composite [] composites = currData.getComposites();
	            ArrayList facts = new ArrayList();
	            for(int i=0;i<composites.length;i++) {
	               StsTotalStudentScoreDetail total = totalData.get(composites[i].getCompositeName());
	               if(total != null  && ("T".equals(total.getValidScore()) || "Y".equals(total.getValidScore())))  {
	            	   IrsLLCompositeFactData newFact = new IrsLLCompositeFactData();
	                   newFact.setAssessmentid(context.getAssessmentId());
	                   newFact.setCompositeid(composites[i].getCompositeId());
	                   newFact.setCurrentResultid(context.getCurrentResultId());
	                   newFact.setGradeid(context.getGradeId());
	                   newFact.setNrsLevelid(new Long(0));
	                   newFact.setOrgNodeid(context.getOrgNodeId());
	                   newFact.setPointsAttempted(total.getPointsAttempted());
	                   newFact.setPointsObtained(total.getPointsObtained());
	                   newFact.setPointsPossible(total.getPointsPossible());
	                   newFact.setProgramid(context.getProgramId());
	                   newFact.setScaleScore((total.getScaleScore()==null)?null:new Long(total.getScaleScore().longValue()));
	                   newFact.setProficencyLevel((total.getProficencyLevel()==null)?null:new Long(total.getProficencyLevel().longValue()));
	                   newFact.setNormalCurveEquivalent((total.getNormalCurveEquivalent()==null)?null:new Long(total.getNormalCurveEquivalent().longValue()));
	                   newFact.setNationalPercentile((total.getNationalPercentile()==null)?null:new Long(total.getNationalPercentile().longValue()));
	                   newFact.setSessionid(context.getSessionId());
	                   newFact.setStudentid(context.getStudentId());
	                   newFact.setTestStartTimestamp(context.getTestStartTimestamp());
	                   newFact.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
	                  /* newFact.setAttr2id(context.getDemographicData().getAttr2Id());
	                   newFact.setAttr9id(context.getDemographicData().getAttr9Id());
	                   newFact.setAttr11id(context.getDemographicData().getAttr11Id());
	                   newFact.setAttr12id(context.getDemographicData().getAttr12Id());
	                   newFact.setAttr13id(context.getDemographicData().getAttr13Id());
	                   newFact.setAttr14id(context.getDemographicData().getAttr14Id());
	                   newFact.setAttr15id(context.getDemographicData().getAttr15Id());
	                   newFact.setAttr16id(context.getDemographicData().getAttr16Id());
	                   newFact.setAttr17id(context.getDemographicData().getAttr17Id());
	                   newFact.setAttr18id(context.getDemographicData().getAttr18Id());
	                   newFact.setAttr19id(context.getDemographicData().getAttr19Id());
	                   newFact.setAttr20id(context.getDemographicData().getAttr20Id());
	                   newFact.setAttr21id(context.getDemographicData().getAttr21Id());
	                   newFact.setAttr22id(context.getDemographicData().getAttr22Id());
	                   newFact.setAttr23id(context.getDemographicData().getAttr23Id());
	                   newFact.setAttr25id(context.getDemographicData().getAttr25Id());
	                   newFact.setAttr26id(context.getDemographicData().getAttr26Id());
	                   newFact.setAttr27id(context.getDemographicData().getAttr27Id());
	                   newFact.setAttr28id(context.getDemographicData().getAttr28Id());
	                   newFact.setAttr29id(context.getDemographicData().getAttr29Id());
	                   newFact.setAttr30id(context.getDemographicData().getAttr30Id());
	                   newFact.setAttr31id(context.getDemographicData().getAttr31Id());
	                   newFact.setAttr32id(context.getDemographicData().getAttr32Id());
	                   newFact.setAttr33id(context.getDemographicData().getAttr33Id());
	                   newFact.setAttr34id(context.getDemographicData().getAttr34Id());
	                   newFact.setAttr35id(context.getDemographicData().getAttr35Id());
	                   newFact.setAttr36id(context.getDemographicData().getAttr36Id());
	                   newFact.setAttr37id(context.getDemographicData().getAttr37Id());*/
	                   if (currData.getContentAreas().length > 0) {
	                        newFact.setFormid(new Long("A".equals(currData.getContentAreas()[0].getSubtestForm())?7:
	                                          "B".equals(currData.getContentAreas()[0].getSubtestForm())?8:
	                                         ("Espa?ol".equals(currData.getContentAreas()[0].getSubtestForm()) 
	                                        		 || "Espanol".equals(currData.getContentAreas()[0].getSubtestForm()) 
	                                        		 || "Español".equals(currData.getContentAreas()[0].getSubtestForm()))?9:10));
	                        
	                        
	                        newFact.setLevelid(new Long("K".equals(currData.getContentAreas()[0].getSubtestLevel())?16:
	                                          "1".equals(currData.getContentAreas()[0].getSubtestLevel())?17:
	                                          "2-3".equals(currData.getContentAreas()[0].getSubtestLevel())?18:
	                                          "4-5".equals(currData.getContentAreas()[0].getSubtestLevel())?19:
	                                          "6-8".equals(currData.getContentAreas()[0].getSubtestLevel())?20:
	                                          "9-12".equals(currData.getContentAreas()[0].getSubtestLevel())?21:22));
	                   }
	                   
	                   facts.add(newFact);
	               }
	               else{
	            	   IrsLLCompositeFactData newFact = new IrsLLCompositeFactData();
	            	   newFact.setCompositeid(composites[i].getCompositeId());
	            	   newFact.setSessionid(context.getSessionId());
	                   newFact.setStudentid(context.getStudentId());
	                   newFact.setCurrentResultid(new Long (2));
	                   facts.add(newFact);
	               }
	            }
	            return (IrsLLCompositeFactData []) facts.toArray(new IrsLLCompositeFactData[0]);
	        } else {
	            return new IrsLLCompositeFactData[0];
	        }
	    }
}
