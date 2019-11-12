package com.example.android.Favor.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.Favor.R;
import com.example.android.Favor.ui.login.LoginActivity;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SettingsFragment";

    private TextView info;
    private CallbackManager callbackManager;

    public Button button; // our button


    public SettingsFragment() {
        // Required empty public constructor
    }

    public void logoutFromFacebook(){
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
//        getActivity().finish();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        button = (Button) v.findViewById(R.id.SignOut);
        button.setOnClickListener(this);


        // Create call back manager to handle login responses
        callbackManager = CallbackManager.Factory.create();


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v) {
        Log.v(TAG, "logout");
        logoutFromFacebook();
    }

}
