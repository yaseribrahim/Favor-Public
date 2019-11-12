package com.example.android.Favor.ui.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.android.Favor.data.models.Favor;

import java.util.ArrayList;


public class FeedDiffCallback extends DiffUtil.Callback {

    ArrayList<Favor> oldFeed;
    ArrayList<Favor> newFeed;

    public FeedDiffCallback(ArrayList<Favor> oldFeed, ArrayList<Favor> newFeed) {
        this.oldFeed = oldFeed;
        this.newFeed = newFeed;
    }

    @Override
    public int getOldListSize() {
        return oldFeed != null ? oldFeed.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newFeed != null ? newFeed.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFeed.get(oldItemPosition).getHashcode() == newFeed.get(newItemPosition).getHashcode();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFeed.get(oldItemPosition).equals(newFeed.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Favor oldFavor = oldFeed.get(oldItemPosition);
        Favor newFavor = newFeed.get(newItemPosition);

        Bundle diffBundle = new Bundle();

        if (!newFavor.getRequest().equals(oldFavor.getRequest())) {
            diffBundle.putString("KEY_REQUEST", newFavor.getRequest());
        }

        if (!newFavor.getPrice().equals(oldFavor.getPrice())) {
            diffBundle.putString("KEY_PRICE", newFavor.getPrice());
        }

        if (!newFavor.getDistance().equals(oldFavor.getDistance())) {
            diffBundle.putString("KEY_DISTANCE", newFavor.getDistance());
        }

        if (!newFavor.getImageURL().equals(oldFavor.getImageURL())) {
            diffBundle.putString("KEY_IMAGE", newFavor.getPrice());
        }

        if (!newFavor.getTime().equals(oldFavor.getTime())) {
            diffBundle.putLong("KEY_TIME", newFavor.getTime());
        }

        if (!newFavor.getName().equals(oldFavor.getName())) {
            diffBundle.putString("KEY_NAME", newFavor.getName());
        }

        if (diffBundle.size() == 0) return null;

        return diffBundle;
        }
}
