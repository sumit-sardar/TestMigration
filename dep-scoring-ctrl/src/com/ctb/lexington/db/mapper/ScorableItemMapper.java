package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.util.Collection;

import com.ctb.lexington.db.record.ScorableItemRecord;

public class ScorableItemMapper extends AbstractDBMapper {
    private static final String EXISTS_NAME = "scorableItemExists";
    private static final String FIND_MANY_BY_ITEM_SET_ID = "findScorableItemByItemSetId";

    /**
     * @param conn
     */
    public ScorableItemMapper(Connection conn) {
        super(conn);
    }

    public boolean exists(String scoreTypeCode, Long itemSetId, String itemId) {
        ScorableItemRecord template = new ScorableItemRecord();
        template.setScoreTypeCode(scoreTypeCode);
        template.setItemSetId(itemSetId);
        template.setItemId(itemId);
        return exists(EXISTS_NAME, template);
    }
    
    public Collection findScorableItemsByItemSetId(Long itemSetId) {
        return findMany(FIND_MANY_BY_ITEM_SET_ID, new ScorableItemRecord(null, itemSetId, null));
    }
}