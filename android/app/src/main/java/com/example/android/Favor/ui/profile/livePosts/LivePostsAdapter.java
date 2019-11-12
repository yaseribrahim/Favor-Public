package com.example.android.Favor.ui.profile.livePosts;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.Favor.R;
import com.example.android.Favor.data.models.Favor;
import com.example.android.Favor.ui.feed.CustomRelativeTimeTextview;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ydot on 2019-03-09.
 */

public class LivePostsAdapter extends RecyclerView.Adapter<LivePostsAdapter.ViewHolder>{

    private Context context;
    private List<Favor> posts;
    private LivePostsPresenter presenter;

    public LivePostsAdapter(Context context, List<Favor> posts, LivePostsPresenter presenter) {
        this.context = context;
        this.posts = posts;
        this.presenter = presenter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, favorTextView, priceTextView, distanceTextView;
        private ImageView picImageView;
        private CustomRelativeTimeTextview timeTextView;
        private ImageView arrowButton;
        private RelativeLayout optionView;



        private ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.favor_name);
            timeTextView = itemView.findViewById(R.id.timestamp);
            picImageView = itemView.findViewById(R.id.favor_pic);
            favorTextView = itemView.findViewById(R.id.favor_text);
            distanceTextView = itemView.findViewById(R.id.favor_distance);
            priceTextView = itemView.findViewById(R.id.favor_price);
            arrowButton = itemView.findViewById(R.id.favor_arrow);
            optionView = itemView.findViewById(R.id.favor_options_layout);

            // change font
            Typeface font = ResourcesCompat.getFont(context, R.font.raleway);
            nameTextView.setTypeface(font, Typeface.BOLD);
            favorTextView.setTypeface(font);
            timeTextView.setTypeface(font);
            distanceTextView.setTypeface(font);
            priceTextView.setTypeface(font, Typeface.BOLD);

        }

    }

    @Override
    public LivePostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_favor, parent, false);
        return new LivePostsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Favor favor = posts.get(position);

        holder.nameTextView.setText(favor.getName());
        holder.favorTextView.setText(favor.getRequest());
        holder.timeTextView.setReferenceTime(favor.getTime());
        holder.priceTextView.setText(context.getString(R.string.price, Currency.getInstance(Locale.getDefault()).getSymbol(), favor.getPrice()));
        holder.distanceTextView.setText(context.getString(R.string.distance,favor.getDistance()));
        Glide.with(context).load(favor.getImageURL()).into(holder.picImageView);
        holder.optionView.setVisibility(View.GONE);

        holder.arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);

                // This activity implements OnMenuItemClickListener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_remove:
                                presenter.removePostClicked(favor);
//                                showRemoveDialog(favor, view);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_actions);
                popupMenu.show();
            }
        });


    }

    @Override
    public long getItemId(int position) {
        Favor favor = posts.get(position);
        return favor.getHashcode();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void showRemoveDialog(final Favor favor, View view) {
        new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme)
                .setTitle("Did you find help through this app?")
                .setMessage("If so, leave review for the person who helped you?")
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
}
