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

package com.frustoo.movies.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.frustoo.movies.R;
import com.frustoo.movies.activities.MovieDetailActivity;
import com.frustoo.movies.model.Movie;
import com.frustoo.movies.tasks.FetchMoviesTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/* Movies List Fragment. Holds a RecyclerView */

public class MoviesListFragment extends Fragment implements FetchMoviesTask.FetchMoviesTaskDelegate {

    private static final String LOG_TAG = MoviesListFragment.class.getSimpleName();

    private MoviesListAdapter mMoviesListAdapter;

    public MoviesListFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "Movies List Fragment on Start");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = preferences.getString(getString(R.string.pref_general_sort_key),
                getString(R.string.pref_general_sort_label_popular));

        new FetchMoviesTask(getActivity(), this).execute(sortOrder);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);

        mMoviesListAdapter = new MoviesListAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_list_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mMoviesListAdapter);
        return rootView;
    }

    /* Adapter for MoviesList */
    public class MoviesListAdapter extends RecyclerView.Adapter<MovieViewHolder> {

        private final ArrayList<Movie> mMovies;

        public MoviesListAdapter() {
            mMovies = new ArrayList<>();
        }


        public void replaceAllItems(ArrayList<Movie> movies) {
            mMovies.clear();
            mMovies.addAll(movies);
            notifyDataSetChanged();
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item, parent, false);

            return new MovieViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieViewHolder holder, int position) {
            holder.bind(mMovies.get(position));
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

    }

    /* Movie Grid Item Click Listener */
    private class MovieOnClickListener implements View.OnClickListener {
        private final Movie mMovie;

        public MovieOnClickListener(Movie movie) {
            mMovie = movie;
        }

        @Override
        public void onClick(View view) {
            Intent intent = MovieDetailActivity.newIntent(getActivity(), mMovie);
            startActivity(intent);
        }
    }

    /* Movie Holder */
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.movie_image_view);
        }

        public void bind(Movie movie) {
            itemView.setOnClickListener(new MovieOnClickListener(movie));

            Picasso.with(getActivity())
                    .load(movie.posterUrlString())
                    .into(mImageView);
        }
    }

    @Override
    public void fetchMoviesSuccess(ArrayList<Movie> movies) {
        mMoviesListAdapter.replaceAllItems(movies);
    }

    @Override
    public void fetchMoviesFailure() {
        Toast.makeText(getActivity(), getString(R.string.fetch_movies_failure), Toast.LENGTH_SHORT).show();
    }
}
