package com.ctb.common.tools.media;

import java.io.*;
import org.apache.log4j.Logger;

import com.ctb.common.tools.IOUtils;
import com.ctb.xmlProcessing.item.Item;

public class Media {
    private static Logger logger = Logger.getLogger(Media.class);

	private String itemId;
    private char[] xml;
    private byte[] akSwf;
    private byte[] pdf;
    private byte[] akPdf;

    public Media() {
    }

    public Media(char[] xml, byte[] akSwf, byte[] pdf, byte[] akPdf) {
        this.xml = xml;
        this.akSwf = akSwf;
        this.pdf = pdf;
        this.akPdf = akPdf;
    }

    public Media(File dir, Item item) throws IOException {
        xml = loadChars(dir, item.getId(), ".xml");
        akSwf = loadBytes(dir, item.getId(), ".swf");
        pdf = loadBytes(dir, item.getId(), ".pdf");
    }

    private byte[] loadBytes(File dir, String id, String extension)
        throws IOException {
        File file = new File(dir, id + extension);

        if (!file.exists()) {
            return null;
        }
        return IOUtils.loadBytes(file);
    }

    private char[] loadChars(File dir, String id, String extension)
        throws IOException {
        File file = new File(dir, id + extension);

        if (!file.exists()) {
            return null;
        }
        return IOUtils.loadChars(file);
    }

    public char[] getXml() {
        return xml;
    }

    public void setXml(char[] xml) {
        this.xml = xml;
    }

    public byte[] getAkSwf() {
        return akSwf;
    }

    public void setAkSwf(byte[] akSwf) {
        this.akSwf = akSwf;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public byte[] getAkPdf() {
        return akPdf;
    }

    public void setAkPdf(byte[] akPdf) {
        this.akPdf = akPdf;
    }

    public static void writeMedia(File file, Media media) {
        try {
            if (media.getAkSwf() != null) {
                File generatedAkSwf = new File(file, "ak.swf");
                IOUtils.writeFile(generatedAkSwf, media.getAkSwf());
            }

            if (media.getAkPdf() != null) {
                File generatedAkPdf = new File(file, "ak.pdf");
                IOUtils.writeFile(generatedAkPdf, media.getAkPdf());
            }

            if (media.getPdf() != null) {
                File generatedItemPdf = new File(file, "item.pdf");
                IOUtils.writeFile(generatedItemPdf, media.getPdf());
            }

            if (media.getXml() != null) {
                File generatedXML = new File(file, "item.xml");
                IOUtils.writeFile(generatedXML, media.getXml());
            }

        } catch (IOException e) {
            logger.error("", e);
        }

    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
