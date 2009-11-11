package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.util.ArrayList;

import com.ctb.lexington.data.DatapointVO;

public class DatapointMapper extends AbstractDBMapper {
    public static final String FIND_NAME = "findDatapoint";
    public static final String FIND_DATAPOINT_BY_ITEM_SET_ID_NAME = "findDatapointByItemSetId";

    /**
     * @param conn
     */
    public DatapointMapper(Connection conn) {
        super(conn);
    }

    public DatapointVO find(Integer datapointId) {
        DatapointVO template = new DatapointVO();
        template.setDatapointId(datapointId);
        return (DatapointVO) find(FIND_NAME, template);
    }

    public ArrayList findDatapointByItemSetId(Integer itemSetId) {
        DatapointVO template = new DatapointVO();
        template.setItemSetId(itemSetId);
        return (ArrayList) findMany(FIND_DATAPOINT_BY_ITEM_SET_ID_NAME, template);
    }
}