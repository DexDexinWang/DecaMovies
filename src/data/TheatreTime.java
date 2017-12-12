package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TheatreTime 
{
	String theatreId;
	String theatre;
	String time;
	String ticketUrl;
	public TheatreTime (String theatreId, String theatre, String time, String ticketUrl) {
		this.theatreId = theatreId;
		this.theatre = theatre;
		this.time = time;
		this.ticketUrl = ticketUrl;
	}
	
	public JSONArray toJSONArray() {
		JSONArray out = new JSONArray();
		JSONObject subObj = new JSONObject();
		try {
			subObj.put("theatreId", theatreId);
			subObj.put("theatre", theatre);
			subObj.put("time", time);
			subObj.put("ticketUrl", ticketUrl);
			out.put(subObj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return out;
	}
}
