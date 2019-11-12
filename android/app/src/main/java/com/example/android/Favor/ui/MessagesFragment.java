package com.example.android.Favor.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.Favor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {


    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_chat, container, false);

    }

}
