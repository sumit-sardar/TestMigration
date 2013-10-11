package com.ctb.util;

//import groovy.util.logging.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

//import org.safehaus.uuid.Logger;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

//@Slf4j
public class HMACQueryStringBuilder {
	private String URL_ENCODING;//UTF-8
	private String ENCODING_ALGORITHM;//HmacSHA1

	private SecretKey secretKey;
	
	private String encryptionKey;//WPZguVF49hXaRuZfe9L29ItsC2I
	private int signatureValiditySeconds;//60
	private String timeZone;//GMT
	
	private static final String API_KEY_PARAMETER_NAME = "apikey=";
    private static final String EXPIRY_DATE_PARAMETER_NAME = "&time_stamp=";//"&validUntil=";
    private static final String IP_ADDRESS_PARAMETER_NAME = "&ip_address=";//"&ipAddress=";
    private static final String SIGNATURE_PARAMETER_NAME = "&signature=";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
    private static final String CUSTOMERID_PARAMETER_NAME = "customer_id=";
    private static final String ORGNODECODE_PARAMETER_NAME = "&org_node_code=";
    private static final String HIERARCHYLEVEL_PARAMETER_NAME = "&hierarchy_level=";
    private static final String APPNAME_PARAMETER_NAME = "&application_name=";
    private static final String SHAREDKEY_PARAMETER_NAME = "&shared_key=";
    private static final String USERROLE_PARAMETER_NAME = "&user_role=";
    

	public HMACQueryStringBuilder() {
	}

	/**
	 * @param encryptionKey	encryption key supplied to you by Digital Measures
	 * @param signatureValiditySeconds	number of seconds that generated signature is valid for
	 *	10 seconds is recommended, unless your server's clock
	 *	tends to drift significantly. Consider using NTP before
	 *	adjusting this too far.
	 */
	public HMACQueryStringBuilder(String encryptionKey, int signatureValiditySeconds, String encodingAlgo)
	{
		this(encryptionKey.getBytes(), signatureValiditySeconds, encodingAlgo);
	}

	/**
	 * @param encryptionKey	encryption key supplied to you by Digital Measures
	 * @param signatureValiditySeconds	number of seconds that generated signature is valid for
	 *	10 seconds is recommended, unless your server's clock
	 *	tends to drift significantly. Consider using NTP before
	 *	adjusting this too far.
	 */
	public HMACQueryStringBuilder(byte[] encryptionKey, int signatureValiditySeconds, String encodingAlgo)
	{
		this.ENCODING_ALGORITHM = encodingAlgo;
		this.secretKey = new SecretKeySpec(encryptionKey, ENCODING_ALGORITHM);
		this.signatureValiditySeconds = signatureValiditySeconds;
	}

	/**
	 * Construct a query string including HMAC signature
	 * @param username	username string that has been authenticated by your systems
	 * @return the authenticated query string
	 */
	public String buildAuthenticatedQueryString(String username) throws Exception
	{
		return buildAuthenticatedQueryString(username, null);
	}

	/**
	 * Construct a query string including HMAC signature
	 * @param username	username string that has been authenticated by your systems
	 * @param ipAddress	user's publicly accessible IP address, or Null. Reasons to omit:
	 *	IP addresses are not available due to network or server configuration
	 *	Users access the public internet through NAT, but your server
	 *	is inside the NAT, and can see internal network IP addresses.
	 * @return the authenticated query string
	 */
	public String buildAuthenticatedQueryString(String username, String ipAddress) throws Exception
	{
		String validUntilDate = getISO8601UTCDate();
		Appendable queryString = buildUnauthenticatedQueryString(username, ipAddress, validUntilDate);
		String signature = getAuthenticationCode(queryString.toString());

		appendAuthenticationCode(queryString, signature);

		return queryString.toString();
	}

