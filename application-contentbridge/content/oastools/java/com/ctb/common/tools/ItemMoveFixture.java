package com.ctb.common.tools;

import net.sf.hibernate.Session;

public class ItemMoveFixture {
    public static final String FRAMEWORK_CODE = "CTB";
    public static final String ITEM_ID_NON_EXISTENT = "lsdfjks";
    public static final String EXT_CMS_ITEM_SET_ID_NON_EXISTENT = "alksfdjjsaf";
    public static final String ITEM_ID_ACTIVE_SR = "4R.1.1.1.02";
    public static final String ITEM_ID_ACTIVE_CR = "4R.2.1.1.05";
    public static final String ITEM_ID_INACTIVE = "4R.1.1.1.06";
    public static final String EXT_CMS_ITEM_SET_ID_ACTIVE_1 = "4R.1.1.1";
    public static final String EXT_CMS_ITEM_SET_ID_ACTIVE_2 = "4R.1.1.2";
    public static final String EXT_CMS_ITEM_SET_ID_ACTIVE_3 = "4R.2.1.1";
    public static final String EXT_CMS_ITEM_SET_ID_INACTIVE = "4R.1.1.3";

    public DBItemGateway igw = null;
    public DBObjectivesGateway ogw = null;
    public DBDatapointGateway dgw = null;

    public ItemMoveFixture(Session session) {
        igw = new DBItemGateway(session);
        ogw = new DBObjectivesGateway(session);
        dgw = new DBDatapointGateway(session);
        validate();
    }

    private void validate() {
        try {
            if (igw
                .itemExistsActiveOrInactive(
                    ItemMoveFixture.ITEM_ID_NON_EXISTENT)) {
                throw new SystemException(
                    "test did not expect existence of item "
                        + ItemMoveFixture.ITEM_ID_NON_EXISTENT);
            }
            if (ogw
                .objectiveExistsWithinFramework(
                    ItemMoveFixture.EXT_CMS_ITEM_SET_ID_NON_EXISTENT,
                    FRAMEWORK_CODE)) {
                throw new SystemException(
                    " test did not expect existence of item set "
                        + ItemMoveFixture.EXT_CMS_ITEM_SET_ID_NON_EXISTENT
                        + " within CTB framework");
            }
            igw.inactivateItem(ItemMoveFixture.ITEM_ID_INACTIVE);
            if (!igw
                .isItemProperlyLinked(
                    ItemMoveFixture.ITEM_ID_ACTIVE_SR,
                    ItemMoveFixture.EXT_CMS_ITEM_SET_ID_ACTIVE_1,
                    FRAMEWORK_CODE)) {
                throw new SystemException(
                    "test expects item "
                        + ItemMoveFixture.ITEM_ID_ACTIVE_SR
                        + " to be linked to objective "
                        + ItemMoveFixture.EXT_CMS_ITEM_SET_ID_ACTIVE_1);
            }
            if (igw
                .isItemProperlyLinked(
                    ItemMoveFixture.ITEM_ID_ACTIVE_SR,
                    ItemMoveFixture.EXT_CMS_ITEM_SET_ID_ACTIVE_2,
                    FRAMEWORK_CODE)) {
                throw new SystemException(
                    "test does not expect item "
                        + ItemMoveFixture.ITEM_ID_ACTIVE_SR
                        + " to be linked to objective "
                        + ItemMoveFixture.EXT_CMS_ITEM_SET_ID_ACTIVE_2
                        + ".  item likely linked to two objectives.  "
                        + " as this should never happen, your test data is likely corrupt");
            }
            if (!igw
                .isItemProperlyLinked(
                    ItemMoveFixture.ITEM_ID_ACTIVE_CR,
                    ItemMoveFixture.EXT_CMS_ITEM_SET_ID_ACTIVE_3,
                    FRAMEWORK_CODE)) {
                throw new SystemException(
                    "test expects item "
                        + ItemMoveFixture.ITEM_ID_ACTIVE_CR
                        + " to be linked to objective "
                        + ItemMoveFixture.EXT_CMS_ITEM_SET_ID_ACTIVE_3);
            }
            if (igw
                .isItemProperlyLinked(
                    ItemMoveFixture.ITEM_ID_ACTIVE_CR,
                    ItemMoveFixture.EXT_CMS_ITEM_SET_ID_ACTIVE_2,
                    FRAMEWORK_CODE)) {
                throw new SystemException(
                    "test does not expect item "
                        + ItemMoveFixture.ITEM_ID_ACTIVE_CR
                        + " to be linked to objective "
                        + ItemMoveFixture.EXT_CMS_ITEM_SET_ID_ACTIVE_2
                        + ".  item likely linked to two objectives.  "
                        + " as this should never happen, your test data is likely corrupt");
            }
            long inactiveItemSetId =
                ogw.getItemSetIdFromObjective(
                    ItemMoveFixture.EXT_CMS_ITEM_SET_ID_INACTIVE,
                    FRAMEWORK_CODE);

            if (!ogw
                .testGetItemSetActivationStatus(inactiveItemSetId)
                .equals(OASConstants.ITEM_SET_STATUS_INACTIVE)) {
                throw new SystemException(
                    "test expectes item set "
                        + ItemMoveFixture.EXT_CMS_ITEM_SET_ID_INACTIVE
                        + " to be inactive");
            }
        } catch (Exception ex) {
            throw new SystemException(
                "unexpected error setting up test fixture",
                ex);
        }
    }
}
