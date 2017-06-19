package com.egkhan.bucketdrops.extras;

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
}
