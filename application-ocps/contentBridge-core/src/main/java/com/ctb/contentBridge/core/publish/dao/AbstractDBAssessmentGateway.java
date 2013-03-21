/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.dao;

import com.ctb.contentBridge.core.publish.dao.xml.AbstractDBGateway;
import com.ctb.contentBridge.core.publish.xml.assessment.AssessmentHolder;
import com.ctb.contentBridge.core.publish.xml.assessment.AssessmentType;

import net.sf.hibernate.Session;

public abstract class AbstractDBAssessmentGateway extends AbstractDBGateway {

    public AbstractDBAssessmentGateway(Session session) {
        super(session);
    }

    public abstract void writeAssessment(AssessmentHolder assessment, AssessmentType assessmentType);

}