package com.ctb.oas.normsdata;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class AllTests extends TestCase {
    public static TestSuite suite() {
        TestSuite suite = new TestSuite("All NormsData Tests");
        suite.addTest(new TestSuite(ContentAreaLookupTest.class));
        suite.addTest(new TestSuite(DefaultScoreIDGeneratorTest.class));
        suite.addTest(new TestSuite(DefaultScoreRecordWriterTest.class));
        suite.addTest(new TestSuite(EGEDScorerTest.class));
        suite.addTest(new TestSuite(ExtendedGradeEquivalentScoreRecordWriterTest.class));
        suite.addTest(new TestSuite(NCEScorerTest.class));
        suite.addTest(new TestSuite(NPScorerTest.class));
        suite.addTest(new TestSuite(ParseDataTest.class));
        suite.addTest(new TestSuite(ScaleScoreScorerTest.class));
        suite.addTest(new TestSuite(ScoreRecordTest.class));
        suite.addTest(new TestSuite(ScorerUtilTest.class));
        suite.addTest(new TestSuite(ScoreTypeTest.class));
        suite.addTest(new TestSuite(SEMScorerTest.class));
        suite.addTest(new TestSuite(TABELoaderTest.class));
        suite.addTest(new TestSuite(TABEScorerTest.class));
        suite.addTest(new TestSuite(TABESSToGEScorerTest.class));
        suite.addTest(new TestSuite(TableTypeTest.class));
        suite.addTest(new TestSuite(TerraNovaLoaderTest.class));
        return suite;
    }
}
