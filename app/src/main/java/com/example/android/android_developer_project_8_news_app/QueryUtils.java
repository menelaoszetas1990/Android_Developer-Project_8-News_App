package com.example.android.android_developer_project_8_news_app;

import android.content.Context;
import android.net.Uri;
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

// Helper methods related to requesting and receiving news data.
public class QueryUtils {

    // Tag for the log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static Context mContext;

    // News API keys
    private static final String API_KEY_RESPONSE = "response";
    private static final String API_KEY_RESULTS = "results";
    private static final String API_KEY_SECTION = "sectionName";
    private static final String API_KEY_PUBLISHED_DATE = "webPublicationDate";
    private static final String API_KEY_TITLE = "webTitle";
    private static final String API_KEY_WEBURL = "webUrl";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    // Query the Guardian dataset and return a list of {@link News} objects.
    public static List<News> fetchNewsData(String requestUrl, Context context) {

        mContext = context;

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link News}
        return news;
    }

    // Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(prepareUrl(stringUrl));
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
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
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList used to add news items */
        List<News> newsItems = new ArrayList<>();

        try {

            JSONObject baseJsonResponse;            // JSON Object for the data retrieved from API request
            JSONObject jsonResults;                 // JSON results fetched
            JSONArray newsArray;                    // Array of News Items
            JSONObject currentNewsItem;             // JSON object for current news item in the newsArray
            String newsTitle = "";                  // News Title
            String newsSection = "";                // News Section
            String newsDate = "";                   // Published Date
            String newsUrl = "";                    // Web URL of the news item

            baseJsonResponse = new JSONObject(newsJSON);
            jsonResults = baseJsonResponse.getJSONObject(API_KEY_RESPONSE);

            if (jsonResults.has(API_KEY_RESULTS)) {
                newsArray = jsonResults.getJSONArray(API_KEY_RESULTS);

                for (int i = 0; i < newsArray.length(); i++) {
                    currentNewsItem = newsArray.getJSONObject(i);

                    if (currentNewsItem.has(API_KEY_TITLE)) {
                        newsTitle = currentNewsItem.getString(API_KEY_TITLE);
                    }

                    if (currentNewsItem.has(API_KEY_SECTION)) {
                        newsSection = currentNewsItem.getString(API_KEY_SECTION);
                    }

                    if (currentNewsItem.has(API_KEY_PUBLISHED_DATE)) {
                        newsDate = currentNewsItem.getString(API_KEY_PUBLISHED_DATE);
                    }

                    if (currentNewsItem.has(API_KEY_WEBURL)) {
                        newsUrl = currentNewsItem.getString(API_KEY_WEBURL);
                    }

                    // Create a new {@link NewsItem} object with parameters obtained from JSON response
                    News newsItem = new News(
                            newsTitle,
                            newsSection,
                            newsDate,
                            newsUrl
                    );

                    // Add the new {@link NewsItem} object to the list of news items
                    newsItems.add(newsItem);
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error fetching data:", e);
        }

        // Return the list of newsItems
        return newsItems;
    }

    // Method to prepare the final URL to be used to fetch data
    private static String prepareUrl(String url) {
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter(
                mContext.getString(R.string.setting_api_label),
                mContext.getString(R.string.setting_api_value));

        Log.i(LOG_TAG, "Query URL => " + uriBuilder.toString());

        return uriBuilder.toString();
    }

}