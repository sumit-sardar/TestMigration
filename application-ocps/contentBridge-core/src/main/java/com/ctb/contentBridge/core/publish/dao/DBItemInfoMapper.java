package com.ctb.contentBridge.core.publish.dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.tools.ItemInfo;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 7, 2003
 * Time: 8:27:34 AM
 * To change this template use Options | File Templates.
 */
public class DBItemInfoMapper implements DBMapper {

    private List itemInfoList = new ArrayList();
    public static final String SR_ITEM_INFO_SELECT_CLAUSE = "select item.item_id, item.item_display_name," +
                                 " item.activation_status, item_set_item.ibs_invisible,item_set.ext_cms_item_set_id," +
                                 "item.correct_answer,datapoint.min_points || ':' || datapoint.max_points " +
                                 "from item,item_set,datapoint";

    public static final String CR_ITEM_INFO_SELECT_CLAUSE = "select item.item_id, item.item_display_name," +
                                 " item.activation_status, item_set_item.ibs_invisible,item_set.ext_cms_item_set_id," +
                                 "item.correct_answer " +
                                 "from item,item_set";

    public ArgumentsSQLPair[] getArgumentsAndSQLForUpdate(Object persistentObject) {
        return null;
    }

    public void clear() {
        itemInfoList.clear();
    }
    public void assembleObjectFromSQLResult(ResultSet rs) {

        try {
            String ak = getAnswerKey(rs);
            itemInfoList.add(new ItemInfo(rs.getString(1),rs.getString(2),rs.getString(3),
                            rs.getString(4),rs.getString(5),ak));
        } catch (SQLException e) {
            throw new SystemException(e.getMessage());
        }
    }

    public ItemInfo[] getItemInformation() {
        ItemInfo[] infos = (ItemInfo[]) itemInfoList.toArray(new ItemInfo[itemInfoList.size()]);
        clear();
        return infos;
    }

    public Object[] getResults() {
        return getItemInformation();
    }
    public String getSelectClause() {
        return SR_ITEM_INFO_SELECT_CLAUSE;
    }
    public String getAnswerKey(ResultSet rs) throws SQLException {

        if (rs.getString(6) == null || rs.getString(6).equalsIgnoreCase("null"))
            return rs.getString(7);
        return rs.getString(6);
    }

    public void assembleObjectFromMultipleRows(ResultSet rs) {
        throw new BusinessException("ItemInfo objects are assembled from single rows. Use assmemble" +
                "ObjectFromResultSet method");

    }
    
    
	public boolean isObjectAssembleFromMultipleRows() {
		return false;
	}
	
	
	public String[] getKeyColumns() {
		return null;
	}
}
