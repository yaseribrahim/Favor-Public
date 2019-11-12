package com.example.android.Favor.ui.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.android.Favor.R;
import com.example.android.Favor.ui.BottomNavAdapter;
import com.example.android.Favor.ui.NoSwipePager;
import com.example.android.Favor.ui.SettingsFragment;
import com.example.android.Favor.ui.feed.FeedFragment;
import com.example.android.Favor.ui.inbox.InboxFragment;
import com.example.android.Favor.ui.inbox.InboxPresenter;
import com.example.android.Favor.ui.post.PostActivity;
import com.example.android.Favor.ui.profile.ProfileFragment;



public class MainActivity extends AppCompatActivity implements MainContract.View {

    private AHBottomNavigation bottomNavigation;
    private BottomNavAdapter pagerAdapter;
    private NoSwipePager viewPager;
    public static String name, facebook_id;
    private FloatingActionButton fab;
    private final static int MY_PERMISSION_ACCESS_FINE_LOCATION = 0; // globally initialize a constant for our location permission


    private MainPresenter mainPresenter;
    private InboxPresenter inboxPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainPresenter = new MainPresenter(this, this);
        mainPresenter.load();
        inboxPresenter = InboxPresenter.getInboxPresenter(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.viewStarted();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.viewStopped();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.close();
    }

    @Override
    public void askLocationPermissions() {
        // check if user wants to have its location retrieved. This the new rule android implemented
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing so request it
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            mainPresenter.locationPermissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    mainPresenter.locationPermissionGranted();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }

    }


    @Override
    public void loadView() {

        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        setUpBottomNav();
        setUpViewPager();
        setUIFunctionality();
    }


    public void setUpViewPager() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);
        pagerAdapter = new BottomNavAdapter(getSupportFragmentManager());

        ProfileFragment profileFragment = new ProfileFragment();
        FeedFragment feedFragment = new FeedFragment();
        InboxFragment inboxFragment = new InboxFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        pagerAdapter.addFragments(feedFragment);
        pagerAdapter.addFragments(inboxFragment);
        pagerAdapter.addFragments(profileFragment);
        pagerAdapter.addFragments(settingsFragment);

        viewPager.setAdapter(pagerAdapter);
    }

    public void setUpBottomNav() {
        // Create 4 items for bottom nav (holds title and icon)
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Feed", R.drawable.ic_feed);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Inbox", R.drawable.ic_messages);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Profile", R.drawable.ic_person);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Settings", R.drawable.ic_settings);

        bottomNavigation.setAccentColor(getResources().getColor(R.color.white));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.colorSecondaryLight));
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorSecondary));
        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.colorAlternate1));

        // Add items to nav bar
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        // start at home
        bottomNavigation.setCurrentItem(0);
    }

    public void setUIFunctionality() {

        // hide floating action button if not displaying feedView
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
//                fragment.updateColor(Color.parseColor(colors[position]));

                if (!wasSelected)
                    viewPager.setCurrentItem(position);
                if (position != 0)
                    fab.hide();
                else
                    fab.show();
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(MainActivity.this, PostActivity.class);
                Intent.putExtra("FROM_ACTIVITY","Main");
                startActivity(Intent);
            }
        });

    }

    @Override
    public void setUnreadMessages(final int count) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bottomNavigation.setNotification(count, 1);
            }
        });

    }
}

