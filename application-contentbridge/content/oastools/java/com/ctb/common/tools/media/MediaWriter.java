package com.ctb.common.tools.media;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.common.tools.DBConnection;
import com.ctb.xmlProcessing.item.Item;


/**
 * @author smaddala
 *
 */
public class MediaWriter {

    public static final String SWF_MIME_TYPE = "application/x-shockwave-flash";// MediaWriter
    public static final String XML_MIME_TYPE = "text/xml";// MediaWriter
    public static final String PDF_MIME_TYPE = "application/pdf";// MediaWriter
    public static final String XML_MEDIA = "IBXML";// MediaWriter
    public static final String ANSWER_KEY_SWF_MEDIA = "AKSWF"; // MediaWriter
    public static final String PDF_MEDIA = "IBPDF"; // MediaWriter
    public static final String ANSWER_KEY_PDF_MEDIA = "AKPDF"; // MediaWriter

    private DBConnection dbconnection;
    private String ID;
    private String idCol = "ITEM_ID";
    String mediaPath = MediaMapper.CAB_MEDIA_PATH;
    // MediaWriter constants

    public MediaWriter(String ID, Connection connection) {
        this(ID,new DBConnection(connection));
    }

    public MediaWriter(String ID, DBConnection dbconnection) {
        this.ID = ID;
        this.dbconnection = dbconnection;
    }

    public Media readMedia() throws SQLException {
        return new Media(readXmlClob(), readAkSwfBlob(), readPdfBlob(),
                readAkPdfBlob());
    }

    public byte[] readPdfBlob() throws SQLException {
        return readMediaBlob(PDF_MEDIA);
    }

    public byte[] readAkSwfBlob() throws SQLException {
        return readMediaBlob(ANSWER_KEY_SWF_MEDIA);
    }

    public byte[] readAkPdfBlob() throws SQLException {
        return readMediaBlob(ANSWER_KEY_PDF_MEDIA);
    }

    public char[] readXmlClob() throws SQLException {
        return readMediaClob(XML_MEDIA);
    }

    public void writeMedia(Item item,Media media) throws IOException, SQLException {
        writeMedia(item.getId(),media);

    }

    public void writeMedia(String itemID ,Media media) throws IOException, SQLException {
        writeXmlMedia(media.getXml());
        writeAkSwfMedia(media.getAkSwf());
        writePdfMedia(media.getPdf());
        byte[] akPdf = media.getAkPdf();

        if (akPdf != null) {
            writeAkPdfMedia(akPdf);
        }
    }

    public void writeMedia(Media media) throws IOException, SQLException {
        writeMedia(ID,media);
    }

    public void writeAkSwfMedia(byte[] bytes) throws SQLException, IOException {
        writeBlob("ITEM_MEDIA", ANSWER_KEY_SWF_MEDIA, SWF_MIME_TYPE, bytes);
    }

    public void writePdfMedia(byte[] bytes) throws SQLException, IOException {
        writeBlob("ITEM_MEDIA", PDF_MEDIA, PDF_MIME_TYPE, bytes);
    }

    public void writeAkPdfMedia(byte[] bytes) throws SQLException, IOException {
        writeBlob("ITEM_MEDIA", ANSWER_KEY_PDF_MEDIA, PDF_MIME_TYPE, bytes);
    }

    public void writeXmlMedia(char[] chars) throws SQLException, IOException {
        writeClob("ITEM_MEDIA", XML_MEDIA, XML_MIME_TYPE, chars);
    }

    // / todo: unify BLOB and CLOB more

    private byte[] readMediaBlob(String mediaType) throws SQLException {
        String cmd = selectBlobOrClob(mediaType, "MEDIA_BLOB");

        return dbconnection.readBlob(cmd);
    }

    private char[] readMediaClob(String mediaType) throws SQLException {
        String cmd = selectBlobOrClob(mediaType, "MEDIA_CLOB");

        return dbconnection.readClob(cmd);
    }

