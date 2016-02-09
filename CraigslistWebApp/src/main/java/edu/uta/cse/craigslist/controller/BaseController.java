package edu.uta.cse.craigslist.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import edu.uta.cse.craigslist.entity.Craiglist;
import edu.uta.cse.craigslist.util.AmazonDynamoDBManager;
import edu.uta.cse.craigslist.util.AmazonS3Manager;

@Controller
public class BaseController {

	private static Logger LOGGER = LoggerFactory
			.getLogger(BaseController.class);

	@RequestMapping(value = "/addItem", method = RequestMethod.GET)
	public String login(ModelMap map) {
		return "AddItem";
	}
	
	@RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
	public String deleteItem(@RequestParam(value = "itemId") String itemId) {
		LOGGER.info("Inside deleteItem method");
		AmazonDynamoDBManager.deleteItem(itemId);
		return "Home";
	}
	
	@RequestMapping(value = "/retrieveCraigslist", method = RequestMethod.GET)
	public String retrieveObjectsFromDB(ModelMap map) {
		List<Craiglist> objectsFromDB = new ArrayList<>();
		objectsFromDB = AmazonDynamoDBManager.retrieveData();
		map.addAttribute("Craiglist", new Craiglist());
		map.addAttribute("allItems",objectsFromDB);
		if (objectsFromDB.isEmpty()) {
			map.addAttribute("SearchResult", "No Search Results Found");
		}
		return "Home";
	}
	

	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public @ResponseBody String uploadDataSet(MultipartRequest request,ModelMap map) {
		MultipartFile mpf = null;
		try {

			LOGGER.info("inside upload method!!");
			
			Iterator<String> itr = request.getFileNames();
			//String fileNameWithExtension=null;
			while (itr.hasNext()) {
				mpf = request.getFile(itr.next());
				LOGGER.info("fileName : "+mpf.getOriginalFilename());
				LOGGER.info("inputstream : "+mpf.getInputStream());
				InputStream data = new ByteArrayInputStream(mpf.getBytes());
				AmazonS3Manager.uploadFile(data,
						mpf.getOriginalFilename());
				LOGGER.info("File : "+mpf.getOriginalFilename()+"uploaded successfully!!" );
			}

		} catch (Exception ex) {
			LOGGER.error("Exception occured : " + ex.getMessage());
			ex.printStackTrace();
		}
		return mpf.getOriginalFilename();
	}

	@RequestMapping(value = "/addNewItemImpl", method = RequestMethod.POST)
	public String addNewItem(@ModelAttribute("Craiglist") Craiglist craiglist,
			@RequestParam(value = "imageName") String uploadedImageName,
			BindingResult result, ModelMap map, Principal principal) {
		LOGGER.info("Inside addNewItemImpl");
		LOGGER.info("Values retrieved from Add Items page");
		LOGGER.info("Craiglist Item Name : "+craiglist.getItemName() );
		LOGGER.info("Uploaded Image Name: "+uploadedImageName);
		
		craiglist.setPostedDate(new Date());
		craiglist.setPostedBy(principal.getName());
		AmazonDynamoDBManager.loadData(craiglist, uploadedImageName);
		return "Home";
	}

}
