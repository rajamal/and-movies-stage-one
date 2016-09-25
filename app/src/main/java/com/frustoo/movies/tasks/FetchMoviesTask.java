/*
    Copyright 2016 Amal Raj

    Licensed under the Apache License, Version 2.0 (the "License");
   	you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */


package com.frustoo.movies.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.frustoo.movies.BuildConfig;
import com.frustoo.movies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/* Fetch Movies Task. It takes a Delegate and a Context.
 * When the call succeeds or fails it calls the methods
 * in the FetchMoviesTaskDelegate interface. If the
 * task succeeds, it calls success with an array list of
 * movies.
*/


public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    public interface FetchMoviesTaskDelegate {
        void fetchMoviesSuccess(ArrayList<Movie> movies);
        void fetchMoviesFailure();
    }

    private final String TAG = FetchMoviesTask.class.getSimpleName();

    private final FetchMoviesTaskDelegate mDelegate;
    private final Context mContext;

    public FetchMoviesTask(Context context, FetchMoviesTaskDelegate delegate) {
        mDelegate = delegate;
        mContext = context;
    }


    /* The HTTP Fetch code is mostly copied from the Android Nanodegree lesson */
    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected()) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonString;

        try {
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIEDB_API_KEY)
                    .build();

            Log.i(TAG, "Requesting URI : " + builtUri.toString());
            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() != 0) {
                moviesJsonString = buffer.toString();
                return getMoviesList(moviesJsonString);
            } else {
                return null;
            }

        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
        } catch (JSONException je) {
            Log.e(TAG, "Error ", je);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    private ArrayList<Movie> getMoviesList(String jsonString)
            throws JSONException {

        final String MOVIE_DB_RESULTS = "results";

        JSONObject moviesResponseJson = new JSONObject(jsonString);
        JSONArray moviesArray = moviesResponseJson.getJSONArray(MOVIE_DB_RESULTS);

        ArrayList<Movie> movies = new ArrayList<>();
        for(int i = 0; i < moviesArray.length(); i++) {
            Movie movie = Movie.fromJson(moviesArray.getJSONObject(i));
            movies.add(movie);
        }
        return movies;
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        Log.i(TAG, "onPostExecute");
        if (movies != null) {
            mDelegate.fetchMoviesSuccess(movies);
        } else {
            mDelegate.fetchMoviesFailure();
        }
    }
}