	/**
	 * Construct a query string including HMAC signature
	 * @param customerId	TASC customerId created in OAS systems
	 * @param orgNodeCode	for all user org levels created in OAS systems
	 * @param hierarchyLevel	user level identifier (1=state) created in OAS systems
	 * @param applicationName	OAS
	 * @param sharedKey	agreed upon key in prism and OAS
	 * @param userRole	admin or user
	 * @param ipAddress	user's publicly accessible IP address, or Null. Reasons to omit:
	 *	IP addresses are not available due to network or server configuration
	 *	Users access the public internet through NAT, but your server
	 *	is inside the NAT, and can see internal network IP addresses.
	 * @param validUntilDate	iso date in the format yyyy-MM-ddThh:mm:ssZ
	 * @return the authenticated query string
	 */
	public String buildAuthenticatedQueryString(int customerId, String orgNodeCode, int hierarchyLevel, String applicationName, String sharedKey, String userRole, String ipAddress, String validUntilDate) throws Exception
	{
		if (validUntilDate == null || validUntilDate.trim().length() == 0)
		{
			validUntilDate = getISO8601UTCDate();
		}
		Appendable queryString = buildUnauthenticatedQueryString(customerId, orgNodeCode, hierarchyLevel, applicationName, sharedKey, userRole, ipAddress, validUntilDate);
		String signature = getAuthenticationCode(queryString.toString());

		appendAuthenticationCode(queryString, signature);

		return queryString.toString();
	}	
	/**
	 * Check if the client provided parameters is valid
	 * @param inputParam
	 * @param ipAddress
	 * @param validUntilDate
	 * @param secretValue
	 * @return
	 * @throws Exception
	 */
	public boolean isValidRequest(String inputParam, String ipAddress, String validUntilDate, String secretValue) throws Exception
	{
		
		TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setTimeZone(timeZone);
		format.setLenient(false);
		Calendar calendar = Calendar.getInstance(timeZone);
		format.format(calendar.getTime());
		
		Date date = format.parse(URLDecoder.decode(validUntilDate, URL_ENCODING));
		Calendar expiryTime = Calendar.getInstance(timeZone);
		expiryTime.setTime(date);
		
		if(expiryTime.compareTo(calendar) > 0) {
			Appendable queryString = buildUnauthenticatedQueryString(
					URLDecoder.decode(inputParam, URL_ENCODING), 
					URLDecoder.decode(ipAddress, URL_ENCODING), 
					URLDecoder.decode(validUntilDate, URL_ENCODING));
			String signature = urlEncode(getAuthenticationCode(queryString.toString()));
			
			if(secretValue != null && secretValue.equals(URLDecoder.decode(signature, URL_ENCODING))) {
				// encoding needed before comparing
				return equals(urlEncode(secretValue), signature);
			}
	
			return equals(secretValue, signature);
		} else {
			//Logger.logInfo("Request key is expired .... ");
			System.out.println("Request key is expired .... ");
			return false;
		}

	}
	
	private boolean equals(String expected, String actual) {
        byte[] expectedBytes = null;
        byte[] actualBytes = null;
        try {
            expectedBytes = expected.getBytes(URL_ENCODING);
            actualBytes = actual.getBytes(URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding while encrypting.",e);
        }

        int expectedLength = expectedBytes == null ? -1 : expectedBytes.length;
        int actualLength = actualBytes == null ? -1 : actualBytes.length;
        if (expectedLength != actualLength) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < expectedLength; i++) {
            result |= expectedBytes[i] ^ actualBytes[i];
        }
        return result == 0;
    }

	/**
	 * Build a timestamp in the UTC time zone, format date according to
	 * ISO8601: yyyy-MM-ddThh:mm:ssZ, with the specified number of seconds added
	 * @return timestamp
	 */
	private String getISO8601UTCDate()
	{
		TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setTimeZone(timeZone);
		format.setLenient(false);

		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.add(Calendar.SECOND, signatureValiditySeconds);

		return format.format(calendar.getTime());
	}

	/**
	 * Build a timestamp in the set time zone, format date according to
	 * ISO8601: yyyy-MM-ddThh:mm:ssZ, with the specified number of seconds added
	 * @return timestamp
	 */
	private String getISO8601DateTime()
	{
		TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setTimeZone(timeZone);
		format.setLenient(false);

		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.add(Calendar.SECOND, signatureValiditySeconds);

		return format.format(calendar.getTime());
	}
	
	/**
	 * Construct query string without HMAC signature
	 * @param username	username string that has been authenticated by your systems
	 * @param ipAddress	user's publicly accessible IP address, or Null
	 * @param validUntilDate	iso date in the format yyyy-MM-ddThh:mm:ssZ
	 * @return query string
	 */
	private Appendable buildUnauthenticatedQueryString(String username, String ipAddress, String validUntilDate) throws Exception
	{
		StringBuilder builder = new StringBuilder();

		builder.append(API_KEY_PARAMETER_NAME).append(urlEncode(username));
		builder.append(EXPIRY_DATE_PARAMETER_NAME).append(urlEncode(validUntilDate));

		if(null != ipAddress && ipAddress.trim().length() > 0)
			builder.append(IP_ADDRESS_PARAMETER_NAME).append(urlEncode(ipAddress));

		return builder;
	}

