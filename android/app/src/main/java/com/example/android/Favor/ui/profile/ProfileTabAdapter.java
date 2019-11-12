package com.example.android.Favor.ui.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.Favor.ui.profile.livePosts.LivePostsFragment;

public class ProfileTabAdapter extends FragmentPagerAdapter {

    public ProfileTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new LivePostsFragment();
        } else {
            return new LivePostsFragment();
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Live Posts";
            case 1:
                return "Reviews";
            default:
                return null;
        }
    }
}
