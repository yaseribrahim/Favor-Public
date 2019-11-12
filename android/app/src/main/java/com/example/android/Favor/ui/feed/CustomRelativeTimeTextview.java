package com.example.android.Favor.ui.feed;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;

import com.example.android.Favor.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;

/**
 * custom relative time textview, see https://github.com/curioustechizen/android-ago
 */
public class CustomRelativeTimeTextview extends RelativeTimeTextView {

    public CustomRelativeTimeTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeTimeTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected CharSequence getRelativeTimeDisplayString(long referenceTime, long now) {
        long difference = now - referenceTime;
        CharSequence relativeTime = (difference >= 0 &&  difference<=DateUtils.MINUTE_IN_MILLIS) ?
                getResources().getString(R.string.just_now):
                DateUtils.getRelativeTimeSpanString(
                        referenceTime,
                        now,
                        DateUtils.MINUTE_IN_MILLIS);

        return getResources().getString(R.string.format_relative_time_with_prefix, relativeTime);
    }
}