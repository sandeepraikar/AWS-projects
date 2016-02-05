package edu.uta.cse.aws.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import edu.uta.cse.util.Constants;

public class ConnectS3 {

	private static Logger LOGGER = LoggerFactory.getLogger(ConnectS3.class);

	public static void main(String args[]) {

		File uploadFile = new File(
				"D:/Test/check.txt");

		try {
			AmazonS3 s3Client = new AmazonS3Client();
			Region usWest2 = Region.getRegion(Regions.US_WEST_2);
			s3Client.setRegion(usWest2);
			
			System.out.println("Please choose from the below Menu");
			System.out.println("Press 1. Create NewBucket in Amazon S3");
			System.out.println("Press 2. Upload File");
			System.out.println("Press 3. Download File");
			System.out
					.println("Press 4. List all the Objects in the existing Bucket");
			System.out.println("Press 5. List the Buckets in S3");
			System.out.println();

			while (true) {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				String key = br.readLine();

				switch (key) {
				case "1":
					LOGGER.info("Please enter the Bucket name");
					createBucket(s3Client);

					break;
				case "2":
					LOGGER.info("Initiating Upload...");
					uploadFile(s3Client, uploadFile);
					LOGGER.info("File Uploaded Successfully!!");
					break;
				case "3":
					System.out
							.println("Please enter the bucket name from which the file has to be downloaded");
					System.out.println();
					BufferedReader breader = new BufferedReader(
							new InputStreamReader(System.in));
					String bucketName = breader.readLine();
					if (s3Client.doesBucketExist(bucketName)) {
						System.out
								.println("Please enter the fileName which needs to be downloaded");

						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(System.in));
						String fileName = bufferedReader.readLine();
						LOGGER.info("Inititating Download..");
						if (fileName.length() > 0) {
							downloadFile(s3Client, bucketName, fileName);
							LOGGER.info("File Downloaded Successfully!!");
						} else {
							LOGGER.info("Please enter the fileName to be downloaded");
						}
						System.out.println();
					} else {
						LOGGER.error("Bucket name does not exist!!");
					}
					break;
				case "4":
					System.out.println("List Objects in existing Bucket!");
					listObjects(s3Client);
					System.out.println();
					break;

				case "5":
					System.out.println("List all the Buckets!");
					listBuckets(s3Client);
					System.out.println();
					break;

				default:
					System.out.println("Invaid selection");

					System.out.println("Please choose from the below Menu");
					System.out.println("Press 1. Create NewBucket in Amazon S3");
					System.out.println("Press 2. Upload File");
					System.out.println("Press 3. Download File");
					System.out
							.println("Press 4. List all the Objects in the existing Bucket");
					System.out.println("Press 5. List the Buckets in S3");
					break;
				}
			}

		} catch (AmazonServiceException ase) {
			LOGGER.info("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			LOGGER.info("Error Message:    " + ase.getMessage());
			LOGGER.info("HTTP Status Code: " + ase.getStatusCode());
			LOGGER.info("AWS Error Code:   " + ase.getErrorCode());
			LOGGER.info("Error Type:       " + ase.getErrorType());
			LOGGER.info("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			LOGGER.info("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			LOGGER.info("Error Message: " + ace.getMessage());
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private static void createBucket(AmazonS3 s3Client) {
		long startTime, endTime;
		try {
			startTime = System.currentTimeMillis();
			System.out
					.println("Please enter the bucket name which needs to be created!");
			System.out.println();
			BufferedReader breader = new BufferedReader(new InputStreamReader(
					System.in));
			String bucketName = breader.readLine();
			if (!s3Client.doesBucketExist(bucketName)) {
				Bucket bucket = s3Client.createBucket(bucketName);
				endTime = System.currentTimeMillis();
				System.out.println(bucket.getName() + "created Successfully");
				LOGGER.info("Total Time taken in milli seconds: "
						+ (endTime - startTime));
			} else {
				LOGGER.error("Bucket alread exist!!");
			}

		} catch (AmazonServiceException e) {
			LOGGER.error("Amazon Service Exception occured while creating new Bucket : "
					+ e);
			e.printStackTrace();
		} catch (AmazonClientException e) {
			LOGGER.error("Amazon Client Exception occured while creating new Bucket : "
					+ e);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.info("IOException occured" + e);
			e.printStackTrace();
		}
	}

	private static void uploadFile(AmazonS3 s3Client, File uploadFile) {
		try {
			long startTime = System.currentTimeMillis();
			System.out
					.println("Please enter the bucket name to which the file has to be uploaded!");
			System.out.println();
			BufferedReader breader = new BufferedReader(new InputStreamReader(
					System.in));
			String bucketName = breader.readLine();
			if (s3Client.doesBucketExist(bucketName)) {
				LOGGER.info("Uploading the file");
				
				/*
				PutObjectRequest putObjectRequest = new PutObjectRequest(
						bucketName + "/", uploadFile.getName(), uploadFile);*/
				PutObjectRequest putObjectRequest = new PutObjectRequest(
						bucketName , uploadFile.getName(), uploadFile);
				PutObjectResult result = s3Client.putObject(putObjectRequest);
			
				LOGGER.info("Version ID : " + result.getVersionId());
				LOGGER.info("Etag:" + result.getETag() + "-->" + result);

				long endTime = System.currentTimeMillis();
				LOGGER.info("Total Time taken in milli seconds: "
						+ (endTime - startTime));
				
			} else {
				LOGGER.error("Specified Bucket does not exist!!");
			}
		} catch (IOException e) {
			LOGGER.info("IOException occured" + e);
			e.printStackTrace();
		}
	}

	private static void listObjects(AmazonS3 s3Client) {
		System.out.println("Please enter the bucket to list all the objects!");
		System.out.println();
		String bucketName = null;
		BufferedReader breader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			bucketName = breader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (s3Client.doesBucketExist(bucketName)) {
			LOGGER.info("Listing objects");

			ObjectListing objectListing = s3Client
					.listObjects(new ListObjectsRequest()
							.withBucketName(bucketName));

			for (S3ObjectSummary objectSummary : objectListing
					.getObjectSummaries()) {
				LOGGER.info(" - " + objectSummary.getKey() + "  " + "(size = "
						+ objectSummary.getSize() + ")");
			}
		} else {
			LOGGER.error("Bucket name does not exist!");
		}
	}

	private static void downloadFile(AmazonS3 s3Client, String bucketName,
			String key) {
		FileOutputStream oStream = null;

		try {
			oStream = new FileOutputStream(Constants.DOWNLOAD_LOCATION + "/"
					+ key);
			LOGGER.info("Download Path : " + Constants.DOWNLOAD_LOCATION + "/"
					+ key);
			long startTime = System.currentTimeMillis();
			LOGGER.info("Initiating download....");
			LOGGER.info("Bucket name retrieved : "+bucketName+", key name: "+ key);
			GetObjectRequest request = new GetObjectRequest(bucketName, key);
			
			S3Object object = s3Client.getObject(request);
			
			S3ObjectInputStream objectContent = object.getObjectContent();
			IOUtils.copy(objectContent, oStream);
			long endTime = System.currentTimeMillis();
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));
		} catch (FileNotFoundException e) {
			LOGGER.info("File Not Found exception: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.info("IO exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				oStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void listBuckets(AmazonS3 s3Client) {
		System.out.println("Listing buckets");
		for (Bucket bucket : s3Client.listBuckets()) {
			LOGGER.info(" - " + bucket.getName());
		}
	}

}