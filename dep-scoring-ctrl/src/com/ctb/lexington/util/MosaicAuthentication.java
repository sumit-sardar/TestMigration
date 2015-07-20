package com.ctb.lexington.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ResourceBundle;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;



public class MosaicAuthentication {
	private static final ResourceBundle rb = ResourceBundle.getBundle("config");
	private static final String HTTP_REQUEST_TYPE = rb.getString("http.request.type");
	private static final String CONTENT_TYPE = rb.getString("content.type");
	private static final String MOSAIC_ENCRYPTION_METHOD_TITLE = rb.getString("mosaic.encryption.method.title");
	private static final String MOSAIC_TIMESTAMP_TITLE = rb.getString("mosaic.timestamp.title");
	private static final String MOSAIC_VERSION_TITLE = rb.getString("mosaic.version.title");
	private static final String MOSAIC_AUTHORIZATION_TITLE = rb.getString("mosaic.authorization.title");
	private static final String MOSAIC_VERSION_VALUE = rb.getString("mosaic.version.value");
	private static final String HEADER_KEY_SEPARATION = rb.getString("header.key.separation");
	private static final String HEADER_SEPARATION = rb.getString("header.separation");
	private static final String API_ENDPOINT = rb.getString("api.endpoint");
	private static final String MOSAIC_ACCESS_KEY = rb.getString("mosaic.access.key");
	private static final String MOSAIC_API_TITLE = rb.getString("mosaic.api.title");
	private static final String ENCODE_CHARSET = rb.getString("encode.charset");
	// 32 zero used for padding
	private static final String ZERO_PADD = rb.getString("zero.padd");
	private static final String MOSAIC_URL = rb.getString("mosaic.url");

