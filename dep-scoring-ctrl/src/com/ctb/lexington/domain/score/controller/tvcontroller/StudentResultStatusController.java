package com.ctb.lexington.domain.score.controller.tvcontroller; 

import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVCompositeFactData;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVPrimObjFactData;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVSecObjFactData;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVItemFactData;
import com.ctb.lexington.db.mapper.tvmapper.IrsTVCompositeFactMapper;
import com.ctb.lexington.db.mapper.tvmapper.IrsTVContentAreaFactMapper;
import com.ctb.lexington.db.mapper.tvmapper.IrsTVPrimObjFactMapper;
import com.ctb.lexington.db.mapper.tvmapper.IrsTVSecObjFactMapper;
import com.ctb.lexington.db.mapper.tvmapper.IrsTVItemFactMapper;

/**
 * @author Rama_Rao
 *
 */
public class StudentResultStatusController{ 
    
    private ContextData context;    
    private IrsTVCompositeFactMapper irsTNCompositeFactMapper;
    private IrsTVContentAreaFactMapper irsTNContentAreaFactMapper;
    private IrsTVPrimObjFactMapper  irsTNPrimObjFactMapper;
    private IrsTVSecObjFactMapper irsTNSecObjFactMapper;
    private IrsTVItemFactMapper irsTNItemFactMapper;
    
    public StudentResultStatusController(Connection conn, ContextData context){
        this.context = context;
        irsTNCompositeFactMapper = new IrsTVCompositeFactMapper(conn);
        irsTNContentAreaFactMapper = new IrsTVContentAreaFactMapper(conn);
        irsTNPrimObjFactMapper = new IrsTVPrimObjFactMapper(conn);
        irsTNSecObjFactMapper = new IrsTVSecObjFactMapper(conn);
        irsTNItemFactMapper = new IrsTVItemFactMapper(conn);
    }

    public void run() throws IOException, DataException, CTBSystemException, SQLException {
        Long priorResult = new Long(1).equals(context.getCurrentResultId())?new Long(2):new Long(1);
        
        IrsTVCompositeFactData TNCompositeFactdata = new IrsTVCompositeFactData();
        TNCompositeFactdata.setStudentid(context.getStudentId());
        TNCompositeFactdata.setSessionid(context.getSessionId());
        TNCompositeFactdata.setCurrentResultid(priorResult);
        TNCompositeFactdata.setAssessmentid(context.getAssessmentId());
        TNCompositeFactdata.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        TNCompositeFactdata.setProgramid(context.getProgramId());
        irsTNCompositeFactMapper.updateCurrentResultStatus(TNCompositeFactdata);
        
        IrsTVContentAreaFactData TNContentAreaFactData = new IrsTVContentAreaFactData();
        TNContentAreaFactData.setStudentid(context.getStudentId());
        TNContentAreaFactData.setSessionid(context.getSessionId());
        TNContentAreaFactData.setCurrentResultid(priorResult);
        TNContentAreaFactData.setAssessmentid(context.getAssessmentId());
        TNContentAreaFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        TNContentAreaFactData.setProgramid(context.getProgramId());
        irsTNContentAreaFactMapper.updateCurrentResultStatus(TNContentAreaFactData);
        
        IrsTVPrimObjFactData TNPrimObjFactData = new IrsTVPrimObjFactData();
        TNPrimObjFactData.setStudentid(context.getStudentId());
        TNPrimObjFactData.setSessionid(context.getSessionId());
        TNPrimObjFactData.setCurrentResultid(priorResult);
        TNPrimObjFactData.setAssessmentid(context.getAssessmentId());
        TNPrimObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        TNPrimObjFactData.setProgramid(context.getProgramId());
        irsTNPrimObjFactMapper.updateCurrentResultStatus(TNPrimObjFactData);
        
        IrsTVSecObjFactData TNSecObjFactData = new IrsTVSecObjFactData();
        TNSecObjFactData.setStudentid(context.getStudentId());
        TNSecObjFactData.setSessionid(context.getSessionId());
        TNSecObjFactData.setCurrentResultid(priorResult);
        TNSecObjFactData.setAssessmentid(context.getAssessmentId());
        TNSecObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        TNSecObjFactData.setProgramid(context.getProgramId());
        irsTNSecObjFactMapper.updateCurrentResultStatus(TNSecObjFactData);
        
        IrsTVItemFactData TNItemFactData = new IrsTVItemFactData();
        TNItemFactData.setStudentid(context.getStudentId());
        TNItemFactData.setSessionid(context.getSessionId());
        TNItemFactData.setCurrentResultid(priorResult);
        TNItemFactData.setAssessmentid(context.getAssessmentId());
        TNItemFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        TNItemFactData.setProgramid(context.getProgramId());
        irsTNItemFactMapper.updateCurrentResultStatus(TNItemFactData);
    }
} 