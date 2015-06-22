package main.java.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.util.FileCopyUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.auth.BasicAWSCredentials;
public class AWSStorageUtil {
	private BasicAWSCredentials credentials;
	private static AmazonS3 s3;
	private String bucketName;
	private static volatile AWSStorageUtil awsstorageUtil = new AWSStorageUtil();
	private static String keyName = "";
	private static String accessKey = "";
	private static String secretKey = "";
	/*
	Method AWSStorageUtil creates the connection object to connect with amazon s3 using accessKey and secretKey
	*/
	private AWSStorageUtil() {
		try {
			this.accessKey = AWSResourceUtil.getAWSDetail("oas.test.accessKey");
			this.secretKey = AWSResourceUtil.getAWSDetail("oas.test.secretKey");
			this.credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
			this.bucketName = AWSResourceUtil.getAWSDetail("oas.test.bucketName");
			this.s3 = new AmazonS3Client(this.credentials);

		} catch (Exception e) {
			System.out.println("exception while creating awss3client : " + e);
		}
	}

	public static AWSStorageUtil getInstance() {
		return awsstorageUtil;
	}

/*
Method getObjectList prints the object summary of a given s3 bucket
*/
	public void getObjectList() {
		System.out.println("Listing objects within " +bucketName);
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			System.out.println(" - " + objectSummary.getKey() + " " + "(size = " + objectSummary.getSize() + ")");
		}
		System.out.println("Exit: getObjectList()");
	}

/*
Read a predefined file from s3 bucket
*/
	public byte[] getBytesFromS3(String key) throws IOException {
		System.out.println("Downloading an object");
		S3Object object = s3.getObject(new GetObjectRequest(this.bucketName, key));
		S3ObjectInputStream inputStream = object.getObjectContent();
		byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
		inputStream.close(); // Must be closed as it is directly opened from Amazon
		return bytes;
	}
	
	

	public void downloadAudioFile(String audioUrl){
		
		AWSStorageUtil aWSStorageUtil = AWSStorageUtil.getInstance();
		aWSStorageUtil.getObjectList();
		String audioLocation = "/tmp/";
		try {
				byte[] bytes = aWSStorageUtil.getBytesFromS3(audioUrl);
				System.out.println(bytes.length);
				
				AWSStorageUtil.createLocal(audioLocation, audioUrl);
				FileCopyUtils.copy(bytes, new File(audioLocation + audioUrl));
				System.out.println("End");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
	}
	/*
	Main Method starts here
		
	*/	
	/*public static void main(String args[]) throws Exception {
		System.out.println("Start");
		AWSStorageUtil aWSStorageUtil = AWSStorageUtil.getInstance();
		//List<Bucket> buckets = s3.listBuckets();
		//System.out.println(buckets);
		aWSStorageUtil.getObjectList();
		int testRosterId = Integer.parseInt(RosterConfigUtil.getDetail("oas.test.rosterId"));
		String itemId = RosterConfigUtil.getDetail("oas.test.itemId");
		keyName = DBUtil.fetchAudioUrl(testRosterId, itemId);
		byte[] bytes = aWSStorageUtil.getBytesFromS3(keyName);
		System.out.println(bytes.length);
		createLocal("C:/AWS/", keyName);
		FileCopyUtils.copy(bytes, new File("C:/AWS/" + keyName));
		System.out.println("End");
	}*/

public static void createLocal(String parentDirLoc, String key) throws Exception {
	String dir = parentDirLoc + key.substring(0, key.lastIndexOf("/"));
	String fileName = key.substring(key.lastIndexOf("/")+1);
	System.out.println(dir+", "+fileName);
	File parentDir = new File(dir);
	System.out.println(parentDir.mkdirs());
	File file = new File(dir + "/" + fileName);
	if (!file.isFile()) {
		System.out.println(file.createNewFile());
	}
	
}

}