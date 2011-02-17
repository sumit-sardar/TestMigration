/**
 * Copyright CTB/McGraw-Hill, 2004
 * CONFIDENTIAL
 *
 * @author <a href="mailto:earl_waller@ctb.com">Earl Waller</a>
 * @version $Revision$
 * $Id: SwfExtracter.java
 */
package com.ctb.common.tools.oneoff;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.ctb.common.tools.*;
import com.ctb.common.tools.media.MediaType;
import com.ctb.hibernate.HibernateSession;
import com.ctb.hibernate.HibernateUtils;
import com.ctb.hibernate.persist.ItemMediaCompositeId;
import com.ctb.hibernate.persist.ItemMediaRecord;
import com.ctb.util.Pipe;

public class SwfExtractor {

    private Session session;
	private static SimpleDateFormat dateformat;
	private static final String DATETIME_FORMAT_PATTERN = "MM/dd/yyyy h:mm a z";
	private static final String CMD_EXTRACT = "extract";
    private static final String PARAM_ENV = "env";
    private static final String PARAM_TARGETDIR = "targetdir";
	private static final String SQL_ITEMS =
		"SELECT Item_Media.Item_Id " +
		"FROM Item " +
		"WHERE Item.Item_Id = Item.Item_Display_Name";

    public SwfExtractor(Session session) {
        this.session = session;
    }

    /**
     * find all unique items and extract the 'AKSWF' type blob from Item_Media
     * table for each item and write it to a file named '<item id>.swf' in the
     * target directory.
     *
     * @param conn database connection
     * @param targetDirectory location the output files will be written into
     */
    void extractSwf(Connection conn, String targetDirectory) {
		Timestamp start = null;
		Timestamp end = null;
		Connection c = conn;
		PreparedStatement statement = null;
		ResultSet result = null;
		String itemId = null;
		String fileName = null;
		int rows = 0;

		start = new Timestamp(System.currentTimeMillis());
        System.out.println(dateformat.format(start) + " Begin extraction of swf files.");

		try {
			statement = c.prepareStatement(SQL_ITEMS);
			result = statement.executeQuery();
			while (result.next()) {
				itemId = result.getString(1);
				fileName = itemId + ".swf";
				WriteMediaToStream(getItemMediaRecord(itemId),
								   new FileOutputStream(targetDirectory + fileName));
				rows++;
				System.out.println("\tfile created:  " + fileName);
			}
			result.close();
			statement.close();
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}

        try {
            session.flush();
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
		end = new Timestamp(System.currentTimeMillis());
        System.out.println(dateformat.format(end) +
                           " Extraction completed: " +
						   rows + " files extracted");
    } // void extractSwf(Connection conn, String targetDirectory)

	/**
	 * Retrieve the SWF media for an item
	 * 
	 * @param string item ID
	 */
	public ItemMediaRecord getItemMediaRecord(String itemId) {
		ItemMediaCompositeId id = new ItemMediaCompositeId();
		id.setItemId(itemId);
		id.setMediaType(MediaType.FLASH_ANSWER_MEDIA_TYPE.getMediaType());
	
		try {
			return (ItemMediaRecord) session.get(ItemMediaRecord.class, id);
		} catch (HibernateException e) {
			throw new SystemException(e.getMessage(),e);
		}
	} // public ItemMediaRecord getItemMediaRecord(String itemId)

	/**
	 * Output the media to the stream
	 * 
	 * @param media 
	 * @param bos
	 */
	public void WriteMediaToStream(ItemMediaRecord media, OutputStream bos) {
		ByteArrayInputStream bis = new ByteArrayInputStream(media.getMediaBlob());
		Pipe pipe = new Pipe(bis, bos);
		pipe.run();
		try {
			bos.close();
			bis.close();
		} catch (IOException e) {
		}
	} // public void WriteMediaToStream(ItemMediaRecord media, OutputStream bos)

    public static void main(String[] args) {
        CommandLine cmdLine = null;
		dateformat = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
		Timestamp now = new Timestamp(System.currentTimeMillis());

        try {
            cmdLine = new CommandLine(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            CommandLine.usage();
            System.exit(1);
        }

		String cmd = cmdLine.getCommand();
		if (!cmd.equals(CMD_EXTRACT)) {
			System.out.println("Invalid command: " + cmd);
			System.exit(1);
		}
		System.out.println(dateformat.format(now) + " " + cmd);
		

        File envFile = new File(cmdLine.getParameterValue(PARAM_ENV) + ".properties");
        String targetDirectory = cmdLine.getParameterValue(PARAM_TARGETDIR);

        Connection conn = null;

        try {
            conn = new DBConfig(envFile).openConnection();
            SwfExtractor extracter = new SwfExtractor(HibernateUtils.getSession(conn));

			extracter.extractSwf(conn, targetDirectory);
        } catch (Exception ex) {
            System.out.println("Error:  " + ex.getMessage());
        } finally {
			try {
				conn.rollback();
			} catch (SQLException sqlEx) {
				System.out.println("Warning!  SQLException while rolling back transaction:");
				sqlEx.printStackTrace();
			}
			HibernateSession.closeSession();
            try {
                conn.close();
            } catch (SQLException sqlEx) {
                System.out.println("Warning!  SQLException while closing connection:");
                sqlEx.printStackTrace();
            }
        }
    } // public static void main(String[] args)
} // public class SwfExtracter
