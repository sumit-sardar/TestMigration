package com.ctb.common.tools;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 7, 2003
 * Time: 8:27:34 AM
 * To change this template use Options | File Templates.
 */
public class DBObjectiveInfoMapper implements DBMapper {

    private List objectiveInfoList = new ArrayList();
    public static final String OBJECTIVE_INFO_SELECT_CLAUSE = "select item_set.item_set_id, " +
                                "item_set.ext_cms_item_set_id, " +
                                "item_set_category.framework_product_id, " +
                                "item_set.activation_status " +
                                "from item_set, item_set_category";

    public ArgumentsSQLPair[] getArgumentsAndSQLForUpdate(Object persistentObject) {
        return null;
    }

    public void clear() {
        objectiveInfoList.clear();
    }
    public void assembleObjectFromSQLResult(ResultSet rs) {

        try {
            objectiveInfoList.add(new ObjectiveInfo(rs.getLong(1),rs.getString(2),rs.getLong(3),
                            rs.getString(4)));
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }

    public ObjectiveInfo[] getObjectiveInformation() {
        ObjectiveInfo[] infos = (ObjectiveInfo[]) objectiveInfoList.toArray(new ObjectiveInfo[objectiveInfoList.size()]);
        clear();
        return infos;
    }

    public Object[] getResults() {
        return getObjectiveInformation();
    }
    public String getSelectClause() {
        return OBJECTIVE_INFO_SELECT_CLAUSE;
    }

    public void assembleObjectFromMultipleRows(ResultSet rs) {
        throw new SystemException("ObjectiveInfo objects are assembled from single rows. Use assmemble" +
                "ObjectFromResultSet method");

    }
    
	public boolean isObjectAssembleFromMultipleRows() {
		return false;
	}
	
	public String[] getKeyColumns() {
		return null;
	}

}
