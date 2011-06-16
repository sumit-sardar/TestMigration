package com.ctb.oas.normsdata;

import junit.framework.TestCase;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class DefaultScoreIDGeneratorTest extends TestCase {
    private DefaultScoreIDGenerator generator = new DefaultScoreIDGenerator();
    StringBuffer buffer = new StringBuffer();
    private static final String CONTENT_AREA_NAME = "Language Arts";
    private static final String FRAMEWORK_CODE = ScoreRecord.TERRANOVA_FRAMEWORK_CODE;
    private static final String GRADE = "03";
    private static final String LEVEL = "02";
    private static final String NORMS_GROUP = "SPR";
    private static final String NORMS_YEAR = "2000";
    private static final int ID_MAX_LENGTH = 40;

    public void testAddToBuffer() {
        buffer = new StringBuffer();
        generator.addToBuffer(null, buffer, false);
        assertEquals(0, buffer.length());

        generator.addToBuffer("foo", buffer, false);
        assertEquals("foo", buffer.toString());
    }

    public void testAddContentArea() {
        generator.addContentArea(CONTENT_AREA_NAME, buffer, false);
        assertEquals("LaAr", buffer.toString());
    }

    public void testAddNormsGroup() {
        generator.addNormsGroup("FALL", buffer, false);
        assertEquals("FAL", buffer.toString());
        buffer = new StringBuffer();
        generator.addNormsGroup("SPRING", buffer, false);
        assertEquals("SPR", buffer.toString());
    }

    public void testGenerateID() {
        NormsData normsData = new NormsData();
        normsData.setFrameworkCode(ScoreRecord.TERRANOVA_FRAMEWORK_CODE);
        normsData.setProduct(ScoreRecord.TERRANOVA_PRODUCT_NAME);
        normsData.setForm(null);
        normsData.setGrade(GRADE);
        normsData.setLevel(LEVEL);
        normsData.setNormsGroup(NORMS_GROUP);
        normsData.setNormsYear(NORMS_YEAR);
        String id = generator.generateId(normsData, CONTENT_AREA_NAME);
        assertEquals(FRAMEWORK_CODE + "_" + generator.getProduct(normsData) + "_" + NORMS_YEAR + "_" + LEVEL + "_" + NORMS_GROUP + "_" + GRADE + "_" + "LaAr", id);
        assertLength(ID_MAX_LENGTH, id.length());
    }

    private void assertLength(int idMaxLength, int i) {
        if (i > idMaxLength) {
            fail("value should be less than " + idMaxLength + ". was : " + i);
        }
    }
}
