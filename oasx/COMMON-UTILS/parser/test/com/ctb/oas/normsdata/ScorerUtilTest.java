package com.ctb.oas.normsdata;

import junit.framework.TestCase;

import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScorerUtilTest extends TestCase {
    private static final String SCALE_SCORE_HEADER = " 1 NUMBER CORRECT - SCALE SCORE NORMS FOR TN CB            FM B  LEVEL 11";
    private static final String SCALE_SCORE_INSTRUCTION = " 5 0033007";
    private static final String INSTRUCTION_LINE_1 = " 5 0033001\n" +
            " 6 135135135135135135135088042027020016014013012011011010010010010010011011012012\n" +
            " 6 013014015017020027059";
    private static final String INSTRUCTION_LINE_2 = " 6 013014015017020027059\n" +
            " 5 0021002";

    private static final String NCE_HEADER_LINE = "1 SS - NCE FOR TN      LEVELS 10 - 22           GRADE-01,QUARTERMONTH-06";
    private static final String NCE_HEADER_LINE_2 = "1 SS - NCE FOR TN      LEVELS 10 - 22           GRADE-KG,QUARTERMONTH-06";
    private static final String EXTENDED_GED_HEADER_LINE = " 1 SCALE SCORE - EXTENDED GRADE EQUIVALENT FOR TERRANOVA    FALL";
    private static final String CSV_FILENAME1 = "CB9RDM.CSV";
    private static final String CSV_FILENAME2 = "CB9TMAp1.txt";

    public void testGetForm() {
        assertEquals("B", ScorerUtil.getForm(SCALE_SCORE_HEADER));
    }

    public void testGetLevel() {
        assertEquals("11", ScorerUtil.getLevel(SCALE_SCORE_HEADER));
        assertNull(ScorerUtil.getLevel(NCE_HEADER_LINE));
    }

    public void testGetContentArea() {
        assertEquals("Mathematics", ScorerUtil.getContentArea(SCALE_SCORE_INSTRUCTION));
    }


    public void testGetLineForCode() {
        List list1 = TestUtils.getTokenizedList(INSTRUCTION_LINE_1);
        List list2 = TestUtils.getTokenizedList(INSTRUCTION_LINE_2);

        String line1 = ScorerUtil.getLineForCode(list1.listIterator(), LayoutConstants.INSTRUCTION_CODE);
        String line2 = ScorerUtil.getLineForCode(list2.listIterator(), LayoutConstants.INSTRUCTION_CODE);
        assertEquals("Reading", ScorerUtil.getContentArea(line1));
        assertEquals("Vocabulary", ScorerUtil.getContentArea(line2));
    }

    public void testGetGrade() {
        String grade = ScorerUtil.getGrade(NCE_HEADER_LINE);
        String grade2 = ScorerUtil.getGrade(EXTENDED_GED_HEADER_LINE);
        String grade3 = ScorerUtil.getGrade(NCE_HEADER_LINE_2);
        assertEquals("1", grade);
        assertEquals(null, grade2);
        assertEquals("KG", grade3);
    }

    public void testGetTrimester() {
        assertEquals("FALL", ScorerUtil.getTrimester(NCE_HEADER_LINE));
        assertEquals("FALL", ScorerUtil.getTrimester(EXTENDED_GED_HEADER_LINE));
    }

    public void testGetContentAreaFromFileName() {
        assertEquals("Reading", ScorerUtil.getContentAreafromFileName(CSV_FILENAME1));
    }

    public void testGetFormFromFileName() {
        assertEquals("9", ScorerUtil.getFormFromFileName(CSV_FILENAME1));
    }

    public void testGetLevelFromFileName() {
        assertEquals("M", ScorerUtil.getLevelFromFileName(CSV_FILENAME1));
        assertEquals("A", ScorerUtil.getLevelFromFileName(CSV_FILENAME2));
    }
}
