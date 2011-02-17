package com.ctb.common.tools.itemxml;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleResultSet;
import oracle.sql.CLOB;

import com.ctb.common.tools.DBConfig;
import com.ctb.common.tools.SystemException;
import com.ctb.util.Pipe;


/**
 * iterate through item media XML CLOBs in OAS datbase, manage instances of <code>ItemsProcessor</code>
 * in the processing of that set of item media XML, and optionally update the processed CLOB contents back into OAS.
 */
public class ItemsProcessor {

    /**
     * sql select statement that will select all active item media xml clobs from
     * the item_media OAS table.  the statement includes a join to the item table.
     * any additional criteria passed to the constructor must inlcude fully
     * qualified column names.
     */
//    public static final String SELECT_ALL_ACTIVE_ITEM_MEDIA_XML_SQL = "select item_media.item_id, item_media.media_clob from "
//            + "" + "item_media, item  where item.activation_status='AC' and "
//            + "item_media.media_type='IBXML' and item_media.item_id = item.item_id";

    public static final String SELECT_ALL_ACTIVE_ITEM_MEDIA_XML_SQL = "select item_media.item_id, item_media.media_clob from "
            + "" + "item_media, item  where "
            + "item_media.media_type='IBXML' and item_media.item_id = item.item_id";

    private String selectSql = SELECT_ALL_ACTIVE_ITEM_MEDIA_XML_SQL;
    private com.ctb.common.tools.itemxml.ItemProcessor clobProcessor;
    private String outputDir;
    private boolean updateCLOB;
    private DBConfig dbConfig;
    private Connection connection;

    /**
     * Constructor
     * @param clobProcessor the item processor
     * @param additionalCriteria sql criteria clause to narrow the set of items to process
     * @param outputDir if specified, CLOB contents will be written in pre-proc and post-proc state to this dir.
     * if null a log of processing activity will not be kept.
     * @param updateCLOB whether to write the processed CLOB contents back to the database
     */
    public ItemsProcessor(com.ctb.common.tools.itemxml.ItemProcessor clobProcessor, String additionalCriteria,
            String outputDir, boolean updateCLOB, DBConfig dbConfig) {
        this(clobProcessor,additionalCriteria,outputDir,updateCLOB,dbConfig.openConnection());
    }


    public ItemsProcessor(com.ctb.common.tools.itemxml.ItemProcessor clobProcessor, String additionalCriteria,
            String outputDir, boolean updateCLOB, Connection connection) {
        this.clobProcessor = clobProcessor;
        if (additionalCriteria != null && additionalCriteria.trim().length() > 0) {
            selectSql += " and ";
            selectSql += additionalCriteria;
        }

        if (outputDir != null && outputDir.trim().length() > 0) {
            this.outputDir = outputDir.trim();
        }
        this.updateCLOB = updateCLOB;
        this.connection = connection;
    }

    /**
     * process the items.  call this method after construction
     * to make the processing happen.
     */
    public void process() {
        // call start hook on processor
        clobProcessor.startProcessingItems();

        // open connection
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            System.out.println("processing sql:  " + selectSql);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(selectSql);

            // process each item CLOB in results
            while (rs.next()) {

                String itemId = rs.getString(1);
                CLOB clob = ((OracleResultSet) rs).getCLOB(2);

                // check with processor before processing item
                if (clobProcessor.doProcessItem(itemId, clob)) {
                    System.out.println("processing item CLOB (count=" + ++count
                            + ", item_id=" + itemId + ")");

                    // process the clob xml
                    String processedClobText = clobProcessor.processItem(itemId,
                            clob);

                    // log process activity
                    if (outputDir != null) {
                        logProcessing(itemId, clob, processedClobText);
                    }

                    // write processed CLOB back to db
                    if (updateCLOB) {
                        updateClob(itemId, processedClobText);
                    }
                } else {
                    System.out.println("not processing item CLOB (count="
                            + ++count + ", item_id=" + itemId + ")");
                }

            }
            clobProcessor.stopProcessingItems(true);
        } catch (SQLException e) {
            e.printStackTrace();
            clobProcessor.stopProcessingItems(false);
        }
    }

    private void logProcessing(String itemId, CLOB clob, String processedClobText) {
        try {
            OutputStream out = new FileOutputStream(new File(outputDir,
                    itemId + "-pre.xml"));

            new Pipe(clob.binaryStreamValue(), out).run();
            out.close();
        } catch (Exception ex) {
            throw new SystemException("unexpected error logging clob contents",
                    ex);
        }
        try {
            OutputStream out = new FileOutputStream(new File(outputDir,
                    itemId + "-post.xml"));
            InputStream in = new ByteArrayInputStream(processedClobText.getBytes());

            new Pipe(in, out).run();
            out.close();
        } catch (Exception ex) {
            throw new SystemException("unexpected error logging clob contents",
                    ex);
        }
    }

    /**
     * update CLOB contents for an item in the database in its own transaction.
     * it is important that this update execute quickly in its own transaction
     * because the CLOB must be locked in order to do the update.
     */
    private void updateClob(String itemId, String clobText) throws SQLException {



            String sql = SELECT_ALL_ACTIVE_ITEM_MEDIA_XML_SQL
                    + " and item_media.item_id='" + itemId + "' for update";
            ResultSet rs = connection.createStatement().executeQuery(sql);

            if (rs.next()) {
                CLOB clob = ((OracleResultSet) rs).getCLOB(2);

                if (clobText.length() < clob.length()) {
                    clob.trim(clobText.length());
                }
                Writer outstream = clob.getCharacterOutputStream();

                try {
                    outstream.write(clobText.toCharArray());
                    outstream.close();
                } catch (IOException ioEx) {
                    throw new RuntimeException("unexpected errror writing CLOB");
                }
                connection.commit();
            } else {
                if (connection != null) {
                    connection.rollback();
                }
                throw new RuntimeException("item id not found");
            }
    }

}
