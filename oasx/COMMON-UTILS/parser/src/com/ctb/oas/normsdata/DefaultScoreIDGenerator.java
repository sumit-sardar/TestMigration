package com.ctb.oas.normsdata;

import java.util.StringTokenizer;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class DefaultScoreIDGenerator implements ScoreIDGenerator {
    private static final String DELIMITER = "_";
    private static final String SURVEY = "Survey";
    private static final String BATTERY = "Battery";

    public String generateId(NormsData data, String contentAreaName) {
        StringBuffer idBuffer = new StringBuffer();

        addToBuffer(data.getFrameworkCode(), idBuffer, true);
        addToBuffer(getProduct(data), idBuffer, true);
        if (data.getFrameworkCode().equals(ScoreRecord.TABE_FRAMEWORK_CODE))
            addTABEJuvenileOrAdultType(data.getDestScoreType(), idBuffer, true);
        addToBuffer(data.getNormsYear(), idBuffer, true);
        addToBuffer(data.getForm(), idBuffer, true);
        addToBuffer(data.getLevel(), idBuffer, true);
        addNormsGroup(data.getNormsGroup(), idBuffer, true);
        addToBuffer(data.getGrade(), idBuffer, true);
        addToBuffer(data.getObjective(), idBuffer, true);
        addContentArea(contentAreaName, idBuffer, false);

        return idBuffer.toString();
    }

    private void addTABEJuvenileOrAdultType(ScoreType type, StringBuffer idBuffer, boolean addDelimiter) {
        if (type == null)
            return;

        if (ScoreType.TABEAdultScoreTypeList.contains(type))
            addToBuffer("ADLT", idBuffer, addDelimiter);

        if (ScoreType.TABEJuvenileScoreTypeList.contains(type))
            addToBuffer("JVNL", idBuffer, addDelimiter);

    }

    private void appendDelimiter(StringBuffer idBuffer) {
        idBuffer.append(DELIMITER);
    }

    protected void addContentArea(String contentAreaName, StringBuffer idBuffer, boolean addDelimiter) {
        if (contentAreaName == null)
            throw new RuntimeException("Cannot generate Id, ContentArea Name is null");
        StringTokenizer tokenizer = new StringTokenizer(contentAreaName, " ");
        while (tokenizer.hasMoreElements()) {
            idBuffer.append(((String) tokenizer.nextElement()).substring(0, 2));
        }
        if (addDelimiter)
            appendDelimiter(idBuffer);
    }

    protected void addNormsGroup(String normsGroup, StringBuffer idBuffer, boolean addDelimiter) {
        if (normsGroup != null) {
            idBuffer.append(normsGroup.substring(0, 3));
            if (addDelimiter)
                appendDelimiter(idBuffer);
        }
    }

    protected void addToBuffer(String value, StringBuffer idBuffer, boolean addDelimiter) {
        if (value != null && value.trim().length() != 0) {
            idBuffer.append(value);
            if (addDelimiter)
                appendDelimiter(idBuffer);
        }
    }

    public String getProduct(NormsData data) {
        String product = data.getProduct();

        if (product == null || product.trim().length() == 0)
            return null;

        if (product.indexOf(BATTERY) != -1)
            return "CB";

        if (product.indexOf(SURVEY) != -1)
            return "SV";

        return null;
    }
}


