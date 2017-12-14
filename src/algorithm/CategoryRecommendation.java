package algorithm;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Movie;
import db.DBConnection;
import db.DBConnectionFactory;
import external.ExternalAPI;
import external.ExternalAPIFactory;

public class CategoryRecommendation {
	
	public List<Movie> recommendMovies(String userId, int zip) {
		DBConnection connection = DBConnectionFactory.getDBConnection();
		//1. According to the current use to get their favorite movie IDs.
		Set<String> favoriteMovieIds = connection.getFavoriteMoviesIds(userId);
		Set<String> favoriteCategories = new HashSet<>();
		//2. Check each movie ID and get their category and insert it into a Set of categories.
		for (String movieId : favoriteMovieIds) {
			favoriteCategories.addAll(connection.getCategories(movieId));
		}
		if(favoriteCategories.isEmpty()) {
			favoriteCategories.add("");
		}
		
		//3. get newest movies from TMSAPI and filter these with categories in next 3 days.
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		Set<Movie> recommendMoives = new HashSet<>();
		ExternalAPI api = ExternalAPIFactory.getExternalAPI();
		List<Movie> movies = api.search(zip, dtf.format(localDate),3,favoriteCategories);
		recommendMoives.addAll(movies);
		return movies;
	}
	
}
