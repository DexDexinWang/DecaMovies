package db;

import java.util.List;
import java.util.Set;

import data.Movie;

public interface DBConnection {
	
	/**
	 * Close the connection
	 */
	public void close();
	/**
	 * For History
	 * @param userId
	 * @param movieIds
	 */
	public void setFavoriteMovies(String userId, List<String> movieIds);
	/**
	 * For History
	 * @param userId
	 * @param itemIds
	 */
	public void unsetFavoriteMovies(String userId, List<String> movieIds);
	/**
	 * For History
	 * @param userId
	 * @return
	 */
	public Set<String> getFavoriteMoviesIds(String userId);
	/**
	 * For History
	 * @param userId
	 * @return
	 */
	public Set<Movie> getFavoriteMovies(String userId);
	/**
	 * For History
	 * @param movieId
	 * @return
	 */
	public Set<String> getCategories(String movieId);
	/**
	 * For History
	 * @param userId
	 * @param date
	 * @param zip
	 * @return
	 */
	public List<Movie> searchMovies(int zip, String date);
	/**
	 * After the search action, all data will stored into database.
	 * @param movie
	 */
	public void saveItem(Movie movie);
	/**
	 * Show user`s name to website
	 * @param userId
	 * @return
	 */
	public String getFullname(String userId);
	/**
	 * confirm login information.
	 * @param userId
	 * @param password
	 * @return
	 */
	public boolean verifyLogin(String userId, String password);
}
