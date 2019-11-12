package com.example.android.Favor.data.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class User {

    String name;
    String facebookID;
    String imageURL;
    String rating;
    String dateJoined;


    public User(JSONObject userObj) {

        try {
            Log.d("createUser", userObj.toString(4));

            name = userObj.getString("name");
            facebookID = userObj.getString("facebookID");
            imageURL = userObj.getString("imageURL");
//            rating = userObj.getString("rating");
            dateJoined = userObj.getString("dateJoined");


        } catch (JSONException e) {
        }
    }

    public String getName() {
        return name;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getRating() {
        return rating;
    }

    public String getDateJoined() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date dateObj = dateTimeFormat.parse(dateJoined);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM d, yyyy");
            newFormat.setTimeZone(TimeZone.getDefault());
            String newDateString = newFormat.format(dateObj);

            return newDateString;

        } catch (ParseException p) {return null;}
    }
}
