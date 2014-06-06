package com.ctb.lexington.domain.score.controller.tccontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.irsdata.irstcdata.IrsTCContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstcdata.IrsTCItemFactData;
import com.ctb.lexington.db.irsdata.irstcdata.IrsTCPrimObjFactData;
import com.ctb.lexington.db.irsdata.irstcdata.IrsTCSecObjFactData;
import com.ctb.lexington.db.mapper.tcmapper.IrsTCContentAreaFactMapper;
import com.ctb.lexington.db.mapper.tcmapper.IrsTCItemFactMapper;
import com.ctb.lexington.db.mapper.tcmapper.IrsTCPrimObjFactMapper;
import com.ctb.lexington.db.mapper.tcmapper.IrsTCSecObjFactMapper;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;

/**
 * @author Rama_Rao
 *
 */
public class StudentResultStatusController{ 
    
    private ContextData context;    
    //private IrsTCCompositeFactMapper irsTCCompositeFactMapper;
    private IrsTCContentAreaFactMapper irsTCContentAreaFactMapper;
    private IrsTCPrimObjFactMapper  irsTCPrimObjFactMapper;
    private IrsTCSecObjFactMapper irsTCSecObjFactMapper;
    private IrsTCItemFactMapper irsTCItemFactMapper;
    
    public StudentResultStatusController(Connection conn, ContextData context){
        this.context = context;
        //irsTCCompositeFactMapper = new IrsTCCompositeFactMapper(conn);
        irsTCContentAreaFactMapper = new IrsTCContentAreaFactMapper(conn);
        irsTCPrimObjFactMapper = new IrsTCPrimObjFactMapper(conn);
        irsTCSecObjFactMapper = new IrsTCSecObjFactMapper(conn);
        irsTCItemFactMapper = new IrsTCItemFactMapper(conn);
    }

    public void run() throws IOException, DataException, CTBSystemException, SQLException {
        Long priorResult = new Long(1).equals(context.getCurrentResultId())?new Long(2):new Long(1);
                
        IrsTCContentAreaFactData tabeCcssContentAreaFactData = new IrsTCContentAreaFactData();
        tabeCcssContentAreaFactData.setStudentid(context.getStudentId());
        tabeCcssContentAreaFactData.setSessionid(context.getSessionId());
        tabeCcssContentAreaFactData.setCurrentResultid(priorResult);
        tabeCcssContentAreaFactData.setAssessmentid(context.getAssessmentId());
        tabeCcssContentAreaFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeCcssContentAreaFactData.setProgramid(context.getProgramId());
        irsTCContentAreaFactMapper.updateCurrentResultStatus(tabeCcssContentAreaFactData);
        
        IrsTCPrimObjFactData tabeCcssPrimObjFactData = new IrsTCPrimObjFactData();
        tabeCcssPrimObjFactData.setStudentid(context.getStudentId());
        tabeCcssPrimObjFactData.setSessionid(context.getSessionId());
        tabeCcssPrimObjFactData.setCurrentResultid(priorResult);
        tabeCcssPrimObjFactData.setAssessmentid(context.getAssessmentId());
        tabeCcssPrimObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeCcssPrimObjFactData.setProgramid(context.getProgramId());
        irsTCPrimObjFactMapper.updateCurrentResultStatus(tabeCcssPrimObjFactData);
        
        IrsTCSecObjFactData tabeCcssSecObjFactData = new IrsTCSecObjFactData();
        tabeCcssSecObjFactData.setStudentid(context.getStudentId());
        tabeCcssSecObjFactData.setSessionid(context.getSessionId());
        tabeCcssSecObjFactData.setCurrentResultid(priorResult);
        tabeCcssSecObjFactData.setAssessmentid(context.getAssessmentId());
        tabeCcssSecObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeCcssSecObjFactData.setProgramid(context.getProgramId());
        irsTCSecObjFactMapper.updateCurrentResultStatus(tabeCcssSecObjFactData);
        
        IrsTCItemFactData tabeCcssItemFactData = new IrsTCItemFactData();
        tabeCcssItemFactData.setStudentid(context.getStudentId());
        tabeCcssItemFactData.setSessionid(context.getSessionId());
        tabeCcssItemFactData.setCurrentResultid(priorResult);
        tabeCcssItemFactData.setAssessmentid(context.getAssessmentId());
        tabeCcssItemFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tabeCcssItemFactData.setProgramid(context.getProgramId());
        irsTCItemFactMapper.updateCurrentResultStatus(tabeCcssItemFactData);
    }
} 