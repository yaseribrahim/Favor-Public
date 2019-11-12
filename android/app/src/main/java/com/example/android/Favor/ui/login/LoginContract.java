package com.example.android.Favor.ui.login;

import org.json.JSONObject;

/**
 * Created by Ydot on 2019-02-18.
 */

public interface LoginContract {

    interface View {

        void loadLoginPage();

        void navigateToHome();
    }

    interface Presenter {

        void onServerLoggedIn(String serverToken, JSONObject userJSON);
    }
}
