package com.ctb.hibernate.persist;

import com.ctb.hibernate.Persistent;

/**
 * @hibernate.class table="ITEM_SET_MEDIA"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemSetMediaRecord extends Persistent {
    private Long createdBy;
    private java.util.Date createdDateTime;
    private byte[] mediaBlob;
    private char[] mediaClob;
    private String mediaPath;
    private String mimeType;
    private Long updatedBy;
    private java.util.Date updatedDateTime;

    private ItemSetMediaCompositeId id = new ItemSetMediaCompositeId();

    /** @hibernate.id generator-class="assigned" */
    public ItemSetMediaCompositeId getId() {
        return id;
    }
    public void setId(ItemSetMediaCompositeId id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * column="CREATED_BY"
     * not-null="false"
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @hibernate.property
     * update="false"
     * column="CREATED_DATE_TIME"
     * not-null="false"
     */
    public java.util.Date getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * @hibernate.property
     * column="MEDIA_BLOB"
     * not-null="false"
     * type="com.ctb.hibernate.usertypes.ByteArrayBlobType"
     */
    public byte[] getMediaBlob() {
        return mediaBlob;
    }

    /**
     * @hibernate.property
     * column="MEDIA_CLOB"
     * not-null="false"
     * type="com.ctb.hibernate.usertypes.CharArrayClobType"
     */
    public char[] getMediaClob() {
        return mediaClob;
    }

    /**
     * @hibernate.property
     * column="MEDIA_PATH"
     * not-null="false"
     */
    public String getMediaPath() {
        return mediaPath;
    }

    /**
     * @hibernate.property
     * column="MIME_TYPE"
     * not-null="true"
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @hibernate.property
     * column="UPDATED_BY"
     * not-null="false"
     */
    public Long getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @hibernate.property
     * column="UPDATED_DATE_TIME"
     * not-null="false"
     */
    public java.util.Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setMediaBlob(byte[] mediaBlob) {
        this.mediaBlob = mediaBlob;
    }

    public void setMediaClob(char[] mediaClob) {
        this.mediaClob = mediaClob;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
