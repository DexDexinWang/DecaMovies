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
import org.json.JSONObject;

import data.Movie;
import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class SearchMovies
 */
@WebServlet("/search")
public class SearchMovies extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchMovies() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get parameters from the front-end
		int zip = Integer.parseInt(request.getParameter("zip"));
		String date = request.getParameter("date");
		String userId = request.getParameter("user");
		//get movies from APIs and save data into datagbase.
		DBConnection connection = DBConnectionFactory.getDBConnection();
		List<Movie> movies = connection.searchMovies(zip, date);
		List<JSONObject> list = new ArrayList<>();
		
		//If the searched movies have been marked as favorite, then response this information back; 
		Set<String> favorite = connection.getFavoriteMoviesIds(userId);
		
		try {
			for(Movie movie: movies) {
				JSONObject obj = movie.toJSONObject();
				//add one field like: {favorite: true}
				if (favorite != null) {
					obj.put("favorite", favorite.contains(movie.getMovieId()));
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray(list);
		RpcHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