	public String authenticateResponseFromMSS(String mosaicScoringRequest ,String timeStampUTC) {
		String signature = null;
		String mssResponse = null;
		
		//Returning the call as requested data is null
		if (mosaicScoringRequest == null || mosaicScoringRequest.isEmpty() )
			return null;

		try {
			//Get the signature
			signature = generateMosaicSignature(mosaicScoringRequest,
					timeStampUTC);

			//Invoking http url
			//System.out.print("\nHttp: \n\n");
			mssResponse = invokeMosaicAPIHttp(signature, mosaicScoringRequest , timeStampUTC);

			/*
			 * //Invoking https url System.out.print("\nHttps: \n\n");
			 * mssResponse = invokeMosaicAPIHttps(signature, mosaicScoringRequest , timeStampUTC);
			 */
			
			return mssResponse;
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Performs https post request with required request header and request body .
	 * 
	 * @param request
	 */
	private String invokeMosaicAPIHttp(String signature, String requestBody , String timeStampUTC)
			throws Exception {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		StringBuilder authorizationHeader = new StringBuilder();
		authorizationHeader.append(MOSAIC_API_TITLE).append(" ")
				.append(MOSAIC_ACCESS_KEY).append(HEADER_KEY_SEPARATION)
				.append(signature);

		HttpPost post = new HttpPost(MOSAIC_URL);
		post.addHeader("Content-Type", CONTENT_TYPE);
		post.addHeader(MOSAIC_ENCRYPTION_METHOD_TITLE, getMD5HexString(requestBody));
		post.addHeader(MOSAIC_TIMESTAMP_TITLE, timeStampUTC);
		post.addHeader(MOSAIC_VERSION_TITLE, MOSAIC_VERSION_VALUE);
		post.addHeader(MOSAIC_AUTHORIZATION_TITLE, authorizationHeader.toString());

		HttpEntity entity = new ByteArrayEntity(requestBody.getBytes(ENCODE_CHARSET));
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		//System.out.println("post: " + post.toString());
		String result = EntityUtils.toString(response.getEntity());
		//System.out.println("result \n\n" + result);
		return result;
	}
	
	
	
	/**
	 * Performs https post request with required request header and request body .
	 * 
	 * @param request
	 */
	private void invokeMosaicAPIHttps(String signature, String requestBody ,  String timeStampUTC)
			throws Exception {
		HttpsURLConnection conn = null;
		StringBuilder authorizationHeader = new StringBuilder();
		authorizationHeader.append(MOSAIC_API_TITLE).append(" ")
				.append(MOSAIC_ACCESS_KEY).append(HEADER_KEY_SEPARATION)
				.append(signature);
		URL obj = new URL(MOSAIC_URL);
		conn = (HttpsURLConnection) obj.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", CONTENT_TYPE);
		conn.setRequestProperty("x-mosaic-content-md5", getMD5HexString(requestBody));
		conn.setRequestProperty("x-mosaic-timestamp", timeStampUTC);
		conn.setRequestProperty("x-mosaic-version", MOSAIC_VERSION_VALUE);
		conn.setRequestProperty("Authorization", authorizationHeader.toString());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		
		// Send post request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(requestBody);
		wr.flush();
		wr.close();
	 
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + MOSAIC_URL);
		System.out.println("Post parameters : " + requestBody);
		System.out.println("Response Code : " + responseCode);
	 
		BufferedReader in = 
	             new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
		
		
	}

	/***
	 * 
	 * Generates the signature using the studentResponse, key and required logics.
	 * @param studentResponse - This data is used for generating the md5digest
	 * @param timeStampUTC - Requested Time in UTC time-zone
	 * @return - Generated signature
	 * @throws SignatureException
	 * @throws NoSuchAlgorithmException
	 */
	private String generateMosaicSignature(String studentResponse , String timeStampUTC)
			throws SignatureException, NoSuchAlgorithmException {
		StringBuilder formattedRequest = new StringBuilder();
		StringBuilder canonicalizedMosaicHeaders = new StringBuilder();

		// md5 representation of request part
		String md5Digest = getMD5HexString(studentResponse);

		// Generate CanonicalizedMosaicHeaders
		canonicalizedMosaicHeaders.append(MOSAIC_ENCRYPTION_METHOD_TITLE)
				.append(HEADER_KEY_SEPARATION).append(md5Digest)
				.append(HEADER_SEPARATION).append(MOSAIC_TIMESTAMP_TITLE)
				.append(HEADER_KEY_SEPARATION).append(timeStampUTC)
				.append(HEADER_SEPARATION).append(MOSAIC_VERSION_TITLE)
				.append(HEADER_KEY_SEPARATION).append(MOSAIC_VERSION_VALUE);

		// Combine request with request headers.
		formattedRequest.append(HTTP_REQUEST_TYPE).append(HEADER_SEPARATION)
				.append(CONTENT_TYPE).append(HEADER_SEPARATION)
				.append(canonicalizedMosaicHeaders).append(HEADER_SEPARATION)
				.append(API_ENDPOINT).append(HEADER_SEPARATION);

		//System.out.println("String for signature -> \n" + formattedRequest.toString());
		// Generate signature with access-key
		String signature = new AmazonSignatureUtils().generateSignatureUsingHmacSHA1(
				MOSAIC_ACCESS_KEY, formattedRequest.toString());

		return signature.trim();
	}


	/**
	 * Generates the md5 digest of the input string
	 * 
	 * @param request
	 *            - This the request for which the md5 digest has to be
	 *            calculated.
	 * @return - String - Calculated md5 digest
	 */
	private String getMD5HexString(String request)
			throws NoSuchAlgorithmException {
		MessageDigest m;
		String hashtext = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(request.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			int offset = (32 - hashtext.length());
			if (offset > 0) {
				String zeroPaddRequired = ZERO_PADD.substring(0, offset);
				hashtext = zeroPaddRequired.concat(hashtext);
			}
			return hashtext.trim();
		} catch (NoSuchAlgorithmException e) {
			System.err.print("getMD5HexString Exception..");
			e.printStackTrace();
			throw e;
		}
	}

}
