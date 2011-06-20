package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ctb.lexington.db.mapper.ScoreLookupMapper;
import com.ctb.lexington.db.record.ScoreLookupRecord;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.Stringx;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class ScoreLookupHelper {
    public BigDecimal getScore(Integer itemSetId, String contentArea, String normGroup,
            String testForm, String testLevel, String grade, ScoreLookupCode sourceScoreType,
            BigDecimal sourceScoreValue, ScoreLookupCode destScoreType, String ageCategory,
            Connection conn) {
        return getScore(DatabaseHelper.asLong(itemSetId), contentArea, normGroup, testForm,
                testLevel, grade, sourceScoreType, sourceScoreValue, destScoreType, ageCategory,
                conn);
    }

    public String getGradeEquivalent(Integer itemSetId, String contentArea, String normGroup,
            String testForm, String testLevel, String grade, ScoreLookupCode sourceScoreType,
            BigDecimal sourceScoreValue, ScoreLookupCode destScoreType, String ageCategory,
            Connection conn) {
        final ScoreLookupRecord record = getScoreLookup(DatabaseHelper.asLong(itemSetId),
                contentArea, normGroup, testForm, testLevel, grade, sourceScoreType,
                sourceScoreValue, destScoreType, ageCategory, conn);

        return buildGradeEquivalent(record);
    }

    public BigDecimal getScore(Integer itemSetId, String normGroup,
            ScoreLookupCode sourceScoreType, BigDecimal sourceScoreValue,
            ScoreLookupCode destScoreType, String ageCategory, Connection conn) {
        return getScore(DatabaseHelper.asLong(itemSetId), normGroup, sourceScoreType,
                sourceScoreValue, destScoreType, ageCategory, conn);
    }

    public BigDecimal getTerraNovaPerformanceLevel(BigDecimal sourceScoreValue, String contentArea,
            String testLevel, Connection conn) {
        try {
            final ScoreLookupMapper mapper = new ScoreLookupMapper(conn);
            return mapper.findTerraNovaPerformanceLevel(sourceScoreValue, testLevel, contentArea);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public BigDecimal getLasLinkPerformanceLevel(BigDecimal sourceScoreValue, String contentArea,
            String testLevel, Connection conn) {
        try {
            final ScoreLookupMapper mapper = new ScoreLookupMapper(conn);
            return mapper.findLasLinkPerformanceLevel(sourceScoreValue, testLevel, contentArea);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal getObjectivePValue(final Long itemSetId, final String testForm, final String contentArea, final String normGroup, final String grade, final String level, Connection conn) {
        try {
            final ScoreLookupMapper mapper = new ScoreLookupMapper(conn);
            return mapper.findObjectivePValue(itemSetId, testForm, contentArea, normGroup, grade, level);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ScoreLookupRecord getTerraNovaCompositeScore(BigDecimal sourceScoreValue,
            ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType, String normGroup, String normYear,
            String grade, Connection conn) {
        try {
            final ScoreLookupMapper mapper = new ScoreLookupMapper(conn);
            String gradeToQueryWith = grade;
            if (ScoreLookupCode.GRADE_EQUIVALENT.equals(destScoreType))
                gradeToQueryWith = null;
            ScoreLookupRecord record = mapper.findTerraNovaCompositeScore(
                    sourceScoreType.getCode(), destScoreType.getCode(), sourceScoreValue,
                    gradeToQueryWith, normGroup, normYear);
            if (record == null) {
                StringBuffer buf = new StringBuffer("No score lookup (TV composite) found for: ");
                buf.append("\n\tgrade: " + grade);
                buf.append("\n\tnormGroup: " + normGroup);
                buf.append("\n\tnormYear: " + normYear);
                buf.append("\n\tsourceScoreType: " + sourceScoreType.getCode());
                buf.append("\n\tdestScoreType: " + destScoreType.getCode());
                buf.append("\n\tsourceScoreValue: " + sourceScoreValue);
                // TODO: is this allowed or disallowed? If allowed, remove the commented code, else
                // uncomment
                //throw new IllegalArgumentException(buf.toString());
                System.out.println(buf.toString());
                record = new ScoreLookupRecord();
                record.setGrade(grade);
                record.setNormGroup(normGroup);
                record.setSourceScoreTypeCode(sourceScoreType.getCode());
                record.setDestScoreTypeCode(destScoreType.getCode());
                record.setSourceScoreValue(sourceScoreValue);
            }
            return record;
        } catch (Exception e) {
            StringBuffer buf = new StringBuffer("Problem finding score lookup (TV composite) for: ");
                buf.append("\n\tgrade: " + grade);
                buf.append("\n\tnormGroup: " + normGroup);
                buf.append("\n\tnormYear: " + normYear);
                buf.append("\n\tsourceScoreType: " + sourceScoreType.getCode());
                buf.append("\n\tdestScoreType: " + destScoreType.getCode());
                buf.append("\n\tsourceScoreValue: " + sourceScoreValue);
                System.out.println(buf.toString());
            throw new RuntimeException(e);
        }
    }

    public BigDecimal getScore(String frameworkCode, String contentArea, String normGroup,
            String testForm, String testLevel, String grade, ScoreLookupCode sourceScoreType,
            BigDecimal sourceScoreValue, ScoreLookupCode destScoreType, Connection conn,
            String ageCategory) {
        final ScoreLookupRecord record = getScoreLookup(frameworkCode, contentArea, normGroup, testForm,
                testLevel, grade, sourceScoreType, sourceScoreValue, destScoreType, ageCategory,
                conn);

        if (record == null) {
            // Only report failure
            StringBuffer buf = new StringBuffer("Looking for score lookup value for: ");
            buf.append("\n\tparams: " + frameworkCode);
            buf.append(" | " + contentArea);
            buf.append(" | " + normGroup);
            buf.append(" | " + testForm);
            buf.append(" | " + testLevel);
            buf.append(" | " + grade);
            buf.append(" | " + sourceScoreType.getCode());
            buf.append(" | " + sourceScoreValue);
            buf.append(" | " + destScoreType.getCode());
            buf.append(" | " + ageCategory);
            buf.append("\n(continuing)\n");

            System.err.println(buf.toString());

            return null;
        }

        return record.getDestScoreValue();
    }

    public String getGradeEquivalent(String frameworkCode, String contentArea, String normGroup,
            String testForm, String testLevel, String grade, ScoreLookupCode sourceScoreType,
            BigDecimal sourceScoreValue, ScoreLookupCode destScoreType, Connection conn,
            String ageCategory) {
        final ScoreLookupRecord record = getScoreLookup(frameworkCode, contentArea, normGroup, testForm,
                testLevel, grade, sourceScoreType, sourceScoreValue, destScoreType, ageCategory,
                conn);

        return buildGradeEquivalent(record);
    }

    private BigDecimal getScore(final Long itemSetId, final String contentArea,
            final String normGroup, final String testForm, final String testLevel,
            final String grade, final ScoreLookupCode sourceScoreType,
            final BigDecimal sourceScoreValue, final ScoreLookupCode destScoreType,
            final String ageCategory, final Connection conn) {
        ScoreLookupRecord record = getScoreLookup(itemSetId, contentArea, normGroup, testForm,
                testLevel, grade, sourceScoreType, sourceScoreValue, destScoreType, ageCategory,
                conn);

        if (record == null) {
            // Only report failure
            StringBuffer buf = new StringBuffer("Looking for score lookup value for: ");
            buf.append("\n\tparams: " + itemSetId);
            buf.append(" | " + contentArea);
            buf.append(" | " + normGroup);
            buf.append(" | " + testForm);
            buf.append(" | " + testLevel);
            buf.append(" | " + grade);
            buf.append(" | " + sourceScoreType.getCode());
            buf.append(" | " + sourceScoreValue);
            buf.append(" | " + destScoreType.getCode());
            buf.append(" | " + ageCategory);
            buf.append("\n(continuing)\n");

            System.err.println(buf.toString());

            return null;
        }

        return record.getDestScoreValue();
    }

    private BigDecimal getScore(Long itemSetId, String normGroup, ScoreLookupCode sourceScoreType,
            BigDecimal sourceScoreValue, ScoreLookupCode destScoreType, String ageCategory,
            Connection conn) {
        ScoreLookupRecord record = getScoreLookup(itemSetId, normGroup, sourceScoreType,
                sourceScoreValue, destScoreType, ageCategory, conn);
        if (record == null) {
            // Only report failure
            StringBuffer buf = new StringBuffer("Looking for score lookup value for: ");
            buf.append("\n\tparams: " + itemSetId);
            buf.append(" | " + normGroup);
            buf.append(" | " + sourceScoreType.getCode());
            buf.append(" | " + sourceScoreValue);
            buf.append(" | " + destScoreType.getCode());
            buf.append(" | " + ageCategory);
            buf.append("\n(continuing)\n");

            System.err.println(buf.toString());

            return null;
        }
        return record.getDestScoreValue();
    }

    private ScoreLookupRecord getScoreLookup(Long itemSetId, String contentArea, String normGroup,
            String testForm, String testLevel, String grade, ScoreLookupCode sourceScoreType,
            BigDecimal sourceScoreValue, ScoreLookupCode destScoreType, String ageCategory,
            Connection conn) {
        try {
            ScoreLookupMapper mapper = new ScoreLookupMapper(conn);
            if (sourceScoreType.equals(ScoreLookupCode.SUBTEST_NUMBER_CORRECT)) {
                if (destScoreType.equals(ScoreLookupCode.OBJECTIVE_P_VALUE)) {
                    //P-Value scores have Zero as a source score value
                    return mapper.findScoreLookupByItemSetIdAndNumberCorrect(itemSetId.longValue(),
                            sourceScoreType.getCode(), destScoreType.getCode(),
                            new BigDecimal("0"), testForm, testLevel, grade, contentArea,
                            normGroup, ageCategory);

                } else { // not OBJECTIVE_P_VALUE
                    // grade and norm group are not applicable to number-correct-sourced derived
                    // scores (Scale and SEM)

                    // TODO: is this correct? NULL fields v. field not applicable
                    return mapper.findScoreLookupByItemSetIdAndNumberCorrect(itemSetId.longValue(),
                            sourceScoreType.getCode(), destScoreType.getCode(), sourceScoreValue,
                            testForm, testLevel, null, contentArea, null, ageCategory);
                }

            } else { // not SUBTEST_NUMBER_CORRECT
                return mapper.findScoreLookupByItemSetIdAndNumberCorrect(itemSetId.longValue(),
                        sourceScoreType.getCode(), destScoreType.getCode(), sourceScoreValue,
                        testForm, testLevel, grade, contentArea, normGroup, ageCategory);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ScoreLookupRecord getScoreLookup(Long itemSetId, String normGroup,
            ScoreLookupCode sourceScoreType, BigDecimal sourceScoreValue,
            ScoreLookupCode destScoreType, String ageCategory, Connection conn) {
        try {
            ScoreLookupMapper mapper = new ScoreLookupMapper(conn);
            return mapper.findScoreLookupByItemSetId(itemSetId, normGroup, sourceScoreType
                    .getCode(), destScoreType.getCode(), sourceScoreValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ScoreLookupRecord getScoreLookup(String frameworkCode, String contentArea,
            String normGroup, String testForm, String testLevel, String grade,
            ScoreLookupCode sourceScoreType, BigDecimal sourceScoreValue,
            ScoreLookupCode destScoreType, String ageCategory, Connection conn) {
        try {
            ScoreLookupMapper mapper = new ScoreLookupMapper(conn);
            return mapper.findScoreLookupByFrameworkCode(frameworkCode, sourceScoreType.getCode(),
                    destScoreType.getCode(), sourceScoreValue, testForm, testLevel, grade,
                    contentArea, normGroup, ageCategory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String buildGradeEquivalent(final ScoreLookupRecord record) {
        String gradeEquivalent = null;
        if (record != null) {
            BigDecimal destValue = record.getDestScoreValue();
            if (destValue != null) {
                gradeEquivalent = destValue.toString();
                String extendedFlag = record.getExtendedFlag();
                if (Stringx.hasContent(extendedFlag)) {
                    gradeEquivalent += extendedFlag;
                }
            }
        }
        return gradeEquivalent;
    }
}