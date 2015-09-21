package com.ctb.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ctb.utils.datamodel.Answer;
import com.ctb.utils.datamodel.DragArea;
import com.ctb.utils.mssmodel.CandidateItemResponse;

public class MSSConstantUtils {

	final public static String ANSWER_TAG = "answer";
	final public static String CHARACTER_ENCODE = "UTF-8";

	// : Constant value of MSS Request JSON parameter
	final public static String ITEM_RESPONSE_SOURCE = "OAS";
	final public static String ITEM_SOURCE = "DAS";
	final public static String ITEM_BANK_ID = "TASC";

	// : MSS Request JSON object final property id
	final public static String MSS_REQUEST_PARAM = "MosaicScoringRequest";
	final public static String CANDIDATEITEMRESPONSE = "CandidateItemResponse";

	// : Various interaction types of TE items
	final public static String INTERACTION_TYPE_TEXT_REST = "Text Restricted";
	final public static String INTERACTION_TYPE_MCQ = "MCQ";
	final public static String INTERACTION_MCQ_PREFIX = ExtractUtils.get("oas.das.mcq.prefix");
	final public static String INTERACTION_MSR_PREFIX = ExtractUtils.get("oas.das.msr.prefix");

	// : Variables are used in customized exclusion strategy policy of MSS JSON
	// object creation
	final public static String EXCLUTION_ATTR_1 = "id";
	final public static String EXCLUTION_ATTR_2 = "mathml_value";
	final public static String EXCLUTION_ATTR_3 = "target";
	final public static String EXCLUTION_ATTR_4 = "html";
	final public static String EXCLUTION_ATTR_5 = "CandidateItemResponseObj";
	final public static String EXCLUTION_ATTR_6 = "order";
	final public static String EXCLUTION_ATTR_7 = "value";
	final public static String EXCLUTION_ATTR_8 = "val";
	final public static String EXCLUTION_ATTR_9 = "ItemOrder";
	final public static String EXCLUTION_ATTR_10 = "ItemScore";

	final public static String PARENT_DAS_INTERACTION = "Parent";

	final public static String COL1_DAS_ITEM_ID = "DAS Item ID";
	final public static String COL2_ITEM_SOURCE = "Item Source";
	final public static String COL3_ITEM_BANK_ID = "Item Bank ID";
	final public static String COL4_S_RESPONSE = "Mosaic Accepted Student Response";
	final public static String COL5_STUDENT_ROSTER_ID = "Student Roster ID";
	final public static String COL6_OAS_ITEM_ID = "OAS Item ID";
	final public static String COL7_PEID = "PEID Item ID";
	final public static String COL8_OAS_JSON = "OAS Captured Student Response";

	final public static String excelSheetName = "Student_Data";

	final public static Map<String, String> textRestrictedMap = new HashMap<String, String>();
	final public static Map<String, String> dndButCompoundItemMap = new HashMap<String, String>();
	final public static Map<String, String> msrDasItemIdsMap = new HashMap<String, String>();
	

