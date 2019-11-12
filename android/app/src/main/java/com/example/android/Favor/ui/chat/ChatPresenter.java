package com.example.android.Favor.ui.chat;

import android.content.Context;

import com.example.android.Favor.data.models.Cache;
import com.example.android.Favor.data.models.Message;
import com.example.android.Favor.data.networking.SocketClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ydot on 2019-02-16.
 */

public class ChatPresenter implements ChatContract.Presenter  {
    private ChatContract.View chatView;
    private SocketClient socketClient;
    private String participantID;
    private Cache cache;

    public ChatPresenter(ChatContract.View chatView, Context context) {
        this.chatView = chatView;
        socketClient = SocketClient.getSocketClient(context);
        socketClient.setChatPresenter(this);
        cache = Cache.getInstance(context);
    }

    public void load(String participantID){
        this.participantID = participantID;
        chatView.loadView();
        socketClient.loadChat(participantID);
    }

    public void viewStarted(){
        cache.setViewStatus("chat", true);

        if (cache.chatParticipantExists(participantID)) {
            chatView.displayUserInfo(cache.getParticipantName(participantID), cache.getParticipantImage(participantID));

            if (cache.chatHistoryExists(participantID)) {
                chatView.loadMessages(cache.getChatHistory(participantID));
            }
        }

    }

    @Override
    public void onUserInfo(JSONObject userObj) {
        try {
            String name = userObj.getString("name");
            String imageURL = userObj.getString("imageURL");

            chatView.displayUserInfo(name, imageURL);
            cache.setParticipantInfo(participantID, name, imageURL);

        } catch (JSONException e) {}
    }

    @Override
    public void onPrevMessages(JSONArray array) {
        ArrayList<Message> messages = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                Message message = new Message(array.getJSONObject(i));
                messages.add(message);
            } catch (JSONException e){}
        }
        chatView.loadMessages(messages);
        cache.setChatHistory(participantID, messages);
        socketClient.markAsRead(participantID);
    }

    public void sendMessage(String message) {
        socketClient.sendMessage(participantID, message);
    }


    @Override
    public void onNewMessage(JSONObject obj) {
        Message message = new Message(obj);
        ArrayList<Message> messages = cache.getChatHistory(participantID);
        messages.add(message);
        cache.setChatHistory(participantID, messages);

        if(cache.isViewVisible("chat")) {
            chatView.loadNewMessage(message);
            socketClient.markAsRead(participantID);
        }
    }

    public void shareLocationClicked() {
        socketClient.shareLocation(participantID);
    }

    public void viewStopped(){
        cache.setViewStatus("chat", false);

    }

    public void chatClosed(){
        socketClient.closeChatListeners();
    }






}
