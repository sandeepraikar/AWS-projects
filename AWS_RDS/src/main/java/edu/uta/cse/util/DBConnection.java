package edu.uta.cse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sandeep
 *
 */
public class DBConnection {

	private static Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);

	public static Connection getCon() {
		Connection connection = null;
		try {
			LOGGER.info("Connecting to Amazon RDS....");
			Class.forName(Constants.JDBC_DRIVER);
			connection = DriverManager.getConnection(
					Constants.RDS_INSTANCE_CONNECTION_URL,
					Constants.RDS_DB_INSTANCE_USERNAME,
					Constants.RDS_DB_INSTANCE_PASSWORD);

			LOGGER.info("Connection test: " + connection.getCatalog());

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

}
