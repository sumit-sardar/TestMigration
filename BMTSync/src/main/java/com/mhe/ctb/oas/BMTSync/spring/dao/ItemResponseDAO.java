package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import com.mhe.ctb.oas.BMTSync.model.ItemResponse;

public class ItemResponseDAO {

	private static final Logger LOGGER = Logger.getLogger(ItemResponseDAO.class);

	// The data source
	@SuppressWarnings("unused")
	private DataSource dataSource;

	// The JDBC template
	private JdbcTemplate template;
	
	// The Constructed Response encoder
	private ConstructedResponseEncoder encoder;

	/** The query to get an item_set_id from a subTestId. */
	private static final String SELECT_ITEM_SET_ID = "SELECT IST.ITEM_SET_ID "
			+ "FROM Student_Item_Set_Status SISS, Item_Set IST "
			+ "WHERE SISS.Item_Set_ID = IST.Item_set_Id AND "
			+ "SISS.Test_Roster_ID = ? AND "
			+ "IST.Ext_Tst_Item_Set_Id = ?";
	
	/** The query to get the next item response id. */
	private static final String SELECT_ITEM_RESPONSE_ID = "SELECT SEQ_ITEM_RESPONSE_ID.NEXTVAL FROM DUAL";
	
	/** The query to get the next sequence number for a given item response. */
	private static final String SELECT_MAX_RESPONSE_SEQUENCE_NUMBER = "SELECT MAX(RESPONSE_SEQ_NUM) FROM ITEM_RESPONSE "
			+ "WHERE TEST_ROSTER_ID = ?"; // Per Arif, this should only be per roster, not per roster and item ID.
			
	/** The query to insert a record into ITEM_RESPONSE. */
	private static final String INSERT_ITEM_RESPONSE = "INSERT INTO ITEM_RESPONSE "
			+ "(ITEM_RESPONSE_ID, TEST_ROSTER_ID, ITEM_SET_ID, ITEM_ID, RESPONSE, RESPONSE_METHOD, RESPONSE_ELAPSED_TIME, "
			+ "RESPONSE_SEQ_NUM, CREATED_DATE_TIME, EXT_ANSWER_CHOICE_ID, STUDENT_MARKED, CREATED_BY) VALUES ("
			+ "?, "			// ITEM_RESPONSE_ID			NUMBER(38,0)		itemResponseId
			+ "?, "			// TEST_ROSTER_ID			NUMBER(38,0)		testRosterId
			+ "?, "			// ITEM_SET_ID				NUMBER(38,0)		itemSetId
			+ "?, "			// ITEM_ID					VARCHAR2(32 BYTE)	itemResponse.getItemCode()
			+ "?, "			// RESPONSE					VARCHAR2(255 BYTE)	itemResponse.getItemResponse() if MC OR NULL
			+ "'M', "		// RESPONSE_METHOD			VARCHAR2(2 BYTE)	'Mouse' for MC, Unused per Tai
			+ "?, "			// RESPONSE_ELAPSED_TIME	NUMBER(8,0)			itemResponse.getItemResponseTime()
			+ "?, "			// RESPONSE_SEQ_NUM			NUMBER(8,0)			Lookup Sequence number.
			+ "sysdate, "	// CREATED_DATE_TIME		DATE				sysdate
			+ "null, "		// EXT_ANSWER_CHOICE_ID		VARCHAR2(32 BYTE)	null
			+ "null, "		// STUDENT_MARKED			VARCHAR2(2 BYTE)	null per Sumit
			+ "6)";			// CREATED_BY				NUMBER(38,0)		Foreign key to USERS table, 6 fixed as "test_client" per Sumit Sardar.
	
	/** The query to get the number of records in ITEM_RESPONSE_CR for a given testRosterId, itemSetId, and itemId. */
	private static final String SELECT_UNIQUE_CONSTRUCTED_RESPONSE = "SELECT COUNT(1) FROM ITEM_RESPONSE_CR "
			+ "WHERE TEST_ROSTER_ID = ? "
			+ "AND ITEM_SET_ID = ? "
			+ "AND ITEM_ID = ?";
	
