/*
 * Created on Jan 20, 2004
 *
 */
package com.ctb.xmlProcessing.subtest;

import java.util.List;

import net.sf.hibernate.Session;

import com.ctb.xmlProcessing.assessment.AssessmentType;

public class SubTestWriterOAS implements SubTestWriter {

    //private Session session;
    private AbstractDBSubTestGateway subTestGateway;
    private DBSubTestMediaGateway mediaGateway;
    private AssessmentType assessmentType;

    public SubTestWriterOAS(Session session, AssessmentType assessmentType) {
		this.subTestGateway = new DBSubTestGateway(session);
		this.mediaGateway = new DBSubTestMediaGateway(session);
		this.assessmentType = assessmentType;
    }
    
    public SubTestWriterOAS(Session session, AssessmentType assessmentType, AbstractDBSubTestGateway gateway) {
		this.subTestGateway = gateway;
		this.mediaGateway = new DBSubTestMediaGateway(session);
		this.assessmentType = assessmentType;
    }

    public List write(SubTestHolder subtest) {    	
		return subTestGateway.writeSubTest(subtest, assessmentType);
    }

}
