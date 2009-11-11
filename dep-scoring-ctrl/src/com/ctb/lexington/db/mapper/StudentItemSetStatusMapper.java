package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.data.StudentItemSetStatusRecord;

public class StudentItemSetStatusMapper extends AbstractDBMapper {
    public final static String FIND_MANY_BY_TEST_ROSTER_ID = "findStudentItemSetStatusesForRoster";

    public StudentItemSetStatusMapper(final Connection conn) {
        super(conn);
    }

    public Map getMapOfSubtestStatusForTestRoster(final Long testRosterId) {
        final List itemSetStatuses = findStudentItemSetStatusesForRoster(
                testRosterId);
        final Map map = new HashMap(itemSetStatuses.size());

        for (final Iterator it = itemSetStatuses.iterator(); it.hasNext();) {
            final StudentItemSetStatusRecord itemSetStatus = (StudentItemSetStatusRecord) it.next();

            map.put(itemSetStatus.getItemSetId(), itemSetStatus);
        }

        return map;
    }

    public List findStudentItemSetStatusesForRoster(final Long testRosterId) {
        return findMany(FIND_MANY_BY_TEST_ROSTER_ID, testRosterId);
    }
}
