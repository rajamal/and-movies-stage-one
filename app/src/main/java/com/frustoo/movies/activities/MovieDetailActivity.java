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

package com.frustoo.movies.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.frustoo.movies.R;
import com.frustoo.movies.fragments.MovieDetailFragment;
import com.frustoo.movies.model.Movie;


/* Movie Detail Activity which displays the Movie Detail Fragment */

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";

    public static Intent newIntent(Context context, Movie movie) {
        Intent  intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MovieDetailFragment())
                .commit();
    }
}
