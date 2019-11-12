package com.example.android.Favor.data.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Ydot on 2018-07-28.
 */



public class Favor {

    public Favor(JSONObject favorObj) {
        try {
            JSONObject postedBy = favorObj.getJSONObject("postedBy");
            name = postedBy.getString("name");
            facebookID = postedBy.getString("facebookID");
            userID = postedBy.getString("_id");
            imageURL = postedBy.getString("imageURL");
            request = favorObj.getString("body");
            time = favorObj.getString("time");
            price = favorObj.getString("price");
            distance = favorObj.getString("distance");
            hashCode = favorObj.getLong("hashCode");


        } catch (JSONException e){}


    }

    private String userID;

    private long hashCode;

    private String name;

    private String request = null;

    private String price;

    private String distance;

    private String imageURL;

    private String time;

    private String facebookID;

    public String getRequest() {
        return request;
    }

    public String getPrice() {
        return price;
    }

    public String getDistance() {
        return distance;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }

    public Long getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            String timeString = time;
            long timePosted = sdf.parse(timeString).getTime();

            return timePosted;

        } catch (ParseException e) {
            return null;
        }
    }


    public String getFacebookID() { return facebookID; }

    public String getUserID() {return userID; }

    public long getHashcode() {return  hashCode;}

}


