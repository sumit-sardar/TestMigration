/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.assessment;

import net.sf.hibernate.Session;

import com.ctb.xmlProcessing.AbstractDBGateway;

public abstract class AbstractDBAssessmentGateway extends AbstractDBGateway {

    public AbstractDBAssessmentGateway(Session session) {
        super(session);
    }

    public abstract void writeAssessment(AssessmentHolder assessment, AssessmentType assessmentType);

}