package com.ctb.common.tools.oneoff.peid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PEIDExportRecord {
    private String root;
    private String rootName;

    private String projectName;
    private String projectId;
    private String moduleName;
    private String moduleId;
    private String levelId;
    private String pageNo;
    private String contentAreaTitle;
    private String contentId;
    private String itemNo;
    private String part;
    private String itemId;
    private String itemType;
    private String answerKey;
    private String rightsKey;
    private String noScorePoints;
    private String nac;
    private String itemsysDesc;
    private String desc;
    private String giResponse;
    private String objectiveId;
    private String objectiveCode;
    private String objectiveTitle;
    private String subskillId;
    private String subskillCode;
    private String subskillTitle;
    private String thinkId;
    private String thinkTitle;
    private String subtestId;
    private String subtestTitle;
    private String subtestCode;

    public PEIDExportRecord(String root, String rootName, List columns) {
        this.root = root;
        this.rootName = rootName;
        this.projectName = (String) columns.get(0);
        this.projectId = (String) columns.get(1);
        this.moduleName = (String) columns.get(2);
        this.moduleId = (String) columns.get(3);
        this.levelId = (String) columns.get(4);
        this.pageNo = (String) columns.get(5);
        this.contentAreaTitle = (String) columns.get(6);
        this.contentId = (String) columns.get(7);
        this.itemNo = (String) columns.get(8);
        this.part = (String) columns.get(9);
        this.itemId = (String) columns.get(10);
        this.itemType = (String) columns.get(11);
        this.answerKey = (String) columns.get(12);
        this.rightsKey = (String) columns.get(13);
        this.noScorePoints = (String) columns.get(14);
        this.nac = (String) columns.get(15);
        this.itemsysDesc = (String) columns.get(16);
        this.desc = (String) columns.get(17);
        this.giResponse = (String) columns.get(18);
        this.objectiveId = (String) columns.get(19);
        this.objectiveCode = (String) columns.get(20);
        this.objectiveTitle = (String) columns.get(21);
        this.subskillId = (String) columns.get(22);
        this.subskillCode = (String) columns.get(23);
        this.subskillTitle = (String) columns.get(24);
        this.thinkId = (String) columns.get(25);
        this.thinkTitle = (String) columns.get(26);
        this.subtestId = (String) columns.get(27);
        this.subtestTitle = (String) columns.get(28);
        this.subtestCode = (String) columns.get(29);
    }

    public String getItemId() {
        return root + "." + subtestCode + "."
                + StringUtils.substring(levelId, 0, 1) + "."
                + Integer.parseInt(objectiveCode) + "." + subskillId + "."
                + itemNo;
    }

    public PEIDObjective getMap() {
        return new PEIDObjective(root + "." + subtestCode + "."
                + StringUtils.substring(levelId, 0, 1) + "."
                + Integer.parseInt(objectiveCode) + "." + subskillId,
                subskillTitle);
    }

    public List getObjectiveHierarchy() {
        List curricula = new ArrayList();

        PEIDObjective rootCurr = new PEIDObjective(root, rootName);
        curricula.add(rootCurr);

        PEIDObjective contentArea = new PEIDObjective(root + "." + subtestCode,
                contentAreaTitle);
        curricula.add(contentArea);

        PEIDObjective level = new PEIDObjective(root + "." + subtestCode + "."
                + StringUtils.substring(levelId, 0, 1), StringUtils.substring(
                levelId, 0, 1));
        curricula.add(level);

        PEIDObjective objective = new PEIDObjective(root + "." + subtestCode
                + "." + StringUtils.substring(levelId, 0, 1) + "."
                + Integer.parseInt(objectiveCode), objectiveTitle);
        curricula.add(objective);

        PEIDObjective skill = new PEIDObjective(root + "." + subtestCode + "."
                + StringUtils.substring(levelId, 0, 1) + "."
                + Integer.parseInt(objectiveCode) + "." + subskillId,
                subskillTitle);
        curricula.add(skill);

        return curricula;
    }
    /**
     * @return Returns the answerKey.
     */
    public String getAnswerKey() {
        return answerKey;
    }
    /**
     * @param answerKey The answerKey to set.
     */
    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }
    /**
     * @return Returns the contentAreaTitle.
     */
    public String getContentAreaTitle() {
        return contentAreaTitle;
    }
    /**
     * @param contentAreaTitle The contentAreaTitle to set.
     */
    public void setContentAreaTitle(String contentAreaTitle) {
        this.contentAreaTitle = contentAreaTitle;
    }
    /**
     * @return Returns the contentId.
     */
    public String getContentId() {
        return contentId;
    }
    /**
     * @param contentId The contentId to set.
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
    /**
     * @return Returns the desc.
     */
    public String getDesc() {
        return desc;
    }
    /**
     * @param desc The desc to set.
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    /**
     * @return Returns the giResponse.
     */
    public String getGiResponse() {
        return giResponse;
    }
    /**
     * @param giResponse The giResponse to set.
     */
    public void setGiResponse(String giResponse) {
        this.giResponse = giResponse;
    }
    /**
     * @return Returns the itemNo.
     */
    public String getItemNo() {
        return itemNo;
    }
    /**
     * @param itemNo The itemNo to set.
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    /**
     * @return Returns the itemsysDesc.
     */
    public String getItemsysDesc() {
        return itemsysDesc;
    }
    /**
     * @param itemsysDesc The itemsysDesc to set.
     */
    public void setItemsysDesc(String itemsysDesc) {
        this.itemsysDesc = itemsysDesc;
    }
    /**
     * @return Returns the itemType.
     */
    public String getItemType() {
        return itemType;
    }
    /**
     * @param itemType The itemType to set.
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    /**
     * @return Returns the levelId.
     */
    public String getLevelId() {
        return levelId;
    }
    /**
     * @param levelId The levelId to set.
     */
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }
    /**
     * @return Returns the moduleId.
     */
    public String getModuleId() {
        return moduleId;
    }
    /**
     * @param moduleId The moduleId to set.
     */
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
    /**
     * @return Returns the moduleName.
     */
    public String getModuleName() {
        return moduleName;
    }
    /**
     * @param moduleName The moduleName to set.
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    /**
     * @return Returns the nac.
     */
    public String getNac() {
        return nac;
    }
    /**
     * @param nac The nac to set.
     */
    public void setNac(String nac) {
        this.nac = nac;
    }
    /**
     * @return Returns the noScorePoints.
     */
    public String getNoScorePoints() {
        return noScorePoints;
    }
    /**
     * @param noScorePoints The noScorePoints to set.
     */
    public void setNoScorePoints(String noScorePoints) {
        this.noScorePoints = noScorePoints;
    }
    /**
     * @return Returns the objectiveCode.
     */
    public String getObjectiveCode() {
        return objectiveCode;
    }
    /**
     * @param objectiveCode The objectiveCode to set.
     */
    public void setObjectiveCode(String objectiveCode) {
        this.objectiveCode = objectiveCode;
    }
    /**
     * @return Returns the objectiveId.
     */
    public String getObjectiveId() {
        return objectiveId;
    }
    /**
     * @param objectiveId The objectiveId to set.
     */
    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }
    /**
     * @return Returns the objectiveTitle.
     */
    public String getObjectiveTitle() {
        return objectiveTitle;
    }
    /**
     * @param objectiveTitle The objectiveTitle to set.
     */
    public void setObjectiveTitle(String objectiveTitle) {
        this.objectiveTitle = objectiveTitle;
    }
    /**
     * @return Returns the pageNo.
     */
    public String getPageNo() {
        return pageNo;
    }
    /**
     * @param pageNo The pageNo to set.
     */
    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }
    /**
     * @return Returns the part.
     */
    public String getPart() {
        return part;
    }
    /**
     * @param part The part to set.
     */
    public void setPart(String part) {
        this.part = part;
    }
    /**
     * @return Returns the projectId.
     */
    public String getProjectId() {
        return projectId;
    }
    /**
     * @param projectId The projectId to set.
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    /**
     * @return Returns the projectName.
     */
    public String getProjectName() {
        return projectName;
    }
    /**
     * @param projectName The projectName to set.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    /**
     * @return Returns the rightsKey.
     */
    public String getRightsKey() {
        return rightsKey;
    }
    /**
     * @param rightsKey The rightsKey to set.
     */
    public void setRightsKey(String rightsKey) {
        this.rightsKey = rightsKey;
    }
    /**
     * @return Returns the root.
     */
    public String getRoot() {
        return root;
    }
    /**
     * @param root The root to set.
     */
    public void setRoot(String root) {
        this.root = root;
    }
    /**
     * @return Returns the rootName.
     */
    public String getRootName() {
        return rootName;
    }
    /**
     * @param rootName The rootName to set.
     */
    public void setRootName(String rootName) {
        this.rootName = rootName;
    }
    /**
     * @return Returns the subskillCode.
     */
    public String getSubskillCode() {
        return subskillCode;
    }
    /**
     * @param subskillCode The subskillCode to set.
     */
    public void setSubskillCode(String subskillCode) {
        this.subskillCode = subskillCode;
    }
    /**
     * @return Returns the subskillId.
     */
    public String getSubskillId() {
        return subskillId;
    }
    /**
     * @param subskillId The subskillId to set.
     */
    public void setSubskillId(String subskillId) {
        this.subskillId = subskillId;
    }
    /**
     * @return Returns the subskillTitle.
     */
    public String getSubskillTitle() {
        return subskillTitle;
    }
    /**
     * @param subskillTitle The subskillTitle to set.
     */
    public void setSubskillTitle(String subskillTitle) {
        this.subskillTitle = subskillTitle;
    }
    /**
     * @return Returns the subtestCode.
     */
    public String getSubtestCode() {
        return subtestCode;
    }
    /**
     * @param subtestCode The subtestCode to set.
     */
    public void setSubtestCode(String subtestCode) {
        this.subtestCode = subtestCode;
    }
    /**
     * @return Returns the subtestId.
     */
    public String getSubtestId() {
        return subtestId;
    }
    /**
     * @param subtestId The subtestId to set.
     */
    public void setSubtestId(String subtestId) {
        this.subtestId = subtestId;
    }
    /**
     * @return Returns the subtestTitle.
     */
    public String getSubtestTitle() {
        return subtestTitle;
    }
    /**
     * @param subtestTitle The subtestTitle to set.
     */
    public void setSubtestTitle(String subtestTitle) {
        this.subtestTitle = subtestTitle;
    }
    /**
     * @return Returns the thinkId.
     */
    public String getThinkId() {
        return thinkId;
    }
    /**
     * @param thinkId The thinkId to set.
     */
    public void setThinkId(String thinkId) {
        this.thinkId = thinkId;
    }
    /**
     * @return Returns the thinkTitle.
     */
    public String getThinkTitle() {
        return thinkTitle;
    }
    /**
     * @param thinkTitle The thinkTitle to set.
     */
    public void setThinkTitle(String thinkTitle) {
        this.thinkTitle = thinkTitle;
    }
    /**
     * @param itemId The itemId to set.
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}