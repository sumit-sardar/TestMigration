package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.util.ArrayList;

import com.ctb.lexington.data.ConditionCodeVO;
import com.ctb.lexington.db.record.Persistent;

public class ConditionCodeMapper extends AbstractDBMapper {
    public static final String FIND_NAME = "findConditionCode";
    public static final String FIND_BY_ITEM_SET_ID_NAME = "findConditionCodeByItemSetId";
    public static final String FIND_ALL_NAME = "findAll";

    /**
     * @param conn
     */
    public ConditionCodeMapper(Connection conn) {
        super(conn);
    }

    public ConditionCodeVO find(Integer conditionCodeId) {
        ConditionCodeVO template = new ConditionCodeVO();
        template.setConditionCodeId(conditionCodeId);
        return (ConditionCodeVO) find(FIND_NAME, template);
    }
    
    public ArrayList findAll() {
        return (ArrayList) findMany(FIND_ALL_NAME, (Persistent) null);
    }
}