    private String selectBlobOrClob(String mediaType, String field) {
        return "SELECT ITEM_ID, " + field + " FROM ITEM_MEDIA WHERE ITEM_ID ='"
                + ID + "'" + " AND MEDIA_TYPE ='" + mediaType + "'";
    }

    private String selectBlobOrClobForUpdate(String mediaType, String field) {
        return selectBlobOrClob(mediaType, field) + " FOR UPDATE";
    }

    private void writeClob(String table, String mediaType, String mimeType, char[] chars) throws SQLException, IOException {
        writeBlobOrClob(table, mediaType, mimeType, null, chars);
    }

    private void writeBlob(String table, String mediaType, String mimeType, byte[] bytes) throws SQLException, IOException {
        writeBlobOrClob(table, mediaType, mimeType, bytes, null);
    }

    private void writeBlobOrClob(String table, String mediaType, String mimeType, byte[] bytes, char[] chars) throws SQLException, IOException {
        if (MediaType.getMediaType(mediaType).getLobType().equals(MediaType.BLOB)) {
        	if (bytes == null) return;
        	
            insertOrUpdateEmptyBlob(table, mediaType, mimeType);
            writeBlob(bytes, mediaType);
        } else if (MediaType.getMediaType(mediaType).getLobType().equals(MediaType.CLOB)) {
        	if (chars == null) return;
        	
            insertOrUpdateEmptyClob(table, mediaType, mimeType);
            writeClob(chars, mediaType);
        }
    }

    private void writeBlob(byte[] bytes, String mediaType) throws SQLException, IOException {
        String cmd = selectBlobOrClobForUpdate(mediaType, "MEDIA_BLOB");

        dbconnection.writeBlob(cmd, bytes);
    }

    private void writeClob(char[] chars, String mediaType) throws SQLException, IOException {
        String cmd = selectBlobOrClobForUpdate(mediaType, "MEDIA_CLOB");

        dbconnection.writeClob(cmd, chars);
    }

    private void insertOrUpdateEmptyBlob(String table, String mediaType, String mimeType) throws SQLException {
        String cmd = "SELECT COUNT(*) FROM " + table + " WHERE " + idCol
                + " = '" + ID + "' AND MEDIA_TYPE ='" + mediaType + "'";
        int count = dbconnection.executeCountQuery(cmd, null);

        if (count == 0) {
            dbconnection.executeUpdate("INSERT INTO " + table
                    + " (MEDIA_TYPE, MEDIA_BLOB, MIME_TYPE, " + idCol
                    + " , MEDIA_PATH) VALUES ('" + mediaType
                    + "', empty_blob(), '" + mimeType + "', '" + ID + "', '"
                    + mediaPath + "')");
        } else {
            dbconnection.executeUpdate("UPDATE " + table
                    + " SET MEDIA_BLOB = empty_blob() WHERE " + idCol + " = '"
                    + ID + "' AND MEDIA_TYPE ='" + mediaType + "'");
        }
    }

    private void insertOrUpdateEmptyClob(String table, String mediaType, String mimeType) throws SQLException {
        String cmd = "SELECT COUNT(*) FROM " + table + " WHERE " + idCol
                + " = '" + ID + "' AND MEDIA_TYPE ='" + mediaType + "'";
        int count = dbconnection.executeCountQuery(cmd, null);

        if (count == 0) {
            dbconnection.executeUpdate("INSERT INTO " + table
                    + " (MEDIA_TYPE, MEDIA_CLOB, MIME_TYPE, " + idCol
                    + " , MEDIA_PATH) VALUES ('" + mediaType
                    + "', empty_clob(), '" + mimeType + "', '" + ID + "', '"
                    + mediaPath + "')");
        } else {
            dbconnection.executeUpdate("UPDATE " + table
                    + " SET MEDIA_CLOB = empty_clob() WHERE " + idCol + " = '"
                    + ID + "' AND MEDIA_TYPE ='" + mediaType + "'");
        }
    }

    public void deleteMedia() {
        String cmd = "DELETE FROM ITEM_MEDIA WHERE ITEM_ID ='" + ID + "'";

        dbconnection.executeUpdate(cmd);
    }
}
