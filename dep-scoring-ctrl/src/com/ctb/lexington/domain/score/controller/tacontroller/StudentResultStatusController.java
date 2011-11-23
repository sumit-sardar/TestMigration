package com.ctb.lexington.domain.score.controller.tacontroller;

import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABECompositeFactData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEItemFactData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEPredSubjectFactData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEPrimObjFactData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABESecObjFactData;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABECompositeFactMapper;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEContentAreaFactMapper;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEPrimObjFactMapper;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABESecObjFactMapper;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEItemFactMapper;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEPredSubjectFactMapper;


public class StudentResultStatusController{ 
    
    private ContextData context;    
    private IrsTABECompositeFactMapper irsTABECompositeFactMapper;
    private IrsTABEContentAreaFactMapper irsTABEContentAreaFactMapper;
    private IrsTABEPrimObjFactMapper  irsTABEPrimObjFactMapper;
  //  private IrsTABESecObjFactMapper irsTABESecObjFactMapper;
    private IrsTABEItemFactMapper irsTABEItemFactMapper;
    private IrsTABEPredSubjectFactMapper irsTABEPredSubjectFactMapper;
    
    public StudentResultStatusController(Connection conn, ContextData context){
        this.context = context;
        irsTABEPredSubjectFactMapper = new IrsTABEPredSubjectFactMapper(conn);
        irsTABECompositeFactMapper = new IrsTABECompositeFactMapper(conn);
        irsTABEContentAreaFactMapper = new IrsTABEContentAreaFactMapper(conn);
        irsTABEPrimObjFactMapper = new IrsTABEPrimObjFactMapper(conn);
    //    irsTABESecObjFactMapper = new IrsTABESecObjFactMapper(conn);
        irsTABEItemFactMapper = new IrsTABEItemFactMapper(conn);
    }

    public void run() throws IOException, DataException, CTBSystemException, SQLException {
        Long priorResult = new Long(1).equals(context.getCurrentResultId())?new Long(2):new Long(1);
                
        IrsTABEPredSubjectFactData tabePredSubjectFactData = new IrsTABEPredSubjectFactData();
        tabePredSubjectFactData.setStudentid(context.getStudentId());
        tabePredSubjectFactData.setSessionid(context.getSessionId());
        tabePredSubjectFactData.setCurrentResultid(priorResult);
        tabePredSubjectFactData.setAssessmentid(context.getAssessmentId());
        tabePredSubjectFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabePredSubjectFactData.setProgramid(context.getProgramId());
        irsTABEPredSubjectFactMapper.updateCurrentResultStatus(tabePredSubjectFactData);
        
        IrsTABECompositeFactData tabeCompositeFactdata = new IrsTABECompositeFactData();
        tabeCompositeFactdata.setStudentid(context.getStudentId());
        tabeCompositeFactdata.setSessionid(context.getSessionId());
        tabeCompositeFactdata.setCurrentResultid(priorResult);
        tabeCompositeFactdata.setAssessmentid(context.getAssessmentId());
        tabeCompositeFactdata.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeCompositeFactdata.setProgramid(context.getProgramId());
        irsTABECompositeFactMapper.updateCurrentResultStatus(tabeCompositeFactdata);
        
        IrsTABEContentAreaFactData tabeContentAreaFactData = new IrsTABEContentAreaFactData();
        tabeContentAreaFactData.setStudentid(context.getStudentId());
        tabeContentAreaFactData.setSessionid(context.getSessionId());
        tabeContentAreaFactData.setCurrentResultid(priorResult);
        tabeContentAreaFactData.setAssessmentid(context.getAssessmentId());
        tabeContentAreaFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeContentAreaFactData.setProgramid(context.getProgramId());
        irsTABEContentAreaFactMapper.updateCurrentResultStatus(tabeContentAreaFactData);
        
        IrsTABEPrimObjFactData tabePrimObjFactData = new IrsTABEPrimObjFactData();
        tabePrimObjFactData.setStudentid(context.getStudentId());
        tabePrimObjFactData.setSessionid(context.getSessionId());
        tabePrimObjFactData.setCurrentResultid(priorResult);
        tabePrimObjFactData.setAssessmentid(context.getAssessmentId());
        tabePrimObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabePrimObjFactData.setProgramid(context.getProgramId());
        irsTABEPrimObjFactMapper.updateCurrentResultStatus(tabePrimObjFactData);
        
      /*  IrsTABESecObjFactData tabeSecObjFactData = new IrsTABESecObjFactData();
        tabeSecObjFactData.setStudentid(context.getStudentId());
        tabeSecObjFactData.setSessionid(context.getSessionId());
        tabeSecObjFactData.setCurrentResultid(priorResult);
        tabeSecObjFactData.setAssessmentid(context.getAssessmentId());
        tabeSecObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeSecObjFactData.setProgramid(context.getProgramId());
        irsTABESecObjFactMapper.updateCurrentResultStatus(tabeSecObjFactData);*/
        
        IrsTABEItemFactData tabeItemFactData = new IrsTABEItemFactData();
        tabeItemFactData.setStudentid(context.getStudentId());
        tabeItemFactData.setSessionid(context.getSessionId());
        tabeItemFactData.setCurrentResultid(priorResult);
        tabeItemFactData.setAssessmentid(context.getAssessmentId());
        tabeItemFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeItemFactData.setProgramid(context.getProgramId());
        irsTABEItemFactMapper.updateCurrentResultStatus(tabeItemFactData);
    }
} 