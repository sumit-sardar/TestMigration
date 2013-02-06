package com.ctb.contentBridge.core.publish.cvm.oas;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemMediaRecord;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * User: mwshort
 * Date: Mar 30, 2004
 * Time: 2:57:07 PM
 */
public class Media {
    byte[] mediaBytes;
    MediaType mediaType;
    File outFile;
    private static final String SPACE = " ";
    private static Category LOG = Logger.getInstance(ItemMediaRecord.class.getName());
    private String systemId;

    protected Media(MediaType type) {
        this.mediaType = type;
        this.systemId = ItemMediaRecord.getSystemId(); // get file url
    }

    public Media(byte[] mediaBytes, MediaType type) {
        this(type);
        this.mediaBytes = mediaBytes;
    }

    public Media(ItemMediaRecord imr, MediaType type) {
        this(type);
        if (type == MediaType.XML_MEDIA_TYPE)
            this.mediaBytes = new String(imr.getMediaClob()).getBytes();  //TODO - find a better way to convert from character array to byte array
        else
            this.mediaBytes = imr.getMediaBlob();
    }


    public byte[] getMediaBytes() {
        return mediaBytes;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public String getFileName() {
        return outFile.getName();
    }

    public String getSystemId() {
        return systemId;
    }

    public Document getDocumentForXMLMedia() {
        if (isXMLMediaType()) {
            SAXBuilder builder = new SAXBuilder(false);
            try {
                return builder.build(new ByteArrayInputStream(getMediaBytes()));
            }
            catch (Exception e) {
                LOG.warn(e);
                throw new SystemException(e.getMessage());
            }
        }
        else
            throw new SystemException("media is not of XML type");
    }


    public String getXMLWithDocType() {
        if (isXMLMediaType()) {
            return getXMLStringWithDocType();
        }
        else
            throw new SystemException("media is not xml type");
    }


    private String getXMLStringWithDocType() {
        //TODO find better way to append doctype to mediabytes
        StringBuffer buffer = new StringBuffer();
        addStringWithSpace(buffer, "<!DOCTYPE");
        addStringWithSpace(buffer, ItemMediaRecord.ROOT_ELEMENT);
        addStringWithSpace(buffer, "PUBLIC");
        addStringWithSpace(buffer, "\"" + ItemMediaRecord.PUBLIC_ID + "\"");
        addStringWithSpace(buffer, "\"" + ItemMediaRecord.getSystemId() + "\"");
        addStringWithSpace(buffer, ">");
        buffer.append(new String(getMediaBytes()).toCharArray());
        return buffer.toString();
    }

    private boolean isXMLMediaType() {
        return mediaType.getMediaType().equals(MediaType.XML_MEDIA_TYPE.getMediaType());
    }

    private void addStringWithSpace(StringBuffer buffer, String string) {
        buffer.append(string + SPACE);
    }


}
