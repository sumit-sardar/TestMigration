/*
 * Created on Sep 12, 2003
 *
 */
package com.ctb.contentBridge.core.publish.cvm.oas;

import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wmli
 */
public class MediaType implements Serializable {
    private static Map mediaTypes = new HashMap();
    //ITEM_MEDIA.MEDIA_TYPE
    public static final String IBXML = "IBXML"; //source xml for item
    public static final String IBSWF = "IBSWF"; //interactive flash movie for students
    public static final String IBPDF = "IBPDF"; //item pdf for students
    public static final String AKSWF = "AKSWF"; //static answer key flash movie for CTB and Teachers
    public static final String AKPDF = "AKPDF"; //answer key PDF, rarely used
    public static final String CRAKPDF = "CRAKPDF"; //answer key to essay questions only
    public static final String CRIBPDF = "CRIBPDF"; //essay questions only


    public static final String BLOB = "BLOB";
    public static final String CLOB = "CLOB";

    //HTTP Content Types
    public static final String SWF_MIME_TYPE = "application/x-shockwave-flash";
    public static final String XML_MIME_TYPE = "text/xml";
    public static final String PDF_MINE_TYPE = "application/pdf";

    //class id references for loading flash or pdf plugins into browser. Used to embed PDF and Flash in page
    public static final String PDF_CLASS_ID = "clsid:CA8A9780-280D-11CF-A24D-444553540000";
    public static final String FLASH_CLASS_ID = "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000";

    public static final String PDF_CODE_BASE = null;
    public static final String FLASH_CODE_BASE = "http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0";

    public static final String PDF_WIDTH = "650";
    public static final String FLASH_WIDTH = "650";
    public static final String FLASH_HEIGHT = "488";
    public static final String PDF_HEIGHT = "488";

    public static final MediaType XML_MEDIA_TYPE =
            new MediaType(IBXML, XML_MIME_TYPE, CLOB, null, null, null, null);
    public static final MediaType FLASH_MOVIE_MEDIA_TYPE =
            new MediaType(IBSWF, SWF_MIME_TYPE, BLOB, FLASH_CLASS_ID, FLASH_CODE_BASE, FLASH_WIDTH, FLASH_HEIGHT);
    public static final MediaType FLASH_ANSWER_MEDIA_TYPE =
            new MediaType(AKSWF, SWF_MIME_TYPE, BLOB, FLASH_CLASS_ID, FLASH_CODE_BASE, FLASH_WIDTH, FLASH_HEIGHT);
    public static final MediaType PDF_ANSWER_MEDIA_TYPE =
            new MediaType(AKPDF, PDF_MINE_TYPE, BLOB, PDF_CLASS_ID, PDF_CODE_BASE, PDF_WIDTH, PDF_HEIGHT);
    public static final MediaType PDF_MEDIA_TYPE =
            new MediaType(IBPDF, PDF_MINE_TYPE, BLOB, PDF_CLASS_ID, PDF_CODE_BASE, PDF_WIDTH, PDF_HEIGHT);

    public static final MediaType PDF_CR_AK_ONLY =
            new MediaType(CRAKPDF, PDF_MINE_TYPE, BLOB, PDF_CLASS_ID, PDF_CODE_BASE, PDF_WIDTH, PDF_HEIGHT);
    public static final MediaType PDF_CR_ONLY =
            new MediaType(CRIBPDF, PDF_MINE_TYPE, BLOB, PDF_CLASS_ID, PDF_CODE_BASE, PDF_WIDTH, PDF_HEIGHT);


    private String lobType;
    private String mineType;
    private String mediaType;
    private String classID;
    private String codeBase;
    private String width;
    private String height;

    public MediaType(String mediaType, String mineType, String lobType, String classID, String codeBase, String width, String height) {
        this.lobType = lobType;
        this.mineType = mineType;
        this.mediaType = mediaType;
        this.classID = classID;
        this.codeBase = codeBase;
        this.width = width;
        this.height = height;

        mediaTypes.put(this.mediaType, this);
    }

    static public Iterator getAllMediaTypes() {
        return mediaTypes.values().iterator();
    }

    public String getClassID() {
        return classID;
    }

    public String getCodeBase() {
        return codeBase;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getLobType() {
        return lobType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getMineType() {
        return mineType;
    }

    public void setLobType(String string) {
        lobType = string;
    }

    public void setMediaType(String string) {
        mediaType = string;
    }

    public void setMineType(String string) {
        mineType = string;
    }

    public boolean isClob() {
        return lobType.equals(CLOB);
    }

    public boolean isBlob() {
        return lobType.equals(BLOB);
    }

    static public MediaType getMediaType(String type) {
        return (MediaType) mediaTypes.get(type);
    }

    private Object readResolve() throws InvalidObjectException {
        MediaType item = getMediaType(mediaType);

        if (item != null) {
            return item;
        }
        else {
            String msg = "Invalid deserialized SofaMediaType:  code = ";

            throw new InvalidObjectException(msg + mediaType);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof MediaType) {
            return this.mediaType.equals(((MediaType) obj).getMediaType());
        }

        return false;

    }
}
