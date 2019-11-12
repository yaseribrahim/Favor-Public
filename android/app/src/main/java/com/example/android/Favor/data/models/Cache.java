package com.example.android.Favor.data.models;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cache {

    private Context context;
    private SharedPreferences sharedPreferences;
    private static Cache instance;
    private SharedPreferences.Editor editor;

    private Cache(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public static synchronized Cache getInstance(Context context) {
        if(instance == null){
            instance = new Cache(context);
        }
        return instance;
    }

    /* Login Activity */
    public void saveLoginToken(String serverToken){

        editor.putString("token", serverToken);
        editor.apply();
    }

    public void setViewStatus(String view, boolean visible){
        editor.putBoolean(view, visible);
        editor.apply();
    }


    public boolean isViewVisible(String view){

        // view is on main activity
        //TODO: haha this isn't nice. Switch case?
        if (view.equals("feed") || view.equals("inbox") || view.equals("profile") || view.equals("settings")
            || view.equals("livePosts")) {

            return sharedPreferences.getBoolean("main", false);

        } else if (view.equals("chat")) {
            return sharedPreferences.getBoolean("chat", false);
        } else {
            return false;
        }

    }

    /** Inbox and Chat Cache **/
    public ArrayList<Chat> getInbox(){
        // get JSON string from sharedpref
        String inboxJSONArray = sharedPreferences.getString("inboxJSON","");
        // convert to arraylist of Chat objects
        ArrayList<Chat> inbox = new Gson().fromJson(inboxJSONArray, new TypeToken<List<Chat>>(){}.getType());


        if (!inbox.isEmpty()) {
            for (int i = 0; i < inbox.size(); i++) {
                Log.d("getInboxTimes: ", String.valueOf(inbox.get(i).getTimeStamp()));

                Log.d("getInboxMsgs: ", String.valueOf(inbox.get(i).getLastMessage()));


            }
        }


        return inbox;
    }

    public void setInbox(ArrayList<Chat> newInbox){
        String inboxJSONArray = new Gson().toJson(newInbox);

        Log.d("setInbox: ", inboxJSONArray);

        editor.putString("inboxJSON", inboxJSONArray);
        editor.apply();

        Log.d("checkInbox", sharedPreferences.getString("inboxJSON", "null"));
    }

    public boolean inboxExists(){
        return sharedPreferences.contains("inboxJSON");
    }

    public void setParticipantInfo(String participantID, String name, String imageURL){
        editor.putString(participantID + "name", name );
        editor.putString(participantID + "imageURL", imageURL);

        editor.putBoolean(participantID + "info", true);
        editor.apply();
    }

    public boolean chatParticipantExists(String participantID){
        return sharedPreferences.getBoolean(participantID + "info", false);
    }

    public String getParticipantName(String participantID){
        return sharedPreferences.getString(participantID + "name", "null");
    }

    public String getParticipantImage(String participantID){
        return sharedPreferences.getString(participantID + "imageURL", "null");
    }

    public void setChatHistory(String participantID, ArrayList<Message> messages){
        String messagesJSONArray = new Gson().toJson(messages);
        editor.putString(participantID + "chatHistory", messagesJSONArray);

        editor.putBoolean(participantID + "chatHistoryExists", true);
        editor.apply();
    }

    public boolean chatHistoryExists(String participantID){
        return sharedPreferences.getBoolean(participantID + "chatHistoryExists", false);
    }

    public ArrayList<Message> getChatHistory(String participantID) {
        String messagesJSONArray = sharedPreferences.getString(participantID + "chatHistory", null);

        ArrayList<Message> chatHistory = new Gson().fromJson(messagesJSONArray, new TypeToken<List<Message>>(){}.getType());

        return chatHistory;
    }


    /** User info cache **/

    public void setUserJSON(JSONObject userJSON){

        editor.putString("user", userJSON.toString());
        editor.commit();
    }

    public JSONObject getUserJSON(){

        try {
            String userJsonString = sharedPreferences.getString("user", null);
            JSONObject userJSON = new JSONObject(userJsonString);

            Log.d("getUserJSON", userJSON.toString(4));
            return userJSON;
        }catch (JSONException err){
            Log.d("Error", err.toString());
            return null;
        }
    }

    public void setName(String name) {
        editor.putString("name", name).commit();
    }

    public String getName() {
        return sharedPreferences.getString("name", null);
    }

    public String getFacebookID() {
        return sharedPreferences.getString("facebookID", null);
    }

    public String getFbImageURL() {
        String fbImageURL = "https://graph.facebook.com/" + getFacebookID() + "/picture?type=large";
        return fbImageURL;
    }

    public String getUserID() {
        String ID = sharedPreferences.getString("userID", null);
        return ID;
    }

    public void setUserID(String userID) {
        editor.putString("userID", userID).commit();
    }

    public void deletePrevUser() {
        editor.remove("name").apply();
        editor.remove("facebookID").apply();
        editor.remove("imageURL").apply();
        editor.remove("_id").apply();
        editor.remove("userID").apply();
        editor.remove("token").apply();
    }

    public void setFacebookID(String facebookID) {
        editor.putString("facebookID", facebookID).commit();
    }

    public void setUsersLivePosts(ArrayList<Favor> livePosts){
        String livePostsJSONArray = new Gson().toJson(livePosts);

        Log.d("UsersLivePosts", livePostsJSONArray);

        editor.putString("livePostsJSON", livePostsJSONArray);
        editor.apply();
    }

    public ArrayList<Favor>  getUsersLivePosts(){
        String inboxJSONArray = sharedPreferences.getString("livePostsJSON","");

        ArrayList<Favor> livePosts = new Gson().fromJson(inboxJSONArray, new TypeToken<List<Favor>>(){}.getType());

        if (!livePosts.isEmpty()) {
            for (int i = 0; i < livePosts.size(); i++) {
                Log.d("getLivePostsRequest: ", String.valueOf(livePosts.get(i).getRequest()));
            }
        }

        return livePosts;
    }

    public boolean livePostsExist(){
        return sharedPreferences.contains("livePostsJSON");
    }

}
