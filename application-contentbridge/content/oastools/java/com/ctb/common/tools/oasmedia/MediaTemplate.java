package com.ctb.common.tools.oasmedia;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.ctb.common.tools.IOUtils;
import com.ctb.common.tools.SystemException;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:16:25 PM
 * To change this template use Options | File Templates.
 */
public class MediaTemplate {

    public static final MediaTemplate PDF_ALL = PDFTemplate.instance();
    public static final MediaTemplate PDF_ALL_AK = PDFAnswerKeyTemplate.instance();
    public static final MediaTemplate PDF_CONSTRUCTIVE_RESPONSE_ONLY = PDFConstructiveResponseOnlyTemplate.instance();
    public static final MediaTemplate PDF_CONSTRUCTIVE_RESPONSE_ONLY_AK = PDFConstructiveResponseAKTemplate.instance();
    public static final MediaTemplate FLASH_MOVIE_TEMPLATE = FlashMovieTemplate.instance();
    public static final MediaTemplate FLASH_MOVIE_AK_TEMPLATE = FlashMovieAKTemplate.instance();

    private static final ArrayList docList = new ArrayList();
    static {
        docList.add(PDF_CONSTRUCTIVE_RESPONSE_ONLY);
        docList.add(PDF_CONSTRUCTIVE_RESPONSE_ONLY_AK);
        docList.add(PDF_ALL);
        docList.add(PDF_ALL_AK);
    }

    private String keyName;
    private byte[] xslDocumentBytes = null;

    protected MediaTemplate(String keyName, byte[] xslDocumentBytes) {
        this.keyName = keyName;
        this.xslDocumentBytes = xslDocumentBytes;
    }

    protected MediaTemplate(String keyName, String fileNameReference) {
        this.keyName = keyName;
        loadInstance(this,fileNameReference);
    }

    public String toString() {
        return keyName;
    }

    public InputStream getDocumentStream() {
        return new ByteArrayInputStream(xslDocumentBytes);
    }

    public InputStreamReader getDocumentStreamReader() {
        return new InputStreamReader(new ByteArrayInputStream(xslDocumentBytes));
    }

    public byte[] getDocumentBytes() {
        return xslDocumentBytes;
    }

    private void setDocumentBytes(byte[] bytes) {
        this.xslDocumentBytes = bytes;
    }

    public static MediaTemplate[] getDocTypes() {
        return (MediaTemplate[]) docList.toArray(new MediaTemplate[docList.size()]);
    }

    private static synchronized void loadInstance(MediaTemplate me, String fileNameReference) {
        try {
            File xslFile = new File(fileNameReference);
            byte[] bytes = IOUtils.loadBytes(xslFile);
            me.setDocumentBytes(bytes);
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

}
