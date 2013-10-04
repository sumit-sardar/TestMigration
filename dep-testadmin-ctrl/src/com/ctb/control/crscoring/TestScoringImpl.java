package com.ctb.control.crscoring;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.ItemData;
import com.ctb.bean.testAdmin.ResponsePoints;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.RubricViewData;
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
	
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.testAdmin.ADS ads;
	
	@org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.testAdmin.CustomerConfigurations customerConfiguration;

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
	public RosterElementData getAllStudentForTestSession(Integer testAdminID,String userName,
			FilterParams filter, PageParams page, SortParams sort)
			throws CTBBusinessException {

		RosterElementData rosterElementData = new RosterElementData();
		Integer pageSize = null;
		try {
			if (page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			RosterElement[] rosterElements = getAllStudentForTestSession(testAdminID, userName);
			//Change for HandScoring: score by student to avoid multiple database hit for checking student status
			/*for (RosterElement rosterElement: rosterElements) {
				rosterElement.setScoringStatus(getScoringStatus(rosterElement.getTestRosterId(),rosterElement.getItemSetIdTC()));
            }*/
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
			Integer testAdminID, Integer itemSetId,String itemId, FilterParams filter,
			PageParams page, SortParams sort) throws CTBBusinessException {
		RosterElementData rosterElementData = new RosterElementData();
		Integer pageSize = null;
		try {

			if (page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			RosterElement[] rosterElements = getAllStudentForTestSessionAndTD(
					testAdminID, itemSetId, itemId);
			rosterElementData.setRosterElements(rosterElements, pageSize);
			for (RosterElement rosterElement: rosterElements) {
				 Integer scorePoints =  getScorePoints(itemSetId, itemId, rosterElement.getTestRosterId());
				 if(scorePoints == null){
					 rosterElement.setScoringStatus("IN");
				 } else {
					 rosterElement.setScoringStatus("CO"); 
				 }
				 rosterElement.setScorePoint(scorePoints);
				//rosterElement.setScoringStatus(getScoringStatus(rosterElement.getTestRosterId(),itemSetId));
            }
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
	public ScorableItemData getAllScorableCRItemsForTestRoster(
			Integer testRosterId, Integer itemSetId, PageParams page,
			SortParams sort) throws CTBBusinessException {
		ScorableItemData scorableItemData = new ScorableItemData();
		Integer pageSize = null;
		Map<Integer, TreeMap<String, ScorableItem>> map = new TreeMap<Integer, TreeMap<String, ScorableItem>>();
		try {

			if (page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			ScorableItem[] rosterElements = getAllScorableCRItemsForTestRoster(
					testRosterId, itemSetId);
			//START: Changes for item set order
			for(ScorableItem item: rosterElements ) {
				if (!map.containsKey(item.getItemSetId())){
					map.put(item.getItemSetId(), getAllScorableCRItemsForTD(item.getItemSetId()));
				}
				item.setItemSetOrder(map.get(item.getItemSetId()).get(item.getItemId()).getItemSetOrder());
			}
			//END: Changes for item set order
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
		Map<Integer, TreeMap<String, ScorableItem>> map = new TreeMap<Integer, TreeMap<String, ScorableItem>>();
		try {

			if (page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			ScorableItem[] rosterElements = getAllScorableCRItemsForItemSet(testAdminId);
			//START: Changes for item set order
			for(ScorableItem item: rosterElements ) {
				if (!map.containsKey(item.getItemSetId())){
					map.put(item.getItemSetId(), getAllScorableCRItemsForTD(item.getItemSetId()));
				}
				item.setItemSetOrder(map.get(item.getItemSetId()).get(item.getItemId()).getItemSetOrder());
			}
			//END: Changes for item set order
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
		/*validator.validateRoster(userName, testRosterId,
				"TestScoringImpl: getCRItemResponseForScoring");*/

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
	 * This method save or update a students points
	 * @param userId - user id
	 * @param itemId - item id
	 * @param itemSetIdTD - item set id TD type
	 * @param testRosterId - roster id
	 * @param point - point
	 * @throws CTBBusinessException
	 */
	@Override
	public Boolean saveOrUpdateScore(Integer userId, String itemId,
			Integer itemSetIdTD, Integer testRosterId, Integer point)
	throws CTBBusinessException {

		Boolean isSuccess  = new Boolean(false);
		try {
			ResponsePoints[] responsePoints = getResponseForScore(itemId,
					itemSetIdTD, testRosterId);

			if (responsePoints == null || responsePoints.length == 0) {
				OASLogger.getLogger("TestAdmin").error(
						"No valid response found for itemId["+itemId+"]"+" itemsetId["+itemSetIdTD+"] testRosterId["+testRosterId+"]");
				ScoringException rde = new ScoringException(
				"TestScoringImpl: saveOrUpdateScore: ");
				throw rde;
			}
			for (ResponsePoints points : responsePoints) {
				points.setCreatedBy(userId);
				points.setCreattionDate(new Date());
				points.setPoint(point);
				Integer noOfRows = saveOrUpdateScore(points);
				if(noOfRows > 0) {
					isSuccess = true;
				}
			}


		} catch (SQLException se) {
			OASLogger.getLogger("TestAdmin").error(
					"Exception occurred while saveing score.", se);
			ScoringException rde = new ScoringException(
					"TestScoringImpl: saveOrUpdateScore: "
					+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;
		} catch (Exception e) {

			OASLogger.getLogger("TestAdmin").error(
					"Exception occurred while saveing score.", e);
			ScoringException rde = new ScoringException(
					"TestScoringImpl: saveOrUpdateScore: "
					+ e.getMessage());
			e.printStackTrace();
			rde.setStackTrace(e.getStackTrace());
			throw rde;

		}
		finally{
			return isSuccess;

		}

	}


	/***
	 * Retrieves Decrypted Item XMl from ADS Database 
	 * 
	 * 
	 * 
	 */

	@Override
	public ItemData getItemXML(String itemId) throws CTBBusinessException{

		ItemData item = new ItemData();
		Blob itemXml = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StringBuilder str = new StringBuilder();


		try{

			ScorableItem [] scrItem = 	ads.getDecryptedItemXml(itemId);
			if(scrItem != null && scrItem.length > 0){
				itemXml = scrItem[0].getItemXml();

			}
			if(itemXml != null){
				InputStream is = itemXml.getBinaryStream();
				Reader reader =//new InputStreamReader(is, Charset.forName("UTF-8")); 
					new InputStreamReader(is, Charset.forName("ISO-8859-1"));
				
				 int data = reader.read();
				    while(data != -1){
				        char dataChar = (char) data;
				        data = reader.read();
				        str.append(dataChar);
				    }

			}
			item.setItem(str.toString().getBytes());
			item.setItemId(itemId);
			item.setCreatedDateTime(scrItem[0].getCreatedDateTime());
		}


		catch (Exception e){
			OASLogger.getLogger("TestAdmin").error(
					"Exception occurred while retrieving the item.", e);
		}
		return item;
	}
	
	/***
	 * Retrieves Decrypted Item XMl from MVIEW_AA_ITEM_DECRYPTED Database 
	 * 
	 * 
	 * 
	 */
	
	@Override
	public ItemData getItemXMLFromADSDev(String itemId) throws CTBBusinessException{

		ItemData item = new ItemData();
		Blob itemXml = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StringBuilder str = new StringBuilder();


		try{

			ScorableItem [] scrItem = 	ads.getDecryptedItemXmlFromADSDev(itemId);
			if(scrItem != null && scrItem.length > 0){
				itemXml = scrItem[0].getItemXml();

			}
			if(itemXml != null){
				InputStream is = itemXml.getBinaryStream();
				Reader reader =//new InputStreamReader(is, Charset.forName("UTF-8")); 
					new InputStreamReader(is, Charset.forName("ISO-8859-1"));
				
				 int data = reader.read();
				    while(data != -1){
				        char dataChar = (char) data;
				        data = reader.read();
				        str.append(dataChar);
				    }

			}
			item.setItem(str.toString().getBytes());
			item.setItemId(itemId);
			item.setCreatedDateTime(scrItem[0].getCreatedDateTime());
		}


		catch (Exception e){
			OASLogger.getLogger("TestAdmin").error(
					"Exception occurred while retrieving the item.", e);
		}
		return item;
	}



	/**
	 * Retrieves array of active students for a test sessions.
	 * 
	 * @param testAdminID -
	 *            identifies the test session
	 * @return RosterElement
	 * @throws SQLException
	 */
	private RosterElement[] getAllStudentForTestSession(Integer testAdminID, String userName)
	throws SQLException {
		return scoring.getAllStudentForTestSession(testAdminID , userName);
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
			Integer testAdminID, Integer itemSetId, String itemId) throws SQLException {
		return scoring.getAllStudentForTestSessionAndTD(testAdminID, itemSetId, itemId);
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
	 * @throws ScoringException 
	 * @throws SQLException
	 * @throws Exception
	 */
	public TestSession getTestAdminDetails(Integer testAdminId) throws CTBBusinessException {
		TestSession testSession = null;
		try {
			testSession = scoring.getTestAdminDetails(testAdminId);
		} catch (Exception e) {
			OASLogger.getLogger("TestAdmin").error(
					"Exception occurred while getting testadmin details.", e);
			ScoringException rde = new ScoringException(
			"TestScoringImpl:testDetails");

			rde.setStackTrace(e.getStackTrace());
			throw rde;
		}
		return testSession;
	}

	/**
	 * This method retrieve ResponsePoints from data base.
	 * 
	 * @param itemId -
	 *            itemId
	 * @param itemSetIdTD -
	 *            testAdminId
	 * @param testRosterId -
	 *            testRosterId
	 * @return - ResponsePoints
	 * @throws SQLException
	 * @throws Exception
	 */
	ResponsePoints[] getResponseForScore(String itemId, Integer itemSetIdTD,
			Integer testRosterId) throws SQLException, Exception {
		ResponsePoints[] responsePoints = scoring.getResponseForScore(itemId,
				itemSetIdTD, testRosterId);
		return responsePoints;
	}

	/**
	 * This method retrieve ResponsePoints from data base.
	 * 
	 * @param itemId -
	 *            itemId
	 * @param itemSetIdTD -
	 *            testAdminId
	 * @param testRosterId -
	 *            testRosterId
	 * @return - int
	 * @throws SQLException
	 * @throws Exception
	 */
	private int saveOrUpdateScore(ResponsePoints responsePoints) throws SQLException,
	Exception {
		int noOfRows= scoring.saveOrUpdateScore(responsePoints);

		return noOfRows;
	}


	/**
	 * Method retrieves score, sample and corresponding explanation for rubric view as per the itemid
	 * 
	 * @param itemId -
	 *            identifies item
	 * @return RubricViewData 
	 * @throws CTBBusinessException
	 */
	@Override
	public RubricViewData[] getRubricDetailsData(String itemId) throws CTBBusinessException {

		RubricViewData[] rubricData = null;

		try {		
			rubricData = scoring.getRubricDataDetails(itemId);
		} catch (SQLException se) {
			se.printStackTrace();//for defect#72205
			OASLogger
			.getLogger("TestAdmin")
			.error(
					"Exception occurred while getting rubric data.",
					se);
			ScoringException rde = new ScoringException(
					"TestScoringImpl: getRubricDetailsData: "
					+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		} catch (Exception se) {
			OASLogger
			.getLogger("TestAdmin")
			.error(
					"Exception occurred while getting rubric data.",
					se);
			ScoringException rde = new ScoringException(
					"TestScoringImpl: getRubricDetailsData: "
					+ se.getMessage());
			rde.setStackTrace(se.getStackTrace());
			throw rde;

		}
		return rubricData;
	}

	/**
	 * This method returns scoring status of student.
	 * @param rosterId
	 * @param itemSetIDTC
	 * @return
	 * @throws Exception
	 */
	private String getScoringStatus(int rosterId, int itemSetIDTC) throws Exception{
		return scoring.getScoringStatus(rosterId, itemSetIDTC);
	}

	private Integer getScorePoints(Integer itemSetId, String itemId,
			Integer testRosterId) throws Exception {
		return scoring.getScorePoints(itemSetId, itemId, testRosterId);
	}

	private TreeMap<String, ScorableItem> getAllScorableCRItemsForTD(
			Integer itemSetIdTD) throws SQLException {
		ScorableItem scorableItem[] = scoring.getAllScorableCRItemsForTD(itemSetIdTD);
		TreeMap<String, ScorableItem> map = new TreeMap<String, ScorableItem>();
		if (scorableItem!= null){
			for (ScorableItem item : scorableItem){
				map.put(item.getItemId(), item);
			}
		}
		return map;
	}

	@Override
	public String getParentProductId(String itemId) throws CTBBusinessException {
		String productId=null;
		try {
		  System.out.println("itemId"+itemId);	
		  productId = customerConfiguration.getParentProductId(itemId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productId;
	}


}