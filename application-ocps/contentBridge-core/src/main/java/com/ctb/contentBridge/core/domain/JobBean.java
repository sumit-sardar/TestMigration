/**
 * 
 */
package com.ctb.contentBridge.core.domain;
import java.sql.Date;
import java.util.Map;


/**
 * @author 
 *
 */
public class JobBean {
	private Long jobPk;
	private String jobID;
    private String jobName;
    private Date jobDateFrom;
    private Date jobDateTo;    
    private String jobRunStatus;
    private String targetEnv;
    private String command;
    private String strJobDateFrom;
    private String strJobDateTo;
    private Map	parameters;
    public boolean valid;
    
    public Long getJobPk() {
		return jobPk;
	}
	public void setJobPk(Long jobPk) {
		this.jobPk = jobPk;
	}
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}	
	
	public Date getJobDateFrom() {
		return jobDateFrom;
	}
	public void setJobDateFrom(Date jobDateFrom) {
		this.jobDateFrom = jobDateFrom;
	}
	public Date getJobDateTo() {
		return jobDateTo;
	}
	public void setJobDateTo(Date jobDateTo) {
		this.jobDateTo = jobDateTo;
	}
	public String getJobRunStatus() {
		return jobRunStatus;
	}
	public void setJobRunStatus(String jobRunStatus) {
		this.jobRunStatus = jobRunStatus;
	}
	
	public String getTargetEnv() {
		return targetEnv;
	}
	public void setTargetEnv(String targetEnv) {
		this.targetEnv = targetEnv;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Map getParameters() {
		return parameters;
	}
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getStrJobDateFrom() {
		return strJobDateFrom;
	}
	public void setStrJobDateFrom(String strJobDateFrom) {
		this.strJobDateFrom = strJobDateFrom;
	}
	public String getStrJobDateTo() {
		return strJobDateTo;
	}
	public void setStrJobDateTo(String strJobDateTo) {
		this.strJobDateTo = strJobDateTo;
	}
}
