package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Movie;
import data.Movie.MovieBuilder;
import data.TheaterTime;
import db.DBConnection;
import external.ExternalAPI;
import external.ExternalAPIFactory;

public class MySQLConnection implements DBConnection{
	//connection will be used for all methods
	private Connection connection;
	/**
	 * initialize connection 
	 */
	public MySQLConnection() {
		try {
			// Create connection instance
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(MySQLDBUtil.URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Will close the connection at the end of interaction.
	 */
	@Override
	public void close() {
		if(connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Insert favorite movies and an userId into history table;
	 */
	@Override
	public void setFavoriteMovies(String userId, List<String> movieIds) {
		if (connection == null) {
			return ;
		}
		String query = "INSERT INTO History(userID, MovieID) VALUES(?,?)";
		try {
			PreparedStatement smt= connection.prepareStatement(query);
			for (String movieId : movieIds) {
				smt.setString(1, userId);
				smt.setString(2, movieId);
				smt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete existing favorite movies and and userId from history table;
	 */
	@Override
	public void unsetFavoriteMovies(String userId, List<String> movieIds) {
		if (connection == null) {
			return ;
		}
		String query = "DELETE FROM History WHERE userID =? and MovieID =?";
		try {
			PreparedStatement smt= connection.prepareStatement(query);
			for (String movieId : movieIds) {
				smt.setString(1, userId);
				smt.setString(2, movieId);
				smt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Get Movie IDs from history with USER ID as input
	 */
	@Override
	public Set<String> getFavoriteMoviesIds(String userId) {
		Set<String> MovieIds = new HashSet<String>();
		if (connection == null) {
			return MovieIds;
		}
		try {
			String query = "SELECT MovieID FROM History WHERE UserID=?";
			PreparedStatement smt = connection.prepareStatement(query);
			smt.setString(1, userId);
			ResultSet res = smt.executeQuery();
			while (res.next()) {
				MovieIds.add(res.getString("MovieID"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return MovieIds;
	}

	@Override
	public Set<Movie> getFavoriteMovies(String userId) {
		Set<Movie> Movies = new HashSet<>();
		if (connection == null) {
			return Movies;
		}
		try {
			Set<String> movieIds = getFavoriteMoviesIds(userId);
			for (String movieId : movieIds) {
				String query = "SELECT * FROM Movies WHERE MovieID =?";
				PreparedStatement smt = connection.prepareStatement(query);
				smt.setString(1, movieId);
				ResultSet res = smt.executeQuery();
				//create MovieBuilder and add it to set.
				MovieBuilder builder =new MovieBuilder();
				if(res.next()) {
					builder.setMovieId(res.getString("MovieID"));
					builder.setName(res.getString("MovieName"));
					builder.setDirector(res.getString("Director"));
					builder.setActor1(res.getString("Actor1"));
					builder.setActor2(res.getString("Actor2"));
					builder.setActor3(res.getString("Actor3"));
					builder.setReleaseDate(res.getString("ReleaseDate"));
					builder.setDuration(res.getInt("Duration"));
					builder.setRating(res.getDouble("Rating"));
					builder.setOfficialUrl(res.getString("OfficialURL"));
					builder.setImageUrl(res.getString("ImaageURL"));
					builder.setDescription(res.getString("Description"));
				}
				Set<String> categories = getCategories(movieId);
				builder.setGeners(categories);
				Movies.add(builder.build());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Movies;
	}

	/**
	 * Give MovieID and get category names from Category table.
	 */
	@Override
	public Set<String> getCategories(String movieId) {
		Set<String> categories = new HashSet<>();
		if (connection == null) {
			return categories;
		}
		try {
			String query = "SELECT CategoryName FROM Categories WHERE MovieID=?";
			PreparedStatement smt = connection.prepareStatement(query);
			smt.setString(1, movieId);
			ResultSet res = smt.executeQuery();
			if (res.next()) {
				categories.add(res.getString("CategoryName"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}
	
	/**
	 * search movies information and save it into database 
	 */
	@Override
	public List<Movie> searchMovies(int zip,String date) {
//		long a = System.currentTimeMillis();
		ExternalAPI api = ExternalAPIFactory.getExternalAPI();
		List<Movie> movies = api.search(zip, date);
//		long b = System.currentTimeMillis();
//		System.out.println("Parse JSON: " + (b-a) + "ms");
		//save each movie`s infomration into databsae.
		for (Movie movie : movies) {
			saveItem(movie);
		}
//		long c = System.currentTimeMillis();
//		System.out.println("Insert into DB: " + (c-b) + "ms");
		return movies;
	}
	/**
	 * Save each movie`s information into 6 tables.
	 */
	@Override
	public void saveItem(Movie movie) {
		if (connection == null) {
			return ;
		}
		try {
//			long a = System.currentTimeMillis();
			//1 for "Movies" table
			String query = "INSERT IGNORE INTO Movies VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement smt = connection.prepareStatement(query);
			smt.setString(1, movie.getMovieId());
			smt.setString(2, movie.getName());
			smt.setString(3, movie.getDirector());
			smt.setString(4, movie.getActor1());
			smt.setString(5, movie.getActor2());
			smt.setString(6, movie.getActor3());
			smt.setString(7, movie.getReleaseDate());
			smt.setInt(8, movie.getDuration());
			smt.setDouble(9, movie.getRating());
			smt.setString(10, movie.getOfficialUrl());
			smt.setString(11, movie.getImageUrl());
			smt.setString(12, movie.getDescription());
			smt.execute();
//			long b = System.currentTimeMillis();
//			System.out.println("movies: "+ (b-a) + "ms");
			//2 for "Theaters" table 
			query = "INSERT IGNORE INTO Theaters VALUES (?,?)";
			for (TheaterTime tt: movie.getTheaterTime()) {
				smt = connection.prepareStatement(query);
				smt.setString(1, tt.getTheaterId());
				smt.setString(2, tt.getTheater());
				smt.execute();
			}
//			long c = System.currentTimeMillis();
//			System.out.println("Theaters: "+ (b-b) + "ms");
			//3 for "ShowTime" table
			query = "INSERT IGNORE INTO ShowTime VALUES (?,?,?,?)";
			for (TheaterTime tt: movie.getTheaterTime()) {
				smt = connection.prepareStatement(query);
				smt.setString(1, tt.getTheaterId());
				smt.setString(2, movie.getMovieId());
				smt.setString(3, tt.getTime());
				smt.setString(4, tt.getTicketUrl());
				smt.execute();
			}
//			long d = System.currentTimeMillis();
//			System.out.println("ShowTime: "+ (d-c) + "ms");
			//4 for "Categories" table 
			query = "INSERT IGNORE INTO Categories VALUES (?,?)";
			for (String categor: movie.getGeners()) {
				smt = connection.prepareStatement(query);
				smt.setString(1, movie.getMovieId());
				smt.setString(2, categor);
				smt.execute();
			}
//			long e = System.currentTimeMillis();
//			System.out.println("Category: "+ (e-d) + "ms");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * get full name from database and show it in front-end
	 */
	@Override
	public String getFullname(String userId) {
		String name="";
		if (connection == null) {
			return name;
		}
		try {
			String query = "SELECT FirstName + ' ' + LastName FROM Users WHERE UserID =?";
			PreparedStatement smt = connection.prepareStatement(query);
			smt.setString(1, userId);
			ResultSet res = smt.executeQuery();
			if (res.next()) {
				name =res.getString("FirstName") + " " + res.getString("LastName");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
	/**
	 * check login information return true or flase;
	 */
	@Override
	public boolean verifyLogin(String userId, String password) {
		if (connection == null) {
			return false;
		}
		try {
			String query = "SELECT UserID FROM Users WHERE UserID =? and PassWord =?";
			PreparedStatement smt = connection.prepareStatement(query);
			smt.setString(1, userId);
			smt.setString(2, password);
			ResultSet res = smt.executeQuery();
			if (res.next()) {
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args) {
		MySQLConnection test = new MySQLConnection();
		//test.searchMovies(90025, "2017-12-13");
		Set<Movie> res = test.getFavoriteMovies("1111");
		System.out.println(res);
	}

}
