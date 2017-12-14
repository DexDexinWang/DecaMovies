package db.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class MYySQLTableCreation {
	
	/**
	 * Connection Database and create tables 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Create connection instance
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = null;
			try {
				System.out.println("Connecting to: " + MySQLDBUtil.URL);
				connection = DriverManager.getConnection(MySQLDBUtil.URL);
			} catch (SQLException e) {
				System.out.println("SQLException " + e.getMessage());
			}
			
			//1st. connection to Mysql
			if (connection == null) {
				return;
			}
			
			//2nd. If tables exist, then drop them
			Statement smt = connection.createStatement();
			String query= "Drop TABLE IF EXISTS Categories";
			smt.executeUpdate(query);
			query= "Drop TABLE IF EXISTS ShowTime";
			smt.executeUpdate(query);
			query= "Drop TABLE IF EXISTS History";
			smt.executeUpdate(query);
			query= "Drop TABLE IF EXISTS Theaters";
			smt.executeUpdate(query);
			query= "Drop TABLE IF EXISTS Movies";
			smt.executeUpdate(query);
			query= "Drop TABLE IF EXISTS Users";
			smt.executeUpdate(query);
			
			//3rd. Create tables
			query = "CREATE TABLE `Users` (\r\n" + 
					"  `UserID` varchar(255) NOT NULL,\r\n" + 
					"  `PassWord` varchar(255) NOT NULL,\r\n" + 
					"  `FirstName` varchar(255) DEFAULT NULL,\r\n" + 
					"  `LastName` varchar(255) DEFAULT NULL,\r\n" + 
					"  PRIMARY KEY (`UserID`)\r\n" + 
					")";
			smt.executeUpdate(query);
			query = "CREATE TABLE `Movies` (\r\n" + 
					"  `MovieID` varchar(255) NOT NULL,\r\n" + 
					"  `MovieName` varchar(255) DEFAULT NULL,\r\n" + 
					"  `Director` varchar(255) DEFAULT NULL,\r\n" + 
					"  `Actor1` varchar(255) DEFAULT NULL,\r\n" + 
					"  `Actor2` varchar(255) DEFAULT NULL,\r\n" + 
					"  `Actor3` varchar(255) DEFAULT NULL,\r\n" + 
					"  `ReleaseDate` varchar(255)  DEFAULT NULL,\r\n" + 
					"  `Duration` int DEFAULT 0,\r\n" + 
					"  `Rating` double DEFAULT 0.0,\r\n" + 
					"  `OfficialURL` varchar(255) DEFAULT NULL,\r\n" + 
					"  `ImaageURL` varchar(255) DEFAULT NULL,\r\n" + 
					"  `Description` int DEFAULT NULL,\r\n" + 
					"  PRIMARY KEY (`MovieID`)\r\n" + 
					")";
			smt.executeUpdate(query);
			query = "CREATE TABLE `History` (\r\n" + 
					"  `HistoryID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,\r\n" + 
					"  `UserID` varchar(255) NOT NULL,\r\n" + 
					"  `MovieID` varchar(255) NOT NULL,\r\n" + 
					"  `LastFavorTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\r\n" + 
					"  PRIMARY KEY (`HistoryID`),\r\n" + 
					"  KEY `User_Id` (`UserID`),\r\n" + 
					"  KEY `Movie_Id` (`MovieID`),\r\n" + 
					"  CONSTRAINT `HistoryID_MovieID_FK` FOREIGN KEY (`MovieID`) REFERENCES `Movies` (`MovieID`),\r\n" + 
					"  CONSTRAINT `HistoryID_UserID_FK` FOREIGN KEY (`UserID`) REFERENCES `Users` (`UserID`)\r\n" + 
					")";
			smt.executeUpdate(query);
			query = "CREATE TABLE `Theaters` (\r\n" + 
					"  `TheaterID` varchar(255) NOT NULL,\r\n" + 
					"  `TheaterName` varchar(255) DEFAULT NULL,\r\n" + 
					"  PRIMARY KEY (`TheaterID`)\r\n" + 
					")";
			smt.executeUpdate(query);
			query = "CREATE TABLE `ShowTime` (\r\n" + 
					"  `TheaterID` varchar(255) NOT NULL,\r\n" + 
					"  `MovieID` varchar(255) NOT NULL,\r\n" + 
					"  `DateTime` varchar(255) NOT NULL,\r\n" + 
					"  `Links` varchar(255) DEFAULT NULL,\r\n" + 
					"  PRIMARY KEY (`TheaterID`,`MovieID`,`DateTime`),\r\n" + 
					"  CONSTRAINT `HistoryIDUserID_MovieID_FK1` FOREIGN KEY (`MovieID`) REFERENCES `Movies` (`MovieID`),\r\n" + 
					"  CONSTRAINT `HistoryID_TheaterID_FK` FOREIGN KEY (`TheaterID`) REFERENCES `Theaters` (`TheaterID`)\r\n" + 
					")";
			smt.executeUpdate(query);
			query = "CREATE TABLE `Categories` (\r\n" + 
					"  `MovieID` varchar(255) NOT NULL,\r\n" + 
					"  `CategoryName` varchar(255) NOT NULL DEFAULT '',\r\n" + 
					"  PRIMARY KEY (`MovieID`,`CategoryName`),\r\n" + 
					"  CONSTRAINT `category_movie_fk` FOREIGN KEY (`MovieID`) REFERENCES `Movies` (`MovieID`)\r\n" + 
					");";
			smt.executeUpdate(query);
			
			//testing step
			query = "INSERT INTO users VALUES (\"1111\", \"3229c1097c00d497a0fd282d586be050\", \"John\", \"Smith\")";

			System.out.println("Executing query:\n" + query);
			smt.executeUpdate(query);

			
			System.out.println("Import is done successfully.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