	static {
		BufferedReader br = null;
		try {
		    String line = null;
		    br = new BufferedReader(new FileReader(ExtractUtils.get("oas.text.mapping.file.path")));
		    while ((line = br.readLine()) != null) {
		    	String key = line.substring(0, line.lastIndexOf("="));
		    	String value = line.substring(line.lastIndexOf("=")+1);
		    	textRestrictedMap.put(key, value);
		    }
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
		}
		
		try {
			String compoundItems = ExtractUtils.get("oas.dnd.compound.items");
			String msrDasItems = ExtractUtils.get("oas.msr.das.items");
			String splitChar = ExtractUtils.get("oas.split.character");
			
			if("".equals(splitChar)){
				throw new Exception("Split character is not provided...");
			}
			if(!"".equals(compoundItems)){
				String[] strarr = compoundItems.split(splitChar);
				for(String val : strarr){
					dndButCompoundItemMap.put(val, val);
				}
			}else{
				throw new Exception("Special DND compund items are not provided...");
			}
			if(!"".equals(msrDasItems)){
				String[] strarr = msrDasItems.split(splitChar);
				for(String val : strarr){
					msrDasItemIdsMap.put(val, val);
				}
			}else{
				throw new Exception("MSR type DAS items are not provided...");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Generate MSR, MCQ, MCQ-MSR type item response structure
	 * 
	 * @param answer
	 * @param candidateResponseList
	 * @param interactionType
	 * @param order
	 */
	public static int processMSRTypeResponse(Answer answer,
			List<CandidateItemResponse> candidateResponseList,
			String interactionType, int order, String dasItemId) {

		CandidateItemResponse candidateResponse = new CandidateItemResponse();
		if (answer.getId() != null && !answer.getId().isEmpty()) {
			candidateResponse.setOrder(String.valueOf((order++)));

			//: add mentioned DAS MSR items & other MCQ item prefix
			candidateResponse
					.setValue((dasItemId != null && MSSConstantUtils.msrDasItemIdsMap
							.containsKey(dasItemId)) ? MSSConstantUtils.INTERACTION_MSR_PREFIX
							.concat(getElementValue(answer.getId()))
							: ((MSSConstantUtils.INTERACTION_TYPE_MCQ
									.equals(interactionType)) ? MSSConstantUtils.INTERACTION_MCQ_PREFIX
									.concat(getElementValue(answer.getId()))
									: getElementValue(answer.getId())));
			candidateResponseList.add(candidateResponse);
		}
		return order;
	}

	/**
	 * Generate DND, DND-DND, DNDPR type item response structure
	 * 
	 * @param answer
	 * @param candidateResponseList
	 * @param interactionType
	 * @param order
	 */
	public static int processDNDTypeResponse(Answer answer,
			List<CandidateItemResponse> candidateResponseList,
			String interactionType, int order) {

		List<DragArea> dragedList = answer.getDragarea();
		for (DragArea dragarea : dragedList) {
			if (dragarea != null) {
				CandidateItemResponse candidateResponse = new CandidateItemResponse();
				candidateResponse.setOrder(String.valueOf((order++)));
				candidateResponse.setTarget(MSSConstantUtils
						.getElementValue(answer.getDroparea().getId()));

				// : Check for DNDPR type item response - START!
				candidateResponse.setVal((INTERACTION_TYPE_TEXT_REST
						.equalsIgnoreCase(interactionType)) ? MSSConstantUtils
						.getTextRespVal(dragarea.getResponse())
						: MSSConstantUtils.getElementValue(dragarea.getId()));

				candidateResponse.setHtml((INTERACTION_TYPE_TEXT_REST
						.equalsIgnoreCase(interactionType)) ? dragarea
						.getHtmlresponse() : dragarea.getResponse());
				// : Check for DNDPR type item response - END!

				candidateResponseList.add(candidateResponse);
			}
		}
		return order;
	}

	/**
	 * Get Text Response value
	 * 
	 * @param response
	 * @return
	 */
	private static String getTextRespVal(String response) {

		if (response != null) {
			String key = response.trim();
			return textRestrictedMap.get(key);
		}
		return null;
	}

	/**
	 * Convert Clob object to String object
	 * 
	 * @param clobObj
	 * @return String
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 */
	public static String clobToString(Clob clobObj) throws SQLException,
			UnsupportedEncodingException {

		if (clobObj != null) {
			String clob = clobObj.getSubString(1, (int) clobObj.length());
			return URLDecoder.decode(clob, CHARACTER_ENCODE);
		}
		return null;
	}

	/**
	 * Parse student response from the given xmlResponse
	 * 
	 * @param xmlString
	 * @return student response as String object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static String parseResponse(String xmlString)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xmlString
				.getBytes()));
		NodeList nodes = doc.getElementsByTagName(ANSWER_TAG);
		if (nodes.getLength() > 0) {
			Element element = (Element) nodes.item(0);
			return MSSConstantUtils.getCharacterDataFromElement(element);
		}
		return null;
	}

	/**
	 * Get response from the given element
	 * 
	 * @param e
	 * @return response in String object
	 */
	public static String getCharacterDataFromElement(org.w3c.dom.Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return null;
	}

	/**
	 * Generates the current time in UTC time zone.
	 * 
	 * @return String
	 */
	public static String getUTCTimezone() {
		SimpleDateFormat dateFormatUtc = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		String parsedDate = dateFormatUtc.format(new Date());
		return parsedDate;
	}

	/**
	 * Returns element number from element id.
	 * 
	 * @param element
	 * @return
	 */
	private static String getElementValue(String element) {

		if (element != null && !element.isEmpty()) {
			int beginIndex = element.lastIndexOf("_") + 1;
			return element.substring(beginIndex);
		}
		return null;
	}

	/**
	 * Wrap CSV text with " character
	 * 
	 * @param s
	 * @return
	 */
	public static String wrap(String s) {
		StringBuffer sb = new StringBuffer();
		if (s != null) {
			sb.append(s);
		}
		sb.insert(0, "\"").append("\"");
		return sb.toString();
	}
	
	/**
	 * Time unit formatter
	 * 
	 * @param millis
	 * @return
	 */
	public static String timeTaken(long millis) {
		long p = millis % 1000;
		long s = (millis / 1000) % 60;
		long m = ((millis / 1000) / 60) % 60;
		long h = ((millis / 1000) / (60 * 60)) % 24;
		return (h == 0) ? ((m == 0) ? String.format("%02d.%03d Sec", s, p)
				: String.format("%02d Minutes %02d.%03d Sec", m, s, p))
				: String.format("%d Hours %02d Minutes %02d.%03d Sec", h, m, s,
						p);
	}
}
