package com.example.android.Favor.ui.profile;

import android.content.Context;
import android.view.View;

import com.example.android.Favor.data.models.Cache;
import com.example.android.Favor.data.models.User;
import com.example.android.Favor.data.networking.SocketClient;

import org.json.JSONObject;

public class ProfileBarPresenter implements ProfileContract.ProfileBarPresenter {

    private static ProfileContract.ProfileBarView profileBarView;
    private SocketClient socketClient;
    private Cache cache;


    public ProfileBarPresenter(ProfileContract.ProfileBarView profileBarView, Context context) {
        this.profileBarView = profileBarView;
        socketClient= SocketClient.getSocketClient(context);
        socketClient.setProfileBarPresenter(this);
        cache = Cache.getInstance(context);
    }

    public View load() {

        // TODO: get user object instead of params
        User user = new User(cache.getUserJSON());

        return profileBarView.loadView(user);
    }

    @Override
    public void onProfileInfo(JSONObject obj) {
//        try {
////            String rating = obj.getString("rating");
//            String dateJoined = obj.getString("dateJoined");
//
//            profileBarView.loadInfo(dateJoined);
//
//        } catch (JSONException e) {}
    }
//
//    @Override
//    public void onLivePosts(JSONArray array) {
//        ArrayList<Favor> livePosts = new ArrayList<>();
//
//        for (int i = 0; i < array.length(); i++) {
//            try {
//                Favor favor = new Favor(array.getJSONObject(i));
//                livePosts.add(favor);
//            } catch (JSONException e) {}
//        }
//
//
//
//        livePostsView.loadLivePosts(livePosts);
//    }
//
//    public void removePostClicked(Favor favor) {
//        livePostsView.showRemoveDialog(favor);
//    }

//    public void leaveReviewClicked(Favor favor) {
//       //TODO: replace the request with the users updated from inbox
//        socketClient.requestPossibleHelpers();
//       livePostsView.showPossibleHelpers();
//    }

//    @Override
//    public void onPossibleHelpers(JSONArray array) {
//        ArrayList<Helper> possibleHelpers = new ArrayList<>();
//
//        for (int i = 0; i < array.length(); i++) {
//            try{
//                Helper helper = new Helper(array.getJSONObject(i));
//                possibleHelpers.add(helper);
//            } catch (JSONException e){}
//        }
//
//        livePostsView.loadPossibleHelpers(possibleHelpers);
//    }

//    public void noReviewClicked(Favor favor) {
//
//    }

//    @Override
//    public void helperSelected(Helper helper) {
//        livePostsView.showRateDialog(helper.getName(), helper.getImageURL());
//    }
}
