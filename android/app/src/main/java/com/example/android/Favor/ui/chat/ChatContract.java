package com.example.android.Favor.ui.chat;

import com.example.android.Favor.data.models.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ydot on 2019-02-18.
 */

public interface ChatContract {

    interface View {

        void loadView();

        void loadNewMessage(Message msg);

        void loadMessages(ArrayList messages);

        void displayUserInfo(String name, String imageURL);
    }

    interface Presenter {

        void onNewMessage(JSONObject obj);

        void onPrevMessages(JSONArray array);

        void onUserInfo(JSONObject obj);

    }

}
