package com.example.android.Favor.data.models;

import org.json.JSONException;
import org.json.JSONObject;


public class Helper {

    private String name;
    private String imageURL;
    private String userID;

    public Helper(JSONObject object) {

        try {
            name = object.getString("name");
            imageURL = object.getString("imageURL");
            userID = object.getString("_id");

        } catch(JSONException e){}
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUserID() {
        return userID;
    }
}
