package com.ctb.testSessionInfo.dto; 

//import com.ctb.bean.testAdmin.ActiveTest;
import com.ctb.bean.testAdmin.ActiveTest;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import java.util.Date;
import com.ctb.testSessionInfo.utils.FileSizeUtils;

public class ActiveTestVO implements java.io.Serializable
{     
    static final long serialVersionUID = 1L;
    private Integer contentSize = null;
    private Integer itemSetId = null;
    private String itemSetName = null;
    private Date loginStartDate = null;
    private Date loginEndDate = null;
    private Double displayContentBytes = null;
    private Integer[] tdItemSetIds = null;
    private String itemSetLevel = null;
    private String itemSetGrade = null;
    
    public ActiveTestVO() 
    {
    }    
    public ActiveTestVO(ActiveTest at) 
    {
        this.contentSize = at.getContentSize();
        this.itemSetId = at.getItemSetId();
        this.itemSetName = at.getItemSetName();
        this.loginStartDate = at.getLoginStartDate();
        this.loginEndDate = at.getLoginEndDate();
        this.displayContentBytes = FileSizeUtils.convertBytesToDisplayMBs(this.contentSize);
        this.tdItemSetIds = createTdItemSetIds(at.getSubtests());
        this.itemSetLevel = at.getItemSetLevel();
        this.itemSetGrade = at.getItemSetGrade();
    }     
    private Integer[] createTdItemSetIds(TestElement[] testElements){
        Integer [] result = new Integer[testElements.length];
        for(int i=0; i<testElements.length; i++){
            TestElement testElement = testElements[i];
            result[i] = testElement.getItemSetId();
        }
        return result;
    }
    public Integer[] getTdItemSetIds(){
        return this.tdItemSetIds;
    }
    public void setTdItemSetIds(Integer[] tdItemSetIds){
        this.tdItemSetIds = tdItemSetIds;
    }
    public Double getDisplayContentBytes(){
        return this.displayContentBytes;
    }
    public String getDisplayContentSize(){
        return this.displayContentBytes.toString() + " MB";
    }
    public Integer getContentSize() {
        return this.contentSize;
    }
    public void setContentSize(Integer contentSize) {
        this.contentSize = contentSize;
    }
    public Integer getItemSetId() {
        return this.itemSetId;
    }
    public void setItemSetId(Integer itemSetId) {
        this.itemSetId = itemSetId;
    }
    public String getItemSetName() {
        return this.itemSetName;
    }
    public void setItemSetName(String itemSetName) {
        this.itemSetName = itemSetName;
    }
    public Date getLoginEndDate() {
        return this.loginEndDate;
    }
    public void setLoginEndDate(Date loginEndDate) {
        this.loginEndDate = loginEndDate;
    }
    public Date getLoginStartDate() {
        return this.loginStartDate;
    }
    public void setLoginStartDate(Date loginStartDate) {
        this.loginStartDate = loginStartDate;
    }
    public String getItemSetLevel() {
        return this.itemSetLevel != null ? this.itemSetLevel : "--";
    }
    public void setItemSetLevel(String itemSetLevel) {
        this.itemSetLevel = itemSetLevel;
    }
    public String getItemSetGrade() {
        return this.itemSetGrade != null ? this.itemSetGrade : "--";
    }
    public void setItemSetGrade(String itemSetGrade) {
        this.itemSetGrade = itemSetGrade;
    }
} 