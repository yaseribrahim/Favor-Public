package com.example.android.Favor.data.networking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ydot on 2018-09-03.
 */

public class FacebookClient {

    private AccessToken accessToken;
    private boolean isLoggedIn;

    private CallbackManager callbackManager;
    private FacebookPresenter presenter;

    private String facebook_id;
    private String name;


    public FacebookClient(FacebookPresenter presenter) {
        this.presenter = presenter;
    }

    private void makeGraphRequest(AccessToken accessToken) {
        // Make graph api request and retain name and unique facebook Id
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {

                    Log.i("blablabla", response.getJSONObject().toString());
                    facebook_id = response.getJSONObject().getString("id");
                    name = response.getJSONObject().getString("name");

                    presenter.onFBLoggedIn();
//                    email = response.getJSONObject().getString("email");

                } catch (JSONException e) {
                    Log.e("ERROR", "failed to get facebook param ");
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email");
        request.setParameters(parameters);
        request.executeAsync();

        Log.d("BLABLA", "request made");
    }



    public void updateLoginState() {
        // Fetch access token from Facebook's graph API
        accessToken = accessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();


    }

    public boolean getLoginState() {

        return isLoggedIn;
    }

    public void createCallbackManager() {

        // Create call back manager to handle login responses from graph API
        callbackManager = CallbackManager.Factory.create();
    }

    public void createLoginEventHandler() {

        // Create a class to handle login events using the FacebookCallback interface
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // access token obtained as a result of login
                        accessToken = loginResult.getAccessToken();
                        makeGraphRequest(accessToken);

                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });

    }

    public void updateCallbackManager(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public String getName() {
        return name;
    }


    public String getFacebook_id() {
        return facebook_id;
    }


}