	/** The query to insert a constructed response. */
	private static final String INSERT_CONSTRUCTED_RESPONSE = "INSERT INTO ITEM_RESPONSE_CR "
			+ "(TEST_ROSTER_ID, ITEM_SET_ID, ITEM_ID, CONSTRUCTED_RESPONSE, AUDIO_URL) VALUES ("
			+ "?, "		// TEST_ROSTER_ID		NUMBER				testRosterId
			+ "?, "		// ITEM_SET_ID			NUMBER				itemSetId
			+ "?, "		// ITEM_ID				VARCHAR2(32 BYTE)	itemResponse.getItemCode()
			+ "?, "		// CONSTRUCTED_RESPONSE	CLOB				URLEncode.encode(itemResponse.getItemResponse(), "UTF-8")
			+ "null)";	// AUDIO_URL			VARCHAR2(1024 BYTE)	audioURL, unused during CR entries.

	/** The query to insert an audio response. */
	private static final String INSERT_AUDIO_RESPONSE = "INSERT INTO ITEM_RESPONSE_CR "
			+ "(TEST_ROSTER_ID, ITEM_SET_ID, ITEM_ID, CONSTRUCTED_RESPONSE, AUDIO_URL) VALUES ("
			+ "?, "		// TEST_ROSTER_ID		NUMBER				testRosterId
			+ "?, "		// ITEM_SET_ID			NUMBER				itemSetId
			+ "?, "		// ITEM_ID				VARCHAR2(32 BYTE)	itemResponse.getItemCode()
			+ "null, "	// CONSTRUCTED_RESPONSE	CLOB				unused during AU entries.		
			+ "?)";		// AUDIO_URL			VARCHAR2(1024 BYTE)	itemResponse.getItemResponse()		

	/** The query to update a constructed response. */
	private static final String UPDATE_CONSTRUCTED_RESPONSE = "UPDATE ITEM_RESPONSE_CR SET "
			+ "CONSTRUCTED_RESPONSE = ? "
			+ "WHERE TEST_ROSTER_ID = ? "
			+ "AND ITEM_SET_ID = ? "
			+ "AND ITEM_ID = ?";

	/** The query to update a constructed response. */
	private static final String UPDATE_AUDIO_RESPONSE = "UPDATE ITEM_RESPONSE_CR SET "
			+ "AUDIO_URL = ? "
			+ "WHERE TEST_ROSTER_ID = ? "
			+ "AND ITEM_SET_ID = ? "
			+ "AND ITEM_ID = ?";

	public ItemResponseDAO(final DataSource ds) {
		this(ds, null, null);
	}

	public ItemResponseDAO(final DataSource ds, final JdbcTemplate jdbcTemplate, final ConstructedResponseEncoder crEncoder) {
		this.dataSource = ds;
		if (jdbcTemplate == null) {
			template = new JdbcTemplate(ds);
			encoder = new ConstructedResponseEncoder();
		} else {
			template = jdbcTemplate;
			encoder = crEncoder;
		}
	}
	
	public boolean addItemResponses(final Integer testRosterId, final String subTestId,
			final List<ItemResponse> itemResponses) throws SQLException {

		final Integer itemSetId = getItemSetIdForRosterAndSubtest(testRosterId, subTestId);
		if (CollectionUtils.isEmpty(itemResponses)) {
			LOGGER.warn("Item responses empty! No records will be recorded. [testRosterId=" + testRosterId
					+ ",subTestId=" + subTestId + "]");
		} else {
			for (final ItemResponse itemResponse : itemResponses) {
				if ("MC".equals(itemResponse.getItemType())) {
					// Multiple choice answer; store result in ITEM_RESPONSE, multiple recordings of the same value is fine.
					insertMultipleChoiceResponse(testRosterId, itemSetId, itemResponse);
				} else {
					// Constructed or audio answer. First, see if there's already an entry for this item.
					if (constructedEntryExists(testRosterId, itemSetId, itemResponse.getItemCode())) {
						updateConstructedResponse(testRosterId, itemSetId, itemResponse);
					} else {
						insertConstructedResponse(testRosterId, itemSetId, itemResponse);
					}
				}
			}
		}
		
		return true;
	}
	
