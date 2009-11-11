package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.util.List;

import com.ctb.lexington.data.ItemSetVO;

public class ItemSetMapper extends AbstractDBMapper {
    public static final String FIND_SUBTEST = "findSubtest";
    public static final String FIND_SUBTESTS_BY_TEST_ROSTER = "findSubtestsByTestRoster";

    /**
     * @param conn
     */
    public ItemSetMapper(final Connection conn) {
        super(conn);
    }

    public ItemSetVO find(final Long itemSetId) {
        return (ItemSetVO) find(FIND_SUBTEST, itemSetId);
    }

    public List findSubtestsByTestRoster(final Long testRosterId) {
        return findMany(FIND_SUBTESTS_BY_TEST_ROSTER, testRosterId);
    }
}
