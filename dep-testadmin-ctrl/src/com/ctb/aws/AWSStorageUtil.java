package com.ctb.aws;

import java.io.IOException;
import java.net.URL;



import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;
public class AWSStorageUtil {
	private BasicAWSCredentials credentials;
	private static AmazonS3 s3;
	private String bucketName;
	private static volatile AWSStorageUtil awsstorageUtil = new AWSStorageUtil();
	private static String keyName = "";
	private static String accessKey = "";
	private static String secretKey = "";
	/*
	 * Constructor AWSStorageUtil creates the connection object to connect with
	 * amazon s3 using accessKey and secretKey
	 */
	private AWSStorageUtil() {
		try {
			AWSResourceUtil awsResourceUtil = new AWSResourceUtil();
			this.accessKey = awsResourceUtil.getAWSDetail("oas.test.accessKey");
			this.secretKey = awsResourceUtil.getAWSDetail("oas.test.secretKey");
			this.credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
			this.bucketName = awsResourceUtil.getAWSDetail("oas.test.bucketName");
			this.s3 = new AmazonS3Client(this.credentials);

		} catch (Exception e) {
			System.out.println("exception while creating awss3client : " + e);
		}
	}
	public static AWSStorageUtil getInstance() {
		return awsstorageUtil;
	}

	public String HttpsUrlfromS3(String key) {

		URL s3AudioUrl = null;
		String s3AudioUrlString = null;
		String tempKey = null;
		S3Object object = null;
		try {
			tempKey = key.substring(key.indexOf(this.bucketName));
			key = tempKey.substring(this.bucketName.length() + 1, tempKey.lastIndexOf('.')) + ".mp3";
			System.out.println("Key-->" + key);
			System.out.println("Bucket Name-->" + this.bucketName);
			object = s3.getObject(this.bucketName, key);
			GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(this.bucketName, key);
			s3AudioUrl = s3.generatePresignedUrl(request);
			System.out.println("generatePresignedUrl-->" + s3AudioUrl);
			s3AudioUrlString = s3AudioUrl.toString();
		} catch (AmazonS3Exception e) {
			String errorCode = e.getMessage();
			System.out.println("ErrorCode-->" + errorCode);
			errorCode = "Unable to retrieve test question response from server.";
			s3AudioUrlString = errorCode;
		} catch (AmazonServiceException e) {
			String errorCode = e.getMessage();
			System.out.println("ErrorCode-->" + errorCode);
			errorCode = "Unable to retrieve test question response from server.";
			s3AudioUrlString = errorCode;
		} catch (Exception e) {
			String errorCode = e.getMessage();
			System.out.println("ErrorCode-->" + errorCode);
			errorCode = "Unable to retrieve test question response from server.";
			s3AudioUrlString = errorCode;
		} finally {
				try {
					if (object != null) {
						object.close(); // Mandatory
					}
					//((AmazonS3Client) s3).shutdown(); // Optional
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			return s3AudioUrlString;
		}

	}

	
}