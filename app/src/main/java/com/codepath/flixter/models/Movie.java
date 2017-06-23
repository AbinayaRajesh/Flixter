package com.codepath.flixter.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by arajesh on 6/22/17.
 */
@Parcel
public class Movie {

    // values from API
    String title;
    String overview;
    String posterPath; // only the path
    String backdropPath;
    double voteAverage;
    Integer id;

    // initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        id = object.getInt("id");
    }

    public Movie() {}

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() { return backdropPath; }

    public double getVoteAverage() {
        return voteAverage;
    }

    public Integer getId() {
        return id;
    }
}
