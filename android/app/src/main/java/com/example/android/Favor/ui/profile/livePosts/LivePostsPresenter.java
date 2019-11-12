package com.example.android.Favor.ui.profile.livePosts;

import android.content.Context;

import com.example.android.Favor.data.models.Cache;
import com.example.android.Favor.data.models.Favor;
import com.example.android.Favor.data.models.Helper;
import com.example.android.Favor.data.networking.SocketClient;
import com.example.android.Favor.ui.profile.ProfileContract;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class LivePostsPresenter implements ProfileContract.LivePostsPresenter {

    private static ProfileContract.LivePostsView livePostsView;
    private Cache cache;
    private SocketClient socketClient;


    public LivePostsPresenter(ProfileContract.LivePostsView livePostsView, Context context){
        this.livePostsView = livePostsView;
        cache = Cache.getInstance(context);
        socketClient = SocketClient.getSocketClient(context);
        socketClient.setLivePostsPresenter(this);
    }

    public void load(){

//        if (cache.livePostsExist()){
//            livePostsView.loadLivePosts(cache.getUsersLivePosts());
//        }

    }


    @Override
    public void onLivePosts(JSONArray array) {
        ArrayList<Favor> livePosts = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                Favor favor = new Favor(array.getJSONObject(i));
                livePosts.add(favor);
            } catch (JSONException e) {}
        }

        //TODO: store in cache first
        cache.setUsersLivePosts(livePosts);

//        if(cache.isViewVisible("livePosts")){
            livePostsView.loadLivePosts(livePosts);
//        }
    }

    public void removePostClicked(Favor favor) {
        livePostsView.showRemoveDialog(favor);
    }

    public void leaveReviewClicked(Favor favor) {
        //TODO: replace the request with the users updated from inbox
        socketClient.requestPossibleHelpers();
        livePostsView.showPossibleHelpers();
    }

    // TODO: cache + check if view is visible
    @Override
    public void onPossibleHelpers(JSONArray array) {
        ArrayList<Helper> possibleHelpers = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try{
                Helper helper = new Helper(array.getJSONObject(i));
                possibleHelpers.add(helper);
            } catch (JSONException e){}
        }

        livePostsView.loadPossibleHelpers(possibleHelpers);
    }

    public void noReviewClicked(Favor favor) {

    }

    @Override
    public void helperSelected(Helper helper) {
        livePostsView.showRateDialog(helper.getName(), helper.getImageURL());
    }


}
