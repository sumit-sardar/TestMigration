/*
 * Created on Nov 3, 2003
 *
 * Worker for retrieve media from database
 */
package com.ctb.contentBridge.core.publish.roundtrip;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.cvm.oas.MediaType;
import com.ctb.contentBridge.core.publish.dao.DBConnection;
import com.ctb.contentBridge.core.publish.media.Media;
import com.ctb.contentBridge.core.util.Pipe;


public class DatabaseMediaReader implements MediaReader {
    private static Logger logger = Logger.getLogger(DatabaseMediaReader.class);
    Connection connection;

    public static final String SELECT_ALL_ACTIVE_ITEM_MEDIA_SQL =
        "select item_media.item_id, item_media.media_type, item_media.media_blob, item_media.media_clob from "
            + "item_media, item  where item.activation_status='AC' and "
            + "item_media.item_id = item.item_id and "
            + "item.item_id = '<ITEM_ID>'";

    public static final String ITEM_ID_TAG = "<ITEM_ID>";

    public DatabaseMediaReader(Connection conn) {
        this.connection = conn;
    }

    public Media readMedia(String itemId) {
        Media media = new Media();

        try {
            readMedia(itemId, media);
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }

        return media;
    }

    private void readMedia(String itemId, Media media) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0;

        stmt = connection.createStatement();

        String selectSql = SELECT_ALL_ACTIVE_ITEM_MEDIA_SQL;
        selectSql = StringUtils.replace(selectSql, ITEM_ID_TAG, itemId);

        rs = stmt.executeQuery(selectSql);

        while (rs.next()) {
            processResult((OracleResultSet) rs, media);
        }
        //close cursors
        DBConnection.safeClose(stmt,rs);
        if (media.getXml() == null) {
            throw new SystemException("Cannot retrieve item.");
        }

    }

    public void processResult(OracleResultSet rs, Media media)
        throws SQLException {
        MediaType mediaType =
            MediaType.getMediaType((String) rs.getString("media_type"));
        logger.debug(mediaType.getMediaType() + " found");

        if (mediaType.equals(MediaType.XML_MEDIA_TYPE)) {
            media.setXml((char[]) processLoB(rs, mediaType));
        } else if (mediaType.equals(MediaType.PDF_MEDIA_TYPE)) {
            media.setPdf((byte[]) processLoB(rs, mediaType));
        } else if (mediaType.equals(MediaType.PDF_ANSWER_MEDIA_TYPE)) {
            media.setAkPdf((byte[]) processLoB(rs, mediaType));
        } else if (mediaType.equals(MediaType.FLASH_ANSWER_MEDIA_TYPE)) {
            media.setAkSwf((byte[]) processLoB(rs, mediaType));
        }
    }

    private Object processLoB(OracleResultSet resultSet, MediaType mediaType)
        throws SQLException {

        if (mediaType.getLobType().equals(MediaType.BLOB)) {
            BLOB blob = ((OracleResultSet) resultSet).getBLOB("media_blob");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            new Pipe(blob.binaryStreamValue(), out, blob.getChunkSize()).run();

            return out.toByteArray();
        } else {
            CLOB clob = ((OracleResultSet) resultSet).getCLOB("media_clob");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            new Pipe(clob.binaryStreamValue(), out, clob.getChunkSize()).run();

            return out.toString().toCharArray();
        }

    }

}