	private void updateConstructedResponse(final Integer testRosterId, final Integer itemSetId,
			final ItemResponse itemResponse) throws SQLException {
		//BMTOAS-1973 fix - insert a record into ITEM_RESPONSE first - per suggested solution
		insertPlaceholderItemResponse(testRosterId, itemSetId, itemResponse);		
		try {
			if ("CR".equals(itemResponse.getItemType()) || "MCR".equals(itemResponse.getItemType())) {
				//fix for BMTOAS-2083: make sure itemType set to "CR", in case when "MCR" was passed for multi-part responses; at this point it can be only CR or MCR
				itemResponse.setItemType("CR");
				final Calendar startDBTime = Calendar.getInstance();
				int rowsUpdated = template.update(UPDATE_CONSTRUCTED_RESPONSE,
						encoder.formatConstructedResponse(itemResponse.getItemResponse()), testRosterId, itemSetId, itemResponse.getItemCode());
				final Calendar endDBTime = Calendar.getInstance();
				final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
		        LOGGER.info("SyncCallTime " + callDBTime + " SyncCallType DirectQuery SyncCallDest ITEM_RESPONSE_CR.UPDATE_CONSTRUCTED_RESPONSE");
		        
		        //BMTOAS-2042 - logging for CloudWatch
		        LOGGER.info("{\"Name\":\"CloudWatchLog\""
		        		+",\"Application\":\"BMTSyncClient\""
		        		+",\"IsError\":false,\"ErrorCode\":0"
		        		+",\"CallType\":\"DirectQuery\""
		        		+",\"CallDest\":\"ITEM_RESPONSE_CR.UPDATE_CONSTRUCTED_RESPONSE\""
		        		+",\"APICallDuration\":"+callDBTime+"}");
		        
				if (rowsUpdated != 1) {
					LOGGER.error("[ItemResponseDAO] Error updating constructed response: expected one row inserted, actual: "
							+ rowsUpdated + "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
							+ itemResponse.getItemCode() + "]");
					throw new SQLException("[ItemResponseDAO] Error inserting constructed response: expected one row inserted, actual: "
							+ rowsUpdated + "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
							+ itemResponse.getItemCode() + "]");
				}
				return;
			}
			if ("AU".equals(itemResponse.getItemType())) {
				final Calendar startDBTime = Calendar.getInstance();
				int rowsUpdated = template.update(UPDATE_AUDIO_RESPONSE,
						itemResponse.getItemResponse(), testRosterId, itemSetId, itemResponse.getItemCode());
				final Calendar endDBTime = Calendar.getInstance();
				final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
		        LOGGER.info("SyncCallTime " + callDBTime + " SyncCallType DirectQuery SyncCallDest ITEM_RESPONSE_CR.UPDATE_AUDIO_RESPONSE");
		        
		        //BMTOAS-2042 - logging for CloudWatch
		        LOGGER.info("{\"Name\":\"CloudWatchLog\""
		        		+",\"Application\":\"BMTSyncClient\""
		        		+",\"IsError\":false,\"ErrorCode\":0"
		        		+",\"CallType\":\"DirectQuery\""
		        		+",\"CallDest\":\"ITEM_RESPONSE_CR.UPDATE_AUDIO_RESPONSE\""
		        		+",\"APICallDuration\":"+callDBTime+"}");
		        
				if (rowsUpdated != 1) {
					LOGGER.error("[ItemResponseDAO] Error inserting audio response: expected one row inserted, actual: "
							+ rowsUpdated + "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
							+ itemResponse.getItemCode() + "]");
					throw new SQLException("[ItemResponseDAO] Error inserting audio response: expected one row inserted, actual: "
							+ rowsUpdated + "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
							+ itemResponse.getItemCode() + "]");
				}
				return;
			}
			LOGGER.error("[ItemResponseDAO] Unknown item response type: '" + itemResponse.getItemType() + "'. [testRosterId="
					+ testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemResponse.getItemType() + "]");
			throw new SQLException("[ItemResponseDAO] Unknown item response type: '" + itemResponse.getItemType() + "'. [testRosterId="
					+ testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemResponse.getItemCode() + "]");
		} catch (final DataAccessException sqle) {
			LOGGER.error("[ItemResponseDAO] Error trying to validate uniqueness of constructed response: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemResponse.getItemCode() + "]", sqle);
			throw new SQLException("[ItemResponseDAO] Error trying to find item_set_id: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemResponse.getItemCode() + "]", sqle);
		} catch (final UnsupportedEncodingException uue) {
			LOGGER.error("[ConstructedResponseEncoder] Error trying to URLEncode constructed response: " + uue.getMessage()
					+ "[response=" + itemResponse.getItemResponse() + "]", uue);
			throw new SQLException("[ItemResponseDAO] Error trying to URLEncode constructed response: " + uue.getMessage()
					+ "[response=" + itemResponse.getItemResponse() + "]", uue);
		}
	}

