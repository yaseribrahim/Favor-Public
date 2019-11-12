package com.example.android.Favor.ui.main;


public interface MainContract {

    interface BottomNavView {

        void setUnreadMessages(int count);
    }

    interface View extends BottomNavView {
        void loadView();
        void askLocationPermissions();
    }


}
