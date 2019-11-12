package com.example.android.Favor.ui.post;

import com.google.android.gms.maps.model.LatLng;

public interface PostView {

    void loadPostPage(String URL);


    void askLocationPermissions();

    void setUpCamera(LatLng latLng);

    void updateRadius(LatLng latLng, int progress);

    void navigateToFeed();

}
