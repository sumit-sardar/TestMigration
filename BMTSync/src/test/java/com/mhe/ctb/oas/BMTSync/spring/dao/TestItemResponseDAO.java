package com.mhe.ctb.oas.BMTSync.spring.dao;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.model.ItemResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestItemResponseDAO {
	
	private ItemResponseDAO itemResponseDao;
	
	private JdbcTemplate mockTemplate;
	
	/** The query to get the next item response id. */
	private static final String SELECT_ITEM_RESPONSE_ID = "SELECT SEQ_ITEM_RESPONSE_ID.NEXTVAL FROM DUAL";
	
	/** The query to get an item_set_id from a subTestId. */
	private static final String SELECT_ITEM_SET_ID = "SELECT IST.ITEM_SET_ID "
			+ "FROM Student_Item_Set_Status SISS, Item_Set IST "
			+ "WHERE SISS.Item_Set_ID = IST.Item_set_Id AND "
			+ "SISS.Test_Roster_ID = ? AND "
			+ "IST.Ext_Tst_Item_Set_Id = ?";
	
	/** The query to get the next sequence number for a given item response. */
	private static final String SELECT_MAX_RESPONSE_SEQUENCE_NUMBER = "SELECT MAX(RESPONSE_SEQ_NUM) FROM ITEM_RESPONSE "
			+ "WHERE TEST_ROSTER_ID = ? "
			+ "AND ITEM_SET_ID = ?";
	
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
			+ "'T', "		// STUDENT_MARKED			VARCHAR2(2 BYTE)	'T', Unused per Tai
			+ "99)";		// CREATED_BY				NUMBER(38,0)		Refers to USERS table, no entry listed.

	@Before
	public void setUp() {
		mockTemplate = mock(JdbcTemplate.class);
		DataSource mockDs = mock(DataSource.class);

		itemResponseDao = new ItemResponseDAO(mockDs, mockTemplate);
	}
	
	@Test
	public void testItemResponseDAO_addItemResponses_addMultipleChoice() {
		final Integer rosterId = 101;
		final String subTestId = "FAKE_TEST_101_A";
		final ItemResponse response = new ItemResponse();
		final Integer itemResponseId = 10101;
		final Integer itemSetId = 1001001;
		final Integer itemId = 11;
		response.setItemCode(itemId);
		response.setItemResponse("A");
		response.setItemCode(110011);
		response.setItemType("MC");
		final List<ItemResponse> itemResponseList = new ArrayList<ItemResponse>();
		itemResponseList.add(response);

		// Set up the updates result:
		when(mockTemplate.queryForObject(SELECT_ITEM_RESPONSE_ID, Integer.class)).thenReturn(itemResponseId);
		when(mockTemplate.queryForObject(SELECT_ITEM_SET_ID, Integer.class, rosterId, subTestId)).thenReturn(itemSetId);
		when(mockTemplate.queryForObject(SELECT_MAX_RESPONSE_SEQUENCE_NUMBER, Integer.class, rosterId, itemSetId))
				.thenReturn(null);
		when(mockTemplate.update(
				INSERT_ITEM_RESPONSE,
				itemResponseId,
				rosterId,
				itemSetId,
				response.getItemCode(),
				response.getItemResponse(),
				response.getItemResponseTime(),
				1)).thenReturn(1);
		
		try {
			if(! itemResponseDao.addItemResponses(rosterId, subTestId, itemResponseList)) {
				fail("Should have succeeded!");
			}
		} catch (SQLException sqle) {
			fail("Shouldn't have thrown exception! Exception was: " + sqle.getMessage());
			sqle.printStackTrace();
		}
		verify(mockTemplate, times(1)).update(
				INSERT_ITEM_RESPONSE,
				itemResponseId,
				rosterId,
				itemSetId,
				response.getItemCode(),
				response.getItemResponse(),
				response.getItemResponseTime(),
				1);
	}

	@Test(expected = SQLException.class)
	public void testItemResponseDAO_addItemResponses_addMultipleChoice_errorOnInsert() throws SQLException {
		final Integer rosterId = 101;
		final String subTestId = "FAKE_TEST_101_A";
		final ItemResponse response = new ItemResponse();
		final Integer itemResponseId = 10101;
		final Integer itemSetId = 1001001;
		final Integer itemId = 11;
		response.setItemCode(itemId);
		response.setItemResponse("A");
		response.setItemCode(110011);
		response.setItemType("MC");
		final List<ItemResponse> itemResponseList = new ArrayList<ItemResponse>();
		itemResponseList.add(response);

		// Set up the updates result:
		when(mockTemplate.queryForObject(SELECT_ITEM_RESPONSE_ID, Integer.class)).thenReturn(itemResponseId);
		when(mockTemplate.queryForObject(SELECT_ITEM_SET_ID, Integer.class, rosterId, subTestId)).thenReturn(itemSetId);
		when(mockTemplate.queryForObject(SELECT_MAX_RESPONSE_SEQUENCE_NUMBER, Integer.class, rosterId, itemSetId))
				.thenReturn(null);
		when(mockTemplate.update(
				INSERT_ITEM_RESPONSE,
				itemResponseId,
				rosterId,
				itemSetId,
				response.getItemCode(),
				response.getItemResponse(),
				response.getItemResponseTime(),
				1)).thenReturn(0);
		
		itemResponseDao.addItemResponses(rosterId, subTestId, itemResponseList);
	}
	
	@Test
	public void testItemResponseDAO_addItemResponses_addNothing() {
		final Integer rosterId = 101;
		final String subTestId = "FAKE_TEST_101_A";
		final List<ItemResponse> itemResponseList = new ArrayList<ItemResponse>();

		// Set up the updates result:
		when(mockTemplate.queryForObject(SELECT_ITEM_RESPONSE_ID, Integer.class)).thenReturn(10101);
		when(mockTemplate.queryForObject(SELECT_ITEM_SET_ID, Integer.class, rosterId, subTestId)).thenReturn(1001001);
		
		try {
			itemResponseDao.addItemResponses(rosterId, subTestId, itemResponseList);
		} catch (SQLException sqle) {
			fail("Shouldn't have thrown exception! Exception was: " + sqle.getMessage());
		}
		verify(mockTemplate, times(0)).update(
				anyString(),
				anyInt(),
				eq(rosterId),
				anyInt(),
				anyInt(),
				anyString(),
				anyInt());
	}

	@Test(expected = SQLException.class)
	public void testItemResponseDAO_addItemResponses_sequenceLookupFails() throws SQLException {
		final Integer rosterId = 101;
		final String subTestId = "FAKE_TEST_101_A";
		final ItemResponse response = new ItemResponse();
		response.setItemCode(10101);
		response.setItemResponse("A");
		response.setItemCode(10);
		response.setItemType("MC");
		final List<ItemResponse> itemResponseList = new ArrayList<ItemResponse>();
		itemResponseList.add(response);

		// Set up the updates result:
		when(mockTemplate.queryForObject(SELECT_ITEM_RESPONSE_ID, Integer.class))
				.thenThrow(new DataIntegrityViolationException("Database failed to return next item response ID."));
		
		itemResponseDao.addItemResponses(rosterId, subTestId, itemResponseList);
	}

	@Test(expected = SQLException.class)
	public void testItemResponseDAO_addItemResponses_ItemIdLookupFails() throws SQLException {
		final Integer rosterId = 101;
		final String subTestId = "FAKE_TEST_101_A";
		final ItemResponse response = new ItemResponse();
		response.setItemCode(10101);
		response.setItemResponse("A");
		response.setItemCode(10);
		response.setItemType("MC");
		final List<ItemResponse> itemResponseList = new ArrayList<ItemResponse>();
		itemResponseList.add(response);

		// Set up the updates result:
		when(mockTemplate.queryForObject(SELECT_ITEM_RESPONSE_ID, Integer.class)).thenReturn(10101);
		when(mockTemplate.queryForObject(SELECT_ITEM_SET_ID, Integer.class, rosterId, subTestId))
				.thenThrow(new DataIntegrityViolationException("No Such Item ID."));
		
		itemResponseDao.addItemResponses(rosterId, subTestId, itemResponseList);
	}
}
