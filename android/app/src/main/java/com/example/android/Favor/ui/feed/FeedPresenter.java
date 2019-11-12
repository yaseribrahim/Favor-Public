package com.example.android.Favor.ui.feed;

import android.content.Context;
import android.view.View;

import com.example.android.Favor.data.models.Favor;
import com.example.android.Favor.data.networking.SocketClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedPresenter implements FeedContract.Presenter {
    private FeedContract.View feedView;
    private SocketClient socketClient;

    public FeedPresenter(Context context, FeedContract.View feedView) {
        this.feedView = feedView;
        socketClient = SocketClient.getSocketClient(context);
        socketClient.setFeedPresenter(this);
    }


    public View load () {

        return feedView.loadView();
    }

    @Override
    public void onLocationUpdated() {

        socketClient.requestFeed();
    }

    // the entire feed was updated
    @Override
    public void onNewFeed(JSONArray array) {
        ArrayList<Favor> newFeed = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                Favor favor = new Favor(array.getJSONObject(i));
                newFeed.add(favor);
            } catch (JSONException e) {}
        }

        if(feedView.isFeedEmpty()) {
            feedView.updateView(newFeed);
        }else {
            feedView.updateViewOnRefresh(newFeed);
        }
    }

    // a single post was emitted
    @Override
    public void onNewPost(JSONObject obj) {
        Favor favor = new Favor(obj);
        feedView.addPost(favor);
    }

    public void onMessageBtnClicked(String userID) {
        feedView.openChat(userID);
    }

    public void onSeeAllClicked(){
        //get all posts in that locality
        socketClient.requestAllPosts();
    }

    @Override
    public void onNoPostsInLocality() {
        feedView.showNoPostsView();

    }
}
