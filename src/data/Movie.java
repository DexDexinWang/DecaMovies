package data;

import java.util.Iterator;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
	private String movieId;
	private String name;
	private String director;	
	private String actor1;
	private String actor2;
	private String actor3;
	private String releaseDate;
	private int duration;
	private double rating;
	private String officialUrl;
	private String imageUrl;
	private String description;
	private Set<String> geners;
	private Set<TheaterTime> theaterTime;
	
	/**
	 * It is the private constructor to create an item with flexible parameters.   
	 * @param builder
	 */
	private Movie(MovieBuilder builder) {
		this.movieId = builder.movieId;
		this.name = builder.name;
		this.director = builder.director;
		this.actor1 = builder.actor1;
		this.actor2 = builder.actor2;
		this.actor3 = builder.actor3;
		this.releaseDate = builder.releaseDate;
		this.duration = builder.duration;
		this.rating = builder.rating;
		this.officialUrl = builder.officialUrl;
		this.imageUrl = builder.imageUrl;
		this.description = builder.description;
		this.geners = builder.geners;
		this.theaterTime = builder.theaterTime;
	}
	
	//Initialize a new construction to save value as sub-objects.
	public String getMovieId() {
		return movieId;
	}
	public String getName() {
		return name;
	}
	public String getDirector() {
		return director;
	}
	public String getActor1() {
		return actor1;
	}
	public String getActor2() {
		return actor2;
	}
	public String getActor3() {
		return actor3;
	}
	public String getReleaseDate() {
		return releaseDate;
	}

	public int getDuration() {
		return duration;
	}
	public double getRating() {
		return rating;
	}
	public String getOfficialUrl() {
		return officialUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getDescription() {
		return description;
	}
	public Set<String> getGeners() {
		return geners;
	}
	public Set<TheaterTime> getTheaterTime() {
		return theaterTime;
	}
	
	/**
	 * convert data as JSON object for front-end;
	 * @return JSONObject
	 */
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("movie_id", movieId);
			obj.put("name", name);
			obj.put("director", director);
			obj.put("actor1", actor1);
			obj.put("actor2", actor2);
			obj.put("actor3", actor3);
			obj.put("release", releaseDate);
			obj.put("duration", duration);
			obj.put("rating", rating);
			obj.put("officialUrl", officialUrl);
			obj.put("imageUrl", imageUrl);
			obj.put("description", description);
			obj.put("geners", new JSONArray(geners));
			//insert a JSONArray of JSONArray
			if (theaterTime != null ) { 
				JSONArray theatreTimeArray = new JSONArray();
				Iterator<TheaterTime> iter = theaterTime.iterator();
				while (iter.hasNext()) {
					theatreTimeArray.put(iter.next().toJSONObject());
				}
				obj.put("theatreTime",theatreTimeArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	
	/**
	 * MovieBuilder is to help Movie class to create an instance with flexible parameters 
	 */
	public static class MovieBuilder{
		private String movieId;
		private String name;
		private String director;
		private String actor1;
		private String actor2;
		private String actor3;
		private String releaseDate;
		private int duration;
		private double rating;
		private String officialUrl;
		private String imageUrl;
		private String description;
		private Set<String> geners;
		private Set<TheaterTime> theaterTime;
		
		public MovieBuilder setMovieId(String movieId) {
			this.movieId = movieId;
			return this;
		}
		public MovieBuilder setName(String name) {
			this.name = name;
			return this;
		}
		public MovieBuilder setDirector(String director) {
			this.director = director;
			return this;
		}
		public MovieBuilder setActor1(String actor1) {
			this.actor1 = actor1;
			return this;
		}
		public MovieBuilder setActor2(String actor2) {
			this.actor2 = actor2;
			return this;
		}
		public MovieBuilder setActor3(String actor3) {
			this.actor3 = actor3;
			return this;
		}
		public MovieBuilder setReleaseDate(String releaseDate) {
			this.releaseDate = releaseDate;
			return this;
		}
		public MovieBuilder setDuration(int duration) {
			this.duration = duration;
			return this;
		}
		public MovieBuilder setRating(double rating) {
			this.rating = rating;
			return this;
		}
		public MovieBuilder setOfficialUrl(String officialUrl) {
			this.officialUrl = officialUrl;
			return this;
		}
		public MovieBuilder setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}
		public MovieBuilder setDescription(String description) {
			this.description = description;
			return this;
		}
		public MovieBuilder setGeners(Set<String> geners) {
			this.geners = geners;
			return this;
		}
		public MovieBuilder setTheaterTime(Set<TheaterTime> theaterTime) {
			this.theaterTime = theaterTime;
			return this;
		}
		/**
		 * Builder will build an dynamic Movie instance with dynamic parameters.
		 * @return
		 */
		public Movie build() {
			return new Movie(this);
		}
	}
}
