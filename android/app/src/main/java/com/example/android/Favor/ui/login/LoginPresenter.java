package com.example.android.Favor.ui.login;

import android.content.Context;
import android.content.Intent;

import com.example.android.Favor.data.models.Cache;
import com.example.android.Favor.data.networking.FacebookClient;
import com.example.android.Favor.data.networking.FacebookPresenter;
import com.example.android.Favor.data.networking.VolleyClient;

import org.json.JSONObject;

/**
 * Created by Ydot on 2018-10-13.
 */

public class LoginPresenter implements FacebookPresenter, LoginContract.Presenter {

    private FacebookClient facebookLogin;
    private LoginContract.View loginView;
    private VolleyClient volleyClient;
//    private User user;
    private Cache cache;

    public LoginPresenter(LoginContract.View loginView, Context context) {
        facebookLogin = new FacebookClient(this);
        this.loginView = loginView;
        volleyClient = new VolleyClient(context, this);
//        user = new User(context);
        cache = Cache.getInstance(context);
    }

    public void load() {

        // If the user is logged in, skip the login and proceed to the next page

        facebookLogin.updateLoginState();

        if (facebookLogin.getLoginState() && volleyClient.getLoginState()) {

            loginView.navigateToHome();

        } else {
            cache.deletePrevUser();

            loginView.loadLoginPage();
            facebookLogin.createCallbackManager();
            facebookLogin.createLoginEventHandler();
        }
    }

    // Update callback manager if user attempts login
    public void loginAttempt(int requestCode, int resultCode, Intent data) {

        facebookLogin.updateCallbackManager(requestCode, resultCode, data);
    }

    // If fbLogin successful, login to server
    @Override
    public void onFBLoggedIn() {
        volleyClient.attemptLogin(facebookLogin.getName(), facebookLogin.getFacebook_id());

    }

    // if server login in successful, save userinfo go to home page
    @Override
    public void onServerLoggedIn(String serverToken, JSONObject userJSON) {

        cache.saveLoginToken(serverToken);
        cache.setUserJSON(userJSON);

//        cache.setName(facebookLogin.getName());
//        cache.setFacebookID(facebookLogin.getFacebook_id());
//        cache.setUserID(userID);




//        user.setName(facebookLogin.getName());
//        user.setFacebookID(facebookLogin.getFacebook_id());
//        user.setUserID(userID);

        loginView.navigateToHome();
    }
}
