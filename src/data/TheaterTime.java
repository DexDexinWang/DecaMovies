package data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Make a sub JSON Object to store one-many relational data
 */
public class TheaterTime 
{
	String theaterId;
	String theater;
	String time;
	String ticketUrl;
	public TheaterTime (String theaterId, String theater, String time, String ticketUrl) {
		this.theaterId = theaterId;
		this.theater = theater;
		this.time = time;
		this.ticketUrl = ticketUrl;
	}
	public String getTheaterId() {
		return theaterId;
	}
	public String getTheater() {
		return theater;
	}
	public String getTime() {
		return time;
	}
	public String getTicketUrl() {
		return ticketUrl;
	}
	/**
	 * Transfer the data format into JSON Object
	 * @return
	 */
	public JSONObject toJSONObject() {
		JSONObject subObj = new JSONObject();
		try {
			subObj.put("theatreId", theaterId);
			subObj.put("theatre", theater);
			subObj.put("time", time);
			subObj.put("ticketUrl", ticketUrl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return subObj;
	}
}
