package com.example.android.Favor.ui.profile;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.Favor.R;
import com.example.android.Favor.data.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ProfileContract.ProfileBarView {

    ImageView profileImageView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView nameTextView;
    TabLayout tabLayout;
    ViewPager viewPager;
    private LayoutInflater inflater;
    private ViewGroup container;
    private ProfileBarPresenter profilePresenter;
    private View rootView;
    private RatingBar ratingBar;
    private TextView dateJoinedTextView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        profilePresenter = new ProfileBarPresenter(this, getActivity());
        return profilePresenter.load();
    }

    @Override
    public View loadView(User user) {
        // inflate profileView
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // initialize views
        profileImageView = rootView.findViewById(R.id.profile_pic);
        nameTextView = rootView.findViewById(R.id.profile_name);
        ratingBar = rootView.findViewById(R.id.profile_ratingBar);
        dateJoinedTextView = rootView.findViewById(R.id.date_joined);
        tabLayout = rootView.findViewById(R.id.profile_tabLayout);
        viewPager = rootView.findViewById(R.id.profile_viewPager);

        ProfileTabAdapter profileTabAdapter = new ProfileTabAdapter(getFragmentManager());

        viewPager.setAdapter(profileTabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(getResources().getColor(R.color.mediumGray), getResources().getColor(R.color.colorOnPrimary));

        // load basic pic and name
        Glide.with(this).load(user.getImageURL()).into(profileImageView);
        nameTextView.setText(user.getName());
        ratingBar.setRating((float) 4.5);
        dateJoinedTextView.setText(getResources().getString(R.string.joined, user.getDateJoined()));
        return rootView;
    }

    @Override
    public void loadInfo(String dateJoined) {

        //Change date format
//        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//        try {
//            Date dateObj = dateTimeFormat.parse(dateJoined);
//            SimpleDateFormat newFormat = new SimpleDateFormat("MMM d, yyyy");
//            newFormat.setTimeZone(TimeZone.getDefault());
//            final String newDateString = newFormat.format(dateObj);
//
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    dateJoinedTextView.setText(getResources().getString(R.string.joined, newDateString));
//                }
//            });
//
//        } catch (ParseException p) {}

    }
}
