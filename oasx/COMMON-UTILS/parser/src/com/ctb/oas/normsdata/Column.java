package com.ctb.oas.normsdata;


/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class Column {
    private String name;
    public static final Column SCORE_LOOKUP_ID = new Column("ScoreLookupId");
    public static final Column FRAMEWORK_CODE = new Column("FrameworkCode");
    public static final Column PRODUCT = new Column("Product");
    public static final Column CONTENT_AREA = new Column("ContentArea");
    public static final Column FORM = new Column("ContentArea");
    public static final Column LEVEL = new Column("Level");
    public static final Column GRADE = new Column("Grade");
    public static final Column NORMS_GROUP = new Column("NormsGroup");
    public static final Column NORMS_YEAR = new Column("NormsYear");
    public static final Column SOURCE_SCORE_TYPE = new Column("SourceScoreType");
    public static final Column DEST_SCORE_TYPE = new Column("DestScoreType");
    public static final Column SOURCE_SCORE_VALUE = new Column("SourceScoreValue");
    public static final Column DEST_SCORE_VALUE = new Column("DestScoreValue");
    public static final Column AGE_CATEGORY = new Column("AgeCategory");
    public static final Column EXTENDED_FLAG = new Column("ExtendedFlag");

    public static final Column[] ALL = new Column[]{SCORE_LOOKUP_ID, FRAMEWORK_CODE, PRODUCT, CONTENT_AREA, FORM, LEVEL, GRADE,
                                                    NORMS_GROUP, NORMS_YEAR, AGE_CATEGORY, EXTENDED_FLAG, SOURCE_SCORE_TYPE, DEST_SCORE_TYPE,
                                                    SOURCE_SCORE_VALUE, DEST_SCORE_VALUE};

    private Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}