package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * A class to provide a static method for encoding constructed response answers in a way that OAS can understand.
 * @author kristy_tracer
 *
 */ 
public class ConstructedResponseEncoder {
	
	private static final String XML_END = "]]></answer></answers>";

	private static final String XML_MIDDLE = "\"><![CDATA[";

	private static final String XML_START = "<answers><answer id=\"widget";
	
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

		builder.append(XML_START);
		builder.append(String.format("%05d%05d", rng.nextInt(99999), rng.nextInt(99999)));
		builder.append(XML_MIDDLE);
		builder.append(response);
		builder.append(XML_END);
		LOGGER.debug("[ConstructedResponseEncoder] Encoding string for storage: " + builder.toString());
		final String builtResponse = URLEncoder.encode(builder.toString(), "UTF-8");
		final String encodedResponse = builtResponse.replaceAll("\\+", "%20");
		
		return encodedResponse.toString();
	}
}
