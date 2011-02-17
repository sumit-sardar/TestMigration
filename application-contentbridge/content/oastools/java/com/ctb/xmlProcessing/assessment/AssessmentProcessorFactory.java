/*
 * Created on Jan 20, 2004
 */
package com.ctb.xmlProcessing.assessment;




import net.sf.hibernate.Session;

public class AssessmentProcessorFactory {

    public static AssessmentProcessor getAssessmentProcessorSofa(Session session) {
        return new AssessmentProcessor(new AssessmentBuilder(), new AssessmentValidaterSofa(),
                new AssessmentWriterOAS(session, AssessmentType.SOFA));
    }

    public static AssessmentProcessor getAssessmentProcessorTerranova(Session session) {
        return new AssessmentProcessor(new AssessmentBuilder(), new AssessmentValidaterTerranova(),
                new AssessmentWriterOAS(session, AssessmentType.TERRANOVA));
    }
    
    public static AssessmentProcessor getAssessmentProcessorStandard(Session session) 
    {
        return new AssessmentProcessor(new AssessmentBuilderStandard(), new AssessmentValidaterStandard(),
                new AssessmentWriterOAS(session, AssessmentType.SOFA));
    }
}