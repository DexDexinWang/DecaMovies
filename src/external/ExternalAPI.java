package external;

import java.util.List;
import java.util.Set;

import data.Movie;

public interface ExternalAPI {
	/**
	 * Create an Interface for Factory Model
	 * @param zip
	 * @param date
	 * @return
	 */
	public List<Movie> search(int zip, String date);
	public List<Movie> search(int zip, String date, int day, Set<String> key);
}
