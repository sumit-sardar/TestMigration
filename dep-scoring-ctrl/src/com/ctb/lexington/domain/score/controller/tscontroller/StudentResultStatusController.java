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
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCSecObjFactData;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCCompositeFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCContentAreaFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCItemFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCSecObjFactMapper;

/**
 * @author Rama_Rao
 *
 */
public class StudentResultStatusController{ 
    
    private ContextData context;    
    private IrsTASCCompositeFactMapper irsTASCCompositeFactMapper;
    private IrsTASCContentAreaFactMapper irsTASCContentAreaFactMapper;
    private IrsTASCSecObjFactMapper irsTASCSecObjFactMapper;
    private IrsTASCItemFactMapper irsTASCItemFactMapper;
    
    public StudentResultStatusController(Connection conn, ContextData context){
        this.context = context;
        irsTASCCompositeFactMapper = new IrsTASCCompositeFactMapper(conn);
        irsTASCContentAreaFactMapper = new IrsTASCContentAreaFactMapper(conn);
        irsTASCSecObjFactMapper = new IrsTASCSecObjFactMapper(conn);
        irsTASCItemFactMapper = new IrsTASCItemFactMapper(conn);
    }

    public void run() throws IOException, DataException, CTBSystemException, SQLException {
        Long priorResult = new Long(1).equals(context.getCurrentResultId())?new Long(2):new Long(1);
                
        IrsTASCCompositeFactData tascCompositeFactdata = new IrsTASCCompositeFactData();
        tascCompositeFactdata.setStudentid(context.getStudentId());
        tascCompositeFactdata.setSessionid(context.getSessionId());
        tascCompositeFactdata.setCurrentResultid(priorResult);
        tascCompositeFactdata.setAssessmentid(context.getAssessmentId());
        tascCompositeFactdata.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tascCompositeFactdata.setProgramid(context.getProgramId());
        irsTASCCompositeFactMapper.updateCurrentResultStatus(tascCompositeFactdata);
        
        IrsTASCContentAreaFactData tascContentAreaFactData = new IrsTASCContentAreaFactData();
        tascContentAreaFactData.setStudentid(context.getStudentId());
        tascContentAreaFactData.setSessionid(context.getSessionId());
        tascContentAreaFactData.setCurrentResultid(priorResult);
        tascContentAreaFactData.setAssessmentid(context.getAssessmentId());
        tascContentAreaFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tascContentAreaFactData.setProgramid(context.getProgramId());
        irsTASCContentAreaFactMapper.updateCurrentResultStatus(tascContentAreaFactData);
        
        IrsTASCSecObjFactData tascSecObjFactData = new IrsTASCSecObjFactData();
        tascSecObjFactData.setStudentid(context.getStudentId());
        tascSecObjFactData.setSessionid(context.getSessionId());
        tascSecObjFactData.setCurrentResultid(priorResult);
        tascSecObjFactData.setAssessmentid(context.getAssessmentId());
        tascSecObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tascSecObjFactData.setProgramid(context.getProgramId());
        irsTASCSecObjFactMapper.updateCurrentResultStatus(tascSecObjFactData);
        
        IrsTASCItemFactData tascItemFactData = new IrsTASCItemFactData();
        tascItemFactData.setStudentid(context.getStudentId());
        tascItemFactData.setSessionid(context.getSessionId());
        tascItemFactData.setCurrentResultid(priorResult);
        tascItemFactData.setAssessmentid(context.getAssessmentId());
        tascItemFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        tascItemFactData.setProgramid(context.getProgramId());
        irsTASCItemFactMapper.updateCurrentResultStatus(tascItemFactData);
    }
} 