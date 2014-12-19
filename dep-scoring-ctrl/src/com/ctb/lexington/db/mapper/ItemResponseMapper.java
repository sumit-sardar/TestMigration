package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ctb.lexington.data.ItemResponseVO;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.SQLUtil;

public class ItemResponseMapper extends AbstractDBMapper {
    public static final String FIND_MANY_BY_ITEM_SET_ID = "findItemResponsesByItemSetIdAndRosterId";
    public static final String FIND_MANY_BY_ITEM_SET_ID_TASC = "findItemResponsesByItemSetIdAndRosterIdForTASC";
    public static final String FIND_MANY_BY_ITEM_SET_ID_TASC_ORG = "findItemResponsesByItemSetIdAndRosterIdForTASCOrg";
    public static final String FIND_MANY_BY_ROSTER_ID = "findItemResponsesByRosterId";
    public static final String FIND_SCORED_OFFLINE_ITEMS = "findOfflineItemsThatAreScored";
    public static final String FIND_SCORED_ITEMS = "findItemsThatAreScored";
    public static final String FIND_SCORED_OFFLINE_ITEMS_FOR_SUBTEST = "findOfflineItemsThatAreScoredBySubtest";
    public static final String FIND_SCORED_ITEMS_FOR_SUBTEST = "findItemsThatAreScoredBySubtest";
    public static final String FIND_ITEMS_RESPONSE_FOR_ADAPTIVE = "findItemResponsesForAdaptive";

    public ItemResponseMapper(final Connection conn) {
        super(conn);
    }

    public List findItemResponsesBySubtest(final Long itemSetId, final Long testRosterId) {
        HashMap map = new HashMap();
        map.put("itemSetId", itemSetId);
        map.put("testRosterId", testRosterId);
    	return findMany(FIND_MANY_BY_ITEM_SET_ID, map);
    }
    
    //Added for TASC 
    public List findItemResponsesBySubtestForTASC(final ArrayList itemSetId, final Long testRosterId) {
        HashMap map = new HashMap();
        map.put("itemSetId", itemSetId);
        map.put("testRosterId", testRosterId);
    	return findMany(FIND_MANY_BY_ITEM_SET_ID_TASC, map);
    }
    
    public List findItemResponsesBySubtestForTASCOrg(final Long itemSetId, final Long testRosterId) {
        HashMap map = new HashMap();
        map.put("itemSetId", itemSetId);
        map.put("testRosterId", testRosterId);
    	return findMany(FIND_MANY_BY_ITEM_SET_ID_TASC_ORG, map);
    }
    
    //Added for tabe adaptive
    public List findItemResponsesBySubtestForAdaptive(final Long itemSetId, final Long testRosterId) {
        HashMap map = new HashMap();
        map.put("itemSetId", itemSetId);
        map.put("testRosterId", testRosterId);
    	return findMany(FIND_ITEMS_RESPONSE_FOR_ADAPTIVE, map);
    }
    
