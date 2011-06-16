package com.ctb.oas.normsdata;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScoreType {
    private static Properties tn_properties = new Properties();
    private static Properties tb_properties = new Properties();
    private String type;
    public static final ScoreType SCALE_SCORE = new ScoreType("ScaleScore");
    public static final ScoreType NUMBER_CORRECT = new ScoreType("NumberCorrect");
    public static final ScoreType STANDARD_ERROR_OF_MEASUREMENT = new ScoreType("SEM");
    public static final ScoreType GRADE_EQUIVALENT = new ScoreType("GED");
    public static final ScoreType MEAN_GRADE_EQUIVALENT = new ScoreType("GME");
    public static final ScoreType EXTENDED_GRADE_EQUIVALENT = new ScoreType("ExtendedGED");
    public static final ScoreType NORMAL_CURVE_EQUIVALENT = new ScoreType("NCE");
    public static final ScoreType NATIONAL_PERCENTILE = new ScoreType(("NP"));
    public static final ScoreType ABE_ALL_P = new ScoreType(("ABE_ALL_P"));
    public static final ScoreType ABE_ALL_NCE = new ScoreType(("ABE_ALL_NCE"));
    public static final ScoreType ABE_ALL_S = new ScoreType(("ABE_ALL_S"));
    public static final ScoreType ABE_J_P = new ScoreType(("ABE_J_P"));
    public static final ScoreType ABE_J_NCE = new ScoreType(("ABE_J_NCE"));
    public static final ScoreType ABE_J_S = new ScoreType(("ABE_J_S"));
    public static final ScoreType EGEDMA = new ScoreType("EGEDMA");
    public static final ScoreType EGEDWR = new ScoreType("EGEDWR");
    public static final ScoreType EGEDRD = new ScoreType("EGEDRD");
    public static final ScoreType EGEDSC = new ScoreType("EGEDSC");
    public static final ScoreType EGEDSO = new ScoreType("EGEDSO");
    public static final ScoreType EGEDAV = new ScoreType("EGEDAV");
    public static final ScoreType PVALUE = new ScoreType("PValue");
    public static final ScoreType PERFORMANCE = new ScoreType(("P"));
    public static final ScoreType REFERENCIAL_NATIONAL_PERCENTILE = new ScoreType(("RNP"));
    public static final ScoreType REFERENCIAL_NATIONAL_CURVE = new ScoreType(("RNC"));
    public static final ScoreType PERFORMANCE_LEVEL = new ScoreType("PL");;

    public static final ScoreType[] TABE_SCORE_TYPES = new ScoreType[]{SCALE_SCORE, NUMBER_CORRECT, STANDARD_ERROR_OF_MEASUREMENT,
                                                                       GRADE_EQUIVALENT, ABE_ALL_P, ABE_ALL_NCE, ABE_ALL_S, ABE_J_P,
                                                                       ABE_J_NCE, ABE_J_S, EGEDMA, EGEDWR, EGEDRD, EGEDSC, EGEDSO, EGEDAV};

    public static final List TABEAdultScoreTypeList = Arrays.asList(new ScoreType[]{ABE_ALL_P, ABE_ALL_NCE, ABE_ALL_S});
    public static final List TABEJuvenileScoreTypeList = Arrays.asList(new ScoreType[]{ABE_J_P, ABE_J_NCE, ABE_J_S});
    public static final ScoreType[] TERRANOVA_TYPES = new ScoreType[]{SCALE_SCORE, STANDARD_ERROR_OF_MEASUREMENT, GRADE_EQUIVALENT,
                                                                      MEAN_GRADE_EQUIVALENT, EXTENDED_GRADE_EQUIVALENT, NORMAL_CURVE_EQUIVALENT,
                                                                      NATIONAL_PERCENTILE};
    public static final ScoreType[] LASLINKS_TYPES = new ScoreType[]{SCALE_SCORE, STANDARD_ERROR_OF_MEASUREMENT, GRADE_EQUIVALENT,
        MEAN_GRADE_EQUIVALENT, EXTENDED_GRADE_EQUIVALENT, NORMAL_CURVE_EQUIVALENT,
        NATIONAL_PERCENTILE, PERFORMANCE, REFERENCIAL_NATIONAL_PERCENTILE, REFERENCIAL_NATIONAL_CURVE, PERFORMANCE_LEVEL};

    static {
        try {
            tn_properties.load(new FileInputStream("conf/score_type.properties"));
            tb_properties.load(new FileInputStream("conf/tabe_score_type.properties"));
        }
        catch (IOException e) {
            throw new RuntimeException("could not load score type properties");
        }
    }

    private ScoreType(String type) {
        this.type = type;
    }

    protected String getTypeString() {
        return type;
    }

    public String getSQLValue() {
        final String value = (String) tn_properties.get(type);
        if (value == null || value.trim().length() == 0)
            throw new RuntimeException("SQL Value for Score Type " + type + "cannot be found ");

        return value;

    }

    public static ScoreType getScoreTypeForTABE(String id) {
        for (int i = 0; i < TABE_SCORE_TYPES.length; i++) {
            ScoreType tabeScoreType = TABE_SCORE_TYPES[i];
            final String expectedId = (String) tb_properties.get(tabeScoreType.getTypeString());
            if (expectedId != null && expectedId.trim().equalsIgnoreCase(id))
                return tabeScoreType;
        }
        return null;
    }
}
