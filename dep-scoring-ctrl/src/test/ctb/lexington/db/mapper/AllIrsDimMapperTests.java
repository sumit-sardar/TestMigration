package test.ctb.lexington.db.mapper;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Rama_Rao
 *
 */
/*	All Dim Tables Mapper TestSuite    */

public class AllIrsDimMapperTests{
	
    public static Test suite(){
    	
        TestSuite suite = new TestSuite("All IRS Dim Mapper Tests");
        
        suite.addTestSuite(IrsStudentDimMapperTest.class);
        suite.addTestSuite(IrsAssessmentDimMapperTest.class);
        suite.addTestSuite(IrsAttr1DimMapperTest.class);
        suite.addTestSuite(IrsAttr2DimMapperTest.class);
        suite.addTestSuite(IrsAttr3DimMapperTest.class);
        suite.addTestSuite(IrsAttr4DimMapperTest.class);
        suite.addTestSuite(IrsAttr5DimMapperTest.class);
        suite.addTestSuite(IrsAttr6DimMapperTest.class);
        suite.addTestSuite(IrsAttr7DimMapperTest.class);
        suite.addTestSuite(IrsAttr8DimMapperTest.class);
        suite.addTestSuite(IrsAttr9DimMapperTest.class);
        suite.addTestSuite(IrsAttr10DimMapperTest.class);
        suite.addTestSuite(IrsSubjectDimMapperTest.class);
        suite.addTestSuite(IrsCompositeDimMapperTest.class);
        suite.addTestSuite(IrsContentAreaDimMapperTest.class);
        suite.addTestSuite(IrsCurrentResultDimMapperTest.class);
        suite.addTestSuite(IrsFormDimMapperTest.class);
        suite.addTestSuite(IrsGradeDimMapperTest.class);
        suite.addTestSuite(IrsItemDimMapperTest.class);
        suite.addTestSuite(IrsLevelDimMapperTest.class);
        suite.addTestSuite(IrsMasteryLevelDimMapperTest.class);
        suite.addTestSuite(IrsNRSLevelDimMapperTest.class);
        suite.addTestSuite(IrsOrgNodeDimMapperTest.class);
        suite.addTestSuite(IrsPrimObjDimMapperTest.class);
        suite.addTestSuite(IrsProductDimMapperTest.class);
        suite.addTestSuite(IrsProgramDimMapperTest.class);
        suite.addTestSuite(IrsRecActivityDimMapperTest.class);
        suite.addTestSuite(IrsRecLevelDimMapperTest.class);
        suite.addTestSuite(IrsResponseDimMapperTest.class);
        suite.addTestSuite(IrsSchedularDimMapperTest.class);
        suite.addTestSuite(IrsScoreTypeDimMapperTest.class);
        suite.addTestSuite(IrsSecObjDimMapperTest.class);
        suite.addTestSuite(IrsSessionDimMapperTest.class);
        suite.addTestSuite(IrsStudentDimMapperTest.class);
        suite.addTestSuite(IrsCustomerDimMapperTest.class);        
        
        return suite;
    }
}