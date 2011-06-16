/*
 * Created on Sep 1, 2004
 *
 */
package com.ctb.oas.normsdata.parser;

/**
 * @author arathore
 */
public class PValueDetails {
    private String form;
    private String level;
    private String contentArea;
    private String grade;
    private String objective;
    private float fallPValue;
    private float winterPValue;
    private float springPValue;
    private int count = 1;


    public PValueDetails(String[] tokens) {
        form = tokens[0];
        level = tokens[1];
        contentArea = tokens[5];
        fallPValue = new Float(tokens[8]).floatValue();
        winterPValue = new Float(tokens[9]).floatValue();
        springPValue = new Float(tokens[10]).floatValue();
        grade = tokens[2];
        objective = tokens[7].substring(0, tokens[7].lastIndexOf('.'));
    }

    public void updateAverages(PValueDetails newDetails) {
        incrementCount();
        setFallPValue(newAverage(fallPValue, newDetails.fallPValue, count));
        setWinterPValue(newAverage(winterPValue, newDetails.winterPValue, count));
        setSpringPValue(newAverage(springPValue, newDetails.springPValue, count));
    }

    private float newAverage(float oldAverage, float newValue, int newDivisor) {
        return (oldAverage * (newDivisor - 1) + newValue) / newDivisor;
    }

    public String toString() {
        return "[ " +
                " FORM:" + form + " " +
                " LEVEL:" + level + " " +
                " cAREA:" + contentArea + " " +
                " F,W,S:" + fallPValue + "," + winterPValue + "," + springPValue +
                " count:" + count +
                " ]";
    }

    public String getContentArea() {
        return contentArea;
    }

    public void setContentArea(String contentArea) {
        this.contentArea = contentArea;
    }

    public float getFallPValue() {
        return fallPValue;
    }

    public void setFallPValue(float fallPValue) {
        this.fallPValue = fallPValue;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public float getSpringPValue() {
        return springPValue;
    }

    public void setSpringPValue(float springPValue) {
        this.springPValue = springPValue;
    }

    public float getWinterPValue() {
        return winterPValue;
    }

    public void setWinterPValue(float winterPValue) {
        this.winterPValue = winterPValue;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        this.count++;
    }
}
