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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frustoo.movies.R;
import com.frustoo.movies.activities.MovieDetailActivity;
import com.frustoo.movies.model.Movie;
import com.squareup.picasso.Picasso;

/* Movie Details Fragment */

public class MovieDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Movie movie = getActivity().getIntent().getParcelableExtra(MovieDetailActivity.EXTRA_MOVIE);

        ((TextView) rootView.findViewById(R.id.movie_title_text_view)).setText(movie.getTitle());
        ((TextView) rootView.findViewById(R.id.movie_date_text_view)).setText(movie.getFormattedDate());
        ((TextView) rootView.findViewById(R.id.movie_overview_text_view)).setText(movie.getOverView());
        ((TextView) rootView.findViewById(R.id.movie_votes_text_view)).setText(movie.getFormatedVotes());
        ((TextView) rootView.findViewById(R.id.movie_votes_average_text_view)).setText(movie.getFormatedVoteAverage());

        ImageView bannerView = (ImageView) rootView.findViewById(R.id.movie_banner_image_view);
        ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_poster_image_view);

        Picasso.with(getActivity()).load(movie.posterUrlString()).into(posterView);
        Picasso.with(getActivity()).load(movie.bannerUrlString()).into(bannerView);

        return rootView;
    }
}
