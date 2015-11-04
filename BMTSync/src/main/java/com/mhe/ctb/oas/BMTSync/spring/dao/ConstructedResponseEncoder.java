package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A class to provide a static method for encoding constructed response answers in a way that OAS can understand.
 * @author kristy_tracer
 *
 */ 
public class ConstructedResponseEncoder {
	
	private static final String XML_ANSWERS_END = "</answers>";

	private static final String XML_ANSWERS_START = "<answers>";

	private static final String XML_END = "</answer>";

	private static final String XML_MIDDLE = "\">";
	
	private static final String XML_CDATA_START = "<![CDATA[";
	
	private static final String XML_CDATA_END = "]]>";

	private static final String XML_START = "<answer id=\"widget";
	
	private static final String MCR_ORDER = "crOrder";
	
	private static final String MCR_RESPONSE = "response";
	
	private final Random rng;
	
	private static final Logger LOGGER = Logger.getLogger(ConstructedResponseEncoder.class);
	
	public ConstructedResponseEncoder() {
		this(null);
	}

	public ConstructedResponseEncoder(final Random random) {
		if (random == null) {
			rng = new Random();
		} else {
			this.rng = random;
		}
	}
	

	/**
	 * Per Sumit Sardar, OAS requires constructed responses to be in the following format:
	 * <answers><answer id="widget6486081002"><![CDATA[dummy response for single line CR.]]></answer></answers>
	 * 
	 * The whole thing must then be URL-Encoded.
	 * 
	 * This is the function that does that formatting.
	 * @param response
	 * @return
	 */
	public String formatConstructedResponse(final String response) throws UnsupportedEncodingException {
		final StringBuilder builder = new StringBuilder();
		
		ArrayList<String> responses = processResponse(response);
		
		builder.append(XML_ANSWERS_START);
		for (int i=0; responses!=null && i<responses.size(); i++) {
			builder.append(XML_START);
			builder.append(String.format("%05d%05d", rng.nextInt(99999), rng.nextInt(99999)));
			builder.append(XML_MIDDLE);
			if (!StringUtils.isBlank(responses.get(i))) {
				builder.append(XML_CDATA_START+responses.get(i)+XML_CDATA_END);
			}
			builder.append(XML_END);
		}
		
		builder.append(XML_ANSWERS_END);
		LOGGER.debug("[ConstructedResponseEncoder] Encoding string for storage: " + builder.toString());
		final String builtResponse = URLEncoder.encode(builder.toString(), "UTF-8");
		final String encodedResponse = builtResponse.replaceAll("\\+", "%20");
		
		return encodedResponse.toString();
	}
	
	/**
	 * Per Prem Kalani, the itemResponse property will contain a String value either with a single response:
	 * 
	 * 		itemResponse: "D"
	 * 
	 * OR with a JSON array with multiple response objects as the following: 
	 * 
	 * 		itemResponse: "[{
	 * 			crOrder: 1,
	 * 			response: "this is essay"
	 * 		},{
	 * 			crOrder: 2,
	 * 			response: ""
	 * 		}]"
	 * 
	 *  
	 * This method processes a response string:
	 * 
	 * - if it contains a JSON array, then return a list of multiple String responses sorted by "crOrder" key
	 * - otherwise, return an array with single response string
	 * 
	 * @param response
	 * @return 
	 */
	private ArrayList<String> processResponse(String response) {
		ArrayList<String> responses = new ArrayList<String>();
		
		//convert response into JSON object in order to check if an array of multiple responses is passed
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		try {
			JsonNode root = mapper.readTree(response);
			if (root!=null && root.isArray()) {
				//process array
				List<Map<String, Object>> jsonResponses = mapper.readValue(response, new TypeReference<List<Map<String,Object>>>() {}); 
				//add sorting by crOrder
				Collections.sort(jsonResponses, new Comparator<Map<String, Object>>() {
					public int compare(Map<String, Object> a, Map<String, Object> b) {
						return ((Integer)a.get(MCR_ORDER)).intValue() - (((Integer)b.get(MCR_ORDER)).intValue());
					}
				});					
				for (int i=0; jsonResponses!=null && i<jsonResponses.size(); i++) {						
					responses.add((String)jsonResponses.get(i).get(MCR_RESPONSE)); 
				}
			}
		} catch (IOException e) {
			LOGGER.warn("Could not deserialize JSON content as a tree! Response is not a JSON: [response=" + response + "]");			
		}

		//if responses array is empty, then there was no JSON response, then it is a single string value response
		if (responses.isEmpty()) {
			//process single string value response
			//response was not a JSON array, then just add it to the responses array
			responses.add(response);
		}
		
		return responses;
	}

}
