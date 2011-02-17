package com.ctb.hibernate.persist;

import com.ctb.hibernate.Persistent;

/**
 * @hibernate.class table="TEMPLATE_CODE"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class TemplateCodeRecord extends Persistent {
    private String isReadingPassage;
    private String isStimulusGraphic;
    private String templateDesc;
    private String templateId;

    /**
     * @hibernate.property
     * column="IS_READING_PASSAGE"
     * not-null="true"
     */
    public String getIsReadingPassage() {
        return isReadingPassage;
    }

    /**
     * @hibernate.property
     * column="IS_STIMULUS_GRAPHIC"
     * not-null="true"
     */
    public String getIsStimulusGraphic() {
        return isStimulusGraphic;
    }

    /**
     * @hibernate.property
     * column="TEMPLATE_DESC"
     * not-null="false"
     */
    public String getTemplateDesc() {
        return templateDesc;
    }

    /** @hibernate.id generator-class="assigned" column="TEMPLATE_ID" */
    public String getTemplateId() {
        return templateId;
    }

    public void setIsReadingPassage(String isReadingPassage) {
        this.isReadingPassage = isReadingPassage;
    }

    public void setIsStimulusGraphic(String isStimulusGraphic) {
        this.isStimulusGraphic = isStimulusGraphic;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

}
