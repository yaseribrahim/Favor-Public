package com.example.android.Favor.data.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.Favor.ui.login.LoginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by: Yaser Ibrahim
 *
 * This class handles the communication to the REST API
 */

//TODO: make all non-realtime events go through this
public class VolleyClient {


    private String URL = "http://192.168.1.73:3000/login";

    private RequestQueue queue;
    private LoginContract.Presenter presenter;
    private Context context;
    private SharedPreferences pref;

    public VolleyClient(Context context, LoginContract.Presenter presenter) {

        this.context = context;
        queue = Volley.newRequestQueue(this.context);
        this.presenter = presenter;
        //TODO: remove
        pref = PreferenceManager.getDefaultSharedPreferences(context);


    }

    // send POST Request to API with user info, if response successful, store the API token
    public void attemptLogin(String name, String facebookID) {
        final JSONObject postObj = new JSONObject();
        try {
            postObj.put("name", name);
            postObj.put("facebookID", facebookID);

        } catch (JSONException e) {
            Log.d("JSON Exception", e.toString());
        }

        Log.d("VOLLEY POST", postObj.toString());


        JsonObjectRequest postRequest = new JsonObjectRequest(URL, postObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getString("status").equals("SUCCESS")) {
                        Log.d("Response", response.toString(4));

                        // user logged in successfully, notify presenter
                        presenter.onServerLoggedIn(response.getString("token"), response.getJSONObject("user"));
                    } else {
                        Log.d("VOLLEY", "Response Unsuccessful");
                    }
                } catch (JSONException e) {
                    Log.d("VOLLEY JSON Exception", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", error.toString());
            }
        });


        queue.add(postRequest);
    }

    public boolean getLoginState() {
        String token = pref.getString("token", null);

        if (token != null) {
            return true;
        } else {
            return false;
        }
    }

}
