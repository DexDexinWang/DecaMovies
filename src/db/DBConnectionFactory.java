package db;

import db.mysql.MySQLConnection;

public class DBConnectionFactory {
	private static final String DEFAULT_DB = "mysql";
	/**
	 * get DBConnection in Factory Model
	 * @return
	 */
	public static DBConnection getDBConnection() {
		return getDBConnection(DEFAULT_DB);
	}
	
	/**
	 * create different APIs based on the Source name 
	 * @param name
	 * @return
	 */
	public static DBConnection getDBConnection(String db) {
		switch(db) {
		case "mysql":
			return new MySQLConnection();
		case "Mongodb":
			return null;
		case "OMDB":
			return null;
		default:
			throw new IllegalArgumentException("invailid database name" + db);
		}
	}
}
