package com.example.android.Favor.ui.feed;

import com.example.android.Favor.data.models.Favor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ydot on 2019-02-18.
 */

public interface FeedContract {

    interface View {

        android.view.View loadView();

        void addPost(Favor favor);

        void updateView(ArrayList<Favor> arrayList);

        void updateViewOnRefresh(ArrayList<Favor> arrayList);

        void openChat(String userID);

        void showNoPostsView();

        boolean isFeedEmpty();


    }

    interface Presenter {

        void onNewPost(JSONObject obj);

        void onLocationUpdated();

        void onNewFeed(JSONArray array);

        void onNoPostsInLocality();
    }
}
