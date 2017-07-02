package com.egkhan.bucketdrops.extras;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.util.List;

/**
 * Created by EgK on 20.06.2017.
 */

public class Util {
    public static void showViews(List<View> views)
    {
        for (View view:views) {
            view.setVisibility(View.VISIBLE);
        }
    } public static void hideViews(List<View> views)
    {
        for (View view:views) {
            view.setVisibility(View.GONE);
        }
    }
public static boolean moreThanJellyBean()
{
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
}

    public static void setBackground(View itemView, Drawable drawable) {
        if (moreThanJellyBean()) {
            itemView.setBackground(drawable);
        }
        else
        {
            itemView.setBackgroundDrawable(drawable);
        }
    }
}
