package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Movie;
import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class MovieHistory
 */
@WebServlet("/history")
public class MovieHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userId = request.getParameter("user_id");
		JSONArray array = new JSONArray();
		DBConnection connection = DBConnectionFactory.getDBConnection();
		Set<Movie> movies = connection.getFavoriteMovies(userId);
		for (Movie item : movies) {
			JSONObject obj = item.toJSONObject();
			try {
				obj.append("favorite", true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(obj);
		}
		RpcHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//get data from the body of the request 
			JSONObject bodyJson = RpcHelper.readJsonObject(request);
			String userId = bodyJson.getString("user_id");
			JSONArray favorites = (JSONArray) bodyJson.get("favorite");
			//convert JSON array as list of String.
			List<String> movieIds = new ArrayList<>();
			for (int i =0; i < favorites.length(); i++) {
				String movieId = favorites.getString(i);
				movieIds.add(movieId);
			}

			//connection DB and delete data from history table
			DBConnection connection = DBConnectionFactory.getDBConnection();
			connection.setFavoriteMovies(userId, movieIds);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result","SUCCESS"));
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//get data from the body of the request 
			JSONObject bodyJson = RpcHelper.readJsonObject(request);
			String userId = bodyJson.getString("user_id");
			JSONArray favorites = (JSONArray) bodyJson.get("favorite");
			//convert JSON array as list of String.
			List<String> movieIds = new ArrayList<>();
			for (int i =0; i < favorites.length(); i++) {
				String movieId = favorites.getString(i);
				movieIds.add(movieId);
			}
			//connection DB and delete data from history table
			DBConnection connection = DBConnectionFactory.getDBConnection();
			connection.unsetFavoriteMovies(userId, movieIds);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result","SUCCESS"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
