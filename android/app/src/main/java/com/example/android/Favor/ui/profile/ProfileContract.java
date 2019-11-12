package com.example.android.Favor.ui.profile;

import com.example.android.Favor.data.models.Favor;
import com.example.android.Favor.data.models.Helper;
import com.example.android.Favor.data.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public interface ProfileContract {

    interface ProfileBarView{

        android.view.View loadView(User user);

        void loadInfo(String dateJoined);

    }


    interface LivePostsView{

        void loadLivePosts(ArrayList<Favor> arrayList);

        void showPossibleHelpers();

        void showRemoveDialog(Favor favor);

        void loadPossibleHelpers(ArrayList<Helper> arrayList);

        void showRateDialog(String name, String  imageURL);

    }

    interface ProfileBarPresenter{

        void onProfileInfo(JSONObject object);
    }


    interface LivePostsPresenter{
        void onLivePosts(JSONArray array);

        void onPossibleHelpers(JSONArray array);

        void helperSelected(Helper helper);
    }
}
