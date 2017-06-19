package com.egkhan.bucketdrops;

import android.app.Application;

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
}
