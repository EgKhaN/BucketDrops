package com.egkhan.bucketdrops;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.egkhan.bucketdrops.adapters.Filter;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by EgK on 6/19/2017.
 */

public class AppBucketDrops extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static void saveFilter(Context context,int filterOption) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("filter", filterOption);
        editor.apply();
    }

    public static int loadFilter(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int filterOption = sharedPreferences.getInt("filter", Filter.NONE);
        return filterOption;
    }

    public static void setRalewayRegular(Context context, TextView textView) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/raleway_thin.tff");
        textView.setTypeface(typeface);
    }
    public static void setRalewayRegular(Context context, TextView... textViews) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/raleway_thin.tff");
        for ( TextView textView: textViews) {
            textView.setTypeface(typeface);
        }

    }
}
