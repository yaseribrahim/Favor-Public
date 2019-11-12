package com.example.android.Favor.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ydot on 2018-03-04.
 */

// An adapter acts as the middle man between an array of data and a Cache that displays the content of that array
// The Cache that displays it is called an AdapterView. A few examples of them are List Cache, Grid Cache, and Spinner.
public class BottomNavAdapter extends FragmentPagerAdapter {
    // An ArrayList is an array that is mutable and can store only objects where as normal array isn't mutable and
    // can store both primitive data types(int, double) and objects. It implements the class List. ArrayList implements
    // the interface List. Recall that a
    private final List<Fragment> fragments = new ArrayList<>();

    public BottomNavAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addFragments(Fragment fragment) {
        fragments.add(fragment);
    }

    // .get(position) requests the position of the list item the user is looking at from the Cache
    // and returns the necessary data to fill the views.
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
