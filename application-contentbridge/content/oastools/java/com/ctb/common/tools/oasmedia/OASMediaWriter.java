package com.ctb.common.tools.oasmedia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.common.tools.DBConnection;
import com.ctb.common.tools.media.MediaMapper;
import com.ctb.common.tools.media.MediaType;

/**
 * User: mwshort
 * Date: Dec 12, 2003
 * Time: 12:11:35 PM
 * 
 *
 */
public class OASMediaWriter {

    // inserts
    public static final String INSERT_CLAUSE =
        "insert into ITEM_SET_MEDIA "
            + "(MEDIA_TYPE, MEDIA_BLOB, MEDIA_CLOB, MIME_TYPE, ITEM_SET_ID, MEDIA_PATH, CREATED_BY, CREATED_DATE_TIME) "
            + "values "
            + "('{0}', empty_blob(), empty_clob(), '{1}', {2}, '{3}', {4}, sysdate)";

    public static final String COUNT_ITEM_SET_MEDIA =
        "select count(*) from ITEM_SET_MEDIA "
            + "where ITEM_SET_ID = {0} "
            + "and MEDIA_TYPE = '{1}'";

    //updates
    //this update changes the app logic fields such as timestamps and user id
    public static final String UPDATE_CLAUSE =
        "update ITEM_SET_MEDIA "
            + "set UPDATED_BY = {0}, "
            + "UPDATED_DATE_TIME = sysdate "
            + "where ITEM_SET_ID = {1} "
            + "and MEDIA_TYPE = '{2}'";

    public static final String DELETE_ITEM_SET_MEDIA =
        "delete from ITEM_SET_MEDIA where ITEM_SET_ID = {0}";

    //This is a select for update so we can access the LOB outputstream for writes and trims

    public static final String SELECT_FOR_UPDATE_CLAUSE =
        "select ITEM_SET_ID, MEDIA_BLOB, MEDIA_CLOB "
            + "from ITEM_SET_MEDIA "
            + "where ITEM_SET_ID = {0} "
            + "and MEDIA_TYPE = '{1}'";
    public static final String FOR_UPDATE = " for update";
    private DBConnection dbConnection;

    private Long userId;

    public OASMediaWriter(Connection connection, Long userId) {
        this(new DBConnection(connection), userId);
    }

    public OASMediaWriter(DBConnection dbConnection, Long userId) {
        this.dbConnection = dbConnection;
        this.userId = userId;
    }

    public void writeMedia(Long testDeliveryItemSetId, OASMedia media)
        throws SQLException, IOException {

        // write the xml media to the db
        writeMediaToDatabase(
            testDeliveryItemSetId,
            media.getAssessmentXML(),
            MediaMapper.CAB_MEDIA_PATH,
            MediaType.XML_MEDIA_TYPE);

        // write the flash movie media to the db
        writeMediaToDatabase(
            testDeliveryItemSetId,
            media.getAssessmentMovie(),
            MediaMapper.CAB_MEDIA_PATH,
            MediaType.FLASH_MOVIE_MEDIA_TYPE);

        // write the flash answer media to the db
        writeMediaToDatabase(
            testDeliveryItemSetId,
            media.getAssessmentMovieWithAnswerKeys(),
            MediaMapper.CAB_MEDIA_PATH,
            MediaType.FLASH_ANSWER_MEDIA_TYPE);

        // write the pdf answer media to the db
        writeMediaToDatabase(
            testDeliveryItemSetId,
            media.getPDFAnswerKey(),
            MediaMapper.CAB_MEDIA_PATH,
            MediaType.PDF_ANSWER_MEDIA_TYPE);

        //write the pdf media to the db
        writeMediaToDatabase(
            testDeliveryItemSetId,
            media.getPDF(),
            MediaMapper.CAB_MEDIA_PATH,
            MediaType.PDF_MEDIA_TYPE);

        //write the pdf CR ak media to the db
        if (media.getPDFAnswerKeyCROnly() != null)
            writeMediaToDatabase(
                testDeliveryItemSetId,
                media.getPDFAnswerKeyCROnly(),
                MediaMapper.CAB_MEDIA_PATH,
                MediaType.PDF_CR_AK_ONLY);

        //write the pdf CR item media to the db
        if (media.getPDFCROnly() != null)
            writeMediaToDatabase(
                testDeliveryItemSetId,
                media.getPDFCROnly(),
                MediaMapper.CAB_MEDIA_PATH,
                MediaType.PDF_CR_ONLY);

    }

    private Long getUserId() {
        return userId;
    }

    protected void writeMediaToDatabase(
        Long testDeliveryItemSetId,
        Object mediaData,
        String mediaPath,
        MediaType mediaType)
        throws SQLException, IOException {
        if (isInsert(testDeliveryItemSetId, mediaType)) {
            createMediaRecord(testDeliveryItemSetId, mediaPath, mediaType);
        } else {
            updateMediaRecord(testDeliveryItemSetId, mediaType);
        }
        writeLOB(testDeliveryItemSetId, mediaData, mediaType);
    }

    private boolean isInsert(Long testDeliveryItemSetId, MediaType mediaType)
        throws SQLException {
        return (getNumberOfItemSetMedia(testDeliveryItemSetId, mediaType) == 0);
    }

    private int getNumberOfItemSetMedia(
        Long testDeliveryItemSetId,
        MediaType mediaType)
        throws SQLException {
        int itemSetMediaCount =
            dbConnection.executeCountQuery(
                COUNT_ITEM_SET_MEDIA,
                new Object[] {
                    testDeliveryItemSetId,
                    mediaType.getMediaType()});

        return itemSetMediaCount;
    }

    private void createMediaRecord(
        Long testDeliveryItemSetId,
        String mediaPath,
        MediaType mediaType) {
        dbConnection.executeUpdate(
            INSERT_CLAUSE,
            new Object[] {
                mediaType.getMediaType(),
                mediaType.getMineType(),
                testDeliveryItemSetId,
                mediaPath,
                getUserId()});
    }

    private void updateMediaRecord(
        Long testDeliveryItemSetId,
        MediaType mediaType) {
        dbConnection.executeUpdate(
            UPDATE_CLAUSE,
            new Object[] {
                getUserId(),
                testDeliveryItemSetId,
                mediaType.getMediaType()});
    }

    private void writeLOB(
        Long testDeliveryItemSetId,
        Object mediaData,
        MediaType mediaType)
        throws SQLException, IOException {
        Object[] arguments = { testDeliveryItemSetId, mediaType.getMediaType()};
        int column = (mediaType.getLobType().equals(MediaType.BLOB)) ? 2 : 3;
        dbConnection.writeBlobOrClob(
            DBConnection.formatSql(
                SELECT_FOR_UPDATE_CLAUSE + FOR_UPDATE,
                arguments),
            mediaData,
            column,
            mediaType);
    }

}
