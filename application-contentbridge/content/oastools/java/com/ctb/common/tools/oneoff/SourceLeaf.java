package com.ctb.common.tools.oneoff;



import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;


/**
 * User: mwshort
 * Date: Dec 24, 2003
 * Time: 10:47:57 AM
 * 
 *
 */
public class SourceLeaf {

    String thinkColumn;
    String objectiveTitle;
    String subskillTitle;
    String thinkTitle;
    String contentTitle;
    public SourceLeaf(String[] columns) {
        thinkColumn= columns[0];
        objectiveTitle = columns[1];
        subskillTitle = columns[2];
        thinkTitle = columns[3];
        contentTitle = columns[4];
    }

    public String getContentTitle() {
        return contentTitle;
    }
    public String getThinkColumn() {
        return thinkColumn;
    }

    public String getObjectiveID() {
        return getObjectiveIDFromThinkColumn(thinkColumn);
    }

    public String getSubskillID() {
        return getSubskillIDFromThinkColumn(thinkColumn);
    }

    public String getObjectiveTitle() {
        return objectiveTitle;
    }

    public void setObjectiveTitle(String objectiveTitle) {
        this.objectiveTitle = objectiveTitle;
    }

    public String getSubskillTitle() {
        return subskillTitle;
    }

    public void setSubskillTitle(String subskillTitle) {
        this.subskillTitle = subskillTitle;
    }

    public String getThinkTitle() {
        return thinkTitle;
    }

    public void setThinkTitle(String thinkTitle) {
        this.thinkTitle = thinkTitle;
    }

    public String getFrameworkCode() {
         return "TERRA";
        //return getFrameworkFromThinkColumn(thinkColumn);
    }

    public String getContentArea() {
        return getContentAreaFromThinkColumn(thinkColumn);
    }

    public String getGrade() {
        return getGradeFromThinkColumn(thinkColumn);
    }

    public String getThinkID() {
        return getThinkIDfromThinkColumn(thinkColumn);
    }
    private List getTokenList(String thinkColumn) {
        StringTokenizer tokenizer = new StringTokenizer(thinkColumn,".");
        List tokenList = new ArrayList();
        while(tokenizer.hasMoreTokens()) {
            tokenList.add(tokenizer.nextToken());

        }
        return tokenList;
    }

    private String getContentAreaFromThinkColumn(String thinkColumn) {
        List tokens = getTokenList(thinkColumn);
        return getGradeFromThinkColumn(thinkColumn) + (String)tokens.get(2);
    }
    private String getFrameworkFromThinkColumn(String thinkColumn) {
        List tokens = getTokenList(thinkColumn);
        return (String)tokens.get(0);
    }

    private String getGradeFromThinkColumn(String thinkColumn) {
        List tokens = getTokenList(thinkColumn);
        return (String)tokens.get(1);
    }

    private String getObjectiveIDFromThinkColumn(String thinkColumn) {
        List tokens = getTokenList(thinkColumn);
        return getContentAreaFromThinkColumn(thinkColumn)
                + "." + (String)tokens.get(3);
    }

    private String getSubskillIDFromThinkColumn(String thinkColumn) {
        List tokens = getTokenList(thinkColumn);
        return getObjectiveID() + "." + (String)tokens.get(4);
    }
    private String getThinkIDfromThinkColumn(String thinkColumn) {
        List tokens = getTokenList(thinkColumn);
        return getSubskillIDFromThinkColumn(thinkColumn) + "." + (String)tokens.get(5);
    }

    public String toString() {
        return thinkColumn;
    }
}
