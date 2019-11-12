package com.example.android.Favor.ui.profile.livePosts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.Favor.R;
import com.example.android.Favor.data.models.Favor;
import com.example.android.Favor.data.models.Helper;
import com.example.android.Favor.ui.profile.HelpersAdapter;
import com.example.android.Favor.ui.profile.ProfileContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LivePostsFragment extends Fragment implements ProfileContract.LivePostsView {

    private View rootView;
    private RecyclerView postsRecyclerView;
    private LivePostsAdapter livePostsAdapter;
    private RecyclerView helpersRecyclerView;
    private View helperDialogView;
    private HelpersAdapter helpersAdapter;
    private View rateDialogView;
    private TextView rateNameTextView;
    private ImageView rateImageView;
    private List<Favor> posts = Collections.synchronizedList(new ArrayList<Favor>()) ;
    private List<Helper> helpers = Collections.synchronizedList(new ArrayList<Helper>());
    private LayoutInflater inflater;
    private LivePostsPresenter presenter;



    public LivePostsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;

        presenter = new LivePostsPresenter(this, getActivity());

        rootView = inflater.inflate(R.layout.fragment_liveposts, container, false);

        // initialize views
        postsRecyclerView = (RecyclerView) rootView.findViewById(R.id.postsRecyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        livePostsAdapter = new LivePostsAdapter(getApplicationContext(), posts, presenter);
        postsRecyclerView.setAdapter(livePostsAdapter);
        postsRecyclerView.setLayoutManager(llm);


        presenter.load();

        return rootView;
    }

    @Override
    public void loadLivePosts(final ArrayList<Favor> arrayList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                posts.clear();
                posts.addAll(arrayList);
                livePostsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showPossibleHelpers() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setTitle("Who helped you?");
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        LayoutInflater inflater = getLayoutInflater();
        helperDialogView= (View) inflater.inflate(R.layout.dialog_find_helper, null);
        builder.setView(helperDialogView);

        helpersRecyclerView = (RecyclerView) helperDialogView.findViewById(R.id.helperRecyclerView);
        helpersRecyclerView = helperDialogView.findViewById(R.id.helperRecyclerView);
        helpersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        helpersAdapter = new HelpersAdapter(helpers, presenter, getApplicationContext());
        helpersRecyclerView.setAdapter(helpersAdapter);


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showRemoveDialog(final Favor favor) {
        new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setTitle("Leave a rating?")
                .setMessage("Before you remove this post, rate whoever helped you.")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.leaveReviewClicked(favor);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.noReviewClicked(favor);
                    }
                })
                .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }

    @Override
    public void loadPossibleHelpers(final ArrayList<Helper> arrayList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                helpers.clear();
                helpers.addAll(arrayList);
                helpersAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void showRateDialog(String name, String imageURL) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        rateDialogView = (View) inflater.inflate(R.layout.dialog_rate_helper, null);
        rateNameTextView = (TextView) rateDialogView.findViewById(R.id.rate_name);
        rateImageView = (ImageView) rateDialogView.findViewById(R.id.rate_image);
        builder.setView(rateDialogView);
        rateNameTextView.setText(name);
        Glide.with(getApplicationContext()).load(imageURL).into(rateImageView);


        builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
