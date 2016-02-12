package edu.uta.cse.aws.rds;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uta.cse.util.DBConnection;

/**
 * @author Sandeep
 *
 */
public class RunTests {

	private static Logger LOGGER = LoggerFactory.getLogger(ConnectRDS.class);

	public static void main(String[] args) {
		retrieveMagnitudeTimeRelation(DBConnection.getCon());
		randomQueries(DBConnection.getCon());
		limitedScopeQueries(DBConnection.getCon());
	}

	private static void limitedScopeQueries(Connection con) {
		Statement stmt = null;
		String sql= null;
		ResultSet rs= null;
		long startTime,endTime;
		try {
			Random rn= new Random();
			stmt = con.createStatement();
			sql= "select count(*) from equake ";
			rs = stmt.executeQuery(sql);
			rs.next();
			int count= rs.getInt(1);
			// Generating 2000 random queries
			startTime = System.currentTimeMillis();
			for (int i = 0; i <= 2000; i++) {
				
				//LOGGER.info("Executing query : "+i);
				int randomInt = rn.nextInt(count);
				sql = "SELECT * from equake Limit "+randomInt+"" ;
				rs = stmt.executeQuery(sql);
			}
			endTime = System.currentTimeMillis();
			LOGGER.info("Total Time taken in milli seconds: "+ (endTime - startTime));
			
		} catch (Exception e) {
			LOGGER.info("Exception occured"+e);
			e.printStackTrace();
		}

	}

	private static void randomQueries(Connection con) {
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		long startTime, endTime;

		try {
			startTime = System.currentTimeMillis();
			stmt = con.createStatement();
			sql = "select latitude from sample5 order by rand()";
			for (int i = 0; i <= 2000; i++) {
				rs = stmt.executeQuery(sql);
			}
			endTime = System.currentTimeMillis();

			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void retrieveMagnitudeTimeRelation(Connection con) {

		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		long startTime, endTime;
		// For Magnitude 2
		try {
			stmt = con.createStatement();

			// Average earthquake magnitude between '2015-02-12' and
			// '2015-02-16'
			sql = "select avg(mag) from equake where time between '2015-02-12' and '2015-02-16'";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Average Earthquake magnitude for the Date range between 2015-02-12 and 2015-02-16 -> "
					+ rs.getFloat(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			// Maximum earthquake magnitude between '2015-02-12' and
			// '2015-02-16'
			sql = "select max(mag) from equake where time between '2015-02-12' and '2015-02-16'";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Maximum Earthquake magnitude for the Date range between 2015-02-12 and 2015-02-16 -> "
					+ rs.getFloat(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			// Minimum earthquake magnitude between '2015-02-12' and
			// '2015-02-16'
			sql = "select min(mag) from equake where time between '2015-02-12' and '2015-02-16'";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Minimum Earthquake magnitude for the Date range between 2015-02-12 and 2015-02-16 ->"
					+ rs.getFloat(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			sql = "select count(*) from equake where mag<=1 and type='earthquake'";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Total no. of earthquake instances, with magnitude <= 1 ->  "
					+ rs.getInt(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			sql = "select count(*) from equake where mag>=1 and mag<=2 and type='earthquake'";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Total no. of earthquake instances, with magnitude ranging between 1&2 ->  "
					+ rs.getInt(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			sql = "select count(*) from equake where mag>=2 and mag<=3 and type='earthquake'";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Total no. of earthquake instances, with magnitude ranging between 2&3 ->  "
					+ rs.getInt(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			sql = "select count(*) from equake where mag>=3 and mag<=4 and type='earthquake'";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Total no. of earthquake instances, with magnitude ranging between 3&4 ->  "
					+ rs.getInt(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			sql = "select count(*) from equake where mag>=4 and mag<=5 and type='earthquake'";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Total no. of earthquake instances, with magnitude ranging between 4&5 ->  "
					+ rs.getInt(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			sql = "select count(*) from equake where mag>=5 and mag<=6 and type='earthquake'";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Total no. of earthquake instances, with magnitude ranging between 5&6 ->  "
					+ rs.getInt(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			sql = "select count(*) from equake where mag>=6 and type='earthquake' ";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Total no. of earthquake instances, with magnitude >= 6 ->  "
					+ rs.getInt(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

			sql = "select count(*) from equake where mag>=6 and type='earthquake' ";
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			rs.next();
			endTime = System.currentTimeMillis();
			LOGGER.info("Total no. of earthquake instances, with magnitude >= 6 ->  "
					+ rs.getInt(1));
			LOGGER.info("Total Time taken in milli seconds: "
					+ (endTime - startTime));

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
