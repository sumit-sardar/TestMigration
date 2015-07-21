package com.ctb.lexington.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;



public class MosaicAuthentication {

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
		authorizationHeader.append(MosaicConstantUtils.MOSAIC_API_TITLE).append(" ")
				.append(MosaicConstantUtils.MOSAIC_ACCESS_KEY).append(MosaicConstantUtils.HEADER_KEY_SEPARATION)
				.append(signature);

		HttpPost post = new HttpPost(MosaicConstantUtils.MOSAIC_URL);
		post.addHeader("Content-Type", MosaicConstantUtils.CONTENT_TYPE);
		post.addHeader(MosaicConstantUtils.MOSAIC_ENCRYPTION_METHOD_TITLE, getMD5HexString(requestBody));
		post.addHeader(MosaicConstantUtils.MOSAIC_TIMESTAMP_TITLE, timeStampUTC);
		post.addHeader(MosaicConstantUtils.MOSAIC_VERSION_TITLE, MosaicConstantUtils.MOSAIC_VERSION_VALUE);
		post.addHeader(MosaicConstantUtils.MOSAIC_AUTHORIZATION_TITLE, authorizationHeader.toString());

		HttpEntity entity = new ByteArrayEntity(requestBody.getBytes(MosaicConstantUtils.CHARACTER_ENCODE));
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
		authorizationHeader.append(MosaicConstantUtils.MOSAIC_API_TITLE).append(" ")
				.append(MosaicConstantUtils.MOSAIC_ACCESS_KEY).append(MosaicConstantUtils.HEADER_KEY_SEPARATION)
				.append(signature);
		URL obj = new URL(MosaicConstantUtils.MOSAIC_URL);
		conn = (HttpsURLConnection) obj.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", MosaicConstantUtils.CONTENT_TYPE);
		conn.setRequestProperty("x-mosaic-content-md5", getMD5HexString(requestBody));
		conn.setRequestProperty("x-mosaic-timestamp", timeStampUTC);
		conn.setRequestProperty("x-mosaic-version", MosaicConstantUtils.MOSAIC_VERSION_VALUE);
		conn.setRequestProperty("Authorization", authorizationHeader.toString());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		
		// Send post request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(requestBody);
		wr.flush();
		wr.close();
	 
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + MosaicConstantUtils.MOSAIC_URL);
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
		canonicalizedMosaicHeaders.append(MosaicConstantUtils.MOSAIC_ENCRYPTION_METHOD_TITLE)
				.append(MosaicConstantUtils.HEADER_KEY_SEPARATION).append(md5Digest)
				.append(MosaicConstantUtils.HEADER_SEPARATION).append(MosaicConstantUtils.MOSAIC_TIMESTAMP_TITLE)
				.append(MosaicConstantUtils.HEADER_KEY_SEPARATION).append(timeStampUTC)
				.append(MosaicConstantUtils.HEADER_SEPARATION).append(MosaicConstantUtils.MOSAIC_VERSION_TITLE)
				.append(MosaicConstantUtils.HEADER_KEY_SEPARATION).append(MosaicConstantUtils.MOSAIC_VERSION_VALUE);

		// Combine request with request headers.
		formattedRequest.append(MosaicConstantUtils.HTTP_REQUEST_TYPE).append(MosaicConstantUtils.HEADER_SEPARATION)
				.append(MosaicConstantUtils.CONTENT_TYPE).append(MosaicConstantUtils.HEADER_SEPARATION)
				.append(canonicalizedMosaicHeaders).append(MosaicConstantUtils.HEADER_SEPARATION)
				.append(MosaicConstantUtils.API_ENDPOINT).append(MosaicConstantUtils.HEADER_SEPARATION);

		//System.out.println("String for signature -> \n" + formattedRequest.toString());
		// Generate signature with access-key
		String signature = new AmazonSignatureUtils().generateSignatureUsingHmacSHA1(
				MosaicConstantUtils.MOSAIC_ACCESS_KEY, formattedRequest.toString());

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
				String zeroPaddRequired = MosaicConstantUtils.ZERO_PADD.substring(0, offset);
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
