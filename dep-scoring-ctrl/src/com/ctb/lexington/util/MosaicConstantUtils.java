package com.ctb.lexington.util;

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
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ctb.lexington.util.jsonobject.Answer;
import com.ctb.lexington.util.jsonobject.DragArea;
import com.ctb.lexington.util.mosaicobject.CandidateItemResponse;

public class MosaicConstantUtils {
	
	/*
	 * Comment Out TASC FT TE Items Scoring for Story OAS-3944
	 */
	/*private static final ResourceBundle rb = ResourceBundle.getBundle("config");
	
	final public static String HTTP_REQUEST_TYPE = rb.getString("http.request.type");
	final public static String CONTENT_TYPE = rb.getString("content.type");
	final public static String MOSAIC_ENCRYPTION_METHOD_TITLE = rb.getString("mosaic.encryption.method.title");
	final public static String MOSAIC_TIMESTAMP_TITLE = rb.getString("mosaic.timestamp.title");
	final public static String MOSAIC_VERSION_TITLE = rb.getString("mosaic.version.title");
	final public static String MOSAIC_AUTHORIZATION_TITLE = rb.getString("mosaic.authorization.title");
	final public static String MOSAIC_VERSION_VALUE = rb.getString("mosaic.version.value");
	final public static String HEADER_KEY_SEPARATION = rb.getString("header.key.separation");
	final public static String HEADER_SEPARATION = rb.getString("header.separation");
	final public static String API_ENDPOINT = rb.getString("api.endpoint");
	final public static String MOSAIC_ACCESS_KEY = rb.getString("mosaic.access.key");
	final public static String MOSAIC_API_TITLE = rb.getString("mosaic.api.title");
	final public static String CHARACTER_ENCODE = rb.getString("encode.charset");
	final public static String ZERO_PADD = rb.getString("zero.padd");
	final public static String MOSAIC_URL = rb.getString("mosaic.url");

	// : Constant value of MSS Request JSON parameter
	final public static String ITEM_RESPONSE_SOURCE = rb.getString("mosaic.item.response.source");
	final public static String ITEM_SOURCE = rb.getString("mosaic.item.source");
	final public static String ITEM_BANK_IDS = rb.getString("mosaic.item.bank.id");*/
	
	final public static String HTTP_REQUEST_TYPE = "POST";
	final public static String CONTENT_TYPE = "application/json";
	final public static String MOSAIC_ENCRYPTION_METHOD_TITLE = "x-mosaic-content-md5";
	final public static String MOSAIC_TIMESTAMP_TITLE = "x-mosaic-timestamp";
	final public static String MOSAIC_VERSION_TITLE = "x-mosaic-version";
	final public static String MOSAIC_AUTHORIZATION_TITLE = "x-mosaic-version";
	final public static String MOSAIC_VERSION_VALUE = "1.0";
	final public static String HEADER_KEY_SEPARATION = ":";
	final public static String HEADER_SEPARATION = "\n";
	final public static String API_ENDPOINT = "restapi/scoreitem";
	final public static String MOSAIC_ACCESS_KEY = "f446b68b20e459881a0ddddb15fbdb43";
	final public static String MOSAIC_API_TITLE = "Mosaic";
	final public static String CHARACTER_ENCODE = "UTF-8";
	final public static String ZERO_PADD = "00000000000000000000000000000000";
	final public static String MOSAIC_URL = "https://mosaic-engine-int.ec2-ctb.com/mosaic-engine/restapi/scoreitem";

	// : Constant value of MSS Request JSON parameter
	final public static String ITEM_RESPONSE_SOURCE = "OAS";
	final public static String ITEM_SOURCE = "DAS";
	final public static String ITEM_BANK_IDS = "TS~TASC|TR~TASC";
	
	final public static String ANSWER_TAG = "answer";

	// : MSS Request JSON object final property id
	final public static String MSS_REQUEST_PARAM = "MosaicScoringRequest";

	// : Various interaction types of TE items
	final public static String INTERACTION_TYPE_TEXT_REST = "Text Restricted";
	final public static String INTERACTION_TYPE_MCQ = "MCQ";

	// : Variables are used in customized exclusion strategy policy of MSS JSON object creation
	final public static String EXCLUTION_ATTR_1 = "id";
	final public static String EXCLUTION_ATTR_2 = "mathml_value";
	final public static String EXCLUTION_ATTR_3 = "target";
	final public static String EXCLUTION_ATTR_4 = "html";
	final public static String EXCLUTION_ATTR_5 = "CandidateItemResponseObj";
	final public static String EXCLUTION_ATTR_6 = "order";
	final public static String EXCLUTION_ATTR_7 = "value";
	final public static String EXCLUTION_ATTR_8 = "ItemOrder";
	final public static String EXCLUTION_ATTR_9 = "ItemScore";

	final public static String INVOKE_STATUS_IP = "Progress";
	final public static String INVOKE_STATUS_SUCCESS = "Success";
	
	final public static String PARENT_DAS_INTERACTION = "Parent";
	
	final public static Map<String, String> ITEM_BANK_COLLECTION = new HashMap<String, String>();
	
	/*
	 * Comment Out TASC FT TE Items Scoring for Story OAS-3944
	 */
	/*static{
		String[] bankDetails = ITEM_BANK_IDS.split("\\|");
		for(int indx=0; indx < bankDetails.length; indx++){
			String[] mapper = bankDetails[indx].split("~");
			ITEM_BANK_COLLECTION.put(mapper[0], mapper[1]);
		}
	}*/
	
	/**
	 * Convert string object to Base64 String
	 * 
	 * @param value
	 * @return String object
	 */
	public static String getBase64StringFromString(String value) {

		try {
			byte[] byteArray = null;

			if (value != null) {
				byteArray = Base64.encodeBase64(value.getBytes());
				return new String(byteArray);
			} else
				return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Convert string object to Base64 byte[]
	 * 
	 * @param value
	 * @return byte[]
	 */
	public static byte[] getBase64FromString(String value) {

		try {
			if (value != null)
				return Base64.encodeBase64(value.getBytes());
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

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
			candidateResponse.setValue(getElementValue(answer.getId()));
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
				candidateResponse.setTarget(getElementValue(answer
						.getDroparea().getId()));

				// : Check for DNDPR type item response - START!
				candidateResponse.setValue((INTERACTION_TYPE_TEXT_REST
						.equalsIgnoreCase(interactionType)) ? dragarea
						.getResponse() : getElementValue(dragarea.getId()));

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
			return getCharacterDataFromElement(element);
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
}
