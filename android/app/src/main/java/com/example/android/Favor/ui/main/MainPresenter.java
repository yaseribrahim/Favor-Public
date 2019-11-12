package com.example.android.Favor.ui.main;

import android.content.Context;

import com.example.android.Favor.data.models.Cache;
import com.example.android.Favor.data.networking.SocketClient;
import com.example.android.Favor.data.networking.MapsClient;
import com.example.android.Favor.data.networking.MapsPresenter;

public class MainPresenter implements MapsPresenter {

    private MainContract.View mainView;
    private SocketClient socketClient;
    private MapsClient mapsClient;
    private Cache cache;



    public MainPresenter(MainContract.View mainView, Context context) {
        this.mainView = mainView;
        socketClient = SocketClient.getSocketClient(context);
        mapsClient = new MapsClient(context, this);
        cache = Cache.getInstance(context);
    }

    public void load() {
        mainView.loadView();
        socketClient.startConnection();
        mainView.askLocationPermissions();
    }


    public void viewStarted(){
        mapsClient.startConnection();
        cache.setViewStatus("main", true);
    }
    public void viewStopped(){
        mapsClient.stopConnection();
        cache.setViewStatus("main", false);
    }

    public void close() {
        socketClient.closeConnection();
    }

    public void locationPermissionGranted(){
        mapsClient.buildClient();

    }
    @Override
    public void mapsConnected() {

        socketClient.updateLocation(mapsClient.getCoordinates(), mapsClient.getLocality());
    }

    @Override
    public void locationUpdated() {
        socketClient.updateLocation(mapsClient.getCoordinates(), mapsClient.getLocality());
    }
}