	private void insertConstructedResponse(final Integer testRosterId, final Integer itemSetId,
			final ItemResponse itemResponse) throws SQLException {
		// THere has to be a placeholder entry in the ITEM_RESPONSE table for the scoring module to work.
		insertPlaceholderItemResponse(testRosterId, itemSetId, itemResponse);
		try {
			if ("CR".equals(itemResponse.getItemType()) || "MCR".equals(itemResponse.getItemType())) {
				//fix for BMTOAS-2083: make sure itemType set to "CR", in case when "MCR" was passed for multi-part responses; at this point it can be only CR or MCR
				itemResponse.setItemType("CR");
				final Calendar startDBTime = Calendar.getInstance();
				int rowsUpdated = template.update(INSERT_CONSTRUCTED_RESPONSE, 
						testRosterId, itemSetId, itemResponse.getItemCode(),
						encoder.formatConstructedResponse(itemResponse.getItemResponse()));
				final Calendar endDBTime = Calendar.getInstance();
				final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
		        LOGGER.info("SyncCallTime " + callDBTime + " SyncCallType DirectQuery SyncCallDest ITEM_RESPONSE_CR.INSERT_CONSTRUCTED_RESPONSE");
		        
		        //BMTOAS-2042 - logging for CloudWatch
		        LOGGER.info("{\"Name\":\"CloudWatchLog\""
		        		+",\"Application\":\"BMTSyncClient\""
		        		+",\"IsError\":false,\"ErrorCode\":0"
		        		+",\"CallType\":\"DirectQuery\""
		        		+",\"CallDest\":\"ITEM_RESPONSE_CR.INSERT_CONSTRUCTED_RESPONSE\""
		        		+",\"APICallDuration\":"+callDBTime+"}");
		        
				if (rowsUpdated != 1) {
					LOGGER.error("[ItemResponseDAO] Error inserting constructed response: expected one row inserted, actual: "
							+ rowsUpdated + "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
							+ itemResponse.getItemCode() + "]");
					throw new SQLException("[ItemResponseDAO] Error inserting constructed response: expected one row inserted, actual: "
							+ rowsUpdated + "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
							+ itemResponse.getItemCode() + "]");
				}
				return;
			}
			if ("AU".equals(itemResponse.getItemType())) {
				final Calendar startDBTime = Calendar.getInstance();
				int rowsUpdated = template.update(INSERT_AUDIO_RESPONSE,
						testRosterId, itemSetId, itemResponse.getItemCode(), itemResponse.getItemResponse());
				final Calendar endDBTime = Calendar.getInstance();
				final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
		        LOGGER.info("SyncCallTime " + callDBTime + " SyncCallType DirectQuery SyncCallDest ITEM_RESPONSE_CR.INSERT_AUDIO_RESPONSE");
		        
		        //BMTOAS-2042 - logging for CloudWatch
		        LOGGER.info("{\"Name\":\"CloudWatchLog\""
		        		+",\"Application\":\"BMTSyncClient\""
		        		+",\"IsError\":false,\"ErrorCode\":0"
		        		+",\"CallType\":\"DirectQuery\""
		        		+",\"CallDest\":\"ITEM_RESPONSE_CR.INSERT_AUDIO_RESPONSE\""
		        		+",\"APICallDuration\":"+callDBTime+"}");
		        
				if (rowsUpdated != 1) {
					LOGGER.error("[ItemResponseDAO] Error inserting audio response: expected one row inserted, actual: "
							+ rowsUpdated + "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
							+ itemResponse.getItemCode() + "]");
					throw new SQLException("[ItemResponseDAO] Error inserting audio response: expected one row inserted, actual: "
							+ rowsUpdated + "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
							+ itemResponse.getItemCode() + "]");
				}
				return;
			}
			LOGGER.error("[ItemResponseDAO] Unknown item response type: '" + itemResponse.getItemType() + "'. [testRosterId="
					+ testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemResponse.getItemType() + "]");
			throw new SQLException("[ItemResponseDAO] Unknown item response type: '" + itemResponse.getItemType() + "'. [testRosterId="
					+ testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemResponse.getItemCode() + "]");
		} catch (final DataAccessException sqle) {
			LOGGER.error("[ItemResponseDAO] Error trying to validate uniqueness of constructed response: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemResponse.getItemCode() + "]", sqle);
			throw new SQLException("[ItemResponseDAO] Error trying to find item_set_id: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemResponse.getItemCode() + "]", sqle);
		} catch (final UnsupportedEncodingException uue) {
			LOGGER.error("[ConstructedResponseEncoder] Error trying to URLEncode constructed response: " + uue.getMessage()
					+ "[response=" + itemResponse.getItemResponse() + "]", uue);
			throw new SQLException("[ItemResponseDAO] Error trying to URLEncode constructed response: " + uue.getMessage()
					+ "[response=" + itemResponse.getItemResponse() + "]", uue);
		}
	}

