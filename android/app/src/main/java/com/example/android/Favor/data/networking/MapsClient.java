package com.example.android.Favor.data.networking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsClient implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest; //mLocation Request handles the quality of the requests from FusedLocationProviderApi
    private LatLng latLng;
    private Location mLocation;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private MapsPresenter mapsPresenter;
    private Geocoder geocoder;
    private Location newLocation;


    public MapsClient(Context context, MapsPresenter mapsPresenter) {
        this.context = context;
        this.mapsPresenter = mapsPresenter;
        geocoder = new Geocoder(context, Locale.getDefault());

    }

    public void buildClient() {

        // Build google api client
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

    }

    public void startConnection() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void stopConnection() {
        if (mGoogleApiClient != null && fusedLocationProviderClient != null) {
//            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mLocation = location;
                    latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

                    mapsPresenter.mapsConnected();
                }
            });

            mLocationRequest = new LocationRequest();
//            mLocationRequest.setInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    newLocation = locationResult.getLastLocation();

//                    // only notify presenter if location change is significant
//                    if (mLocation.distanceTo(newLocation) > 10) {
                    mapsPresenter.locationUpdated();

                    mLocation = newLocation;
                }
//                }
            };
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
        }
    }


    public LatLng getLatLng() {
        return latLng;
    }

    public String getLocality() {
        try {
            List<Address> addresses = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);

            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            } else {
                return "Couldn't assign city name to these coordinates";
            }

        } catch (IOException e) {
            return "IO Exception";
        }

    }

    public JSONObject getCoordinates() {

        JSONObject coordinates = new JSONObject();
        try {
            coordinates.put("latitude", mLocation.getLatitude());
            coordinates.put("longitude", mLocation.getLongitude());
        } catch (JSONException e) {
        }

        return coordinates;
    }

}