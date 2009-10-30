package data; 

public class Condition implements java.io.Serializable  
{ 
    static final long serialVersionUID = 1L;

    
    private Boolean hasBreak = Boolean.FALSE;
    private Boolean offGradeTestingDisabled = Boolean.FALSE;
    private Boolean showStudentFeedback = Boolean.FALSE;
    private Boolean showCancelOnFirstPage = Boolean.FALSE; 
    private Boolean hasStudentLoggedIn = Boolean.FALSE;
    private Boolean allStudentLoggedIn = Boolean.FALSE;
    private Boolean isCopyTest = Boolean.FALSE;
    private Boolean testSessionExpired = Boolean.FALSE;
    private Boolean reloadTestSession = Boolean.FALSE;
    private Boolean isSearchTestList = Boolean.FALSE;
    private Boolean selectTestChanged = Boolean.FALSE;

    public Condition() 
    {
        this.hasBreak = Boolean.FALSE;
        this.offGradeTestingDisabled = Boolean.FALSE;
        this.showStudentFeedback = Boolean.FALSE;
        this.showCancelOnFirstPage = Boolean.FALSE; 
        this.hasStudentLoggedIn = Boolean.FALSE;
        this.allStudentLoggedIn = Boolean.FALSE;
        this.isCopyTest = Boolean.FALSE;
        this.testSessionExpired = Boolean.FALSE;
        this.reloadTestSession = Boolean.FALSE;
        this.isSearchTestList = Boolean.FALSE;
        this.selectTestChanged = Boolean.FALSE;
    }    


    public Boolean getHasBreak() {
        return this.hasBreak;
    }
    public void setHasBreak(Boolean hasBreak) {
        this.hasBreak = hasBreak;
    }

    public Boolean getOffGradeTestingDisabled() {
        return this.offGradeTestingDisabled;
    }
    public void setOffGradeTestingDisabled(Boolean offGradeTestingDisabled) {
        this.offGradeTestingDisabled = offGradeTestingDisabled;
    }

    public Boolean getShowStudentFeedback() {
        return this.showStudentFeedback;
    }
    public void setShowStudentFeedback(Boolean showStudentFeedback) {
        this.showStudentFeedback = showStudentFeedback;
    }

    public Boolean getShowCancelOnFirstPage() {
        return this.showCancelOnFirstPage;
    }
    public void setShowCancelOnFirstPage(Boolean showCancelOnFirstPage) {
        this.showCancelOnFirstPage = showCancelOnFirstPage;
    }

    public Boolean getHasStudentLoggedIn() {
        return this.hasStudentLoggedIn;
    }
    public void setHasStudentLoggedIn(Boolean hasStudentLoggedIn) {
        this.hasStudentLoggedIn = hasStudentLoggedIn;
    }

    public Boolean getAllStudentLoggedIn() {
        return this.allStudentLoggedIn;
    }
    public void setAllStudentLoggedIn(Boolean allStudentLoggedIn) {
        this.allStudentLoggedIn = allStudentLoggedIn;
    }

    public Boolean getIsCopyTest() {
        return this.isCopyTest;
    }
    public void setIsCopyTest(Boolean isCopyTest) {
        this.isCopyTest = isCopyTest;
    }

    public Boolean getTestSessionExpired() {
        return this.testSessionExpired;
    }
    public void setTestSessionExpired(Boolean testSessionExpired) {
        this.testSessionExpired = testSessionExpired;
    }

    public Boolean getReloadTestSession() {
        return this.reloadTestSession;
    }
    public void setReloadTestSession(Boolean reloadTestSession) {
        this.reloadTestSession = reloadTestSession;
    }

    public Boolean getIsSearchTestList() {
        return this.isSearchTestList;
    }
    public void setIsSearchTestList(Boolean isSearchTestList) {
        this.isSearchTestList = isSearchTestList;
    }

    public Boolean getSelectTestChanged() {
        return this.selectTestChanged;
    }
    public void setSelectTestChanged(Boolean selectTestChanged) {
        this.selectTestChanged = selectTestChanged;
    }
} 
