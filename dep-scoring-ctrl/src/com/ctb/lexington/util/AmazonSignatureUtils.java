package com.ctb.lexington.util;

import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.amazonaws.util.BinaryUtils;

public class AmazonSignatureUtils {

	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	/**
	 * Generate signature following - "Amazon AWS Signature v2 Process" and
	 * signature method - "HMAC-SHA256"
	 * 
	 * @param accessKey
	 *            - This private key is to used to generate signature.
	 * @param request
	 *            - This the request for which the signature has to be
	 *            calculated.
	 * @return - String - Generated signature
	 */
	public String generateSignatureUsingHmacSHA256(String key, String data)
			throws java.security.SignatureException {
		String result;
		try {
			// Get an hmac_sha256 key from the raw key bytes.
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF8"),
					HMAC_SHA256_ALGORITHM);
			// Get an hmac_sha256 Mac instance and initialize with the signing
			// key.
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(signingKey);
			// Compute the hmac on input data bytes.
			byte[] rawHmac = mac.doFinal(data.getBytes("UTF8"));
			// Base64-encode the hmac by using the utility in the SDK
			result = BinaryUtils.toBase64(rawHmac);
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMACSHA256 : "
					+ e.getMessage());
		}
		return result;
	}

	/**
	 * Generate signature following - "Amazon AWS Signature v2 Process" and
	 * signature method - "HMAC-SHA1"
	 * 
	 * @param accessKey
	 *            - This private key is to used to generate signature.
	 * @param request
	 *            - This the request for which the signature has to be
	 *            calculated.
	 * @return - String - Generated signature
	 */
	public String generateSignatureUsingHmacSHA1(String key, String data)
			throws java.security.SignatureException {
		String result;
		try {
			// Get an hmac_sha1 key from the raw key bytes.
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF8"),
					HMAC_SHA1_ALGORITHM);
			// Get an hmac_sha1 Mac instance and initialize with the signing
			// key.
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			// Compute the hmac on input data bytes.
			byte[] rawHmac = mac.doFinal(data.getBytes("UTF8"));
			// Base64-encode the hmac by using the utility in the SDK
			result = BinaryUtils.toBase64(rawHmac);
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMACSHA1 : "
					+ e.getMessage());
		}
		return result;
	}

}
