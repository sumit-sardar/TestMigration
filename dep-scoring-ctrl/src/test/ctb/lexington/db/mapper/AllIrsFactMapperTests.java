package test.ctb.lexington.db.mapper;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Rama_Rao
 *
 */

public class AllIrsFactMapperTests{

	public static Test suite(){
		
		TestSuite suite = new TestSuite("All IRSTABEFactTests");
		
		suite.addTestSuite(IrsTABECompositeFactMapperTest.class);
		suite.addTestSuite(IrsTABEItemFactMapperTest.class);
		suite.addTestSuite(IrsTABEPrimObjFactMapperTest.class);
		suite.addTestSuite(IrsTABESecObjFactMapperTest.class);
		suite.addTestSuite(IrsTABEContentAreaFactMapperTest.class);
		
		return suite;
	}	
}