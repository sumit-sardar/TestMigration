package com.ctb.testSessionInfo.dto; 

import java.util.List;

public class ProgramStatusReportVO implements java.io.Serializable 
{
    static final long serialVersionUID = 1L;

    private String customer = null;
    private String program = null;
    private String organization = null;
    private String test = null;
    private String subtest = null;
    private String subtestStatus = null;
    private String sessionNameFilter = null;
    private String sessionNameValue = null;
    private String sessionNumberFilter = null;
    private String sessionNumberValue = null;
    private String loginIdFilter = null;
    private String loginIdValue = null;
    private String passwordFilter = null;
    private String passwordValue = null;
    private String accessCodeFilter = null;
    private String accessCodeValue = null;
    private List subtestStatusList = null;
    
    public ProgramStatusReportVO()
    {
    }
    public ProgramStatusReportVO(String customer,
                                 String program,
                                 String organization,
                                 String test,
                                 String subtest,
                                 String subtestStatus,
                                 String sessionNameFilter,
                                 String sessionNameValue,
                                 String sessionNumberFilter,
                                 String sessionNumberValue,
                                 String loginIdFilter,
                                 String loginIdValue,
                                 String passwordFilter,
                                 String passwordValue,
                                 String accessCodeFilter,
                                 String accessCodeValue,
                                 List subtestStatusList)
    {
        this.customer = customer;
        this.program = program;
        this.organization = organization;
        this.test = test;
        this.subtest = subtest;
        this.subtestStatus = subtestStatus;
        this.sessionNameFilter = sessionNameFilter;
        this.sessionNameValue = sessionNameValue;
        this.sessionNumberFilter = sessionNumberFilter;
        this.sessionNumberValue = sessionNumberValue;
        this.loginIdFilter = loginIdFilter;
        this.loginIdValue = loginIdValue;
        this.passwordFilter = passwordFilter;
        this.passwordValue = passwordValue;
        this.accessCodeFilter = accessCodeFilter;
        this.accessCodeValue = accessCodeValue;
        this.subtestStatusList = subtestStatusList;
    }
    public String getCustomer() {
        return this.customer;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public String getProgram() {
        return this.program;
    }
    public void setProgram(String program) {
        this.program = program;
    }
    public String getOrganization() {
        return this.organization;
    }
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    public String getTest() {
        return this.test;
    }
    public void setTest(String test) {
        this.test = test;
    }
    public String getSubtest() {
        return this.subtest;
    }
    public void setSubtest(String subtest) {
        this.subtest = subtest;
    }
    public String getSubtestStatus() {
        return this.subtestStatus;
    }
    public void setSubtestStatus(String subtestStatus) {
        this.subtestStatus = subtestStatus;
    }
    public String getSessionNameFilter() {
        return this.sessionNameFilter;
    }
    public void setSessionNameFilter(String sessionNameFilter) {
        this.sessionNameFilter = sessionNameFilter;
    }

    public String getSessionNameValue() {
        return this.sessionNameValue;
    }
    public void setSessionNameValue(String sessionNameValue) {
        this.sessionNameValue = sessionNameValue;
    }
    public String getSessionNumberFilter() {
        return this.sessionNumberFilter;
    }
    public void setSessionNumberFilter(String sessionNumberFilter) {
        this.sessionNumberFilter = sessionNumberFilter;
    }
    public String getSessionNumberValue() {
        return this.sessionNumberValue;
    }
    public void setSessionNumberValue(String sessionNumberValue) {
        this.sessionNumberValue = sessionNumberValue;
    }
    public String getLoginIdFilter() {
        return this.loginIdFilter;
    }
    public void setLoginIdFilter(String loginIdFilter) {
        this.loginIdFilter = loginIdFilter;
    }
    public String getLoginIdValue() {
        return this.loginIdValue;
    }
    public void setLoginIdValue(String loginIdValue) {
        this.loginIdValue = loginIdValue;
    }
    public String getPasswordFilter() {
        return this.passwordFilter;
    }
    public void setPasswordFilter(String passwordFilter) {
        this.passwordFilter = passwordFilter;
    }
    public String getPasswordValue() {
        return this.passwordValue;
    }
    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }
    public String getAccessCodeValue() {
        return this.accessCodeValue;
    }
    public void setAccessCodeValue(String accessCodeValue) {
        this.accessCodeValue = accessCodeValue;
    }
    public String getAccessCodeFilter() {
        return this.accessCodeFilter;
    }
    public void setAccessCodeFilter(String accessCodeFilter) {
        this.accessCodeFilter = accessCodeFilter;
    }
    public List getSubtestStatusList() {
        return this.subtestStatusList;
    }
    public void setSubtestStatusList(List subtestStatusList) {
        this.subtestStatusList = subtestStatusList;
    }

} 
