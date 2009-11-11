package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsGradeDimData implements Persistent {

    private Long gradeid;
    private String grade;
    public Long getGradeid() {
        return gradeid;
    }

    public void setGradeid(Long gradeid) {
        this.gradeid = gradeid;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
