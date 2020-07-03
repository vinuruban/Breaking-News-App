package com.example.breakingnews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class QueryUtils {


    private QueryUtils() {
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    //THIS IS AN EXTRA METHOD USED TO HANDLE MULTIPLE URLS, fetchData() SLIGHTLY CHANGED
    public static List<NewsObject> handleMultipleUrls(String requestUrl, String requestUrl2, String requestUrl3) {

        String firstJsonResponse = fetchData(requestUrl);

        //NOTE: extractFeatureFromFirstJson IS DIFFERENT TO extractFeatureFromJson
        List<String> news = extractFeatureFromFirstJson(firstJsonResponse);

        // looping through All news items
        String jsonResponse = "";
        List<NewsObject> newsList = new ArrayList<>();
        for (int i = 0; i < news.size(); i++) {

            String code = news.get(i);

            jsonResponse = fetchData(requestUrl2 + code + requestUrl3);

            // Extract relevant fields from the JSON response and create a list of {@link Book}s
            //NOTE: extractFeatureFromFirstJson IS DIFFERENT TO extractFeatureFromJson
            newsList.add(extractFeatureFromJson(jsonResponse));
        }

        // Return the list of {@link Book}s
        return newsList;
    }

    /**
     * Query the USGS dataset and return a list of {@link NewsObject} objects.
     */

    public static String fetchData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return jsonResponse;
    }



    //    CODE BELOW IS NEEDED TO EXECUTE THE NETWORK REQUEST. NO NEED TO AMEND IT!!



    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    //    BELOW IS THE CODE THAT I MUST AMEND. IT DETAILS HOW TO EXTRACT SPECIFIC DATA WITHIN THE JSON


    /**
     * Return a list of {@link NewsObject} objects that has been built up from
     * parsing the given JSON response.
     */
    public static List<String> extractFeatureFromFirstJson(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<String> news = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONArray jsonArray = new JSONArray(newsJSON);

            // looping through All jsonArray
            for (int i = 0; i < 5; i++) {

                String code = jsonArray.getString(i);
                news.add(code);
            }
        } catch(JSONException e){
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return news;

    }

    /**
     * Return a list of {@link NewsObject} objects that has been built up from
     * parsing the given JSON response.
     */
    public static NewsObject extractFeatureFromJson(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        String id = "";
        String time = "";
        String title = "";
        String url = "";

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject jsonObj = new JSONObject(newsJSON);

            id = jsonObj.getString("id");

            time = jsonObj.getString("time");

            title = jsonObj.getString("title");

            url = jsonObj.getString("url");

            } catch(JSONException e){
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
            }

            // Return the list of books
        return new NewsObject(id, time, title, url);

    }

}
