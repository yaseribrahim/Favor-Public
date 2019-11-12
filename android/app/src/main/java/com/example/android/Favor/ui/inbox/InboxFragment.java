package com.example.android.Favor.ui.inbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.example.android.Favor.R;
import com.example.android.Favor.data.models.Chat;
import com.example.android.Favor.ui.chat.ChatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InboxFragment extends Fragment implements InboxContract.View {

    private LayoutInflater inflater;
    private ViewGroup container;
    private Context context;
    private InboxPresenter inboxPresenter;
    private RecyclerView recyclerView;
    private InboxAdapter inboxAdapter;
    private List<Chat> inboxList = Collections.synchronizedList(new ArrayList<Chat>());
    private View emptyInbox;

    public InboxFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;
        context = getContext();

        inboxPresenter = InboxPresenter.getInboxPresenter(this, context);

        return inboxPresenter.load();
    }

    @Override
    public void onStart() {
        super.onStart();
        inboxPresenter.viewStarted();
    }


    @Override
    public View loadView() {

        // inflate view
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        // setup recyclerView
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.inbox_recyclerView);
        inboxAdapter = new InboxAdapter(getContext(), inboxList, inboxPresenter);
        recyclerView.setAdapter(inboxAdapter);
        recyclerView.setLayoutManager(llm);

        // setup empty view placeholder
        emptyInbox = (View) rootView.findViewById(R.id.empty_inbox_view);
        emptyInbox.setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public void loadChats(final ArrayList<Chat> arrayList) {

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // on UIThread

            if(!arrayList.isEmpty()) {

                hideEmptyView();

                inboxList.clear();
                inboxList.addAll(arrayList);
                inboxAdapter.notifyDataSetChanged();

            } else {
                showEmptyView();
            }
        } else {
            // not on UIthread

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!arrayList.isEmpty()) {

                        hideEmptyView();

                        inboxList.clear();
                        inboxList.addAll(arrayList);
                        inboxAdapter.notifyDataSetChanged();

                    } else {
                        showEmptyView();
                    }
                }
            });
        }
    }



    @Override
    public void openChat(String userID) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    @Override
    public void createNewChat(final Chat chat) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideEmptyView();
                inboxList.add(chat);
                inboxAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void updateChat(final Chat chat, final int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideEmptyView();
                inboxList.remove(position);
                inboxAdapter.notifyItemRemoved(position);
                inboxList.add(0, chat);
                inboxAdapter.notifyItemInserted(0);
            }
        });
    }

    void showEmptyView(){
        emptyInbox.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    void hideEmptyView(){
        emptyInbox.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


}
