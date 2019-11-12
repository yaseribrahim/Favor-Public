package com.example.android.Favor.data.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Message {
    private String message;
    private String timeStamp;
    private String receiver;
    private String sender;
    private JSONObject senderObj;
    private String senderID;
    private String senderName;
    private Date date;
    private String senderImageURL;
    private String mapsURL;
    private JSONObject coordinatesObj;
    private String latitude;
    private String longitude;

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


    public Message(JSONObject messageObj) {
        try{
            message = messageObj.getString("body");
            timeStamp = messageObj.getString("time");
            receiver = messageObj.getString("receiver");
            sender = messageObj.getString("sender");
            mapsURL = messageObj.getString("mapsURL");
            coordinatesObj = messageObj.getJSONObject("coordinates");
            latitude = coordinatesObj.getString("latitude");
            longitude = coordinatesObj.getString("longitude");

//            senderObj = messageObj.getJSONObject("sender");
//            senderID = senderObj.getString("_id");
//            senderName = senderObj.getString("name");
//            senderImageURL = senderObj.getString("imageURL");

        } catch (JSONException e) {}
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderImageURL() {
        return senderImageURL;
    }

    public String getSenderID() {
        return sender;
    }

    public String getTime() {

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            date = dateTimeFormat.parse(timeStamp);
            dateTimeFormat.setTimeZone(TimeZone.getDefault());

            return timeFormat.format(date);

        } catch (ParseException e) {
            return null;
        }

    }

    public String getDate() {
        SimpleDateFormat dateFormat0 = new SimpleDateFormat("MMM d, yyyy");
        dateFormat0.setTimeZone(TimeZone.getDefault());

        try {
            date = dateTimeFormat.parse(timeStamp);

            return dateFormat0.format(date);

        } catch (ParseException e) {
            return null;
        }
    }

    public String getMapsURL() {
        return mapsURL;
    }


}
