package com.example.android.Favor.ui.feed;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.Favor.R;
import com.example.android.Favor.data.models.Favor;
import com.example.android.Favor.ui.chat.ChatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment implements FeedContract.View {


    public FeedFragment() {
        // Required empty public constructor
    }

    private List<Favor> feed = Collections.synchronizedList(new ArrayList<Favor>());
    private ArrayList<Favor> newFeed;
    private RecyclerView mRecyclerView;
    private FeedAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private FeedPresenter feedPresenter;
    private LayoutInflater inflater;
    private ViewGroup container;
    private View emptyView;
    private View noPostsInCity;
    private Button seeAllButton;
    private LinearLayoutManager linearLayoutManager;
    private FloatingActionButton scrollUpButton;
    private int MY_PERMISSION_ACCESS_FINE_LOCATION = 0; // globally initialize a constant for our location permission
    private static final String BUNDLE_RECYCLER_LAYOUT = "feedFragment.recycler.layout";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;
        context = getContext();

        feedPresenter = new FeedPresenter(context, this);

        return feedPresenter.load();

    }

    @Override
    public View loadView() {

        // inflate view
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        // set up empty feed view
        emptyView = rootView.findViewById(R.id.empty_feed_view);
        emptyView.setVisibility(View.GONE);
        seeAllButton = (Button) rootView.findViewById(R.id.empty_button);
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedPresenter.onSeeAllClicked();
            }
        });

        // absolutely empty feed in city view
        noPostsInCity = rootView.findViewById(R.id.no_posts_in_city);
        noPostsInCity.setVisibility(View.GONE);

        // setup recyclerView
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_list);
        adapter = new FeedAdapter(getContext(), feed, feedPresenter);
        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // add swipe up to refresh functionality
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorSecondary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!newFeed.isEmpty()) {
                    // refresh feed
                    showRecyclerView();
                    feed.clear();
                    feed.addAll(newFeed);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    // show empty view
                    mRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        });

        swipeRefreshLayout.setEnabled(false);

        // set up and hide scroll up button
        scrollUpButton = (FloatingActionButton) rootView.findViewById(R.id.scroll_up_fab);
        scrollUpButton.hide();
        scrollUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutManager.scrollToPosition(0);
                scrollUpButton.hide();
            }
        });

        return rootView;
    }

    //new post added to feed
    @Override
    public void addPost(final Favor favor) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getContext(), "New favour posted", Toast.LENGTH_LONG);

                showRecyclerView();
                feed.add(0,favor);
                adapter.notifyItemInserted(0);

                if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0){
                    //show fab to scroll up
                    scrollUpButton.show();
                    toast.show();
                } else {
                    // only show toast new post added
                   toast.show();
                }

            }
        });
    }

    @Override
    public boolean isFeedEmpty() {
        return feed.isEmpty();
    }

    // show feed if there is one
    @Override
    public void updateView(final ArrayList<Favor> newFeed) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showRecyclerView();
                if (!newFeed.isEmpty()) {
                    feed.addAll(newFeed);
                    adapter.notifyDataSetChanged();
                } else {
                    showEmptyView();
                }
            }
        });
    }

    // only update feed on refresh
    @Override
    public void updateViewOnRefresh(final ArrayList<Favor> feed) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newFeed = feed;
                swipeRefreshLayout.setEnabled(true);
            }
        });
    }
    
    @Override
    public void openChat(String userID) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    public void showEmptyView(){
        if (emptyView.getVisibility() == View.GONE){
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void showRecyclerView(){
        if (mRecyclerView.getVisibility() == View.GONE){
            emptyView.setVisibility(View.GONE);
            noPostsInCity.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showNoPostsView() {
        emptyView.setVisibility(View.GONE);
        noPostsInCity.setVisibility(View.VISIBLE);

    }
}
