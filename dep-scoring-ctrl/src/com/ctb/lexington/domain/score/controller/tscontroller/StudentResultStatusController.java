package com.ctb.lexington.domain.score.controller.tscontroller;

import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCCompositeFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCItemFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCPredSubjectFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCPrimObjFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCSecObjFactData;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCCompositeFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCContentAreaFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCItemFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCPredSubjectFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCPrimObjFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCSecObjFactMapper;

/**
 * @author Rama_Rao
 *
 */
public class StudentResultStatusController{ 
    
    private ContextData context;    
    private IrsTASCCompositeFactMapper irsTASCCompositeFactMapper;
    private IrsTASCContentAreaFactMapper irsTASCContentAreaFactMapper;
    private IrsTASCPrimObjFactMapper  irsTASCPrimObjFactMapper;
    private IrsTASCSecObjFactMapper irsTASCSecObjFactMapper;
    private IrsTASCItemFactMapper irsTASCItemFactMapper;
    private IrsTASCPredSubjectFactMapper irsTASCPredSubjectFactMapper;
    
    public StudentResultStatusController(Connection conn, ContextData context){
        this.context = context;
        irsTASCPredSubjectFactMapper = new IrsTASCPredSubjectFactMapper(conn);
        irsTASCCompositeFactMapper = new IrsTASCCompositeFactMapper(conn);
        irsTASCContentAreaFactMapper = new IrsTASCContentAreaFactMapper(conn);
        irsTASCPrimObjFactMapper = new IrsTASCPrimObjFactMapper(conn);
        irsTASCSecObjFactMapper = new IrsTASCSecObjFactMapper(conn);
        irsTASCItemFactMapper = new IrsTASCItemFactMapper(conn);
    }

    public void run() throws IOException, DataException, CTBSystemException, SQLException {
        Long priorResult = new Long(1).equals(context.getCurrentResultId())?new Long(2):new Long(1);
                
        IrsTASCPredSubjectFactData tabePredSubjectFactData = new IrsTASCPredSubjectFactData();
        tabePredSubjectFactData.setStudentid(context.getStudentId());
        tabePredSubjectFactData.setSessionid(context.getSessionId());
        tabePredSubjectFactData.setCurrentResultid(priorResult);
        tabePredSubjectFactData.setAssessmentid(context.getAssessmentId());
        tabePredSubjectFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabePredSubjectFactData.setProgramid(context.getProgramId());
        irsTASCPredSubjectFactMapper.updateCurrentResultStatus(tabePredSubjectFactData);
        
        IrsTASCCompositeFactData tabeCompositeFactdata = new IrsTASCCompositeFactData();
        tabeCompositeFactdata.setStudentid(context.getStudentId());
        tabeCompositeFactdata.setSessionid(context.getSessionId());
        tabeCompositeFactdata.setCurrentResultid(priorResult);
        tabeCompositeFactdata.setAssessmentid(context.getAssessmentId());
        tabeCompositeFactdata.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeCompositeFactdata.setProgramid(context.getProgramId());
        irsTASCCompositeFactMapper.updateCurrentResultStatus(tabeCompositeFactdata);
        
        IrsTASCContentAreaFactData tabeContentAreaFactData = new IrsTASCContentAreaFactData();
        tabeContentAreaFactData.setStudentid(context.getStudentId());
        tabeContentAreaFactData.setSessionid(context.getSessionId());
        tabeContentAreaFactData.setCurrentResultid(priorResult);
        tabeContentAreaFactData.setAssessmentid(context.getAssessmentId());
        tabeContentAreaFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeContentAreaFactData.setProgramid(context.getProgramId());
        irsTASCContentAreaFactMapper.updateCurrentResultStatus(tabeContentAreaFactData);
        
        IrsTASCPrimObjFactData tabePrimObjFactData = new IrsTASCPrimObjFactData();
        tabePrimObjFactData.setStudentid(context.getStudentId());
        tabePrimObjFactData.setSessionid(context.getSessionId());
        tabePrimObjFactData.setCurrentResultid(priorResult);
        tabePrimObjFactData.setAssessmentid(context.getAssessmentId());
        tabePrimObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabePrimObjFactData.setProgramid(context.getProgramId());
        irsTASCPrimObjFactMapper.updateCurrentResultStatus(tabePrimObjFactData);
        
        IrsTASCSecObjFactData tabeSecObjFactData = new IrsTASCSecObjFactData();
        tabeSecObjFactData.setStudentid(context.getStudentId());
        tabeSecObjFactData.setSessionid(context.getSessionId());
        tabeSecObjFactData.setCurrentResultid(priorResult);
        tabeSecObjFactData.setAssessmentid(context.getAssessmentId());
        tabeSecObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeSecObjFactData.setProgramid(context.getProgramId());
        irsTASCSecObjFactMapper.updateCurrentResultStatus(tabeSecObjFactData);
        
        IrsTASCItemFactData tabeItemFactData = new IrsTASCItemFactData();
        tabeItemFactData.setStudentid(context.getStudentId());
        tabeItemFactData.setSessionid(context.getSessionId());
        tabeItemFactData.setCurrentResultid(priorResult);
        tabeItemFactData.setAssessmentid(context.getAssessmentId());
        tabeItemFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeItemFactData.setProgramid(context.getProgramId());
        irsTASCItemFactMapper.updateCurrentResultStatus(tabeItemFactData);
    }
} 