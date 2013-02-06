/*
 * Created on Jan 20, 2004
 */
package com.ctb.contentBridge.core.publish.xml.assessment;

import com.ctb.contentBridge.core.publish.dao.AbstractDBAssessmentGateway;
import com.ctb.contentBridge.core.publish.dao.DBAssessmentGateway;

import net.sf.hibernate.Session;

public class AssessmentWriterOAS {

    private AbstractDBAssessmentGateway gateway;
    private AssessmentType assessmentType;

    public AssessmentWriterOAS(Session session, AssessmentType assessmentType) {
        this.gateway = new DBAssessmentGateway(session);
        this.assessmentType = assessmentType;
    }

    public AssessmentWriterOAS(Session session, AssessmentType assessmentType,
            AbstractDBAssessmentGateway gateway) {
        this.gateway = new DBAssessmentGateway(session);
        this.assessmentType = assessmentType;
        this.gateway = gateway;
    }

    public void writeAssessment(AssessmentHolder assessmentHolder) {
        gateway.writeAssessment(assessmentHolder, assessmentType);
    }

}