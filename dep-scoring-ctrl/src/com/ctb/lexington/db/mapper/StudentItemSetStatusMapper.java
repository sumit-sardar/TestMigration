package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.data.ItemSetVO;
import com.ctb.lexington.data.StudentItemSetStatusRecord;

public class StudentItemSetStatusMapper extends AbstractDBMapper {
    public final static String FIND_MANY_BY_TEST_ROSTER_ID = "findStudentItemSetStatusesForRoster";
    public final static String FIND_SUBTEST_VALIDATION_BY_ROSTER_ID_AND_ITEM_SET_ID = "findSubtestValidationByRosterIdAndItemSetId";

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
    
    public String getSubtestValidationStatusForTestRosterAndItemSetId(final Long testRosterId, final Long itemSetId) {
    	
    	StudentItemSetStatusRecord template = new StudentItemSetStatusRecord();
    	template.setTestRosterId(testRosterId);
    	template.setItemSetId(itemSetId);
        final List validationSetStatuses = findMany(FIND_SUBTEST_VALIDATION_BY_ROSTER_ID_AND_ITEM_SET_ID,template);
        
        String validated = "VA";
        String valid = null;
        String exemption =null;
        String absent = null;
        for (final Iterator it = validationSetStatuses.iterator(); it.hasNext();) {
            final ItemSetVO validationSetStatus = (ItemSetVO) it.next();
            valid = validationSetStatus.getValidationStatus().toString();
            exemption = validationSetStatus.getExemptions().toString();
            absent = validationSetStatus.getAbsent().toString();
        }
        if(absent!= null && exemption != null) {
        	if(valid.equals("VA") && exemption.equals("N") && absent.equals("N"))
        		validated = "VA";
        	else
        		validated = "IN";
        } else {
        	if(valid.equals("VA"))
        		validated = "VA";
        	else
        		validated = "IN";
        }
        return validated;
    }

    public List findStudentItemSetStatusesForRoster(final Long testRosterId) {
        return findMany(FIND_MANY_BY_TEST_ROSTER_ID, testRosterId);
    }
}