	private boolean constructedEntryExists(final Integer testRosterId, final Integer itemSetId, final Integer itemId)
			throws SQLException {
		try {
			final Calendar startDBTime = Calendar.getInstance();
			final Integer extantRows = template.queryForObject(SELECT_UNIQUE_CONSTRUCTED_RESPONSE, Integer.class, testRosterId, itemSetId, itemId);
			final Calendar endDBTime = Calendar.getInstance();
			final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
	        LOGGER.info("SyncCallTime " + callDBTime + " SyncCallType DirectQuery SyncCallDest ITEM_RESPONSE_CR.SELECT_UNIQUE_CONSTRUCTED_RESPONSE");
	        
	        //BMTOAS-2042 - logging for CloudWatch
	        LOGGER.info("{\"Name\":\"CloudWatchLog\""
	        		+",\"Application\":\"BMTSyncClient\""
	        		+",\"IsError\":false,\"ErrorCode\":0"
	        		+",\"CallType\":\"DirectQuery\""
	        		+",\"CallDest\":\"ITEM_RESPONSE_CR.SELECT_UNIQUE_CONSTRUCTED_RESPONSE\""
	        		+",\"APICallDuration\":"+callDBTime+"}");
	        
			if (extantRows > 0) {
				LOGGER.debug("No rows found in ITEM_RESPONSE_CR."
						+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemId + "]");
				return true;
			} else {
				LOGGER.debug("One or more rows found in ITEM_RESPONSE_CR."
						+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemId + "]");
				return false;
			}
		} catch (final DataAccessException sqle) {
			LOGGER.error("[ItemResponseDAO] Error trying to validate uniqueness of constructed response: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemId + "]", sqle);
			throw new SQLException("[ItemResponseDAO] Error trying to validate uniqueness of constructed response: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId=" + itemId + "]", sqle);
		}
	}

	private void insertMultipleChoiceResponse(final Integer testRosterId, final Integer itemSetId,
			final ItemResponse itemResponse) throws SQLException {
		insertItemResponseEntry(testRosterId, itemSetId, itemResponse.getItemCode(),
				itemResponse.getItemResponse(), itemResponse.getItemResponseTime());
	}
	
	private void insertPlaceholderItemResponse(final Integer testRosterId, final Integer itemSetId,
			final ItemResponse itemResponse) throws SQLException {
		insertItemResponseEntry(testRosterId, itemSetId, itemResponse.getItemCode(), null, itemResponse.getItemResponseTime());
	}

