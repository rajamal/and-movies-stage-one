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

package com.frustoo.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/* Parcelable Movie Model */


public class Movie implements Parcelable {

    private String mId;
    private String mTitle;
    private String mPosterPath;
    private String mVoteAverage;
    private String mOverView;
    private String mReleaseDate;
    private String mBannerPath;
    private String mVoteCount;


    private Movie(Parcel parcel) {
        mId = parcel.readString();
        mTitle = parcel.readString();
        mPosterPath = parcel.readString();
        mVoteAverage = parcel.readString();
        mOverView = parcel.readString();
        mReleaseDate = parcel.readString();
        mBannerPath = parcel.readString();
        mVoteCount = parcel.readString();
    }

    private Movie() { }

    public String getId() {
        return mId;
    }

    private void setId(String id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    private void setTitle(String title) {
        mTitle = title;
    }

    private String getPosterPath() {
        return mPosterPath;
    }

    private void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    private String getVoteAverage() {
        return mVoteAverage;
    }

    private void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getOverView() {
        return mOverView;
    }

    private void setOverView(String overView) {
        mOverView = overView;
    }

    private String getReleaseDate() {
        return mReleaseDate;
    }

    private void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    private String getBannerPath() {
        return mBannerPath;
    }

    private void setBannerPath(String bannerPath) {
        mBannerPath = bannerPath;
    }

    private String getVoteCount() {
        return mVoteCount;
    }

    private void setVoteCount(String voteCount) {
        mVoteCount = voteCount;
    }

    public String getFormattedDate() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = format.parse(getReleaseDate());
            SimpleDateFormat destFormat = new SimpleDateFormat("MMM, yyyy", Locale.US);
            return destFormat.format(date);
        } catch (ParseException pe) {
            return null;
        }
    }

    public String getFormatedVoteAverage() {
        return getVoteAverage() + " / 10";
    }

    public String getFormatedVotes() {
        return getVoteCount() + " votes";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mVoteAverage);
        parcel.writeString(mOverView);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mBannerPath);
        parcel.writeString(mVoteCount);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mVoteAverage='" + mVoteAverage + '\'' +
                ", mOverView='" + mOverView + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mBannerPath='" + mBannerPath + '\'' +
                '}';
    }

    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p";

    public String posterUrlString() {
        return IMAGE_BASE_URL + "/w185" + getPosterPath();
    }

    public String bannerUrlString() {
        return IMAGE_BASE_URL + "/w300" + getBannerPath();
    }

    public static Movie fromJson(JSONObject object) throws JSONException {
        final String MOVIE_DB_POSTER_PATH = "poster_path";
        final String MOVIE_DB_BACKDROP_PATH = "backdrop_path";
        final String MOVIE_DB_OVERVIEW = "overview";
        final String MOVIE_DB_RELEASE_DATE = "release_date";
        final String MOVIE_DB_ID = "id";
        final String MOVIE_DB_TITLE = "title";
        final String MOVIE_DB_VOTE_AVERAGE = "vote_average";
        final String MOVIE_DB_VOTES = "vote_count";

        Movie m = new Movie();
        m.setId(object.getString(MOVIE_DB_ID));
        m.setTitle(object.getString(MOVIE_DB_TITLE));
        m.setPosterPath(object.getString(MOVIE_DB_POSTER_PATH));
        m.setOverView(object.getString(MOVIE_DB_OVERVIEW));
        m.setReleaseDate(object.getString(MOVIE_DB_RELEASE_DATE));
        m.setVoteAverage(object.getString(MOVIE_DB_VOTE_AVERAGE));
        m.setBannerPath(object.getString(MOVIE_DB_BACKDROP_PATH));
        m.setVoteCount(object.getString(MOVIE_DB_VOTES));
        return m;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
