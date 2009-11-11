package com.ctb.lexington.data;

import com.ctb.lexington.db.mapper.ConditionCodeMapper;
import com.ctb.lexington.db.utils.DataSourceFactory;
import com.ctb.lexington.util.SQLUtil;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConditionCodes {
    protected Map conditionCodesByCode = new HashMap();
    protected Map conditionCodesById = new HashMap();
    private static ConditionCodes instance;

    private ConditionCodes() {
        Connection connection = null;
        try {
            connection = DataSourceFactory.getInstance()
                    .getDataSource(DataSourceFactory.OASDATASOURCE).getConnection();
            Collection codes = new ConditionCodeMapper(connection).findAll();

            for (Iterator iter = codes.iterator(); iter.hasNext();) {
                ConditionCodeVO conditionCode = (ConditionCodeVO) iter.next();

                conditionCodesByCode.put(conditionCode.getConditionCode(), conditionCode);
                conditionCodesById.put(conditionCode.getConditionCodeId(), conditionCode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            SQLUtil.close(connection);
        }
    }

    public static ConditionCodes getInstance() {
        if (instance == null) {
            instance = new ConditionCodes();
        }
        return instance;
    }

    /**
     * Checks if a given item is attempted by examining the condition codes for the item, the
     * response code from the student, and the condition code id from the student.
     *
     * We need to filter by IDs because a response value might be valid for some items, but not
     * valid for others. We're only interested in response codes that are valid for a particular
     * subset of ids.
     */
    public boolean isAttempted(int[] subsetOfIds, String responseCode, Integer conditionCodeId) {
        final Map subset = getSubset(subsetOfIds);
        final ConditionCodeVO codeByResponse = (ConditionCodeVO) subset.get(responseCode);
        if (codeByResponse != null)
            return codeByResponse.isAttempted();

        final ConditionCodeVO codeById = (ConditionCodeVO) conditionCodesById.get(conditionCodeId);
        if(codeById != null)
            return codeById.isAttempted();

        return true;
    }

    public boolean isConditionCode(int[] subsetOfIds, String responseCode) {
        if (responseCode == null)
            return false;
        Map subset = getSubset(subsetOfIds);
        return subset.containsKey(responseCode);
    }

    /**
     * @param conditionCodeIds
     * @return
     */
    private Map getSubset(int[] conditionCodeIds) {
        Map subset = new HashMap();

        for (int i = 0; i < conditionCodeIds.length; i++) {
            int conditionCodeId = conditionCodeIds[i];

            ConditionCodeVO conditionCode = (ConditionCodeVO) conditionCodesById.get(new Integer(conditionCodeId));
            subset.put(conditionCode.getConditionCode(), conditionCode);
        }

        return subset;
    }
}