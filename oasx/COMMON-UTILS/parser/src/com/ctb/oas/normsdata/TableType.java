package com.ctb.oas.normsdata;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TableType {
    private String type;
    private static Properties properties;
    public static final TableType NCToSS = new TableType("NCToSS", ScoreType.SCALE_SCORE);
    public static final TableType NCToSEM = new TableType("NCToSEM", ScoreType.STANDARD_ERROR_OF_MEASUREMENT);
    public static final TableType SSToXGED = new TableType("SSToXGED", ScoreType.EXTENDED_GRADE_EQUIVALENT);
    public static final TableType SSToGED = new TableType("SSToGED", ScoreType.GRADE_EQUIVALENT);
    public static final TableType SSToMGED = new TableType("SSToMGED", ScoreType.MEAN_GRADE_EQUIVALENT);
    public static final TableType SSToNCE = new TableType("SSToNCE", ScoreType.NORMAL_CURVE_EQUIVALENT);
    public static final TableType SSToNP = new TableType("SSToNP", ScoreType.NATIONAL_PERCENTILE);
    
    public static final TableType SSToPL = new TableType("SSToPL", ScoreType.PERFORMANCE);
    public static final TableType SSToRNP = new TableType("SSToRNP", ScoreType.REFERENCIAL_NATIONAL_PERCENTILE);
    public static final TableType SSToRNC = new TableType("SSToRNC", ScoreType.REFERENCIAL_NATIONAL_CURVE);

    public static final TableType[] TERRANOVA_TYPES = new TableType[]{NCToSS, NCToSEM, SSToXGED, SSToGED, SSToMGED, SSToNCE, SSToNP};
    public static final TableType[] LASLINKS_TYPES  = new TableType[]{NCToSS, NCToSEM, SSToXGED, SSToGED, SSToMGED, SSToNCE, SSToNP, SSToPL, SSToRNP, SSToRNC};
    private ScoreType targetScoreType;

    private TableType(String type, ScoreType target) {
        try {
            if (properties == null) {
                properties = new Properties();
                properties.load(new FileInputStream("conf/table_type.properties"));
            }
            String value = (String) properties.get(type);
            if (value != null)
                this.type = value.trim();
            else
                this.type = type;

            this.targetScoreType = target;
        }
        catch (IOException e) {
            throw new RuntimeException("cannot load table types from properties", e);
        }

    }

    protected String getTypeString() {
        return type;
    }

    protected static TableType getTableType(String headerLine) {
        for (int i = 0; i < TERRANOVA_TYPES.length; i++) {
            TableType tableType = TERRANOVA_TYPES[i];
            if (headerLine.indexOf(tableType.getTypeString()) != -1)
                return tableType;
        }
        return null;
    }
    
    protected static TableType getLasLinksTableType(String headerLine) {
        for (int i = 0; i < LASLINKS_TYPES.length; i++) {
            TableType tableType = LASLINKS_TYPES[i];
            if (headerLine.indexOf(tableType.getTypeString()) != -1)
                return tableType;
        }
        return null;
    }

    protected ScoreType getDestScoreType() {
        return targetScoreType;
    }

}
