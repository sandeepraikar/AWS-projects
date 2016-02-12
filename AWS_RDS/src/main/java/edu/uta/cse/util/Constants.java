package edu.uta.cse.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @author Sandeep_Raikar
 * 
 */
public class Constants {

	public static final String DATA_DUMP_LOCATION;
	public static final String RDS_INSTANCE_CONNECTION_URL;
	public static final String RDS_INSTANCE_DATABASE_NAME;
	public static final String RDS_DB_INSTANCE_USERNAME;
	public static final String RDS_DB_INSTANCE_PASSWORD;
	public static final String RDS_DB_INSTANCE_TARGET_TABLE;
	public static final String JDBC_DRIVER;

	static {

		Configuration config = null;
		try {
			config = new PropertiesConfiguration(
					"src/main/resources/config.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

		DATA_DUMP_LOCATION = config.getString("data.dump.location");
		RDS_INSTANCE_CONNECTION_URL = config
				.getString("aws.rds.connection.url");
		JDBC_DRIVER = config.getString("jdbc.driver");
		RDS_INSTANCE_DATABASE_NAME = config
				.getString("aws.rds.instance.db.name");
		RDS_DB_INSTANCE_USERNAME = config
				.getString("aws.rds.instance.username");
		RDS_DB_INSTANCE_PASSWORD = config
				.getString("aws.rds.instance.password");
		RDS_DB_INSTANCE_TARGET_TABLE = config
				.getString("aws.rds.instance.target.table.name");

	}
}
