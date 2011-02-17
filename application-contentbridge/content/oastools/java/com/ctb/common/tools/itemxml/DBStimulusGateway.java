/*
 * Created on Dec 9, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.io.CharArrayReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.ctb.common.tools.AbstractDBGateway;
import com.ctb.common.tools.DBConnection;
import com.ctb.common.tools.SystemException;

public class DBStimulusGateway extends AbstractDBGateway {
    DBConnection conn = null;
    StimulusCache cache = null;

    public DBStimulusGateway(Connection connection, String environment) {
        super(connection);
        conn = new DBConnection(connection);
        cache = new StimulusCache(environment);
    }

    public Map getItemsAndStatusForStimulusId(String stimulusId)
        throws SQLException {
        ResultSet rs = null;
        try {

            Map itemIds = new HashMap();
            rs =
                conn.executeQuery(
                    "SELECT ITEM_ID, ACTIVATION_STATUS FROM ITEM where EXT_STIMULUS_ID = '{0}'",
                    new Object[] { stimulusId });
            while (rs.next()) {
                String s = rs.getString(1);
                String as = rs.getString(2);
                if (s != null)
                    itemIds.put(s, as);
            }
            return itemIds;
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        } finally {
            DBConnection.safeClose(rs.getStatement(), rs);
        }
    }

    public Set getStimuli() throws SQLException {
        ResultSet rs = null;
        try {
            Set stimulusIds = new HashSet();
            rs =
                conn.executeQuery(
                    "SELECT DISTINCT EXT_STIMULUS_ID FROM ITEM where item_id = item_display_name",
                    null);
            while (rs.next()) {
                String s = rs.getString(1);
                if (s != null)
                    stimulusIds.add(s);
            }
            return stimulusIds;
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        } finally {
            DBConnection.safeClose(rs.getStatement(), rs);
        }
    }

    public Map getStimuli(String[] stimulusIds) {
        Map stimuli = new HashMap();
        for (int i = 0; i < stimulusIds.length; i++) {
            String stimulusId = stimulusIds[i];
            Element stimulus = null;

            char[] media_clob_from_Cache = cache.get(stimulusId);
            if (media_clob_from_Cache != null)
                stimulus = getElementFromCharArray(media_clob_from_Cache);

            if (stimulus == null)
                stimulus = getStimulusIdFromDB(stimulusId);

            if (stimulus != null)
                stimuli.put(stimulusId, stimulus);
        }
        return stimuli;

    }

    /**
     * @return stimulus element from datbase. null if item was imported without media
     */
    private Element getStimulusIdFromDB(String stimulusId) {
        Element stimulus = null;
        char[] media_clob;
        try {
            media_clob = getMediaClob(stimulusId);
            if (media_clob == null)
                return null;

            // if (media_clob == null) the item was imported without media
            stimulus =
                StimulusIdUtils.getStimulus(
                    getElementFromCharArray(media_clob));
            cache.put(
                stimulusId,
                new XMLOutputter().outputString(stimulus).toCharArray());
        } catch (Exception e) {
        }

        return stimulus;
    }

    private char[] getMediaClob(String stimulusId) throws SQLException {
        char[] media_clob = null;
        ResultSet rs = null;
        try {
            rs =
                conn.executeQuery(
                    "SELECT ITEM_ID FROM ITEM where EXT_STIMULUS_ID = '{0}'",
                    new Object[] { stimulusId });
            while (rs.next()) {
                String itemId = rs.getString(1);

                media_clob =
                    conn.readClob(
                        "SELECT ITEM_ID, MEDIA_CLOB FROM ITEM_MEDIA where ITEM_ID = '"
                            + DBConnection.escapeForSql(itemId)
                            + "' and MEDIA_TYPE = 'IBXML'");
                if (media_clob != null)
                    break;
            }
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        } finally {
            DBConnection.safeClose(rs.getStatement(), rs);
        }
        return media_clob;
    }

    /**
     * Retrieves Stimuli from a database
     */
    Map getStimuliFromDatabase(Set stimulusIds) {
        Map stimuliMap = null;
        try {
            stimuliMap =
                getStimuli(
                    (String[]) stimulusIds.toArray(
                        new String[stimulusIds.size()]));
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
        return stimuliMap;
    }

    Element getElementFromCharArray(char[] media_clob) {
        Element stimulus = null;
        try {
            stimulus =
                (new SAXBuilder())
                    .build(new CharArrayReader(media_clob))
                    .getRootElement();
        } catch (Exception e) {
        }
        return stimulus;
    }

}
