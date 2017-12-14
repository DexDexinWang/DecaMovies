package db.mysql;

/**
 * Define some parameters to make MySQL connection 
 */
public class MySQLDBUtil {
	 private static final String DB_LAB = "jdbc:mysql://";
	 private static final String HOSTNAME = "localhost";
	 private static final String PORT_NUM = "3307"; // change it to your mysql port number
	 public static final String DB_NAME = "DecaMovies";
	 private static final String USERNAME = "root";
	 private static final String PASSWORD = "root";
	 public static final String URL = DB_LAB + HOSTNAME + ":" + PORT_NUM + "/" + DB_NAME
	     + "?user=" + USERNAME + "&password=" + PASSWORD + "&autoreconnect=true";
}
