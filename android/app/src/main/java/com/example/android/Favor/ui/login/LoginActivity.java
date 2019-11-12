package com.example.android.Favor.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


import com.example.android.Favor.R;
import com.example.android.Favor.ui.main.MainActivity;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Ydot on 2018-02-07.
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    LoginButton fbLoginButton;
    Button fbCustomButton;
    LoginPresenter loginPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginPresenter = new LoginPresenter(this, this);
        loginPresenter.load();

    }



    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    // OnClick method for custom fb button
    public void fb_login(View view) {
        fbLoginButton.performClick();
    }

    // The fb login popup activity returns a result, notify the presenter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loginPresenter.loginAttempt(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void loadLoginPage() {
        // load the login page and initialize sdk
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());

        //log events back to fb
        AppEventsLogger.activateApp(this);

        // The facebook button that comes with the sdk looks ugly. Here we reference
        // a custom button that will inherit the standard facebook buttons functionality
        fbLoginButton = findViewById(R.id.login_button);
        fbCustomButton = findViewById(R.id.fb_custom_button);

        // add clicking animation to button by turning gray once clicked
        buttonEffect(fbCustomButton);


    }

    @Override
    public void navigateToHome() {
        Intent feed = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(feed);
        finish();
        System.exit(0); // what is this?

    }
}
