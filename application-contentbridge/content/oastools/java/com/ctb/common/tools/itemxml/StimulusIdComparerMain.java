/*
 * Created on Dec 8, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import com.ctb.common.tools.DBConfig;

public class StimulusIdComparerMain {

    static {
        PropertyConfigurator.configure("conf/log4j.properties");
    }

    Set distinctSet;
    StimulusIdComparer comparer;
    double threshold;
    static String environment = "wmli.properties";
    DBStimulusGateway dbsgw = null;
    Connection connection;
    PrintStream out = System.out;
    FileOutputStream logFile = null;
    BufferedOutputStream bufferedLogFile = null;
    PrintStream pLogFile = null;
    public static boolean writeToFile = false;

    /* ************************************************************************/

    public void setUp() throws FileNotFoundException {
        //        if (writeToFile) {
        //            System.setOut(pLogFile);
        //            logFile = new FileOutputStream("report.out");
        //            bufferedLogFile = new BufferedOutputStream(logFile);
        //            pLogFile = new PrintStream(bufferedLogFile);
        //        }
        connection =
            new DBConfig(new File(environment)).openNoCommitConnection();
        dbsgw = new DBStimulusGateway(connection, environment);
    }

    public void tearDown() throws SQLException {
        out.flush();
        this.connection.close();

    }

    public static void main(String[] argv) throws Exception {
        if (argv.length > 0)
            environment = argv[0];
        writeToFile = true;
        StimulusIdComparerMain comparerMain = new StimulusIdComparerMain();
        comparerMain.setUp();
        comparerMain.out.println(
            "\n*********** START OF REPORT ******************************\n");
        comparerMain.out.println(new Date() + ": Using: " + environment);
        // comparerMain.testFromDatabaseWithGroups();
        comparerMain.testAllStimuli();
        comparerMain.out.println(
            "\n*********** END OF REPORT ********************************\n");
        comparerMain.tearDown();
    }

    /* ************************************************************************/

    private void testAllStimuli() throws Exception {
        Set stimulusIds = getAllStimulusIds();
        Map identicalStimuli =
            returnIdenticalNonTrivialStimuli(stimulusIds, connection);

        if (identicalStimuli.size() > 0) {
            out.println(new Date() + " IDENTICAL STIMULI:\n");
            listMap(identicalStimuli);
        }
    }

    private void listMap(Map identicalStimuli) {
        int i = 0;
		out.println("________________________________________________________________\n");
		
        for (Iterator iter = identicalStimuli.keySet().iterator();
            iter.hasNext();
            i++) {
            String stimulus = (String) iter.next();
            Set stimulusIds = (Set) identicalStimuli.get(stimulus);

            Set wordlikeStimulusIds =
                StimulusIdUtils.filterByQualification(stimulusIds, true);
            Set codelikeStimulusIds =
                StimulusIdUtils.filterByQualification(stimulusIds, false);

            StringBuffer buf = new StringBuffer();

            if (stimulusIds.size() > 0) {
                buf.append(
                    StringUtils.leftPad(i + ".", 5)
                        + "STIMULUS: "
                        + stimulus
                        + "\n     STIMULUS IDs:");
            }

            if (wordlikeStimulusIds.size() > 0)
                buf.append("\t" + wordlikeStimulusIds.toString());
            if (codelikeStimulusIds.size() > 0)
                buf.append(
                    "\n                  \t" + codelikeStimulusIds.toString());

            out.println(buf.toString());

            if (stimulusIds.size() > 0) {
				out.println();
                out.println(
                    StringUtils.rightPad("    Stimulus Id", 40) + "Item Id");
				out.println("    ____________________________________________________________");
                Map items = getItemIdsInMatchingGroup(stimulusIds);
                listItems(items);
				
            }
            
			out.println("________________________________________________________________");
        }
    }

    /* ************************************************************************/

    public void testFromDatabaseWithGroups() throws Exception {
        threshold = 0.75;
        Set stimulusIds = getAllStimulusIds();

        Set stimulusIdsForSimilarity =
            StimulusIdUtils.filterByQualification(stimulusIds, true);
        out.println(stimulusIdsForSimilarity.size() + " for similarity groups");

        Set stimulusIdsForNeighbourhood =
            StimulusIdUtils.filterByQualification(stimulusIds, false);
        out.println(
            stimulusIdsForNeighbourhood.size() + " for neighbourhood groups");

        processSimilarityGroups(stimulusIdsForSimilarity);
        processSubStringGroups(stimulusIds);
        // processNeighbourhoods(stimulusIdsForNeighbourhood);

    }

    /* ************************************************************************/

    private void processNeighbourhoods(Set stimulusIdsForNeighbourhood)
        throws SQLException {
        out.println(
            "\n*********** NEIGHBOURHOODS **********************************\n");
        Set[] neighbourhoods =
            getNeighbouringGroups(stimulusIdsForNeighbourhood, 1);
        out.println("Found " + neighbourhoods.length + " neighbourhoods:\n");
        printHeader();
        //        for (int i = 0; i < neighbourhoods.length; i++) {
        //            if (neighbourhoods[i].size() > 1) {
        //                Map items = getItemIdsInMatchingGroup(neighbourhoods[i]);
        //                listItems(items);
        //            }
        //        }
        out.println(
            "*********** IDENTICAL STIMULI IN NEIGHBOURHOODS **********\n");
        for (int i = 0; i < neighbourhoods.length; i++) {
            if (neighbourhoods[i].size() > 1) {
                reportIdenticalStimuli(neighbourhoods[i]);
            }
        }
    }

    private Set[] getNeighbouringGroups(Set stimulusIds, int thresholdDistance)
        throws SQLException {
        StimulusIdComparerNeighbourhood stimulusIdComparator =
            new StimulusIdComparerNeighbourhood(stimulusIds, thresholdDistance);
        return stimulusIdComparator.getAllMatchingGroups();
    }

    /* ************************************************************************/

    private void processSubStringGroups(Set stimulusIds) {
        out.println(
            "\n*********** SUBSTRING GROUPS ********************************\n");
        Set[] subStringGroups = getSubStringGroups(stimulusIds);
        out.println("Found " + subStringGroups.length + " substring groups:\n");
        printHeader();
        //        for (int i = 0; i < subStringGroups.length; i++) {
        //            if (subStringGroups[i].size() > 1) {
        //                Map items = getItemIdsInMatchingGroup(subStringGroups[i]);
        //                listItems(items);
        //            }
        //        }
        out.println(
            "*********** IDENTICAL STIMULI IN SUBSTRING GROUPS ********\n");
        for (int i = 0; i < subStringGroups.length; i++) {
            if (subStringGroups[i].size() > 1) {
                reportIdenticalStimuli(subStringGroups[i]);
            }
        }
    }

    private Set[] getSubStringGroups(Set stimulusIds) {
        StimulusIdComparerSubstring stimulusIdComparator =
            new StimulusIdComparerSubstring(stimulusIds);
        return stimulusIdComparator.getAllMatchingGroups();
    }

    /* ************************************************************************/

    private void processSimilarityGroups(Set stimulusIdsForSimilarity)
        throws SQLException {
        out.println(
            "\n*********** SIMILARITY GROUPS *******************************\n");
        Set[] similarGroups =
            getSimilarGroups(threshold, stimulusIdsForSimilarity);
        out.println(
            "Found "
                + similarGroups.length
                + " stimulusId groups with similarity greater than "
                + threshold
                + "\n");
        printHeader();

        //        for (int i = 0; i < similarGroups.length; i++) {
        //            if (similarGroups[i].size() > 1) {
        //                Map items = getItemIdsInMatchingGroup(similarGroups[i]);
        //                                listItems(items);
        //            }
        //        }
        out.println(
            "\n*********** IDENTICAL STIMULI IN SIMILARITY GROUPS **********\n");
        for (int i = 0; i < similarGroups.length; i++) {
            if (similarGroups[i].size() > 1) {
                reportIdenticalStimuli(similarGroups[i]);
            }
        }
    }

    private Set[] getSimilarGroups(double threshold, Set stimulusIds)
        throws SQLException {
        StimulusIdComparerSimilarity stimulusIdComparator =
            new StimulusIdComparerSimilarity(stimulusIds, threshold);
        return stimulusIdComparator.getAllMatchingGroups();
    }

    /* ************************************************************************/

    private Set getAllStimulusIds() throws SQLException {
        out.println(
            new Date()
                + ": Retrieving distinct Stimulus Ids from Item table ...");
        Set stimulusIds = dbsgw.getStimuli();
        out.println(
            new Date() + ": " + stimulusIds.size() + " stimulus Ids found.");
        return stimulusIds;
    }

    private void reportIdenticalStimuli(Set matchingGroup) {
        Map identicalStimuli =
            returnIdenticalNonTrivialStimuli(matchingGroup, this.connection);

        if (identicalStimuli.size() > 0)
            out.println("FOUND:" + identicalStimuli);
    }

    /**
     * For the set of input stimulus Ids, this method compares the stimuli 
     * retrieved from the database to each other. If more than one
     * stimulusId has the same stimulus, that stimulus is returned in a Map along
     * with a Set containing all the corresponding stimulusIds.
     * 
     * <p>The ID attrubute of Stimui retrieved from the database is ignored
     * for the comparison. Also, stimuli that do not have any data in them
     * are discarded from the Map
     * 
     * @param stimulusIds set of similar stimulus Ids
     * @param environment environment for DBConnection
     * @return a Map, each entry is keyed by a Stimulus and contains a Set of stimulusIds
     */

    private Map returnIdenticalNonTrivialStimuli(
        Set stimulusIds,
        Connection connection) {
        out.println(new Date() + ": Retrieving Stimuli from Item Media table");
        Map stimuliMap = dbsgw.getStimuliFromDatabase(stimulusIds);

        out.println(new Date() + ": " + stimuliMap.size() + " stimuli found\n");

        Map m =
            new StimulusComparerAsString(stimuliMap, out)
                .getAllMatchingGroups();
        System.out.println(
            new Date()
                + ": "
                + m.size()
                + " stimuli match multiple Stimulus Ids");
        return m;
    }

    /* ************************************************************************/

    private Map getItemIdsInMatchingGroup(Set group) {
        Map items = new HashMap();
        for (Iterator iter = group.iterator(); iter.hasNext();) {
            String stimulusId = (String) iter.next();

            try {
                items.put(
                    stimulusId,
                    dbsgw.getItemsAndStatusForStimulusId(stimulusId));
            } catch (SQLException e) {

            }
        }
        return items;
    }

    private void listItems(Map items) {
        for (Iterator iter = items.keySet().iterator(); iter.hasNext();) {
            String stimulusId = (String) iter.next();
            StringBuffer buf = new StringBuffer();
            buf.append("\t");
            buf.append(StringUtils.rightPad(stimulusId, 40));
            Map itemAndStatus = (Map) items.get(stimulusId);
            for (Iterator iterator = itemAndStatus.keySet().iterator();
                iterator.hasNext();
                ) {
                String itemId = (String) iterator.next();
                buf.append(
                    "[" + itemId + "," + itemAndStatus.get(itemId) + "] ");
            }
            out.println(buf.toString());
        }
    }

    /* ************************************************************************/

    private void printHeader() {
        //        out.println(
        //            StringUtils.rightPad("STIMULUS ID", 40)
        //                + " [ITEM ID, ACTIVATION STATUS] combinations");
    }

}