	private void insertItemResponseEntry(final Integer testRosterId, final Integer itemSetId, final Integer itemCode,
			final String itemResponse, final Integer itemResponseTime) throws SQLException {
		final Integer itemResponseId = getNextItemResponseSequenceId();
		try {
			final Calendar startDBTime = Calendar.getInstance();
			int rowsUpdated = template.update(INSERT_ITEM_RESPONSE,
					itemResponseId,
					testRosterId,
					itemSetId,
					itemCode,
					itemResponse,
					itemResponseTime,
					getNextItemSequenceResponseNumber(testRosterId));
			final Calendar endDBTime = Calendar.getInstance();
			final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
	        LOGGER.info("SyncCallTime " + callDBTime + " SyncCallType DirectQuery SyncCallDest ITEM_RESPONSE.INSERT_ITEM_RESPONSE");
	        
	        //BMTOAS-2042 - logging for CloudWatch
	        LOGGER.info("{\"Name\":\"CloudWatchLog\""
	        		+",\"Application\":\"BMTSyncClient\""
	        		+",\"IsError\":false,\"ErrorCode\":0"
	        		+",\"CallType\":\"DirectQuery\""
	        		+",\"CallDest\":\"ITEM_RESPONSE.INSERT_ITEM_RESPONSE\""
	        		+",\"APICallDuration\":"+callDBTime+"}");
	        
			if (rowsUpdated != 1) {
				throw new SQLException("[ItemResponseDAO] INSERT error! Expected updatedRows = 1, Actual updatedRows = "
						+ rowsUpdated + "! [testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
						+ itemCode + "]");
			}
		} catch (final DataAccessException sqle) {
			LOGGER.error("[ItemResponseDAO] [ItemResponseDAO] INSERT error: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
						+ itemCode + "]", sqle);
			throw new SQLException("[ItemResponseDAO] INSERT error: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",itemSetId=" + itemSetId + ",itemId="
						+ itemCode + "]", sqle);
		}				
	}
	
	private Integer getItemSetIdForRosterAndSubtest(final Integer testRosterId, final String subTestId) throws SQLException {
		Integer itemSetId;
		try {
			final Calendar startDBTime = Calendar.getInstance();
			itemSetId = template.queryForObject(SELECT_ITEM_SET_ID, Integer.class, testRosterId, subTestId);
			final Calendar endDBTime = Calendar.getInstance();
			final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
	        LOGGER.info("SyncCallTime " + callDBTime + " SyncCallType DirectQuery SyncCallDest ITEM_RESPONSE.SELECT_ITEM_SET_ID");
	        
	        //BMTOAS-2042 - logging for CloudWatch
	        LOGGER.info("{\"Name\":\"CloudWatchLog\""
	        		+",\"Application\":\"BMTSyncClient\""
	        		+",\"IsError\":false,\"ErrorCode\":0"
	        		+",\"CallType\":\"DirectQuery\""
	        		+",\"CallDest\":\"ITEM_RESPONSE.SELECT_ITEM_SET_ID\""
	        		+",\"APICallDuration\":"+callDBTime+"}");
	        
		} catch (final DataAccessException sqle) {
			LOGGER.error("[ItemResponseDAO] Error trying to find item_set_id: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",extCmsItemSetId=" + subTestId + "]", sqle);
			throw new SQLException("[ItemResponseDAO] Error trying to find item_set_id: " + sqle.getMessage()
					+ "[testRosterId=" + testRosterId + ",extCmsItemSetId=" + subTestId + "]", sqle);
		}
		
		return itemSetId;
	}

