package com.example.android.Favor.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.Favor.R;
import com.example.android.Favor.data.models.Cache;
import com.example.android.Favor.data.models.Message;
import com.example.android.Favor.data.models.User;

import java.util.ArrayList;


public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private ArrayList<Message> messageList;
    private User user;
    private Cache cache;

    public MessageListAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        cache = Cache.getInstance(context);
//        user = new User(context);


    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {

        Message message = (Message) messageList.get(position);

//        Log.d("senderID", message.getSenderID());

        if (message.getSenderID().equals(cache.getUserID())) {
            Log.d("ViewType", "message sent");

            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            Log.d("ViewType", "message received");
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }





    public class SentMessageHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView, timeTextView, dayTextView;
        private ImageView mapView;


        public SentMessageHolder(View itemView) {
            super(itemView);

            messageTextView = (TextView) itemView.findViewById(R.id.text_message_body);
            timeTextView = (TextView) itemView.findViewById(R.id.text_message_time);
            dayTextView = (TextView) itemView.findViewById(R.id.day);
            mapView = (ImageView) itemView.findViewById(R.id.chat_map);


            Typeface font = ResourcesCompat.getFont(context, R.font.raleway);
            messageTextView.setTypeface(font);
            dayTextView.setTypeface(font);
        }

        void bind(final Message message) {
            dayTextView.setText(message.getDate());
            dayTextView.setVisibility(View.VISIBLE);
            timeTextView.setText(String.valueOf(message.getTime()));
            mapView.setVisibility(View.GONE);

            if(message.getMessage().equals("::map::")) {
                messageTextView.setVisibility(View.GONE);
                Glide.with(context).load(message.getMapsURL()).into(mapView);
                mapView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("geo:<" + message.getLatitude() + ">,<" + message.getLongitude() + ">?q=<" + message
                                        .getLatitude() + ">,<" + message.getLongitude() + ">(" + message.getSenderName()
                                        + "'s " + "Location)"));
                        context.startActivity(intent);
                    }
                });
            } else{
                messageTextView.setText(message.getMessage());
                messageTextView.setVisibility(View.VISIBLE);
            }


        }

        void bind(final Message message, Message prevMessage) {
            dayTextView.setVisibility(View.GONE);
            timeTextView.setVisibility(View.GONE);
            mapView.setVisibility(View.GONE);

            if (!message.getDate().equals(prevMessage.getDate())) {
                dayTextView.setText(message.getDate());
                dayTextView.setVisibility(View.VISIBLE);
            }

            if(!message.getTime().equals(prevMessage.getTime())) {
                timeTextView.setText(String.valueOf(message.getTime()));
                timeTextView.setVisibility(View.VISIBLE);
            }

            if(message.getMessage().equals("::map::")) {
                messageTextView.setVisibility(View.GONE);
                Glide.with(context).load(message.getMapsURL()).into(mapView);
                mapView.setVisibility(View.VISIBLE);

                mapView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + message.getLatitude() + ">,<" + message.getLongitude() + ">?q=<" + message.getLatitude() + ">,<" + message.getLongitude() + ">(" + message.getSenderName() + "'s Location)"));
                        context.startActivity(intent);
                    }
                });
            }else{
                messageTextView.setText(message.getMessage());
                messageTextView.setVisibility(View.VISIBLE);

            }
        }
    }

    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView, timeText, dayText;
        public ImageView mapView;

        public ReceivedMessageHolder (View itemView) {
            super(itemView);

            messageTextView = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            dayText = (TextView) itemView.findViewById(R.id.day);
            mapView = (ImageView) itemView.findViewById(R.id.chat_map);

            // change font
            Typeface font = ResourcesCompat.getFont(context, R.font.raleway);
            messageTextView.setTypeface(font);
            dayText.setTypeface(font);

        }


        void bind(final Message message) {
            dayText.setText(message.getDate());
            timeText.setText(String.valueOf(message.getTime()));
            mapView.setVisibility(View.GONE);

            if(message.getMessage().equals("::map::")) {
                messageTextView.setVisibility(View.GONE);
                Glide.with(context).load(Uri.parse(message.getMapsURL())).into(mapView);
                mapView.setVisibility(View.VISIBLE);
            }else{
                messageTextView.setText(message.getMessage());
                messageTextView.setVisibility(View.VISIBLE);

                mapView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + message.getLatitude() + ">,<" + message.getLongitude() + ">?q=<" + message.getLatitude() + ">,<" + message.getLongitude() + ">(" + message.getSenderName() + "'s Location)"));
                        context.startActivity(intent);
                    }
                });
            }
        }


        // if prevMessage passed, only display date and time if they differ in date
        void bind(final Message message, Message prevMessage) {
            dayText.setVisibility(View.GONE);
            timeText.setVisibility(View.GONE);
            mapView.setVisibility(View.GONE);


            if (!message.getDate().equals(prevMessage.getDate())) {
                dayText.setText(message.getDate());
                dayText.setVisibility(View.VISIBLE);
            }

            if(!message.getTime().equals(prevMessage.getTime())) {
                timeText.setText(String.valueOf(message.getTime()));
                timeText.setVisibility(View.VISIBLE);
            }

            if(message.getMessage().equals("::map::")) {
                messageTextView.setVisibility(View.GONE);
                Glide.with(context).load(Uri.parse(message.getMapsURL())).into(mapView);
                mapView.setVisibility(View.VISIBLE);

                mapView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + message.getLatitude() + ">,<" + message.getLongitude() + ">?q=<" + message.getLatitude() + ">,<" + message.getLongitude() + ">(" + message.getSenderName() + "'s Location)"));
                        context.startActivity(intent);
                    }
                });

            }else{
                messageTextView.setText(message.getMessage());
                messageTextView.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) messageList.get(position);
        Message prevMessage;


            if (holder.getItemViewType() == VIEW_TYPE_MESSAGE_RECEIVED) {

                if (position > 0) {
                    prevMessage = messageList.get(position - 1);
                    ((ReceivedMessageHolder) holder).bind(message, prevMessage);
                }else {
                    ((ReceivedMessageHolder) holder).bind(message);
                }

            } else if (holder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT) {

                if (position > 0) {
                    prevMessage = messageList.get(position - 1);
                    ((SentMessageHolder) holder).bind(message, prevMessage);
                }else {
                    ((SentMessageHolder) holder).bind(message);
            }
        }
    }
}

