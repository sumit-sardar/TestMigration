package com.ctb.control.crscoring;

import org.apache.beehive.controls.api.bean.ControlInterface;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.RubricViewData; //Added for rubric view
import com.ctb.bean.testAdmin.ScorableCRAnswerContent;
import com.ctb.bean.testAdmin.ScorableItemData;
import com.ctb.bean.testAdmin.ItemData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.ScoringException;

/**
 * Platform control provides functions related to CR item scoring.
 * 
 * @author TCS
 */
@ControlInterface()
public interface TestScoring {

	
	
	/**
	 * Retrieves a filtered, sorted, paged list of active students for a test
	 * sessions.
	 * 
	 * @param userName -
	 *            identifies the user
	 * @param filter -
	 *            for filtering
	 * @param page -
	 *            for pagination
	 * @param sort -
	 *            for sorting
	 * @param testAdminID -
	 *            identifies the test session
	 * @return RosterElementData
	 * @throws CTBBusinessException
	 */
	public RosterElementData getAllStudentForTestSession(Integer testAdminID, String userName,
			FilterParams filter, PageParams page, SortParams sort
			) throws CTBBusinessException;

	/**
	 * Retrieves a filtered, sorted, paged list of active students for a test
	 * sessions and itemset id.
	 * 
	 * @param userName -
	 *            identifies the user
	 * @param filter -
	 *            for filtering
	 * @param page -
	 *            for pagination
	 * @param sort -
	 *            for sorting
	 * @param testAdminID -
	 *            identifies the test session
	 * @param itemSetId -
	 *            identifies the test of type TD
	 * @return RosterElementData
	 * @throws CTBBusinessException
	 */
	public RosterElementData getAllStudentForTestSessionAndTD(
			Integer testAdminID, Integer itemSetId,String itemId, FilterParams filter,
			PageParams page, SortParams sort) throws CTBBusinessException;

	/**
	 * Retrieves a filtered, sorted, paged list of items for a itemset and test
	 * session.
	 * 
	 * @param userName -
	 *            identifies the user
	 * @param filter -
	 *            for filtering
	 * @param page -
	 *            for pagination
	 * @param sort -
	 *            for sorting
	 * @param testRosterId -
	 *            identifies the test session
	 * @param itemSetId -
	 *            identifies the test of type TD
	 * @return ScorableItemData
	 * @throws CTBBusinessException
	 */
	public ScorableItemData getAllScorableCRItemsForTestRoster(Integer testRestorId, Integer itemSetId,
			 PageParams page, SortParams sort
			) throws CTBBusinessException;

	/**
	 * Retrieves a filtered, sorted, paged list of items for a TD and test
	 * session.
	 * 
	 * @param userName -
	 *            identifies the user
	 * @param filter -
	 *            for filtering
	 * @param page -
	 *            for pagination
	 * @param sort -
	 *            for sorting
	 * @param itemSetId -
	 *            identifies the test of type TD
	 * @return ScorableItemData
	 * @throws CTBBusinessException
	 */
	public ScorableItemData getAllScorableCRItemsForItemSet(
			FilterParams filter, PageParams page, SortParams sort,
			Integer itemSetId) throws CTBBusinessException;

	/**
	 * Retrieves CR item response for student
	 * 
	 * @param userName -
	 *            identifies the user
	 * @param testRosterId -
	 *            scheduled student
	 * @param deliverableItemId -
	 *            identifies the test of type TD
	 * @param itemId -
	 *            identifies item
	 * @return String response
	 * @throws CTBBusinessException
	 */
	public ScorableCRAnswerContent getCRItemResponseForScoring(String userName,
			Integer testRosterId, Integer deliverableItemId, String itemId,
			String itemType) throws CTBBusinessException;

	/**
	 * This method save or update a students points
	 * @param userId - user id
	 * @param itemId - item id
	 * @param itemSetIdTD - item set id TD type
	 * @param testRosterId - roster id
	 * @param point - point
	 * @throws CTBBusinessException
	 */
	public Boolean saveOrUpdateScore(Integer userId, String itemId, Integer itemSetIdTD,
			Integer testRosterId, Integer point) throws CTBBusinessException;
	
	/**
	 * This method retrieves the rubric data for view purpose
	 * @param itemId - item id
	 * @return RubricViewData
	 * @throws CTBBusinessException
	 */
	public RubricViewData[] getRubricDetailsData(String itemId) throws CTBBusinessException;

	
	/**
	 * This method retrieves the item xml data for view the Item 
	 * @param itemId - item id
	 * @return ItemData
	 * @throws CTBBusinessException
	 */
	public ItemData getItemXML(String itemId) throws CTBBusinessException;
	
	public TestSession getTestAdminDetails(Integer testAdminId) throws CTBBusinessException;
	
	public String getStatusForRosterAndCRTDs(Integer testRestorId, Integer itemSetIdTC) throws CTBBusinessException;
	

}