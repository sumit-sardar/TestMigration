package com.ctb.lexington.db.irsdata;
import java.util.Date;
import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsStudentDimData implements Persistent{

    private Long studentid;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date birthDate;
    private String reportStudentId;

    public Long getStudentid() {
        return studentid;
    }

    public void setStudentid(Long studentid) {
        this.studentid = studentid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
    
    public String getReportStudentId() {
        return reportStudentId;
    }

    public void setReportStudentId(String reportStudentId) {
        this.reportStudentId = reportStudentId;
    }
    
    public boolean equals(Object other) {
        return
            this.getStudentid().equals(((IrsStudentDimData) other).getStudentid()) &&
            (this.getFirstName() == null || this.getFirstName().equals(((IrsStudentDimData) other).getFirstName())) &&
            (this.getMiddleName() == null || this.getMiddleName().equals(((IrsStudentDimData) other).getMiddleName())) &&
            (this.getLastName().equals(((IrsStudentDimData) other).getLastName())) &&
            (this.getReportStudentId() == null || this.getReportStudentId().equals(((IrsStudentDimData) other).getReportStudentId())) &&
            (this.getBirthDate() == null || this.getBirthDate().equals(((IrsStudentDimData) other).getBirthDate()));
    }

    public int hashCode() {
        return (int) studentid.longValue();
    }
}
