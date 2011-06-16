package com.ctb.oas.normsdata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class NormsData {
    private String frameworkCode;
    private String objective;
    private String form;
    private String level;
    private String normsGroup;
    private String normsYear;
    private String grade;
    private String product;
    private Map contentAreaScores = new HashMap();
    private ScoreType sourceScoreType;
    private ScoreType destScoreType;

    public String getFrameworkCode() {
        return frameworkCode;
    }

    public void setFrameworkCode(String frameworkCode) {
        this.frameworkCode = frameworkCode;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNormsYear() {
        return normsYear;
    }

    public void setNormsYear(String normsYear) {
        this.normsYear = normsYear;
    }

    public String getNormsGroup() {
        return normsGroup;
    }

    public void setNormsGroup(String normsGroup) {
        this.normsGroup = normsGroup;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Map getContentAreaScores() {
        return contentAreaScores;
    }

    public void setContentAreaScores(Map contentAreaScores) {
        this.contentAreaScores = contentAreaScores;
    }

    public ScoreType getDestScoreType() {
        return destScoreType;
    }

    public void setDestScoreType(ScoreType destScoreType) {
        this.destScoreType = destScoreType;
    }

    public ScoreType getSourceScoreType() {
        return sourceScoreType;
    }

    public void setSourceScoreType(ScoreType sourceScoreType) {
        this.sourceScoreType = sourceScoreType;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }
}
