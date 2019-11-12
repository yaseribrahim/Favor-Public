package com.example.android.Favor.ui.post;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.Favor.R;
import com.example.android.Favor.ui.feed.FeedFragment;
import com.example.android.Favor.ui.main.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostActivity extends FragmentActivity implements PostView, OnMapReadyCallback {


    protected PostPresenter postPresenter;
    protected ImageView imageView;
    protected Context context;
    protected Toolbar myToolbar;
    protected Button postButton;
    protected EditText postEditText;
    protected EditText offeringPriceEditText;
    protected TextView radiusTextView;
    protected GoogleMap mMap;
    protected SupportMapFragment mapFragment;
    protected NestedScrollView scrollView;
    private SeekBar mSeekBar;
    private double radius = 200;// initial value for radius
    private int MY_PERMISSION_ACCESS_FINE_LOCATION = 0; // globally initialize a constant for our location permission
    private CameraPosition cameraPosition;
    private Circle circle;
     // Initial value of Radius


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();


        postPresenter = new PostPresenter(this, context);
        postPresenter.load();



    }

    @Override
    public void loadPostPage(String imageURL) {
        setContentView(R.layout.activity_post);

        getUiReferences();
        displayFbImage(imageURL);
        setUiFunctionality();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        postPresenter.viewStarted();
    }

    @Override
    protected void onStop() {
        super.onStop();
        postPresenter.viewStopped();
        mMap.clear();
    }

    @Override
    public void askLocationPermissions() {
        // check if user wants to have its location retrieved. This the new rule android implemented
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing so request it
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void setUpCamera(LatLng latLng) {

        cameraPosition = new CameraPosition(latLng, 16, 0,0);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.getUiSettings().setZoomControlsEnabled(false);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // show blue dot
            mMap.setMyLocationEnabled(true);

        }
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeColor(getResources().getColor(R.color.colorSecondaryLight)) // argb values for colorSecondary
                .fillColor(getResources().getColor(R.color.colorSecondaryLight))
       );




    }




    @Override
    public void updateRadius(LatLng latLng, int progress) {
        radius = progress*3;
        radiusTextView.setText(radius + "");

        mMap.clear();

        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeColor(getResources().getColor(R.color.colorSecondaryLight)) // argb values for colorSecondary
                .fillColor(getResources().getColor(R.color.colorSecondaryLight))
        );
    }


    public void getUiReferences(){
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        postEditText = (EditText) findViewById(R.id.make_request_text);
        offeringPriceEditText = (EditText) findViewById(R.id.offering_price);
        radiusTextView = (TextView) findViewById(R.id.radius_value);
        postButton = (Button) findViewById(R.id.postButton);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        scrollView = (NestedScrollView) findViewById(R.id.postViewScrollView);

    }


    public void setUiFunctionality(){
        // set functionality for exit button
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postPresenter.close();
                finish();
            }
        });

        // Automatically initialize the maps system and view
        mapFragment.getMapAsync(this);

        // notify presenter when seekBar is moved
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                postPresenter.seekBarMoved(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // when post button is clicked check if text box is empty and currency is of correct format before passing JSON object
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get values from view
                String postMsg = postEditText.getText().toString();
                String postPriceStr = offeringPriceEditText.getText().toString();
                double postPrice = Double.parseDouble(postPriceStr);
                double postRadius = radius;


                if (TextUtils.isEmpty(postMsg)) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Text box is empty", Toast.LENGTH_SHORT);
                    toast.show();

                } else if(!currencyRegex(postPriceStr)) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Price isn't in format XX.XX", Toast.LENGTH_SHORT);
                    toast.show();

                } else {

                    JSONObject postObj = new JSONObject();
                    try {
                        postObj.put("message", postMsg);
                        postObj.put("price", postPrice);
                        postObj.put("radius", postRadius);
                    } catch (JSONException e){ }

                    postPresenter.postAttempted(postObj);

                }

            }
        });



    }

    public boolean currencyRegex (String price) {

        // allow any number of digits before decimal and 2 after
        String currencyFormat = "(\\d+)(.)(\\d{2})";
        Pattern currencyPattern = Pattern.compile(currencyFormat);
        Matcher currencyMatcher = currencyPattern.matcher(price);
        if (currencyMatcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public void displayFbImage(String imageURL) {
        imageView = findViewById(R.id.post_pic);
        Glide.with(this).load(imageURL).into(imageView);

    }

    @Override
    public void navigateToFeed() {
        finish();
    }
}


