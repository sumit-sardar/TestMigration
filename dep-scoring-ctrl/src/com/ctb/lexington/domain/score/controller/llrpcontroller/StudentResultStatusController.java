package com.ctb.lexington.domain.score.controller.llrpcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPCompositeFactData;
import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPContentAreaFactData;
import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPItemFactData;
import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPPrimObjFactData;
import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPSecObjFactData;
import com.ctb.lexington.db.mapper.llrpmapper.IrsLLRPCompositeFactMapper;
import com.ctb.lexington.db.mapper.llrpmapper.IrsLLRPContentAreaFactMapper;
import com.ctb.lexington.db.mapper.llrpmapper.IrsLLRPItemFactMapper;
import com.ctb.lexington.db.mapper.llrpmapper.IrsLLRPPrimObjFactMapper;
import com.ctb.lexington.db.mapper.llrpmapper.IrsLLRPSecObjFactMapper;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;

/**
 * @author TCS
 *
 */
public class StudentResultStatusController{ 
    
    private ContextData context;    
    private IrsLLRPCompositeFactMapper irsLLRPCompositeFactMapper;
    private IrsLLRPContentAreaFactMapper irsLLRPContentAreaFactMapper;
    private IrsLLRPPrimObjFactMapper  irsLLRPPrimObjFactMapper;
    private IrsLLRPSecObjFactMapper irsLLRPSecObjFactMapper;
    private IrsLLRPItemFactMapper irsLLRPItemFactMapper;
    
    
    public StudentResultStatusController(Connection conn, ContextData context){
        this.context = context;
        irsLLRPCompositeFactMapper = new IrsLLRPCompositeFactMapper(conn);
        irsLLRPContentAreaFactMapper = new IrsLLRPContentAreaFactMapper(conn);
        irsLLRPPrimObjFactMapper = new IrsLLRPPrimObjFactMapper(conn);
        irsLLRPSecObjFactMapper = new IrsLLRPSecObjFactMapper(conn);
        irsLLRPItemFactMapper = new IrsLLRPItemFactMapper(conn);
    }

    public void run() throws IOException, DataException, CTBSystemException, SQLException {
        Long priorResult = new Long(1).equals(context.getCurrentResultId())?new Long(2):new Long(1);
                
       
        IrsLLRPCompositeFactData llCompositeFactdata = new IrsLLRPCompositeFactData();
        llCompositeFactdata.setStudentid(context.getStudentId());
        llCompositeFactdata.setSessionid(context.getSessionId());
        llCompositeFactdata.setCurrentResultid(priorResult);
        llCompositeFactdata.setAssessmentid(context.getAssessmentId());
        llCompositeFactdata.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        llCompositeFactdata.setProgramid(context.getProgramId());
        irsLLRPCompositeFactMapper.updateCurrentResultStatus(llCompositeFactdata);
        
        IrsLLRPContentAreaFactData llContentAreaFactData = new IrsLLRPContentAreaFactData();
        llContentAreaFactData.setStudentid(context.getStudentId());
        llContentAreaFactData.setSessionid(context.getSessionId());
        llContentAreaFactData.setCurrentResultid(priorResult);
        llContentAreaFactData.setAssessmentid(context.getAssessmentId());
        llContentAreaFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        llContentAreaFactData.setProgramid(context.getProgramId());
        irsLLRPContentAreaFactMapper.updateCurrentResultStatus(llContentAreaFactData);
        
        IrsLLRPPrimObjFactData llPrimObjFactData = new IrsLLRPPrimObjFactData();
        llPrimObjFactData.setStudentid(context.getStudentId());
        llPrimObjFactData.setSessionid(context.getSessionId());
        llPrimObjFactData.setCurrentResultid(priorResult);
        llPrimObjFactData.setAssessmentid(context.getAssessmentId());
        llPrimObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        llPrimObjFactData.setProgramid(context.getProgramId());
        irsLLRPPrimObjFactMapper.updateCurrentResultStatus(llPrimObjFactData);
        
        IrsLLRPSecObjFactData llSecObjFactData = new IrsLLRPSecObjFactData();
        llSecObjFactData.setStudentid(context.getStudentId());
        llSecObjFactData.setSessionid(context.getSessionId());
        llSecObjFactData.setCurrentResultid(priorResult);
        llSecObjFactData.setAssessmentid(context.getAssessmentId());
        llSecObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        llSecObjFactData.setProgramid(context.getProgramId());
        irsLLRPSecObjFactMapper.updateCurrentResultStatus(llSecObjFactData);
        
        IrsLLRPItemFactData llItemFactData = new IrsLLRPItemFactData();
        llItemFactData.setStudentid(context.getStudentId());
        llItemFactData.setSessionid(context.getSessionId());
        llItemFactData.setCurrentResultid(priorResult);
        llItemFactData.setAssessmentid(context.getAssessmentId());
        llItemFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        llItemFactData.setProgramid(context.getProgramId());
        irsLLRPItemFactMapper.updateCurrentResultStatus(llItemFactData);
    }
} 