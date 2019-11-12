package com.example.android.Favor.ui.inbox;

import android.content.Context;

import com.example.android.Favor.data.models.Chat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public interface InboxContract {

    interface View {

        android.view.View loadView();

        void loadChats(ArrayList<Chat> arrayList);

        void updateChat(Chat chat, int position);

        void createNewChat(Chat chat);

        void openChat(String userID);



    }

    interface Presenter {

        void onChatsReceived(JSONArray array, Context context);

        void onChatUpdate(JSONObject obj, Context context);


    }
}
