package com.example.android.Favor.ui.post;

import android.content.Context;

import com.example.android.Favor.data.models.Cache;
import com.example.android.Favor.data.models.User;
import com.example.android.Favor.data.networking.SocketClient;
import com.example.android.Favor.data.networking.MapsClient;
import com.example.android.Favor.data.networking.MapsPresenter;

import org.json.JSONException;
import org.json.JSONObject;

public class PostPresenter implements MapsPresenter {

    private PostView postView;
    private MapsClient mapsClient;
    private SocketClient socketClient;
//    private User user;
    private Cache cache;


    public PostPresenter(PostView postView, Context postContext) {
        this.postView = postView;
        mapsClient = new MapsClient(postContext, this);
        socketClient = SocketClient.getSocketClient(postContext);
//        this.user = new User(postContext);
        cache = Cache.getInstance(postContext);
    }

    public void load() {

        postView.loadPostPage(cache.getFbImageURL());
        mapsClient.buildClient();
        postView.askLocationPermissions();


    }

    @Override
    public void mapsConnected() {
        postView.setUpCamera(mapsClient.getLatLng());
    }

    @Override
    public void locationUpdated() {
        postView.setUpCamera(mapsClient.getLatLng());
    }

    public void viewStarted() {
        mapsClient.startConnection();
    }

    public void seekBarMoved(int progress) {
        postView.updateRadius(mapsClient.getLatLng(), progress);

    }

    public void viewStopped () {
        mapsClient.stopConnection();
    }

    public void postAttempted (JSONObject postObj) {

        // add current location to post information
        try {
            postObj.put("coordinates", mapsClient.getCoordinates());
            postObj.put("locality", mapsClient.getLocality());
        } catch (JSONException e){}

        socketClient.makePost(postObj);

        postView.navigateToFeed();
    }

    public void close() {}







}
