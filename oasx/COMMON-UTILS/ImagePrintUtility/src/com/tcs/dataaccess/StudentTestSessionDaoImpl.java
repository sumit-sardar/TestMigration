package com.tcs.dataaccess;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.xmlbeans.XmlObject;
import org.jdom.CDATA;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.tcs.Main;
import com.tcs.conversion.HTMLToPDFConversion;
import com.tcs.conversion.UTF8CharConversion;
import com.tcs.conversion.XMLUtils;
import com.tcs.executor.SingleReportGeneratingThread;
import com.tcs.model.ItemObject;
import com.tcs.model.SubTestObject;
import com.tcs.model.TestObject;
import com.tcs.model.TestRosterObject;
import com.tcs.parser.SaxParserHandler;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class StudentTestSessionDaoImpl
implements StudentTestSessionDao
{
	private static Logger slf4jLogger = LoggerFactory.getLogger(StudentTestSessionDaoImpl.class);
	public SAXBuilder saxBuilder = new SAXBuilder();
	HashMap<String, byte[]> assetMap = new HashMap();

	public String[] getDistinctTestAdminIdPerProduct(ConnectionManager oasConnManager, String productId, String customerId, String districtCode)
			throws Exception
			{
		ResultSet rSet = null;
		PreparedStatement statemnt = null;
		Connection oasConn = null;
		String[] testAdminIdArray = (String[])null;
		String modifiedQueryString = " select distinct ta.test_admin_id   from test_roster       tr,        test_admin        ta,        org_node          district,        org_node_ancestor ona,       org_node_category onc  where tr.org_node_id = ona.org_node_id    and ona.ancestor_org_node_id = district.org_node_id    and district.org_node_category_id = onc.org_node_category_id    and lower(onc.category_name) in ('district', 'corporation')    and tr.customer_id = ?    and tr.test_admin_id = ta.test_admin_id    and ta.product_id = ?    and TR.TEST_COMPLETION_STATUS NOT IN ('SC', 'NT', 'IP') ";
		Long startTime = Long.valueOf(System.currentTimeMillis());
		try {
			oasConn = oasConnManager.getConnection();

			if ((districtCode != null) && (districtCode.trim().length() > 0))
				modifiedQueryString = modifiedQueryString + " and district.org_node_code = ? ";
			statemnt = oasConn.prepareStatement(modifiedQueryString);
			statemnt.setInt(1, Integer.parseInt(customerId));
			statemnt.setInt(2, Integer.parseInt(productId));
			if ((districtCode != null) && (districtCode.trim().length() > 0))
				statemnt.setString(3, districtCode);
			rSet = statemnt.executeQuery();
			List testAdminIdList = new ArrayList();
			while (rSet.next()) {
				testAdminIdList.add(rSet.getString("TEST_ADMIN_ID"));
			}
			if (testAdminIdList.size() > 0)
				testAdminIdArray = (String[])testAdminIdList.toArray(new String[testAdminIdList.size()]);
			slf4jLogger.info("Time taken to get TestAdmin Ids " + (System.currentTimeMillis() - startTime.longValue()) + "ms");
			slf4jLogger.info("TestAdmin IDs :: " + testAdminIdList);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new Exception(sqle);
		} finally {
			oasConnManager.close(oasConn);
			oasConnManager.close(rSet);
			oasConnManager.close(statemnt);
		}

		return testAdminIdArray;
			}

	public Set<TestRosterObject> getItemIdsFromItemSet(ConnectionManager oasConnManager, ConnectionManager adsConnManager, Integer testAdminId)
			throws Exception
			{
		slf4jLogger.info("Fetching ADS item ids.... for test admin id " + testAdminId);
		TestObject testObject = new TestObject();
		Map<Integer, String> subTestMap = new HashMap<Integer, String>();
		Map<Integer, List<String>> subTestItemMap = new HashMap<Integer, List<String>>();
		Map<String, ItemObject> itemMap = new HashMap<String, ItemObject>();
		Set<TestRosterObject> testRosterDetails = new HashSet<TestRosterObject>();
		ResultSet rSet = null;
		PreparedStatement statemnt = null;
		Connection oasConn = null;
		List<String> itemIds = new ArrayList<String>();
		String[] arrAdsItemIds = null;
		String commodityCode = null;
		Map<Integer,Integer> subtestOrderMap =  new TreeMap<Integer, Integer>();
		Map<Integer,String> itemObjectOrderMap =  new TreeMap<Integer,String>();
		try {
			oasConn = oasConnManager.getConnection();
			statemnt = oasConn.prepareStatement("SELECT TC.PRODUCT_ID      AS PRODUCT_ID,       TC.TEST_NAME       AS TEST_NAME,       TA.TEST_ADMIN_NAME AS TEST_SESSION_NAME,       TC.ITEM_SET_ID     AS TEST_CATALOG_ID,       ISA.ITEM_SET_ID    AS SUBTEST_ID,       ISET.ITEM_SET_NAME AS SUB_TEST_NAME,       I.ITEM_ID          AS ADS_ITEM_ID,       DERIVED.ITEM_NO    AS QUESTION_ORDER  FROM ITEM_SET_ANCESTOR ISA,       TEST_CATALOG TC,       ITEM_SET_ITEM ISI,       ITEM I,       ITEM_SET ISET,       TEST_ADMIN TA,       (SELECT (ISI.ITEM_SET_ID * 75 /*assumption: each subtest has max */ + ISI.ITEM_SORT_ORDER)  ITEM_NO, ISI.ITEM_ID ITEM_ID          FROM ITEM_SET_ITEM ISI,               (SELECT ROWNUM TD_NUM, ISP.ITEM_SET_ID ITEM_SET_ID                  FROM ITEM_SET_PARENT ISP                 WHERE PARENT_ITEM_SET_ID IN                       (SELECT TS_SET.ITEM_SET_ID                          FROM ITEM_SET TS_SET,                               (SELECT ISA.ITEM_SET_ID                                  FROM ITEM_SET_ANCESTOR ISA, TEST_ADMIN ADM                                 WHERE ISA.ANCESTOR_ITEM_SET_ID =                                      ADM.ITEM_SET_ID                                   AND ADM.TEST_ADMIN_ID = ?                                   AND ITEM_SET_TYPE = 'TS'                                 ORDER BY ITEM_SET_SORT_ORDER) TS_ORDER                         WHERE TS_SET.ITEM_SET_ID = TS_ORDER.ITEM_SET_ID)) TD_ORDER         WHERE TD_ORDER.ITEM_SET_ID = ISI.ITEM_SET_ID) DERIVED WHERE DERIVED.ITEM_ID = I.ITEM_ID   AND TC.ITEM_SET_ID = ISA.ANCESTOR_ITEM_SET_ID   AND ISA.ITEM_SET_TYPE = 'TD'   AND TC.PRODUCT_ID = "+Main.PRODUCT_ID+"   AND ISA.ITEM_SET_ID = ISI.ITEM_SET_ID   AND ISI.ITEM_ID = I.ITEM_ID   AND ISA.ITEM_SET_ID = ISET.ITEM_SET_ID   AND I.ITEM_TYPE = 'CR'   AND ISET.SAMPLE = 'F'   AND TA.ITEM_SET_ID = TC.ITEM_SET_ID   AND I.ANSWER_AREA IS NULL   AND TA.TEST_ADMIN_ID = ? ORDER BY QUESTION_ORDER ASC",
								ResultSet.TYPE_SCROLL_SENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
			statemnt.setInt(1, testAdminId.intValue());
			statemnt.setInt(2, testAdminId.intValue());
			rSet = statemnt.executeQuery();
			if(!rSet.next()){
				throw new Exception("Error :: Test Details for AdminId " + testAdminId.intValue() + " not found.....");
			}
			rSet.beforeFirst();
			while (rSet.next()) {

					testObject.setProductId(Integer.valueOf(rSet.getInt("PRODUCT_ID")));
				if (testObject.getTestCatalogId() == null)
					testObject.setTestCatalogId(Integer.valueOf(rSet.getInt("TEST_CATALOG_ID")));
				if (testObject.getTestName() == null)
					testObject.setTestName(rSet.getString("TEST_NAME"));
				if (testObject.getTestSessionName() == null) {
					testObject.setTestSessionName(rSet
							.getString("TEST_SESSION_NAME"));
				}

				commodityCode = getKeyFromValue(Main.commodityCodeMap, testObject.getTestName().trim());
				slf4jLogger.info("CommodityCode ::  " + commodityCode + " TestName :: " + testObject.getTestName().trim());
				if (commodityCode == null) {
					throw new Exception("Error :: " + testObject.getTestName() + " doesn't have matching Commodity Code...");
				}
				testObject.setCommodityCode(commodityCode);

				subTestMap.put(Integer.valueOf(rSet.getInt("SUBTEST_ID")), rSet
						.getString("SUB_TEST_NAME"));

				if (subTestItemMap.get(Integer.valueOf(rSet.getInt("SUBTEST_ID"))) != null) {
					((List)subTestItemMap.get(Integer.valueOf(rSet.getInt("SUBTEST_ID")))).add(
							rSet.getString("ADS_ITEM_ID"));
				} else {
					List adsItemList = new ArrayList();
					adsItemList.add(rSet.getString("ADS_ITEM_ID"));
					subTestItemMap.put(Integer.valueOf(rSet.getInt("SUBTEST_ID")), adsItemList);
				}

				String adsItemId = rSet.getString("ADS_ITEM_ID");
				itemIds.add(adsItemId);

				if (subtestOrderMap.get(Integer.valueOf(rSet.getInt("QUESTION_ORDER"))) == null) {
					subtestOrderMap.put(Integer.valueOf(rSet.getInt("QUESTION_ORDER")), Integer.valueOf(rSet.getInt("SUBTEST_ID")));
				}

				if (itemObjectOrderMap.get(Integer.valueOf(rSet.getInt("QUESTION_ORDER"))) == null) {
					itemObjectOrderMap.put(Integer.valueOf(rSet.getInt("QUESTION_ORDER")), rSet.getString("ADS_ITEM_ID"));
				}
			}

			arrAdsItemIds = (String[])itemIds.toArray(new String[itemIds.size()]);
			List<ItemObject> itemList = getItemListFromADS(adsConnManager, 
					arrAdsItemIds);

			for (ItemObject itemObject : itemList) {
				if (itemMap.get(itemObject.getItemId()) == null) {
					itemMap.put(itemObject.getItemId(), itemObject);
				}

			}

			List subTestList = new ArrayList();
			for (Integer subtestId : subTestMap.keySet()) {
				SubTestObject subTestObject = new SubTestObject();
				subTestObject.setSubTestId(subtestId);
				subTestObject.setSubTestName((String)subTestMap.get(subtestId));
				subTestObject.setItemObjectOrderMap(itemObjectOrderMap);
				List adsItemSet = new ArrayList();
				for (String adsItemId : subTestItemMap.get(subtestId)) {
					adsItemSet.add((ItemObject)itemMap.get(adsItemId));
				}
				subTestObject.setAdsItemObjectList(adsItemSet);
				subTestList.add(subTestObject);
			}
			testObject.setSubTestList(subTestList);
			testObject.setSubtestOrderMap(subtestOrderMap);

			slf4jLogger.info("Fetching All Roster Details.... for test admin id " + testAdminId + " started...");
			testRosterDetails = getTotalRosterDetails(
					oasConnManager, testAdminId, testObject);
			slf4jLogger.info("Fetching All Roster Details.... for test admin id " + testAdminId + " completed...");
			slf4jLogger.info("Fetching ADS item ids.... for test admin id " + testAdminId + " completed...");
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new Exception(sqle);
		} finally {
			oasConnManager.close(oasConn);
			oasConnManager.close(rSet);
			oasConnManager.close(statemnt);
		}

		return testRosterDetails;
			}

	private Set<TestRosterObject> getTotalRosterDetails(ConnectionManager oasConnManager, Integer testAdminId, TestObject testObject)
	{
		int studentCounter = 0;

		TestRosterObject objRoster = null;
		Set rosterObjList = new HashSet();
		Map rosterMap = new HashMap();
		Connection oasConn = null;
		PreparedStatement statemnt = null;
		ResultSet rSet = null;
		Integer testrosterId = null;
		String crResponse = null;
		long startTime = System.currentTimeMillis();
		ExecutorService executorService = Executors.newFixedThreadPool(Main.THREAD_COUNT.intValue());
		try {
			oasConn = oasConnManager.getConnection();
			statemnt = oasConn
					.prepareStatement("SELECT ST.FIRST_NAME || ' ' || ST.MIDDLE_NAME || ' ' || ST.LAST_NAME AS STUDENT_NAME,       ST.EXT_PIN1 PIN,       ST.STUDENT_ID AS STUDENT_ID,       ST.GRADE AS GRADE,       TR.TEST_ROSTER_ID AS TEST_ROSTER_ID,            OG.ORG_NODE_CODE,       OG.ORG_NODE_NAME  FROM TEST_ADMIN        TA,       TEST_ROSTER       TR,            STUDENT           ST,       ORG_NODE_STUDENT  ONS,       ORG_NODE          OG,       ORG_NODE_ANCESTOR ONA,       ORG_NODE_CATEGORY ONC WHERE TA.TEST_ADMIN_ID = ?   AND TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID   AND TR.TEST_COMPLETION_STATUS NOT IN ('SC', 'NT', 'IP')   AND ST.STUDENT_ID = TR.STUDENT_ID   AND ONS.STUDENT_ID = ST.STUDENT_ID   AND ONS.ORG_NODE_ID = TR.ORG_NODE_ID   AND ONA.ORG_NODE_ID = ONS.ORG_NODE_ID   AND ONC.CUSTOMER_ID = TA.CUSTOMER_ID   AND ONC.CATEGORY_LEVEL = 2   AND OG.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID   AND OG.ORG_NODE_ID = ONA.ANCESTOR_ORG_NODE_ID   AND OG.ACTIVATION_STATUS = 'AC'   ORDER BY TR.TEST_ROSTER_ID");
			statemnt.setInt(1, testAdminId.intValue());
			rSet = statemnt.executeQuery();

			while (rSet.next()) {
				objRoster = new TestRosterObject();

				testrosterId = Integer.valueOf(rSet.getInt("TEST_ROSTER_ID"));
				objRoster.setTestRosterId(testrosterId);
				objRoster.setStudentName(rSet.getString("STUDENT_NAME"));
				objRoster.setStudentId(Integer.valueOf(rSet.getInt("STUDENT_ID")));
				objRoster.setGrade(rSet.getString("GRADE"));
				objRoster.setObjTest(testObject);
				objRoster.setDistrictCode(rSet.getString("ORG_NODE_CODE"));
				objRoster.setDistrictName(rSet.getString("ORG_NODE_NAME"));
				objRoster.setStudentPinNumber(rSet.getString("PIN"));

				objRoster.setResponseItemObjectList(getItemResponseObjectForRosterObject(oasConnManager, testrosterId));

				slf4jLogger.info("Fetching rosters... for testadminid " + testAdminId + " completed in " + (
						System.currentTimeMillis() - startTime) + "ms");
				studentCounter = Main.getStudentCounter();
				slf4jLogger.info("StudentCounter # " + studentCounter);
				SingleReportGeneratingThread thread = new SingleReportGeneratingThread(objRoster, studentCounter);
				executorService.execute(thread);
			}

			executorService.shutdown();

			while (!executorService.isTerminated());
			if (executorService.isTerminated())
				slf4jLogger.info("Total number of PDF generated # " + studentCounter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return rosterObjList;
	}

	public void createReport(TestRosterObject rosterObj, int studentCounter) throws Exception
	{
		long startOfIndividualTime = System.currentTimeMillis();
		String finalHtml = updateAnswerInItem(rosterObj, studentCounter++);
		finalHtml = finalHtml.replaceAll("&nbsp;", "&#160;").replaceAll("&bull;", "&#8226;")
				.replaceAll("<br>", "<br/>").replaceAll("&ldquo;", "&#8220;").replaceAll("&rdquo;", "&#8221;")
				.replaceAll("&rsquo;", "&#8217;").replaceAll("&lsquo;", "&#8216;").replaceAll("&ndash;", "&#8211;").replaceAll("&#13;&#10;", "<br/>")
				.replaceAll("&mdash;", "&#8212;").replaceAll("&sbquo;", "&#8212;").replaceAll("&bdquo;", "&#8222;")
				.replaceAll("<br/>", "&#10;").replaceAll("&lt;br /&gt;", "&#10;")
				.replaceAll("&middot;", "&#10; &#8226;").replaceAll("&#147;", "&#8220;")
				.replaceAll("&quot;", "&#34;").replaceAll("&#42", "<b>*</b>")
				.replaceAll("&lt;b&gt;", "<b>").replaceAll("&lt;/b&gt;", "</b>")
				.replaceAll("&lt;sup&gt;", "<sup>").replaceAll("&lt;/sup&gt;", "</sup>").replaceAll("&acute;", "&#8217;")
				.replaceAll("&reg;", "&#174;").replaceAll("&atilde", "&#195;");

		slf4jLogger.info("updating html took " + (System.currentTimeMillis() - startOfIndividualTime) + "ms");

		HTMLToPDFConversion.createPDFFromHTML(finalHtml, "OAS" + rosterObj.getObjTest().getCommodityCode() + rosterObj.getTestRosterId(), 
				Main.BASE_DIR + System.getProperty("file.separator") + rosterObj.getDistrictCode());

		slf4jLogger.info("Time Taken to create PDF is  " + (System.currentTimeMillis() - startOfIndividualTime) + "ms");
		Main.corporationSet.add(rosterObj.getDistrictCode().toString());
	}

	private List<ItemObject> getItemResponseObjectForRosterObject(ConnectionManager oasConnManager, Integer testRosterId) throws Exception {
		List answers = new ArrayList();
		ItemObject objItemResponse = null;
		Connection oasConn = null;
		PreparedStatement statemnt = null;
		ResultSet rSet = null;
		String crResponse = null;
		try {
			oasConn = oasConnManager.getConnection();
			statemnt = oasConn.prepareStatement(" select IRC.ITEM_ID              AS ITEM_ID,         IRC.CONSTRUCTED_RESPONSE AS CONSTRUCTED_RESPONSE                  from ITEM_RESPONSE_CR IRC         where IRC.TEST_ROSTER_ID = ?");
			statemnt.setInt(1, testRosterId.intValue());
			rSet = statemnt.executeQuery();

			while (rSet.next()) {
				objItemResponse = new ItemObject();
				objItemResponse.setItemId(rSet.getString("ITEM_ID"));
				int len = (int)rSet.getClob("CONSTRUCTED_RESPONSE").length();
				crResponse = extractAnswer(rSet.getClob("CONSTRUCTED_RESPONSE").getSubString(1L, len), objItemResponse);
				objItemResponse.setItemObject(crResponse);
				answers.add(objItemResponse);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return answers;
	}

	public List<ItemObject> getItemListFromADS(ConnectionManager adsConnManager, String[] arrAdsItemIds)
			throws Exception
			{
		List listOfItems = new ArrayList();
		ItemObject adsItemObj = null;
		String imageId = null;
		Connection conn = null;
		ResultSet result = null;
		PreparedStatement stmt = null;
		try {
			conn = adsConnManager.getConnection();
			int inClauselimit = 999;

			int loopCounters = arrAdsItemIds.length / inClauselimit;
			if (arrAdsItemIds.length % inClauselimit > 0) {
				loopCounters++;
			}
			StringBuilder adsIdsStr = new StringBuilder();
			for (int counter = 0; counter < loopCounters; counter++)
			{
				adsIdsStr.append("SELECT AA_ITEM_ID,ITEM_DELIVERY_XML_BLOB FROM AA_ITEM WHERE ");
				adsIdsStr.append("AA_ITEM_ID IN (");
				for (String adsItemId : arrAdsItemIds) {
					adsIdsStr.append("'" + adsItemId + "',");
				}

				String query = adsIdsStr.toString();
				query = query.substring(0, adsIdsStr.lastIndexOf(","));
				query = query + ") order by AA_ITEM.DATE_ROW_INSERTED asc";
				stmt = conn.prepareStatement(query);
				result = stmt.executeQuery();
				while (result.next()) {
					adsItemObj = new ItemObject();
					Blob blobData = result
							.getBlob("ITEM_DELIVERY_XML_BLOB");
					byte[] data = convertBLOBToByteArray(blobData);

					blobData.free();

					String itemXML = new String(data);

					itemXML = doUTF8Chars(itemXML);

					byte[] itemEncodedXML = itemXML.getBytes("UTF-8");

					org.jdom.Document itemDoc = null;
					synchronized (this.saxBuilder) {
						itemDoc = this.saxBuilder.build(
								new ByteArrayInputStream(itemEncodedXML));
					}
					org.jdom.Element element = itemDoc
							.getRootElement();
					element = element.getChild("assets");
					if (element != null) {
						List imageList = element.getChildren();
						for (int i = 0; i < imageList.size(); i++) {
							element = (org.jdom.Element)imageList.get(i);
							imageId = element.getAttributeValue("id");

							if (!this.assetMap.containsKey(imageId)) {
								createImageData(imageId, element);
							}

						}

						String itemxml = updateItem(itemEncodedXML, this.assetMap);

						adsItemObj.setItemObject(itemxml);
					} else {
						adsItemObj.setItemObject(itemXML);
					}
					adsItemObj.setItemId(result.getString("AA_ITEM_ID"));
					listOfItems.add(adsItemObj);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			adsConnManager.close(conn);
			adsConnManager.close(result);
			adsConnManager.close(stmt);
		}
		return listOfItems;
			}

	private byte[] convertBLOBToByteArray(Blob itemXml) throws Exception {
		StringBuilder str = new StringBuilder();
		if (itemXml != null) {
			InputStream is = itemXml.getBinaryStream();
			Reader reader = new InputStreamReader(is, 
					Charset.forName("ISO-8859-1"));

			int data = reader.read();
			while (data != -1) {
				char dataChar = (char)data;
				data = reader.read();
				str.append(dataChar);
			}
		}
		return str.toString().getBytes();
	}

	private byte[] convertCLOBToByteArray(Clob answers) throws Exception
	{
		StringBuilder str = new StringBuilder();
		if (answers != null) {
			Reader reader = answers.getCharacterStream();

			int data = reader.read();
			while (data != -1) {
				char dataChar = (char)data;
				data = reader.read();
				str.append(dataChar);
			}
		}
		return str.toString().getBytes();
	}

	public static String doUTF8Chars(String input)
	{
		int lineFeed = 10;
		int carriageReturn = 13;
		int tab = 9;
		int plusSign = 43;
		int maxASCII = 127;
		int space = 127;
		StringBuffer retVal = new StringBuffer(input.length() * 2);
		boolean isPreviousCharSpace = false;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			int intc = c;
			if ((intc != 9) && (intc != 10) && (intc != 13)) {
				if ((intc <= 127) && (intc != 43)) {
					if (intc == 127) {
						if (!isPreviousCharSpace) {
							retVal.append(c);
							isPreviousCharSpace = true;
						}
					} else {
						isPreviousCharSpace = false;
						retVal.append(c);
					}
				} else {
					isPreviousCharSpace = false;
					retVal.append("&#").append(intc).append(';');
				}
			}
		}
		String s = retVal.toString();
		s = replaceAll(s, "&#+;", "&#x002B;");
		s = replaceAll(s, "+", "&#x002B;");
		s = replaceAll(s, "&#x2011;", "&#8211;");
		s = s.replaceAll("&#x003C", "&LT;");
		s = s.replaceAll("&lt;", "&LT;");
		s = replaceAll(s, "&#x2008;", "&#160;");
		s = s.replaceAll("&#x2022;", "&#183;");
		s = s.replaceAll("&#x201C;", "&#34;");
		s = s.replaceAll("&#x201D;", "&#34;");
		s = s.replaceAll("&#x2019;", "&#39;");
		s = s.replaceAll("&#x2014;", "&#45;");
		s = s.replaceAll("&#8226;", "&#183;");
		s = s.replaceAll("&#8211;", "&#45;");
		s = s.replaceAll("&#8212;", "&#45;");
		s = s.replaceAll("&#9329;&#9338;", "");
		s = s.replaceAll("&#9329;&#9338;", "");
		s = s.replaceAll("&#9330;&#9338;", "");
		s = s.replaceAll("&#9331;&#9338;", "");
		s = s.replaceAll("&#9332;&#9338;", "");
		s = s.replaceAll("&#9333;&#9338;", "");
		s = s.replaceAll("&#9334;&#9338;", "");
		s = s.replaceAll("&#9335;&#9338;", "");
		s = s.replaceAll("&#9336;&#9338;", "");
		s = s.replaceAll("&#9337;&#9338;", "");
		s = s.replaceAll("<b>", "&lt;b&gt;");
		s = s.replaceAll("</b>", "&lt;/b&gt;");
		s = s.replaceAll("&#x2006;", "");
		s = s.replaceAll("&#x2713;", "");

		return s;
	}

	public static String replaceAll(String src, String toBeReplace, String replaceWith)
	{
		String result = src;
		int index = 0;
		int difference = replaceWith.length();
		while ((index = result.indexOf(toBeReplace, index)) >= 0) {
			result = result.substring(0, index) + replaceWith + 
					result.substring(index + toBeReplace.length());
			index += difference;
		}
		return result;
	}

	private void createImageData(String imageId, org.jdom.Element element) {
		String mimeType = element.getAttributeValue("type");
		String ext = mimeType.substring(mimeType.lastIndexOf("/") + 1);
		String b64data = element.getText();
		b64data = replaceAll(b64data, "&#43;", "+");

		byte[] imageData = Base64.decode(b64data);
		this.assetMap.put(imageId, imageData);
	}

	private String updateItem(byte[] itemBytes, HashMap assetMap)
			throws Exception
			{
		org.jdom.Document itemDoc = null;
		synchronized (this.saxBuilder) {
			itemDoc = this.saxBuilder.build(new ByteArrayInputStream(itemBytes));
		}
		org.jdom.Element rootElement = itemDoc
				.getRootElement();
		if (rootElement.getChild("assets") != null)
			rootElement.getChild("assets").detach();
		List items = extractAllElement(".//image_widget", rootElement);
		for (int i = 0; i < items.size(); i++) {
			org.jdom.Element element = (org.jdom.Element)items.get(i);
			String id = element.getAttributeValue("image_ref");
			if ((id != null) && (assetMap.containsKey(id)))
				element.setAttribute("src", id);
		}
		XMLOutputter aXMLOutputter = new XMLOutputter();
		StringWriter aStringWriter = new StringWriter();
		aXMLOutputter.output(rootElement, aStringWriter);
		return aStringWriter.getBuffer().toString();
			}

	private List extractAllElement(String pattern, org.jdom.Element element)
			throws Exception
			{
		ArrayList results = new ArrayList();
		pattern = pattern.substring(pattern.indexOf(".//") + 3);
		List children = element.getChildren();
		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			org.jdom.Element elem = (org.jdom.Element)iterator.next();
			if (pattern.equals(elem.getName())) {
				results.add(elem);
			}
			results.addAll(extractAllElement(".//" + pattern, elem));
		}
		return results;
			}

	public String getHTMLWithoutAnswerAppended(TestRosterObject rosterObj)
			throws Exception
			{
		String convertedHTML = "";
		String question = "";
		List<SubTestObject> subtestItemObjects = rosterObj.getObjTest().getSubTestList();
		for (SubTestObject subTestObj : subtestItemObjects) {
			List questionObjList = subTestObj.getAdsItemObjectList();
			convertedHTML = doSaxConversion(rosterObj, questionObjList, 0);
		}
		return convertedHTML;
			}

	public String updateAnswerInItem(TestRosterObject rosterObj, int studentCounter) throws Exception {
		String finalXML = null;
		List<SubTestObject> subtestItemObjects = rosterObj.getObjTest().getSubTestList();
		Map<String, String> itemQsnMap = new HashMap<String, String>();
		List<ItemObject> finalItemsWithAns = new ArrayList<ItemObject>();

		for (SubTestObject subTestObj : subtestItemObjects) {
			List<ItemObject> questionObj = subTestObj.getAdsItemObjectList();
			for (ItemObject itemObj : questionObj) {
				if (itemObj != null) {
					itemQsnMap.put(itemObj.getItemId(), itemObj.getItemObject());

					ItemObject itemObject = new ItemObject();
					String question = itemObj.getItemObject();

					itemObject.setItemId(itemObj.getItemId());
					itemObject.setItemObject(question);
					finalItemsWithAns.add(itemObject);
				}
			}
		}

		finalXML = doSaxConversion(rosterObj, finalItemsWithAns, studentCounter++);

		return finalXML;
	}

	private String doSaxConversion(TestRosterObject rosterObject, List<ItemObject> itemQsnObj, int studentCounter)
			throws Exception
			{

		Map<Integer,Integer> subtestOrderMap = rosterObject.getObjTest().getSubtestOrderMap();
		List<SubTestObject> subtestList = null;
		Map<Integer,String> itemObjectOrderMap = null;
		Map<String, String> itemAnsMap = new HashMap<String, String>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		String itemId = "";
		ItemObject tempItem = null;
		String htmlString = "";
		String tempString = "";
		String currentSubTestName = "";
		String previousSubTestName = "";
		int questionCounter = 1;
		int superScriptcount = 1;

		htmlString = htmlString + "<html><head><style>" + Main.CSS_BODY + "</style></head><body>" + 
				"<div style=\"font-size:large;\">Student #&nbsp;&nbsp;" + studentCounter + "</div>" + 
				"<div style=\"background-color:#000000;color:#FFFFFF;\" align=\"center\">" + 
				rosterObject.getObjTest().getTestSessionName() + "</div>" + 
				"<div style=\"position: running(footer);\">" + 
				"<div id=\"page-footer\" style=\"font-size:xx-small;\">" + 
				"Copyright &#169; 2013 by CTB/McGraw-Hill LLC. All rights reserved. Only authorized customers may copy, download and/or print the document, located online at ctb.com. Any other use or reproduction of this document, in whole or in part, requires written permission of the publisher." + 
				"</div>" + 
				"</div>" + 
				"<div><br /></div><div><table cellspacing=\"0\" cellpadding=\"2\" width=\"100%\">";
		htmlString = htmlString + "<tr><td style=\"width:13%;background-color:#FF9933;border-bottom: thin #ccc solid;\">Student Name: </td><td style=\"width:87%;background-color:#FF9933;color:#000000;border-bottom: thin #ccc solid;\" align=\"left\">" + 
				rosterObject.getStudentName().trim() + "</td></tr>";
		htmlString = htmlString + "<tr><td style=\"width:13%;background-color:#FF9933;border-bottom: thin #ccc solid;\">Student ID: </td><td style=\"width:87%;background-color:#FF9933;color:#000000;border-bottom: thin #ccc solid;\" align=\"left\">" + 
				rosterObject.getStudentPinNumber() + "</td></tr>";
		htmlString = htmlString + "<tr><td style=\"width:13%;background-color:#FF9933;\">Student Grade: </td><td style=\"width:87%;background-color:#FF9933;color:#000000;\" align=\"left\">" + 
				rosterObject.getGrade().trim() + "</td></tr>";
		htmlString = htmlString + "<tr><td colspan=\"2\"><hr/></td></tr></table></div>";

		List<ItemObject> answerObj = rosterObject.getResponseItemObjectList();
		Map itemWithWidgetIdAnswerMap = new HashMap();
		for (ItemObject itemobj : answerObj) {
			itemAnsMap.put(itemobj.getItemId(), itemobj.getItemObject());
			itemWithWidgetIdAnswerMap.put(itemobj.getItemId(), itemobj.getItemResponseMapPerRoster());
		}
		//chages for ordering Subtest and Items
		if ((subtestOrderMap != null) && (subtestOrderMap.size() > 0)) {
			subtestList = rosterObject.getObjTest().getSubTestList();
			if ((subtestList != null) && (subtestList.size() > 0)) {
				itemObjectOrderMap = ((SubTestObject)subtestList.get(0)).getItemObjectOrderMap();
			}
			for (Map.Entry entry : itemObjectOrderMap.entrySet())
			{
				itemId = (String)entry.getValue();
				tempItem = getOrderedItemObject(itemQsnObj, itemId);
				currentSubTestName = getSubTestNameByItemId(subtestList, itemId);
				if (!previousSubTestName.equalsIgnoreCase(currentSubTestName))
					questionCounter = 1;
				previousSubTestName = currentSubTestName;
				superScriptcount = 1;
				if (tempItem != null) {
					String[] splitArr = (String[])null;
					String itemXML = tempItem.getItemObject().toString();

					tempString = getHTMLThroughXSLT(itemXML);

					org.jsoup.nodes.Document doc = Jsoup.parse(new String(tempString.getBytes(), "UTF-8"));
					Elements elements = doc.getAllElements();
					String tempHtmlString = null;
					org.jsoup.nodes.Element table = null;
					Iterator localIterator2 = elements.iterator(); if (localIterator2.hasNext()) { org.jsoup.nodes.Element tr = (org.jsoup.nodes.Element)localIterator2.next();
					table = tr.getElementById("QuestionPartTable");
					tempHtmlString = table.toString();
					}

					doc.select("#QuestionPartTable").remove();
					tempString = doc.body().toString();

					//update TEst SEssion NAMe and ITem number
					doc = Jsoup.parse(new String(tempString.getBytes(),"UTF-8"));
					elements = doc.select("td");					
					for (org.jsoup.nodes.Element tableData : elements) {
						org.jsoup.nodes.Attributes tableDataAttributes = tableData.attributes();
						if(tableDataAttributes.hasKey("id")){
							for (org.jsoup.nodes.Attribute attribute: tableDataAttributes){
								if(attribute.getKey().equals("id")){
									if(attribute.getValue().equalsIgnoreCase("SessionID")){
										tableData.appendText("<b> " + currentSubTestName + " - Question "+ questionCounter++ + "</b>");
									}									
								}
							}
						}
					}
					//remove all anchor tags
					tempString = doc.body().toString();
					Elements anchors = null;
					anchors = doc.select("a");
					String anchorText = "";
					String textInsideAnchorTag;
					for (org.jsoup.nodes.Element anchor : anchors)
					{
						org.jsoup.parser.Tag newtextTag = org.jsoup.parser.Tag.valueOf("text");
						org.jsoup.nodes.Element e = new org.jsoup.nodes.Element(newtextTag, "");
						String replaceWith = "<sup>" + superScriptcount++ + "</sup>";
						textInsideAnchorTag = "";
						if (anchor.html().startsWith("<b>")) {
							textInsideAnchorTag = anchor.html();
						}
						else if (anchor.html().charAt(anchor.html().length() - 1) == '.')
							textInsideAnchorTag = anchor.html().substring(0, anchor.html().length() - 1) + "<b>" + replaceWith + "</b>" + ".";
						else if (anchor.html().charAt(anchor.html().length() - 1) == ',') {
							textInsideAnchorTag = anchor.html().substring(0, anchor.html().length() - 1) + "<b>" + replaceWith + "</b>" + ",";
						}
						else
							textInsideAnchorTag = anchor.html().substring(0, anchor.html().length()) + "<b>" + replaceWith + "</b>";
						e.appendText(textInsideAnchorTag);
						anchor.replaceWith(e);
					}

					tempString = doc.body().toString();
					tempString = tempString.substring(6, tempString.lastIndexOf("</body>"));

					String[] tempArray = tempString.split("<td id=\"answer\">");

					doc = Jsoup.parse(new String(tempHtmlString.getBytes(), "UTF-8"));
					elements = doc.select("td");
					for (Object newtextTag = elements.iterator(); ((Iterator)newtextTag).hasNext(); ) { org.jsoup.nodes.Element tableData = (org.jsoup.nodes.Element)((Iterator)newtextTag).next();
					Attributes tableDataAttributes = tableData.attributes();
					if (tableDataAttributes.hasKey("id")) {
						for (org.jsoup.nodes.Attribute attribute : tableDataAttributes) {
							if (attribute.getKey().equals("id")) {
								String tableDataIdAttributeValue = attribute.getValue().split("\\$")[0];
								String answer = null;
								if (itemWithWidgetIdAnswerMap.get(itemId) != null) {
									answer = (String)((Map)itemWithWidgetIdAnswerMap.get(itemId)).get(tableDataIdAttributeValue);
								}
								if ((answer == null) || (answer.trim().length() == 0)) {
									answer = "<b>Answer:</b> This question was not attempted.";
								}
								else
									answer = "<b>Answer:</b> " + (answer.indexOf(":") > 0 ? 
											answer.substring(answer.indexOf(":") + 1, answer.length()) : answer);
								tableData.appendText(answer);
							}
						}
					}
					}
					tempString = tempArray[0] + "<td id=\"answer\" valign=\"top\">" + doc.toString() + tempArray[1];
					htmlString = htmlString + tempString;
				}

			}

		}

		htmlString = htmlString + "</body></html>";

		return (String)htmlString;
			}

	private ItemObject getOrderedItemObject(List<ItemObject> listItemObject, String itemId) {
		ItemObject ret = null;
		for (ItemObject obj : listItemObject) {
			if (obj.getItemId().equalsIgnoreCase(itemId)) {
				return obj;
			}
		}

		return ret;
	}

	private String escapeSpecialCharacters(String chars) {
		StringBuilder builder = new StringBuilder(chars.length());
		for (int i = 0; i < chars.length(); i++)
		{
			char c = chars.charAt(i);

			if ((c < ' ') || (c > '~'))
			{
				int charAsInt = c;
				builder.append("&#").append(charAsInt).append(";");
			}
			else
			{
				switch (c)
				{
				case '"':
					builder.append("&quot;");
					break;
				case '&':
					builder.append("&amp;");
					break;
				case '<':
					builder.append("&lt;");
					break;
				case '>':
					builder.append("&gt;");
					break;
				default:
					builder.append(String.valueOf(c));
				}
			}
		}
		return builder.toString();
	}

	private String getHTMLThroughXSLT(String itemXML) throws Exception
	{
		File stylesheet = new File("StaticData"+System.getProperty("file.separator")+"xslt"+System.getProperty("file.separator")+"Conversion.xsl");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(itemXML));
		is.setEncoding("ISO-8859-1");
		org.w3c.dom.Document document = builder.parse(is);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		StreamSource stylesource = new StreamSource(stylesheet);
		Transformer transformer = tFactory.newTransformer(stylesource);
		DOMSource source = new DOMSource(document);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		OutputStream output = new OutputStream()
		{
			private StringBuilder string = new StringBuilder();

			public void write(int b) throws IOException {
				this.string.append((char)b);
			}

			public String toString() {
				return this.string.toString();
			}
		};
		StreamResult result = new StreamResult(stream);
		transformer.transform(source, result);
		String finalHtml = result.getOutputStream().toString();

		return finalHtml;
	}
	private String extractAnswer(String responseXML, ItemObject responseObject) throws Exception {
		List<Element> list = null;
		String itemResponse ="";
		String idAttributeValue = null;
		Map<String,String> answerIdValueMap = new HashMap<String,String>();
		String answerIdValue = null;
		List<String> itemResponseData = new ArrayList<String>();
		String processedResponseXML = XMLUtils.processHexString(responseXML);
		Document parsedDoc = XMLUtils.parse(processedResponseXML,"UTF-8");
		list = XMLUtils.extractAllElement("answer", parsedDoc.getRootElement());
		for (Element el : list) {
			boolean isAnswered = false;
			idAttributeValue = el.getAttribute("id").getValue();
			answerIdValue = idAttributeValue.split("\\$")[0];
			List<Object> listCh = el.getContent();
			for (Object e2 : listCh) {
				if (e2 instanceof CDATA) {
					isAnswered = true;					
					answerIdValueMap.put(answerIdValue, ((CDATA) e2).getText());
					itemResponse += ((CDATA) e2).getText();
				}
			}
			if (!isAnswered) {
				//				itemResponseData.add("");
				itemResponse += "";
				answerIdValueMap.put(answerIdValue, "This question was not attempted");
			}
		}
		responseObject.setItemResponseMapPerRoster(answerIdValueMap);
		return itemResponse;
	}
	private String getKeyFromValue(Map<String, String> map, String value) {
		for (String key : map.keySet()) {
			if (((String)map.get(key)).equalsIgnoreCase(value))
				return key;
		}
		return null;
	}

	private String getSubTestNameByItemId(List<SubTestObject> subTestList, String itemID) {
		String subTestName = "";
		List<ItemObject> adsItemObjectList = null;
		if(subTestList != null && subTestList.size() > 0){
			for(SubTestObject subTest : subTestList){
				adsItemObjectList = subTest.getAdsItemObjectList();
				if(adsItemObjectList!= null && adsItemObjectList.size() > 0){
					for(ItemObject item : adsItemObjectList){
						if(item.getItemId().equalsIgnoreCase(itemID)){
							subTestName = subTest.getSubTestName();
							break;
						}
					}
				}
			}
		}

		return subTestName;
	}
}