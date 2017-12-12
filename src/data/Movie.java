package data;

import java.util.Iterator;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
	private String movieId;
	private String name;
	private String releaseDate;
	private Set<String> director;
	private int duration;
	private double rating;
	private String officialUrl;
	private String imageUrl;
	private String description;
	private Set<String> geners;
	private Set<String> casts;
	private Set<TheatreTime> theatreTime;
	
	//It is the private constructor to create an item with flexible parameters.   
	private Movie(MovieBuilder builder) {
		this.movieId = builder.movieId;
		this.name = builder.name;
		this.releaseDate = builder.releaseDate;
		this.director = builder.director;
		this.duration = builder.duration;
		this.rating = builder.rating;
		this.officialUrl = builder.officialUrl;
		this.imageUrl = builder.imageUrl;
		this.description = builder.description;
		this.geners = builder.geners;
		this.casts = builder.casts;
		this.theatreTime = builder.theatreTime;
	}

	public String getMovieId() {
		return movieId;
	}
	public String getName() {
		return name;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public Set<String> getDirector() {
		return director;
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
	public Set<String> getCasts() {
		return casts;
	}
	public Set<TheatreTime> getTheatreTime() {
		return theatreTime;
	}
	
	//convert array list data as JSON object for frontend;
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("movie_id", movieId);
			obj.put("name", name);
			obj.put("release", releaseDate);
			obj.put("director", new JSONArray(director));
			obj.put("duration", duration);
			obj.put("rating", rating);
			obj.put("officialUrl", officialUrl);
			obj.put("imageUrl", imageUrl);
			obj.put("geners", new JSONArray(geners));
			obj.put("cast", new JSONArray(casts));
			obj.put("description", description);
			//insert a JSONArray of JSONArray
			JSONArray theatreTimeArray = new JSONArray();
			Iterator<TheatreTime> iter = theatreTime.iterator();
			while (iter.hasNext()) {
				theatreTimeArray.put(iter.next().toJSONArray());
			}
			obj.put("theatreTime",theatreTimeArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	//Initialize a new construction to save value as sub-objects.
	
	
	//MovieBuilder is to help Movie class to create an instance with flexible parameters 
	public static class MovieBuilder{
		private String movieId;
		private String name;
		private String releaseDate;
		private Set<String> director;
		private int duration;
		private double rating;
		private String officialUrl;
		private String imageUrl;
		private String description;
		private Set<String> geners;
		private Set<String> casts;
		private Set<TheatreTime> theatreTime;
		
		public MovieBuilder setMovieId(String movieId) {
			this.movieId = movieId;
			return this;
		}
		public MovieBuilder setName(String name) {
			this.name = name;
			return this;
		}
		public MovieBuilder setReleaseDate(String releaseDate) {
			this.releaseDate = releaseDate;
			return this;
		}
		
		public MovieBuilder setDirector(Set<String> director) {
			this.director = director;
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

		public MovieBuilder setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}
		
		public MovieBuilder setOfficialUrl(String officialUrl) {
			this.officialUrl = officialUrl;
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
		
		public MovieBuilder setCasts(Set<String> casts) {
			this.casts = casts;
			return this;
		}
		
		public MovieBuilder setTheatreTime(Set<TheatreTime> theatreTime) {
			this.theatreTime = theatreTime;
			return this;
		}
		//
		public Movie build() {
			return new Movie(this);
		}
	}
}
