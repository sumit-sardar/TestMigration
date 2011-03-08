package com.ctb.mapping;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.ctb.common.tools.*;
import com.ctb.hibernate.HibernateSession;
import com.ctb.hibernate.HibernateUtils;
import com.ctb.xmlProcessing.item.*;

public class ItemMover {

    private Session session;
    private static final String CMD_MOVEITEM = "move";
    private static final String CMD_MOVEITEMVALIDATE = "validate";
    private static final String PARAM_ENV = "env";
    private static final String PARAM_ITEM = "item";
    private static final String PARAM_SOURCEOBJ = "sourceobj";
    private static final String PARAM_TARGETOBJ = "targetobj";
    private static final String PARAM_FRAMEWORK = "framework";

    public ItemMover(Session session) {
        this.session = session;
    }

    /**
     * move an item from one objective to another.  see <code>ItemTestCase</code>
     * and validateMove() for rules and assumptions around legal item moves.
     *
     * @param item ITEM.ITEM_ID for item to be moved
     * @param sourceObjective ITEM_SET.EXT_CMS_ITEM_SET_ID for item's current objective
     * @param targetObjective ITEM_SET.EXT_CMS_ITEM_SET_ID for item's new objective
     */
    public void moveItem(
        String item,
        String frameworkCode,
        String sourceObjective,
        String targetObjective) {
        DBObjectivesGateway og = new DBObjectivesGateway(session);
        long sourceItemSetId =
            og.getItemSetIdFromObjective(sourceObjective, frameworkCode);
        long targetItemSetid =
            og.getItemSetIdFromObjective(targetObjective, frameworkCode);
       List targetItemSetidList = new ArrayList();
       targetItemSetidList.add(new Long(targetItemSetid));
        validateMove(
            item,
            frameworkCode,
            sourceObjective,
            sourceItemSetId,
            targetObjective);
        DBItemMoveGateway img = new DBItemMoveGateway(session);

        img.updateItemSetItem(
            item,
            sourceItemSetId,
            targetItemSetid,
            frameworkCode);
        System.out.println("ITEM_SET_ITEM row updated ...");
        if (og.activateAllParentObjectives(targetItemSetidList) > 0) { //SPRINT 10: TO SUPPORT MAPPING AN ITEM TO MULTIPLE OBJECTIVE
            System.out.println("Target objective activated ...");
        }
        DBItemGateway itemGW = new DBItemGateway(session);
        String itemType = itemGW.getItemTypeCode(item);

        if (itemType.equals(Item.CONSTRUCTED_RESPONSE)) {
            img.updateDatapoint(
                item,
                sourceItemSetId,
                targetItemSetid,
                frameworkCode);
            System.out.println("DATAPOINT row updated ...");
        } else {
            if (img
                .updateDatapointIfExists(
                    item,
                    sourceItemSetId,
                    targetItemSetid,
                    frameworkCode)
                == 1) {
                System.out.println("DATAPOINT row updated ...");
            }
        }
        try {
            session.flush();
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
        System.out.println("Item move completed.");
        printMoveSummary(item, frameworkCode, sourceObjective, targetObjective);

        // call pruner to deactivate objectives that are not associated with another items.
        pruneObjectives(frameworkCode);

    }

    private void pruneObjectives(String frameworkCode) {
        try {
            DBProductGateway pGateway =
                new DBProductGateway(session.connection());
            Product product =
                pGateway.findRootProductByFrameworkCode(frameworkCode);

            Pruner pruner = new Pruner(session.connection());
            pruner.prune(product);

        } catch (HibernateException e1) {
            throw new SystemException(e1.getMessage(), e1);
        }
    }

    public void validateMove(
        String item,
        String frameworkCode,
        String sourceObjective,
        String targetObjective) {
        try {
            DBObjectivesGateway og = new DBObjectivesGateway(session);
            long sourceItemSetId =
                og.getItemSetIdFromObjective(sourceObjective, frameworkCode);

            validateMove(
                item,
                frameworkCode,
                sourceObjective,
                sourceItemSetId,
                targetObjective);
            System.out.println("Item move validated.");
            printMoveSummary(
                item,
                frameworkCode,
                sourceObjective,
                targetObjective);
        } catch (RuntimeException e) {
            System.out.println("Item move validation failed.");
            throw e;
        }
    }

    private void validateMove(
        String item,
        String frameworkCode,
        String sourceObjective,
        long sourceItemSetId,
        String targetObjective) {

        DBItemGateway itemGW = new DBItemGateway(session);
        DBObjectivesGateway objectivesGW = new DBObjectivesGateway(session);
        DBDatapointGateway dpGW = new DBDatapointGateway(session);

        if (!itemGW.activeItemExists(item)) {
            failValidation("item " + item + " does not exist or is inactive");
        }
        String itemType = itemGW.getItemTypeCode(item);

        if (!itemType.equals(Item.CONSTRUCTED_RESPONSE)
            && !itemType.equals(Item.SELECTED_RESPONSE)) {
            failValidation(
                "item " + item + " must be of type CR or SR to be moved.");
        }
        if (itemType.equals(Item.CONSTRUCTED_RESPONSE)
            && !dpGW.datapointExists(item, sourceItemSetId)) {
            failValidation(
                "item "
                    + item
                    + " is of type CR and does not have a datapoint in "
                    + sourceObjective
                    + " to be moved");
        }
        if (!objectivesGW
            .objectiveExistsWithinFramework(sourceObjective, frameworkCode)) {
            failValidation(
                "objective "
                    + sourceObjective
                    + " does not exist in framework "
                    + frameworkCode);
        }
        if (!objectivesGW
            .objectiveExistsWithinFramework(targetObjective, frameworkCode)) {
            failValidation(
                "objective "
                    + targetObjective
                    + " does not exist in framework "
                    + frameworkCode);
        }
        if (!itemGW
            .isItemProperlyLinked(item, sourceObjective, frameworkCode)) {
            failValidation(
                "item "
                    + item
                    + " not currently linked to source objective "
                    + sourceObjective);
        }

    }

    private void failValidation(String msg) {
        throw new SystemException(msg);
    }

    private void printMoveSummary(
        String item,
        String frameworkCode,
        String sourceObj,
        String targetObj) {
        System.out.println("Item move summary:");
        System.out.println("\titem:  " + item);
        System.out.println("\tframework:  " + frameworkCode);
        System.out.println("\tsource objective:  " + sourceObj);
        System.out.println("\ttarget objective:  " + targetObj);
        System.out.println();
    }

    public static void main(String[] args) {
        CommandLine cmdLine = null;

        try {
            cmdLine = new CommandLine(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            CommandLine.usage();
            System.exit(1);
        }

        String cmd = cmdLine.getCommand();

        if (!cmd.equals(CMD_MOVEITEM) && !cmd.equals(CMD_MOVEITEMVALIDATE)) {
            System.out.println("Invalid command: " + cmd);
            System.exit(1);
        }

        File envFile =
            new File(cmdLine.getParameterValue(PARAM_ENV) + ".properties");
        String item = cmdLine.getParameterValue(PARAM_ITEM);
        String sourceObjective = cmdLine.getParameterValue(PARAM_SOURCEOBJ);
        String targetObjective = cmdLine.getParameterValue(PARAM_TARGETOBJ);
        String framework = cmdLine.getParameterValue(PARAM_FRAMEWORK);

        Connection conn = null;

        try {
            conn = new DBConfig(envFile).openConnection();
            ItemMover mover = new ItemMover(HibernateUtils.getSession(conn));

            if (cmd.equals(CMD_MOVEITEM)) {
                mover.moveItem(
                    item,
                    framework,
                    sourceObjective,
                    targetObjective);
                conn.commit();
            } else {
                mover.validateMove(
                    item,
                    framework,
                    sourceObjective,
                    targetObjective);
            }
        } catch (Exception ex) {
            if (cmd.equals(CMD_MOVEITEM)) {
                System.out.println(
                    "Unexpected error processing item move.  Transaction will roll back.  ");
            }
            System.out.println("Error:  " + ex.getMessage());
            try {
                conn.rollback();
            } catch (SQLException sqlEx) {
                System.out.println(
                    "Warning!  SQLException while rolling back transaction:");
                sqlEx.printStackTrace();
            }
        } finally {
            try {
                HibernateSession.closeSession();
                conn.close();
            } catch (SQLException sqlEx) {
                System.out.println(
                    "Warning!  SQLException while closing connection:");
                sqlEx.printStackTrace();
            }
        }
    }
}
