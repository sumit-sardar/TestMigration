/*
 * Created on Sep 12, 2003
 *
 */
package com.ctb.common.tools.media;

import java.io.*;
import java.util.*;

/**
 * @author wmli
 *
 */
public class MediaType implements Serializable {
    private static Map mediaTypes = new HashMap();

    public static final String IBXML = "IBXML";
    public static final String IBSWF = "IBSWF";
    public static final String IBPDF = "IBPDF";
    public static final String AKSWF = "AKSWF";
    public static final String AKPDF = "AKPDF";
    public static final String CRAKPDF = "CRAKPDF";
    public static final String CRIBPDF = "CRIBPDF";


    public static final String BLOB = "BLOB";
    public static final String CLOB = "CLOB";

    public static final String SWF_MIME_TYPE = "application/x-shockwave-flash";
    public static final String XML_MIME_TYPE = "text/xml";
    public static final String PDF_MINE_TYPE = "application/pdf";

    public static final MediaType XML_MEDIA_TYPE =
        new MediaType(IBXML, XML_MIME_TYPE, CLOB);
    public static final MediaType FLASH_MOVIE_MEDIA_TYPE =
        new MediaType(IBSWF, SWF_MIME_TYPE, BLOB);
    public static final MediaType FLASH_ANSWER_MEDIA_TYPE =
        new MediaType(AKSWF, SWF_MIME_TYPE, BLOB);
    public static final MediaType PDF_ANSWER_MEDIA_TYPE =
        new MediaType(AKPDF, PDF_MINE_TYPE, BLOB);
    public static final MediaType PDF_MEDIA_TYPE =
        new MediaType(IBPDF, PDF_MINE_TYPE, BLOB);

    public static final MediaType PDF_CR_AK_ONLY =
        new MediaType(CRAKPDF, PDF_MINE_TYPE, BLOB);
    public static final MediaType PDF_CR_ONLY =
        new MediaType(CRIBPDF, PDF_MINE_TYPE, BLOB);


    private String lobType;
    private String mineType;
    private String mediaType;

    public MediaType(String mediaType, String mineType, String lobType) {
        this.mediaType = mediaType;
        this.mineType = mineType;
        this.lobType = lobType;

        mediaTypes.put(this.mediaType, this);
    }

    static public Iterator getAllMediaTypes() {
        return mediaTypes.values().iterator();
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
        } else {
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
