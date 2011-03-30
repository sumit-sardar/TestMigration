package com.ctb.control.crscoring;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScorableCRAnswerContent;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.ScorableItemData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.ScoringException;
import com.ctb.exception.testAdmin.StudentNotAddedToSessionException;
import com.ctb.exception.testAdmin.TestAdminDataNotFoundException;
import com.ctb.exception.testAdmin.TestElementDataNotFoundException;
import com.ctb.util.Constants;
import com.ctb.util.OASLogger;
import com.ctb.util.XMLUtils;

/**
 * Platform control provides functions related to CR item scoring.
 * 
 * @author TCS
 */
@ControlImplementation(isTransient = true)
public class TestScoringImpl implements TestScoring {

	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.validation.Validator validator;

	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.CRScoring scoring;

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
	public RosterElementData getAllStudentForTestSession(Integer testAdminID,
			FilterParams filter, PageParams page, SortParams sort)
			throws CTBBusinessException {

		RosterElementData rosterElementData = new RosterElementData();
		Integer pageSize = null;
		try {
			if (page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			RosterElement[] rosterElements = getAllStudentForTestSession(testAdminID);
			rosterElementData.setRosterElements(rosterElements, pageSize);
			if (filter != null)
				rosterElementData.applyFiltering(filter);
			if (sort != null)
				rosterElementData.applySorting(sort);
			if (page != null)
				rosterElementData.applyPaging(page);
			return rosterElementData;
		} catch (SQLException se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting all Student for a TestSession.",
							se);
			StudentNotAddedToSessionException rde = new StudentNotAddedToSessionException(
					"TestScoringImpl: getAllStudentForTestSession: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		} catch (Exception se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting all Student for a TestSession.",
							se);
			StudentNotAddedToSessionException rde = new StudentNotAddedToSessionException(
					"TestScoringImpl: getAllStudentForTestSession: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		}

	}

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
	@Override
	public RosterElementData getAllStudentForTestSessionAndTD(
			Integer testAdminID, Integer itemSetId, FilterParams filter,
			PageParams page, SortParams sort) throws CTBBusinessException {
		RosterElementData rosterElementData = new RosterElementData();
		Integer pageSize = null;
		try {

			if (page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			RosterElement[] rosterElements = getAllStudentForTestSessionAndTD(
					testAdminID, itemSetId);
			rosterElementData.setRosterElements(rosterElements, pageSize);
			if (filter != null)
				rosterElementData.applyFiltering(filter);
			if (sort != null)
				rosterElementData.applySorting(sort);
			if (page != null)
				rosterElementData.applyPaging(page);
			return rosterElementData;
		} catch (SQLException se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting all Student for a TestSession and TD.",
							se);
			StudentNotAddedToSessionException rde = new StudentNotAddedToSessionException(
					"TestScoringImpl: getAllStudentForTestSessionAndTD: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		} catch (Exception se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting all Student for a TestSession and TD.",
							se);
			StudentNotAddedToSessionException rde = new StudentNotAddedToSessionException(
					"TestScoringImpl: getAllStudentForTestSessionAndTD: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		}

	}

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
	@Override
	public ScorableItemData getAllScorableCRItemsForTestRoster(Integer testRosterId, Integer itemSetId,
			 PageParams page, SortParams sort
			)
			throws CTBBusinessException {
		ScorableItemData scorableItemData = new ScorableItemData();
		Integer pageSize = null;
		try {

			if (page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			ScorableItem[] rosterElements = getAllScorableCRItemsForTestRoster(
					testRosterId, itemSetId);
			scorableItemData.setScorableItems(rosterElements, pageSize);
			if (sort != null)
				scorableItemData.applySorting(sort);
			if (page != null)
				scorableItemData.applyPaging(page);
			return scorableItemData;
		} catch (SQLException se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting all scorable CR Items for student and TD.",
							se);
			TestElementDataNotFoundException rde = new TestElementDataNotFoundException(
					"TestScoringImpl: getAllScorableCRItemsForTestRoster: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		} catch (Exception se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting all scorable CR Items for student and TD.",
							se);
			TestElementDataNotFoundException rde = new TestElementDataNotFoundException(
					"TestScoringImpl: getAllScorableCRItemsForTestRoster: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		}
	}

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
	@Override
	public ScorableItemData getAllScorableCRItemsForItemSet(
			FilterParams filter, PageParams page, SortParams sort,
			Integer testAdminId) throws CTBBusinessException {
		ScorableItemData scorableItemData = new ScorableItemData();
		Integer pageSize = null;
		try {

			if (page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			ScorableItem[] rosterElements = getAllScorableCRItemsForItemSet(testAdminId);
			scorableItemData.setScorableItems(rosterElements, pageSize);
			if (filter != null)
				scorableItemData.applyFiltering(filter);
			if (sort != null)
				scorableItemData.applySorting(sort);
			if (page != null)
				scorableItemData.applyPaging(page);
			return scorableItemData;
		} catch (SQLException se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting all scorable CR Items for TD.",
							se);
			TestAdminDataNotFoundException rde = new TestAdminDataNotFoundException(
					"TestScoringImpl: getAllStudentForTestSessionAndTD: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		} catch (Exception se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting all scorable CR Items for TD.",
							se);
			TestElementDataNotFoundException rde = new TestElementDataNotFoundException(
					"TestScoringImpl: getAllScorableCRItemsForTestRoster: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		}
	}

	/**
	 * Method retrieves CR item response for student, itemset
	 * 
	 * @param userName -
	 *            identifies the user
	 * @param testRosterId -
	 *            scheduled student
	 * @param deliverableItemSetId -
	 *            identifies the test of type TD
	 * @param itemId -
	 *            identifies item
	 * @return String response
	 * @throws CTBBusinessException
	 */
	@Override
	public ScorableCRAnswerContent getCRItemResponseForScoring(String userName,
			Integer testRosterId, Integer deliverableItemSetId, String itemId,
			String itemType) throws CTBBusinessException {
		String[] itemResponse = null;
		ScorableCRAnswerContent answerContent = new ScorableCRAnswerContent();
		validator.validateRoster(userName, testRosterId,
				"TestScoringImpl: getCRItemResponseForScoring");

		try {
			if (itemType.equals("CR")) {
				itemResponse = getCRItemResponse(testRosterId,
						deliverableItemSetId, itemId);
				answerContent.setIsAudioItem(false);
				answerContent.setCRItemContent(itemResponse);
			} else if (itemType.equals("AI")) {
				itemResponse = getAIItemResponse(testRosterId,
						deliverableItemSetId, itemId);
				answerContent.setIsAudioItem(true);
				answerContent.setAudioItemContent(itemResponse[0]);
			}
			answerContent.setTestRosterId(testRosterId);
			answerContent.setItemSetId(deliverableItemSetId);
			answerContent.setItemId(itemId);

		} catch (SQLException se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting scorable CR Items response.",
							se);
			ScoringException rde = new ScoringException(
					"TestScoringImpl: getCRItemResponseForScoring: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		} catch (Exception se) {
			OASLogger
					.getLogger("TestAdmin")
					.error(
							"Exception occurred while getting scorable CR Items response.",
							se);
			ScoringException rde = new ScoringException(
					"TestScoringImpl: getCRItemResponseForScoring: "
							+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		}
		return answerContent;
	}

	/**
	 * Retrieves array of active students for a test sessions.
	 * 
	 * @param testAdminID -
	 *            identifies the test session
	 * @return RosterElement
	 * @throws SQLException
	 */
	private RosterElement[] getAllStudentForTestSession(Integer testAdminID)
			throws SQLException {
		return scoring.getAllStudentForTestSession(testAdminID);
	}

	/**
	 * Method retrieves list of active students for a test sessions and item set .
	 * 
	 * @param testAdminID -
	 *            identifies the test session
	 * @param itemSetId -
	 *            identifies the itemSetId of type TD
	 * @return Array of RosterElement
	 * @throws SQLException
	 */
	private RosterElement[] getAllStudentForTestSessionAndTD(
			Integer testAdminID, Integer itemSetId) throws SQLException {
		return scoring.getAllStudentForTestSessionAndTD(testAdminID, itemSetId);
	}

	/**
	 * Method retrieves all scorable CR and Audio item from data base based on
	 * testroster and itemSet, returns as an array of ScorableItem.
	 * 
	 * @param testRosterId -
	 *            identifies the schedule student
	 * @param itemSetId -
	 *            identifies the test of type TD
	 * @return Array of ScorableItem
	 * @throws SQLException
	 */
	private ScorableItem[] getAllScorableCRItemsForTestRoster(
			Integer testRosterId, Integer itemSetId) throws SQLException {
		return scoring.getAllScorableCRItemsForTestRoster(testRosterId,
				itemSetId);
	}

	/**
	 * Method retrieves all scorable CR and Audio item from data base based on
	 * item id and returns as an array of ScorableItem.
	 * 
	 * @param itemSetId
	 * @return Array of ScorableItem
	 * @throws SQLException
	 */
	private ScorableItem[] getAllScorableCRItemsForItemSet(Integer testAdminId)
			throws SQLException {
		return scoring.getAllScorableCRItemsForItemSet(testAdminId);
	}

	/**
	 * Method retrieves audio response from data base and returns as an array of
	 * string.
	 * 
	 * @param testRosterId -
	 *            identifies the schedule student
	 * @param deliverableItemId -
	 *            identifies the test of type TD
	 * @param itemId -
	 *            identifies the Item
	 * @return String
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private String[] getAIItemResponse(Integer testRosterId,
			Integer deliverableItemId, String itemId) throws SQLException {
		return getAnswerStrValue(scoring.getAIItemResponse(testRosterId,
				deliverableItemId, itemId));
	}

	/*
	 * private String getCRItemResponse(Integer testRosterId, Integer
	 * deliverableItemId, String itemId) throws SQLException { String
	 * crItemResponse = scoring.getCRItemResponse(testRosterId,
	 * deliverableItemId, itemId); return crItemResponse; }
	 */

	/**
	 * Method retrieves CR response from data base and returns as an array of
	 * string.
	 * 
	 * @param testRosterId -
	 *            identifies the schedule student
	 * @param deliverableItemId -
	 *            identifies the test of type TD
	 * @param itemId -
	 *            identifies the Item
	 * @return String[]
	 * @throws SQLException
	 */
	private String[] getCRItemResponse(Integer testRosterId,
			Integer deliverableItemId, String itemId) throws Exception {
		String crResponse = "";
		Clob responseContent = null;
		List<String> cRItemResponse = new ArrayList<String>();
		responseContent = scoring.getCRItemResponse(testRosterId,
				deliverableItemId, itemId);
		int len = (int) responseContent.length();
		crResponse = responseContent.getSubString(1, len);
		cRItemResponse = extractAnswer(crResponse);
		return cRItemResponse.toArray(new String[cRItemResponse.size()]);
	}

	/**
	 * Method converts input clob to string.
	 * 
	 * @param responseContent -
	 *            Clob data
	 * @return String representation of input Clob
	 * @throws SQLException
	 */
	private String[] getAnswerStrValue(Clob responseContent)
			throws SQLException {
		String[] crResponse = new String[1];
		if (responseContent == null) {
			crResponse[0] = "";
		}
		int len = (int) responseContent.length();
		crResponse[0] = responseContent.getSubString(1, len);
		return crResponse;
	}

	/**
	 * Method reads raw xml, process CR answer and return answer content as list
	 * 
	 * @param responseXML -
	 *            saved answer XML
	 * @return - list of answer
	 * @throws Exception
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<String> extractAnswer(String responseXML) throws Exception {
		List<Element> list = null;
		List<String> itemResponseData = new ArrayList<String>();
		String processedResponseXML = XMLUtils.processHexString(responseXML);
		Document parsedDoc = XMLUtils.parse(processedResponseXML,
				Constants.ENCODING_UTF8);
		list = XMLUtils.extractAllElement("answer", parsedDoc.getRootElement());
		for (Element el : list) {
			boolean isAnswered = false;
			List<Object> listCh = el.getContent();
			for (Object e2 : listCh) {
				if (e2 instanceof CDATA) {
					isAnswered = true;
					itemResponseData.add(((CDATA) e2).getText());
				}
			}
			if (!isAnswered) {
				itemResponseData.add("");
			}
		}

		return itemResponseData;
	}

	/**
	 * This method retrieve testAdminDetails from data base.
	 * 
	 * @param testAdminId -
	 *            testAdminId
	 * @return - TestSession
	 * @throws SQLException
	 * @throws Exception
	 */
	TestSession getTestAdminDetails(Integer testAdminId) throws SQLException,
			Exception {
		TestSession testSession = scoring.getTestAdminDetails(testAdminId);
		return testSession;
	}

}