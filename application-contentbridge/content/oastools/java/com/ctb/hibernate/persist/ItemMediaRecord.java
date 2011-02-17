package com.ctb.hibernate.persist;

import com.ctb.cvm.oas.Media;
import com.ctb.cvm.oas.MediaType;
import com.ctb.hibernate.Persistent;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;



import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

/**
 * @hibernate.class table="ITEM_MEDIA"
 * persister="com.ctb.hibernate.CTBEntityPe`rsister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemMediaRecord extends Persistent {
    private Long createdBy;
    private java.util.Date createdDateTime;
    private byte[] mediaBlob;
    private char[] mediaClob;
    private String mediaPath;
    private String mimeType;
    private Long updatedBy;
    private java.util.Date updatedDateTime;
    public static final String SYSTEM_ID = "R2_Flash_UI.dtd";
    public static final String ROOT_ELEMENT = "Item";
    public static final String PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN";
    public static final String STEM_ELEMENT = "Stem";

//    <!DOCTYPE Assessment PUBLIC
//    "-//CTB//DTD Online Assessment System Authoring V 2.0//EN">


    private ItemMediaCompositeId id = new ItemMediaCompositeId();

    /**
     * @hibernate.id generator-class="assigned"
     */
    public ItemMediaCompositeId getId() {
        return id;
    }

    public void setId(ItemMediaCompositeId id) {
        this.id = id;
    }

    /**
     * @hibernate.property column="CREATED_BY"
     * not-null="false"
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @hibernate.property update="false"
     * column="CREATED_DATE_TIME"
     * not-null="true"
     */
    public java.util.Date getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * @hibernate.property column="MEDIA_BLOB"
     * not-null="false"
     * type="com.ctb.hibernate.usertypes.ByteArrayBlobType"
     */
    public byte[] getMediaBlob() {
        return mediaBlob;
    }

    /**
     * @hibernate.property column="MEDIA_CLOB"
     * not-null="false"
     * type="com.ctb.hibernate.usertypes.CharArrayClobType"
     */
    public char[] getMediaClob() {
        return mediaClob;
    }

    /**
     * @hibernate.property column="MEDIA_PATH"
     * not-null="false"
     */
    public String getMediaPath() {
        return mediaPath;
    }
    
    /**
     * @hibernate.property column="MIME_TYPE"
     * not-null="true"
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @hibernate.property column="UPDATED_BY"
     * not-null="false"
     */
    public Long getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @hibernate.property column="UPDATED_DATE_TIME"
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

    public static String getSystemId() {
        String systemIdPath = null;
        URL resource = ItemMediaRecord.class.getClassLoader().getResource(ItemMediaRecord.SYSTEM_ID);
        if (resource != null )
            systemIdPath  = resource.toExternalForm();

        return systemIdPath;
    }

    public void validateXML() throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder(true);
        Media media = new Media(this, MediaType.XML_MEDIA_TYPE);
        builder.build(new StringReader(media.getXMLWithDocType()));
    }
}
