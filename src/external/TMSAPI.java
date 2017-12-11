package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class TMSAPI {
	private static final String Protocal = "http://";
	private static final String APIHost = "data.tmsapi.com";
	private static final String SearchPath = "/v1.1/movies/showings";
	private static final String APIKey = "dmdnqahexz7958xa64cqne6f";

	//get data from API with latitude and longitude
	public JSONArray search(double lat, double lon, String date) {
		String url = Protocal + APIHost + SearchPath;
		//URL QUERY Format: startDate=2015-01-21&zip=60611&api_key=1234567890
		String query = String.format("startDate=%s&lat=%s&lng=%s&api_key=%s", date,Double.toString(lat),Double.toString(lon),APIKey);
		url = url + "?" + query;
		//sent wrapping the URL  to searchHelper which will get JSON data.
		System.out.println(url);
		JSONArray movies= searchHelper(url);
		return movies;
	}
	//get data from API with zip
	public JSONArray search(int zip, String date) {
		String url = Protocal + APIHost + SearchPath;
		//URL QUERY Format: startDate=2015-01-21&zip=60611&api_key=1234567890
		String query = String.format("startDate=%s&zip=%s&api_key=%s", date,zip,APIKey);
		url = url + "?" + query;
		//sent wrapping the URL  to searchHelper which will get JSON data. 
		JSONArray movies= searchHelper(url);
		return movies;
	}
	//to create a connection and to get data from TMSAPI
	public JSONArray searchHelper(String url) {
		try {
			//create Http connection with url
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
			return movies;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	//testing function with latitude, longitude, and date
	private void testGeoAPI(double lat, double lon, String date) {
		JSONArray movies = search(lat,lon,date);
		testPrinter(movies);
	}
	//testing function with zip code and date
	private void testGeoAPI(int zip, String date) {
		JSONArray movies = search(zip, date);
		testPrinter(movies);
	}	
	
	//testing function with movies
	private void testPrinter(JSONArray movies) {
		try {
			for ( int i = 0; i< movies.length(); i++) {
				JSONObject movie = movies.getJSONObject(i);
				System.out.println(movie);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//main function to test
	public static void main(String[] args) {
		TMSAPI tms = new TMSAPI();
		tms.testGeoAPI(90025,"2017-12-11");
		tms.testGeoAPI(37.38,-122.08,"2017-12-11");
	}
	
}
