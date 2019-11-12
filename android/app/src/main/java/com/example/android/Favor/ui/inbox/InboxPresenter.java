package com.example.android.Favor.ui.inbox;


import android.content.Context;
import android.view.View;

import com.example.android.Favor.data.models.Cache;
import com.example.android.Favor.data.models.Chat;
import com.example.android.Favor.data.networking.SocketClient;
import com.example.android.Favor.ui.main.MainContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InboxPresenter implements InboxContract.Presenter {

    private static InboxPresenter instance = null;

    private InboxContract.View inboxView;
    private static MainContract.BottomNavView bottomNavView;
    private SocketClient socketClient;
    private Cache cache;

    public static synchronized InboxPresenter getInboxPresenter(InboxContract.View inboxView, Context context) {
        if (instance == null){
            instance = new InboxPresenter(inboxView, context);
        }

        return instance;
    }

    public static synchronized InboxPresenter getInboxPresenter(MainContract.BottomNavView view) {
        bottomNavView = view;

        return instance;
    }

    private InboxPresenter(InboxContract.View inboxView, Context context) {
        this.inboxView = inboxView;
        socketClient = SocketClient.getSocketClient(context);
        socketClient.setInboxPresenter(this);
        cache = Cache.getInstance(context);

    }

    public View load () {

//        socketClient.requestChats();

        return inboxView.loadView();
    }

    public void viewStarted() {
        ArrayList<Chat> inbox = new ArrayList<>();
        if(cache.inboxExists()){
            inbox.addAll(cache.getInbox());

            int unreadMessages = 0;

            for(int i = 0; i <  inbox.size(); i++){
                unreadMessages += inbox.get(i).getUnreadMsgCount();
            }

            bottomNavView.setUnreadMessages(unreadMessages);
            inboxView.loadChats(inbox);
        }
    }

    // user clicked on an inbox item
    public void onOpenChat(String participantID) {
        inboxView.openChat(participantID);
    }

    @Override
    public void onChatsReceived(JSONArray array, Context context) {
        Cache mCache = Cache.getInstance(context);

        // replace entire inbox
        ArrayList<Chat> updatedInbox = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                Chat chat = new Chat(array.getJSONObject(i));
                updatedInbox.add(chat);

            } catch (JSONException e) {
            }
        }

        mCache.setInbox(updatedInbox);

        if ( mCache.isViewVisible("inbox") ) {
            int unreadMessages = 0;

            for(int i = 0; i <  updatedInbox.size(); i++){
                unreadMessages += updatedInbox.get(i).getUnreadMsgCount();
            }

            bottomNavView.setUnreadMessages(unreadMessages);
            inboxView.loadChats(updatedInbox);
        }

    }

    @Override
    public void onChatUpdate(JSONObject obj, Context context) {
        Cache mCache = Cache.getInstance(context);

        ArrayList<Chat> inbox = mCache.getInbox();
        int totalUnreadMessages = 0;
        for(int i = 0; i <  inbox.size(); i++){
            totalUnreadMessages += inbox.get(i).getUnreadMsgCount();
        }

        Chat chat = new Chat(obj);
        boolean chatExists = false;
        int position = -1;
        int oldUnreadMsgCount = 0;

        // check if chat already exists
        for (int i = 0; i < inbox.size(); i++) {
            if (inbox.get(i).getChatID().equals(chat.getChatID())) {
                chatExists = true;
                position = i;
                oldUnreadMsgCount = inbox.get(i).getUnreadMsgCount();
            }
        }

        if (chatExists) {
            // replace old chat
            inbox.remove(position);
            // remove old count of unread Messages
            totalUnreadMessages -= oldUnreadMsgCount;
            // add new count of unread Messages
            totalUnreadMessages += chat.getUnreadMsgCount();
        }

        inbox.add(0, chat);

        mCache.setInbox(inbox);

        if ( mCache.isViewVisible("inbox")){
            bottomNavView.setUnreadMessages(totalUnreadMessages);

            if (chatExists) {
                inboxView.updateChat(chat, position);
            } else {
                inboxView.createNewChat(chat);
            }
        }
    }
}
