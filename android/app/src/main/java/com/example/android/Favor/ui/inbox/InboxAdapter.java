package com.example.android.Favor.ui.inbox;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.Favor.R;
import com.example.android.Favor.data.models.Chat;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private Context context;
    private List<Chat> chatList;
    private InboxPresenter inboxPresenter;


    public InboxAdapter(Context context, List<Chat> chatList, InboxPresenter inboxPresenter) {
        this.context = context;
        this.chatList = chatList;
        this.inboxPresenter = inboxPresenter;
    }


    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_inbox, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(InboxAdapter.ViewHolder holder, int position) {
        final Chat chat = chatList.get(position);

        // show descriptor view on the last post
        holder.descriptorView.setVisibility(position == chatList.size() -1?View.VISIBLE:View.GONE);

        // show unread messages
        holder.unReadTextView.setVisibility(chat.getUnreadMsgCount() > 0? View.VISIBLE:View.GONE);
        holder.unReadTextView.setText(chat.getUnreadMsgCount() + "");

        holder.nameTextView.setText(chat.getParticipantName());
        Glide.with(context).load(chat.getParticipantImageURL()).into(holder.imageView);
        holder.timeTextView.setText(chat.getMessageTime());
        holder.lastMessageTextView.setText(chat.getLastMessage());

        // make last message bold if unread
        if (chat.getUnreadMsgCount() > 0) {
            holder.lastMessageTextView.setTypeface(holder.lastMessageTextView.getTypeface(), Typeface.BOLD);
            holder.lastMessageTextView.setTextColor(context.getResources().getColor(R.color.colorOnPrimary));
        } else {
            holder.lastMessageTextView.setTypeface(holder.lastMessageTextView.getTypeface(), Typeface.NORMAL);
            holder.lastMessageTextView.setTextColor(context.getResources().getColor(R.color.mediumGray));
        }

        holder.chatCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inboxPresenter.onOpenChat(chat.getParticipantID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTextView;
        private ImageView imageView;
        private TextView lastMessageTextView;
        private TextView timeTextView;
        private View descriptorView;
        private CardView chatCardView;
        private TextView unReadTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.participant_name);
            imageView = (ImageView) itemView.findViewById(R.id.message_pic);
            lastMessageTextView = (TextView) itemView.findViewById(R.id.latest_message);
            timeTextView = (TextView) itemView.findViewById(R.id.last_message_time);
            chatCardView = (CardView) itemView.findViewById(R.id.message_card);
            descriptorView = itemView.findViewById(R.id.view_below_last_message);
            unReadTextView = (TextView) itemView.findViewById(R.id.num_unread_messages);

        }
    }




}
