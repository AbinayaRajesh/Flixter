package com.codepath.flixter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.flixter.models.Config;
import com.codepath.flixter.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.flixter.MovieListActivity.API_BASE_URL;
import static com.codepath.flixter.MovieListActivity.API_KEY_PARAM;
import static com.codepath.flixter.R.id.imgView;
import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by arajesh on 6/23/17.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;

    // Instance fields
    AsyncHttpClient client;

    // image config
    Config config;

    public final static String TAG = "MovieDetailsActivity";

    // the view objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    @BindView(imgView) ImageView img;

    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Enable up icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        client = new AsyncHttpClient();


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        config = (Config) Parcels.unwrap(getIntent().getParcelableExtra(Config.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        String path = movie.getBackdropPath();
        ImageView imageview = (ImageView) findViewById(R.id.imgView);
        String imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());

        // load image using glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .bitmapTransform(new RoundedCornersTransformation(this, 25, 0))
                .into(imageview);

        getVideo();
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // first parameter is the context, second is the class of the activity to launch
                Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                // put "extras" into the bundle for access in the second activity
                //getVideo();
                i.putExtra("videoId", key);
                // brings up the second activity
                startActivity(i);

            }
        });

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = (float) movie.getVoteAverage();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }


    // get the list of currently playing movies from the API
    private void getVideo() {
        // create the url
        String temp = movie.getId().toString();
        String url = API_BASE_URL+"/movie/"+temp+"/videos";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));  // Always needs API key
        // request a GET response expecting a JSON object response

        client.get(url,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load the results into movies list

                try {
                    JSONArray results = response.getJSONArray("results");
                    JSONObject curMovie = results.getJSONObject(0);
                    key = curMovie.getString("key");

                } catch (JSONException e) {
                    logError("Failed to parse play_video endpoint", e, true);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from Now_playing endpoint", throwable, true);
            }
        });

    }



    // handle errors, log and alert users
    private void logError(String message, Throwable error, boolean alertUser){
        // always log the error
        log.e(TAG, message, error);
        // alert the user to avoid silent errors
        if (alertUser){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        }

    }

}