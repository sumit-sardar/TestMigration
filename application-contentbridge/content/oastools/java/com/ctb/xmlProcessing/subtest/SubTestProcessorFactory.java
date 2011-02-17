package com.ctb.xmlProcessing.subtest;

import java.util.ArrayList;

import net.sf.hibernate.Session;

import com.ctb.common.tools.ADSConfig;
import com.ctb.sofa.ScorableItemConfig;
import com.ctb.xmlProcessing.assessment.AssessmentType;


/**
 * @author wmli
 */
public class SubTestProcessorFactory {
    public static SubTestProcessor getSubTestProcessorTerranova(Session session) {
        return new SubTestProcessor(new SubTestBuilderTerranova(), new SubTestValidaterTerranova(),
                new SubTestMediaGeneratorTerranova(), new SubTestDBValidaterNull(),
                new SubTestWriterOAS(session, AssessmentType.TERRANOVA));
    }
    
    public static SchedulableUnitProcessor getSchedulableUnitProcessor(Session session, boolean doSubtestMedia ) 
    {
        if ( doSubtestMedia )
        {
	        return new SchedulableUnitProcessor(new SubTestBuilderSofa(), new SubTestValidaterStandard(),
	                new SubTestMediaGeneratorSofa(), new SubTestDBValidaterNull(),
	                new SubTestWriterOAS(session, AssessmentType.SOFA));
        }
        else
        {
            return new SchedulableUnitProcessor(new SubTestBuilderSofa(), new SubTestValidaterStandard(),
                    new SubTestMediaGeneratorTerranova(), new SubTestDBValidaterNull(),
                    new SubTestWriterOAS(session, AssessmentType.SOFA));
        }
    }
    
    public static DeliverableUnitProcessor getDeliverableUnitProcessor(Session session, boolean doSubtestMedia, ArrayList unicodeList, ADSConfig adsConfig ) 
    {
        if ( doSubtestMedia )
        {
	        return new DeliverableUnitProcessor(new SubTestBuilderSofa(), new SubTestValidaterStandard(),
	                new SubTestMediaGeneratorSofa(), new SubTestDBValidaterNull(),
	                new SubTestWriterOAS(session, AssessmentType.SOFA), unicodeList,adsConfig );
        }
        else
        {
            return new DeliverableUnitProcessor(new SubTestBuilderSofa(), new SubTestValidaterStandard(),
                    new SubTestMediaGeneratorTerranova(), new SubTestDBValidaterNull(),
                    new SubTestWriterOAS(session, AssessmentType.SOFA), unicodeList, adsConfig);
        }
    }

    public static SubTestProcessor getSubTestProcessorSofa(Session session,
            ScorableItemConfig scorableItemConfig) {
        return new SubTestProcessor(new SubTestBuilderSofa(scorableItemConfig),
                new SubTestValidaterSofa(), new SubTestMediaGeneratorSofa(),
                new SubTestDBValidaterSofa(session), new SubTestWriterOAS(session,
                        AssessmentType.SOFA));
    }
}