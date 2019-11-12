package com.example.android.Favor.ui.profile;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.Favor.R;
import com.example.android.Favor.data.models.Helper;
import com.example.android.Favor.ui.profile.livePosts.LivePostsPresenter;

import java.util.List;

/**
 * Created by Ydot on 2019-04-14.
 */

public class HelpersAdapter extends RecyclerView.Adapter<HelpersAdapter.ViewHolder> {

    private List<Helper> helpers;
    private LivePostsPresenter presenter;
    private Context context;


    public HelpersAdapter(List<Helper> helpers, LivePostsPresenter presenter, Context context) {
        this.helpers = helpers;
        this.presenter = presenter;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private ImageView picImageView;
        private RelativeLayout helperView;

        public ViewHolder(View itemView) {
            super(itemView);

            // limit number of items displayed
            int max = 3;
            if (getAdapterPosition() > max - 1) {
                itemView.setVisibility(View.GONE);
                return;
            }
            itemView.setVisibility(View.VISIBLE);

            helperView = itemView.findViewById(R.id.helper_info);
            nameTextView = itemView.findViewById(R.id.helper_name);
            picImageView = itemView.findViewById(R.id.helper_pic);

            // change font
            Typeface font = ResourcesCompat.getFont(context, R.font.raleway);
            nameTextView.setTypeface(font, Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return helpers.size();
    }

    @Override
    public HelpersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_helper, parent, false);
        return new HelpersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Helper helper = helpers.get(position);

        holder.nameTextView.setText(helper.getName());
        Glide.with(context).load(helper.getImageURL()).into(holder.picImageView);
        holder.helperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.helperSelected(helper);
            }
        });

    }
}
