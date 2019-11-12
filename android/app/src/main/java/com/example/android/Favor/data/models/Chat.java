package com.example.android.Favor.data.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Chat {

    private String participantName;
    private String participantImageURL;
    private String participantID;
    private String chatID;
    private String lastMessage;
    private String lastSender;
    private Date date;
    private String timeStamp;
    private int unreadMsgCount;

    public Chat(JSONObject chatObject) {
        try{
            Log.d("chatObject", chatObject.toString(4));

            JSONObject participant = chatObject.getJSONObject("participant");
            participantName = participant.getString("name");
            participantImageURL = participant.getString("imageURL");
            participantID = participant.getString("_id");
            unreadMsgCount = chatObject.getInt("unreadMessages");

            chatID = chatObject.getString("chatID");

            JSONObject lastMsgObj = chatObject.getJSONObject("latestMessage");
            timeStamp = lastMsgObj.getString("time");
            lastSender = lastMsgObj.getString("sender");
            lastMessage = lastMsgObj.getString("body");


        } catch (JSONException e) {}

    }

    public String getTimeStamp() {return timeStamp;}

    public String getParticipantName() {
        return participantName;
    }

    public String getParticipantImageURL() {
        return participantImageURL;
    }

    public String getParticipantID() {
        return participantID;
    }

    public String getChatID() {
        return chatID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastSender() {
        return lastSender;
    }

    public CharSequence getMessageTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            date = sdf.parse(timeStamp);
            long now = System.currentTimeMillis();

            CharSequence ago = DateUtils.getRelativeTimeSpanString(date.getTime(), now, DateUtils.MINUTE_IN_MILLIS);

            return ago;

        } catch (ParseException e) {
            return null;
        }

    }

    public int getUnreadMsgCount() {
        return unreadMsgCount;
    }
}
