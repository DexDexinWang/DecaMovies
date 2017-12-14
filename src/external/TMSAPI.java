package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Movie;
import data.Movie.MovieBuilder;
import data.TheaterTime;

public class TMSAPI implements ExternalAPI {
	private static final String Protocal = "http://";
	private static final String APIHost = "data.tmsapi.com";
	private static final String SearchPath = "/v1.1/movies/showings";
	private static final String APIKey = "dmdnqahexz7958xa64cqne6f";

	/**
	 * get data from API with latitude and longitude
	 * @param lat
	 * @param lon
	 * @param date
	 * @return
	 */
	public List<Movie> search(double lat, double lon, String date) {
		String url = Protocal + APIHost + SearchPath;
		//URL QUERY Format: startDate=2015-01-21&zip=60611&api_key=1234567890
		String query = String.format("startDate=%s&lat=%s&lng=%s&api_key=%s", date,Double.toString(lat),Double.toString(lon),APIKey);
		url = url + "?" + query;
		//sent wrapping the URL  to searchHelper which will get JSON data.
		System.out.println(url);
		List<Movie> movies= searchHelper(url);
		return movies;
	}
	/**
	 * get data from API with zip
	 */
	@Override
	public List<Movie> search(int zip, String date) {
		String url = Protocal + APIHost + SearchPath;
		//URL QUERY Format: startDate=2015-01-21&zip=60611&api_key=1234567890
		String query = String.format("startDate=%s&zip=%s&api_key=%s", date,zip,APIKey);
		url = url + "?" + query;
		//sent wrapping the URL  to searchHelper which will get JSON data. 
		List<Movie> movies= searchHelper(url);
		return movies;
	}
	/**
	 * to create a connection and to get data from TMSAPI
	 * @param url
	 * @return
	 */
	public List<Movie> searchHelper(String url) {
		try {
			//create a http connection with url
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			//add requested method, "GET"
			connection.setRequestMethod("GET");
			//get response result
			int responseStatus = connection.getResponseCode();
			//testing print
			System.out.println("\nSending 'GET' requrest to URL:" + url);
			System.out.println("Response State:" + responseStatus);
			//read response body to get movies data 
			InputStreamReader streamIn = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferIn = new BufferedReader(streamIn);
			String lineIn;
			StringBuilder response = new StringBuilder();
			while((lineIn = bufferIn.readLine()) != null) {
				response.append(lineIn);
			}
			//convert String as JSONArray and return it back.
			JSONArray movies = new JSONArray(response.toString());
			return getMovieList(movies);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Convert JSONArray to a list of movie object. Will insert it into the database.
	 * @param movies
	 * @return
	 * @throws JSONException
	 */
	private List<Movie> getMovieList(JSONArray movies) throws JSONException {
		List<Movie> movieList = new ArrayList<Movie>();
		for (int i= 0; i < movies.length(); i++) {
			JSONObject movie = movies.getJSONObject(i);
			MovieBuilder builder = new MovieBuilder();
			builder.setMovieId(getStringOrNull(movie, "tmsId"));
			builder.setName(getStringOrNull(movie,"title"));
			builder.setDirector(getFirstDirector(movie));
			builder.setActor1(getActors(movie, 0));
			builder.setActor2(getActors(movie, 1));
			builder.setActor3(getActors(movie, 2));
			builder.setReleaseDate(getStringOrNull(movie,"releaseDate"));
			builder.setDuration(getDuration(movie));
			builder.setRating(getRating(movie));
			builder.setOfficialUrl(getStringOrNull(movie,"officialUrl"));
			builder.setDescription(getStringOrNull(movie,"shortDescription"));
			builder.setImageUrl(getImageUrl(movie));
			builder.setGeners(getArrayObject(movie,"genres"));
			builder.setTheaterTime(getTheaterTime(movie));
			movieList.add(builder.build());
		}
		return movieList;
	}
	
	/**
	 * To get the top 3 actors for the movie {"topCast": ["zero","one","two"]};
	 */
	private String getActors(JSONObject movie,int index) throws JSONException {
		if(!movie.isNull("topCast")) {
			JSONArray array = movie.getJSONArray("topCast");
			if (!array.isNull(index)) {
				return array.getString(index);
			}
		}
		return "";
	}
	
	/**
	 * To get the first director from JSON file {"directors": ["zero",..]}
	 * @param movie
	 * @return
	 * @throws JSONException
	 */ 	
	private String getFirstDirector(JSONObject movie) throws JSONException {
		if(!movie.isNull("directors")) {
			JSONArray array = movie.getJSONArray("directors");
			return array.getString(0);
		}
		return "";
	}
	
	/**
	 * get JSONArray from {"showtimes": [{"theatre"{"id","name"},"dateTime","ticketURI"}]}
	 * @param movie
	 * @return
	 * @throws JSONException
	 */
	private Set<TheaterTime> getTheaterTime (JSONObject movie) throws JSONException {
		Set<TheaterTime> theaterTimes = new HashSet<>();
		if (!movie.isNull("showtimes")) {
			JSONArray array = movie.getJSONArray("showtimes");
			for (int i = 0 ; i < array.length() ; i++) {
				String id="", name="", date="", link="";
				JSONObject obj = array.getJSONObject(i);
				if (!obj.isNull("theatre")) {
					JSONObject innerObj = obj.getJSONObject("theatre");
					if (!innerObj.isNull("id")) {
						id = innerObj.getString("id");
					}
					if (!innerObj.isNull("name")) {
						name = innerObj.getString("name");
					}
				}
				if(!obj.isNull("dateTime")) {
					date = obj.getString("dateTime");
				}
				if(!obj.isNull("ticketURI")) {
					link = obj.getString("ticketURI");
				}
				theaterTimes.add(new TheaterTime(id,name,date,link));
			}
			return theaterTimes;
		}
		return theaterTimes;
	}
	/**
	 * get JSONArray from {"field": ["values"]}
	 * @param movie
	 * @param field
	 * @return
	 * @throws JSONException
	 */
	private Set<String> getArrayObject(JSONObject movie, String field) throws JSONException {
		Set<String> values = new HashSet<>();
		if(!movie.isNull(field)) {
			JSONArray array = movie.getJSONArray(field);
			for (int j = 0; j < array.length(); j++) {
				values.add(array.getString(j));
			}
			return values;
		}
		return values;
	} 
	
	/**
	 * get ImageURL from {"preferredImage": {"uri": "assets/p10673530_p_v5_aa.jpg"}}
	 * @param movie
	 * @return
	 * @throws JSONException
	 */
	private String getImageUrl(JSONObject movie) throws JSONException {
		if (!movie.isNull("preferredImage")) {
			JSONObject pi = movie.getJSONObject("preferredImage");
			if(!pi.isNull("uri")) {
				return pi.getString("uri");
			}
		}
		return "";
	}
	
	/**
	 * get rating from {"qualityRating": {"value": "4"}}
	 * @param movie
	 * @return
	 * @throws JSONException
	 */
	private double getRating(JSONObject movie) throws JSONException {
		if (!movie.isNull("qualityRating")) {
			JSONObject qr = movie.getJSONObject("qualityRating");
			if(!qr.isNull("value")) {
				return qr.getDouble("value");
			}
		}
		return 0.0;
	}
	
	/**
	 * get a string value from the JSON object by comparing the filed 
	 * @param movie
	 * @param field
	 * @return
	 * @throws JSONException
	 */
	private String getStringOrNull(JSONObject movie, String field) throws JSONException {
		return movie.isNull(field) ? null : movie.getString(field);
	}
	
	/**
	 * get an int value for movie duration from the JSON object by comparing the filed
	 * @param movie
	 * @return
	 * @throws JSONException
	 */
	private int getDuration(JSONObject movie) throws JSONException {
		if (!movie.isNull("runTime")) {
			String str = movie.getString("runTime");
			String hours = str.substring(2, 4);
			String mins = str.substring(5, 7);
			int res = Integer.valueOf(hours) * 60 + Integer.valueOf(mins);
			return res;
		}
		return 0;
	}
	
	
	//testing function with latitude, longitude, and date
//	private void testGeoAPI(double lat, double lon, String date) {
//		List<Movie> movies = search(lat,lon,date);
//		testPrinter(movies);
//	}
	/**
	 * testing function with zip code and date
	 * @param zip
	 * @param date
	 */
	private void testGeoAPI(int zip, String date) {
		List<Movie> movies = search(zip, date);
		testPrinter(movies);
	}	
	
	/**
	 * testing function with movies
	 * @param movies
	 */
	private void testPrinter(List<Movie> movies) {
		try {
			for (Movie movie: movies) {
				JSONObject printObject = movie.toJSONObject();
				System.out.println(printObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Search the recent 3 days` movies bases on the categories
	 */
	@Override
	public List<Movie> search(int zip, String date, int day, Set<String> key) {
		String url = Protocal + APIHost + SearchPath;
		//URL QUERY Format: startDate=2015-01-21&zip=60611&api_key=1234567890
		String query = String.format("startDate=%s&numDays=%s&zip=%s&api_key=%s", date,day,zip,APIKey);
		url = url + "?" + query;
		//sent wrapping the URL  to searchHelper which will get JSON data.
		System.out.println(url);
		List<Movie> movies = searchHelper(url);
		List<Movie> resMovies = new ArrayList<>();
		for (Movie movie: movies) {
			Set<String> categories = movie.getGeners();
			categories.retainAll(key);
			if (categories.size()!=0) {
				resMovies.add(movie);
			}
		}
		return resMovies;
	}
	
	/**
	 * main function to test
	 * @param args
	 */
	public static void main(String[] args) {
		TMSAPI tms = new TMSAPI();
		//tms.testGeoAPI(90025,"2017-12-13");
		//tms.testGeoAPI(37.38,-122.08,"2017-12-11");
		Set<String> test= new HashSet<String>();
		List<Movie> res = tms.search(90025, "2017-12-13", 3, test);
		System.out.println(res.size());
	}

	
}
