package edu.uta.cse.craigslist.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




import edu.uta.cse.craigslist.entity.Craiglist;
import edu.uta.cse.craigslist.util.AmazonDynamoDBManager;


@Controller
public class LoginController {

	
	private static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	/**
	 * This method 
	 * @param map : ModelMap object, for adding model data objects in the view
	 * @return : redirects to "Login.jsp"
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String login(ModelMap map) {
		LOGGER.info("Redirecting to Home page");
		//Check if the required table is created to run this application.
		AmazonDynamoDBManager.checkIfRequiredTablesExist();
		
		return "Login";
	}
	

	/** Invoked once login is successful.This method would get perform actions such as: get details displayed on
	 *  dash-board & store variables in session.
	 * @param map : ModelMap object, for adding model data objects in the view
	 * @param principal : Principal object - Spring Security object, for retrieving the logged in user's user name from the current session.
	 * @param session : HttpSession object for storing the Cluster and Application List in session
	 * @return : redirects to "home.jsp"
	 */
	@RequestMapping(value = "/loginSuccess")
	public String loginSuccess(ModelMap map, Principal principal,
			HttpSession session) {
		session.setAttribute("userName", principal.getName());
		List<Craiglist> objectsFromDB = new ArrayList<>();
		objectsFromDB = AmazonDynamoDBManager.retrieveData();
		map.addAttribute("Craiglist", new Craiglist());
		map.addAttribute("allItems",objectsFromDB);
		if (objectsFromDB.isEmpty()) {
			map.addAttribute("SearchResult", "No Search Results Found");
		}
		return "Home";

	}
	
}
