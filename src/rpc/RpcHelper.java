package rpc;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import data.Movie;

/**
* A helper class to handle rpc related parsing logics.
*/
public class RpcHelper {
/**
 * Parses a JSONObject from http request.
 * @param request
 * @return
 */
 public static JSONObject readJsonObject(HttpServletRequest request) {
   StringBuffer sb = new StringBuffer();
   String line = null;
   try {
	 //get the body information from request which is a kind JSON file.
     BufferedReader reader = request.getReader(); 
     while ((line = reader.readLine()) != null) {
    	 sb.append(line);
     }
     reader.close();
     return new JSONObject(sb.toString());
   } catch (Exception e) {
     e.printStackTrace();
   }
   return null;
 }

/**
 * Writes a JSONObject to http response.
 * @param response
 * @param obj
 */
 public static void writeJsonObject(HttpServletResponse response, JSONObject obj) {
   try {
     response.setContentType("application/json");
     response.addHeader("Access-Control-Allow-Origin", "*");
     PrintWriter out = response.getWriter();
     out.print(obj);
     out.flush();
     out.close();
   } catch (Exception e) {
     e.printStackTrace();
   }
 }

/**
 * Writes a JSONArray to http response.
 * @param response
 * @param array
 */
 public static void writeJsonArray(HttpServletResponse response, JSONArray array) {
   try {
     response.setContentType("application/json");
     response.addHeader("Access-Control-Allow-Origin", "*");
     PrintWriter out = response.getWriter();
     out.print(array);
     out.flush();
     out.close();
   } catch (Exception e) {
     e.printStackTrace();
   }
 }
 
 /**
  * Converts a list of Item objects to JSONArray.
  * @param movies
  * @return
  */
 public static JSONArray getJSONArray(List<Movie> movies) {
   JSONArray result = new JSONArray();
   try {
     for (Movie movie : movies) {
       result.put(movie.toJSONObject());
     }
   } catch (Exception e) {
     e.printStackTrace();
   }
   return result;
 }
}