	/**
	 * Construct query string without HMAC signature
	 * @param customerId	TASC customerId created in OAS systems
	 * @param orgNodeCode	for all user org levels created in OAS systems
	 * @param hierarchyLevel	user level identifier (1=state) created in OAS systems
	 * @param applicationName	OAS
	 * @param sharedKey	agreed upon key in prism and OAS
	 * @param userRole	admin or user
	 * @param ipAddress	user's publicly accessible IP address, or Null
	 * @param validUntilDate	iso date in the format yyyy-MM-ddThh:mm:ssZ
	 * @return query string
	 */
	private Appendable buildUnauthenticatedQueryString(int customerId, String orgNodeCode, int hierarchyLevel, String applicationName, String sharedKey, String userRole, String ipAddress, String validUntilDate) throws Exception
	{
		StringBuilder builder = new StringBuilder();

		//** Params=customer_id=123&OrgNodeCode=abc&Hierarchy_Level=1&application_name=OAS&datetimestamp=&Shared_Key=&User_Role=Admin
		//builder.append(API_KEY_PARAMETER_NAME).append(urlEncode(username));
		builder.append(CUSTOMERID_PARAMETER_NAME).append(urlEncode(String.valueOf(customerId)));
		builder.append(ORGNODECODE_PARAMETER_NAME).append(urlEncode(orgNodeCode));
		builder.append(HIERARCHYLEVEL_PARAMETER_NAME).append(urlEncode(String.valueOf(hierarchyLevel)));
		builder.append(APPNAME_PARAMETER_NAME).append(urlEncode(applicationName));
		//builder.append(SHAREDKEY_PARAMETER_NAME).append(urlEncode(sharedKey));
		builder.append(USERROLE_PARAMETER_NAME).append(urlEncode(userRole));
		builder.append(EXPIRY_DATE_PARAMETER_NAME).append(urlEncode(validUntilDate));
		
		if(null != ipAddress && ipAddress.trim().length() > 0)
			builder.append(IP_ADDRESS_PARAMETER_NAME).append(urlEncode(ipAddress));

		return builder;
	}

	/**
	 * Append the authentication code to an unauthenticated query string
	 * @param queryString	unauthenticated query string
	 * @param signature	authentication signature
	 * @return authenticated query string
	 */
	private void appendAuthenticationCode(Appendable queryString, String signature) throws Exception
	{
		queryString.append(SIGNATURE_PARAMETER_NAME).append(urlEncode(signature));
	}

	/**
	 * Perform HMAC SHA-1 hash on the supplied message
	 * @param message	message to hash
	 * @return hashed message
	 */
	private String getAuthenticationCode(String message) throws Exception
	{
		Mac messageAuthenticationCode = Mac.getInstance(ENCODING_ALGORITHM);

		messageAuthenticationCode.init(secretKey);
		messageAuthenticationCode.update(message.getBytes());

		byte[] digest = messageAuthenticationCode.doFinal();

		return Base64.encode(digest);
	}

	/**
	 * Percent-escape any characters that are not valid in a URL
	 * @param value	raw value to encode
	 * @return encoded value
	 */
	private String urlEncode(String value) throws Exception
	{
		return URLEncoder.encode(value, URL_ENCODING);
	}
	
	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public int getSignatureValiditySeconds() {
		return signatureValiditySeconds;
	}

	public void setSignatureValiditySeconds(int signatureValiditySeconds) {
		this.signatureValiditySeconds = signatureValiditySeconds;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getURL_ENCODING() {
		return URL_ENCODING;
	}

	public void setURL_ENCODING(String uRL_ENCODING) {
		URL_ENCODING = uRL_ENCODING;
	}

	public String getENCODING_ALGORITHM() {
		return ENCODING_ALGORITHM;
	}

	public void setENCODING_ALGORITHM(String eNCODING_ALGORITHM) {
		ENCODING_ALGORITHM = eNCODING_ALGORITHM;
	}
	
}