    public List findItemResponsesByRoster(final Long testRosterId) throws CTBSystemException {
        String sql = "SELECT DISTINCT " +
    "IR.ITEM_RESPONSE_ID as itemResponseId, " +
    "IR.ITEM_SET_ID as itemSetId, " +
    "IR.TEST_ROSTER_ID as testRosterId, " +
    "IR.RESPONSE as response, " +
    "IR.RESPONSE_METHOD as responseMethod, " +
    "IR.RESPONSE_ELAPSED_TIME as responseElapsedTime, " +
    "IR.RESPONSE_SEQ_NUM as responseSeqNum, " +
    "IR.CREATED_DATE_TIME as createdDateTime, " +
    "IR.ITEM_ID as itemId, " +
    "IR.EXT_ANSWER_CHOICE_ID as extAnswerChoiceId, " +
    "IR.STUDENT_MARKED as studentMarked, " +
    "IR.CREATED_BY as createdBy, " +
    "IRP.POINTS as points, " +
    "IRP.CONDITION_CODE_ID as conditionCodeId, " +
    "IRP.COMMENTS as comments " +
   // "IRC.CONSTRUCTED_RESPONSE as constructedResponse " + TO AVOID MULTI DATA POINT ENTRY
"FROM " +
    "ITEM_RESPONSE IR, " +
    "ITEM_RESPONSE_CR IRC, " +
	"ITEM_RESPONSE_POINTS IRP, " +
	"ITEM_SET_ITEM ISI, " +
	"ITEM " +
"WHERE IR.TEST_ROSTER_ID = IRC.TEST_ROSTER_ID (+) " +
    	"AND IR.ITEM_ID = IRC.ITEM_ID (+) " +
    	"AND IR.ITEM_SET_ID = IRC.ITEM_SET_ID (+) " + 
		"AND IR.TEST_ROSTER_ID = IRC.TEST_ROSTER_ID (+) " + 
    	"AND IR.ITEM_RESPONSE_ID = IRP.ITEM_RESPONSE_ID (+) " +
        "AND IR.TEST_ROSTER_ID = ? " +
        "AND ITEM.ITEM_ID = IR.ITEM_ID " +
        "AND ISI.ITEM_SET_ID = IR.ITEM_SET_ID " +
		"AND ISI.ITEM_ID = ITEM.ITEM_ID " +
		"AND ISI.SUPPRESSED = 'F' " +
        "AND ITEM.ITEM_TYPE != 'NI' " +
		"AND (IRP.ITEM_RESPONSE_POINTS_SEQ_NUM = " +
            "(SELECT " +
                "MAX(ITEM_RESPONSE_POINTS_SEQ_NUM) " +
             "FROM " +
                 "ITEM_RESPONSE_POINTS " +
             "WHERE " +
                 "ITEM_RESPONSE_ID = IRP.ITEM_RESPONSE_ID) " +
            "OR (SELECT COUNT(*) " +
             "FROM " +
                "ITEM_RESPONSE_POINTS " +
             "WHERE " +
                 "ITEM_RESPONSE_ID = IRP.ITEM_RESPONSE_ID) = 0) " +
"ORDER BY " +
    "IR.RESPONSE_SEQ_NUM ASC";
        List responses = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	ps = conn.prepareStatement(sql);
        	ps.setLong(1, testRosterId.longValue());
        	rs = ps.executeQuery();
        	while (rs.next()) {
        		ItemResponseVO response = new ItemResponseVO();
        		response.setItemId(SQLUtil.getString(rs, "itemId"));
        		response.setItemSetId(SQLUtil.getInteger(rs, "itemSetId"));
        		//response.setConstructedResponse(SQLUtil.getClob(rs, "constructedResponse"));
        		responses.add(response);
        	}
        } catch (SQLException e) {
			e.printStackTrace();
			throw new CTBSystemException(e.getMessage());
		} finally {
            SQLUtil.close(rs, ps);
        }
		return responses;
    }

    public int getCountOfOfflineItemsThatAreScored(Long testRosterId) {
        return count(FIND_SCORED_OFFLINE_ITEMS, testRosterId).intValue();
    }
    
    public int getCountOfItemsThatAreScored(Long testRosterId) {
        return count(FIND_SCORED_ITEMS, testRosterId).intValue();
    }
    
    public int getCountOfItemsThatAreScored(Long testRosterId, Long itemSetId) {
    	HashMap params = new HashMap();
    	params.put("testRosterId", testRosterId);
    	params.put("itemSetId", itemSetId);
        return count(FIND_SCORED_ITEMS_FOR_SUBTEST, params).intValue();
    }
    
    public int getCountOfOfflineItemsThatAreScored(Long testRosterId, Long itemSetId) {
    	HashMap params = new HashMap();
    	params.put("testRosterId", testRosterId);
    	params.put("itemSetId", itemSetId);
        return count(FIND_SCORED_OFFLINE_ITEMS_FOR_SUBTEST, params).intValue();    
    }
    
    // Added for TABE CCSS
    public List findItemResponsesBySubtestForTabeCC(final Long itemSetId, final Long testRosterId) throws CTBSystemException{
        String sql = "SELECT IR.ITEM_RESPONSE_ID as itemResponseId," + 
        "		       IR.ITEM_SET_ID as itemSetId," + 
        "		       IR.TEST_ROSTER_ID as testRosterId," + 
        "		       IR.RESPONSE_METHOD as responseMethod," + 
        "		       NVL(IR.RESPONSE, '-') as response," + 
        "		       IR.RESPONSE_ELAPSED_TIME as responseElapsedTime," + 
        "		       IR.RESPONSE_SEQ_NUM as responseSeqNum," + 
        "		       IR.CREATED_DATE_TIME as createdDateTime," + 
        "		       IR.ITEM_ID as itemId," + 
        "		       IR.EXT_ANSWER_CHOICE_ID as extAnswerChoiceId," + 
        "		       IR.STUDENT_MARKED as studentMarked," + 
        "		       IR.CREATED_BY as createdBy," + 
        "		       IRP.POINTS as points," + 
        "		       IRP.CONDITION_CODE_ID as conditionCodeId," + 
        "		       IRP.COMMENTS as comments," + 
//        "		       DECODE(ITEM.ITEM_TYPE, 'SR', '--', IRC.CONSTRUCTED_RESPONSE) as teItemResponse," + 
        "		       IRC.CONSTRUCTED_RESPONSE as teItemResponse," +
        "		       ITEM.ITEM_TYPE as itemType" + 
        "		  FROM ITEM_RESPONSE IR," + 
        "		       ITEM_RESPONSE_POINTS IRP," + 
        "		       ITEM_SET_ITEM ISI," + 
        "		       ITEM," + 
        "		       TEST_ROSTER TR," + 
        "		       ITEM_SET ISET," + 
        "		       TEST_ADMIN ADM," + 
        "		       PRODUCT PROD," + 
        "		       ITEM_RESPONSE_CR IRC" + 
        "		 WHERE IR.ITEM_RESPONSE_ID = IRP.ITEM_RESPONSE_ID(+)" + 
        "		   AND IR.ITEM_SET_ID = ? " + 
        "		   AND IR.TEST_ROSTER_ID = ? " + 
        "		   AND ITEM.ITEM_ID = IR.ITEM_ID" + 
        "		   AND ISI.ITEM_SET_ID = IR.ITEM_SET_ID" + 
        "		   AND ISI.ITEM_ID = ITEM.ITEM_ID" + 
        "		   AND ISI.SUPPRESSED = 'F'" + 
        "		   AND ITEM.ITEM_TYPE not in ('RQ', 'NI')" + 
        "		   AND (IRP.ITEM_RESPONSE_POINTS_SEQ_NUM =" + 
        "		       (SELECT MAX(ITEM_RESPONSE_POINTS_SEQ_NUM)" + 
        "		           FROM ITEM_RESPONSE_POINTS" + 
        "		          WHERE ITEM_RESPONSE_ID = IRP.ITEM_RESPONSE_ID) OR" + 
        "		       (SELECT COUNT(*)" + 
        "		           FROM ITEM_RESPONSE_POINTS" + 
        "		          WHERE ITEM_RESPONSE_ID = IRP.ITEM_RESPONSE_ID) = 0)" + 
        "		   AND IR.RESPONSE_SEQ_NUM =" + 
        "		       (SELECT MAX(RESPONSE_SEQ_NUM)" + 
        "		          FROM ITEM_RESPONSE" + 
        "		         WHERE TEST_ROSTER_ID = IR.TEST_ROSTER_ID" + 
        "		           AND ITEM_ID = IR.ITEM_ID)" + 
        "		   AND ISET.ITEM_SET_ID = ISI.ITEM_SET_ID" + 
        "		   AND TR.TEST_ROSTER_ID = IR.TEST_ROSTER_ID" + 
        "		   AND ADM.TEST_ADMIN_ID = TR.TEST_ADMIN_ID" + 
        "		   AND PROD.PRODUCT_ID = ADM.PRODUCT_ID" + 
        "		   AND IR.TEST_ROSTER_ID = IRC.TEST_ROSTER_ID(+)" + 
        "		   AND IR.ITEM_SET_ID = IRC.ITEM_SET_ID(+)" + 
        "		   AND IR.ITEM_ID = IRC.ITEM_ID(+)" + 
        "		   AND (ISET.ITEM_SET_LEVEL != 'L' OR PROD.PRODUCT_TYPE = 'TL')" + 
        "		 ORDER BY IR.RESPONSE_SEQ_NUM ASC";
        List responses = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	ps = conn.prepareStatement(sql);
        	ps.setLong(1, itemSetId.longValue());
        	ps.setLong(2, testRosterId.longValue());
        	rs = ps.executeQuery();
        	while (rs.next()){
        		ItemResponseVO response = new ItemResponseVO();
        		response.setItemResponseId(SQLUtil.getInteger(rs, "itemResponseId"));
        		response.setItemSetId(SQLUtil.getInteger(rs, "itemSetId"));
        		response.setTestRosterId(SQLUtil.getInteger(rs, "testRosterId"));
        		response.setResponseMethod(SQLUtil.getString(rs, "responseMethod"));
        		response.setResponse(SQLUtil.getString(rs, "response"));
        		response.setResponseElapsedTime(SQLUtil.getInteger(rs, "responseElapsedTime"));
        		response.setResponseSeqNum(SQLUtil.getInteger(rs, "responseSeqNum"));
        		response.setCreatedDateTime(SQLUtil.getDate(rs, "createdDateTime"));
        		response.setItemId(SQLUtil.getString(rs, "itemId"));
        		response.setExtAnswerChoiceId(SQLUtil.getString(rs, "extAnswerChoiceId"));
        		response.setStudentMarked(SQLUtil.getString(rs, "studentMarked"));
        		response.setCreatedBy(SQLUtil.getInteger(rs, "createdBy"));
        		response.setPoints(SQLUtil.getInteger(rs, "points"));
        		response.setConditionCode(SQLUtil.getString(rs, "conditionCodeId"));
        		response.setComments(SQLUtil.getString(rs, "comments"));
        		response.setTeItemResponse(SQLUtil.getClob(rs, "teItemResponse"));
        		response.setItemType(SQLUtil.getString(rs, "itemType"));
        		responses.add(response);
        	}
        } catch (SQLException e) {
			e.printStackTrace();
			throw new CTBSystemException(e.getMessage());
		} finally {
            SQLUtil.close(rs, ps);
        }
    	return responses;
    }
}