package com.example.android.Favor.ui.feed;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.Favor.R;
import com.example.android.Favor.data.models.Favor;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context context;
    private List<Favor> favorList;
    private FeedPresenter presenter;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;


    public FeedAdapter(Context context, List<Favor> favorList, FeedPresenter presenter) {
        this.context = context;
        this.favorList = favorList;
        this.presenter = presenter;
    }


    @Override
    public long getItemId(int position) {
        Favor favor = favorList.get(position);
        return favor.getHashcode();
    }


    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favor, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final FeedAdapter.ViewHolder holder, final int position) {
        final Favor favor = favorList.get(position);

        // show descriptor view on the last post
        holder.descriptorView.setVisibility(position == favorList.size() -1?View.VISIBLE:View.GONE);

        // functionality for expanding item on click
        final boolean isExpanded = holder.getLayoutPosition() == mExpandedPosition;
        holder.optionView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.arrowButton.setRotation(isExpanded? 180 : 0);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded) {
            previousExpandedPosition = holder.getAdapterPosition();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandedPosition = isExpanded ? -1: holder.getLayoutPosition();

                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(holder.getLayoutPosition());

            }
        });

        holder.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onMessageBtnClicked(favor.getUserID());
            }
        });

        holder.nameTextView.setText(favor.getName());
//        holder.textUni.setText(favor.getUni());
        holder.favorTextView.setText(favor.getRequest());
        holder.timeTextView.setReferenceTime(favor.getTime());
        holder.priceTextView.setText(context.getString(R.string.price, Currency.getInstance(Locale.getDefault()).getSymbol(), favor.getPrice()));
        holder.distanceTextView.setText(context.getString(R.string.distance,favor.getDistance()));
        Glide.with(context).load(favor.getImageURL()).into(holder.picImageView);

    }

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
//        if (payloads.isEmpty()) {
//            super.onBindViewHolder(holder, position, payloads);
//        } else {
//            Bundle changes = (Bundle) payloads.get(0);
//            Favor favor = favorList.get(position);
//            for (String key: changes.keySet()) {
//                if(key.equals("KEY_NAME")) {
//                    holder.nameTextView.setText(favor.getName());
//                }
//                if(key.equals("KEY_PRICE")) {
//                    holder.priceTextView.setText(context.getString(R.string.price, Currency.getInstance(Locale.getDefault()).getSymbol(), favor.getPrice()));
//                }
//                if(key.equals("KEY_DISTANCE")) {
//                    holder.distanceTextView.setText(context.getString(R.string.distance,favor.getDistance()));
//                }
//                if(key.equals("KEY_TIME")) {
//                    holder.timeTextView.setReferenceTime(favor.getTimeStr());
//                }
//                if(key.equals("KEY_IMAGE")) {
//                    Glide.with(context).load(favor.getImageURL()).into(holder.picImageView);
//                }
//                if(key.equals("KEY_REQUEST")) {
//                    holder.favorTextView.setText(favor.getRequest());
//                }
//
//            }
//        }
//    }

    @Override
    public int getItemCount() {
        return favorList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, favorTextView, priceTextView, distanceTextView;
        private ImageView picImageView, arrowButton;
        private CustomRelativeTimeTextview timeTextView;
        private RelativeLayout optionView;
        private Button messageButton;
        private View descriptorView;

        private ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.favor_name);
            picImageView = itemView.findViewById(R.id.favor_pic);
            timeTextView = itemView.findViewById(R.id.timestamp);
            favorTextView = itemView.findViewById(R.id.favor_text);
            distanceTextView = itemView.findViewById(R.id.favor_distance);
            priceTextView = itemView.findViewById(R.id.favor_price);
            optionView = itemView.findViewById(R.id.favor_options_layout);
            arrowButton = itemView.findViewById(R.id.favor_arrow);
            messageButton = itemView.findViewById(R.id.favor_message);
            descriptorView = itemView.findViewById(R.id.view_below_last_favor);

            // change font
            Typeface font = ResourcesCompat.getFont(context, R.font.raleway);
            nameTextView.setTypeface(font, Typeface.BOLD);
            favorTextView.setTypeface(font);
            timeTextView.setTypeface(font);
            distanceTextView.setTypeface(font);
            priceTextView.setTypeface(font, Typeface.BOLD);


        }
    }
}
