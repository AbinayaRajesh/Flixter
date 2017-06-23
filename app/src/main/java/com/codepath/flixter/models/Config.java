package com.codepath.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arajesh on 6/22/17.
 */

public class Config {

    // the base url for loading images
    String imageBaseUrl;
    // the poster size to use when fetching images, part of the url
    String posterSize;
    // the backdrop size to use when fetching images
    String backdropSize;

    public Config (JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        // get the image base url
        imageBaseUrl = images.getString("secure_base_url");
        // get image size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // parse the poster sizes and use the option at index 3 or w342
        posterSize  = posterSizeOptions.optString(3, "w342");
        // parse the backdrop sizes and use the option at index 1 or w780
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");

    }

    // helper method for creating urls
    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() { return backdropSize; }
}
