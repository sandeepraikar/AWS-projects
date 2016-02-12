package edu.uta.cse.aws.rds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uta.cse.util.*;
import edu.uta.cse.util.Constants;

/**
 * @author Sandeep Raikar
 *
 */
public class ConnectRDS {

	private static Logger LOGGER = LoggerFactory.getLogger(ConnectRDS.class);

	public static void main(String[] args) {
		try {
			
			LOGGER.info("Loading the CSV file.");
			LOGGER.info("Importing the CSV data dump to Target Table in Amazon RDS");
			long startTime = System.currentTimeMillis();
			CSVLoader loader = new CSVLoader(DBConnection.getCon());
			loader.loadCSV(Constants.DATA_DUMP_LOCATION,
					Constants.RDS_DB_INSTANCE_TARGET_TABLE, true);
			long endTime = System.currentTimeMillis();
			LOGGER.info("Total Time taken in milli seconds: "
				+ (endTime - startTime));
			LOGGER.info("Data Dump imported successfully to Amazon RDS");
		} catch (Exception e) {
			LOGGER.error("Exception occured"+e.getMessage());
			e.printStackTrace();
		}
	}
}
