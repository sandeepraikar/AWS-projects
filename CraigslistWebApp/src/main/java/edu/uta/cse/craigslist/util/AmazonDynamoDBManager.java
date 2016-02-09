package edu.uta.cse.craigslist.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import edu.uta.cse.craigslist.entity.Craiglist;

public class AmazonDynamoDBManager {

	private static Logger LOGGER = LoggerFactory
			.getLogger(AmazonDynamoDBManager.class);
	private static DynamoDB dynamoDB;
	private static AmazonDynamoDBClient dbClient;

	static{
		try {

			AWSCredentials awscredentials = new BasicAWSCredentials(Constants.AWS_ACCESS_KEY,Constants.AWS_SECRET_KEY);
			dbClient = new AmazonDynamoDBClient(awscredentials);
			Region usWest2 = Region.getRegion(Regions.US_WEST_2);
			dbClient.setRegion(usWest2);
			dynamoDB = new DynamoDB(dbClient);

		} catch (AmazonServiceException e) {
			LOGGER.error("Amazon Service Exception occured : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Amazon Client Exception occured : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void loadData(Craiglist newItem, String imageName) {
		try {
			
			InputStream stream =AmazonS3Manager.getObject(imageName);
			
			LOGGER.info("Content Length : "+ AmazonS3Manager.getObjectMetadata(imageName).getContentLength());
			if(AmazonS3Manager.getObjectMetadata(imageName).getContentLength()<35000){
				newItem.setEncodedImage(imageEncoding(stream,imageName));
			}
			URL objectURL =AmazonS3Manager.getImageURL(imageName);
			if(objectURL!=null){
				newItem.setS3url(objectURL.toString());
			}
			LOGGER.info("Starting to save to dynamodb");
			
			DynamoDBMapper mapper = new DynamoDBMapper(dbClient);
			mapper.save(newItem);
			
			LOGGER.info("Persisted successfully in database");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("restriction")
	private static String imageEncoding(InputStream stream ,String imageName) {
		String encodedImage=null;
		try {
			BufferedImage image = ImageIO.read(stream);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			//byte[] res = baos.toByteArray();
			encodedImage = Base64.encode(baos.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return encodedImage;
	}

	public static List<Craiglist> retrieveData() {

		Map<String, AttributeValue> lastKeyEvaluated = null;
		List<Craiglist> items = new ArrayList<>();
		do {
			ScanRequest scanRequest = new ScanRequest()
					.withTableName("craiglist").withLimit(10)
					.withExclusiveStartKey(lastKeyEvaluated);

			ScanResult result = dbClient.scan(scanRequest);

			DynamoDBMapper mapper = new DynamoDBMapper(dbClient);
			for (Map<String, AttributeValue> item : result.getItems()) {
				items.add(mapper.load(Craiglist.class, item.get("itemId")
						.getS()));
			}
			lastKeyEvaluated = result.getLastEvaluatedKey();
		} while (lastKeyEvaluated != null);
		LOGGER.info("List of items retrieved" + items.size());
		return items;

	}

	public static void checkIfRequiredTablesExist() {
		if(!Tables.doesTableExist(dbClient, Constants.TABLE_NAME)){

			try {

				ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
				attributeDefinitions.add(new AttributeDefinition()
						.withAttributeName("itemId").withAttributeType("S"));

				ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
				keySchema.add(new KeySchemaElement().withAttributeName("itemId")
						.withKeyType(KeyType.HASH));

				CreateTableRequest request = new CreateTableRequest()
						.withTableName(Constants.TABLE_NAME)
						.withKeySchema(keySchema)
						.withAttributeDefinitions(attributeDefinitions)
						.withProvisionedThroughput(
								new ProvisionedThroughput().withReadCapacityUnits(
										5L).withWriteCapacityUnits(6L));

				LOGGER.info("Issuing CreateTable request for " + Constants.TABLE_NAME);
				Table table = dynamoDB.createTable(request);

				LOGGER.info("Waiting for " + Constants.TABLE_NAME
						+ " to be created...this may take a while...");
				table.waitForActive();

				getTableInformation(table.getTableName());

			} catch (Exception e) {
				LOGGER.info("CreateTable request failed for " + Constants.TABLE_NAME);
				LOGGER.info(e.getMessage());
				e.printStackTrace();
			}

		}
		
	}
	

	private static void getTableInformation(String tableName) {

		LOGGER.info("Describing " + tableName);

		TableDescription tableDescription = dynamoDB.getTable(tableName)
				.describe();
		System.out.format("Name: %s:\n" + "Status: %s \n"
				+ "Provisioned Throughput (read capacity units/sec): %d \n"
				+ "Provisioned Throughput (write capacity units/sec): %d \n",
				tableDescription.getTableName(), tableDescription
						.getTableStatus(), tableDescription
						.getProvisionedThroughput().getReadCapacityUnits(),
				tableDescription.getProvisionedThroughput()
						.getWriteCapacityUnits());
	}
	
	public static void deleteItem(String itemId){

			Table table = dynamoDB.getTable(Constants.TABLE_NAME);
			DeleteItemOutcome outcome = table.deleteItem("itemId", itemId);
			LOGGER.info("Delete successfull! "+ outcome.getDeleteItemResult());
			
	}
	


}
