package edu.uta.cse.craigslist.entity;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "craiglist")
public class Craiglist {

	@DynamoDBHashKey(attributeName = "itemId")
	@DynamoDBAutoGeneratedKey
	private String itemId;

	@DynamoDBAttribute(attributeName = "itemName")
	private String itemName;

	@DynamoDBAttribute(attributeName = "description")
	private String description;

	@DynamoDBAttribute(attributeName = "price")
	private Double price;

	@DynamoDBAttribute(attributeName = "postedBy")
	private String postedBy;

	@DynamoDBAttribute(attributeName = "postedDate")
	private Date postedDate;
	
	@DynamoDBAttribute(attributeName = "encodedImage")
	private String encodedImage;
	
	@DynamoDBAttribute(attributeName = "s3url")
	private String s3url;

	
	public String getS3url() {
		return s3url;
	}

	public void setS3url(String s3url) {
		this.s3url = s3url;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public String getEncodedImage() {
		return encodedImage;
	}

	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
	}
		
}
