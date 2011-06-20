package com.ctb.lexington.domain.score.controller.llcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLCompositeFactData;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLContentAreaFactData;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLItemFactData;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLPrimObjFactData;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLSecObjFactData;
import com.ctb.lexington.db.mapper.llmapper.IrsLLCompositeFactMapper;
import com.ctb.lexington.db.mapper.llmapper.IrsLLContentAreaFactMapper;
import com.ctb.lexington.db.mapper.llmapper.IrsLLItemFactMapper;
import com.ctb.lexington.db.mapper.llmapper.IrsLLPrimObjFactMapper;
import com.ctb.lexington.db.mapper.llmapper.IrsLLSecObjFactMapper;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;

/**
 * @author Rama_Rao
 *
 */
public class StudentResultStatusController{ 
    
    private ContextData context;    
    private IrsLLCompositeFactMapper irsLLCompositeFactMapper;
    private IrsLLContentAreaFactMapper irsLLContentAreaFactMapper;
    private IrsLLPrimObjFactMapper  irsLLPrimObjFactMapper;
    private IrsLLSecObjFactMapper irsLLSecObjFactMapper;
    private IrsLLItemFactMapper irsLLItemFactMapper;
    
    
    public StudentResultStatusController(Connection conn, ContextData context){
        this.context = context;
        irsLLCompositeFactMapper = new IrsLLCompositeFactMapper(conn);
        irsLLContentAreaFactMapper = new IrsLLContentAreaFactMapper(conn);
        irsLLPrimObjFactMapper = new IrsLLPrimObjFactMapper(conn);
        irsLLSecObjFactMapper = new IrsLLSecObjFactMapper(conn);
        irsLLItemFactMapper = new IrsLLItemFactMapper(conn);
    }

    public void run() throws IOException, DataException, CTBSystemException, SQLException {
        Long priorResult = new Long(1).equals(context.getCurrentResultId())?new Long(2):new Long(1);
                
       
        IrsLLCompositeFactData LLCompositeFactdata = new IrsLLCompositeFactData();
        LLCompositeFactdata.setStudentid(context.getStudentId());
        LLCompositeFactdata.setSessionid(context.getSessionId());
        LLCompositeFactdata.setCurrentResultid(priorResult);
        LLCompositeFactdata.setAssessmentid(context.getAssessmentId());
        LLCompositeFactdata.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        LLCompositeFactdata.setProgramid(context.getProgramId());
        irsLLCompositeFactMapper.updateCurrentResultStatus(LLCompositeFactdata);
        
        IrsLLContentAreaFactData LLContentAreaFactData = new IrsLLContentAreaFactData();
        LLContentAreaFactData.setStudentid(context.getStudentId());
        LLContentAreaFactData.setSessionid(context.getSessionId());
        LLContentAreaFactData.setCurrentResultid(priorResult);
        LLContentAreaFactData.setAssessmentid(context.getAssessmentId());
        LLContentAreaFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        LLContentAreaFactData.setProgramid(context.getProgramId());
        irsLLContentAreaFactMapper.updateCurrentResultStatus(LLContentAreaFactData);
        
        IrsLLPrimObjFactData LLPrimObjFactData = new IrsLLPrimObjFactData();
        LLPrimObjFactData.setStudentid(context.getStudentId());
        LLPrimObjFactData.setSessionid(context.getSessionId());
        LLPrimObjFactData.setCurrentResultid(priorResult);
        LLPrimObjFactData.setAssessmentid(context.getAssessmentId());
        LLPrimObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        LLPrimObjFactData.setProgramid(context.getProgramId());
        irsLLPrimObjFactMapper.updateCurrentResultStatus(LLPrimObjFactData);
        
        IrsLLSecObjFactData LLSecObjFactData = new IrsLLSecObjFactData();
        LLSecObjFactData.setStudentid(context.getStudentId());
        LLSecObjFactData.setSessionid(context.getSessionId());
        LLSecObjFactData.setCurrentResultid(priorResult);
        LLSecObjFactData.setAssessmentid(context.getAssessmentId());
        LLSecObjFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        LLSecObjFactData.setProgramid(context.getProgramId());
        irsLLSecObjFactMapper.updateCurrentResultStatus(LLSecObjFactData);
        
        IrsLLItemFactData LLItemFactData = new IrsLLItemFactData();
        LLItemFactData.setStudentid(context.getStudentId());
        LLItemFactData.setSessionid(context.getSessionId());
        LLItemFactData.setCurrentResultid(priorResult);
        LLItemFactData.setAssessmentid(context.getAssessmentId());
        LLItemFactData.setTestCompletionTimestamp(context.getTestCompletionTimestamp());
        LLItemFactData.setProgramid(context.getProgramId());
        irsLLItemFactMapper.updateCurrentResultStatus(LLItemFactData);
    }
} 