	private Integer getNextItemResponseSequenceId() throws SQLException {
		Integer itemResponseId;
		try {
			final Calendar startDBTime = Calendar.getInstance();
			itemResponseId = template.queryForObject(SELECT_ITEM_RESPONSE_ID, Integer.class);
			final Calendar endDBTime = Calendar.getInstance();
			final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
	        LOGGER.info("SyncCallTime " + callDBTime + " SyncCallType DirectQuery SyncCallDest OAS.SELECT_ITEM_RESPONSE_ID");
	        
	        //BMTOAS-2042 - logging for CloudWatch
	        LOGGER.info("{\"Name\":\"CloudWatchLog\""
	        		+",\"Application\":\"BMTSyncClient\""
	        		+",\"IsError\":false,\"ErrorCode\":0"
	        		+",\"CallType\":\"DirectQuery\""
	        		+",\"CallDest\":\"OAS.SELECT_ITEM_RESPONSE_ID\""
	        		+",\"APICallDuration\":"+callDBTime+"}");
	        
		} catch (final DataAccessException sqle) {
			LOGGER.error("[ItemResponseDAO] Error trying to select next item_response_id: " + sqle.getMessage() + "]", sqle);
			throw new SQLException("[ItemResponseDAO] Error trying to select next item_response_id: " + sqle.getMessage() + "]", sqle);
		}
		
		return itemResponseId;
	}
	
	private Integer getNextItemSequenceResponseNumber(final Integer rosterId) throws SQLException {
		Integer seqResponseNumber;
		try {
			final Calendar startDBTime = Calendar.getInstance();
			seqResponseNumber = template.queryForObject(SELECT_MAX_RESPONSE_SEQUENCE_NUMBER, Integer.class,
					rosterId);
			final Calendar endDBTime = Calendar.getInstance();
			final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
	        LOGGER.info("SyncCallTime " + callDBTime + " SyncCallType DirectQuery SyncCallDest ITEM_RESPONSE.SELECT_MAX_RESPONSE_SEQ_NUM");
	        
	        //BMTOAS-2042 - logging for CloudWatch
	        LOGGER.info("{\"Name\":\"CloudWatchLog\""
	        		+",\"Application\":\"BMTSyncClient\""
	        		+",\"IsError\":false,\"ErrorCode\":0"
	        		+",\"CallType\":\"DirectQuery\""
	        		+",\"CallDest\":\"ITEM_RESPONSE.SELECT_MAX_RESPONSE_SEQ_NUM\""
	        		+",\"APICallDuration\":"+callDBTime+"}");
	        
		} catch (final DataAccessException sqle) {
			LOGGER.error("[ItemResponseDAO] Error trying to select next sequence_response_num: " + sqle.getMessage() + "]", sqle);
			throw new SQLException("[ItemResponseDAO] Error trying to select next sequence_response_num: " + sqle.getMessage() + "]", sqle);
		}
		if (seqResponseNumber == null) {
			seqResponseNumber = 0;
		}
		
		return seqResponseNumber + 1;
	}
}

/*
ITEM_RESPONSE_ID	NUMBER(38,0)           itemResponseId
TEST_ROSTER_ID	NUMBER(38,0)               testRosterId
ITEM_SET_ID	NUMBER(38,0)                   itemSetId
ITEM_ID	VARCHAR2(32 BYTE)                  itemResponse.getItemCode()
RESPONSE	VARCHAR2(255 BYTE)             itemResponse.getItemResponse() if MC OR NULL
RESPONSE_METHOD	VARCHAR2(2 BYTE)           'M' for MC, 'K' for Essay
RESPONSE_ELAPSED_TIME	NUMBER(8,0)        itemResponse.getItemResponseTime()
RESPONSE_SEQ_NUM	NUMBER(8,0)            null
CREATED_DATE_TIME	DATE                   sysdate
EXT_ANSWER_CHOICE_ID	VARCHAR2(32 BYTE)  null
STUDENT_MARKED	VARCHAR2(2 BYTE)           'T'
CREATED_BY	NUMBER(38,0)                   6

TEST_ROSTER_ID	NUMBER                     testRosterId
ITEM_SET_ID	NUMBER                         itemSetId
ITEM_ID	VARCHAR2(32 BYTE)                  itemResponse.getItemCode()
CONSTRUCTED_RESPONSE	CLOB               itemResponse.getItemResponse() if CR
AUDIO_URL	VARCHAR2(1024 BYTE)            itemResponse.getItemResponse() if AU

*/