/*
 * Created on Sep 11, 2003
 */
package com.ctb.contentBridge.core.publish.xml.subtest;

import java.io.*;


import org.apache.log4j.*;

import com.ctb.contentBridge.core.publish.tools.IOUtils;

/**
 * @author wmli
 */
public class SubTestMedia {
    private static Logger logger = Logger.getLogger(SubTestMedia.class);

    Long itemSetId;
    char[] premadeTestXml;
    byte[] premadeTestFlashMovie;
    byte[] premadeTestFlashAnswer;
    byte[] premadeTestPDFAnswer;
    byte[] premadeTestPDFCRAnswer;
    byte[] premadeTestPDFCRQuestions;
    byte[] premadeTestPDFQuestions;

    public byte[] getPremadeTestPDFCRAnswer() {
        return premadeTestPDFCRAnswer;
    }

    public void setPremadeTestPDFCRAnswer(byte[] premadeTestPDFCRAnswer) {
        this.premadeTestPDFCRAnswer = premadeTestPDFCRAnswer;
    }

    public byte[] getPremadeTestPDFCRQuestions() {
        return premadeTestPDFCRQuestions;
    }

    public void setPremadeTestPDFCRQuestions(byte[] premadeTestPDFCRQuestions) {
        this.premadeTestPDFCRQuestions = premadeTestPDFCRQuestions;
    }

    public byte[] getPremadeTestPDFQuestions() {
        return premadeTestPDFQuestions;
    }

    public void setPremadeTestPDFQuestions(byte[] premadeTestPDFQuestions) {
        this.premadeTestPDFQuestions = premadeTestPDFQuestions;
    }

    public byte[] getPremadeTestFlashAnswer() {
        return premadeTestFlashAnswer;
    }

    public byte[] getPremadeTestFlashMovie() {
        return premadeTestFlashMovie;
    }

    public char[] getPremadeTestXml() {
        return premadeTestXml;
    }

    public void setPremadeTestFlashAnswer(byte[] bs) {
        premadeTestFlashAnswer = bs;
    }

    public void setPremadeTestFlashMovie(byte[] bs) {
        premadeTestFlashMovie = bs;
    }

    public void setPremadeTestXml(char[] cs) {
        premadeTestXml = cs;
    }

    public byte[] getPremadeTestPDFAnswer() {
        return premadeTestPDFAnswer;
    }

    public void setPremadeTestPDFAnswer(byte[] bs) {
        premadeTestPDFAnswer = bs;
    }

    public static void writeMedia(File file, SubTestMedia media) {
        File generateFlash = new File(file, "generatedSofaMovie.swf");

        try {
            IOUtils.writeFile(generateFlash, media.getPremadeTestFlashMovie());

            File generateFlashAK = new File(file, "generateSofaAnswers.swf");

            IOUtils.writeFile(generateFlashAK, media.getPremadeTestFlashAnswer());

            File generatePDFAK = new File(file, "generateSofaAnswers.pdf");

            IOUtils.writeFile(generatePDFAK, media.getPremadeTestPDFAnswer());

            File generateXml = new File(file, "generateXML.xml");

            IOUtils.writeFile(generateXml, media.getPremadeTestXml());
        } catch (IOException e) {
            logger.error("", e);
        }

    }

    public Long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(Long long1) {
        itemSetId = long1;
    }
}