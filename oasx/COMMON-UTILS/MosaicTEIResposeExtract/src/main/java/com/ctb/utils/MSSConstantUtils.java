package com.ctb.utils;

import java.io.ByteArrayInputStream;
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
	final public static String INTERACTION_MCQ_PREFIX = "mc";

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

	static {

		textRestrictedMap
				.put("If she read that the heroine of the novel was nursing a sick man, she longed to move with noiseless steps about the room of a sick man;",
						"1");
		textRestrictedMap
				.put("Anna read and understood, but it was distasteful to her to read, that is, to follow the reflection of other people /u2019 s lives.",
						"2");
		textRestrictedMap
				.put("She remembered the ball, remembered Vronsky and his face of slavish adoration, remembered all her conduct with him:",
						"3");
		textRestrictedMap.put("For a moment she regained her self-possession,",
				"4");
		textRestrictedMap
				.put("The driving snow and the wind rushed to meet her and struggled with her over the door. But she enjoyed the struggle.",
						"5");
		textRestrictedMap
				.put("I have come after them and made repair Where they have left not one stone on a stone,",
						"1");
		textRestrictedMap
				.put("We have to use a spell to make them balance: \"Stay where you are until our backs are turned!\"",
						"2");
		textRestrictedMap
				.put("My apple trees will never get across And eat the cones under his pines, I tell him.",
						"3");
		textRestrictedMap
				.put("And on a day we meet to walk the line And set the wall between us once again.",
						"4");
		textRestrictedMap
				.put("Not long after, a concerned friend told her, \"You need a new dog.\"",
						"1");
		textRestrictedMap
				.put("Jean \"was having some difficulty getting around and ... was having trouble getting the dog enough exercise,\" explained Illmensee, 66, a retired teacher from Huntington, N.Y.",
						"2");
		textRestrictedMap.put(
				"\"They don't get the importance of seniors having animals.",
				"3");
		textRestrictedMap
				.put("\"If I stop working, I couldn't afford to have him,\" Rogers explained.",
						"4");
		textRestrictedMap
				.put("Jennifer Devine, SWAP chariwoman and a geriatric social worker, said many seniors find that having a pet is soothing and reassuring, even with the extra work.",
						"5");
		textRestrictedMap
				.put("At Little Shelter in Huntington, there is a food pantry for those who can no longer afford pet food.",
						"6");
		textRestrictedMap
				.put("One study found that employees—both mentors and mentees—who participated in a mentoring program were 20% more likely to get a raise than those who were not in such a program.",
						"1");
		textRestrictedMap
				.put("Mentors also gain the satisfaction of knowing that they are helping to change lives for the better.",
						"2");
		textRestrictedMap
				.put("Mentors give kids a safe place to spend their free time, developing valuable social and communication skills in the process.",
						"3");
		textRestrictedMap
				.put("By being positive role models to young people, they are not only helping them to develop but giving back to their communities as well.",
						"4");
		textRestrictedMap
				.put("Remember that although mentors are not parents, they can have a great influence on a young person's life.",
						"5");
		textRestrictedMap
				.put("Many mentors say that they gain as much from mentoring as do their mentees.",
						"6");
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
			String interactionType, int order) {

		CandidateItemResponse candidateResponse = new CandidateItemResponse();
		if (answer.getId() != null && !answer.getId().isEmpty()) {
			candidateResponse.setOrder(String.valueOf((order++)));
			candidateResponse
					.setValue((MSSConstantUtils.INTERACTION_TYPE_MCQ
							.equals(interactionType)) ? MSSConstantUtils.INTERACTION_MCQ_PREFIX
							.concat(getElementValue(answer.getId()))
							: getElementValue(answer.getId()));
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
}
