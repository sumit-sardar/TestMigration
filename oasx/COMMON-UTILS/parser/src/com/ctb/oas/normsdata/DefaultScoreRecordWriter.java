package com.ctb.oas.normsdata;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class DefaultScoreRecordWriter implements ScoreRecordWriter {
    public void writeScoreRecord(Writer writer, NormsData data) {
        Map contentAreas = data.getContentAreaScores();
        Iterator iterator = contentAreas.keySet().iterator();
        while (iterator.hasNext()) {
            String contentAreaString = (String) iterator.next();
            ContentAreaScore contentAreaScore = (ContentAreaScore) contentAreas.get(contentAreaString);
            writeScoreRecord(writer, data, contentAreaScore);
        }
    }

    public void writeScoreRecord(Writer writer, NormsData data, ContentAreaScore contentAreaScore) {
        int sourceScoreStartValue;

        if (data.getSourceScoreType().equals(ScoreType.NUMBER_CORRECT))
            sourceScoreStartValue = 0;
        else
            sourceScoreStartValue = contentAreaScore.getSourceScoreStartIndex();

        List targetScores = contentAreaScore.getTargetScores();

        for (int i = 0; i < targetScores.size(); i++) {
        	if(data.getGrade()!=null && data.getGrade().endsWith("*")){
        		data.setGrade("K");
        		writeScoreRecord(writer, data, contentAreaScore.getContentAreaString(), new Integer(sourceScoreStartValue), targetScores.get(i));
        		for(int k=1; k<=12; k++) {
        			data.setGrade(""+k);
        			writeScoreRecord(writer, data, contentAreaScore.getContentAreaString(), new Integer(sourceScoreStartValue), targetScores.get(i));	
        		}
        		data.setGrade("*");
        	} else {
        		writeScoreRecord(writer, data, contentAreaScore.getContentAreaString(), new Integer(sourceScoreStartValue), targetScores.get(i));
               
        	}  
        	sourceScoreStartValue++;
        }
    }

    public void writeScoreRecord(Writer writer, NormsData data, String contentArea, Object sourceScore, Object targetScore) {
        ScoreRecord scoreRecord = new ScoreRecord();
        setNonScoreValues(scoreRecord, contentArea, data);
        setScoreValues(scoreRecord, data, sourceScore, targetScore);
        try {
            writer.write(scoreRecord.toString() + "\n");
        }
        catch (IOException e) {
            throw new RuntimeException("cannot write to file", e);

        }
    }

    private void setScoreValues(ScoreRecord scoreRecord, NormsData normsData, Object sourceScore, Object targetScore) {
        TargetScoreFilter filter = TargetScoreFilterFactory.getFilter(normsData.getDestScoreType());

        if (scoreIsExtended(normsData, targetScore))
            scoreRecord.putValue(Column.EXTENDED_FLAG, ScoreRecord.EXTENDED_FLAG_VALUE);

        if (normsData.getSourceScoreType() != null) // source score type can be null for PValue
            scoreRecord.putValue(Column.SOURCE_SCORE_TYPE, normsData.getSourceScoreType().getSQLValue());
        
        if(normsData.getDestScoreType().getTypeString().equalsIgnoreCase(ScoreType.PERFORMANCE_LEVEL.getTypeString())){
        	Integer updatedSourceScore = new Integer(((Integer) sourceScore).intValue()+1);
        
        	scoreRecord.putValue(Column.SOURCE_SCORE_VALUE, filter.filterScore(targetScore));
        	scoreRecord.putValue(Column.DEST_SCORE_VALUE, updatedSourceScore);
        }
        	
        else {
        	scoreRecord.putValue(Column.SOURCE_SCORE_VALUE, sourceScore);
        	scoreRecord.putValue(Column.DEST_SCORE_VALUE, filter.filterScore(targetScore));
        }

        scoreRecord.putValue(Column.DEST_SCORE_TYPE, normsData.getDestScoreType().getSQLValue());

        //set age category for nce, ns and np tabe scores
        String ageCategory = null;
        if ((normsData.getDestScoreType() == ScoreType.ABE_J_NCE) ||
                (normsData.getDestScoreType() == ScoreType.ABE_J_P) ||
                (normsData.getDestScoreType() == ScoreType.ABE_J_S))
            ageCategory = ScoreRecord.TABE_JUVENILE_CODE;
        else if ((normsData.getDestScoreType() == ScoreType.ABE_ALL_NCE) ||
                (normsData.getDestScoreType() == ScoreType.ABE_ALL_P) ||
                (normsData.getDestScoreType() == ScoreType.ABE_ALL_S)
        )
            ageCategory = ScoreRecord.TABE_ADULT_CODE;

        scoreRecord.putValue(Column.AGE_CATEGORY, ageCategory);
    }

    private boolean scoreIsExtended(NormsData normsData, Object targetScore) {
        if (normsData.getDestScoreType() == ScoreType.GRADE_EQUIVALENT || normsData.getDestScoreType() == ScoreType.MEAN_GRADE_EQUIVALENT) {
            String targetScoreString = (String) targetScore;
            return (targetScoreString.indexOf("+") != -1);
        }

        return false;
    }

    private void setNonScoreValues(ScoreRecord scoreRecord, String contentArea, NormsData data) {
        scoreRecord.putValue(Column.SCORE_LOOKUP_ID, ScoreIDGenerator.INSTANCE.generateId(data, contentArea));
        scoreRecord.putValue(Column.FRAMEWORK_CODE, data.getFrameworkCode());
        scoreRecord.putValue(Column.PRODUCT, data.getProduct());
        scoreRecord.putValue(Column.CONTENT_AREA, contentArea);
        scoreRecord.putValue(Column.FORM, data.getForm());
        scoreRecord.putValue(Column.LEVEL, data.getLevel());
        scoreRecord.putValue(Column.GRADE, data.getGrade());
        scoreRecord.putValue(Column.NORMS_GROUP, data.getNormsGroup());
        scoreRecord.putValue(Column.NORMS_YEAR, data.getNormsYear());
    }


}
